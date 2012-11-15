
public abstract class Attribute {

	int id;

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
	
	public double computeEntropy(SampleSet set){
		return 5000; //Very bad Entropy.
	}
	
	protected double computeEntropy(int neg, int pos){
		double entropy =0.0;
		int total = neg+pos;
		entropy = neg/total*(java.lang.Math.log(total/neg)/java.lang.Math.log(2));
		entropy = pos/total*(java.lang.Math.log(total/pos)/java.lang.Math.log(2));
		return entropy;
	}
}
