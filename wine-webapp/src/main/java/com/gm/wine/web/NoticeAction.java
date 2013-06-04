//-------------------------------------------------------------------------
// Copyright (c) 2000-2010 Digital. All Rights Reserved.
//
// This software is the confidential and proprietary information of
// Digital
//
// Original author: qingang
//
//-------------------------------------------------------------------------
// LOOSOFT MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
// THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
// TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
// PARTICULAR PURPOSE, OR NON-INFRINGEMENT. UFINITY SHALL NOT BE
// LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING,
// MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
//
// THIS SOFTWARE IS NOT DESIGNED OR INTENDED FOR USE OR RESALE AS ON-LINE
// CONTROL EQUIPMENT IN HAZARDOUS ENVIRONMENTS REQUIRING FAIL-SAFE
// PERFORMANCE, SUCH AS IN THE OPERATION OF NUCLEAR FACILITIES, AIRCRAFT
// NAVIGATION OR COMMUNICATION SYSTEMS, AIR TRAFFIC CONTROL, DIRECT LIFE
// SUPPORT MACHINES, OR WEAPONS SYSTEMS, IN WHICH THE FAILURE OF THE
// SOFTWARE COULD LEAD DIRECTLY TO DEATH, PERSONAL INJURY, OR SEVERE
// PHYSICAL OR ENVIRONMENTAL DAMAGE ("HIGH RISK ACTIVITIES"). UFINITY
// SPECIFICALLY DISCLAIMS ANY EXPRESS OR IMPLIED WARRANTY OF FITNESS FOR
// HIGH RISK ACTIVITIES.
//-------------------------------------------------------------------------
package com.gm.wine.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.PropertyFilter;
import org.springside.modules.utils.web.struts2.Struts2Utils;

import cn.common.lib.springside.util.ParamPropertyUtils;

import com.gm.wine.core.NoticeManager;
import com.gm.wine.entity.Notice;
import com.gm.wine.entity.User;
import com.gm.wine.vo.GlobalMessage;
import com.gm.wine.vo.NoticeList;
import com.gm.wine.vo.NoticeVO;
import com.gm.wine.vo.UserVO;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * 留言公告管理
 * 
 * @author qingang
 * @version 1.0
 * @since 2012-7-28
 */
@ParentPackage("json-default")
@Namespace("/")
@Results( {
    @Result(name = "success", type = "json", params = { "contentType",
    "text/html" }),
    @Result(name = "error", type = "json", params = { "contentType",
    "text/html" }) })
    public class NoticeAction extends ActionSupport
    {

    /**
     * serialVersionUID long
     */
    private static final long serialVersionUID = 1L;

    private String            data;

    @Autowired
    private NoticeManager     noticeManager;

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest request = Struts2Utils.getRequest();
        Page<Notice> page = new Page<Notice>(Integer.parseInt(request
                .getParameter("pageSize")));
        page.setPageNo(Integer.parseInt(request.getParameter("pageNo")));
        NoticeList nl = new NoticeList();

        try
        {
            List<PropertyFilter> filters = PropertyFilter
            .buildFromHttpRequest(request);
            ParamPropertyUtils.replacePropertyRule(filters);
            filters.add(new PropertyFilter("EQB_topic", "true"));


            page.setOrderBy("id");
            page.setOrder(Page.DESC);

            page = noticeManager.search(page, filters);
            nl.setPageSize(page.getPageSize());
            nl.setTotalSize(page.getTotalCount());
            nl.setNoticeList(this.convertToListVO(page.getResult()));
            nl.setErrorCode(GlobalMessage.SUCCESS_CODE);
            nl.setErrorMessage(GlobalMessage.SUCCESS_MESSAGE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            nl.setErrorCode(GlobalMessage.ERROR_CODE);
            nl.setErrorMessage(GlobalMessage.ERROR_MESSAGE);
        }

        data = new Gson().toJson(nl);
        return SUCCESS;
    }

    public String detail() throws Exception
    {
        HttpServletRequest request = Struts2Utils.getRequest();
        NoticeVO nv = null;

        try
        {


            nv = convertToVO(noticeManager.get(Long.parseLong(request
                    .getParameter("id"))));

            nv.setErrorCode(GlobalMessage.SUCCESS_CODE);
            nv.setErrorMessage(GlobalMessage.SUCCESS_MESSAGE);
        }
        catch (Exception e)
        {
            nv = new NoticeVO();
            e.printStackTrace();
            nv.setErrorCode(GlobalMessage.ERROR_CODE);
            nv.setErrorMessage(GlobalMessage.ERROR_MESSAGE);
        }

        data = new Gson().toJson(nv);
        return SUCCESS;
    }

    private NoticeVO convertToVO(Notice p)
    {
        NoticeVO n = new NoticeVO();
        n.setId(p.getId());
        n.setTitle(p.getTitle());
        n.setContent(p.getContent());
        n.setCreatedate(p.getCreatedate());
        n.setReplyNum(0);
        if (p.getChilds() != null && !p.getChilds().isEmpty())
        {
            n.setChilds(convertToListVO(p.getChilds()));
            n.setReplyNum(p.getChilds().size());
        }
        User v = p.getUser();
        n.setUser(new UserVO(v.getId(), v.getLoginName(), v.getName()));
        return n;
    }

    private List<NoticeVO> convertToListVO(List<Notice> noticeList)
    {
        List<NoticeVO> vos = Lists.newArrayList();
        if (noticeList != null && !noticeList.isEmpty())
        {
            for (Notice v : noticeList)
            {
                vos.add(this.convertToVO(v));
            }
        }
        return vos;
    }

    @JSON(name="data")
    public String getData()
    {
        return data;
    }


    }
