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
package com.gm.wine.web.manage;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.PropertyFilter;
import org.springside.modules.security.springsecurity.SpringSecurityUtils;
import org.springside.modules.utils.web.struts2.Struts2Utils;

import cn.common.lib.springside.util.ParamPropertyUtils;
import cn.common.lib.springside.web.CrudActionSupport;

import com.gm.wine.contant.Global;
import com.gm.wine.core.NoticeManager;
import com.gm.wine.core.UserManager;
import com.gm.wine.entity.Notice;
import com.gm.wine.entity.Product;
import com.gm.wine.util.CommonUtils;

/**
 * 
 * 留言公告action
 * 
 * @author qingang
 * @version 1.0
 * @since 2012-7-28
 */
@Namespace("/backend")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "notice.action", type = "redirect") })
public class NoticeAction extends CrudActionSupport<Notice>
{

    /**
     * serialVersionUID long
     */
    private static final long serialVersionUID = 1L;

    private Long              id;

    private Notice            entity;

    private Product           product;

    private List<Long>        ids;

    private Page<Notice>      page             = new Page<Notice>(10);

    @Autowired
    private NoticeManager     noticeManager;


    @Autowired
    private UserManager       userManager;

    private Long              parentid;                                // 主贴id


    @Override
    public String input() throws Exception
    {
        return INPUT;
    }

    @Override
    public String delete() throws Exception
    {
        try
        {
            noticeManager.delete(id);
            this.addActionMessage(Global.DELETE_SUCCESS);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.addActionMessage(Global.DELETE_LOSE);
        }
        return RELOAD;
    }

    /**
     * 
     * 删除回复
     * 
     * @since 2012-12-1
     * @author qingang
     * @return
     * @throws Exception
     */
    public String deleteChild() throws Exception
    {
        try
        {
            noticeManager.delete(id);
            Struts2Utils.renderText("删除成功");
        }
        catch (Exception e)
        {
            Struts2Utils.renderText("删除失败");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * 批量删除
     * 
     * @since 2012-7-29
     * @author qingang
     * @return
     * @throws Exception
     */
    public String batchDelete() throws Exception
    {

        try
        {
            noticeManager.batchDelete(ids);
            this.addActionMessage(Global.DELETE_SUCCESS);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.addActionMessage(Global.DELETE_LOSE);
        }
        return RELOAD;
    }

    @Override
    public String list() throws Exception
    {
        try
        {
            HttpServletRequest request = Struts2Utils.getRequest();
            List<PropertyFilter> filters = PropertyFilter
            .buildFromHttpRequest(request);
            ParamPropertyUtils.replacePropertyRule(filters);
            filters.add(new PropertyFilter("EQB_topic", "true"));
            if (!page.isOrderBySetted())
            {
                page.setOrderBy("id");
                page.setOrder(Page.DESC);
            }
            page = noticeManager.search(page, filters);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return SUCCESS;
    }

    @Override
    protected void prepareModel() throws Exception
    {
        if (id != null)
        {
            entity = noticeManager.get(id);
        }
        else
        {
            entity = new Notice();
        }

    }

    @Override
    public String save() throws Exception
    {
        try
        {
            if (id == null)
            {
                Notice p = noticeManager.get(parentid);
                p.setTanswer(true);
                noticeManager.save(p);
                entity.setAnswer(true);
                entity.setTanswer(true);
                entity.setParent(p);
                entity.setCreatedate(new Date());
                entity.setUser(userManager
                        .getUserByUsername(SpringSecurityUtils
                                .getCurrentUserName()));
                entity
                .setContent(CommonUtils.escapeHTMLTags(entity
                        .getContent()));
            }
            noticeManager.save(entity);
            this.addActionMessage(Global.SAVE_SUCCESS);
        }
        catch (Exception e)
        {
            this.addActionMessage(Global.SAVE_LOSE);
            e.printStackTrace();
        }
        return RELOAD;
    }





    @Override
    public Notice getModel()
    {
        return entity;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Notice getEntity()
    {
        return entity;
    }

    public void setEntity(Notice entity)
    {
        this.entity = entity;
    }

    public List<Long> getIds()
    {
        return ids;
    }

    public void setIds(List<Long> ids)
    {
        this.ids = ids;
    }

    public Page<Notice> getPage()
    {
        return page;
    }

    public void setPage(Page<Notice> page)
    {
        this.page = page;
    }


    public Product getProduct()
    {
        return product;
    }

    public void setProduct(Product product)
    {
        this.product = product;
    }

    public Long getParentid()
    {
        return parentid;
    }

    public void setParentid(Long parentid)
    {
        this.parentid = parentid;
    }

}
