
public class ContinuousTestValue implements TestValue {
	/* If the parent attribute is Continuous/Numerical
	 * we need a true or false regarding the split.
	 */
	boolean lessThan;
	double value;
	
	public ContinuousTestValue(double value,boolean lessThan){
		this.lessThan = lessThan;
		this.value = value;
	}
	
	@Override
	public String printTestValue() {
		if(lessThan){
			return "i < "+ Double.toString(value);
		}else{
			return "i >= "+ Double.toString(value);
		}
	}
}
