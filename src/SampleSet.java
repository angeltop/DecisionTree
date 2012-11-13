import java.util.ArrayList;
import java.util.List;


public class SampleSet {
	
	List<Attribute> attributes;
	List<Sample> samples;
	
	public SampleSet(){
		attributes= new ArrayList<Attribute>();
		samples= new ArrayList<Sample>();
	}
	public List<Attribute> getAttributes(){
		return new ArrayList(attributes);
	}
	
	public List<Sample> getSamples (){
		return new ArrayList(samples);
	}
	
	public void addSample(Sample sample){
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
