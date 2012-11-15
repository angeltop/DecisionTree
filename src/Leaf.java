import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Leaf extends Node {

	/* In order to describe a Leaf
	 * of the tree we need the result.
	 */
	public boolean result;
	
	public Leaf(boolean b){
		result = b;
		super.setTestValue(null);
	}
	
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}

	@Override
	public boolean isLeaf() {
		return true;
	}


	/* This function creates the line that describes the attributes of the given node
	 * we call this function recursively in order to access all nodes. As id we give the id of the 
	 * current node and and as nextAvailable id we give the number that is available for the
	 * next node without id.
	 */
	@Override
	public int printNode(FileWriter f,
			int id, int nextAvailableId) throws IOException {
		String outputText ="";
		if(this.result)			
			outputText = Integer.toString(id) +"\t"+ this.getTestValue().printTestValue()  +"\t+\n";
		else
			outputText = Integer.toString(id) +"\t"+ this.getTestValue().printTestValue()  +"\t-\n";
		f.write(outputText);
		return nextAvailableId; // the next available id is the same as before since no new node is found
	}
}
