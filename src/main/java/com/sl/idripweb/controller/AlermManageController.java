package com.sl.idripweb.controller;

import com.sl.common.swagger.annos.ApiJsonObject;
import com.sl.common.swagger.annos.ApiJsonProperty;
import com.sl.common.utils.WebResult;
import com.sl.idripweb.service.AlermManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@Controller
@RequestMapping("/alerm")
@Api(value = "alerm", description = "报警管理")
public class AlermManageController {

    @Autowired
    private AlermManagerService alermManagerService;


    @ApiOperation(value = "初始化数据(页面打开数据)", notes = "初始化数据(页面打开数据)", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "pagNum", example = "1", description = "页码"),
            @ApiJsonProperty(type = Integer.class,key = "pagSize", example = "10", description = "每页条数"),
            @ApiJsonProperty(type = String.class,key = "btime", example = "2020-10-10", description = "开始时间"),
            @ApiJsonProperty(type = String.class,key = "etime", example = "2020-10-10", description = "结束时间"),
            @ApiJsonProperty(type = String.class,key = "isdeal", example = "1", description = "处理类别 0:未处理 1：已处理."),
            @ApiJsonProperty(type = Integer.class,key = "factory_id", example = "36", description = "厂区id,若不填写，默认选择系统第一个厂区"),
            @ApiJsonProperty(type = Integer.class,key = "category", example = "2", description = "报警类型"),
            @ApiJsonProperty(type = String.class,key = "business_name", example = "", description = "商户名称")
    })
    @RequestMapping(value = "initData",method = RequestMethod.POST)
    @ResponseBody
    public WebResult initData(@RequestAttribute Map<String, Object> map) {

        return alermManagerService.initData(map);
    }


    @ApiOperation(value = "查询数据", notes = "查询数据", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "pagNum", example = "1", description = "页码"),
            @ApiJsonProperty(type = Integer.class,key = "pagSize", example = "10", description = "每页条数"),
            @ApiJsonProperty(type = String.class,key = "btime", example = "2020-10-10", description = "开始时间"),
            @ApiJsonProperty(type = String.class,key = "etime", example = "2020-10-10", description = "结束时间"),
            @ApiJsonProperty(type = String.class,key = "isdeal", example = "1", description = "处理类别 0:未处理 1：已处理."),
            @ApiJsonProperty(type = Integer.class,key = "factory_id", example = "36", description = "厂区id,若不填写，默认选择系统第一个厂区"),
            @ApiJsonProperty(type = Integer.class,key = "category", example = "2", description = "报警类型"),
            @ApiJsonProperty(type = String.class,key = "business_name", example = "视通天地", description = "商户名称")
    })
    @RequestMapping(value = "getData",method = RequestMethod.POST)
    @ResponseBody
    public WebResult getData(@RequestAttribute Map<String, Object> map) {
        return alermManagerService.getData(map);
    }



    @ApiOperation(value = "获取下拉框数据 分类+厂区等", notes = "获取下拉框数据 分类+厂区等", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
    })
    @RequestMapping(value = "getCategoryAndRoom",method = RequestMethod.POST)
    @ResponseBody
    public WebResult getCategoryAndRoom(@RequestAttribute Map<String, Object> map) {
        return alermManagerService.getCategoryAndRoom(map);
    }




    @ApiOperation(value = "报警处理详情", notes = "报警处理详情", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "logid", example = "1", description = "报警id")
    })
    @RequestMapping(value = "queryDealDetail",method = RequestMethod.POST)
    @ResponseBody
    public WebResult queryDealDetail(@RequestAttribute Map<String, Object> map) {
        return alermManagerService.queryDealDetail(map);
    }



    @ApiOperation(value = "报警处理", notes = "报警处理", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "logid", example = "1", description = "报警id"),
            @ApiJsonProperty(type = String.class,key = "msg", example = "标记内容", description = "标记内容")
    })
    @RequestMapping(value = "setDeal",method = RequestMethod.POST)
    @ResponseBody
    public WebResult setDeal(@RequestAttribute Map<String, Object> map) {
        return alermManagerService.setDeal(map);
    }



    @ApiOperation(value = "维修登记", notes = "维修登记", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "logid", example = "1", description = "报警id"),
            @ApiJsonProperty(type = String.class,key = "msg", example = "标记内容", description = "标记内容")
    })
    @RequestMapping(value = "setRepairMsg",method = RequestMethod.POST)
    @ResponseBody
    public WebResult setRepairMsg(@RequestAttribute Map<String, Object> map) {
        return alermManagerService.setRepairMsg(map);
    }



    @ApiOperation(value = "全局弹窗报警信息", notes = "全局弹窗报警信息", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "pagNum", example = "1", description = "页码"),
            @ApiJsonProperty(type = Integer.class,key = "pagSize", example = "10", description = "每页条数")
    })
    @RequestMapping(value = "getPopupData",method = RequestMethod.POST)
    @ResponseBody
    public WebResult getPopupData(@RequestAttribute Map<String, Object> map) {
        return alermManagerService.getPopupData(map);
    }



    @ApiOperation(value = "全局报警确认", notes = "全局报警确认", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "ids", example = "13,14", description = "报警id 多个以,隔开")
    })
    @RequestMapping(value = "confirm",method = RequestMethod.POST)
    @ResponseBody
    public WebResult confirm(@RequestAttribute Map<String, Object> map) {
        return alermManagerService.confirm(map);
    }




    @ApiOperation(value = "报警打印", notes = "报警打印", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = String.class,key = "btime", example = "2020-10-10", description = "开始时间"),
            @ApiJsonProperty(type = String.class,key = "etime", example = "2020-10-10", description = "结束时间"),
            @ApiJsonProperty(type = String.class,key = "isdeal", example = "1", description = "处理类别 0:未处理 1：已处理."),
            @ApiJsonProperty(type = Integer.class,key = "factory_id", example = "36", description = "厂区id,若不填写，默认选择系统第一个厂区"),
            @ApiJsonProperty(type = Integer.class,key = "category", example = "2", description = "报警类型"),
            @ApiJsonProperty(type = String.class,key = "business_name", example = "", description = "商户名称")
    })
    @RequestMapping(value = "print_alerm",method = RequestMethod.POST)
    @ResponseBody
    public WebResult printAlerm(@RequestAttribute Map<String, Object> map) {
        return alermManagerService.printAlerm(map);
    }


}
