package com.sl.idripweb.controller;

import com.sl.common.swagger.annos.ApiJsonObject;
import com.sl.common.swagger.annos.ApiJsonProperty;
import com.sl.common.utils.WebResult;
import com.sl.idripweb.service.PowerService;
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
@RequestMapping("/power")
@Api(value = "power", description = "用电监测/电路图")
public class PowerController {

    @Autowired
    private PowerService powerService;



    @ApiOperation(value = "计量表数据（旧电路图）", notes = "计量表数据（旧电路图）", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "factory_id", example = "36", description = "厂区id"),
            @ApiJsonProperty(type = Integer.class,key = "room_id", example = "9", description = "配电室id"),
            @ApiJsonProperty(type = Integer.class,key = "category_type_id", example = "7", description = "变压器/线路id."),
            @ApiJsonProperty(type = String.class,key = "room_type", example = "1", description = "配电室类型")
    })
    @RequestMapping(value = "getDatav3",method = RequestMethod.POST)
    @ResponseBody
    public WebResult getDatav3(@RequestAttribute Map<String, Object> map) {

        return powerService.getDatav3(map);
    }
    @ApiOperation(value = "计量表数据（新电路图）", notes = "计量表数据（新电路图）", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "factory_id", example = "36", description = "厂区id"),
            @ApiJsonProperty(type = Integer.class,key = "room_id", example = "9", description = "配电室id"),
            @ApiJsonProperty(type = Integer.class,key = "category_type_id", example = "7", description = "变压器/线路id."),
            @ApiJsonProperty(type = String.class,key = "room_type", example = "1", description = "配电室类型")
    })
    @RequestMapping(value = "getDeviceMonitor",method = RequestMethod.POST)
    @ResponseBody
    public WebResult getDeviceMonitor(@RequestAttribute Map<String, Object> map) {

        return powerService.getDeviceMonitor(map);
    }



    @ApiOperation(value = "获取选择框数据", notes = "获取选择框数据", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "room_id", example = "9", description = "配电室id"),
            @ApiJsonProperty(type = String.class,key = "room_type", example = "1", description = "配电室类型."),
    })
    @RequestMapping(value = "getSelectsData",method = RequestMethod.POST)
    @ResponseBody
    public WebResult getSelectsData(@RequestAttribute Map<String, Object> map) {

        return powerService.getSelectsData(map);
    }


}
