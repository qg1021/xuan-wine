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
package com.gm.wine.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;
import org.springside.modules.orm.PropertyFilter;

import cn.common.lib.cache.CacheKeyUtil;
import cn.common.lib.cache.CacheManager;
import cn.common.lib.springside.orm.BaseDao;

import com.gm.wine.api.ProductDao;
import com.gm.wine.entity.Product;
import com.google.common.collect.Lists;

/**
 * 用户商品询价业务逻辑类
 * 
 * @author qingang
 * @version 1.0
 * @since 2012-7-25
 */
@Component
@Transactional
public class ProductManager extends CacheEntityManager<Product, Long> {
	@Autowired
	private ProductDao productDao;

	@Autowired
	@Qualifier("ehCacheManager")
	private CacheManager cacheManager;

	/**
	 * {@inheritDoc}
	 * 
	 * @since 2012-7-25
	 * @see cn.common.lib.springside.service.EntityManager#getEntityDao()
	 */
	@Override
	protected BaseDao<Product, Long> getEntityDao() {
		return productDao;
	}

	@Override
	public void save(Product entity) {

		productDao.save(entity);
		cacheManager.clear(CacheKeyUtil.ProductPrefix);
	}

	@Override
	public void delete(Long id) {
		super.delete(id);
		cacheManager.clear(CacheKeyUtil.ProductPrefix);
	}

	/**
	 * 
	 * 批量删除
	 * 
	 * @since 2012-7-26
	 * @author qingang
	 * @param ids
	 */
	public void batchDelete(List<Long> ids) {
		productDao.batchDelete(ids);
		cacheManager.clear(CacheKeyUtil.ProductPrefix);
	}

	/**
	 * 
	 * 发布
	 * 
	 * @since 2012-11-25
	 * @author qingang
	 * @param ids
	 */
	public void publish(List<Long> ids) {
		productDao.operate(ids, true);
		cacheManager.clear(CacheKeyUtil.ProductPrefix);
	}

	/**
	 * 
	 * 取消发布
	 * 
	 * @since 2012-7-26
	 * @author qingang
	 * @param ids
	 */
	public void cancel(List<Long> ids) {
		productDao.operate(ids, false);
		cacheManager.clear(CacheKeyUtil.ProductPrefix);
	}

	/**
	 * 
	 * 取一定产品
	 * 
	 * @since 2012-7-26
	 * @author qingang
	 * @param pagesize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Product> search(long thirdcat, int pagesize) {

		String cacheKey = CacheKeyUtil.buildProductKey(thirdcat, pagesize);
		Object object = cacheManager.get(cacheKey);
		if (object == null) {// get from db
			Page<Product> page = new Page<Product>(pagesize);
			List<PropertyFilter> filters = Lists.newArrayList();
			filters.add(new PropertyFilter("EQB_status", "true"));
			filters.add(new PropertyFilter("EQL_threecatid", String
					.valueOf(thirdcat)));
			List<Product> list = productDao.findPage(page, filters).getResult();
			// 从对象列表中取得key列表
			cacheManager.put(cacheKey, getIDs(list));
			return list;
		} else {
			List<Long> idList = (List<Long>) object;
			return super.getObjectsbyIds(idList);
		}
	}

	/**
	 * 
	 * 首页热销推荐
	 * 
	 * @since 2012-12-23
	 * @author qingang
	 * @param pagesize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Product> searchRecommend(int pagesize) {

		String cacheKey = CacheKeyUtil.buildRecommendProductKey(pagesize);
		Object object = cacheManager.get(cacheKey);
		if (object == null) {// get from db
			Page<Product> page = new Page<Product>(pagesize);
			List<PropertyFilter> filters = Lists.newArrayList();
			filters.add(new PropertyFilter("EQB_status", "true"));
			filters.add(new PropertyFilter("EQB_recommend", String
					.valueOf(true)));
			List<Product> list = productDao.findPage(page, filters).getResult();
			// 从对象列表中取得key列表
			cacheManager.put(cacheKey, getIDs(list));
			return list;
		} else {
			List<Long> idList = (List<Long>) object;
			return super.getObjectsbyIds(idList);
		}
	}

}
