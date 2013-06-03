package com.gm.wine.vo;

import java.util.Date;

/**
 * 
 * 产品
 * 
 * @author qingang
 * @version 1.0
 * @since 2012-7-24
 */
public class ProductVO extends BaseVO {

    /**
     * serialVersionUID long
     */
    private static final long serialVersionUID = 1L;

    private Long              id;

    private String name; // 名称

    private String remark; // 简介

    private String price; // 价格

    private Date pubdate; // 发布时间

    private String picurl; // 图片路径

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getPicurl()
    {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Date getPubdate() {
        return pubdate;
    }

    public void setPubdate(Date pubdate) {
        this.pubdate = pubdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}