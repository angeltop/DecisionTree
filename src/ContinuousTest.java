
public class ContinuousTest extends Test {
	/* If the attribute is numerical/continuous besides its id
	 * we also need its split information, which is the type of the split
	 * and the value of the split.
	 */
	private SplitType splitType;
	private double splitValue;
	
	public ContinuousTest(Attribute testAttribute, SplitType splitType, double splitValue) {
		super(testAttribute);
		this.splitType = splitType;
		this.splitValue = splitValue;		
	}
	
	public SplitType getSplitType(){
		return splitType;
	}
	
	public void setSplitType(SplitType type){
		this.splitType = type;
	}
	
	public double getSplitValue() {
		return splitValue;
	}

	public void setSplitValue(double splitValue) {
		this.splitValue = splitValue;
	}

	@Override
	public String printTest() {
		String splitinfo;
		switch(splitType){
		case LESS:
			splitinfo = "<";
			break;
		case LESSOREQUAL:
			splitinfo = "<=";
			break;
		case MOREOREQUAL:
			splitinfo = ">=";
			break;
		case MORE:
			splitinfo = ">";
			break;
		default:
			splitinfo ="";
		}
		return testAttribute.getId() + splitinfo + splitValue + " ";
	}

	public enum SplitType{LESS, LESSOREQUAL, MOREOREQUAL, MORE};
}

