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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springside.modules.utils.web.struts2.Struts2Utils;

import com.gm.wine.core.UserManager;
import com.gm.wine.entity.User;
import com.gm.wine.vo.GlobalMessage;
import com.gm.wine.vo.UserVO;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * 用户登录action
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
    public class LoginvalidateAction extends ActionSupport
    {

    /**
     * serialVersionUID long
     */
    private static final long serialVersionUID = 1L;

    private String            data;

    @Autowired
    private UserManager             userManager;

    @Autowired
    @Qualifier("org.springframework.security.authenticationManager")
    protected AuthenticationManager authenticationManager;

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest request = Struts2Utils.getRequest();
        String loginName = request.getParameter("loginName");
        String password = request.getParameter("password");
        UserVO u = new UserVO();

        try
        {
            User user = userManager.getUserByUsername(loginName);
            if (user != null)
            {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        loginName, password);
                token.setDetails(new WebAuthenticationDetails(request));
                Authentication authenticatedUser = authenticationManager
                .authenticate(token);

                SecurityContextHolder.getContext().setAuthentication(
                        authenticatedUser);
                request
                .getSession()
                .setAttribute(
                        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                        SecurityContextHolder.getContext());
                u.setErrorCode(GlobalMessage.SUCCESS_CODE);
                u.setErrorMessage("登录成功");
                u.setId(user.getId());
                u.setLoginName(user.getLoginName());
                u.setName(user.getName());
            }
            else
            {
                u.setErrorCode(GlobalMessage.ERROR_CODE);
                u.setErrorMessage("用户不存在");
            }
        }
        catch (AuthenticationException e)
        {
            e.printStackTrace();
            u.setErrorCode(GlobalMessage.ERROR_CODE);
            u.setErrorMessage("密码错误");
        }

        data = new Gson().toJson(u);
        return SUCCESS;
    }





    @JSON(name="data")
    public String getData()
    {
        return data;
    }


    }
