package com.sl.idripweb.controller;

import com.sl.common.swagger.annos.ApiJsonObject;
import com.sl.common.swagger.annos.ApiJsonProperty;
import com.sl.common.swagger.response.ApiResponseObject;
import com.sl.common.swagger.response.ApiResponseProperty;
import com.sl.common.utils.WebResult;
import com.sl.idripweb.service.CurveAnalysisService;
import com.sl.idripweb.service.SynthesizeEnergy3dService;
import com.sl.idripweb.service.SynthesizeEnergyListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("synthesize_energy_3d")
@Api(value = "synthesize_energy_3d", description = "能耗分析==>综合能耗地图首页")
public class Energy3dController {

    @Autowired
    private SynthesizeEnergy3dService energy3dService;

    @Autowired
    private SynthesizeEnergyListService energyListService;


    /**
     * 能耗分析==>综合能耗地图首页
     * @param
     * @param
     * @return
     */
    @ApiOperation(value = "页面数据", notes = "能耗分析==>综合能耗地图首页", httpMethod = "POST")
    @PostMapping("index")
    public WebResult index(HttpServletRequest request){
        return energy3dService.index(request);
    }


    /**
     * 能耗分析==>综合能耗列表
     * @param
     * @param
     * @return
     */
    @ApiOperation(value = "页面数据", notes = "能耗分析==>综合能耗列表", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "factory_id", example = "36", description = "厂区id"),
            @ApiJsonProperty(type = String.class,key = "date_to", example = "2020-11-30", description = "选中的截止日期"),
    })
    @PostMapping("get_energy_info")
    public WebResult getEnergyInfo(HttpServletRequest request, @RequestAttribute Map<String,Object> map){
        return energyListService.index(request, map);
    }
}
