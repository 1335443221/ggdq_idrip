package com.sl.idripweb.controller;

import com.sl.common.swagger.annos.ApiJsonObject;
import com.sl.common.swagger.annos.ApiJsonProperty;
import com.sl.common.utils.WebResult;
import com.sl.idripweb.service.ElecAnalysisService;
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
@RequestMapping("/elec_analysis")
@Api(value = "elec_analysis", description = "用电/用水/用气 能耗分析")
public class ElecAnalysisController {

    @Autowired
    private ElecAnalysisService elecAnalysisService;


    @ApiOperation(value = "用电/用水/用气 能耗分析", notes = "用电/用水/用气 能耗分析", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "factory_id", example = "36", description = "厂区id"),
            @ApiJsonProperty(type = Integer.class,key = "room_type", example = "1", description = "room类型[1电、2水、3气]."),
            @ApiJsonProperty(type = Integer.class,key = "room_id", example = "8", description = "配电室/水泵房/调压站id"),
            @ApiJsonProperty(type = Integer.class,key = "category_type", example = "1", description = "用能类型:1分类、2区域、3支路"),
            @ApiJsonProperty(type = Integer.class,key = "category_id", example = "0", description = "饼图下节点id；默认0全部"),
            @ApiJsonProperty(type = String.class,key = "date_type", example = "day_data", description = "报表类型:day_data、month_data、year_data"),
            @ApiJsonProperty(type = String.class,key = "date", example = "2020-10-22", description = "日期"),
    })
    @RequestMapping(value = "index",method = RequestMethod.POST)
    @ResponseBody
    public WebResult index(@RequestAttribute Map<String, Object> map) {

        return elecAnalysisService.index(map);
    }



    @ApiOperation(value = "获取负载率（电-支路用能）", notes = "获取负载率（电-支路用能）", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "room_type", example = "1", description = "room类型[1电、2水、3气]."),
            @ApiJsonProperty(type = Integer.class,key = "room_id", example = "8", description = "配电室/水泵房/调压站id"),
            @ApiJsonProperty(type = Integer.class,key = "transformer_id", example = "11", description = "变压器id"),
            @ApiJsonProperty(type = String.class,key = "date", example = "2020-07-16", description = "日期"),
    })
    @RequestMapping(value = "get_load_rate",method = RequestMethod.POST)
    @ResponseBody
    public WebResult getLoadRate(@RequestAttribute Map<String, Object> map) {

        return elecAnalysisService.getLoadRate(map);
    }

}
