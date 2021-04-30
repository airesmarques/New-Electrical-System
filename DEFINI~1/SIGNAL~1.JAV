/*===========================================================================
	
	Signals.java
	
	Implemented by  Cristiana Areias ( 1998)				
	
===========================================================================*/

import javax.swing.*;
import java.awt.*;
import java.lang.*;
import java.util.*;
import java.awt.event.*;
/*===========================================================================
  Class signals
===========================================================================*/
/**
 * A <code>signals</code> is an extension of javax.swing.JFrame and implements
 * the window to show the list of signals for one specify schematic with a
 * certain effectivity and project <p>
 * This window is called in the Definition window, or in the EPD connect.<p>
 * When is called by EPD connect, this class run the update routines
 *  (Class updatedef) 
 * <p>
 * <center><img src="definition/signals.gif" alt="Signals View" ></center>
 * @version 	1.01, 03/03/99
 * @author 	Cristiana Areias
 * @author 	Aires Marques
 * @see #SelectDevices 
 * @see SignalMaster
 * @see wire
 * @see Definition
 * @see updatedef
 * @see nes
 * @see javax.swing.JFrame
 * @since       NES 1.0
 */

public class signals extends JFrame
{
	/**
	 * Window height 
	 * @see #WIDTH
	 */	final static protected int 	HEIGHT = 460;

	/**
	 * Window width 
	 * @see #HEIGHT
	 */	 
	final static protected int 	WIDTH  = 650;
	 /**
	  * Instance of the utilities class in this class. This instance is 
	  * initialized in the constructor and it is provided by NES main window.
	  * @see utilities
	  * @see #signals
	  */	
	public utilities	_utils;
	/**
	 * Instance of the class DBAcc. Initialized in the constructor
	 * @see DBAcc
	 */
	public DBAcc		db    ;

	//{{DECLARE_CONTROLS AND Dbase
	/**
	 * TextField with the Effectivity Value.
	 */
	public JTextField 	TextEffectivity;
	/**
	 * TextField with the Project Value.
	 */
	public JTextField 	TextProject;
	/**
	 * TextField with the number of signals.
	 */
	public JTextField 	TextSignals;
	/**
	 * TextField with the Schematic Value.
	 */
	public JTextField 	TextSchematic;
	/**
	 * TextField with the Circuit Value.
	 */
	public JTextField 	TextCircuit;
	/**
	* This string has the project in format 80,81...
	*/
	public String		strProject;
	/**
	 * Table that is where there is the data for the ListScrollPane.
	 */
	public JTable		myTable;
	/**
	 * Toolbar for this frame.
	 */
	public JToolBar		myToolBar;
	/**
	* 
	*/
	public boolean isAscending = true;
	/**
	* View Selected Signals / Devices
	*/
	public boolean SIGNALS= true;
	/**
	* Variable for the item Sort in menu Tools
	* @see #buildToolsMenu
	*/
	public JMenu 		ItemToolsSort;
	public int SubIndex=0;	
	public ActionListener ItemToolsSortListener;
	/**
	* Contain the Schematic ID and Effectivity ID.
	*/	
	public int SchId, EffId;
	/**
	 * Object that refer to this window.
	 */
	private JFrame 		win;
	/**
	 * Constains objects that refer to child windows.
	 */	
	 public boolean EPD=false;
	private Vector		windows=new Vector();
	/**
	* Constructor for signals with data from file. From EPD connect to run the update routines.
	* <p>	
	* @param  Project  Project Value
	* @param  Effectivity  Effectivity Value
	* @param  Schematic Schematic Value
	* @param Circuit Circuit Value
	* @param utils	Utilities instance	
	* @see       #Signals
	* @see	updatedef
	* @since     NES 1.0
	*/	
	


