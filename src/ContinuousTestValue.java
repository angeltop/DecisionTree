
public class ContinuousTestValue extends TestValue {

	private	Attribute attribute;
	private SplitType splitType;
	private double splitValue;
	

	public ContinuousTestValue(){
		super();
		attribute = new Attribute() {
		};
		splitValue= 0.0;
	}
	
	public ContinuousTestValue(String parentsTest ,Attribute a, SplitType splitType, double splitValue) {
		super(parentsTest);
		attribute = a;
		this.splitType = splitType;
		this.splitValue = splitValue;		
	}
	
	public Attribute getAttribute(){
		return attribute;
	}
	
	public SplitType getSplitType(){
		return splitType;
	}
	
	public void setAttribute(Attribute a){
		attribute = a;
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
		return Integer.toString(attribute.getId())+ splitinfo + Double.toString(splitValue)+ "  ";
	}

	public enum SplitType{LESS, LESSOREQUAL, MOREOREQUAL, MORE};
}

