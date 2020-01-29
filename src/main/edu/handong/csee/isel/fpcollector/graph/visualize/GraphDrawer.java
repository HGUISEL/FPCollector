//package edu.handong.csee.isel.fpcollector.graph.visualize;
//
//import java.awt.*;
//import javax.swing.*;
//import javax.swing.tree.*;
//
//import org.eclipse.jdt.core.dom.SimpleName;
//
//import edu.handong.csee.isel.fpcollector.graph.ControlNode;
//import edu.handong.csee.isel.fpcollector.graph.DataNode;
//import edu.handong.csee.isel.fpcollector.graph.GraphNode;
//
//public class GraphDrawer extends JFrame{
//	ControlNode root;
//	
//	public GraphDrawer() {
//		super("Jtree");
//	}
//	
//	private void makeTree(ControlNode n, Node root) {
//		for (GraphNode n_ : n.nexts) {
//			if (n_ instanceof DataNode) {
//				root.add(n_.node.getClass().getSimpleName() 
//						+ "(state: " + ((DataNode)n_).state + ", " + ((DataNode)n_).inCondition + ", " + ((DataNode)n_).type +  ")", "Sepcialization");
//				
//				if(n_.node instanceof SimpleName)
//					System.out.println("level: " + n_.level + "(D) n: " + n_.node.getClass().getSimpleName() + "( "+ n_.node +" )" +  ", state : " + ((DataNode)n_).state + " " + ((DataNode)n_).inCondition + " " + ((DataNode)n_).type + " " + ((DataNode)n_).from);
//				else
//					System.out.println("level: " + n_.level + "(D) n: " + n_.node.getClass().getSimpleName()  + ", state : " + ((DataNode)n_).state + " " + ((DataNode)n_).inCondition + " " + ((DataNode)n_).from);
//			}
//			else {
//				for(int k = 0 ; k < n_.level; k ++) {
//					System.out.printf("\t");
//				}
//				System.out.println("level: " + n_.level + "(C) n: " + n_.node.getClass().getSimpleName() + /*"( "+ ("" + n_.node).split("\n")[0] +" )" +*/ ", state : " + ((ControlNode)n_).state + " " + ((ControlNode)n_).property);
//			}
//			if (n_ instanceof ControlNode) {
//				printChildren((ControlNode)n_);
//			}
//		}
//	}
//	
//	public void run(ControlNode r) {
//        Container content = getContentPane();
//        Node root = new Node("Method");
//        
//        
//        
//        Node children = new Node("")
//        
//        
//
//        N weapons = new N("Weapons").add(
//            new N("One-handed").add(
//                new N("Sword").add("Proficiency", "Specialization"), 
//                new N("Mace").add("Proficiency")),
//            new N("Bow").add("Proficiency"));
//        root.add(weapons);
//
//        N phys = new N("Physical & Mental").add(
//            new N("Life"),
//            new N("Strength").add(
//                "Double", "Triple", "Quadruple"));
//
//        root.add(phys);
//        N med = new N("Medical");
//        med.add(new N("Bind Wounds"));
//        med.add(new N("Set Broken Bones"));
//        root.add(med);
//
//        JTree tree = new JTree(root);
//        content.add(new JScrollPane(tree), BorderLayout.CENTER);
//        setSize(275, 300);
//        setVisible(true);
//	}
//	
//	private class Node extends DefaultMutableTreeNode {
//        public Node(String s) {
//        	super(s);
//        }
//        
//        
//        public Node add(String... strs) {
//            for (String s : strs) {
//                super.add(new Node(s));
//            }
//            
//            return this;
//        }
//        
//        public Node add(Node... ns) {
//            for (Node n : ns) {
//                super.add(n);
//            }
//            
//            return this;
//        }
//    }
//}
