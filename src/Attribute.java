
public abstract class Attribute {

	int id;

	public Attribute(){
		id=0;

	}
	public Attribute(int id){
		this.id = id;

	}
	
	public boolean isContinuous(){
		return true;
	}
	
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}
}
