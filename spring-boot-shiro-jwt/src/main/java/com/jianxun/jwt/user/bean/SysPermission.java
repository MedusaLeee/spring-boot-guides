package com.jianxun.jwt.user.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 权限.
 */

@Data
public class SysPermission implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private long id;//主键.
	
	private String name;//权限名称.
	
	private String resouceType;//资源类型 【menu|button】
	
	private String url;//资源路径
	
	private String permission;//权限字符串,menu --> role:*，button--> role:create,role:update,role:delete,role:view
	
	private Long parentId;//父节点编号.
	
	private String parentIds;//父编号列表.
	
	private Boolean available = false;

	//权限 - 角色 多对多的关系.
	private List<SysRole> roles;//角色.
}	
