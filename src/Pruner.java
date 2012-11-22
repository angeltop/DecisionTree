import java.util.ArrayList;
import java.util.List;

/* 
 * The Pruner class is used to prune a given Decision Tree using a new set of samples.
 * The samples should be different from the samples used to create the tree to ensure
 * the effectiveness of the Pruner.
 */

public class Pruner {
	
	/*
	 * The Pruner requires the pruning samples and the root node of the tree to prune.
	 * Therefore these are the members and constructor parameters of the Pruner
	 */
	SampleSet pruningSet;
	Node decisionTreeRoot;
	
	public Pruner(SampleSet pruningSet, Node decisionTree){
		this.pruningSet = pruningSet;
		this.decisionTreeRoot = decisionTree;
	}
	
	/*
	 * The actual pruning requires some preparation:
	 * First the pruningSet is distributed over the tree according to the node tests,
	 * and the according test values of the children.
	 * After that the tree is pruned.
	 */
	public Node prune(){
		divideSamples(decisionTreeRoot, pruningSet);
		return pruneSubtree(decisionTreeRoot);
	}
	
	/*
	 * The distribution of the samples is done recursively.
	 * divideSamples(node, nodeSet) works for any subtree.
	 */
	private void divideSamples(Node node, SampleSet nodeSet){
		/*
		 * The first step is to assign the given set to the top node of the subtree.
		 * If it is a leaf, we're done; else we have to divide the samples into the
		 * correct subsets for the subtrees.
		 */
		node.setSampleSet(nodeSet);
		if(!node.isLeaf()){
			
			InternalNode decisionNode = (InternalNode) node;
			Test nodeTest = decisionNode.getTest();

			System.out.println("Dividing pruning set based on "+ decisionNode.getTest().printTest());

			/*
			 * For tests on continuous attributes, the division is always binary.
			 * Therefore we can divide the set into the set for which the test is true
			 * and the set for which it's false. This is obviously dependent on the 
			 * test of the node, which is always a comparison "<x" due to the construction implementation.
			 */
			if(nodeTest instanceof ContinuousTest){
				
				SampleSet trueSet = new SampleSet();
				trueSet.setAtrributes(nodeSet.getAttributes());
				SampleSet falseSet = new SampleSet();
				falseSet.setAtrributes(nodeSet.getAttributes());
				
				for(Sample s: nodeSet.getSamples()){
					if(s.getValue(nodeTest.getTestAttribute().getId()).doubleValue() < ((ContinuousTest) nodeTest).getSplitValue()){
						trueSet.addSample(s);
					}else
						falseSet.addSample(s);
					}
				
				/*
				 * After the determination of the true and false set dependent on the node's test
				 * those sets are recursively distributed among the two subtrees starting with the node's children.
				 */
				for(Node child: decisionNode.getChildren()){
					if (((ContinuousTestValue) child.getTestValue()).value == true){
						divideSamples(child, trueSet);
					}else{
						divideSamples(child, falseSet);
					}
				}
			}
			/*
			 * For categorical Attributes the distribution is done per child.
			 * For each child, all samples with the correct attribute value according
			 * to the testValue of the child are assembled in a new set of samples.
			 * Then the derived samples are distributed in the subtree of that child.
			 */
			else{
				CategoricalAttribute decisionAttribute = (CategoricalAttribute) decisionNode.getTest().getTestAttribute();
				
				for(Node child: decisionNode.getChildren()){
					SampleSet childSet = new SampleSet();
					childSet.setAtrributes(nodeSet.getAttributes());
				
					String childValue = ((CategoricalTestValue) decisionNode.getTestValue()).getValue();
					
					for(Sample s: nodeSet.getSamples()){
						if(s.getValue(decisionAttribute.getId()).equals(decisionAttribute.addValue(childValue))){
							childSet.addSample(s);
						}
					}
					
					divideSamples(child, childSet);
				}
			}
		}
	}
	
