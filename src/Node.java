import java.util.List;



public abstract class Node{
	
	private TestValue testValue;
	private	SampleSet subSet;
		
	public void setTestValue(TestValue test){
		this.testValue = test;
	}
	public void setSampleSet(SampleSet set){
		subSet = set;
	}
	
	public TestValue getTestValue(){
		return testValue;
	}
	public SampleSet getSampleSet(){
		return subSet;
	}
	
	public void createDT(SampleSet set, List<Attribute> attributes){
	double currentEntropy=2.0,maxEntropy =2.0;
	Attribute maxEntropyAttribute;
	for(Attribute a : attributes){
		currentEntropy=a.computeEntropy(set);
		if(currentEntropy < maxEntropy){
			maxEntropy=currentEntropy;
			maxEntropyAttribute=a;
		}
		
	}
	}
	
	public abstract boolean isLeaf();
    
	
}