	public signals(String Project, String Effectivity, String Schematic,
		       String Circuit, utilities Utils)
	{
	
	
		//{{INIT_CONTROLS
		super(" Definition: List of Signals  ");	
		_utils=Utils;
		win=this;
		db=new DBAcc(_utils);
		this.setSize(WIDTH, HEIGHT);		
		this.setLocation (100,100);
		strProject=Project;
		int _project=new Integer(strProject).intValue();
		updatedef defifinition = new updatedef(Schematic,Effectivity,
				Circuit,_project);
				
                db.table.select("SELECT id from collection where name='"+
		Effectivity+"'");
		if (db.table.getRowCount()!=0)
		{
		EffId=new Integer(db.table.getValueAt (0,0).toString().trim()).
			intValue();
		}
		else EffId=0;
		db.table.select("SELECT id from effschematic where schematic='"+
		Schematic+"'");
		if (db.table.getRowCount()!=0)
		{
		SchId=new Integer(db.table.getValueAt (0,0).toString().trim()).
			intValue();
		}
		else SchId=0;
		
		win=this;		
		EPD=true;
		
		Panels(Effectivity,Schematic,Circuit);
	}	
	/**
	* Constructor for signals with data selected in table from the window Definition.
	* <p>
	* @param  Project  Project Value
	* @param  Effectivity  Effectivity Value
	* @param  Schematic Schematic Value
	* @param Circuit Circuit Value
	* @param utils	Utilities instance	
	* @param _sch Schematic ID
	* @param _eff Effectivity ID
	* @see       Signals
	* @since     NES 1.0
	*/	
	public signals(String Project, String Effectivity, String Schematic,
		       String Circuit, utilities Utils,int _sch,int _eff)
	{
		//{{INIT_CONTROLS
		super(" Definition: List of Signals  ");	
		_utils=Utils;
		db=new DBAcc(_utils);
		EffId=_eff;
		SchId=_sch;
		win=this;
		this.setSize(WIDTH, HEIGHT);		
		this.setLocation (100,100);
		win=this;
		EPD=false;
		strProject=Project;
		Panels(Effectivity,Schematic,Circuit);
	}	
	
	/**
	* Create all interface for this window.
	* @see #Data
	*/
	public void Panels(String strEffect, String strSchem,String strCircuit)
	{
		
		
		// Create and add ToolBar
				
		menus();		
		GridBagLayout gridBagLayout= new GridBagLayout();		
		GridBagConstraints gbc = new GridBagConstraints();		
		this.getContentPane().setLayout(gridBagLayout);
		gbc.gridwidth = GridBagConstraints.REMAINDER;			
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5,5,5,5);
						
		JPanel myPan = addToolBar();
		gridBagLayout.setConstraints(myPan,gbc);				
		this.getContentPane ().add(myPan);										
		myPan.setBackground(_utils.toolBarBackgroundColor);
		
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.WEST;			
		gbc.insets = new Insets(0,5,5,5);
		JPanel myPanel = Data();
		myPanel.setLayout(gridBagLayout);
		gridBagLayout.setConstraints(myPanel,gbc);
		myPanel.setBackground(_utils.panelBackgroundColor);
		myPanel.setBorder(_utils.masterDataPanelBorder);
		this.getContentPane ().add(myPanel);						

		JPanel _myPanel=new JPanel();
		_myPanel.setLayout(gridBagLayout);
		gridBagLayout.setConstraints(_myPanel,gbc);
		myPanel.add(_myPanel);
		_myPanel.setOpaque(false);
		// JPanel to Table
		gbc.weighty = 13;
		gbc.insets = new Insets(0,5,5,5);		
		myTable=db.createTable();
		JScrollPane ListScroll = new JScrollPane(myTable);
		ListScroll.setFont(_utils.defaultTableFont);
		myTable.setAutoResizeMode (JTable.AUTO_RESIZE_OFF);
		myTable.setFont(_utils.defaultTableFont);
		myTable.getTableHeader().setFont(_utils.TableHeaderFont);
		ListScroll.setBackground(_utils.tableBackgroundColor);			
		gridBagLayout.setConstraints(ListScroll, gbc);
		ListScroll.setForeground(_utils.tableForegroundColor);					
		this.getContentPane().add(ListScroll);
		myTable.setSelectionMode(0);	
		//}}
		
		// Text in Textfields
		TextEffectivity.setText(strEffect);
		TextSchematic.setText(strSchem);
		TextCircuit.setText(strCircuit);		
		TextProject.setText(strProject);

		win.setVisible(true);								

