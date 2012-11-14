import java.util.Iterator;


public class Pruner {

	SampleSet pruningSet;
	Node decisionTreeRoot;
	
	public Pruner(SampleSet pruningSet, Node decisionTree){
		this.pruningSet = pruningSet;
		this.decisionTreeRoot = decisionTree;
		//divideSamples(decisionTreeRoot, pruningSet);
	}
	
	public Node prune(){
		//pruneSubtree(decisionTreeRoot);
		
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
				//Map<>
				for(Node child : decisionNode.getChildren()){
					
				}
			}
		}
	}
	
//	private Node pruneSubtree(Node node) {
//		double nodeEntropy = getNodeEntropy(node);
//		if(!node.isLeaf()){
//			double subTreeEntropy = 0;
//			int nodeSize = node.getSampleSet().getSamples().size();
//			for(Iterator<Node>i= ((InternalNode) node).getChildren().iterator(); i.hasNext()){
//				double childWeight = child.getSampleSet().getSamples().size()/nodeSize;
//				subTreeEntropy = childWeight*pruneSubtree(child);
//			}
//			
//		}
//		return nodeEntropy;
//	}

	public double getSuccessProbability(Node subtree) {
		if(subtree.isLeaf()){
			return getNodeSuccessProbability(subtree);
		}else{
			double successProbability=0;
			double nodeSize = subtree.getSampleSet().getSamples().size();
			for(Node child: ((InternalNode) subtree).getChildren()){
				double childWeight = child.getSampleSet().getSamples().size()/nodeSize;
				successProbability += childWeight * getSuccessProbability(child);
			}
			return successProbability;
		}
	}
	
	public double getNodeSuccessProbability(Node node) {
		double trueCount=0;
		double falseCount=0;
		for(Sample s: node.getSampleSet().getSamples()){
			if (s.getResult()){
				trueCount++;
			}else{
				falseCount++;
			}
		}
		double trueProbability = trueCount/(trueCount+falseCount);
		double falseProbability = falseCount/(trueCount+falseCount);
		if(node.isLeaf()){
			if(((Leaf) node).getResult()){
				return trueProbability;
			}else{
				return falseProbability;
			}
		}
		else if(trueProbability>falseProbability){
			return trueProbability;
		}else{
			return falseProbability;
		}
	}
}