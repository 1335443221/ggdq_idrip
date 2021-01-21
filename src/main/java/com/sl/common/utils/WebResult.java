package com.sl.common.utils;

public class WebResult<T> {
	private String msg;
	private int status;
	private T data;

//************************构造方法*************************//
	/**
	 * 成功时候的调用 自定义状态码
	 * @return
	 */
	public static <T> WebResult<T> success(int status,String msg,T data){
		return new WebResult<T>(status,msg,data);

	}

	/**
	 * 构造方法  自定义状态码+扩展数据
	 */
	private WebResult(int status,String msg,T data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}
	/**
	 * 构造方法  自定义状态码
	 */
	private WebResult(int status,String msg) {
		this.status = status;
		this.msg = msg;
	}

	/**
	 * 构造方法  状态码+扩展数据
	 */
	private WebResult(int status,T data) {
		this.status = status;
		this.msg = WebCodeMsg.codeMsg.get(status);
		this.data = data;
	}

	/**
	 * 构造方法  只传错误码 返回 错误码对应的文字说明
	 */
	private WebResult(int status) {
		this.status = status;
		this.msg = WebCodeMsg.codeMsg.get(status);
	}
	/**
	 * 成功时构造方法   只传数据
	 */
	private WebResult(T data) {
		this.status = 200;
		this.msg = "请求成功";
		this.data = data;
	}


 //************************成功时调用*************************//
	/**
	 * 成功时调用  只传数据
	 */
	public static <T> WebResult<T> success(T data){
		return new WebResult<T>(data);
	}

	/**
	 * 成功时调用   自定义错误码
	 */
	public static <T> WebResult<T> success(int status,String msg){
		return new WebResult<T>(status,msg);
	}

	/**
	 * 成功，不需要传入参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> WebResult<T> success(){
		return (WebResult<T>) success("");
	}
//************************失败时调用*************************//

	/**
	 * 失败时调用   只传错误码 返回 错误码对应的文字说明
	 */
	public static <T> WebResult<T> error(int status){
		return new WebResult<T>(status);
	}

	/**
	 * 失败时候的调用,扩展消息参数
	 * @param msg
	 * @return
	 */
	public static <T> WebResult<T> error(int status, String msg){
		msg=WebCodeMsg.codeMsg.get(status)+"----"+msg;
		return new WebResult<T>(status,msg);
	}
	/**
	 * 失败时候的调用,扩展消息参数
	 * @return
	 */
	public static <T> WebResult<T> error(int status, T data){
		return new WebResult<T>(status,data);
	}

	/**
	 * 失败时候的调用,扩展消息参数
	 * @return
	 */
	public static <T> WebResult<T> error(int status,String msg, T data){
		return new WebResult<T>(status,msg,data);
	}

	/**
	 * 失败时候的调用,扩展数据参数,数据参数""空字符串和null对前端有区别
	 * 失败时候的调用,扩展消息参数
	 * @return
	 */
	public static <T> WebResult<T> errorData(int status, T data) {
		return new WebResult<T>(status, data);
	}


	public T getData() {
		return data;
	}
	public String getMsg() {
		return msg;
	}
	public int getStatus() {
		return status;
	}


	@Override
	public String toString() {
		return "{\"status\":" + status +", \"msg\":\"" + msg + "\" ,\"data\":\"" + data +"\"}";
	}
}
