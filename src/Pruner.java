
public class Pruner {

	SampleSet pruningSet;
	Node decisionTreeRoot;
	
	public Pruner(SampleSet pruningSet, Node decisionTree){
		this.pruningSet = pruningSet;
		this.decisionTreeRoot = decisionTree;
		divideSamples(decisionTreeRoot, pruningSet);
	}
	
	public Node prune(){
		return decisionTreeRoot;
	}
	
	private void divideSamples(Node decisionNode, SampleSet nodeSet){
		decisionNode.setSampleSet(nodeSet);
		if(!decisionNode.isLeaf()){
			for(Sample s: nodeSet.getSamples()){
				Attribute decisionAttribute = ((InternalNode) decisionNode).getTestAttribute();
			}
		}
	}
}
