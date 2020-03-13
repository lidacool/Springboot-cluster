package com.lee.designPatterns.composite;

import java.util.Enumeration;

public class Tree {

    private TreeNode root=null;

    public Tree(String name) {
        this.root = new TreeNode(name);
    }

    public static void main(String[] args) {
        Tree tree=new Tree("A");
        TreeNode chrildrenLeft=new TreeNode("B");
        TreeNode chrildrenRight=new TreeNode("C");

        TreeNode grundChrildren=new TreeNode("D");

        chrildrenLeft.addChildren(grundChrildren);
        tree.root.addChildren(chrildrenLeft);
        tree.root.addChildren(chrildrenRight);
        Enumeration<TreeNode> chridren= tree.root.getChildren();
        while (chridren.hasMoreElements())
            System.out.println(chridren.nextElement().toString());
    }
}
