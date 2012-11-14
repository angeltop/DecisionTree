
public class Sample {
	
	private Number[] values;
	private boolean result;
	
	public Sample(int size){
		values = new Number[size];
	};
	
	public void addValue(int id, Number value){
		values[id-1] = value;
	}
	
	public void setResult(boolean result){
		this.result = result;
	}
	
	public Number getValue(int id){
		return values[id-1];
	}
	
	public boolean getResult(){
		return result;
	}
	
	public int numberOfAttributes(){
		return values.length;
	}
}
