package com.gm.wine.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.common.lib.springside.orm.BaseDao;
import cn.common.lib.springside.service.EntityManager;

import com.gm.wine.api.UserDao;
import com.gm.wine.entity.User;


/**
 * 用户实体的管理类.
 */
// Spring Bean的标识.
@Component
// 默认将类中的所有函数纳入事务管理.
@Transactional
public class UserManager extends EntityManager<User, Long>
{

    @Autowired
    private UserDao            userDao;

    public static final String AREA_TYPE = "examArea";

    public static final String SITE_TYPE = "examSite";

    /**
     * 
     * 根据用户名获得用户
     * 
     * @since 2012-1-11
     * @author fangyong
     * @param username
     * @return
     */
    public User getUserByUsername(String username) throws Exception
    {
        return userDao.findUniqueBy("loginName", username);
    }

    /**
     * 
     * 判断用户名是否存在
     * 
     * @since 2012-3-2
     * @author qingang
     * @param newName
     * @param oldName
     * @return
     */
    @Transactional(readOnly = true)
    public boolean isLoginNameExists(String newName, String oldName)
    {
        return userDao.isPropertyUnique("loginName", newName, oldName);
    }

    /**
     * 
     * 修改用户密码
     * 
     * @since 2012-1-11
     * @author fangyong
     * @param user
     */
    public void updatePassword(User user, String password) throws Exception
    {
        user.setPassword(password);
        super.save(user);
    }

    /**
     * 
     * 批量删除
     * 
     * @since 2012-7-26
     * @author qingang
     * @param ids
     */
    public void batchDelete(List<Long> ids)
    {
        userDao.batchDelete(ids);
    }

    /**
     * 
     * 批量锁定用户
     * 
     * @since 2012-11-28
     * @author qingang
     * @param ids
     */
    public void locked(List<Long> ids)
    {
        userDao.batchOperate(User.LOCK_USER, ids);
    }

    /**
     * 
     * 批量用户解锁
     * 
     * @since 2012-11-28
     * @author qingang
     * @param ids
     */
    public void clear(List<Long> ids)
    {
        userDao.batchOperate(User.PUBLIC_USER, ids);
    }

    @Override
    protected BaseDao<User, Long> getEntityDao()
    {
        return userDao;
    }
}
