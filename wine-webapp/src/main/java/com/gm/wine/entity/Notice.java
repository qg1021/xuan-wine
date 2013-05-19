package com.gm.wine.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.google.common.collect.Lists;

/**
 * 
 * 留言公告
 * 
 * @author qingang
 * @version 1.0
 * @since 2012-7-24
 */
@Entity
@Table(name = "t_notice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Notice extends BaseEntity
{

    /**
     * serialVersionUID long
     */
    private static final long serialVersionUID = 1L;

    private String            title;                                  // 留言标题

    private String            content;                                // 留言内容



    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "user_id")
    private User              user;                                   // 留言人


    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @OrderBy(value = "id")
    private List<Notice>     childs           = Lists.newArrayList();

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "parent_id")
    private Notice           parent;                                 // 主询价


    private boolean           topic;                                  // 是否为主贴

    private boolean           answer;                                 // 是否主回复

    private boolean           tanswer;                                // 主题是否已回复

    private Date              createdate;

    public boolean isAnswer()
    {
        return answer;
    }

    public void setAnswer(boolean answer)
    {
        this.answer = answer;
    }

    public boolean isTanswer()
    {
        return tanswer;
    }

    public void setTanswer(boolean tanswer)
    {
        this.tanswer = tanswer;
    }

    private long              uid;

    public long getUid()
    {
        return uid;
    }

    public void setUid(long uid)
    {
        this.uid = uid;
    }

    public boolean isTopic()
    {
        return topic;
    }

    public void setTopic(boolean topic)
    {
        this.topic = topic;
    }



    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }





    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
        this.uid = user.getId();
    }

    public Date getCreatedate()
    {
        return createdate;
    }

    public void setCreatedate(Date createdate)
    {
        this.createdate = createdate;
    }

    public Notice getParent()
    {
        return parent;
    }

    public void setParent(Notice parent)
    {
        this.parent = parent;
    }

    public List<Notice> getChilds()
    {
        return childs;
    }

    public void setChilds(List<Notice> childs)
    {
        this.childs = childs;
    }


    @Transient
    public String getAnswerName()
    {
        return tanswer ? "已回复" : "未回复";
    }

}