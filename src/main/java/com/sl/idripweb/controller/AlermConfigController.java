package com.sl.idripweb.controller;

import com.sl.common.swagger.annos.ApiJsonObject;
import com.sl.common.swagger.annos.ApiJsonProperty;
import com.sl.common.utils.WebResult;
import com.sl.idripweb.service.AlermConfigService;
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

import java.util.Map;

@Controller
@RequestMapping("/alConf")
@Api(value = "alConf", description = "报警配置/增删改查等接口")
public class AlermConfigController {

    @Autowired
    private AlermConfigService alermConfigService;



    @ApiOperation(value = "查询", notes = "查询", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "pagNum", example = "1", description = "页码"),
            @ApiJsonProperty(type = Integer.class,key = "pagSize", example = "10", description = "每页条数"),
            @ApiJsonProperty(type = Integer.class,key = "alerm_type_id", example = "2", description = "大类id(水/电/气)"),
            @ApiJsonProperty(type = Integer.class,key = "category", example = "2", description = "报警类别(电流/电压)"),
            @ApiJsonProperty(type = Integer.class,key = "confLevel", example = "1", description = "报警等级"),
            @ApiJsonProperty(type = Integer.class,key = "factory_id", example = "36", description = "厂区id")
    })
    @RequestMapping(value = "query",method = RequestMethod.POST)
    @ResponseBody
    public WebResult query(@RequestAttribute Map<String, Object> map) {
        return alermConfigService.query(map);
    }



    @ApiOperation(value = "报警分类关系", notes = "报警分类关系", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "alerm_type_id", example = "2", description = "报警类型")
    })
    @RequestMapping(value ="getCategoryRelation",method = RequestMethod.POST)
    @ResponseBody
    public WebResult getCategoryRelation(@RequestAttribute Map<String, Object> map) {

        return alermConfigService.getCategoryRelation(map);
    }



    @ApiOperation(value = "设备标签树", notes = "设备标签树", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "alerm_type_id", example = "2", description = "报警类型"),
            @ApiJsonProperty(type = Integer.class,key = "factory_id", example = "36", description = "厂区id")
    })
    @RequestMapping(value ="getRoomDeviceTagTree",method = RequestMethod.POST)
    @ResponseBody
    public WebResult getRoomDeviceTagTree(@RequestAttribute Map<String, Object> map) {

        return alermConfigService.getRoomDeviceTagTree(map);
    }



    @ApiOperation(value = "大类", notes = "大类", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
    })
    @RequestMapping(value ="getAlermType",method = RequestMethod.POST)
    @ResponseBody
    public WebResult getAlermType(@RequestAttribute Map<String, Object> map) {

        return alermConfigService.getAlermType(map);
    }



    @ApiOperation(value = "获取标签", notes = "获取标签", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "device_id", example = "10,11", description = "设备id,取返回树中的device_id,而不是id，多个用逗号隔开"),
            @ApiJsonProperty(type = Integer.class,key = "alerm_type_id", example = "1", description = "报警大类id")
    })
    @RequestMapping(value ="getTagByDeviceId",method = RequestMethod.POST)
    @ResponseBody
    public WebResult getTagByDeviceId(@RequestAttribute Map<String, Object> map) {

        return alermConfigService.getTagByDeviceId(map);
    }




    @ApiOperation(value = "新增", notes = "新增", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "parent_cate_id", example = "1", description = "报警父级类别id"),
            @ApiJsonProperty(type = Integer.class,key = "alerm_type_id", example = "1", description = "大类id"),
            @ApiJsonProperty(type = Integer.class,key = "category_id", example = "1", description = "分类id"),
            @ApiJsonProperty(type = Integer.class,key = "confLevel", example = "1", description = "报警等级"),
            @ApiJsonProperty(type = Integer.class,key = "factory_id", example = "1", description = "区域或厂区id"),
            @ApiJsonProperty(type = String.class,key = "device_id", example = "10,11", description = "设备id，多个用逗号连接"),
            @ApiJsonProperty(type = String.class,key = "tag", example = "uab", description = "标签名称"),
            @ApiJsonProperty(type = String.class,key = "confDesc", example = "描述", description = "报警配置描述"),
            @ApiJsonProperty(type = String.class,key = "hLevel1", example = "150", description = "高限"),
            @ApiJsonProperty(type = String.class,key = "hLevel2", example = "200", description = "高高限"),
            @ApiJsonProperty(type = String.class,key = "lLevel1", example = "100", description = "低限"),
            @ApiJsonProperty(type = String.class,key = "lLevel2", example = "50", description = "低低限"),
            @ApiJsonProperty(type = Integer.class,key = "position_val", example = "0", description = "开关（0:关，1开，2开关都报警）"),
            @ApiJsonProperty(type = Integer.class,key = "is_effect", example = "1", description = "是否启用(0否 1是)")
    })
    @RequestMapping(value ="save",method = RequestMethod.POST)
    @ResponseBody
    public WebResult save(@RequestAttribute Map<String, Object> map) {

        return alermConfigService.save(map);
    }



    @ApiOperation(value = "编辑", notes = "编辑", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "id", example = "1", description = "报警配置id"),
            @ApiJsonProperty(type = Integer.class,key = "parent_cate_id", example = "1", description = "报警父级类别id"),
            @ApiJsonProperty(type = Integer.class,key = "alerm_type_id", example = "1", description = "大类id"),
            @ApiJsonProperty(type = Integer.class,key = "category_id", example = "1", description = "分类id"),
            @ApiJsonProperty(type = Integer.class,key = "confLevel", example = "1", description = "报警等级"),
            @ApiJsonProperty(type = Integer.class,key = "factory_id", example = "1", description = "区域或厂区id"),
            @ApiJsonProperty(type = String.class,key = "device_id", example = "10,11", description = "设备id，多个用逗号连接"),
            @ApiJsonProperty(type = String.class,key = "tag", example = "uab", description = "标签名称"),
            @ApiJsonProperty(type = String.class,key = "confDesc", example = "描述", description = "报警配置描述"),
            @ApiJsonProperty(type = String.class,key = "hLevel1", example = "150", description = "高限"),
            @ApiJsonProperty(type = String.class,key = "hLevel2", example = "200", description = "高高限"),
            @ApiJsonProperty(type = String.class,key = "lLevel1", example = "100", description = "低限"),
            @ApiJsonProperty(type = String.class,key = "lLevel2", example = "50", description = "低低限"),
            @ApiJsonProperty(type = Integer.class,key = "position_val", example = "0", description = "开关（0:关，1开，2开关都报警）"),
            @ApiJsonProperty(type = Integer.class,key = "is_effect", example = "1", description = "是否启用(0否 1是)")
    })
    @RequestMapping(value ="update",method = RequestMethod.POST)
    @ResponseBody
    public WebResult update(@RequestAttribute Map<String, Object> map) {

        return alermConfigService.update(map);
    }




    @ApiOperation(value = "删除", notes = "删除", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "报警配置id")
    })
    @RequestMapping(value ="delete",method = RequestMethod.POST)
    @ResponseBody
    public WebResult delete(@RequestAttribute Map<String, Object> map) {

        return alermConfigService.delete(map);
    }

}
