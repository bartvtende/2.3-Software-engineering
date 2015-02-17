import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Builds a tree and prints the orderings
 * 
 * @author Bart van 't Ende and Jan-Bert van Slochteren
 * @version 1.0
 */
public class Tree {
	
	private DefaultMutableTreeNode tree;

	public static void main(String[] args) {
		Tree treeInstance = new Tree();
		treeInstance.tree = treeInstance.makeTree();
		treeInstance.printOrders();
	}
	
	/**
	 * Builds a tree
	 * 
	 * @return DefaultMutableTreeNode tree
	 */
	public DefaultMutableTreeNode makeTree() {
		// Make the root item
		DefaultMutableTreeNode person = new DefaultMutableTreeNode("Person");
		// Make the childs
		DefaultMutableTreeNode employee = makeTreeNode("Employee", person);
		DefaultMutableTreeNode customer = makeTreeNode("Customer", person);
		makeTreeNode("Sales_rep", employee);
		makeTreeNode("Engineer", employee);
		DefaultMutableTreeNode us_customer = makeTreeNode("US_customer", customer);
		makeTreeNode("Local_customers", us_customer);
		makeTreeNode("Regional_customers", us_customer);
		makeTreeNode("Non_US_customer", customer);
		
		return person;
	}
	
	/**
	 * Prints the breadth, preorder and postorders of the tree
	 * 
	 */
	public void printOrders() {
		// Breedte-ordening
		System.out.println("Breedte-ordening:");
		Enumeration e1 = this.tree.breadthFirstEnumeration();
		while (e1.hasMoreElements()) {
			System.out.println(e1.nextElement());
		}
		// Pre-ordening
		System.out.println("Pre-ordening:");
		Enumeration e2 = this.tree.preorderEnumeration();
		while (e2.hasMoreElements()) {
			System.out.println(e2.nextElement());
		}
		// Post-ordening
		System.out.println("Post-ordening:");
		Enumeration e3 = this.tree.postorderEnumeration();
		while (e3.hasMoreElements()) {
			System.out.println(e3.nextElement());
		}
	}
	
	/**
	 * Helper function for creating tree nodes
	 * 
	 * @param title
	 * @param parent
	 * @return DefaultMutableTreeNode item
	 */
	private DefaultMutableTreeNode makeTreeNode(String title, DefaultMutableTreeNode parent) {
		DefaultMutableTreeNode item = new DefaultMutableTreeNode(title);
		parent.add(item);
		return item;
	}

}
