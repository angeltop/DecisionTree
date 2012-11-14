
public class ContinuousTestValue extends TestValue {


	private String splitinfo; // for example "< 20.0"
	
	public ContinuousTestValue(){
		super();
		splitinfo = new String();
	}
	public ContinuousTestValue(Attribute attribute, String test, String splitInfo) {
		super(attribute, test);
		this.splitinfo = splitInfo;
		// TODO Auto-generated constructor stub
	}
	
	public String getSplitInfo(){
		return new String(splitinfo);
	}
	
	public void setSplitInfo(String info){
		this.splitinfo = info;
	}

	public String printTest() {
		// TODO Auto-generated method stub
		return parentsTest+"  "+Integer.toString(attribute.getId())+ splitinfo;
	}

}
