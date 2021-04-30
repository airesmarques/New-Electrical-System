/***************************************************************************
	
	SignalMaster.java
	
	Implemented by  Cristiana Areias ( November 1998)				
	
***************************************************************************/

import javax.swing.*;
import java.awt.*;
import java.lang.*;
import java.util.*;
import java.awt.event.*;

//**************************************************************************
//  Class SIGNALMASTER
//**************************************************************************
/**
 * A <code>SignalMaster</code> is an extension of javax.swing.JFrame and implements
 * the window to show the master data for one signal.
 * This window is called in the signal window.
 * <p>
 * <center><img src="definition/SignalMaster.gif" alt="Signal Master View" ></center>
 * @version 	1.01, 03/03/99
 * @author 	Cristiana Areias
 * @author 	Aires Marques
 * @see signals
 * @see nes
 * @see javax.swing.JFrame
 * @since       NES 1.0
 */

public class SignalMaster extends JFrame
{
	 /**
	  * Instance of the utilities class in this class. This instance is 
	  * initialized in the constructor and it is provided by NES main window.
	  * @see utilities
	  * @see #SignalMaster
	  */	
	public utilities	_utils;
	/**
	 * Instance of the class DBAcc. Initialized in the constructor
	 * @see DBAcc
	 */
	public DBAcc		dbacc    ;
	/**
	 * Object that refer to this window.
	 */	
	private JFrame		win;
	
	//******************************************************************	
	// Constructor for SignalMaster
	//******************************************************************
	/**
	* Constructor for SignalMaster.
	*/	
	

	public SignalMaster(String id,String Name,String Hname,String F1,
			  String T1,String T2,String F2,String WType,
			  String color,String Bundle,utilities _u,DBAcc db)
	{
		super(" Definition: Signal Master Data ");
		this.setSize(430,310);
		dbacc=db;
		win=this;
	        this.setLocation (150,150);
		_utils=_u;		
		menus();
		// Create and add ToolBar

		GridBagLayout gridBagLayout= new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		this.getContentPane().setLayout(gridBagLayout);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.BOTH;

		gbc.weightx = 1.0;
		gbc.insets = new Insets(0,0,0,0);
		JPanel myPan = addToolBar();
		gridBagLayout.setConstraints(myPan,gbc);
		this.getContentPane ().add(myPan);
		gbc.weighty =2.0;
		gbc.insets = new Insets(0,5,5,5);
		JPanel myPanel = SignalData (id,Name,Hname,F1,T1,T2,F2,WType,
			  	             color,Bundle);
		gridBagLayout.setConstraints(myPanel,gbc);
		
		myPanel.setBackground(Color.lightGray);
		myPanel.setBorder(BorderFactory.createEtchedBorder ());
		this.getContentPane ().add(myPanel);
		setVisible(true);

	}
	/**
	 * Creates the menus. 
	 * <p>
	 * To simplify, it parts the operation into smaller ones:
	 * buildFileMenu(),
	 * buildEditMenu(),
	 * buildHelpMenu()
	 * <p>
	 * Some action listeners are added during this process.
	 * @see addActionListener
	 */

	protected void menus()
	{
		JMenuBar myMenu = new JMenuBar();
		myMenu.setOpaque(true);
		JMenu MenuFile = buildFileMenu();
		JMenu MenuEdit = buildEditMenu();
		JMenu MenuHelp = buildHelpMenu();
		myMenu.add(MenuFile);
		myMenu.add(MenuEdit);
		myMenu.add(MenuHelp);
		setJMenuBar(myMenu);
	}
	/**
	 * Creates the "File" menu and submenus. 
	 */
	
