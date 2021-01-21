package com.sl.idripweb.dao;

import com.sl.idripweb.entity.alerm.CategoryRelationTree;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AlermConfigDao{
    List<Map<String,Object>> queryList(Map<String,Object> map);

    List<String> getWaterTagDescByName(@Param("tag_name") String tag_name);
    List<String> getElecTagDescByName(@Param("tag_name") String tag_name);

    Integer queryListCount(Map<String,Object> map);
    List<Map<String,Object>> getCategoryRelation(Map<String,Object> map);
    //报警类型
    List<Map<String,Object>> getAlermType(Map<String, Object> map);

    //报警配置 设备树
    List<CategoryRelationTree> getCategoryRelationTreeElec(Map<String, Object> map);
    List<CategoryRelationTree> getCategoryRelationTreeWater(Map<String, Object> map);
    List<CategoryRelationTree> getCategoryRelationTreeGas(Map<String, Object> map);
    List<CategoryRelationTree> getCategoryRelationTreeDetector(Map<String, Object> map);
    List<CategoryRelationTree> getCategoryRelationTreeWaterBusiness(Map<String, Object> map);
    List<CategoryRelationTree> getCategoryRelationTreeElecBusiness(Map<String, Object> map);

    //获取设备的标签
    List<Map<String, Object>> getTagByRelationIdsElec(Map<String, Object> map);
    List<Map<String, Object>> getTagByRelationIdsWater(Map<String, Object> map);
    List<Map<String, Object>> getTagByRelationIdsGas(Map<String, Object> map);
    List<Map<String, Object>> getTagByRelationIdsFireDetector(Map<String, Object> map);

    List<Map<String, Object>> getTagByRelationIdsWaterBusiness(Map<String, Object> map);
    List<Map<String, Object>> getTagByRelationIdsElecBusiness(Map<String, Object> map);

    List<Map<String, Object>> getTagByRelationIdsImmersionDetector(Map<String, Object> map);
    List<Map<String, Object>> getTagByRelationIdsSmokeDetector(Map<String, Object> map);

    //增删改
    int save(Map<String, Object> map);
    int update(Map<String, Object> map);
    int delete(Map<String, Object> map);

    //判断是否已经存在
    int selectIsExist(Map<String, Object> map);
    int selectIsExistBusiness(Map<String, Object> map);
    List<Map<String, Object>> getTgidByRelationId(Map<String, Object> map);

}
