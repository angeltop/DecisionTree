import java.util.ArrayList;
import java.util.List;


public class CategoricalAttribute extends Attribute {


	private List<Number> valueSet;
	
	public CategoricalAttribute(int id){
		super(id);
		valueSet = new ArrayList<Number>();
	}
	public boolean isContinuous(){
		return false;
	}
	
	public List getValueSet(){
		return new ArrayList(valueSet);
	}
	
	public void addValue(Number newValue){
		if(!valueSet.contains(newValue)){
			valueSet.add(newValue);
		}
	}
	
}
