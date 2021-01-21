package com.sl.idripweb.controller;

import com.sl.common.swagger.annos.ApiJsonObject;
import com.sl.common.swagger.annos.ApiJsonProperty;
import com.sl.common.utils.WebResult;
import com.sl.idripweb.service.ParkOverview3DService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/park_overview_3d")
@Api(value = "park_overview_3d", description = "三维园区/三维配电室")
public class ParkOverview3DController {

    @Autowired
    private ParkOverview3DService parkOverview3DService;



    @ApiOperation(value = "3D配电室数据", notes = "配电室当日总能耗/温度/湿度/", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "room_type", example = "1", description = "配电室类型，1电、2水、3气"),
            @ApiJsonProperty(type = Integer.class,key = "room_id", example = "8", description = "配电室id")
    })
    @RequestMapping(value = "transformerroomData",method = RequestMethod.POST)
    @ResponseBody
    public WebResult transformerroomData(@RequestAttribute Map<String, Object> map) {
        return parkOverview3DService.transformerroomData(map);
    }



}
