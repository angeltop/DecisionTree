import java.util.ArrayList;
import java.util.List;


public class SampleSet {
	/* Our SampleSet contains all the attributes and
	 * all the Samples
	 */
	List<Attribute> attributes;
	List<Sample> samples;
	
	public SampleSet(){
		attributes= new ArrayList<Attribute>();
		samples= new ArrayList<Sample>();
	}
	public List<Attribute> getAttributes(){
		return new ArrayList<Attribute>(attributes);
	}
	
	public List<Sample> getSamples (){
		return new ArrayList<Sample>(samples);
	}
	
	public void addSample(Sample sample){
		if(samples==null){
			samples = new ArrayList<Sample>();
		}
		if(!samples.contains(sample))
			samples.add(sample);
	}
	
	public void addAttribute(Attribute a){
		if(!attributes.contains(a))
			attributes.add(a);
	}
	
	public void setAtrributes(List<Attribute> attributes){
		this.attributes = attributes;
	}
	
	public void setSamples(List<Sample>samples){
		this.samples = samples;
	}
	
}
