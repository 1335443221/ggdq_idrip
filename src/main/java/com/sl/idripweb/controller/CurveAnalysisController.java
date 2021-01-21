package com.sl.idripweb.controller;

import com.sl.common.swagger.annos.ApiJsonAttribute;
import com.sl.common.swagger.annos.ApiJsonObject;
import com.sl.common.swagger.annos.ApiJsonProperty;
import com.sl.common.swagger.response.ApiResponseObject;
import com.sl.common.swagger.response.ApiResponseProperty;
import com.sl.common.utils.WebResult;
import com.sl.idripweb.service.CurveAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("curve_analysis")
@Api(value = "curve_analysis", description = "曲线分析接口")
public class CurveAnalysisController {

    @Autowired
    private CurveAnalysisService curveAnalysisService;


    /**
     * 曲线分析==>获取配电室/水泵房/调压站下meter列表
     * @param
     * @param map
     * @return
     */
    @ApiOperation(value = "获取配电室/水泵房/调压站下meter列表", notes = "曲线分析==>获取配电室/水泵房/调压站下meter列表", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = String.class,key = "room_type", example = "1", description = "room类型[1电、2水、3气]."),
            @ApiJsonProperty(type = String.class,key = "room_id", example = "0", description = "配电室/水泵房/调压站id"),
            @ApiJsonProperty(type = String.class,key = "factory_id", example = "36", description = "厂区id"),
    })
    @ApiResponseObject(properties = {
            @ApiResponseProperty(name = "room_name",description = "配电室名",type = "string"),
            @ApiResponseProperty(name = "device_id",description = "设备id",type = "string"),
            @ApiResponseProperty(name = "device_name",description = "设备名",type = "string"),
            @ApiResponseProperty(name = "panel_id",description = "设备所属柜子id",type = "string"),
            @ApiResponseProperty(name = "tg_id",description = "设备所属通讯机",type = "string"),
            @ApiResponseProperty(name = "device_desc",description = "设备描述",type = "string"),
            @ApiResponseProperty(name = "factory_id",description = "厂区id",type = "string"),
    })
    @PostMapping("get_room_device")
    public WebResult getRoomDevice(@RequestAttribute Map<String,Object> map){
        return curveAnalysisService.getRoomDevice(map);
    }

    /**
     * 曲线分析==>获取配电室低压进线表
     * @param
     * @param map
     * @return
     */
    @ApiOperation(value = "获取配电室低压进线表", notes = "曲线分析==>获取配电室低压进线表", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = String.class,key = "room_type", example = "1", description = "room类型[1电、2水、3气]."),
            @ApiJsonProperty(type = String.class,key = "room_id", example = "8", description = "配电室id"),
    })
    @ApiResponseObject(properties = {
            @ApiResponseProperty(name = "device_id",description = "设备id",type = "string"),
            @ApiResponseProperty(name = "desc",description = "设备描述",type = "string"),
            @ApiResponseProperty(name = "device_name",description = "设备名",type = "string"),
    })
    @PostMapping("get_low_incoming_line")
    public WebResult getLowIncomingLine(@RequestAttribute Map<String,Object> map){
        return curveAnalysisService.getLowIncomingLine(map);
    }

    /**
     * 曲线分析==>页面数据
     * @param
     * @param map
     * @return
     */
    @ApiOperation(value = "页面数据", notes = "曲线分析==>页面数据", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = String.class,key = "room_type", example = "1", description = "room类型[1电、2水、3气]."),
            @ApiJsonProperty(type = String.class,key = "room_id", example = "8", description = "配电室id"),
            @ApiJsonProperty(type = String.class,key = "device_id", example = "290", description = "设备id"),
            @ApiJsonProperty(type = String.class,key = "device_name", example = "a2_b12", description = "设备名称"),
            @ApiJsonProperty(type = List.class,key = "params_arr", example = "[\"uPhase\",\"va\"]", description = "uPhase(U相)； uLine(U线)；I(I)；va(thd-va)；vb(thd-vb)；vc(thd-vc)；ia(thd-ia)；ib(thd-ib)；ic(thd-ic)"),
            @ApiJsonProperty(type = List.class,key = "thdNum_arr", example = "[]", description = "0(谐波总量)；2~17"),
            @ApiJsonProperty(type = String.class,key = "date_from", example = "2020-11-18 16:26", description = "开始时间"),
            @ApiJsonProperty(type = String.class,key = "date_to", example = "2020-11-19 16:26", description = "结束时间（时间范围小于24小时）"),
    })
    @ApiResponseObject(properties = {
            @ApiResponseProperty(name = "chart_list",description = "返回结果",type = "object"),
            @ApiResponseProperty(name = "res1",description = "UI相关曲线",type = "object"),
            @ApiResponseProperty(name = "res2",description = "三相不平衡曲线",type = "object"),
            @ApiResponseProperty(name = "legend",description = "图例列表",type = "string"),
            @ApiResponseProperty(name = "xAxis",description = "X轴列表",type = "string"),
            @ApiResponseProperty(name = "data",description = "Y轴数据",type = "string"),
            @ApiResponseProperty(name = "max",description = "数据最大值",type = "string"),
            @ApiResponseProperty(name = "min",description = "数据最小值",type = "string"),
            @ApiResponseProperty(name = "avg",description = "数据平均值",type = "string"),
    })
    @PostMapping("index")
    public WebResult index(HttpServletRequest request, @RequestAttribute Map<String,Object> map){
        return curveAnalysisService.index(request, map);
    }
}
