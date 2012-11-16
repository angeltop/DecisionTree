
public abstract class Attribute {

	protected int id;

	public Attribute(){
		id=0;

	}
	public Attribute(int id){
		this.id = id;

	}
	
	public boolean isContinuous(){
		return true;
	}
	
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}
	
	/**
	 * Computes Entropy of the attribute in the exaples @set.
	 * @param set
	 * @return
	 */
	public abstract double computeEntropy(SampleSet set);
	
	/**
	 * Computes the actual entropy values
	 * @param neg #positive examples 
	 * @param pos #negative examples
	 * @return
	 */
	protected double computeEntropy(int neg, int pos){
		double entropy =0.0;
		double total = neg+pos;
		if(pos==0) return 0.0;
		if(neg==0) return 0.0;
		entropy += (neg/total)*(java.lang.Math.log(total/neg)/java.lang.Math.log(2.0));
		entropy += (pos/total)*(java.lang.Math.log(total/pos)/java.lang.Math.log(2.0));
		return entropy;
	}
	public Node splitData(SampleSet set){
		return null;
	}
	public abstract Test createTest();
}
