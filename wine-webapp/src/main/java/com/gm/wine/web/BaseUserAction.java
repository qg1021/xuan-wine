package com.gm.wine.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.utils.web.struts2.Struts2Utils;

import cn.common.lib.springside.web.CrudActionSupport;
import cn.common.lib.util.web.ParamUtils;

import com.gm.wine.core.RoleManager;
import com.gm.wine.core.UserManager;
import com.gm.wine.entity.User;

@SuppressWarnings("serial")
public class BaseUserAction extends CrudActionSupport<User>
{
    @Autowired
    protected UserManager userManager;

    @Autowired
    protected RoleManager roleManager;

    // -- 页面属性 --//
    protected Long        id;

    protected User        entity;



    // -- ModelDriven 与 Preparable函数 --//
    public void setId(Long id)
    {
        this.id = id;
    }

    public User getModel()
    {
        return entity;
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
        }
    }

    @Override
    public String list() throws Exception
    {
        return "";
    }

    @Override
    public String save() throws Exception
    {
        return "";
    }

    @Override
    public String delete() throws Exception
    {
        return "";
    }

    @Override
    public String input() throws Exception
    {
        return "";
    }

    /**
     * 支持使用Jquery.validate Ajax检验用户名是否重复.
     */
    public String checkUserName()
    {
        HttpServletRequest request = ServletActionContext.getRequest();
        String newUserName = ParamUtils.getParameter(request, "loginName");
        String oldUserName = ParamUtils.getParameter(request, "oldUserName");

        if (userManager.isLoginNameExists(newUserName, oldUserName))
        {
            Struts2Utils.renderText("true");
        }
        else
        {
            Struts2Utils.renderText("false");
        }
        // 因为直接输出内容而不经过jsp,因此返回null.
        return null;
    }

    public User getEntity()
    {
        return entity;
    }

    public void setEntity(User entity)
    {
        this.entity = entity;
    }

}
