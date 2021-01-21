package com.sl.common.entity;

import java.util.ArrayList;
import java.util.List;

public class CategoryTree {
   private String category_id;
   private String parentCategoryId;
   private String text;
   private List<CategoryTree> nodes;

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(String parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<CategoryTree> getNodes() {
		return nodes;
	}

	public void setNodes(List<CategoryTree> nodes) {
		this.nodes = nodes;
	}



	/**
	 * 递归生成Tree结构数据
	 * @param rootId 根id
	 * @param treeList 树的全部数据
	 * @return
	 */
	public List<CategoryTree> getCategoryTreeByRecursion(String rootId, List<CategoryTree> treeList){
		//所有子节点
		List<CategoryTree> childs = this.getChildrenNodeById(rootId,treeList);
		//能进行for 证明有子节点 --否则为叶子节点 返回叶子节点
		for (CategoryTree item : childs) {
			List<CategoryTree> node = this.getCategoryTreeByRecursion(item.getCategory_id(),treeList);
			if (node.size()!=0){ //不是叶子节点
				item.setNodes(node);  //存子节点
			}else{
				item.setNodes(new ArrayList<>());
			}
		}
		return childs;
	}

	/**
	 *查询cid下的所有子节点
	 * @param nodeId
	 * @return
	 */
	public List<CategoryTree> getChildrenNodeById(String nodeId,List<CategoryTree> treeList){
		List<CategoryTree> childs = new ArrayList<>();
		for (CategoryTree item : treeList) {
			if(nodeId.equals(item.getParentCategoryId())){
				childs.add(item);
			}
		}
		return childs;
	}

}
