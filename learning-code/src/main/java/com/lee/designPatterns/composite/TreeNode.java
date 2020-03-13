package com.lee.designPatterns.composite;

import java.util.Enumeration;
import java.util.Vector;

public class TreeNode {

    private String name;
    private TreeNode parent;
    private Vector<TreeNode> children=new Vector<TreeNode>();

    public TreeNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public TreeNode getParent() {
        return parent;
    }

    public Enumeration<TreeNode> getChildren() {
        return children.elements();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public void addChildren(TreeNode children) {
        this.children.add(children);
        children.setParent(this);
    }

    public void removeChildren(TreeNode children) {
        this.children.remove(children);
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "name='" + name + '\'' +
                ", parent=" + parent.name +
                ", children=" + children +
                '}';
    }
}
