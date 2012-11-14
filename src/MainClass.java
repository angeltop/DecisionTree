import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.monitor.Monitor;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


public class MainClass {

	/**
	 * @param args
	 */
	boolean prunning;
	String fileDir;
	JFrame mainWindow;
	String outputText;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		MainClass main = new MainClass();
		ButtonListener listener = main.new ButtonListener();
		main.mainWindow = new JFrame();
		
		main.mainWindow.setTitle("****** TDIDT ******");
		
		main.mainWindow.setLayout(new BorderLayout(30, 30));		
		
		JButton fileSelection = new JButton("Select File");
		fileSelection.setActionCommand("file");
		fileSelection.addActionListener(listener);
		main.mainWindow.add(fileSelection, BorderLayout.PAGE_START);
		
		
		JLabel prunning = new JLabel("Would you like to use prunning?");
		JRadioButton yes = new JRadioButton("Yes",true);
		yes.setActionCommand("yes");
		yes.addActionListener(listener);
		main.prunning = true;
		JRadioButton no = new JRadioButton("No", false);
		no.setActionCommand("no");
		ButtonGroup gr = new ButtonGroup();
		gr.add(yes);
		gr.add(no);
		
		
		JPanel prune = new JPanel();
		prune.setLayout(new FlowLayout());
		prune.add(prunning,FlowLayout.LEFT);
		prune.add(yes, FlowLayout.CENTER);
		prune.add(no, FlowLayout.RIGHT);
		
		
		main.mainWindow.add(prune, BorderLayout.CENTER);
		
