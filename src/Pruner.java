
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
	
	private void divideSamples(Node node, SampleSet nodeSet){
		node.setSampleSet(nodeSet);
		if(!node.isLeaf()){
			InternalNode decisionNode = (InternalNode) node;
			Attribute decisionAttribute = decisionNode.getTestAttribute();
			if(decisionAttribute.isContinuous()){
				//Do, when implementation of continuous test is clear
			}
			else{
				Map<>
				for(Node child : decisionNode.getChildren()){
					
				}
			}
		}
	}
}
