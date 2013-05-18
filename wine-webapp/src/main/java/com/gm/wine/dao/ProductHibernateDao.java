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
package com.gm.wine.dao;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springside.modules.orm.hibernate.HibernateDao;

import com.gm.wine.api.ProductDao;
import com.gm.wine.entity.Product;

/**
 * 
 * 操作用户商品数据访问类
 * 
 * @author qingang
 * @version 1.0
 * @since 2011-11-8
 */
@Component
public class ProductHibernateDao extends HibernateDao<Product, Long> implements
		ProductDao {
	/**
	 * {@inheritDoc}
	 * 
	 * @since 2012-11-25
	 * @see com.gm.machine.api.AdvertDao#batchDelete(java.util.List)
	 */
	@Override
	public void batchDelete(List<Long> ids) {
		String hql = "delete from Product where id in(:ids)";
		Map<String, List<Long>> values = Collections.singletonMap("ids", ids);
		super.batchExecute(hql, values);

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 2012-11-25
	 * @see com.gm.machine.api.AdvertDao#operate(java.util.List, boolean)
	 */
	@Override
	public void operate(List<Long> ids, boolean status) {
		String hql = "update Product set status = :status,pubdate= :pubdate where id in(:ids)";
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("status", status);
		values.put("pubdate", new Date());
		values.put("ids", ids);
		super.batchExecute(hql, values);

	}

}
