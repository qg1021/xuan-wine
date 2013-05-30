package com.gm.wine.common;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * 留言公告
 * 
 * @author qingang
 * @version 1.0
 * @since 2012-7-24
 */
public class NoticeVO implements Serializable {

	/**
	 * serialVersionUID long
	 */
	private static final long serialVersionUID = 1L;

	private String title; // 留言标题

	private String content; // 留言内容

	private UserVO user; // 留言人
	private List<NoticeVO> childs;

	private Date createdate;

	public UserVO getUser() {
		return user;
	}

	public void setUser(UserVO user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public List<NoticeVO> getChilds() {
		return childs;
	}

	public void setChilds(List<NoticeVO> childs) {
		this.childs = childs;
	}

}