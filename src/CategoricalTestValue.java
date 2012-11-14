
public class CategoricalTestValue implements TestValue {

	private Attribute attribute;
	private String value;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Attribute getAttribute(){
		return attribute;
	}
	public void setAttribute(Attribute a){
		attribute = a;
	}
	
	
	
	@Override
	public String printTest() {
		return value;
	}

}
