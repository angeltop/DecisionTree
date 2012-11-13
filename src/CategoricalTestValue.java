
public class CategoricalTestValue implements TestValue {

	private Attribute attribute;
	
	public Attribute getAttribute(){
		return attribute;
	}
	public void setAttribute(Attribute a){
		attribute = a;
	}
	
	@Override
	public String printTest() {
		// TODO Auto-generated method stub
		if(!attribute.isContinuous())
			return Integer.toString(attribute.getId());
		return null;
	}

}
