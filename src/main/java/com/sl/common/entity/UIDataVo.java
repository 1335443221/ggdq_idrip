package com.sl.common.entity;

public class UIDataVo {
   private String _id;
   private String data_time;
   private String val;
   private String tag;
   private String device;
   private String tg;
   private String log_time;
public String get_id() {
	return _id;
}
public void set_id(String _id) {
	this._id = _id;
}
public String getData_time() {
	return data_time;
}
public void setData_time(String data_time) {
	this.data_time = data_time;
}
public String getVal() {
	return val;
}
public void setVal(String val) {
	this.val = val;
}
public String getTag() {
	return tag;
}
public void setTag(String tag) {
	this.tag = tag;
}
public String getDevice() {
	return device;
}
public void setDevice(String device) {
	this.device = device;
}
public String getTg() {
	return tg;
}
public void setTg(String tg) {
	this.tg = tg;
}
public String getLog_time() {
	return log_time;
}
public void setLog_time(String log_time) {
	this.log_time = log_time;
}
@Override
public String toString() {
	return "UIDataVo [_id=" + _id + ", data_time=" + data_time + ", val=" + val + ", tag=" + tag + ", device=" + device
			+ ", tg=" + tg + ", log_time=" + log_time + "]";
}
   
}
