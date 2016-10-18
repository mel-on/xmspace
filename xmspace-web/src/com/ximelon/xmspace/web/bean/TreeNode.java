package com.ximelon.xmspace.web.bean;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
	
	String id;
	String circuitId;
	String name;
	String type;
	String icon;
	boolean checked;
	boolean isParent;
	boolean nocheck;
	boolean open;
	String iconSkin;
	List<TreeNode> children;
	String pid;
	
	public TreeNode() {
		isParent = false;
		checked = false;
		children = new ArrayList<TreeNode>();
	}
	
	public boolean isNocheck() {
		return nocheck;
	}
	
	public TreeNode setNocheck(boolean nocheck) {
		this.nocheck = nocheck;
		return this;
	}
	public String getId() {
		return id;
	}
	public TreeNode setId(String id) {
		this.id = id;
		return this;
	}
	public String getName() {
		return name;
	}
	public TreeNode setName(String name) {
		this.name = name;
		return this;
	}
	public String getType() {
		return type;
	}
	public TreeNode setType(String type) {
		this.type = type;
		return this;
	}
	public String getIcon() {
		return icon;
	}
	public TreeNode setIcon(String icon) {
		this.icon = icon;
		return this;
	}
	public boolean getIsParent() {
		return isParent;
	}
	public TreeNode setIsParent(boolean isParent) {
		this.isParent = isParent;
		return this;
	}
	public List<TreeNode> getChildren() {
		return children;
	}
	
	public TreeNode addChildren(TreeNode node){
		if(node!=null){
			children.add(node);
		}
		return this;
	}
	public TreeNode newChildren(){
		TreeNode node=new TreeNode();
		children.add(node);
		return node;
	}
	
	public TreeNode setChildren(List<TreeNode> nodes) {
		this.children = nodes;
		return this;
	}

	public TreeNode setChecked(boolean checked) {
		this.checked = checked;
		return this;
	}
	public boolean isChecked() {
		return checked;
	}


	public boolean isOpen() {
		return open;
	}

	public TreeNode setOpen(boolean open) {
		this.open = open;
		return this;
	}

	public String getIconSkin() {
		return iconSkin;
	}

	public TreeNode setIconSkin(String iconSkin) {
		this.iconSkin = iconSkin;
		return this;
	}

	public TreeNode setParent(boolean isParent) {
		this.isParent = isParent;
		return this;
	}

	public String getCircuitId() {
		return circuitId;
	}

	public TreeNode setCircuitId(String circuitId) {
		this.circuitId = circuitId;
		return this;
	}

	public String getPid() {
		return pid;
	}

	public TreeNode setPid(String pid) {
		this.pid = pid;
		return this;
	}
	
	public List<TreeNode> descendants() {
		List<TreeNode> list = new ArrayList<TreeNode>();
		loadDescendants(this, list);
		return list;
		
	}
	
	private void loadDescendants(TreeNode ancestor, List<TreeNode> descendants) {
		List<TreeNode> children = ancestor.getChildren();
		if(children!=null && children.size()>0) {
			//descendants.addAll(children);
			for(TreeNode child : children) {
				descendants.add(child);
				loadDescendants(child, descendants);
			}
		}
	}
	
	public List<TreeNode> selfAndDescendants() {
		List<TreeNode> list = new ArrayList<TreeNode>();
		list.add(this);
		list.addAll(descendants());
		return list;
	}
}
