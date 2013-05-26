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
package com.gm.wine.common;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import com.google.gson.Gson;

/**
 * 
 * 和http server进行http通讯工具工具类
 * 
 * @author qingang
 * @version 1.0
 * @since 2011-11-25
 */
public class HttpClientUtil
{


    /**
     * 向一个url提交post请求，
     * 
     * @param url
     * @param paramsStr
     *            参数串
     * @param method
     * @return String 请求后返回的串
     */
    public static String post(String url, String paramsStr, String method)
    {

        HttpClient client = new HttpClient();
        HttpMethod httpMethod = new PostMethod(url);

        if (method.equalsIgnoreCase("get"))
        {
            httpMethod = new GetMethod(url);
        }
        httpMethod.setQueryString(paramsStr);
        try
        {
            int returnCode = client.executeMethod(httpMethod);
            if (returnCode == HttpStatus.SC_OK)
            {
                return httpMethod.getResponseBodyAsString();
            }
            else
                if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED)
                {
                    System.err
                    .println("The Post method is not implemented by this URI");
                }
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
        finally
        {
            httpMethod.releaseConnection();
        }
        return "";
    }
    public static void main(String[] args)
    {
        try
        {
            String ss = HttpClientUtil.post(
                    "http://localhost:8088/wine-webapp/news.action", null,
            "get");
            Gson gson = new Gson();

            System.out.println("--------"
                    + gson.fromJson(gson.fromJson(ss, Result.class).getData(),
                            NewsVO.class).getTitle());
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
