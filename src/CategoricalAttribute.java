import java.util.ArrayList;
import java.util.List;
import java.lang.Math;;


public class CategoricalAttribute extends Attribute {


	private ArrayList<String> valueSet;
	
	public CategoricalAttribute(int id){
		super(id);
		valueSet = new ArrayList<String>();
	}
	public boolean isContinuous(){
		return false;
	}
	
	public List<String> getValueSet(){
		return new ArrayList<String>(valueSet);
	}
	
	public String getValue(int i) {
		return valueSet.get(i);
	}
	public int addValue(String newValue){
		if(!valueSet.contains(newValue)){
			valueSet.add(newValue);
			return (valueSet.size()-1);
		}
		else{
			return valueSet.indexOf(newValue);
		}
	}
	public double computeEntropy(SampleSet set){
		double entropy = 0.0;
		int[] pos=new int[valueSet.size()],neg = new int[valueSet.size()];
		int index = 0;
		for(Sample s: set.samples){
					index = s.getValue(getId()).intValue();
					if(s.getResult() == true) pos[index] +=1;
					else neg[index] +=1;
		}
		
		//compute Entropy.
		for(String v: valueSet){
			index= valueSet.indexOf(v);
			entropy += ((pos[index]+neg[index])/set.getSamples().size())*super.computeEntropy(neg[index], pos[index]);
		}
		return entropy;
	}
	
	public Node splitData(SampleSet set){
		SampleSet[] newSets = new SampleSet[this.valueSet.size()];
		for(Sample s : set.getSamples()){
			newSets[valueSet.indexOf(s.getValue(this.id))].addSample(s);
		}
		InternalNode n = new InternalNode();
		for (SampleSet newSet : newSets){
			for (Attribute a : set.getAttributes()){
				if(a.id !=this.id) newSet.addAttribute(a);
			}
			n.addChild(Node.createDT(newSet));
		}
		return n;
	}
	
}
