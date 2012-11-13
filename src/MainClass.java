import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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

		main.mainWindow.setVisible(true);
		main.mainWindow.pack();
		main.mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
			// run TDIDT
				
				TDIDT tree = new TDIDT();
				
				try {
					
					System.out.println(fileDir);
					List instances =tree.createInstancesX(fileDir);
					
					for(Iterator it = instances.iterator(); it.hasNext(); ){
						LinkedList < String > curr = (LinkedList<String>) it.next();
						for(Iterator<String> itt = curr.iterator(); itt.hasNext();){
							System.out.print(itt.next()+"\t");
						}
						System.out.println();
					}
					
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
