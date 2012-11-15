
public class ContinuousTest extends Test {
	/* If the attribute is numerical/continuous besides its id
	 * we also need its split information, which is the type of the split
	 * and the value of the split.
	 */
	private double splitValue;
	
	public ContinuousTest(Attribute testAttribute, double splitValue) {
		super(testAttribute);
		this.splitValue = splitValue;		
	}
	

	
	public double getSplitValue() {
		return splitValue;
	}

	public void setSplitValue(double splitValue) {
		this.splitValue = splitValue;
	}

	@Override
	public String printTest() {
		return testAttribute.getId() + " < " + splitValue + " ";
	}

}

