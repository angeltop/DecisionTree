import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public abstract class Node{
	/* In order to describe a Node we need a 
	 * TestValue regarding the parent and
	 * the sub-set of samples
	 */
	private TestValue testValue;
	private	SampleSet subSet;
	private Test test;
	
	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}
		
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
	
	/**
	 * Creates a decision Tree from the given SampleSet.
	 * This is done using top down induction.
	 * @param set The sample set.
	 * @return A decision tree learned from @set.
	 */
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
			//No Data given.
			return null;
		}
		deltaResult=firstResult;
		//Check if data is already split.
		while(it.hasNext()){
			deltaResult=it.next().getResult();
			if(deltaResult != firstResult) break;
		}
		if(deltaResult == firstResult) return new Leaf(firstResult);
		if(set.getAttributes().size() ==0){
			int pos =0, neg=0;
			for (Sample s :set.getSamples()){
				if (s.getResult()) pos++;
				else neg++;
			}
			if(pos > neg) return new Leaf(true);
			else return new Leaf(false);
		}
		
		//Compute entropies for all attributes.
		for(Attribute a : set.getAttributes()){
			currentEntropy=a.computeEntropy(set);
			if(currentEntropy < minEntropy){
				minEntropy=currentEntropy;
				minEntropyAttribute=a;
			}
		}
		
		//split examples by the optimal attribute. 
		Node ret = minEntropyAttribute.splitData(set);
		ret.setTest(minEntropyAttribute.createTest());
		ret.setTestValue(new CategoricalTestValue(null));
		return ret;
	}
	
	public abstract boolean isLeaf();
    
	/* This function creates the output file for the given tree, input of this function is 
	 * the root node.
	 */
	public File createOutputFile(String fileDir) throws IOException{
		
		int nodeId = 1;
		
		String outputText = new String("");
		File parentFolder = new File(fileDir).getParentFile();
		File output = new File(parentFolder.getAbsolutePath()+File.separator+"output.txt");
		
		int i=1;
		/* while the file exists we try to find an appropriate name for
		 * our output file
		 */
		while(output.exists()){
			output = new File(parentFolder.getAbsolutePath()+File.separator+"output"+i+".txt");
			i++;
		}
		FileWriter out = new FileWriter(output);
		this.printNode(out,nodeId, nodeId+1);
		out.write(outputText);
		out.close();
		return output;
		
	}

	/* This function creates the line that describes the attributes of the given node
	 * we call this function recursively in order to access all nodes. As id we give the id of the 
	 * current node and and as nextAvailable id we give the number that is available for the
	 * next node without id.
	 */
	public abstract int printNode(FileWriter f,int id, int nextAvailableId) throws IOException;
	
	public abstract void prettyPrint(int i);
}
