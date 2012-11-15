
import java.util.ArrayList;
import java.util.List;


public class InternalNode extends Node{
	
	private TestValue test;

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
	
	public TestValue getTest() {
		return test;
	}

	public void setTest(TestValue test) {
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

