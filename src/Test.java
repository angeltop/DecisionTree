
public abstract class Test {
	// This class is used for defining the attribute test
	protected Attribute testAttribute;
	
	public Test(Attribute testAttribute){
		this.testAttribute = testAttribute;
	}

	public Attribute getTestAttribute() {
		return testAttribute;
	}

	public void setTestAttribute(Attribute testAttribute) {
		this.testAttribute = testAttribute;
	}
	
	public abstract String printTest();
	
}
