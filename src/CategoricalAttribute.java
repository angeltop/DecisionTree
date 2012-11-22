import java.util.ArrayList;
import java.util.List;



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
	/**
	 * Adds a new Value to the categorical attribute 
	 * @param newValue Name of the value.
	 * @return the index of the value. A new index is assigned if the value is not present yet.
	 */
	public int addValue(String newValue){
		if(!valueSet.contains(newValue)){
			int index =valueSet.size();
			valueSet.add(index,newValue);
			return (index);
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
			entropy += new Double((pos[index]+neg[index])/set.getSamples().size())
								*super.computeEntropy(neg[index], pos[index]);
		}
		return entropy;
	}
	
	public Node splitData(SampleSet set){
		Node m = null;
		SampleSet[] newSets = new SampleSet[this.valueSet.size()];
		int i;
		System.out.print("Splitting on CategoricalAttribute ");
		System.out.print(this.id);
		System.out.println(".");
		//initialize Sets.
		for(i=0;i<newSets.length;i++) newSets[i] = new SampleSet();
		// Add samples to the correct set, indexed by categorical attribute index.
		for(Sample s : set.getSamples()){
			newSets[s.getValue(this.id).intValue()].addSample(s);
		}
		InternalNode n = new InternalNode();
		for (i=0;i <newSets.length; ++i){
			for (Attribute a : set.getAttributes()){
				if(a.id !=this.id) newSets[i].addAttribute(a);
			}
			//create new node and add information for output.
			m=Node.createDT(newSets[i]);
			m.setTestValue(new CategoricalTestValue(this.getValueSet().get(i)));
			n.addChild(m);
		}
		return n;

	}
	
	public CategoricalTest createTest(){
		return new CategoricalTest(this);
	}

}
