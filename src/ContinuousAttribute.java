import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;



public class ContinuousAttribute extends Attribute implements Comparator<Sample> {
	
	private double splitValue;
	
	
	public ContinuousAttribute(int id){
		super(id);
	}
	
public double computeEntropy(SampleSet set){
	double entropy = 0.0;
	int pos=0,neg=0,total=0;
	boolean status = true;
	Iterator<Sample> it;
	Sample sample =null;
	//Sort by this attribute.
	java.util.Collections.sort(set.getSamples(),this);
	//count number of positive and negative examples
	for(Sample s : set.getSamples()){
		if(s.getResult())pos++;
		else neg++;
	}
	total=pos+neg;
	entropy = super.computeEntropy(neg,pos);
	
	int posSoFar=0, negSoFar=0;
	double currentEntropy;
	it=set.getSamples().iterator();
	sample = it.next();
	status = sample.getResult();
	if (status)posSoFar++;
	else negSoFar++;
	while(it.hasNext()){
		sample = it.next();
		//no change in result => just increment counters
		if (sample.getResult()==status){
			if (status) posSoFar++;
			else negSoFar++;
		}
		// change in result ==> update entropy, if lower.
		else{
			currentEntropy = 0.0;
			currentEntropy += ((posSoFar+negSoFar)/total)*super.computeEntropy(negSoFar,posSoFar);
			currentEntropy += ((total-posSoFar-negSoFar)/total)*super.computeEntropy(neg-negSoFar,pos-posSoFar);
			if(currentEntropy < entropy){
				this.splitValue=sample.getValue(this.id).doubleValue();
				entropy = currentEntropy;
			}
			if (sample.getResult()) posSoFar++;
			else negSoFar++;
		}
	}
	return entropy;
	}

public double getSplitValue(){
	return splitValue;
}


@Override
public int compare(
		Sample o1, Sample o2) {
	double d1 = o1.getValue(this.getId()).doubleValue();
	double d2 = o2.getValue(this.getId()).doubleValue();
	double res = d1-d2;
	if(res == 0.0){
		if(o1.getResult()){
			if(o2.getResult()) return 0;
			else return 1;
		}
		if(o2.getResult()) return -1;
		else return 0;
	}
	if(res <0) return -1;
	return 1;
}

	/**
	 * splits Data of a given sample set according to this attribute
	 */

	public Node splitData(SampleSet set){
		System.out.print("Splitting on ContinuousAttribute ");
		System.out.print(this.id);
		System.out.print(" with SplitValue ");
		System.out.print(this.splitValue);
		System.out.println(".");
		SampleSet sLT = new SampleSet();
		SampleSet sGEQ = new SampleSet();
		InternalNode n = new InternalNode();
		Node m;
		//Adding attributes to new Sets.
		for(Attribute a: set.getAttributes()){
			if(a.getId() != this.id){
				sLT.addAttribute(a);
				sGEQ.addAttribute(a);
			}
		}
		//Do the split.
		for(Sample s:set.getSamples()){
			if(s.getValue(this.getId()).doubleValue()<this.splitValue){
				sLT.addSample(s);
			}
			else sGEQ.addSample(s);
		}
		//add information to the nodes for output later on.
				try{
					m=Node.createDT(sLT);
					m.setTestValue(new ContinuousTestValue(true));
					n.addChild(m);
				}
				catch (NullPointerException e){
					int neg=0,pos=0;
					for(Sample t: sGEQ.getSamples()){
						if(t.getResult()) pos++;
						else neg ++;
					}
					if(pos >= neg) return new Leaf(true);
					else return new Leaf(false);
				}
				try{
					m =Node.createDT(sGEQ);
					m.setTestValue(new ContinuousTestValue( false));
					n.addChild(m);
					n.setTest(new ContinuousTest(this, this.splitValue));
					return n;
				}
				catch (NullPointerException e){
					int neg=0,pos=0;
					for(Sample t: sLT.getSamples()){
						if(t.getResult()) pos++;
						else neg ++;
					}
					if(pos >= neg) return new Leaf(true);
					else return new Leaf(false);
				}
	}

	public Test createTest(){
		return new ContinuousTest(this,this.splitValue);
	}

}
