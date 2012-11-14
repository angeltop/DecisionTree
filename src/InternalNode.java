
import java.util.ArrayList;
import java.util.List;


public class InternalNode extends Node{
	
	private Attribute testAttribute;

	private	List<Node> children;
	
	public InternalNode(){
		children = new ArrayList<Node>();
	}
	
	public List<Node> getChildren(){
		return new ArrayList<Node>(children);
	}
	public Attribute getTestAttribute(){
		return testAttribute;
	}
	public void setAttribute(Attribute a){
		testAttribute = a;
	}
	public void addChild(Node child){
		if(!this.children.contains(child)){
			this.children.add(child);
		}
	}
	
	@Override
	public boolean isLeaf() {
		return false;
	}
}

