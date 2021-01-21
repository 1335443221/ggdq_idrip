package com.sl.idripweb.entity.elecAnalysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Author: 李旭日
 * Date: 2020/11/16 14:57
 * FileName: HistoryData
 * Description: ${DESCRIPTION}
 */
public class CategoryMeterData {
    private int categoryId;
    private int parentCategoryId;
    private String categoryName;
    private double power;
    private String detail;
    private List<CategoryMeterData> childs;
    private List<String> detailList;  //所有子节点详情合集



    /**
     * 递归生成Tree结构数据
     * @param rootId 根id
     * @param treeList 树的全部数据
     * @return
     */
    public List<CategoryMeterData> getCategoryMeterDataByRecursion(int rootId, List<CategoryMeterData> treeList){
        //所有子节点
        List<CategoryMeterData> childs = this.getChildrenNodeById(rootId,treeList);
        //能进行for 证明有子节点 --否则为叶子节点 返回叶子节点
        for (CategoryMeterData item : childs) {
            List<CategoryMeterData> node = this.getCategoryMeterDataByRecursion(item.getCategoryId(),treeList);
            if (node.size()!=0){ //不是叶子节点
                item.setChilds(node);  //存子节点
                double power=0;
                List<String> detailList=new ArrayList<>();
                for (CategoryMeterData rootItem : node){
                    power+=rootItem.getPower();  //电量累加
                    detailList.addAll(rootItem.getDetailList());  //详情累加
                }
                item.setPower(power);  //累加电量
                item.setDetailList(detailList);  //累加详情
            }else{ //叶子节点 把详情存到list中
                List<String> detailList=new ArrayList<>();
                detailList.add(item.getDetail());
                item.setDetailList(detailList);
            }
        }
        return childs;
    }

    /**
     *查询cid下的所有子节点
     * @param nodeId
     * @return
     */
    public List<CategoryMeterData> getChildrenNodeById(int nodeId,List<CategoryMeterData> treeList){
        List<CategoryMeterData> childs = new ArrayList<>();
        for (CategoryMeterData item : treeList) {
            if(item.getParentCategoryId() == nodeId){
                childs.add(item);
            }
        }
        return childs;
    }


    /**
     *根据cid获取节点对象
     * @param nodeId
     * @return
     */
    public CategoryMeterData getNodeById(int nodeId,List<CategoryMeterData> treeList){
        CategoryMeterData treeNode = new CategoryMeterData();
        for (CategoryMeterData item : treeList) {
            if (item.getCategoryId() == nodeId) {
                treeNode = item;
                break;
            }
        }
        return treeNode;
    }


    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(int parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<CategoryMeterData> getChilds() {
        return childs;
    }

    public void setChilds(List<CategoryMeterData> childs) {
        this.childs = childs;
    }

    public List<String> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<String> detailList) {
        this.detailList = detailList;
    }
}