	protected JMenu buildFileMenu()
	{
		JMenu MenuFile = new JMenu("File");
		JMenuItem ItemFileNew = new JMenuItem("New");
		JMenuItem ItemFileOpen = new JMenuItem("Open");
		JMenuItem ItemFileClose = new JMenuItem("Close");		
		JMenuItem ItemFileSave = new JMenuItem("Save");
		JMenuItem ItemFilePrintSet = new JMenuItem("Print Settings...");
		JMenuItem ItemFilePrint = new JMenuItem("Print");
		JMenuItem ItemFileQuit = new JMenuItem("Quit");
		
		// {{ Listeners
				
		ItemFileClose.addActionListener(ToolClose);
				
		ItemFileQuit.addActionListener(new ActionListener () {
                public void actionPerformed (ActionEvent e) {
			// Code Listener
			_utils.quit((JComponent)win.getContentPane(),
						new Boolean(_utils.getProperty("GEN_CONFIRM_EXIT")).booleanValue());
		}});
		
		// }} Listeners
		MenuFile.setFont(_utils.defaultMenuFont);
		ItemFileNew.setFont(_utils.defaultMenuFont);
		ItemFileOpen.setFont(_utils.defaultMenuFont);
		ItemFileClose.setFont(_utils.defaultMenuFont);
		ItemFileSave.setFont(_utils.defaultMenuFont);
		ItemFilePrintSet.setFont(_utils.defaultMenuFont);
		ItemFilePrint.setFont(_utils.defaultMenuFont);
		ItemFileQuit.setFont(_utils.defaultMenuFont);						
		
		MenuFile.add(ItemFileNew);
		MenuFile.add(ItemFileOpen);
		MenuFile.add(ItemFileClose);
		MenuFile.add(ItemFileSave);
		MenuFile.addSeparator();
		MenuFile.add(ItemFilePrintSet);
		MenuFile.add(ItemFilePrint);
		MenuFile.addSeparator();
		MenuFile.add(ItemFileQuit);
		ItemFileNew.setEnabled(false);
		ItemFileOpen.setEnabled(false);
		ItemFileSave.setEnabled(false);
		return MenuFile;
	}
	
	/**
	 * Creates the submenus for the menu "Edit" 
	 */
	protected JMenu buildEditMenu()
	{
		JMenu MenuEdit = new JMenu("Edit");
		JMenuItem ItemEditUndo = new JMenuItem("Undo");
		JMenuItem ItemEditRedo = new JMenuItem("Redo");
		JMenuItem ItemEditCut = new JMenuItem("Cut");
		JMenuItem ItemEditCopy = new JMenuItem("Copy");
		JMenuItem ItemEditPaste = new JMenuItem("Paste");
		
		ItemEditUndo.setFont(_utils.defaultMenuFont);
		ItemEditCut.setFont(_utils.defaultMenuFont);
		ItemEditCopy.setFont(_utils.defaultMenuFont);
		ItemEditPaste.setFont(_utils.defaultMenuFont);
		ItemEditRedo.setFont(_utils.defaultMenuFont);
		MenuEdit.setFont(_utils.defaultMenuFont);
		
		// Add into menu		
		
		MenuEdit.add(ItemEditUndo);
		ItemEditUndo.setEnabled(false);	
		MenuEdit.add(ItemEditRedo);
		ItemEditRedo.setEnabled(false);
		MenuEdit.addSeparator();
		MenuEdit.add(ItemEditCut);
		ItemEditCut.setEnabled(false);		      
		MenuEdit.add(ItemEditCopy);
		ItemEditCopy.setEnabled(false);		      
		MenuEdit.add(ItemEditPaste);
		ItemEditPaste.setEnabled(false);		      
		return MenuEdit;		
	}
	
	/**
	 * Creates the "Help" menu and submenus. 
	 */
	protected JMenu buildHelpMenu()
	{
	
		JMenu MenuHelp = new JMenu("Help");	
		JMenuItem ItemHelpIntro = new JMenuItem("Introduction...");
		JMenuItem ItemHelpRefer = new JMenuItem("Reference...");
		JMenuItem ItemHelpOn = new JMenuItem("On Item...");
		JMenuItem ItemHelpAbout = new JMenuItem("About AES...");
		
		// {{ Listeners				
		// }} Listeners
		ItemHelpIntro.setFont(_utils.defaultMenuFont);
		ItemHelpRefer.setFont(_utils.defaultMenuFont);
		ItemHelpOn.setFont(_utils.defaultMenuFont);
		ItemHelpAbout.setFont(_utils.defaultMenuFont);
		MenuHelp.setFont(_utils.defaultMenuFont);						
		
		MenuHelp.add(ItemHelpIntro);
		MenuHelp.addSeparator();		
		MenuHelp.add(ItemHelpRefer);
		MenuHelp.add(ItemHelpOn);
		MenuHelp.addSeparator();
		MenuHelp.add(ItemHelpAbout);
		return MenuHelp;		
	}
	
