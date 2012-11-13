
public class ClassTestValue implements TestValue {

	private	boolean result;
	
	public boolean getResult(){
		return result;
	}
	public void setResult(boolean result){
		this.result = result;
	}
	@Override
	public String printTest() {
		// TODO Auto-generated method stub
		if(result)
			return "+";
		return "-";
	}

}
