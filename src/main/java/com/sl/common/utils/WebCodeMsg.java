package com.sl.common.utils;

import java.util.HashMap;
import java.util.Map;

public class WebCodeMsg {
	public static Map<Integer,String> codeMsg=new HashMap<>();
	//初始化状态码与文字说明
	static {
		/* 成功状态码 */
		codeMsg.put(200, "请求成功");

		/* 操作失败状态码 */
		codeMsg.put(201,"操作失败");
		/* 密码错误 */
		codeMsg.put(202,"密码错误");

		/* 权限错误码  205-300*/
		codeMsg.put(205,"你没有权限查看该模块!");
		codeMsg.put(206,"暂无权限!");

		/* 暂无数据状态码 */
		codeMsg.put(207, "暂无数据");

		/* 参数错误：301-400 */
		codeMsg.put(301, "参数缺失");

		/* 前端拦截错误码 401-500*/
		codeMsg.put(450,"登录已过期!");
		codeMsg.put(404,"请求url错误!");
		codeMsg.put(500,"服务器错误!");


		/* 数据错误：500-600 */
		codeMsg.put(501, "数据重复插入");
		codeMsg.put(502, "redis数据异常");


		/* 业务错误：601-700 */
		codeMsg.put(601, "用户名/密码错误");
		codeMsg.put(602, "未找到该用户的项目信息，请联系管理员！");
		codeMsg.put(603, "用户不存在");
		codeMsg.put(604, "新密码与旧密码相同，请更换新密码");
		codeMsg.put(605, "原密码错误");
		codeMsg.put(606, "无此通讯机");

	}

	private int status;
	private String msg;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
