package com.sl.idripweb.entity.alerm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryRelationTree {

private String transformerroom_id;
private String id;
private String text;  //名称
private String parentId; //父级ID
private List<CategoryRelationTree> nodes;  //子节点
private String device_id;  //设备id


public String getTransformerroom_id() {
	return transformerroom_id;
}
public void setTransformerroom_id(String transformerroom_id) {
	this.transformerroom_id = transformerroom_id;
}
public String getDevice_id() {
	return device_id;
}
public void setDevice_id(String device_id) {
	this.device_id = device_id;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getParentId() { return parentId; }
public void setParentId(String parentId) {
this.parentId = parentId;
}
public String getText() {
	return text;
}
public void setText(String text) {
	this.text = text;
}
public List<CategoryRelationTree> getNodes() {
	return nodes;
}
public void setNodes(List<CategoryRelationTree> nodes) { this.nodes = nodes; }




	/**
	 * 递归生成Tree结构数据
	 * @param rootId 根id
	 * @param treeList 树的全部数据
	 * @return
	 */
	public List<CategoryRelationTree> getCategoryRelationTreeByRecursion(String rootId, List<CategoryRelationTree> treeList){
		//获取子节点
		List<CategoryRelationTree> childs = this.getChildrenNodeById(rootId,treeList);
		for (CategoryRelationTree item : childs) {
			List<CategoryRelationTree> node = this.getCategoryRelationTreeByRecursion(item.getId(),treeList);
			if (node.size()!=0) { //不是叶子节点
				item.setNodes(node);
			}
		}
		return childs;
	}

	/**
	 *查询cid下的所有子节点
	 * @param nodeId
	 * @return
	 */
	public List<CategoryRelationTree> getChildrenNodeById(String nodeId,List<CategoryRelationTree> treeList){
		List<CategoryRelationTree> childs = new ArrayList<>();
		for (CategoryRelationTree item : treeList) {
			if(item.getParentId().equals(nodeId)){
				childs.add(item);
			}
		}
		return childs;
	}

@Override
public String toString() {
	return "CategoryRelationTree [transformerroom_id=" + transformerroom_id + ", id=" + id + ", text=" + text + ", parentId="
			+ parentId + ", nodes=" + nodes + ", device_id=" + device_id + "]";
}

}
