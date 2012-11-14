
public class CategoricalTestValue extends TestValue {


	
	public CategoricalTestValue(){
		super();
	}
	
	public CategoricalTestValue(Attribute attribute, String test){
		super(attribute, test);
	}
	

	public String printTest() {
		// TODO Auto-generated method stub
		if(!attribute.isContinuous())
			return  parentsTest +"  "+Integer.toString(attribute.getId());
		return null;
	}

}
