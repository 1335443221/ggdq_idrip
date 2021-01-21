package com.sl.idripweb.controller;

import com.sl.common.swagger.annos.ApiJsonObject;
import com.sl.common.swagger.annos.ApiJsonProperty;
import com.sl.common.utils.WebResult;
import com.sl.idripweb.service.MessageManageService;
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
@RequestMapping("/message_manage")
@Api(value = "message_manage", description = "短信管理/短信配置")
public class MessageManageController {

    @Autowired
    private MessageManageService messageManageService;


    @ApiOperation(value = "查看按钮", notes = "查看按钮", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "pagNum", example = "1", description = "页码"),
            @ApiJsonProperty(type = Integer.class,key = "pagSize", example = "10", description = "每页条数"),
            @ApiJsonProperty(type = String.class,key = "date", example = "2020-10-10", description = "时间")
    })
    @RequestMapping(value = "getMessageInfo",method = RequestMethod.POST)
    @ResponseBody
    public WebResult getMessageInfo(@RequestAttribute Map<String, Object> map) {
        return messageManageService.getMessageInfo(map);
    }



    @ApiOperation(value = "统计", notes = "统计", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "pagNum", example = "1", description = "页码"),
            @ApiJsonProperty(type = Integer.class,key = "pagSize", example = "10", description = "每页条数"),
            @ApiJsonProperty(type = String.class,key = "bdate", example = "2019-05-10", description = "开始时间"),
            @ApiJsonProperty(type = String.class,key = "edate", example = "2020-10-10", description = "开始时间")
    })
    @RequestMapping(value = "getMessageCount",method = RequestMethod.POST)
    @ResponseBody
    public WebResult getMessageCount(@RequestAttribute Map<String, Object> map) {
        return messageManageService.getMessageCount(map);
    }



    @ApiOperation(value = "新增", notes = "新增", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "role_id", example = "49", description = "角色id"),
            @ApiJsonProperty(type = Integer.class,key = "alerm_level", example = "1", description = "报警等级")
    })
    @RequestMapping(value = "save",method = RequestMethod.POST)
    @ResponseBody
    public WebResult save(@RequestAttribute Map<String, Object> map) {
        return messageManageService.save(map);
    }



    @ApiOperation(value = "修改", notes = "修改", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "role_id", example = "1", description = "角色id"),
            @ApiJsonProperty(type = Integer.class,key = "alerm_level", example = "1", description = "报警等级"),
            @ApiJsonProperty(type = Integer.class,key = "id", example = "1", description = "配置id")
    })
    @RequestMapping(value = "update",method = RequestMethod.POST)
    @ResponseBody
    public WebResult update(@RequestAttribute Map<String, Object> map) {
        return messageManageService.update(map);
    }



    @ApiOperation(value = "删除", notes = "删除", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "id", example = "1", description = "配置id")
    })
    @RequestMapping(value = "delete",method = RequestMethod.POST)
    @ResponseBody
    public WebResult delete(@RequestAttribute Map<String, Object> map) {
        return messageManageService.delete(map);
    }



    @ApiOperation(value = "查看", notes = "查看", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "alerm_level", example = "1", description = "报警等级"),
            @ApiJsonProperty(type = Integer.class,key = "role_id", example = "49", description = "角色id"),
            @ApiJsonProperty(type = Integer.class,key = "pagNum", example = "1", description = "页码"),
            @ApiJsonProperty(type = Integer.class,key = "pagSize", example = "10", description = "每页条数")
    })
    @RequestMapping(value = "query",method = RequestMethod.POST)
    @ResponseBody
    public WebResult query(@RequestAttribute Map<String, Object> map) {
        return messageManageService.query(map);
    }




    @ApiOperation(value = "获取角色列表(岗位)", notes = "获取角色列表(岗位)", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
    })
    @RequestMapping(value = "getRoles",method = RequestMethod.POST)
    @ResponseBody
    public WebResult getRoles(@RequestAttribute Map<String, Object> map) {
        return messageManageService.getRoles(map);
    }




    @ApiOperation(value = "getUserByRoleId", notes = "通过角色id获取user信息", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "role_id", example = "49", description = "角色id")
    })
    @RequestMapping(value = "getUserByRoleId",method = RequestMethod.POST)
    @ResponseBody
    public WebResult getUserByRoleId(@RequestAttribute Map<String, Object> map) {
        return messageManageService.getUserByRoleId(map);
    }


}
