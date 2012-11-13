
public class ContinuousTestValue implements TestValue {

	private	Attribute attribute;
	private String splitinfo;
	

	public Attribute getAttribute(){
		return attribute;
	}
	
	public String getSplitInfo(){
		return new String(splitinfo);
	}
	
	public void setAttribute(Attribute a){
		attribute = a;
	}
	
	public void setSplitInfo(String info){
		this.splitinfo = info;
	}
	@Override
	public String printTest() {
		// TODO Auto-generated method stub
		return Integer.toString(attribute.getId())+" "+ splitinfo;
	}

}