	/**
	 * Adds the toolbar to this frame.
	 */
	public JPanel addToolBar()
	{

		JPanel Panel_ToolBar = new JPanel ();	
		GridBagLayout Bar_Layout=new GridBagLayout();		
		Panel_ToolBar.setLayout (Bar_Layout);
		GridBagConstraints c2=new GridBagConstraints();								
		
		c2.fill = GridBagConstraints.HORIZONTAL;			
		c2.insets = new Insets (4,4,3,5);								
		c2.weightx =1.0;			
				
		// Create the toolbar
		JToolBar myToolBar= new JToolBar();		
		myToolBar.setBorderPainted(false);
		myToolBar.setFloatable (false);
		// Call addTool Function
		
		addTool(myToolBar,"Close Window","Close",true,ToolClose);
		myToolBar.addSeparator();				
		addTool(myToolBar," Print ","Print",true,null);	
		addTool(myToolBar,"Open Context Help","Help",true,null);
		
		Bar_Layout.setConstraints(myToolBar,c2);		
		
		myToolBar.putClientProperty ("JToolBar.isRolover",Boolean.FALSE);

		Bar_Layout.setConstraints(myToolBar,c2);		
				
		Panel_ToolBar.add(myToolBar);		

		return Panel_ToolBar;
		
		
	}
	
	/** 
	 * Add an item to toolbar.
	 */
	public void addTool(JToolBar toolBar,String ToolTip, String name, boolean enable,ActionListener listener)
	{
		JButton but = new JButton(new ImageIcon(_utils.IconDir + name + "_nc.jpg",name));		
		ImageIcon Close=new ImageIcon(_utils.IconDir + name + "_c.jpg",name);			
		but.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		but.setPressedIcon(Close);
		toolBar.add(but);		
		but.setToolTipText(ToolTip);
		but.setMargin(new Insets(0,0,0,0));
		but.setEnabled(enable);
		but.getAccessibleContext().setAccessibleName(name);
		but.addActionListener (listener);
	
	}
					
	/// LISTENERS
	
	/**
	 * Closes and disposes the frame.
	 *@see #dispose
	 */

	ActionListener ToolClose = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    		
	
