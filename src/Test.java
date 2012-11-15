
public abstract class Test {

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
