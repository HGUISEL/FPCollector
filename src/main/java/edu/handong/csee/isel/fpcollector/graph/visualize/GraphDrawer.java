package edu.handong.csee.isel.fpcollector.graph.visualize;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.*;

import org.eclipse.jdt.core.dom.SimpleName;

import edu.handong.csee.isel.fpcollector.graph.ControlNode;
import edu.handong.csee.isel.fpcollector.graph.ControlState;
import edu.handong.csee.isel.fpcollector.graph.DataNode;
import edu.handong.csee.isel.fpcollector.graph.GraphNode;

public class GraphDrawer extends JFrame{
	ControlNode root;
	
	public GraphDrawer() {
		super("Jtree");
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 800);
	}
	
	private void makeTree(ControlNode n, Node root) {
		System.out.println(n.state);
		if (n.state != ControlState.E) {
			for (GraphNode n_ : n.nexts) {
				if (n_ instanceof DataNode) {
					root.add(n_.node.getClass().getSimpleName() 
							+ " (state: " + ((DataNode)n_).state + ", " 
							+ ((DataNode)n_).inCondition + ", " 
							+ ((DataNode)n_).type +  ")");
					
//					if(n_.node instanceof SimpleName)
//						System.out.println("level: " + n_.level + "(D) n: " + n_.node.getClass().getSimpleName() + "( "+ n_.node +" )" +  ", state : " + ((DataNode)n_).state + " " + ((DataNode)n_).inCondition + " " + ((DataNode)n_).type + " " + ((DataNode)n_).from);
//					else
//						System.out.println("level: " + n_.level + "(D) n: " + n_.node.getClass().getSimpleName()  + ", state : " + ((DataNode)n_).state + " " + ((DataNode)n_).inCondition + " " + ((DataNode)n_).from);
				} else {
					Node subRoot = new Node(n_.node.getClass().getSimpleName() 
									+ " (state: " + ((ControlNode)n_).state + ", " 
									+ ((ControlNode)n_).property +  ")");
					
					makeTree((ControlNode)n_, subRoot);
					root.add(subRoot);

//					for(int k = 0 ; k < n_.level; k ++) {
//						System.out.printf("\t");
//					}
//					System.out.println("level: " + n_.level + "(C) n: " + n_.node.getClass().getSimpleName() + /*"( "+ ("" + n_.node).split("\n")[0] +" )" +*/ ", state : " + ((ControlNode)n_).state + " " + ((ControlNode)n_).property);
				}
			}
		}
	}
	
	public void run(ControlNode r, int name) {
        Node root = new Node("Method");
        
        makeTree(r, root);

        JTree tree = new JTree(root);
        
        this.add(tree);
 
        //Expanding rows
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
        
        setVisible(true);
        
        saveImage(tree, name);
	}
	
	private void saveImage(JTree tree, int name) {
        BufferedImage image = new BufferedImage(tree.getWidth(), tree.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        tree.paint(g);
        g.dispose();
 
        // File to save output Image
        File newDir = new File("graphImage");
		if(!newDir.exists()) {
			newDir.mkdir();
		}
		
        File imageOut = new File("graphImage/" + name + ".jpg");
        try {
            ImageIO.write(image, "jpg", imageOut);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
	
	private class Node extends DefaultMutableTreeNode {
        public Node(String s) {
        	super(s);
        }
        
        
        public Node add(String... strs) {
            for (String s : strs) {
                super.add(new Node(s));
            }
            
            return this;
        }
        
        public Node add(Node... ns) {
            for (Node n : ns) {
                super.add(n);
            }
            
            return this;
        }
    }
}
