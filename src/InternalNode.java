
import java.util.ArrayList;
import java.util.List;


public class InternalNode extends Node{
	
	/* In order to describe an internal
	 * node of the tree we need the Test
	 * for this node, which depends on the 
	 * attribute type and the children nodes.
	 */
	private Test test;

	private	List<Node> children;
	
	public InternalNode(){
		children = new ArrayList<Node>();
	}
	
	public List<Node> getChildren(){
		return new ArrayList<Node>(children);
	}
	public void addChild(Node child){
		if(!this.children.contains(child)){
			this.children.add(child);
		}
	}
	
	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}
}

