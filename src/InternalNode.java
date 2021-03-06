
import java.io.FileWriter;
import java.io.IOException;
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
		super.setTestValue(null);
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
	

	/* This function creates the line that describes the attributes of the given node
	 * we call this function recursively in order to access all nodes. As id we give the id of the 
	 * current node and and as nextAvailable id we give the number that is available for the
	 * next node without id.
	 */
	@Override
	public boolean isLeaf() {
		return false;
	}
	public int printNode(FileWriter f, int id, int nextAvailableId) throws IOException{
		String outputText;
		/* we have to print the id, the the parent test, the attribute test and the
		 * children ids
		 */
						 
		if(this.getTestValue()==null)
			outputText = Integer.toString(id) + "\t"+ "root"+ "\t"+ this.getTest().printTest()+"\t";
		else
			outputText = Integer.toString(id) + "\t"+ this.getTestValue().printTestValue()+ "\t"+ this.getTest().printTest()+"\t";
		int children = (this).getChildren().size();
		int givenId =nextAvailableId; 
		int[] givenIds = new int[children];
		int count=0;
		while(givenId< nextAvailableId+children){	// print the children ids
			outputText = outputText + " "+ Integer.toString(givenId);
			givenIds[count] = givenId;	// we keep the children ids in a local matrix, we will use them
			count++;					// when we will print the children info
			givenId ++;
		}
		outputText = outputText+"\n";
		f.write(outputText);
		nextAvailableId = givenId;
			
		for(int i =0; i<children; i++){ // iterate in children and print its info
			nextAvailableId = this.getChildren().get(i).printNode(f,givenIds[i], nextAvailableId);
		}
		return nextAvailableId;
	}

	@Override
	public void prettyPrint(
			int i) {
		int k, c2=this.children.size() / 2;
		for(k=0; k<c2;k++){
			this.children.get(k).prettyPrint(i+2);
		}
		
		for(k=0;k<=i;k++){
			System.out.print("    ");
		}
		if(i==0){//We are at the root
			System.out.print("    A_");
			System.out.println(this.getTest().printTest());
		}
		else{
			System.out.print("~~~~> ");
			System.out.print(this.getTestValue().printTestValue());
			System.out.print("  A_");
			System.out.println(this.test.printTest());
		}
		for(k=c2; k<this.children.size();k++){
			this.children.get(k).prettyPrint(i+2);
		}
		
	}
}

