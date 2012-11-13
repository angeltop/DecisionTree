import java.util.ArrayList;
import java.util.List;


public class InternalNode extends Node{
	
	private Attribute testAttribute;

	private	List<Node> children;
	
	public List<Node> getChildren(){
		return new ArrayList(children);
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
}
