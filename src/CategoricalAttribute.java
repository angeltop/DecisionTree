import java.util.ArrayList;
import java.util.List;


public class CategoricalAttribute extends Attribute {


	private List<String> valueSet;
	
	public boolean isContinuous(){
		return false;
	}
	
	public List getValueSet(){
		return new ArrayList(valueSet);
	}
	
	public void addValue(String newValue){
		if(!valueSet.contains(newValue)){
			valueSet.add(newValue);
		}
	}
	
}