		// Code Listener			
		dispose();    
	}};

	
	/**
	 * Create the panels in the frame. The panel which contain the  labels
	 * and the textfields.
	 */
	public JPanel SignalData(String id,String Name,String Hname,String F1,
			  String T1,String T2,String F2,String WType,
			  String color,String Bundle) 
	{
	Font Dialog= new Font("Dialog", Font.PLAIN, 12);
	Color Black=new Color(0);

	JPanel Panel = new JPanel ();			
	GridBagLayout Layout= new GridBagLayout();		
	GridBagConstraints Const = new GridBagConstraints();
	Panel.setLayout(Layout);
	Layout.setConstraints(Panel,Const);        
	
		Const.weighty = 1.0;		
		Const.weightx = 2;	
		Const.insets = new Insets(5,5,5,5);

		JPanel Panel_1 = new JPanel ();
		
		Layout.setConstraints(Panel_1,Const);
		Panel_1.setBackground(Color.lightGray);				
		Panel_1.setOpaque(false);				
		Panel.add(Panel_1);
		Const.weightx =2;
		JPanel Panel_2 = new JPanel ();		
		
		Layout.setConstraints(Panel_2,Const);						
		Panel_2.setOpaque(false);						
		Panel.add(Panel_2);
		
		// Labels and Textfields inside Panel1

		JLabel LabelLink = new JLabel ("Link-ID "),
			LabelSignal = new JLabel ("Signal "),
			LabelFIN1   = new JLabel ("FIN-1 "),
			LabelFIN2   = new JLabel ("FIN-2 "),
			LabelWire   = new JLabel ("Wire Type "),
			LabelColor  = new JLabel ("Color "),
			LabelBundle  = new JLabel ("Bundle ");			
			   
		
		
		JTextField TextLink   = new JTextField(5),
			   TextSignal = new JTextField(),
			   TextFIN1   = new JTextField(),
			   TextFIN2   = new JTextField(),
			   TextWire   = new JTextField(),
			   TextBundle = new JTextField(),			   
			   TextColor  = new JTextField();			   
			   
			   
		GridBagConstraints gbc_ = new GridBagConstraints();		
		GridBagLayout Layout_1= new GridBagLayout();		
		Panel_1.setLayout(Layout_1);		
                gbc_.gridwidth = GridBagConstraints.RELATIVE;
		gbc_.anchor=GridBagConstraints.WEST;
		gbc_.fill = GridBagConstraints.BOTH;

		Layout_1.setConstraints(LabelLink,gbc_);
		gbc_.insets = new Insets(0,0,5,0);														
		Layout_1.setConstraints(LabelSignal,gbc_);				
		gbc_.insets = new Insets(0,0,5,0);																
		Layout_1.setConstraints(LabelFIN1,gbc_);			
		gbc_.insets = new Insets(0,0,5,0);																
		Layout_1.setConstraints(LabelFIN2,gbc_);			
		gbc_.insets = new Insets(0,0,5,0);																
		Layout_1.setConstraints(LabelWire,gbc_);			
		gbc_.insets = new Insets(0,0,5,0);																
		Layout_1.setConstraints(LabelColor,gbc_);
		gbc_.insets = new Insets(0,0,0,0);																
		Layout_1.setConstraints(LabelBundle,gbc_);				
						
                gbc_.gridwidth = GridBagConstraints.NONE;		
		gbc_.weighty = 1;	
		gbc_.insets = new Insets(0,0,5,45);
		Layout_1.setConstraints(TextLink,gbc_);
		TextLink.setDisabledTextColor(Black);
		TextLink.setEnabled(false);
		gbc_.insets = new Insets(0,0,5,30);
		Layout_1.setConstraints(TextSignal,gbc_);
		TextSignal.setDisabledTextColor(Black);
		TextSignal.setEnabled(false);
		gbc_.insets = new Insets(0,0,5,0);				
		Layout_1.setConstraints(TextFIN1,gbc_);	
		TextFIN1.setDisabledTextColor(Black);
		TextFIN1.setEnabled(false);
		gbc_.insets = new Insets(0,0,5,0);	
		Layout_1.setConstraints(TextFIN2,gbc_);		
		TextFIN2.setDisabledTextColor(Black);
		TextFIN2.setEnabled(false);
		gbc_.insets = new Insets(0,0,5,30);						
		Layout_1.setConstraints(TextWire,gbc_);		
		TextWire.setDisabledTextColor(Black);
		TextWire.setEnabled(false);
		gbc_.insets = new Insets(0,0,5,30);						
		Layout_1.setConstraints(TextColor,gbc_);
		gbc_.insets = new Insets(0,0,0,0);						
		Layout_1.setConstraints(TextBundle,gbc_);
		
		TextColor.setDisabledTextColor(Black);
		TextColor.setEnabled(false);
		
		TextBundle.setDisabledTextColor(Black);
		TextBundle.setEnabled(false);
		
		LabelLink.setForeground(Black);				
		LabelLink.setFont(_utils.defaultLabelFont);		
		LabelSignal.setForeground(Black);
		LabelSignal.setFont(_utils.defaultLabelFont);		
		LabelFIN1.setForeground(Black);				
		LabelFIN1.setFont(_utils.defaultLabelFont);		
		LabelFIN2.setFont(_utils.defaultLabelFont);		
		LabelWire.setFont(_utils.defaultLabelFont);		
		LabelColor.setFont(_utils.defaultLabelFont);
		LabelBundle.setFont(_utils.defaultLabelFont);		
		LabelFIN2.setForeground(Black);				
		LabelWire.setForeground(Black);				
		LabelColor.setForeground(Black);
		LabelBundle.setForeground(Black);		
		TextLink.setForeground(_utils.defaultTextColor);	
		TextSignal.setForeground(_utils.defaultTextColor);	
		TextFIN1.setForeground(_utils.defaultTextColor);			
		TextFIN2.setForeground(_utils.defaultTextColor);
		TextWire.setForeground(_utils.defaultTextColor);			
		TextColor.setForeground(_utils.defaultTextColor);			
		TextBundle.setForeground(_utils.defaultTextColor);
		TextLink.setFont(_utils.defaultTextFieldFont);
		TextSignal.setFont(_utils.defaultTextFieldFont);
		TextFIN1.setFont(_utils.defaultTextFieldFont);
		TextFIN2.setFont(_utils.defaultTextFieldFont);
		TextWire.setFont(_utils.defaultTextFieldFont);
		TextColor.setFont(_utils.defaultTextFieldFont);
		TextBundle.setFont(_utils.defaultTextFieldFont);
		Panel_1.add(LabelLink);				
		Panel_1.add(TextLink);		
		Panel_1.add(LabelSignal);		
		Panel_1.add(TextSignal);	
		Panel_1.add(LabelFIN1);			
		Panel_1.add(TextFIN1);							
		Panel_1.add(LabelFIN2);			        
		Panel_1.add(TextFIN2);				
		Panel_1.add(LabelWire);	
		Panel_1.add(TextWire);	
		Panel_1.add(LabelColor);		
		Panel_1.add(TextColor);			
		Panel_1.add(LabelBundle);		
		Panel_1.add(TextBundle);			
		
		// Labels and Textfields inside Panel2
		JLabel LabelP1 = new JLabel ("P1 "),
			LabelP2 = new JLabel ("P2 "),
			LabelH1 = new JLabel ("Hndl ");
		
		JTextField TextP1   = new JTextField(5),
			   TextP2 = new JTextField(5),
			   TextH1   = new JTextField(8);
			   			  
		Panel_2.setLayout(Layout);		
		gbc_.weightx = 0;		
                gbc_.gridwidth = GridBagConstraints.RELATIVE;
		gbc_.anchor=GridBagConstraints.WEST;
		gbc_.fill = GridBagConstraints.BOTH;
		gbc_.insets = new Insets(0,0,0,0);
		Layout.setConstraints(LabelH1,gbc_);
		gbc_.insets = new Insets(0,0,5,0);				
		Layout.setConstraints(LabelP1,gbc_);
		gbc_.insets = new Insets(0,0,55,0);														
		Layout.setConstraints(LabelP2,gbc_);				
		
                gbc_.gridwidth = GridBagConstraints.NONE;
		gbc_.weighty = 1;	
		gbc_.insets = new Insets(0,0,5,0);
		Layout.setConstraints(TextH1,gbc_);
		gbc_.insets = new Insets(0,0,5,50);				
		Layout.setConstraints(TextP1,gbc_);			
		gbc_.insets = new Insets(0,0,55,50);						
		Layout.setConstraints(TextP2,gbc_);
		TextP1.setDisabledTextColor(Black);
		TextP2.setDisabledTextColor(Black);
		TextP1.setEnabled(false);
		TextP2.setEnabled(false);
		LabelP1.setForeground(Black);
		LabelH1.setFont(_utils.defaultLabelFont);		
		LabelP1.setFont(_utils.defaultLabelFont);		
		LabelP2.setFont(_utils.defaultLabelFont);		
		LabelP2.setForeground(Black);
		LabelH1.setFont(_utils.defaultLabelFont);		
		LabelH1.setForeground(Black);		
		LabelH1.setFont(_utils.defaultLabelFont);		
		TextH1.setDisabledTextColor(Black);
		TextH1.setEnabled(false);		
		TextH1.setFont(_utils.defaultTextFieldFont);
		TextP1.setFont(_utils.defaultTextFieldFont);
		TextP2.setFont(_utils.defaultTextFieldFont);
		TextH1.setForeground(_utils.defaultTextColor);	
		TextP1.setForeground(_utils.defaultTextColor);	
		TextP2.setForeground(_utils.defaultTextColor);	
		Panel_2.add(LabelH1);
		Panel_2.add(TextH1);
		Panel_2.add(LabelP1);				
		Panel_2.add(TextP1);		
		Panel_2.add(LabelP2);		
		Panel_2.add(TextP2);
		
		TextLink.setText(id);
		TextSignal.setText(Name);
		TextFIN1.setText(F1);
		TextFIN2.setText(F2);
		TextWire.setText(WType);
		TextColor.setText(color);
		TextP1.setText(T1);
		TextP2.setText(T2);
		TextH1.setText(Hname);
		TextBundle.setText(Bundle);

	return Panel;
	}
	
	/**
	 * Disposes the frame.
	 */ 

        public void dispose()
        {
		setVisible(false);
		super.dispose();
        } // Close
}
