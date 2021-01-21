package com.sl.idripweb.service.impl;

import com.sl.common.utils.SetParams;
import com.sl.common.utils.WebResult;
import com.sl.idripweb.dao.AlermConfigDao;
import com.sl.idripweb.entity.alerm.CategoryRelationTree;
import com.sl.idripweb.service.AlermConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("alermConfigService")
public class AlermConfigServiceImpl implements AlermConfigService {

    @Autowired
    private AlermConfigDao alermConfigDao;

    /**
     * 查看数据
     * @param map
     * @return
     */
    @Override
    public WebResult query(Map<String, Object> map) {
        //分页  默认20页
        SetParams.setPagParam(map);
        //返回数据+数据条数
        LinkedHashMap<String,Object> result=new LinkedHashMap<>(2);
        result.put("total", alermConfigDao.queryListCount(map));

        List<Map<String,Object>> data=alermConfigDao.queryList(map);
        for (int i=0;i<data.size();i++){
            //处理 商户的标签
            if (data.get(i).get("code_use_name")!=null&&data.get(i).get("code_use_name").toString().equals("water_business")){
                if (data.get(i).get("tag")!=null) {
                    List<String> tagList = alermConfigDao.getWaterTagDescByName(data.get(i).get("tag").toString());
                    if (tagList.size() > 0) {
                        data.get(i).put("tag_name", tagList.get(0));
                    }
                }

            }
            if (data.get(i).get("code_use_name")!=null&&data.get(i).get("code_use_name").toString().equals("elec_business")){
                if (data.get(i).get("tag")!=null){
                    List<String> tagList= alermConfigDao.getElecTagDescByName(data.get(i).get("tag").toString());
                    if (tagList.size()>0){
                        data.get(i).put("tag_name",tagList.get(0));
                    }
                }
            }
        }
        result.put("data", data);
        return WebResult.success(result);
    }
    /**
     * 大类
     * @param map
     * @return
     */
    @Override
    public WebResult getAlermType(Map<String, Object> map) {
        List<Map<String, Object>> result = alermConfigDao.getAlermType(map);
        return WebResult.success(result);
    }

    /**
     * 报警分类关系
     * @param map
     * @return
     */
    @Override
    public WebResult getCategoryRelation(Map<String, Object> map) {
        ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
        List<Map<String, Object>> CateGoryRealtion = alermConfigDao.getCategoryRelation(map);
        //处理分类关系
        if(CateGoryRealtion!=null&&CateGoryRealtion.size()>0){
            ArrayList<String> tmpList = new ArrayList<String>();
            for(int i=0;i<CateGoryRealtion.size();i++){
                if(CateGoryRealtion.get(i)!=null&&CateGoryRealtion.get(i).get("cate_name3")!=null){
                    ArrayList<Object> list1 = new ArrayList<Object>();
                    String c3name = CateGoryRealtion.get(i).get("cate_name3").toString();
                    String c3id = CateGoryRealtion.get(i).get("c3id").toString();
                    for(int j=0;j<CateGoryRealtion.size();j++){
                        String id = CateGoryRealtion.get(j).get("id").toString();
                        String c3name1 = CateGoryRealtion.get(j).get("cate_name3").toString();
                        String category_name = CateGoryRealtion.get(j).get("category_name").toString();
                        if(!tmpList.contains(c3name)&&c3name.equals(c3name1)){
                            HashMap<String, Object> myMap = new HashMap<>();
                            myMap.put("name",category_name);
                            myMap.put("id",id);
                            list1.add(myMap);
                        }
                    }
                    if(!tmpList.contains(c3name)){
                        HashMap<String, Object> c3Map1 = new HashMap<>();
                        c3Map1.put("name", c3name);
                        c3Map1.put("id", c3id);
                        c3Map1.put("nodes", list1);
                        result.add(c3Map1);
                        tmpList.add(c3name);
                    }
                }
            }
        }
        return WebResult.success(result);
    }

