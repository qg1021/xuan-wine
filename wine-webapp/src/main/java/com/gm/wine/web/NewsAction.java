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

import com.gm.wine.core.NewsManager;
import com.gm.wine.entity.News;
import com.gm.wine.vo.GlobalMessage;
import com.gm.wine.vo.NewsList;
import com.gm.wine.vo.NewsVO;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * 前台新闻焦点、热点资讯action
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
    public class NewsAction extends ActionSupport
    {

    /**
     * serialVersionUID long
     */
    private static final long serialVersionUID = 1L;

    private String            data;

    @Autowired
    private NewsManager       newsManager;

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest request = Struts2Utils.getRequest();
        Page<News> page = new Page<News>(Integer.parseInt(request
                .getParameter("pageSize")));
        page.setPageNo(Integer.parseInt(request.getParameter("pageNo")));
        NewsList nl = new NewsList();

        try
        {
            List<PropertyFilter> filters = PropertyFilter
            .buildFromHttpRequest(request);
            ParamPropertyUtils.replacePropertyRule(filters);
            filters.add(new PropertyFilter("EQB_ispublish", "true"));

            page.setOrderBy("id");
            page.setOrder(Page.DESC);

            page = newsManager.search(page, filters);
            nl.setPageSize(page.getPageSize());
            nl.setTotalSize(page.getTotalCount());
            nl.setNewsList(this.convertToListVO(page.getResult()));
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
        NewsVO nv = null;

        try
        {


            nv = convertToVO(newsManager.get(Long.parseLong(request
                    .getParameter("id"))));

            nv.setErrorCode(GlobalMessage.SUCCESS_CODE);
            nv.setErrorMessage(GlobalMessage.SUCCESS_MESSAGE);
        }
        catch (Exception e)
        {
            nv = new NewsVO();
            e.printStackTrace();
            nv.setErrorCode(GlobalMessage.ERROR_CODE);
            nv.setErrorMessage(GlobalMessage.ERROR_MESSAGE);
        }

        data = new Gson().toJson(nv);
        return SUCCESS;
    }

    private NewsVO convertToVO(News news)
    {
        NewsVO n = new NewsVO();
        n.setId(news.getId());
        n.setTitle(news.getTitle());
        n.setDesciption(news.getDesciption());
        n.setPublishdate(news.getPublishdate());
        n.setSource(news.getSource());
        return n;
    }

    private List<NewsVO> convertToListVO(List<News> newsList)
    {
        List<NewsVO> vos = Lists.newArrayList();
        if (newsList != null && !newsList.isEmpty())
        {
            for (News v : newsList)
            {
                v.setDesciption(null);
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
