
public class ContinuousTestValue implements TestValue {

	private	Attribute attribute;
	private SplitType splitType;
	private double splitValue;
	

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
		return Integer.toString(attribute.getId())+" "+ splitinfo + " " + splitValue;
	}

	public enum SplitType{LESS, LESSOREQUAL, MOREOREQUAL, MORE};
}
