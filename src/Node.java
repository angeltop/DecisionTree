import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JOptionPane;

public abstract class Node{
	/* In order to describe a Node we need a 
	 * TestValue regarding the parent and
	 * the sub-set of samples
	 */
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
		this.printNode(nodeId, nodeId+1);
		out.write(outputText);
		out.close();
		return output;
		
	}

	/* This function creates the line that describes the attributes of the given node
	 * we call this function recursively in order to access all nodes. As id we give the id of the 
	 * current node and and as nextAvailable id we give the number that is available for the
	 * next node without id.
	 */
	public abstract int printNode(int id, int nextAvailableId);
	
}
