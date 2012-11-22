import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


public class MainClass {

	/**
	 * @param args
	 */
	boolean pruning =true;
	static String fileDir;
	static JFrame mainWindow;
	
	public static void main(String[] args) {
		/* We create the main window for this application
		 */

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

		main.mainWindow.setVisible(true);
		
		main.mainWindow.pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int width = main.mainWindow.getSize().width;
		int height = main.mainWindow.getSize().height;
		main.mainWindow.setLocation((dim.width-width)/2,(dim.height-height)/2);
		main.mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	
	/* This function reads the given file and create our instance space X
	 */
	public SampleSet createInstancesX(String textFile) throws IOException{
		
		File instances = new File(textFile);
		
		FileReader in = new FileReader(instances);
		BufferedReader reader = new BufferedReader(in);	
		SampleSet X = new SampleSet();
		String firstline = reader.readLine();	// the first line gives us the type of the attribute
		Attribute a = null;
		
		if (firstline!=null){					
			Pattern pat = Pattern.compile("\\d+\\:(n|c)\\s*");	// d+ for the id of the attribute, n or c for the type of attribute
			Matcher type = pat.matcher(firstline);
			while(type.find()){
				String attrType = type.group();
				
				if(attrType.contains("n"))	//if n it is a numerical attribute
					a = new ContinuousAttribute(Integer.valueOf(attrType.substring(0, attrType.indexOf(":"))));
				else		// if c then it is a categorical attribute
					a = new CategoricalAttribute(Integer.valueOf(attrType.substring(0, attrType.indexOf(":"))));
		
				for(int i =0; i<X.attributes.size(); i++){	// we check if the attribute id already exists in our data set
					if(X.attributes.get(i).getId()==a.getId()){
						JOptionPane.showMessageDialog(mainWindow, "Duplicated attribute id, unacceptable input");
						System.exit(0);
					}
				}
					
				X.attributes.add(a);
			}
		}
		String line = reader.readLine();
		
		int attributeId = 0;// d+ for the attribute id, the rest for its value in this sample. It can either be
							// an number +/- d+.d+ or +/-d+  or has a name sunny or very-cloudy w+-?w+
		Pattern pattern = Pattern.compile("\\d+\\:[\\+\\-]?((\\d+\\.\\d+)|\\d+|(\\w+-?\\w+))\\s*");
		
		while(line!=null){
			Sample instance = new Sample(X.attributes.size());
			int indexOfValue=0;
			Matcher att = pattern.matcher(line);
			while(att.find()){
				String value = att.group();
				attributeId = Integer.valueOf(value.substring(0, value.indexOf(":")));
				value = value.substring(value.indexOf(":")+1);
				value = value.replace(" ", "");
				value = value.replace("\t", "");// we ignore the empty spaces
			
				if(X.attributes.get(attributeId-1).getClass().equals(CategoricalAttribute.class)){
					
					Attribute attribute = (Attribute) X.attributes.get(attributeId-1);
					if(attributeId==attribute.getId()){
						// add value returns the index of the value's insertion in the matrix
						indexOfValue=((CategoricalAttribute)attribute).addValue(value);	
						X.attributes.set(attributeId-1,attribute); // we place back the updated attribute
						
					}else{
						for(int i=0; i<X.attributes.size(); i++){
							if(X.attributes.get(i).getId()==attributeId){
								attribute = (CategoricalAttribute) X.attributes.get(i);
								// add value returns the index of the value's insertion in the matrix
								indexOfValue = ((CategoricalAttribute)attribute).addValue(value);
								X.attributes.set(i,attribute); // we place back the updated attribute
								break;
							}
								
						}					}
					instance.addValue(attributeId, indexOfValue); // we add the attribute's id 
																  // and it's value in the sample
					
				}
				else
					instance.addValue(attributeId, Double.valueOf(value));// we add the attribute's id 
			}
			if(line.endsWith("+"))	// we add true as result if we find a +
				instance.setResult(true);
			else					// otherwise we add false as result
				instance.setResult(false);
			
			X.samples.add(instance);
			line = reader.readLine();	// continue reading the file line-by-line
		}
		
		return X;	// return the sample set
	}
	
/* This function is used for splitting the sample set into two sample sets
 * 70% percent used for training and 30% percent for pruning.
 */ 
	public SampleSet[] randomSplitForPrunning(SampleSet set){
		
		SampleSet[] newSets = new SampleSet[2];
		newSets[0] = new SampleSet();
		newSets[1] = new SampleSet();
		
		Random randomizedPick = new Random();
		int sizeOfTrainingSet = (int) Math.ceil(0.7*(set.samples.size()));
		
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
	
	 
	
	
	/* This class implements the buttonlistener for our
	 * window/frame
	 */
	private class ButtonListener implements ActionListener{

		public ButtonListener(){
			
			
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
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
				pruning = true;
			}else if (arg0.getActionCommand().equals("no")){
				pruning = false;
			}else if(arg0.getActionCommand().equals("ok")){
				mainWindow.setVisible(false);
				Node result = null;
				try {
					SampleSet instances = createInstancesX(fileDir);
					if(pruning){
						SampleSet[] newSets = randomSplitForPrunning(instances);
						Node root = Node.createDT(newSets[0]);
						System.out.println("Before Pruning:");
						root.prettyPrint(0);
						Pruner pruner = new Pruner(newSets[1], root);
						result = pruner.prune();
						System.out.println("After pruning:");
						result.prettyPrint(0);
					}else{
						
						result = Node.createDT(instances);
						result.prettyPrint(0);
					}
					result.createOutputFile(fileDir);
					System.exit(0);
					
				} catch (IOException e) {

					JOptionPane.showMessageDialog(mainWindow, e.getMessage()+"\nPlease choose another file!");
					mainWindow.setVisible(true);
					
				}
			}else if (arg0.getActionCommand().equals("cancel")){
				mainWindow.setVisible(false);
				System.exit(0);
			}
		
		}
				
	}
}
