
public class Leaf extends Node {

	boolean result;
	
	public boolean getResult(){
		return result;
	}
	public void setResult(boolean result){
		this.result = result;
	}
	
	@Override
	public boolean isLeaf() {
		return true;
	}
}
