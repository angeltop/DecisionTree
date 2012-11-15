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
	java.util.Collections.sort(set.getSamples(),this);
	for(Sample s : set.getSamples()){
		if(s.getResult())pos++;
		else neg++;
	}
	total=pos+neg;
	entropy = super.computeEntropy(neg,pos);
	if(entropy ==0){
		this.splitValue=set.getSamples().get(0).getValue(this.getId()).doubleValue();
		return 0.0;
	}
	int posSoFar=0, negSoFar=0;
	double currentEntropy;
	it=set.getSamples().iterator();
	sample = it.next();
	status = sample.getResult();
	if (status)posSoFar++;
	else negSoFar++;
	while(it.hasNext()){
		sample = it.next();
		if (sample.getResult()==status){
			if (status) posSoFar++;
			else negSoFar++;
		}
		else{
			currentEntropy =0.0;
			currentEntropy += ((posSoFar+negSoFar)/total)*super.computeEntropy(negSoFar,posSoFar);
			currentEntropy += ((total-posSoFar+negSoFar)/total)*super.computeEntropy(neg-negSoFar,pos-posSoFar);
			if(currentEntropy < entropy){
				this.splitValue=sample.getValue(this.id).doubleValue();
				entropy = currentEntropy;
			}
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

public Node splitData(SampleSet set){
	SampleSet sLT = new SampleSet();
	SampleSet sGEQ = new SampleSet();
	InternalNode n = new InternalNode();
	//Adding attributes to new Sets.
	for(Attribute a: set.getAttributes()){
		if(a.getId() != this.id){
			sLT.addAttribute(a);
			sGEQ.addAttribute(a);
		}
	}
	for(Sample s:set.getSamples()){
		if(s.getValue(this.getId()).doubleValue()<this.splitValue){
			sLT.addSample(s);
		}
		else sGEQ.addSample(s);
	}
	n.addChild(Node.createDT(sLT));
	n.addChild(Node.createDT(sGEQ));
	return n;
}


}
