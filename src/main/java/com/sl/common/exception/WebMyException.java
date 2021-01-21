package com.sl.common.exception;

import com.sl.common.utils.WebCodeMsg;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * Author: 李旭日
 * Date: 2020/9/30 15:48
 * FileName: WebMyException
 * Description: Web端自定义异常
 */
public class WebMyException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(getClass());

    private int status;
    private String msg;
    private String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public WebMyException() {}


    public WebMyException(int status) {
        this.status =status;
        this.msg = WebCodeMsg.codeMsg.get(status);
    }
    public WebMyException(int i, String msg, String info) {
        this.status = i;
        this.msg = msg;
        this.info = info;
    }
    public WebMyException(int status, String info) {
        this.status =status;
        this.msg = WebCodeMsg.codeMsg.get(status);
        this.info = info;
    }

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



    @Override
    public String toString() {
        return "AppMyException{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
}
