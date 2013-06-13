package com.gm.wine.vo;


/**
 * 用户
 */
public class UserVO extends BaseVO {

    /**
     * serialVersionUID long
     */
    private static final long serialVersionUID = 1L;

    private long              id;
    private String loginName;
    private String name;
    private String password;

    private boolean           rememberMe;

    public boolean isRememberMe()
    {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe)
    {
        this.rememberMe = rememberMe;
    }

    public UserVO()
    {
    }

    public UserVO(long id, String loginName, String name)
    {
        this.id = id;
        this.loginName = loginName;
        this.name = name;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}
