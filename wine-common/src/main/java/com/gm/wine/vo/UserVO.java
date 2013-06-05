package com.gm.wine.vo;

import java.util.Date;

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
    private Date createDate; // 创建时间

    private String roleName; // 角色名称

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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}
