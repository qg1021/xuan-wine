package com.gm.wine.account;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.gm.wine.core.AccountManager;
import com.gm.wine.entity.Role;
import com.gm.wine.entity.User;


public class UserSuccessHandler extends SimpleUrlAuthenticationSuccessHandler
{
    protected final Log    logger = LogFactory.getLog(this.getClass());

    @Autowired
    private AccountManager accountManager;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
    throws ServletException, IOException
    {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = accountManager.findUserByLoginName(userDetails
                .getUsername());
        user.setCreateDate(new Date());
        accountManager.saveUser(user);
        request.getSession().setAttribute("loginuser", user);
        StringBuffer sb = new StringBuffer();
        if (user.getRoleList() != null)
        {
            Role role = user.getRoleList().get(0);
            sb.append("身份：").append(role.getName());
        }

        getRedirectStrategy().sendRedirect(request, response,
                this.getDefaultTargetUrl());

    }

}
