
public class Leaf extends Node {

	/* In order to describe a Leaf
	 * of the tree we need the result.
	 */
	public boolean result;
	
	public Leaf(boolean b){
		result = b;
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
}