	/*
	 * The pruning of any subtree is done recursively:
	 * If we are at a leaf, we are at the end of the recursion and simply check if the label
	 * fits the pruning set.
	 * Else we need to prune all subtrees of the current top node first. The top nodes of the
	 * resulting subtrees are treated as the new children of the top node.
	 * Then the success probability of the subtree is compared with the success probability, 
	 * if the current top node were a leaf instead.
	 * If treating it as a leaf increases the success probability of the subtree an according
	 * new Leaf is created and returned as the pruned subtree.
	 */
	private Node pruneSubtree(Node node) {
		if(!node.isLeaf()){
			System.out.println("Revisiting label of leaf");
			((Leaf) node).setResult(getMajorityClass(node.getSampleSet()));
			return node;
		}else{
			InternalNode decisionNode = (InternalNode) node;
			System.out.println("Pruning node "+ decisionNode.getTest().printTest() + ": Evaluating children");
			List<Node> newChildren = new ArrayList<Node>();
			for(Node child: decisionNode.getChildren()){
				newChildren.add(pruneSubtree(child));
			}
			decisionNode.setChildren(newChildren);
			
			System.out.println("Pruning node "+decisionNode.getTest().printTest() +":");
			
			if(getNodeSuccessProbability(decisionNode) > getSuccessProbability(decisionNode)){
				boolean replacementLeafResult = getMajorityClass(decisionNode.getSampleSet());
				Leaf replacementLeaf = new Leaf(replacementLeafResult);
				replacementLeaf.setSampleSet(decisionNode.getSampleSet());
				replacementLeaf.setTestValue(node.getTestValue());
				
				System.out.println("Replacing node with new leaf");
				return replacementLeaf;
			}
			else{
				System.out.println("Keeping node");
				return decisionNode;
			}
		}
	}

	/*
	 * This function returns the success probability of a subtree starting at a given node.
	 * If the node is a leaf, the success probability is only dependent on that single node.
	 * If it is an internal node, the success probability for every subtree is determined
	 * and weighted depending on the size of the child's sampleSet in comparison to the sampleSet of the parent
	 */
	public double getSuccessProbability(Node subtree) {
		double successProbability = 0.0;
		if(subtree.isLeaf()){
			successProbability = getNodeSuccessProbability(subtree);
		}else{
			double nodeSize = subtree.getSampleSet().getSamples().size();
			for(Node child: ((InternalNode) subtree).getChildren()){
				double childWeight = child.getSampleSet().getSamples().size()/nodeSize;
				successProbability += childWeight * getSuccessProbability(child);
			}
		}
		System.out.println("Success probability of subtree is "+ successProbability);
		return successProbability;
	}
	
	/*
	 * This function returns the success probability of a node, if it were a leaf.
	 * The number of + and - classified samples are counted and the probabilities calculated.
	 * If the node actually is a leaf, the probability of the labeled class is returned.
	 * If is is not, the bigger probability is returned, since the optimal choice would be made
	 * for a leaf based on that node.
	 */
	public double getNodeSuccessProbability(Node node) {
		double trueCount=0;
		double falseCount=0;
		double successProbability = 0.0;
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
			if(((Leaf) node).isResult()){
				successProbability = trueProbability;
			}else{
				successProbability = falseProbability;
			}
		}
		else if(trueProbability>falseProbability){
			successProbability = trueProbability;
		}else{
			successProbability = falseProbability;
		}
		System.out.println("SuccessProbability of node is " + successProbability);
		return successProbability;
	}
	
	/*
	 * This function returns the class label which is more frequent
	 * in the given sample set.
	 */
	public boolean getMajorityClass(SampleSet samples){
		int trueCount=0;
		int falseCount=0;
		for(Sample s: samples.getSamples()){
			if (s.getResult()){
				trueCount++;
			}else{
				falseCount++;
			}
		}
		boolean majorityClass = trueCount>falseCount;
		if(majorityClass){
			System.out.println("The majority of these samples is +");
		}else{
			System.out.println("The majority of these samples is -");
		}
		return majorityClass;
	}
}