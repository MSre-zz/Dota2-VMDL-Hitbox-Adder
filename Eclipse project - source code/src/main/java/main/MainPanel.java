package main;





import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import com.google.common.io.Files;
import javax.swing.JTextArea;


public class MainPanel {

	private JFrame frame;
	private JTextField textFieldMinX;
	private JLabel lblMinY;
	private JTextField textFieldMinY;
	private JTextField textFieldMinZ;
	private JLabel lblMinZ;
	private JLabel lblMaxX;
	private JTextField textFieldMaxX;
	private JLabel lblMaxY;
	private JTextField textFieldMaxY;
	private JTextField textFieldMaxZ;
	private JLabel lblMaxZ;
	private java.awt.List list;
	private ArrayList<File> fileList = new ArrayList<File>();
	private JButton btnNewButton;
	
	private HTMLDocument doc;
	private HTMLEditorKit editorKit;
	private JButton btnNewButton_1;
	
	private JTextPane textPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainPanel window = new MainPanel();
					window.SetupDragNDrop();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainPanel() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 778, 503);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(0, 0, 196, 468);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblMinX = new JLabel("Min X:");
		lblMinX.setBounds(10, 40, 46, 14);
		panel.add(lblMinX);
		
		textFieldMinX = new JTextField();
		textFieldMinX.setBounds(10, 55, 50, 20);
		panel.add(textFieldMinX);
		textFieldMinX.setColumns(10);
		
		lblMinY = new JLabel("Min Y:");
		lblMinY.setBounds(70, 40, 46, 14);
		panel.add(lblMinY);
		
		textFieldMinY = new JTextField();
		textFieldMinY.setBounds(70, 55, 50, 20);
		panel.add(textFieldMinY);
		textFieldMinY.setColumns(10);
		
		textFieldMinZ = new JTextField();
		textFieldMinZ.setBounds(130, 55, 50, 20);
		panel.add(textFieldMinZ);
		textFieldMinZ.setColumns(10);
		
		lblMinZ = new JLabel("Min Z");
		lblMinZ.setBounds(130, 40, 46, 14);
		panel.add(lblMinZ);
		
		lblMaxX = new JLabel("Max X:");
		lblMaxX.setBounds(10, 96, 46, 14);
		panel.add(lblMaxX);
		
		textFieldMaxX = new JTextField();
		textFieldMaxX.setColumns(10);
		textFieldMaxX.setBounds(10, 111, 50, 20);
		panel.add(textFieldMaxX);
		
		lblMaxY = new JLabel("Max Y:");
		lblMaxY.setBounds(70, 96, 46, 14);
		panel.add(lblMaxY);
		
		textFieldMaxY = new JTextField();
		textFieldMaxY.setColumns(10);
		textFieldMaxY.setBounds(70, 111, 50, 20);
		panel.add(textFieldMaxY);
		
		textFieldMaxZ = new JTextField();
		textFieldMaxZ.setColumns(10);
		textFieldMaxZ.setBounds(130, 111, 50, 20);
		panel.add(textFieldMaxZ);
		
		lblMaxZ = new JLabel("Max Z");
		lblMaxZ.setBounds(130, 96, 46, 14);
		panel.add(lblMaxZ);
		
		JButton btnAddHitboxes = new JButton("Add Hitboxes");
		btnAddHitboxes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AddHitboxesSetup();
			}
		});
		
		JButton buttonRemoveFiles = new JButton("<html>Remove<br>Selected<br>Files</html>");
		buttonRemoveFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Integer> indexes = new ArrayList<Integer>();
				for(int a : list.getSelectedIndexes())
				{
					indexes.add(a);
				}
				
				for(int i = indexes.size() - 1; i >= 0; i--)
				{
					list.remove(indexes.get(i));
					fileList.remove(indexes.get(i));
					
				}
			}
		});
		buttonRemoveFiles.setBounds(676, 10, 76, 63);
		frame.getContentPane().add(buttonRemoveFiles);
		
		
		btnAddHitboxes.setBounds(0, 419, 196, 49);
		panel.add(btnAddHitboxes);
		
		JLabel lblTheAboveFields = new JLabel("<html>The above fields are all or nothing.<br>\r\nFill them all in to have it input your own\r\nvalues for cube size, or leave them empty\r\nand the program will use the defaults\r\n(-50,-50-,50) and (50,50,50).<br>These values will be used for all files\r\nper execution.</html>\r\n");
		lblTheAboveFields.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblTheAboveFields.setBounds(10, 142, 170, 116);
		panel.add(lblTheAboveFields);
		
		JLabel lblNewLabel = new JLabel("<html>To execute, drag and drop your model files into the white box and then hit the button below. You can drag and drop all of your files and this program will only pick up the .vmdl files.</html>\r\n");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNewLabel.setBounds(10, 313, 170, 87);
		panel.add(lblNewLabel);
		//textPane.setTransferHandler();
		
		list = new java.awt.List();
		list.setMultipleSelections(true);
		list.setMultipleMode(true);
		list.setBounds(202, 10, 468, 199);
		frame.getContentPane().add(list);
		
		btnNewButton = new JButton("<html>Clear<br>Selection</html>");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int selected : list.getSelectedIndexes())
				{
					list.deselect(selected);
				}
			}
		});
		btnNewButton.setBounds(676, 102, 76, 63);
		
		
		JButton btnClearselection = new JButton("<html>Clear<br>Selection</html>");
		btnClearselection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ClearSelection();
			}
		});
		btnClearselection.setBounds(676, 146, 76, 63);
		frame.getContentPane().add(btnClearselection);
		
		btnNewButton_1 = new JButton("<html>Remove<br>All<br>Files</html>\r\n");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RemoveAllFiles();
			}
		});
		btnNewButton_1.setBounds(676, 78, 76, 63);
		frame.getContentPane().add(btnNewButton_1);
		
		textPane = new JTextPane();
		frame.getContentPane().add(textPane);
		textPane.setContentType("text/html");
		textPane.setPreferredSize(new Dimension(200,200));
		textPane.setBounds(206, 215, 464, 238);
		textPane.setBackground(new Color(211, 211, 211));
		textPane.setEditable(false);
		
		
		SetupTextPane();
		


	}
	
	private void SetupDragNDrop()
	{
		@SuppressWarnings("unused")
		DropTarget target = new DropTarget(list, new DropTargetListener(){

			@Override
			public void dragEnter(DropTargetDragEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void dragExit(DropTargetEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void dragOver(DropTargetDragEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void drop(DropTargetDropEvent e) {
				try 
				{
					e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					List<File> inputList = (List<File>)e.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					
					for(File file : inputList)
					{
						if(Files.getFileExtension(file.getName()).equals("vmdl"))
						{
							//System.out.println("vmdl");
							fileList.add(file);
							list.add(file.getName());
						}
					}	
				} 
				catch (Exception e2) 
				{
					// TODO: handle exception
				}
				
			}

			@Override
			public void dropActionChanged(DropTargetDragEvent e) {
				
				
			}
			
		});
	}
	
	private void AddHitboxesSetup()
	{
		int minX = 0, minY = 0, minZ = 0, maxX = 0, maxY = 0, maxZ = 0;
		boolean usesVectorInputsFromGui = true; //if one try fails, set to false and use default constructor
		try
		{
			minX = Integer.parseInt(textFieldMinX.getText());
			minY = Integer.parseInt(textFieldMinY.getText());
			minZ = Integer.parseInt(textFieldMinZ.getText());
			
			maxX = Integer.parseInt(textFieldMaxX.getText());
			maxY = Integer.parseInt(textFieldMaxY.getText());
			maxZ = Integer.parseInt(textFieldMaxZ.getText());
		} catch(NumberFormatException e)
		{
			usesVectorInputsFromGui = false;
		}
		
		
		for(File file : fileList)
		{
			try {
				VmdlReWrite temp;
				if(usesVectorInputsFromGui)
				{
					temp = new VmdlReWrite(file, minX, minY, minZ, maxX, maxY, maxZ);
				}
				else
				{
					temp = new VmdlReWrite(file);
				}
				
				if(temp.PerformReWrite())
				{
					String text = "\n" + file.getName() + ": Successfully added Cube.dmx!";
					try {
						editorKit.insertHTML(doc, doc.getLength(), text, 0, 0, null);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					String text = "\n" + file.getName() + ": <font color=\"red\">ERROR! Failed to add Cube.dmx!</font>";
					try {
						editorKit.insertHTML(doc, doc.getLength(), text, 0, 0, null);
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void ClearSelection()
	{
		int[] selected = list.getSelectedIndexes();
		for(int index : selected)
		{
			list.deselect(index);
		}
	}
	
	private void RemoveAllFiles()
	{
		fileList.clear();
		list.removeAll();
	}
	
	//added this function because windowbuilder thing kept deleting this stuff in the initializer,
	//don't really know the correct process to change the visual design so I just did this.
	//kept a comment copy below because sometimes it deletes the shit inside the function >:O
	private void SetupTextPane()
	{
		textPane.setContentType("text/html");
		doc = (HTMLDocument)textPane.getDocument();
		editorKit = (HTMLEditorKit)textPane.getEditorKit();
		JScrollPane sp = new JScrollPane(textPane,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setBounds(206, 215, 464, 238);
		frame.add(sp);		
	}
	
//	textPane.setContentType("text/html");
//	doc = (HTMLDocument)textPane.getDocument();
//	editorKit = (HTMLEditorKit)textPane.getEditorKit();
//	JScrollPane sp = new JScrollPane(textPane,
//			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
//			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//	sp.setBounds(206, 215, 464, 238);
//	frame.add(sp);	
}
