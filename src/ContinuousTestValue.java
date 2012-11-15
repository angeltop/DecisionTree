
public class ContinuousTestValue implements TestValue {

	boolean value;
	
	public ContinuousTestValue(boolean value){
		this.value = value;
	}
	
	@Override
	public String printTestValue() {
		if(value){
			return "yes";
		}else{
			return "no";
		}
	}
}
