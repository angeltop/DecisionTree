import java.util.ArrayList;
import java.util.List;
import java.lang.Math;;


public class CategoricalAttribute extends Attribute {


	private ArrayList<Integer> valueSet;
	
	public CategoricalAttribute(int id){
		super(id);
		valueSet = new ArrayList<Integer>();
	}
	public boolean isContinuous(){
		return false;
	}
	
	public List<Integer> getValueSet(){
		return new ArrayList(valueSet);
	}
	
	public void addValue(Number newValue){
		if(!valueSet.contains(newValue)){
			valueSet.add(newValue.intValue());
		}
	}
	public double computeEntropy(SampleSet set){
		double entropy = 0.0;
		int[] pos=new int[valueSet.size()],neg = new int[valueSet.size()];
		for(Sample s: set.samples){
			for(Integer v :valueSet){
				if(v == s.getValue(this.getId())){
					if(s.getResult() == true) pos[v] +=1;
					else neg[v] +=1;
				}
			}
		}
		//compute Entropy.
		for(Integer v: valueSet){
			entropy += ((pos[v]+neg[v])/set.getSamples().size())*super.computeEntropy(neg[v], pos[v]);
		}
		return entropy;
	}
	
}