		addMouseListenerInTable();
		// LISTENERS
		this.addWindowListener (new WindowAdapter () {
					public void windowClosing (WindowEvent e) {
						dispose();
					}});
		this.addWindowListener (new WindowAdapter () {
					public void windowActivated (WindowEvent e) {
					if (SIGNALS)
					SelectSignals();
					}});
					
	}
	/**
	 * Create the panel in the frame. The panel contain the combobox 
	 * and the textfields.
	 */
	
	public JPanel Data()
	{
		
		GridBagConstraints gbC = new GridBagConstraints();
		GridBagLayout 	   gbL= new GridBagLayout();	
		gbC.anchor = GridBagConstraints.WEST;		
		gbC.gridwidth = GridBagConstraints.RELATIVE;
		gbC.fill = GridBagConstraints.BOTH;
		
		JPanel Panel = new JPanel ();	
		
		JPanel Panel0 = new JPanel ();	
		JPanel Panel1 = new JPanel ();	
		JPanel Panel1_= new JPanel ();	
		JPanel Panel1__= new JPanel ();	
		Panel.setLayout(gbL);
		gbL.setConstraints(Panel,gbC);
		Panel0.setLayout(gbL);		
		gbL.setConstraints(Panel0,gbC);

		Panel1.setLayout(gbL);		

		
		JLabel 	LabelProject=new JLabel("   Project "),
			LabelEffectivity=new JLabel(" Effectivity "),
			LabelSignals=new JLabel("   Signals "),
			LabelCircuit=new JLabel("   Circuit "),	
			LabelSchematic=new JLabel(" Schematic  ");
		
		TextEffectivity=new JTextField(9);
		TextSchematic=new JTextField(19);
		TextSignals=new JTextField(5);
		TextProject=new JTextField(5);			
		TextCircuit=new JTextField(4);			
				
	
		JPanel Panel_empty=new JPanel();
		gbC.gridwidth = GridBagConstraints.REMAINDER;
		gbC.weightx=1;
		gbL.setConstraints(Panel1__,gbC);				
		gbL.setConstraints(Panel_empty,gbC);		
		gbL.setConstraints(TextSignals,gbC);		
		Panel1_.setLayout(gbL);
		Panel1__.setLayout(gbL);		
		gbC.insets = new Insets(0,0,10,0);
		gbL.setConstraints(Panel1_,gbC);		
		
		gbC.insets = new Insets(10,5,10,0);
		gbL.setConstraints(LabelEffectivity,gbC);
		gbC.insets = new Insets(0,5,10,0);
		gbL.setConstraints(LabelSchematic,gbC);		
		Panel0.add(LabelEffectivity);
		Panel0.add(LabelSchematic);	
		Panel1_.add(TextEffectivity);		
		Panel1_.add(LabelProject);
		Panel1_.add(TextProject);				
		Panel1_.add(LabelCircuit);				
		Panel1_.add(TextCircuit);			
		Panel1_.add(LabelSignals);				
		Panel1_.add(TextSignals);	
		Panel1__.add(TextSchematic);			
		Panel1__.add(Panel_empty);					
		Panel1.add(Panel1_);
		Panel1.add(Panel1__);		
		Panel.add(Panel0);
		Panel.add(Panel1);
		Panel1.setOpaque(false);
		Panel0.setOpaque(false);
		Panel1_.setOpaque(false);
		Panel1__.setOpaque(false);
		Panel_empty.setOpaque(false);

		

		LabelEffectivity.setFont(_utils.defaultLabelFont);
		LabelSignals.setFont(_utils.defaultLabelFont);
		LabelSchematic.setFont(_utils.defaultLabelFont);
		LabelCircuit.setFont(_utils.defaultLabelFont);
		LabelProject.setFont(_utils.defaultLabelFont);
		TextProject.setFont(_utils.defaultTextFieldFont);
		TextEffectivity.setFont(_utils.defaultTextFieldFont);
		TextSignals.setFont(_utils.defaultTextFieldFont);
		TextSchematic.setFont(_utils.defaultTextFieldFont);
		TextCircuit.setFont(_utils.defaultTextFieldFont);
		TextCircuit.setFont(_utils.defaultTextFieldFont);		
		LabelEffectivity.setForeground(_utils.defaultTextColor);
		LabelSignals.setForeground(_utils.defaultTextColor);
		LabelSchematic.setForeground(_utils.defaultTextColor);
		LabelCircuit.setForeground(_utils.defaultTextColor);
		LabelProject.setForeground(_utils.defaultTextColor);
		TextProject.setDisabledTextColor(_utils.defaultTextColor);
		TextEffectivity.setDisabledTextColor(_utils.defaultTextColor);
		TextSignals.setDisabledTextColor(_utils.defaultTextColor);
		TextSchematic.setDisabledTextColor(_utils.defaultTextColor);
		TextCircuit.setDisabledTextColor(_utils.defaultTextColor);
		TextProject.setEnabled(false);
		TextEffectivity.setEnabled(false);
		TextSignals.setEnabled(false);				
		TextSchematic.setEnabled(false);						
		TextCircuit.setEnabled(false);								
		LabelProject.setLabelFor(TextProject);
		LabelEffectivity.setLabelFor(TextEffectivity);
		LabelSignals.setLabelFor(TextSignals);		
		LabelSchematic.setLabelFor(TextSchematic);				
		LabelCircuit.setLabelFor(TextCircuit);
	return Panel;
	}


	// =====================================================================================
	/** Takes the data from the table, to be displayed in the window SignalMaster
	 * @see SignalMaster
	 */  
	// =====================================================================================
	
	public void Window_SignalMaster() 
	{
		int Row=myTable.getSelectedRow();
		if (Row==-1)
		{
		      JOptionPane.showMessageDialog(getGlassPane (),
                                    "Please, select one row first !!!");
		}
		else
		{
		String 	id, signal, handle, f1, p1, f2, p2, wtype, col, bund;
			
		id=db.table.getValueAt (Row,0).toString().trim();	
		signal=db.table.getValueAt (Row,1).toString().trim();
	   	try { handle=db.table.getValueAt (Row,2).toString().trim(); }
		catch (java.lang.NullPointerException e) { handle=""; }		
		try { f1=db.table.getValueAt (Row,3).toString().trim(); }
		catch (java.lang.NullPointerException e) { f1=""; }
		try { p1=db.table.getValueAt (Row,4).toString().trim(); }
		catch (java.lang.NullPointerException e) { p1=""; }
		try { p2=db.table.getValueAt (Row,5).toString().trim(); }
		catch (java.lang.NullPointerException e) { p2=""; }
		try { f2=db.table.getValueAt (Row,6).toString().trim(); }
		catch (java.lang.NullPointerException e) { f2=""; }
		try { wtype=db.table.getValueAt (Row,7).toString().trim(); }
		catch (java.lang.NullPointerException e) { wtype=""; }
		try { col=db.table.getValueAt (Row,8).toString().trim(); }
		catch (java.lang.NullPointerException e) { col=""; }
	   	try { bund=db.table.getValueAt (Row,9).toString().trim(); }
		catch (java.lang.NullPointerException e) {    bund=""; }
		
		JFrame f=new SignalMaster(id,signal,handle,f1,p1,p2,f2,wtype,
		 	col,bund,_utils,db);
		windows.addElement(f);
		_utils.WindowsVector.addElement(f);
		}
	
	}
	
	// =====================================================================================
	/** Description: This calls the SignalMaster when the user clicks over one occurence in
	 * the displayed table. 
	 * @see SignalMaster
	 */  
	// =====================================================================================

	public void addMouseListenerInTable() {

              MouseAdapter listMouseListener = new MouseAdapter() {
	      
        	  public void mouseClicked(MouseEvent e) {
                      if(e.getClickCount() == 2 && SIGNALS==true) {		      
			    Window_SignalMaster();
                      }
        	   }
               };
              myTable.addMouseListener(listMouseListener);
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
		c2.insets = new Insets (0,0,0,0);								
		c2.weightx =1.0;			
		
							
		Panel_ToolBar.setBackground(Color.lightGray);	
		
				
		// Create the toolbar
				
		myToolBar= new JToolBar();
		myToolBar.setBorderPainted(false);
		myToolBar.setFloatable (false);
		
		// Call addTool Function
		
		addTool(myToolBar,"Close Window","Close",true,ToolClose);
		addTool(myToolBar,"Show Physical Wires","wirelist",true,ToolSh_PWires);		
		addTool(myToolBar,"Store in Database","StoreData",true,ToolStore);
		myToolBar.addSeparator();
		addTool(myToolBar,"Show Signals","ShowSignals",true,ToolSh_Signals);		
		addTool(myToolBar,"Show Devices","ShowDevices",true,ToolSh_Devices);		
		addTool(myToolBar,"Open Browser View","browserView",true,ToolBrowserView);						
		myToolBar.addSeparator();		
		addTool(myToolBar,"Show Detail Information ","ShowInf",true,ToolShowInf);
		addTool(myToolBar,"Find...","Find",true,null);
		addTool(myToolBar,"Filter...","Filter",true,null);
		myToolBar.addSeparator();										

		addTool(myToolBar,"Highlight Component","HighlightComp",true,null);
		addTool(myToolBar,"Stop Highlighting","Stop",true,null);

		myToolBar.addSeparator();
		addTool(myToolBar,"Print the current View","Print",true,null);		
		addTool(myToolBar,"Show Preferences","Preferences",true,ToolPreferences);		
		addTool(myToolBar,"Open Context Help","Help",true,null);
		
		Bar_Layout.setConstraints(myToolBar,c2);		
		
		myToolBar.putClientProperty ("JToolBar.isRolover",Boolean.FALSE);

		Bar_Layout.setConstraints(myToolBar,c2);		
				
		Panel_ToolBar.add(myToolBar);		
		
		return Panel_ToolBar;
		
		
	}
	/**
	 * Calls the browser view.
	 * @see browser
	 */
	ActionListener ToolBrowserView = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener
		int Row=myTable.getSelectedRow();
		String Schematic;
		int _EffId;
		JFrame f=new browser (TextSchematic.getText().trim(),EffId);
		windows.addElement(f);
		_utils.WindowsVector.addElement(f);
	}};
	

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
	//******************************************************************
	// Listerners
	//******************************************************************	
	
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
	 * Calls the list of physical wires for the specify schematic and effectivity
	 * @see wire
	 */
	ActionListener ToolSh_PWires = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener	
		JFrame f=new wire(TextEffectivity.getText(),TextSchematic.getText(),
				TextProject.getText(),_utils);
		windows.addElement(f);
		_utils.WindowsVector.addElement(f);
	}};
	/**
	 * Call the update routines for definition module. Update the database.
	 * @see updatedef
	 */	ActionListener ToolStore = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener
		int _project=new Integer(_utils.project_no_zeros(strProject)).
				intValue();
		updatedef def = new updatedef(TextSchematic.getText(),TextEffectivity.getText(),
				TextCircuit.getText(),_project);
		SelectSignals();				
		
	}};	
	/**
	 * Select the list of signals.
	 * @see #SelectSignals
	 */
	
	ActionListener ToolSh_Signals = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener			
		SelectSignals();		
	}};
	/**
	 * Select the list of devices.
	 * @see #SelectDevices
	 */	
	ActionListener ToolSh_Devices = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener			
		SelectDevices();		
	}};
	/**
	 * Listener to call Preferences window.
	 * @see #preferences
	 * @see preferences
	 */	
	ActionListener ToolPreferences = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener			
		_utils.WindowsVector.addElement(new winPrefs (_utils,4));
	}};
	/**
	 * Listener to call the SignalMaster window.
	 * @see #Window_SignalMaster
	 * @see SignalMaster
	 */		
	 ActionListener ToolShowInf = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener			
		if (SIGNALS==true)
			Window_SignalMaster();
		else {
		      JOptionPane.showMessageDialog(getGlassPane (),
                                    "Show Signals Information!!!");
		}
	}};	
	/**
	 * Create the menus. 
	 * <p>
	 * To simplify, it parts the operation into smaller ones:
	 * buildFileMenu(),
	 * buildEditMenu(),
	 * buildToolsMenu(),
	 * buildViewMenu(),
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
		JMenu MenuTools = buildToolsMenu();
		JMenu MenuView = buildViewMenu();
		JMenu MenuHelp = buildHelpMenu();
		myMenu.add(MenuFile);
		myMenu.add(MenuEdit);
		myMenu.add(MenuTools);
		myMenu.add(MenuView);		
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
		
		ItemFileNew.setEnabled(false);	

		ItemFileOpen.addActionListener(ToolSh_PWires);
		
		ItemFileClose.addActionListener(ToolClose);
		
		ItemFileSave.addActionListener(ToolStore);
				
		ItemFilePrintSet.addActionListener(new ActionListener () {
                public void actionPerformed (ActionEvent e) {    
			// Code Listener			
		}});			
		
		ItemFilePrint.addActionListener(new ActionListener () {
                public void actionPerformed (ActionEvent e) {    
			// Code Listener			
		}});			

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
		MenuFile.addSeparator();			
		MenuFile.add(ItemFileSave);
		MenuFile.addSeparator();		
		MenuFile.add(ItemFilePrintSet);		
		MenuFile.add(ItemFilePrint);		
		MenuFile.addSeparator();		
		MenuFile.add(ItemFileQuit);					
		return MenuFile;	
	}
	
	/**
	 * Creates the submenus for the menu "Edit" 
	 */
	
	protected JMenu buildEditMenu()
	{
		JMenu MenuEdit = new JMenu("Edit");
		JMenuItem ItemEditUndo = new JMenuItem("Undo");
		JMenuItem ItemEditCut = new JMenuItem("Cut");
		JMenuItem ItemEditCopy = new JMenuItem("Copy");
		JMenuItem ItemEditPaste = new JMenuItem("Paste");
		JMenuItem ItemEditDelete = new JMenuItem("Delete");
		JMenuItem ItemEditPre = new JMenuItem("Preferences...");
		
		// {{ Listeners

		ItemEditPre.addActionListener(new ActionListener () {
                public void actionPerformed (ActionEvent e) {    
			// Code Listener			
		}});	
						
		// }} Listeners
		ItemEditUndo.setFont(_utils.defaultMenuFont);
		ItemEditCut.setFont(_utils.defaultMenuFont);
		ItemEditCopy.setFont(_utils.defaultMenuFont);
		ItemEditPaste.setFont(_utils.defaultMenuFont);
		ItemEditDelete.setFont(_utils.defaultMenuFont);
		ItemEditPre.setFont(_utils.defaultMenuFont);
		MenuEdit.setFont(_utils.defaultMenuFont);
		
		MenuEdit.add(ItemEditUndo);		
		ItemEditUndo.setEnabled(false);	
		MenuEdit.addSeparator();			
		MenuEdit.add(ItemEditCut);
		ItemEditCut.setEnabled(false);		      
		MenuEdit.add(ItemEditCopy);
		ItemEditCopy.setEnabled(false);		      
		MenuEdit.add(ItemEditPaste);
		ItemEditPaste.setEnabled(false);		      
		MenuEdit.add(ItemEditDelete);
		ItemEditDelete.setEnabled(false);		      						
		MenuEdit.add(ItemEditPre);				
		return MenuEdit;		
	}

	/**
	 * Creates the "Tools" menu and submenus. 	 
	 * @see #ControlMenuSort()
	 */
	

	protected JMenu buildToolsMenu()
	{
	
		JMenu MenuTools = new JMenu("Tools");
		JMenuItem ItemToolsFind = new JMenuItem("Find...");
		JMenuItem ItemToolsFindagain = new JMenuItem("Find again");
		JMenuItem ItemToolsFilter = new JMenuItem("Filter...");
		ItemToolsSort = new JMenu("Sort");

		ItemToolsFind.setFont(_utils.defaultMenuFont);
		ItemToolsFindagain.setFont(_utils.defaultMenuFont);
		ItemToolsFilter.setFont(_utils.defaultMenuFont);
		MenuTools.setFont(_utils.defaultMenuFont);
		ItemToolsSort.setFont(_utils.defaultMenuFont);

		
		MenuTools.add(ItemToolsFind);
		MenuTools.add(ItemToolsFindagain);
		MenuTools.addSeparator();
		MenuTools.add(ItemToolsFilter);
		MenuTools.add(ItemToolsSort);
		ControlMenuSort();
		return MenuTools;
	}

	/**
	 * Creates the "View" menu and submenus. 
	 */
	
	
	protected JMenu buildViewMenu()
	{
	
		JMenu MenuView = new JMenu("View");						
		JMenuItem ItemViewSignals = new JMenuItem("Signals");
		JMenuItem ItemViewDevices = new JMenuItem("Devices");
		JMenuItem ItemViewBrowser = new JMenuItem("Browser");
		JMenuItem ItemViewSelect = new JMenuItem("Highlight");
		JMenuItem ItemViewUnselect = new JMenuItem("Stop Highlight");
								
		
		// {{ Listeners

		ItemViewSignals.addActionListener(ToolSh_Signals);
		
		ItemViewDevices.addActionListener(ToolSh_Devices);

		// }} Listeners
		ItemViewSignals.setFont(_utils.defaultMenuFont);		
		ItemViewDevices.setFont(_utils.defaultMenuFont);		
		ItemViewBrowser.setFont(_utils.defaultMenuFont);
		ItemViewSelect.setFont(_utils.defaultMenuFont);		
		ItemViewUnselect.setFont(_utils.defaultMenuFont);		
		MenuView.setFont(_utils.defaultMenuFont);
		MenuView.add(ItemViewSignals);
		MenuView.add(ItemViewDevices);
		MenuView.add(ItemViewBrowser);
		MenuView.addSeparator();
		MenuView.add(ItemViewSelect);
		MenuView.add(ItemViewUnselect);		

		return MenuView;
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
		
		ItemHelpIntro.addActionListener(new ActionListener () {
                public void actionPerformed (ActionEvent e) {    
			// Code Listener			
		}});			

		ItemHelpRefer.addActionListener(new ActionListener () {
                public void actionPerformed (ActionEvent e) {    
			// Code Listener			
		}});		
		
		ItemHelpOn.addActionListener(new ActionListener () {
                public void actionPerformed (ActionEvent e) {    
			// Code Listener			
		}});

		ItemHelpAbout.addActionListener(new ActionListener () {
                public void actionPerformed (ActionEvent e) {    
			// Code Listener			
		}});			
		
		// }} Listeners
		ItemHelpIntro.setFont(_utils.defaultMenuFont);
		ItemHelpRefer.setFont(_utils.defaultMenuFont);
		ItemHelpOn.setFont(_utils.defaultMenuFont);
		ItemHelpAbout.setFont(_utils.defaultMenuFont);
		MenuHelp.setFont(_utils.defaultMenuFont);
		MenuHelp.add(ItemHelpIntro);
		MenuHelp.add(ItemHelpRefer);
		MenuHelp.add(ItemHelpOn);
		MenuHelp.addSeparator();
		MenuHelp.add(ItemHelpAbout);
		return MenuHelp;		
	}
		
	/**
	 * Creates the submenus for the submenu "Sort" in menu Tools
	 * This submenu depend from the view selected (Signals or Devices).
	 * @see #ChangeMenuSort	 
	 */
	public void ControlMenuSort()
	{	
		ItemToolsSortListener = new ActionListener () 
		{
			public void actionPerformed (ActionEvent e) 
			{
			if (myTable.getRowCount() != 0)
			{

				String actionString = e.paramString();
				int index = actionString.indexOf ("=");				
				String mySubMenu = new String (actionString.substring (index + 4));			        
				index = myTable.getTableHeader().getColumnModel().getColumnIndex(mySubMenu);
				SubIndex=index;
				db.getSorter().sortByColumn(index,isAscending);				
				}
			}
		};
		ChangeMenuSort();
	}	
	/**
	* Apply the corrects submenus in the menu sort, depending the current view.
	*/
	public void ChangeMenuSort()
	{
		 int i=0;
		 boolean scan=true;
		 ItemToolsSort.removeAll();

		 ButtonGroup group0 = new ButtonGroup();		
		 String string;

		 while (scan)
		 {
			 String auxStr = "Column"+i;
			 try
			 {
				 if (SIGNALS)
					 string = _utils.resHeaderSignals.getString (auxStr);
				 else
					 string = _utils.resHeaderDevices.getString (auxStr);
				 StringBuffer strBuffer = new StringBuffer (string);
				 JRadioButtonMenuItem  newItem = new JRadioButtonMenuItem  ("By "+string);
				newItem.setFont(_utils.defaultMenuFont);
				 if (i==0)
				 {
					 newItem.setSelected(true);
				 }
				 newItem.addActionListener (ItemToolsSortListener);
				 ItemToolsSort.add(newItem);				
				 group0.add(newItem);		
			 } catch (MissingResourceException e)
			 {
				 scan = false;
			 }
			 i++;
		 }
			
		
		// ASCENDING / DESCENDING
		 
		ItemToolsSort.add(new JSeparator());
		
		ActionListener ascendingListener = new ActionListener ()
		{
		public void actionPerformed (ActionEvent e)

		{
			String actionString = e.paramString();
			int index = actionString.indexOf("=");
			String mySubMenu = new String (actionString.substring (index + 1));
			
			if (mySubMenu.equals("Ascending") && (isAscending==false))
			{
				isAscending = true;
				
				db.getSorter().sortByColumn(SubIndex,isAscending);										
				
			}
			else if (mySubMenu.equals("Descending") && (isAscending==true))
			{
				isAscending = false;	
				db.getSorter().sortByColumn(SubIndex,isAscending);						
				
			}
		}
		};
		
		ButtonGroup group = new ButtonGroup();
		JRadioButtonMenuItem ascending = new JRadioButtonMenuItem ("Ascending");
		group.add(ascending);
		ascending.setSelected (true);
		ascending.addActionListener (ascendingListener);
		ItemToolsSort.add(ascending);		
		JRadioButtonMenuItem descending = new JRadioButtonMenuItem ("Descending");
		descending.addActionListener (ascendingListener);
		group.add(descending);
		ItemToolsSort.add(descending);
		ascending.setFont(_utils.defaultMenuFont);
 		descending.setFont(_utils.defaultMenuFont);
	}

	/**
	 * Executes a SELECT. The DBAcc instance provides the methods needed to do it.
	 * It is selected the signals for the respective schematic and effectivity
	 * @see DBAcc
	 * @see #SelectDevices
	 */

        public void SelectSignals()
        {
		SIGNALS=true;
		ChangeMenuSort();
		db.table.setColumnTitle(_utils.resHeaderSignals);	
                db.table.select("SELECT DISTINCT V_D.Id,V_D.Name,V_D.Hname,"  +
				"V_D.F1Name,V_D.T1Name,V_D.T2Name,V_D.F2Name,"+
				" V_D.Wiretype, V_D.Color, V_D.BundleName "   +
				"FROM V_DefLink V_D where EffId =" + EffId  +
				" AND SchId = "+SchId,_utils.resHeaderSignals);
		Integer rowCount = new Integer(myTable.getRowCount());
		String myRowCount = new String(rowCount.toString());
		TextSignals.setText(myRowCount);		
                db.table.initColumnSizes (myTable);
		Preferences();
        }
	
	/**
	 * Executes a SELECT. The DBAcc instance provides the methods needed to do it.
	 * It is selected the devices for the respective schematic and effectivity
	 *<p>
	 * <center><img src="definition/devices.gif" alt="Devices View" ></center>
	 * @see #SelectSignals
	 * @see #Signals
	 * @see DBAcc
	 */
	
        public void SelectDevices()
        {
		SIGNALS=false;
		ChangeMenuSort();
                db.table.setColumnTitle(_utils.resHeaderDevices);		
                db.table.select("SELECT DISTINCT VE.FINNAME, VE.DESCRIPTION, VE.STATUS " +
				"FROM V_DefEquip VE where EffId = "+EffId+
				" AND SchId = "+ SchId+" order by FinName",_utils.resHeaderDevices);

		Integer rowCount = new Integer(myTable.getRowCount());
		String myRowCount = new String(rowCount.toString());
		TextSignals.setText(myRowCount);		
                db.table.initColumnSizes (myTable);
        }	
	/**
	 * Calls the preferences window
	 * @see preferences
	 */
	private void Preferences()	
	{	
	int Column_Count=myTable.getColumnCount();
	  if (_utils.getProperty("SIG_ID").compareTo("False")==0)
	  	myTable.removeColumn(myTable.getColumnModel().getColumn(0));
	  if (_utils.getProperty("SIG_HANDLENAME").compareTo("False")==0)
	      myTable.removeColumn(myTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,2,myTable.getColumnCount())));
	  if (_utils.getProperty("SIG_FIN1").compareTo("False")==0)
	      myTable.removeColumn(myTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,3,myTable.getColumnCount())));
	  if (_utils.getProperty("SIG_PIN1").compareTo("False")==0)
	      myTable.removeColumn(myTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,4,myTable.getColumnCount())));
	  if (_utils.getProperty("SIG_PIN2").compareTo("False")==0)
	      myTable.removeColumn(myTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,5,myTable.getColumnCount())));
	  if (_utils.getProperty("SIG_FIN2").compareTo("False")==0)
	      myTable.removeColumn(myTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,6,myTable.getColumnCount())));
	  if (_utils.getProperty("SIG_WIRETYPE").compareTo("False")==0)
	      myTable.removeColumn(myTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,7,myTable.getColumnCount())));
	  if (_utils.getProperty("SIG_COLOR").compareTo("False")==0)
	      myTable.removeColumn(myTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,8,myTable.getColumnCount())));
	  if (_utils.getProperty("SIG_BUNDLE").compareTo("False")==0)
	      myTable.removeColumn(myTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,9,myTable.getColumnCount())));
		
	}
	/**
	 * Disposes the frame and all childs windows.
	 */ 
        public void dispose()
        {	
		JFrame window;
		if (EPD)
		{
			System.out.println("Java Nes Closed...");		
			System.exit(0);
		}
		for (int i=0;i<windows.size();i++)
		{
		 try{
			window=(JFrame)windows.elementAt(i);
			window.dispose();
		}
		catch(java.lang.NullPointerException e)
		{ }
		
		}
		super.dispose();
        } // close
	
}	
	
