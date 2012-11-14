

public abstract class Node{
	
	private TestValue test;
	private	SampleSet subSet;
		
	public void setTestValue(TestValue test){
		this.test = test;
	}
	public void setSampleSet(SampleSet set){
		subSet = set;
	}
	
	public TestValue getTestValue(){
		return test;
	}
	public SampleSet getSampleSet(){
		return subSet;
	}
	
	public abstract boolean isLeaf();
}
