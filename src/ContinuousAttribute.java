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
	Integer count = new Integer(0);
	int pos=0,neg=0,total=0;
	boolean status = true;
	Iterator<Sample> it;
	java.util.Collections.sort(set.getSamples(),this);
	for(Sample s : set.getSamples()){
		if(s.getResult())pos++;
		else neg++;
	}
	total=pos+neg;
	for(Sample s : set.getSamples()){
		
	}
	return entropy;
	}

public double getSplitValue(){
	return splitValue;
}


@Override
public int compare(
		Sample o1, Sample o2) {
	double d1 = ((Sample) o1).getValue(this.getId()).doubleValue();
	double d2 = ((Sample) o2).getValue(this.getId()).doubleValue();
	return Double.compare(d1, d2);
}


}
