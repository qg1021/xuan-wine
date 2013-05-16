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
import org.springside.modules.orm.Page;
import org.springside.modules.orm.PropertyFilter;
import org.springside.modules.security.springsecurity.SpringSecurityUtils;
import org.springside.modules.utils.web.struts2.Struts2Utils;

import cn.common.lib.springside.util.ParamPropertyUtils;
import cn.common.lib.springside.web.CrudActionSupport;

import com.gm.wine.contant.Global;
import com.gm.wine.entity.Role;
import com.gm.wine.entity.User;
import com.gm.wine.web.BaseUserAction;
import com.google.common.collect.Lists;

/**
 * 
 * 用户管理action
 * 
 * @author qingang
 * @version 1.0
 * @since 2012-7-28
 */
@Namespace("/backend")
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "user.action", type = "redirect") })
public class UserAction extends BaseUserAction
{

    /**
     * serialVersionUID long
     */
    private static final long serialVersionUID = 1L;

    private Long              id;

    private Page<User>        page             = new Page<User>(10);

    private List<Long>        ids;

    @Override
    public String input() throws Exception
    {
        return INPUT;
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
            filters.add(new PropertyFilter("EQS_roleName", Role.ROLE_TYPE_0));
            if (!page.isOrderBySetted())
            {
                page.setOrderBy("id");
                page.setOrder(Page.DESC);
            }
            page = userManager.search(page, filters);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return SUCCESS;
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
            userManager.batchDelete(ids);
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
     * 批量加锁
     * 
     * @since 2012-7-29
     * @author qingang
     * @return
     * @throws Exception
     */
    public String batchLocked() throws Exception
    {

        try
        {
            userManager.locked(ids);
            this.addActionMessage(Global.LOCK_SUCCESS);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.addActionMessage(Global.LOCK_LOSE);
        }
        return RELOAD;
    }

    /**
     * 
     * 批量解锁
     * 
     * @since 2012-7-29
     * @author qingang
     * @return
     * @throws Exception
     */
    public String batchClear() throws Exception
    {

        try
        {
            userManager.clear(ids);
            this.addActionMessage(Global.CLEAR_SUCCESS);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.addActionMessage(Global.CLEAR_LOSE);
        }
        return RELOAD;
    }

    @Override
    protected void prepareModel() throws Exception
    {
        if (id != null)
        {
            entity = userManager.get(id);
        }
        else
        {
            entity = new User();
            List<Role> rlist = Lists.newArrayList();
            rlist.add(roleManager.findByName(Role.ROLE_TYPE_0));
            entity.setRoleList(rlist);
            entity.setRoleName(Role.ROLE_TYPE_0);
            entity.setCreateDate(new Date());
        }

    }

    @Override
    public String save() throws Exception
    {
        try
        {
            userManager.save(entity);
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
    public String delete() throws Exception
    {
        try
        {
            userManager.delete(id);
            this.addActionMessage(Global.DELETE_SUCCESS);
        }
        catch (Exception e)
        {
            this.addActionMessage(Global.DELETE_LOSE);
            e.printStackTrace();
        }
        return RELOAD;
    }

    /**
     * 
     * 初始化密码
     * 
     * @since 2012-9-4
     * @author qingang
     * @return
     * @throws Exception
     */
    public String deaultUserPassword() throws Exception
    {

        try
        {
            entity = userManager.get(id);
            entity.setPassword(User.DEFAULT_PASSWORD);
            userManager.save(entity);
            this.addActionMessage("密码初始化成功");
        }
        catch (Exception e)
        {
            this.addActionMessage("密码初始化失败");
            e.printStackTrace();
        }
        return RELOAD;
    }

    public String savePass() throws Exception
    {
        try
        {
            String password = Struts2Utils.getParameter("password");
            entity = userManager.get(id);
            entity.setPassword(password);
            userManager.save(entity);
            this.addActionMessage(Global.MODIFY_SUCCESS);

        }
        catch (Exception e)
        {
            this.addActionMessage(Global.MODIFY_LOSE);
            e.printStackTrace();
        }
        return "pass";
    }

    /**
     * 
     * 修改密码初始化
     * 
     * @since 2012-9-6
     * @author qingang
     * @return
     * @throws Exception
     */
    public String view() throws Exception
    {
        entity = userManager.getUserByUsername(SpringSecurityUtils
                .getCurrentUserName());
        return "pass";
    }

    @Override
    public User getModel()
    {
        return entity;
    }

    public Long getId()
    {
        return id;
    }

    @Override
    public void setId(Long id)
    {
        this.id = id;
    }

    public List<Long> getIds()
    {
        return ids;
    }

    public void setIds(List<Long> ids)
    {
        this.ids = ids;
    }

    public Page<User> getPage()
    {
        return page;
    }

    public void setPage(Page<User> page)
    {
        this.page = page;
    }

}
