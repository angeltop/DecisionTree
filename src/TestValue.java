
public abstract class TestValue {


	protected String parentsTest; // for every node the test for its parent
							// if parent is continuous attribute it can be YES or NO
							// if it is categorical attribute it takes the value of parent's attribute
	public TestValue(){

		this.parentsTest = new String();
	}

	public TestValue(String test){

		this.parentsTest = new String(test);
	}
		
	public String getTest(){
		return parentsTest;
	}
	
	public void setTest(String test){
		this.parentsTest = new String(test);
	}
	
	public abstract String printTest();
	
	
}
