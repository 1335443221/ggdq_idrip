package com.sl.common.controller;

import com.sl.common.service.CommonService;
import com.sl.common.swagger.annos.ApiJsonObject;
import com.sl.common.swagger.annos.ApiJsonProperty;
import com.sl.common.utils.WebResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("common")
@Api(value = "common", description = "通用接口")
public class CommonController {

    @Autowired
    private CommonService commonService;


    @ApiOperation(value = "获取变压器列表", notes = "获取变压器列表", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "room_type", example = "1", description = "room类型[1电、2水、3气]."),
            @ApiJsonProperty(type = Integer.class,key = "room_id", example = "8", description = "配电室/水泵房/调压站id")
    })
    @RequestMapping(value = "get_transformer_list",method = RequestMethod.POST)
    @ResponseBody
    public WebResult getTransformerList(@RequestAttribute Map<String, Object> map) {
        return commonService.getTransformerList(map);
    }


    /**
     * 获取主页面4个菜单
     * @param
     * @param
     * @return
     */
    @ApiOperation(value = "获取主页面菜单", notes = "获取主页面4个菜单", httpMethod = "POST")
    @PostMapping("goMainPage")
    @ResponseBody
    public WebResult goMainPage(HttpServletRequest request){
        return commonService.goMainPage(request);
    }

    /**
     * 获取菜单树
     * @param
     * @param
     * @return
     */
    @ApiOperation(value = "获取菜单树", notes = "获取菜单树", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "parent_menu_id", example = "1", description = "父级菜单id，默认0全部")
    })
    @PostMapping("main_nav")
    @ResponseBody
    public WebResult mainNav(HttpServletRequest request, @RequestAttribute Map<String, Object> map){
        return commonService.mainNav(request, map);
    }

    @ApiOperation(value = "获取树结构/水电气", notes = "获取树结构/水电气", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "room_type", example = "1", description = "room类型[1电、2水、3气]."),
            @ApiJsonProperty(type = Integer.class,key = "room_id", example = "8", description = "配电室/水泵房/调压站id"),
            @ApiJsonProperty(type = Integer.class,key = "category_type", example = "1", description = "用能类型:1分类（默认）、2区域、3支路"),
            @ApiJsonProperty(type = Integer.class,key = "factory_id", example = "36", description = "厂区id；默认取session"),
    })
    @RequestMapping(value = "get_category_tree",method = RequestMethod.POST)
    @ResponseBody
    public WebResult getCategoryTree(@RequestAttribute Map<String, Object> map, HttpServletRequest request) {
        return commonService.getCategoryTree(map,request);
    }

    /**
     * 获取厂区列表
     * @param
     * @param
     * @return
     */
    @ApiOperation(value = "获取厂区列表", notes = "获取厂区列表", httpMethod = "POST")
    @PostMapping("get_factory_by_project")
    @ResponseBody
    public WebResult getFactoryByProject(HttpServletRequest request){
        return commonService.getFactoryByProject(request);
    }

    /**
     * 获取room列表
     * @param
     * @param
     * @return
     */
    @ApiOperation(value = "获取room列表", notes = "获取room列表", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "room_type", example = "1", description = "room类型[1电、2水、3气]"),
            @ApiJsonProperty(type = Integer.class,key = "is_all", example = "1", description = "返回结果是否包含“全部”")
    })
    @PostMapping("get_room_by_factory")
    @ResponseBody
    public WebResult getRoomByFactory(HttpServletRequest request, @RequestAttribute Map<String, Object> map){
        return commonService.getRoomByFactory(request, map);
    }

    /**
     * 验证敏感操作密码
     * @param
     * @param
     * @return
     */
    @ApiOperation(value = "验证敏感操作密码", notes = "验证敏感操作密码", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = Integer.class,key = "oper_pwd", example = "d0e1e2dc9473cdb301f7e97de3bda3cd", description = "操作密码")
    })
    @PostMapping("verify_pwd")
    @ResponseBody
    public WebResult verify_pwd(HttpServletRequest request, @RequestAttribute Map<String, Object> map){
        return commonService.verifyPwd(request, map);
    }


    /**
     * 下置并记录操作日志（需先调用验证操作密码接口）
     * oper_pwd md5后的操作密码
     * tg_name 通讯机名
     *  tag 标签名
     *  val 标签值
     *  behavior_type 操作类型
     * detailed_type 操作行为
     */
    @ApiOperation(value = "下置并记录操作日志（需先调用验证操作密码接口）", notes = "下置并记录操作日志（需先调用验证操作密码接口）", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = String.class,key = "factory_id", example = "36", description = "厂区"),
            @ApiJsonProperty(type = String.class,key = "verify_str", example = "d0e1e2dc9473cdb301f7e97de3bda3cd", description = "验证操作密码接口的返回值"),
            @ApiJsonProperty(type = String.class,key = "tg_name", example = "TG320", description = "通讯机名"),
            @ApiJsonProperty(type = String.class,key = "tag", example = "a1_b1_di", description = "标签名"),
            @ApiJsonProperty(type = String.class,key = "val", example = "0", description = "表状态"),
            @ApiJsonProperty(type = String.class,key = "behavior_type", example = "elec_control", description = "操作类型air_conditioning_control/light_control/elec_control/water_control/door_control/cooling_system_control"),
            @ApiJsonProperty(type = String.class,key = "detailed_type", example = "开", description = "具体行为;开/关/温度控制/灯光控制")
    })
    @PostMapping("set_xuansi_val")
    @ResponseBody
    public WebResult setXuansiVal(HttpServletRequest request, @RequestAttribute Map<String, Object> map){
        return commonService.setXuansiVal(request, map);
    }


    /**
     * 下置并记录操作日志（不需要验证操作密码）
     * oper_pwd md5后的操作密码
     * tg_name 通讯机名
     *  tag 标签名
     *  val 标签值
     *  behavior_type 操作类型
     * detailed_type 操作行为
     */
    @ApiOperation(value = "下置并记录操作日志（不需要验证操作密码）", notes = "下置并记录操作日志（不需要验证操作密码）", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = String.class,key = "factory_id", example = "36", description = "厂区"),
            @ApiJsonProperty(type = String.class,key = "tg_name", example = "TG320", description = "通讯机名"),
            @ApiJsonProperty(type = String.class,key = "tag", example = "a1_b1_di", description = "标签名"),
            @ApiJsonProperty(type = String.class,key = "val", example = "0", description = "表状态"),
            @ApiJsonProperty(type = String.class,key = "behavior_type", example = "elec_control", description = "操作类型air_conditioning_control/light_control/elec_control/water_control/door_control/cooling_system_control"),
            @ApiJsonProperty(type = String.class,key = "detailed_type", example = "开", description = "具体行为;开/关/温度控制/灯光控制")
    })
    @PostMapping("set_xuansi_val2")
    @ResponseBody
    public WebResult setXuansiVal2(HttpServletRequest request, @RequestAttribute Map<String, Object> map){
        return commonService.setXuansiVal2(request, map);
    }
    /**
     * 验证操作密码并下置并记录操作日志
     * oper_pwd md5后的操作密码
     * tg_name 通讯机名
     *  tag 标签名
     *  val 标签值
     *  behavior_type 操作类型
     * detailed_type 操作行为
     */
    @ApiOperation(value = "验证操作密码并下置并记录操作日志", notes = "验证操作密码并下置并记录操作日志", httpMethod = "POST")
    @ApiJsonObject(name = "params", value = {
            @ApiJsonProperty(type = String.class,key = "factory_id", example = "36", description = "厂区"),
            @ApiJsonProperty(type = String.class,key = "oper_pwd", example = "d0e1e2dc9473cdb301f7e97de3bda3cd", description = "操作密码"),
            @ApiJsonProperty(type = String.class,key = "tg_name", example = "TG320", description = "通讯机名"),
            @ApiJsonProperty(type = String.class,key = "tag", example = "a1_b1_di", description = "标签名"),
            @ApiJsonProperty(type = String.class,key = "val", example = "0", description = "表状态"),
            @ApiJsonProperty(type = String.class,key = "behavior_type", example = "elec_control", description = "操作类型air_conditioning_control/light_control/elec_control/water_control/door_control/cooling_system_control"),
            @ApiJsonProperty(type = String.class,key = "detailed_type", example = "开", description = "具体行为;开/关/温度控制/灯光控制")
    })
    @PostMapping("setValue")
    @ResponseBody
    public WebResult setValue(HttpServletRequest request, @RequestAttribute Map<String, Object> map){
        return commonService.setValue(request, map);
    }


}
