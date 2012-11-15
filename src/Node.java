import java.util.Iterator;
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
	
	public static Node createDT(SampleSet set){
		double currentEntropy=2.0,minEntropy =2.0;
		Attribute minEntropyAttribute = null;
		Iterator<Sample> it =set.getSamples().iterator();
		boolean firstResult,deltaResult;
		//get Result of the first Sample
		if(it.hasNext()){
			firstResult=it.next().getResult();
		}
		else{
			//%TODO EXCEPTION
			return null;
		}
		deltaResult=firstResult;
		//Check if data is already splited.
		while(it.hasNext()){
			deltaResult=it.next().getResult();
			if(deltaResult != firstResult) break;
		}
		if(deltaResult == firstResult) return new Leaf(firstResult);
		for(Attribute a : set.getAttributes()){
			currentEntropy=a.computeEntropy(set);
			if(currentEntropy < minEntropy){
				minEntropy=currentEntropy;
				minEntropyAttribute=a;
			}
		}
		return minEntropyAttribute.splitData(set);
	}
	
	public abstract boolean isLeaf();
    
	
}
