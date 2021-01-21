package com.sl.common.utils;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SetParams {
	public static void setPagParam(Map<String,Object> map){
		int fromNum=0;
		int pagSize=20;
		if(map.get("pagNum")!=null&&map.get("pagSize")!=null){
			 fromNum=(Integer.parseInt(map.get("pagNum").toString())-1)*Integer.parseInt(map.get("pagSize").toString());
			 pagSize=Integer.parseInt(map.get("pagSize").toString());
		}
		map.put("fromNum", fromNum);
		map.put("pagSize", pagSize);
	}

	public static void setPageParam(Map<String,Object> map){
		int fromNum=0;
		int pagSize=20;
		if(map.get("pageNum")!=null&&map.get("pageSize")!=null){
			 fromNum=(Integer.parseInt(map.get("pageNum").toString())-1)*Integer.parseInt(map.get("pageSize").toString());
			 pagSize=Integer.parseInt(map.get("pageSize").toString());
		}
		map.put("fromNum", fromNum);
		map.put("pageSize", pagSize);
	}
}
