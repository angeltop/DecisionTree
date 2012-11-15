public class CategoricalTestValue implements TestValue {

	protected String value;
	
	public CategoricalTestValue(String value){
		this.value = value;
	}
	
	@Override
	public String printTestValue() {
		return value;
	}
}