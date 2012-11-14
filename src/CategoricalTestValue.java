

public class CategoricalTestValue extends TestValue {

	private Attribute attribute;

	public Attribute getAttribute(){
		return attribute;
	}
	public void setAttribute(Attribute a){
		attribute = a;
	}
	
	
	@Override
	public String printTest() {
		return parentsTest+"  "+ Integer.toString(attribute.getId()) + "  ";
	}

}