		JButton OK = new JButton("OK");
		OK.setActionCommand("ok");
		OK.addActionListener(listener);
		JButton cancel = new JButton("Cancel");
		cancel.setActionCommand("cancel");
		cancel.addActionListener(listener);
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());
		buttons.add(OK);
		buttons.add(cancel);
		
		main.mainWindow.add(buttons, BorderLayout.PAGE_END);

		main.mainWindow.setVisible(false);//////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////
		try {
			SampleSet instances = main.createInstancesX("trainingSet"+File.separator+"training.txt");
			
			SampleSet[] newSets = new SampleSet[2];
			newSets = main.randomSplitForPrunning(instances);
			System.out.println("\n\n**********Sample Set***********\n\n\n");
			main.printSampleSet(instances);
			System.out.println("\n\n**********Training Set***********\n\n\n");
			main.printSampleSet(newSets[0]);
			System.out.println("\n\n**********Testing Set***********\n\n\n");
			main.printSampleSet(newSets[1]);
			
			main.createOutputFile( main.createOutputTestTree());
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//////////////////////////////////////////////////////////////////////////
		main.mainWindow.pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int width = main.mainWindow.getSize().width;
		int height = main.mainWindow.getSize().height;
		main.mainWindow.setLocation((dim.width-width)/2,(dim.height-height)/2);
		main.mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	
	// read the given file and create our instance space X
	
	public SampleSet createInstancesX(String textFile) throws IOException{
		
		System.out.println("Create Instances");
		File instances = new File(textFile);
		
		FileReader in = new FileReader(instances);
		BufferedReader reader = new BufferedReader(in);	
		SampleSet X = new SampleSet();
		String firstline = reader.readLine();	// the first line gives us the type of the attribute
		
		if (firstline!=null){					
			System.out.println(firstline);	
			Pattern pat = Pattern.compile("\\d+\\:(n|c)\\s*");	
			Matcher type = pat.matcher(firstline);
			while(type.find()){
				Attribute a = new Attribute() {
				};
				String attrType = type.group();
				
				if(attrType.contains("n"))	//if n it is a numerical attribute
					a = new ContinuousAttribute(Integer.valueOf(attrType.substring(0, attrType.indexOf(":"))));
				else		// if c then it is a categorical attribute
					a = new CategoricalAttribute(Integer.valueOf(attrType.substring(0, attrType.indexOf(":"))));
				X.attributes.add(a);
			}
		}
		String line = reader.readLine();
		
		int attributeId = 0;
		Pattern pattern = Pattern.compile("\\d+\\:((\\d+\\.\\d+)|\\d+)\\s*");
		
		while(line!=null){
		//	System.out.println(line);
			Sample instance = new Sample(X.attributes.size());
			
			Matcher att = pattern.matcher(line);
			while(att.find()){
				//System.out.print("Found");
				String value = att.group();
				attributeId = Integer.valueOf(value.substring(0, value.indexOf(":")));
				value = value.substring(value.indexOf(":")+1);
				value = value.replace(" ", "");
				value = value.replace("\t", "");
			
				if(X.attributes.get(attributeId-1).getClass().equals(CategoricalAttribute.class)){
					
					instance.addValue(attributeId,  Integer.valueOf(value));
					
					Attribute attribute = (Attribute) X.attributes.get(attributeId-1);
					if(attributeId==attribute.getId()){
						((CategoricalAttribute)attribute).addValue(Integer.valueOf(value));
						X.attributes.set(attributeId-1,attribute);
					}else{
						for(int i=0; i<X.attributes.size(); i++){
							if(X.attributes.get(i).getId()==attributeId){
								attribute = (CategoricalAttribute) X.attributes.get(i);
								((CategoricalAttribute)attribute).addValue(Integer.valueOf(value));
								X.attributes.set(i,attribute);
							}
								
						}
					}
					
				}
				else
					instance.addValue(attributeId, Double.valueOf(value));
			}
			if(line.endsWith("+"))
				instance.setResult(true);
			else
				instance.setResult(false);
			
			X.samples.add(instance);
			line = reader.readLine();
		}
		
		return X;
	}
	

	public SampleSet[] randomSplitForPrunning(SampleSet set){
		
		SampleSet[] newSets = new SampleSet[2];
		newSets[0] = new SampleSet();
		newSets[1] = new SampleSet();
		
		Random randomizedPick = new Random();

		
		int sizeOfTrainingSet = (int) Math.ceil(0.7*(set.samples.size()));
		System.out.println("Training set size "+ sizeOfTrainingSet);
		newSets[0].attributes = new ArrayList<Attribute>(set.attributes);
		newSets[1].attributes = new ArrayList<Attribute>(set.attributes);
		newSets[0].samples = new ArrayList<Sample>();
		newSets[1].samples = new ArrayList<Sample>();
		int size = 0;
		while(size<sizeOfTrainingSet){
			Sample s = set.samples.get(randomizedPick.nextInt(set.samples.size()));
			if(!newSets[0].samples.contains(s)){
				newSets[0].samples.add(s);
				size++;
			}
		}
		
		SampleSet temp = new SampleSet();
		temp.samples = new ArrayList<Sample>(set.samples);
		temp.samples.removeAll(newSets[0].samples);
		
		newSets[1].samples = new ArrayList<Sample>(temp.samples);
		
		return newSets;
	}
	
	private void printSampleSet(SampleSet instances){
		System.out.println("Attributes");
		for(Iterator it = instances.attributes.iterator(); it.hasNext(); ){
			Attribute curr = (Attribute) it.next();
			System.out.print(curr.getId()+"\t");
			if(!curr.isContinuous()){
				CategoricalAttribute c = (CategoricalAttribute) curr;
				for (int q=0; q<c.getValueSet().size(); q++){
					System.out.print("Value "+c.getValueSet().get(q)+ "\t");
				}
			}
									
			System.out.println();
		}
		
		for(Iterator it = instances.samples.iterator(); it.hasNext(); ){
			Sample curr = (Sample) it.next();
			for(int p=1; p<=curr.numberOfAttributes(); p++)
				System.out.print(curr.getValue(p)+"\t");
			System.out.print(curr.getResult());
			System.out.println();
		}
		
	}
	
	public int printNode(Node n, int id, int nextAvailableId){
		
		if(n.getClass().equals(InternalNode.class)){
			
			outputText = outputText + Integer.toString(id) + " "+ ((InternalNode)n).getTestValue().printTest() +" ";
			int children = ((InternalNode)n).getChildren().size();
			int givenId =nextAvailableId; 
			int[] givenIds = new int[children];
			int count=0;
			while(givenId< nextAvailableId+children){
				outputText = outputText + " "+ Integer.toString(givenId);
				givenIds[count] = givenId;
				count++;
				givenId ++;
			}
			outputText = outputText+"\n";
			nextAvailableId = givenId;
			
			for(int i =0; i<children; i++){
				nextAvailableId = printNode(((InternalNode)n).getChildren().get(i), givenIds[i], nextAvailableId);
			}
			return nextAvailableId;
		}else{
			
			outputText = outputText + Integer.toString(id) +" "+ n.getTestValue().printTest()  + "\n";
			return nextAvailableId;
		}
	}

		
	public void createOutputFile(InternalNode tree){
		
		int nodeId = 1;
		
		outputText = new String();
		outputText = "";
		File output = new File("trainingSet"+File.separator+"output.txt");
		
		try {
			FileWriter out = new FileWriter(output);
			printNode(tree, nodeId, nodeId+1);
			out.write(outputText);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	private InternalNode createOutputTestTree(){
		
		InternalNode root, child1, child2;
		Node leaf1, leaf2, leaf3, leaf4, leaf5;
		
		leaf1 = new Node();
		ClassTestValue tl1 = new ClassTestValue();
		tl1.setResult(true);
		tl1.setTest("1");
		leaf1.setTestValue(tl1);
		
		leaf2 = new Node();
		ClassTestValue tl2 = new ClassTestValue();
		tl2.setResult(false);
		tl2.setTest("2");		
		leaf2.setTestValue(tl2);
		
		leaf3 = new Node();
		ClassTestValue tl3 = new ClassTestValue();
		tl3.setResult(true);
		tl3.setTest("3");
		leaf3.setTestValue(tl3);
		
		leaf4 = new Node();
		ClassTestValue tl4 = new ClassTestValue();
		tl4.setResult(true);
		tl4.setTest("Yes");
		leaf4.setTestValue(tl4);
		
		leaf5 = new Node();
		ClassTestValue tl5 = new ClassTestValue();
		tl5.setResult(true);
		tl5.setTest("No");
		leaf5.setTestValue(tl5);
		
		child1 = new InternalNode();
		CategoricalTestValue tc1 = new CategoricalTestValue();
		tc1.setAttribute(new CategoricalAttribute(1));
		tc1.setTest("3");
		child1.setTestValue(tc1);
		((InternalNode)child1).addChild(leaf1);
		((InternalNode)child1).addChild(leaf2);
		((InternalNode)child1).addChild(leaf3);
		
		
		child2 = new InternalNode();
		ContinuousTestValue tc2 = new ContinuousTestValue();
		tc2.setAttribute(new CategoricalAttribute(3));
		tc2.setTest("5");
		tc2.setSplitInfo("<5");
		child2.setTestValue(tc2);
		((InternalNode)child2).addChild(leaf4);
		((InternalNode)child2).addChild(leaf5);

		root = new InternalNode();
		CategoricalTestValue value = new CategoricalTestValue();
		value.setAttribute(new CategoricalAttribute(2));
		value.setTest("root");
		root.setTestValue(value);
		
		((InternalNode)root).addChild(child1);
		((InternalNode)root).addChild(child2);
		
		return root;		
	}
	
	private class ButtonListener implements ActionListener{

		public ButtonListener(){
			
			
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println(arg0.getActionCommand());
			if(arg0.getActionCommand().equals("file")){

				JFileChooser chooser = new JFileChooser();
				int retval = chooser.showOpenDialog(null);
				
				if(retval == JFileChooser.APPROVE_OPTION){
					chooser.setVisible(false);
					fileDir = chooser.getSelectedFile().getAbsolutePath();
				}
				else{
					chooser.setVisible(false);
				}
			}
			else if (arg0.getActionCommand().equals("yes")){
				prunning = true;
			}else if (arg0.getActionCommand().equals("no")){
				prunning = false;
			}else if(arg0.getActionCommand().equals("ok")){
				mainWindow.setVisible(false);
			
				try {
					
					System.out.println(fileDir);
					SampleSet instances = createInstancesX(fileDir);
					if(prunning){
						SampleSet[] newSets = randomSplitForPrunning(instances);
									////////////////////////////////run TDIDT
								//// with newSets[0]
						
						// prune on newSets[1]
						
						
					}else{
						
						///////////////////// run TDIDT with instances
						
					}
					
					
					//////////////Output
					System.exit(0);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if (arg0.getActionCommand().equals("cancel")){
				mainWindow.setVisible(false);
				System.exit(0);
			}
		
		}
				
	}
}
