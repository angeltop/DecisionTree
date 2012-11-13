
public class Sample {
	
	private Number[] values;
	private boolean result;
	
	public Sample(int size){
		values = new Number[size];
	};
	
	public void addValue(int index, Number value){
		values[index-1] = value;
	}
	
	public void setResult(boolean result){
		this.result = result;
	}
	
	public Number getValue(int index){
		return values[index];
	}
	
	public boolean getResult(){
		return result;
	}
	
	public int numberOfAttributes(){
		return values.length;
	}
}
