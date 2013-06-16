package com.gm.wine.web.manage;

import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.gm.wine.core.NoticeManager;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * 网站后台首页
 * 
 * @author qingang
 * @version 1.0
 * @since 2012-7-21
 */
@Namespace("/backend")
public class IndexAction extends ActionSupport
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private NoticeManager     noticeManager;

    private long              noReplyNum;

    @Override
    public String execute() throws Exception
    {
        noReplyNum = noticeManager.countNoReplyNum();
        return "index";
    }

    public long getNoReplyNum()
    {
        return noReplyNum;
    }

    public void setNoReplyNum(long noReplyNum)
    {
        this.noReplyNum = noReplyNum;
    }

}
