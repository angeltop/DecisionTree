public class CategoricalTestValue implements TestValue {
	/* If the parent has a Categorical attribute
	 * we only need its value for the definition of TestValue
	 */
	protected String value;
	
	public CategoricalTestValue(String value){
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String printTestValue() {
		if(value==null)
			return "";
		return value.toString();
	}
}