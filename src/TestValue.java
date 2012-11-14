
public abstract class TestValue {

	protected Attribute attribute;	// the attribute of the node

	protected String parentsTest; // for every node the test for its parent
							// if parent is continuous attribute it can be YES or NO
							// if it is categorical attribute it takes the value of parent's attribute
	public TestValue(){
		this.attribute = new Attribute(){};
		this.parentsTest = new String();
	}

	public TestValue(Attribute attribute, String test){
		this.attribute = attribute;
		this.parentsTest = new String(test);
	}
	
	public Attribute getAttribute(){
		return attribute;
	}
	
	public void setAttribute(Attribute a){
		attribute = a;
	}
	
	public String getTest(){
		return parentsTest;
	}
	
	public void setTest(String test){
		this.parentsTest = new String(test);
	}
	
	public abstract String printTest();
	
	
}
