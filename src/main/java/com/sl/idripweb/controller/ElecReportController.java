package com.sl.idripweb.controller;

import com.sl.common.swagger.annos.ApiJsonObject;
import com.sl.common.swagger.annos.ApiJsonProperty;
import com.sl.common.utils.WebResult;
import com.sl.idripweb.service.ElecReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/elec_report")
@Api(value = "elec_report", description = "用电报表")
public class ElecReportController {

    @Autowired
    private ElecReportService elecReportService;



    @ApiOperation(value = "index", notes = "用电报表", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "factory_id", example = "36", description = "厂区id"),
            @ApiJsonProperty(type = Integer.class,key = "room_type", example = "1", description = "room类型[1电、2水、3气]."),
            @ApiJsonProperty(type = Integer.class,key = "room_id", example = "8", description = "配电室id"),
            @ApiJsonProperty(type = Integer.class,key = "category_type", example = "3", description = "用能类型:1分类（默认）、2区域、3支路"),
            @ApiJsonProperty(type = String.class,key = "date_type", example = "day_data", description = "报表类型:hour_data（分时报表）、day_data、month_data、year_data"),
            @ApiJsonProperty(type = String.class,key = "date_from", example = "2020-11-19", description = "开始时间"),
            @ApiJsonProperty(type = String.class,key = "date_to", example = "2020-11-19", description = "结束时间"),
            @ApiJsonProperty(type = String.class,key = "select_category_ids", example = "", description = "选中节点id；多个值用逗号拼接；默认空（全部）")
    })
    @RequestMapping(value = "index",method = RequestMethod.POST)
    @ResponseBody
    public WebResult index(HttpServletRequest request, @RequestAttribute Map<String, Object> map) {

        return elecReportService.index(request, map);
    }


    @ApiOperation(value = "index", notes = "用电报表下载（GET请求）", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "factory_id", value = "厂区id", example = "36"),
            @ApiImplicitParam(name = "room_type", value = "room类型[1电、2水、3气]", example = "1"),
            @ApiImplicitParam(name = "room_id", value = "配电室id", example = "8"),
            @ApiImplicitParam(name = "category_type", value = "用能类型:1分类（默认）、2区域、3支路", example = "1"),
            @ApiImplicitParam(name = "date_type", value = "报表类型:hour_data（分时报表）、day_data、month_data、year_data", example = "day_data"),
            @ApiImplicitParam(name = "date_from", value = "开始时间", example = "2020-11-19"),
            @ApiImplicitParam(name = "date_to", value = "结束时间", example = "2020-11-19"),
            @ApiImplicitParam(name = "select_category_ids", value = "选中节点id；多个值用逗号拼接；默认空（全部）", example = "")
    })
    @RequestMapping(value = "index",method = RequestMethod.GET)
    @ResponseBody
    public WebResult indexExport(HttpServletRequest request, HttpServletResponse response,@RequestAttribute Map<String, Object> map) throws Exception{
        return elecReportService.indexExport(request,response,map);
    }


}
