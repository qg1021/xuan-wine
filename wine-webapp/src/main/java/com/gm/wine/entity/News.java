package com.gm.wine.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.gm.wine.contant.Global;

/**
 * 
 * 新闻焦点、热点资讯
 * 
 * @author qingang
 * @version 1.0
 * @since 2012-7-24
 */
@Entity
@Table(name = "t_news")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class News extends BaseEntity
{

    /**
     * serialVersionUID long
     */
    private static final long serialVersionUID = 1L;

    private String            title;                // 标题

    private String            source;               // 来源

    private Date              createdate;           // 创建时间

    private Boolean           ispublish;            // 是否发布

    private Date              publishdate;          // 发布时间

    private String            desciption;

    public String getDesciption()
    {
        return desciption;
    }

    public void setDesciption(String desciption)
    {
        this.desciption = desciption;
    }

    private String          link;      // 链接

    private long            viewcount; // 浏览次数

    private int             type;      // 0：新闻焦点 1：热点资讯

    public final static int TYPE_0 = 0;

    public final static int TYPE_1 = 1;

    public long getViewcount()
    {
        return viewcount;
    }

    public void setViewcount(long viewcount)
    {
        this.viewcount = viewcount;
    }

    @Transient
    public String getStatusName()
    {
        if (ispublish)
        {
            return "已发布";
        }
        return "未发布";
    }

    @Transient
    public String getMenuName()
    {
        return Global.newsTypeList.get(type).getLabel();
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Date getCreatedate()
    {
        return createdate;
    }

    public void setCreatedate(Date createdate)
    {
        this.createdate = createdate;
    }

    public Boolean getIspublish()
    {
        return ispublish;
    }

    public void setIspublish(Boolean ispublish)
    {
        this.ispublish = ispublish;
    }

    public Date getPublishdate()
    {
        return publishdate;
    }

    public void setPublishdate(Date publishdate)
    {
        this.publishdate = publishdate;
    }

}