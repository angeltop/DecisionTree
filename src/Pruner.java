import java.util.ArrayList;
import java.util.List;

/* 
 * The Pruner class is used to prune a given decision Tree using a new SampleSet
 * The SampleSet should be different from the Sample Set used to create the tree
 * to ensure effectiveness of the pruner
 */

public class Pruner {
	
	/*
	 * The pruner requires the pruningSet and the root node of the tree to prune to get start
	 * Therefore these are the members and constructor parameters of the Pruner
	 */
	SampleSet pruningSet;
	Node decisionTreeRoot;
	
	public Pruner(SampleSet pruningSet, Node decisionTree){
		this.pruningSet = pruningSet;
		this.decisionTreeRoot = decisionTree;
	}
	
	/*
	 * The actual pruning requires some preparation
	 * First the pruningSet is distributed over the tree according to the node tests,
	 * and the according test values of the children.
	 * After that the tree is pruned.
	 */
	public Node prune(){
		divideSamples(decisionTreeRoot, pruningSet);
		return pruneSubtree(decisionTreeRoot);
	}
	
	/*
	 * The division of the samples is done recursively.
	 * divideSamples(node, nodeSet) works for any subtree.
	 */
	private void divideSamples(Node node, SampleSet nodeSet){
		/*
		 * The first step is to assign the given set to the top node of the subtree.
		 * If it is a leaf, we're done, else we have to divide the samples into the
		 * correct sampleSets for the subtrees.
		 */
		node.setSampleSet(nodeSet);
		if(!node.isLeaf()){
			
			InternalNode decisionNode = (InternalNode) node;
			Test nodeTest = decisionNode.getTest();
			
			/*
			 * For tests on continuous attributes, the division is always binary.
			 * Therefore we can divide the set into the set for which the test is true
			 * and the set for which it's false. This is obviously dependent on the 
			 * test of the node.
			 */
			if(nodeTest instanceof ContinuousTest){
				
				SampleSet trueSet = new SampleSet();
				trueSet.setAtrributes(nodeSet.getAttributes());
				SampleSet falseSet = new SampleSet();
				falseSet.setAtrributes(nodeSet.getAttributes());
				
				switch(((ContinuousTest) nodeTest).getSplitType()){
				case LESS:
					for(Sample s: nodeSet.getSamples()){
						if(s.getValue(nodeTest.getTestAttribute().getId()).doubleValue() < ((ContinuousTest) nodeTest).getSplitValue()){
							trueSet.addSample(s);
						}else{
							falseSet.addSample(s);
						}
					}
					break;
				case LESSOREQUAL:
					for(Sample s: nodeSet.getSamples()){
						if(s.getValue(nodeTest.getTestAttribute().getId()).doubleValue() <= ((ContinuousTest) nodeTest).getSplitValue()){
							trueSet.addSample(s);
						}else{
							falseSet.addSample(s);
						}
					}
					break;
				case MOREOREQUAL:
					for(Sample s: nodeSet.getSamples()){
						if(s.getValue(nodeTest.getTestAttribute().getId()).doubleValue() >= ((ContinuousTest) nodeTest).getSplitValue()){
							trueSet.addSample(s);
						}else{
							falseSet.addSample(s);
						}
					}
					break;
				case MORE:
					for(Sample s: nodeSet.getSamples()){
						if(s.getValue(nodeTest.getTestAttribute().getId()).doubleValue() > ((ContinuousTest) nodeTest).getSplitValue()){
							trueSet.addSample(s);
						}else{
							falseSet.addSample(s);
						}
					}
					break;
				}
				
				/*
				 * After the determination of the true and false set dependent on the test
				 * those sets are recursively divide among the two subtrees with the children on top.
				 */
				for(Node child: decisionNode.getChildren()){
					if (((ContinuousTestValue) child.getTestValue()).lessThan = true){
						divideSamples(child, trueSet);
					}else{
						divideSamples(child, falseSet);
					}
				}
			}
			/*
			 * For categorical Attributes the distribution is done per child.
			 * For each child, all samples with the correct attribute value according
			 * to the testValue of the child are assembled in a new SampleSet.
			 * Then the sampleset is divided in the subtree with that child on top.
			 */
			else{
				CategoricalAttribute decisionAttribute = (CategoricalAttribute) decisionNode.getTest().getTestAttribute();
				
				for(Node child: decisionNode.getChildren()){
					SampleSet childSet = new SampleSet();
					childSet.setAtrributes(nodeSet.getAttributes());
				
					for(Sample s: nodeSet.getSamples()){
						if(s.getValue(decisionAttribute.getId()).equals(decisionAttribute.addValue(((CategoricalTestValue) decisionNode.getTestValue()).getValue()))){
							childSet.addSample(s);
						}
					}
					
					divideSamples(child, childSet);
				}
			}
		}
	}
	
	/*
	 * The pruning of any subtree is done recursively.
	 * First, all subtrees of the current top node are pruned.
	 * The resulting subtrees are treated as the new children of the top node.
	 * Then the success probability of the subtree is compared to the success probability, 
	 * if the current top node were treated as a leaf.
	 * If treating it as a leaf would increase the success probability an according new Leaf
	 * is created and returned as the pruned subtree.
	 * If we are at a leaf, we simply check if the label fits the pruning set.
	 */
	private Node pruneSubtree(Node node) {
		if(!node.isLeaf()){
			((Leaf) node).setResult(getMajorityClass(node.getSampleSet()));
			return node;
		}else{
			InternalNode decisionNode = (InternalNode) node;
			List<Node> newChildren = new ArrayList<Node>();
			for(Node child: decisionNode.getChildren()){
				newChildren.add(pruneSubtree(child));
			}
			decisionNode.setChildren(newChildren);
			if(getNodeSuccessProbability(decisionNode) > getSuccessProbability(decisionNode)){
				boolean replacementLeafResult = getMajorityClass(decisionNode.getSampleSet());
				Leaf replacementLeaf = new Leaf(replacementLeafResult);
				replacementLeaf.setSampleSet(decisionNode.getSampleSet());
				replacementLeaf.setTestValue(node.getTestValue());
				return replacementLeaf;
			}
			else return decisionNode;
		}
	}

	/*
	 * This function returns the success probability of a subtree starting at a given node.
	 * If the node is a leaf, the success probability is only dependent on that single node.
	 * If it is an internal node, the success probability for every subtree is determined
	 * and weighted depending on the size of the child's sampleSet in comparison to the sampleSet of the parent
	 */
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
	
	/*
	 * This function returns the success probability of a node, if it were a leaf.
	 * The number of + and - classified samples are counted and the probabilities calculated.
	 * If the node actually is a leaf, the probability of the labeled class is returned.
	 * If is is not, the bigger probability is returned, since that is the label,
	 * since the optimal choice would be made for a leaf based on that node.
	 */
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
			if(((Leaf) node).isResult()){
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
		return trueCount>falseCount;
	}
}