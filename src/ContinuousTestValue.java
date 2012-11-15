
public class ContinuousTestValue implements TestValue {
	/* If the parent attribute is Continuous/Numerical
	 * we need a true or false regarding the split.
	 */
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
