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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.PropertyFilter;
import org.springside.modules.utils.web.struts2.Struts2Utils;

import cn.common.lib.springside.util.ParamPropertyUtils;

import com.gm.wine.contant.Global;
import com.gm.wine.core.ProductManager;
import com.gm.wine.entity.Product;
import com.gm.wine.vo.GlobalMessage;
import com.gm.wine.vo.ProductList;
import com.gm.wine.vo.ProductVO;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * 前台产品管理
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
    public class ProductAction extends ActionSupport
    {

    /**
     * serialVersionUID long
     */
    private static final long serialVersionUID = 1L;

    private String            data;

    @Autowired
    private ProductManager    productManager;

    @Override
    public String execute() throws Exception
    {
        HttpServletRequest request = Struts2Utils.getRequest();
        Page<Product> page = new Page<Product>(Integer.parseInt(request
                .getParameter("pageSize")));
        page.setPageNo(Integer.parseInt(request.getParameter("pageNo")));
        ProductList nl = new ProductList();

        try
        {
            List<PropertyFilter> filters = PropertyFilter
            .buildFromHttpRequest(request);
            ParamPropertyUtils.replacePropertyRule(filters);
            filters.add(new PropertyFilter("EQB_status", "true"));

            page.setOrderBy("id");
            page.setOrder(Page.DESC);

            page = productManager.search(page, filters);
            nl.setPageSize(page.getPageSize());
            nl.setTotalSize(page.getTotalCount());
            nl.setProductList(this.convertToListVO(page.getResult()));
            nl.setErrorCode(GlobalMessage.SUCCESS_CODE);
            nl.setErrorMessage(GlobalMessage.SUCCESS_MESSAGE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            nl.setErrorCode(GlobalMessage.ERROR_CODE);
            nl.setErrorMessage(GlobalMessage.ERROR_MESSAGE);
        }

        data = new Gson().toJson(nl);
        return SUCCESS;
    }

    public String detail() throws Exception
    {
        HttpServletRequest request = Struts2Utils.getRequest();
        ProductVO nv = null;

        try
        {


            nv = convertToVO(productManager.get(Long.parseLong(request
                    .getParameter("id"))));

            nv.setErrorCode(GlobalMessage.SUCCESS_CODE);
            nv.setErrorMessage(GlobalMessage.SUCCESS_MESSAGE);
        }
        catch (Exception e)
        {
            nv = new ProductVO();
            e.printStackTrace();
            nv.setErrorCode(GlobalMessage.ERROR_CODE);
            nv.setErrorMessage(GlobalMessage.ERROR_MESSAGE);
        }

        data = new Gson().toJson(nv);
        return SUCCESS;
    }

    private ProductVO convertToVO(Product p)
    {
        ProductVO n = new ProductVO();
        n.setId(p.getId());
        n.setName(p.getName());
        n.setRemark(p.getRemark());
        n.setPubdate(p.getPubdate());
        n.setPrice(p.getPrice());
        n.setPicurl(Global.appurl + Global.picpath + p.getPicurl());
        return n;
    }

    private List<ProductVO> convertToListVO(List<Product> productList)
    {
        List<ProductVO> vos = Lists.newArrayList();
        if (productList != null && !productList.isEmpty())
        {
            for (Product v : productList)
            {
                v.setRemark(null);
                vos.add(this.convertToVO(v));
            }
        }
        return vos;
    }

    @JSON(name="data")
    public String getData()
    {
        return data;
    }


    }
