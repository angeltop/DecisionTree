import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TDIDT {

	List X;
	List<String> Attributes;
	List Output;
	public TDIDT(){
		
	}
	
	public void topDownInduction(Node node,List set){
		boolean pos=false, neg=false;
		for(Iterator<LinkedList<String>> it = set.iterator(); it.hasNext();){
			List cur =it.next();
			if(cur.contains("+"))
				pos=true;
			if(cur.contains("-"))
				neg=true;
			
			if(neg==pos && pos==true)
				break;
		}
		if(neg==false || pos==false){	// LEAF DETECTED
			
			//Output.add(e)				// ADD IT TO THE OUTPUT
		}
		int attr = Attributes.indexOf("n");
		if (attr>-1){
			set = mergeSortNumerical(attr, set);
		}
		
		double entropy = entropyOfSet(set);
		
		
		
		
	}
	private double attributeSelection( List<LinkedList<String>> SubSet){
		
		int sizeOfSubSet = SubSet.size();
		int attribute=0;
		double entrSum1 = 10.0, entrSum2 = 0.0;
		Hashtable<String, List> AttHash = new Hashtable<String, List>(sizeOfSubSet);
		int positive = 0, negative = 0;
		for(int i=0; i<Attributes.size(); i++){
			entrSum2 =0.0;
			
			if(Attributes.get(i).equals("c")){ //categorical attribute
				// create a hash table for the instances according to the value of attribute
				
				Hashtable hash = new Hashtable<String, List>(sizeOfSubSet);
				for(Iterator<LinkedList<String>> it = SubSet.iterator(); it.hasNext();){
					List cur = it.next();
					hash.put(cur.get(i), cur);
					
				}
				Enumeration<String> keys = hash.keys();
				while(keys.hasMoreElements()){
					List attr = (List) hash.get(keys.nextElement());
					int numOfInstances = attr.size();
					positive =0;
					for(Iterator< List> itt = attr.iterator(); itt.hasNext();){
						List <String> cur = itt.next();
						if(cur.get(cur.size()-1).equals("+"))
							positive++;
					}
					negative=numOfInstances-positive;
					
					entrSum2 = entrSum2 + (numOfInstances/sizeOfSubSet)*entropy(positive, negative);
				}
				
				if(entrSum1>entrSum2){
					entrSum1 = entrSum2;
					AttHash = hash;
					attribute = i;
				}
			}else{	// numerical attribute
				for(int j=0; j<SubSet.size()-1; j++){
					if( !SubSet.get(j).getLast().equals(SubSet.get(j+1).getLast())){
						entrSum2 =0.0;
						double key = (string2double(SubSet.get(j).get(attribute))+string2double(SubSet.get(j+1).get(attribute)))/2;
						String hashKey = String.valueOf(i) + " < "+ String.valueOf(key);
						Hashtable hash = new Hashtable<String, List>(sizeOfSubSet);
						for(int q=0; q<j+1; j++ ){
							hash.put(hashKey, SubSet.get(q));
							if(SubSet.get(q).getLast().equals("+"))
								positive++;
						}
						negative=j-positive;
						entrSum2 = entrSum2 + (j/sizeOfSubSet)*entropy(positive, negative);
						
						positive=0;
						negative=0;
						
						for(int q=j+1; q<SubSet.size(); q++ ){
							hash.put(String.valueOf(i)+" >= " + String.valueOf(key), SubSet.get(q));
							if(SubSet.get(q).getLast().equals("+"))
								positive++;
						}
						negative = sizeOfSubSet-positive;
						entrSum2 = entrSum2 + ((sizeOfSubSet-j)/sizeOfSubSet)*entropy(positive, negative);
						
						if(entrSum1>entrSum2){
							entrSum1 = entrSum2;
							attribute = i;
							AttHash = hash;
						}
					}
				}
			
			}
			
		}
		
		return  (0.0);
	}
	
	// split numerical attributes algorithm
	
	private double[] splitNumericalValues(int attribute, List<LinkedList<String>> set){
		double[] splits = null;
		int p = 0;
		for(int i=0; i<set.size()-1; i++){
			if( !set.get(i).getLast().equals(set.get(i+1).getLast())){
				splits[p] = (string2double(set.get(i).get(attribute))+string2double(set.get(i+1).get(attribute)))/2;
				p++;
			}
		}
		return splits;
	}
	

	private double log2(double value){
		if(value==0)
			return 0;
		else
			return (Math.log(value)/Math.log(2));
	}
	
	private double entropy(int positive, int negative){
		
		if(positive==0 || negative==0)
			return (0.0);
		else
			return (-(positive/(positive+negative))*log2(positive/(positive+negative))
					-(negative/(positive+negative))*log2(negative/(positive+negative)));
		
	}
	
	private double entropyOfSet(List<LinkedList<String>> set){
		int pos =0, neg = 0;
		
		for(Iterator<LinkedList<String>> it = set.iterator(); it.hasNext();){
			List cur  = it.next();
			if(cur.contains("+"))
				pos++;
			else
				neg++;
		}
		return (entropy(pos, neg));
		
	}
	
	private List<LinkedList<String>> mergeSortNumerical(int attr, List instances){
		
		if(instances.size()<2)
			return instances;
		
		List left = new LinkedList<LinkedList<String>>();
		for(int i=0; i<Math.floor(instances.size()/2); i++){
			left.add(instances.get(i));
		}
		List right = new LinkedList<LinkedList<String>>();
		for(int j= (int) (Math.floor(instances.size()/2)); j<instances.size(); j++){
			right.add(instances.get(j));
		}
	
		left = mergeSortNumerical(attr, left);
		right = mergeSortNumerical(attr, right);
		
		return merge(left, right, attr);
	}
	
	private List merge(List<LinkedList<String>> left, List<LinkedList<String>> right, int attr){
		List<LinkedList<String>> result = new LinkedList<LinkedList<String>>();
		while(!left.isEmpty() || !right.isEmpty()){
			if(!left.isEmpty() && !right.isEmpty()){
				if(  string2double(left.get(0).get(attr))<= string2double(right.get(0).get(attr))){
					result.add(left.get(0));
					left.remove(0);
				}else{
					result.add(right.get(0));
					right.remove(0);
				}
					
			}else if(!left.isEmpty()){
				result.add(left.get(0));
				left.remove(0);
			}else if(!right.isEmpty()){
				result.add(right.get(0));
				right.remove(0);
			}
		}
		return result;
	}
	
	private double string2double(String value){
		int indx = value.indexOf(".");
		int pow =0;
		double res =0.0;
		if(indx<0){
			for(int i=value.length(); i>-1; i--){
				res= res+ value.charAt(i)*Math.pow(10, pow);
				pow++;
			}
			return res;
		}else{
			for(int i=indx-1; i>-1; i--){
				res= res+ value.charAt(i)*Math.pow(10, pow);
				pow++;
			}
			pow=1;
			for(int i=indx+1; i<value.length(); i++){
				res =res+value.charAt(i)*Math.pow(0.1, pow);
				pow++;
			}
			return res;
		}
	}
	
	
	
	
}