    /**
     * 设备标签树
     * @param map
     * @return
     */
    @Override
    public WebResult getRoomDeviceTagTree(Map<String, Object> map) {
        List<Map<String, Object>> alermType = alermConfigDao.getAlermType(map);
        //选择的报警大类
        String type_English_name = "";
        if (alermType != null && alermType.size() > 0){
            type_English_name = String.valueOf(alermType.get(0).get("type_English_name"));
        }
        CategoryRelationTree categoryRelationTree=new CategoryRelationTree();
        List<CategoryRelationTree> treeList =new ArrayList<>();
        switch (type_English_name) {
            case "elec":  //电
                treeList = alermConfigDao.getCategoryRelationTreeElec(map);
                break;
            case "water":  //水
                treeList = alermConfigDao.getCategoryRelationTreeWater(map);
                break;
            case "gas":  //气
                treeList = alermConfigDao.getCategoryRelationTreeGas(map);
                break;
            case "smoke_detector":  //烟感
                map.put("tableName","smoke_detector"); //查询表
                treeList =alermConfigDao.getCategoryRelationTreeDetector(map);
                break;
            case "immersion_detector":  //浸水
                map.put("tableName","immersion_detector"); //查询表
                treeList =alermConfigDao.getCategoryRelationTreeDetector(map);
                break;
            case "fire":  //电气火灾
                map.put("tableName","fire_detector"); //查询表
                treeList =alermConfigDao.getCategoryRelationTreeDetector(map);
                break;
            case "water_business":  //用水量
                treeList =alermConfigDao.getCategoryRelationTreeWaterBusiness(map);
                break;
            case "elec_business":  //用电量
                treeList =alermConfigDao.getCategoryRelationTreeElecBusiness(map);
                break;
            default:
                break;
        }
        List<CategoryRelationTree> resultList =categoryRelationTree.getCategoryRelationTreeByRecursion("0",treeList);
        return WebResult.success(resultList);
    }

    /**
     * 获取标签
     * @param map
     * @return
     */
    @Override
    public WebResult getTagByDeviceId(Map<String, Object> map) {
        List<Map<String, Object>> alermType = alermConfigDao.getAlermType(map);
        //选择的报警大类
        String type_English_name = "";
        if (alermType != null && alermType.size() > 0){
            type_English_name = String.valueOf(alermType.get(0).get("type_English_name"));
        }
        String relationIds = String.valueOf(map.get("device_id"));
        List<String> asList = Arrays.asList(relationIds.split(","));
        map.put("list", asList);  //device_type_relation_id 集合
        map.put("length", asList.size());  //公共标签 需要判断
       List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        switch (type_English_name) {
            case "elec":  //电
                result = alermConfigDao.getTagByRelationIdsElec(map);
                break;
            case "water":  //水
                result = alermConfigDao.getTagByRelationIdsWater(map);
                break;
            case "gas":  //气
                result = alermConfigDao.getTagByRelationIdsGas(map);
                break;
            case "smoke_detector":  //烟感
                result = alermConfigDao.getTagByRelationIdsSmokeDetector(map);
                break;
            case "immersion_detector":  //浸水
                result = alermConfigDao.getTagByRelationIdsImmersionDetector(map);
                break;
            case "fire":  //电气火灾
                result = alermConfigDao.getTagByRelationIdsFireDetector(map);
                break;
            case "water_business":  //用水量
                result = alermConfigDao.getTagByRelationIdsWaterBusiness(map);
                break;
            case "elec_business":  //用电量
                result = alermConfigDao.getTagByRelationIdsElecBusiness(map);
                break;
            default:
                break;
        }
        return WebResult.success(result);
    }

    /**
     * 保存
     * @param map
     * @return
     */
    @Override
    public WebResult save(Map<String, Object> map) {
        //报警类型的英文名
        List<Map<String, Object>> alermType = alermConfigDao.getAlermType(map);
        String device_type = String.valueOf(alermType.get(0).get("type_English_name"));

        //设备id列表
        List<String> deviceList = Arrays.asList(map.get("device_id").toString().split(","));
        //检测是否已经存在
        Map<String,Object> params=new HashMap<>();
        params.put("tag", map.get("tag"));
        params.put("list", deviceList);
        int count=0;
        if("elec_business".equals(device_type)||"water_business".equals(device_type)){
            count = alermConfigDao.selectIsExistBusiness(params);
        }else{
            count = alermConfigDao.selectIsExist(params);
        }
        if (count>0){
            return WebResult.error(501);
        }
        map.put("code_use_name", device_type);
        int result=0;
        for (int i=0;i<deviceList.size();i++){
            map.put("device_id",deviceList.get(i));
            //获取通讯机id tg_id
            List<Map<String, Object>> tgList = alermConfigDao.getTgidByRelationId(map);
            if (tgList.size()>0){
                map.put("tg_id", tgList.get(0).get("tg_id").toString());
            }
            result = alermConfigDao.save(map);
        }
        if (result>0){
            return WebResult.success();
        }else{
            return WebResult.error(201);
        }
    }

    /**
     * 修改
     * @param map
     * @return
     */
    @Override
    public WebResult update(Map<String, Object> map) {
        int result = alermConfigDao.update(map);
        if (result>0){
            return WebResult.success();
        }else{
            return WebResult.error(201);
        }
    }



    /**
     * 删除
     * @param map
     * @return
     */
    @Override
    public WebResult delete(Map<String, Object> map) {
       int result= alermConfigDao.delete(map);
       if (result>0){
           return WebResult.success();
       }else{
           return WebResult.error(201);
       }
    }
}
