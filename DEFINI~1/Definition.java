/*===========================================================================
	
	Definition.java
	
	( October 1998)				
	
===========================================================================*/


import javax.swing.*;
import java.awt.*;
import java.lang.*;
import java.util.*;
import java.awt.event.*;

import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.event.TableModelEvent;

/*===========================================================================
  Class Definition
===========================================================================*/

/**
 * A <code>Definition</code> is an extension of javax.swing.JFrame and implements
 * the window to show all schematics for a selected project. 
 * The displayed schematics can be filtered in the Textfields Effectivity and Circuit.
 * <p>
 * <center><img src="definition/Definition.gif" alt="Definition View" ></center>
 * <p><p>The <a href="updatedef.html"> Update routines </a>for the definition module are presented in Class 
 * updatedef.
 * @version 	1.01, 03/03/99
 * @author 	Cristiana Areias
 * @author 	Aires Marques
 * @see NewDocument
 * @see signals
 * @see SignalMaster
 * @see wire
 * @see WireMaster
 * @see updatedef 
 * @see nes
 * @see javax.swing.JFrame
 * @since       NES 1.0
 */


public class Definition extends JFrame
{
	/**
	 * Window height 
	 * @see #WIDTH
	 */
	final static protected int WIDTH=650;
	/**
	 * Window width 
	 * @see #HEIGHT
	 */
	final static protected int HEIGHT=460;
	 /**
	  * Instance of the utilities class in this class. This instance is 
	  * initialized in the constructor and it is provided by NES main window.
	  * @see utilities
	  * @see #Definition(utilities _util,Object _P) 
	  */
	public utilities	_utils;
	/**
	 * Instance of the class DBAcc. Initialized in the constructor
	 * @see DBAcc
	 */
	public DBAcc		db;
	/**
	 * Table that is where there is the data for the ListScrollPane.
	 */
	public JTable		myTable;
	
	/** 
	 * Menubar from this class 
	 */

	public JMenuBar 	myMenu; 
	/**
	 * Toolbar for this frame.
	 */
	public JToolBar		myToolBar;
	/**
	 * TextField with the Effectivity, Circuit and Project value.
	 */
	public JTextField 	TextEffectivity,
				TextCircuit;
	public JComboBox	ComboProject;

	/**
	 * Object that refer to this window.
	 */
	public JFrame 		frame;
	/**
	 * Constains objects that refer to child windows.
	 */	
	public Vector		windows=new Vector();
	public boolean PREF=false;
	
	/**
	* Constructs a new Definition module. 
	* <p>
	* The window is not initially visible. Call the <code>show</code> 
	* method to cause the window to become visible.
	* @param     Utilities  Utilities instance
	* @param     _P         The selected element in the main window of NES
	* @see       nes
	* @since     NES 1.0
	*/	
	//******************************************************************	
	// Constructor for Definition
	//******************************************************************
	
	public Definition(utilities _util,Object _P) 
	{	
		super(" Definition ");
		_utils=_util;
		frame=this;
		db = new DBAcc (_utils,false);
		this.setSize(WIDTH, HEIGHT);
		setLocation (50,50);		
		menus();
		
		// Create and add ToolBar
		JPanel	myPan;
						
		GridBagLayout gridBagLayout= new GridBagLayout();		
		GridBagConstraints gbc = new GridBagConstraints();
		this.getContentPane().setLayout(gridBagLayout);

		gbc.gridwidth = GridBagConstraints.REMAINDER;			
		gbc.fill = GridBagConstraints.BOTH;		

		gbc.insets = new Insets(0,0,0,0);
		myPan = addToolBar();
		myPan.setBorder(_utils.toolBarBorder);		
		myPan.setBackground(_utils.toolBarBackgroundColor);		
		gridBagLayout.setConstraints(myPan,gbc);				
		this.getContentPane ().add(myPan);
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.WEST;			
		gbc.insets = new Insets(0,5,5,5);
		JPanel myPanel = Data();
		myPanel.setLayout (new FlowLayout (FlowLayout.LEFT, 3, 3));
		gridBagLayout.setConstraints(myPanel,gbc);
		myPanel.setBackground(_utils.panelBackgroundColor);
		myPanel.setBorder(_utils.masterDataPanelBorder);
		this.getContentPane ().add(myPanel);	

					
		gbc.weighty =13.0;
		gbc.insets = new Insets(0,5,5,5);
		myTable=db.createTable();
		JScrollPane ListScroll;
		ListScroll = new JScrollPane(myTable);


		myTable.setAutoResizeMode (JTable.AUTO_RESIZE_OFF);		
		myTable.setFont(_utils.defaultTableFont);
		myTable.getTableHeader().setFont(_utils.TableHeaderFont);
		ListScroll.setBackground(_utils.tableBackgroundColor);	
		gridBagLayout.setConstraints(ListScroll,gbc);
		ListScroll.setForeground(_utils.tableForegroundColor);
		this.getContentPane ().add(ListScroll);
		myTable.setSelectionMode(0);			
		setVisible(true);		
		ComboProject.setSelectedItem(_P);		
		ComboProject.setFont(_utils.defaultComboBoxFont);
		ComboProject.addActionListener(actionPerformedOnInputFields);
		TextEffectivity.addActionListener(actionPerformedOnInputFields);
		TextCircuit.addActionListener(actionPerformedOnInputFields);
		addMouseListenerInTable();
		ShowData();
		// LISTENERS
		this.addWindowListener (new WindowAdapter () {
					public void windowClosing (WindowEvent e) {
					     dispose();						
					}});					
		this.addWindowListener (new WindowAdapter () {
					public void windowActivated (WindowEvent e) {
					TextEffectivity.grabFocus();
					}});					
		this.addWindowListener (new WindowAdapter () {
					public void windowDeiconified (WindowEvent e) {
					}});
		this.addWindowListener (new WindowAdapter () {
					public void windowActivated (WindowEvent e) {
					if (PREF)
					{
						ShowData();
						PREF=false;
					}
					}});
					
		TextEffectivity.grabFocus();								
	}

	
	
	 /** This ActionListener has the actions to be performed when the user
	  *  selects something from the project combobox or, imputs something in the
	  * effectivity JTextField or the Circuit JTextField
	  */

	ActionListener actionPerformedOnInputFields = new ActionListener () 
	{
	  public void actionPerformed (ActionEvent e) 
	     {
		 ShowData();
		 myTable.repaint();
	     }	     
	};
	// =====================================================================================
	/** Description: This calls the List of Wires when the user clicks over one occurence in
	 * the displayed table. The list of wires is another window, therefore is class too.
	 */  
	// =====================================================================================
	public void addMouseListenerInTable() {

              MouseAdapter listMouseListener = new MouseAdapter() {
	      
        	  public void mouseClicked(MouseEvent e) {
                      if(e.getClickCount() == 2 && myTable.getSelectedRow() !=-1) {
		           int Row=myTable.getSelectedRow();
			  String Project=new String((String)ComboProject.
		  			   getSelectedItem());
			  String Effectivity=new String((db.table.getValueAt (Row,2).
			  		toString().trim()));			  
			  String Schematic=new String((db.table.getValueAt (Row,4).
			  		toString().trim()));			  
			  String Circuit=new String((db.table.getValueAt (Row,3).
			  		toString().trim()));	
		
			   int _EffId=new Integer((db.table.getValueAt (Row,1).
					 toString().trim())).intValue();			  
			   int _SchId=new Integer((db.table.getValueAt (Row,0).
					 toString().trim())).intValue();							
			JFrame f=new signals(Project,Effectivity,
					Schematic,Circuit,_utils,_SchId,_EffId);
			windows.addElement(f);
			_utils.WindowsVector.addElement(f);
			
			
			  
			  
                      }
        	   }
               };
              myTable.addMouseListener(listMouseListener);
	  }
	/**
	 * Creates the panel in the frame which has the combobox and the textfields and controls the ListScroll that is located below.
	 */
	  
	public JPanel Data()
	{
		JPanel Panel = new JPanel ();	
	
		JPanel auxPanel = new JPanel ();
		auxPanel.setOpaque(false);
		Panel.add(auxPanel);
		

		JLabel 	LabelProject=new JLabel("Project "),
			LabelEffectivity=new JLabel(" Effectivity "),
			LabelCircuit=new JLabel("  Circuit ");	
		
		TextEffectivity=new JTextField(8);
		TextCircuit=new JTextField(4);	
		TextEffectivity.setBackground(Color.white);
		TextCircuit.setBackground(Color.white);
		ComboProject = new JComboBox();

		JPanel Panel1 = new JPanel ();		
		auxPanel.add(Panel1);
		JPanel Panel2 = new JPanel ();
		auxPanel.add(Panel2);
		JPanel Panel3 = new JPanel ();
		auxPanel.add(Panel3);
		
		Panel1.setOpaque(false);				
		Panel2.setOpaque(false);				
		Panel3.setOpaque(false);				
		
		LabelProject.setForeground(_utils.defaultTextColor);
		LabelEffectivity.setForeground(_utils.defaultTextColor);
		LabelCircuit.setForeground(_utils.defaultTextColor);
		ComboProject.setForeground(_utils.defaultTextColor);
		TextEffectivity.setForeground(_utils.defaultTextColor);
		TextCircuit.setForeground(_utils.defaultTextColor);	
		
		LabelProject.setFont(_utils.defaultLabelFont);		
		LabelEffectivity.setFont(_utils.defaultLabelFont);		
		LabelCircuit.setFont(_utils.defaultLabelFont);		
		TextEffectivity.setFont(_utils.defaultTextFieldFont);
		TextCircuit.setFont(_utils.defaultTextFieldFont);	

		LabelProject.setLabelFor(ComboProject);
		LabelEffectivity.setLabelFor(TextEffectivity);
		LabelCircuit.setLabelFor(TextCircuit);		
	 	db.fillCBox (ComboProject,"SELECT code FROM Project");
		Panel1.add(LabelProject);
		Panel1.add(ComboProject);		
		Panel2.add(LabelEffectivity);
		Panel2.add(TextEffectivity);		
		Panel3.add(LabelCircuit);				
		Panel3.add(TextCircuit);						

	return Panel;
	}
	
	/**
	 * Creates the menus. 
	 * <p>
	 * To simplify, it parts the operation into smaller ones:
	 * buildFileMenu(),
	 * buildEditMenu(),
	 * buildOptionsMenu(),
	 * buildHelpMenu()
	 * <p>
	 * Some action listeners are added during this process.
	 * @see addActionListener
	 */

	//******************************************************************
	// Build Menus
	//******************************************************************		
	protected void menus()
	{
		myMenu = new JMenuBar();
		myMenu.setOpaque(true);
		JMenu MenuFile = buildFileMenu();	
		JMenu MenuEdit = buildEditMenu();
		JMenu MenuOptions = buildOptionsMenu();		
		JMenu MenuHelp = buildHelpMenu();
		myMenu.add(MenuFile);
		myMenu.add(MenuEdit);
		myMenu.add(MenuOptions);		
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
		JMenu ItemFileOpen = buildItemFileOpen();
		JMenuItem ItemFileClose = new JMenuItem("Close");		
		JMenuItem ItemFileSave = new JMenuItem("Save");
		JMenuItem ItemFilePrintSet = new JMenuItem("Print Settings...");
		JMenuItem ItemFilePrint = new JMenuItem("Print");
		JMenuItem ItemFileQuit = new JMenuItem("Quit");
		
		// {{ Listeners
				
		ItemFileClose.addActionListener(new ActionListener () {
                public void actionPerformed (ActionEvent e) {    
			// Code Listener			
			dispose();    			
		}});
		
				
		ItemFileNew.addActionListener(ToolNew);

		ItemFileQuit.addActionListener(new ActionListener () {
                public void actionPerformed (ActionEvent e) {    
			// Code Listener			
			_utils.quit((JComponent)frame.getContentPane(),
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
	 * Creates the submenus for the submenu "Open" in "File" menu
	 */
	
	protected JMenu buildItemFileOpen()
	{
		JMenu MenuOpen = new JMenu("Open");
		JMenuItem ItemOpenSignal = new JMenuItem("Signal");
		JMenuItem ItemOpenBrowser = new JMenuItem("Browser");		
		JMenuItem ItemOpenSchematic = new JMenuItem("Schematic");
		JMenuItem ItemOpenMaster = new JMenuItem("Master");
		JMenuItem ItemOpenTree = new JMenuItem("Tree View");

		ItemOpenSignal.addActionListener(ToolWireList);
		ItemOpenSignal.setFont(_utils.defaultMenuFont);
		ItemOpenBrowser.setFont(_utils.defaultMenuFont);
		ItemOpenSchematic.setFont(_utils.defaultMenuFont);
		ItemOpenMaster.setFont(_utils.defaultMenuFont);
		ItemOpenTree.setFont(_utils.defaultMenuFont);
		MenuOpen.setFont(_utils.defaultMenuFont);
		MenuOpen.add(ItemOpenSignal);
		MenuOpen.add(ItemOpenBrowser);
		MenuOpen.add(ItemOpenSchematic);
		MenuOpen.add(ItemOpenMaster);
		MenuOpen.add(ItemOpenTree);	
									
		return MenuOpen;
	}
	/**
	 * Creates the "Edit" menu and submenus. 
	 */
	
	
	protected JMenu buildEditMenu()
	{
		JMenu MenuEdit = new JMenu("Edit");
		JMenuItem ItemEditUndo = new JMenuItem("Undo");
		JMenuItem ItemEditRedo = new JMenuItem("Redo");		
		JMenuItem ItemEditCut = new JMenuItem("Cut");
		JMenuItem ItemEditCopy = new JMenuItem("Copy");
		JMenuItem ItemEditPaste = new JMenuItem("Paste");
		JMenuItem ItemEditDelete = new JMenuItem("Delete");		
		
		ItemEditUndo.setFont(_utils.defaultMenuFont);
		ItemEditRedo.setFont(_utils.defaultMenuFont);
		ItemEditCut.setFont(_utils.defaultMenuFont);
		ItemEditCopy.setFont(_utils.defaultMenuFont);
		ItemEditPaste.setFont(_utils.defaultMenuFont);
		ItemEditDelete.setFont(_utils.defaultMenuFont);
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
		MenuEdit.add(ItemEditDelete);
		
		ItemEditDelete.addActionListener(new ActionListener () {
                public void actionPerformed (ActionEvent e) {    
			// Code Listener			
			Delete();
		}});			

		
		return MenuEdit;		
	}
	
	/**
	 * Creates the "Options" menu and submenus. 
	 */
	

	
	protected JMenu buildOptionsMenu()
	{
		JMenu MenuOptions = new JMenu("Options");
		JMenuItem ItemOptionsPref = new JMenuItem("Preferences");
		ItemOptionsPref.setFont(_utils.defaultMenuFont);
		MenuOptions.setFont(_utils.defaultMenuFont);
		// Add into menu		
		MenuOptions.add(ItemOptionsPref);
		return MenuOptions;		
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
		Panel_ToolBar.setOpaque(false);		
		GridBagConstraints c2=new GridBagConstraints();								
		
		c2.fill = GridBagConstraints.HORIZONTAL;			
		c2.insets = new Insets (4,4,3,5);								
		c2.weightx =1.0;			
				
		// Create the toolbar
		myToolBar= new JToolBar();		
		myToolBar.setBorderPainted(false);
		myToolBar.setOpaque(false);
		myToolBar.setFloatable (false);
		// Call addTool Function
		
		addTool(myToolBar,"Close Window","Close",true,ToolClose);
		myToolBar.addSeparator();				
		addTool(myToolBar,"Show Signals in List View","wirelist",true,ToolWireList);				
		addTool(myToolBar,"Show Signals in Browser View","browserView",true,ToolBrowserView);		
		addTool(myToolBar,"New Document","New",true,ToolNew);		
		myToolBar.addSeparator();								
		addTool(myToolBar,"Open Schematic","OpenSchem",true,null);
		addTool(myToolBar,"Show Detail Information ","ShowInf",true,null);
		addTool(myToolBar,"Find...","Find",true,null);
		addTool(myToolBar,"Filter...","Filter",true,null);						
		myToolBar.addSeparator();										
		addTool(myToolBar,"Print the current View","Print",true,ToolPrint);				
		addTool(myToolBar,"Show Preferences for this Window","Preferences",true,
				  ToolPreferences);		
		addTool(myToolBar,"Open Context Help","Help",true,null);						
		
		Bar_Layout.setConstraints(myToolBar,c2);		
		
		myToolBar.putClientProperty ("JToolBar.isRolover",Boolean.FALSE);

		Bar_Layout.setConstraints(myToolBar,c2);		
				
		Panel_ToolBar.add(myToolBar);		

		return Panel_ToolBar;
		
		
	}
	
	/** 
	 * Add an item to toolbar
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
	 */

	ActionListener ToolClose = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    		
	
		// Code Listener			
		dispose();    
	}};
	
	/**
	 * Calls the list of signals for one specify schematic with a certain effectivity
	 * @see signals
	 */
	ActionListener ToolWireList = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener
		
		int Row=myTable.getSelectedRow();
		String Project,Effectivity,Schematic,Circuit;
		int _EffId,_SchId;
		
		if (Row!=-1)
		{
		Project= new String((String)ComboProject.
		  			   getSelectedItem());
		Effectivity=new String((db.table.getValueAt (Row,2).
			      toString().trim()));			  
		Schematic=new String((db.table.getValueAt (Row,4).
			      toString().trim()));			  
		Circuit=new String((db.table.getValueAt (Row,3).
			      toString().trim()));
		_EffId=new Integer((db.table.getValueAt (Row,1).
			      toString().trim())).intValue();			  
		_SchId=new Integer((db.table.getValueAt (Row,0).
			      toString().trim())).intValue();					      
		}
		else
		{
		Project="";
		Effectivity="";
		Schematic=""; 
		Circuit="";
		_EffId=0;
		_SchId=0;
		}
 	        JFrame f=new signals(Project,Effectivity,
				Schematic,Circuit,_utils,_SchId,_EffId);
		windows.addElement(f);
		_utils.WindowsVector.addElement(f);
				
				
	}};	
	
	/**
	 * Calls New Document window to create a new document for one schematic
	 * @see NewDocument
	 */

	ActionListener ToolNew = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener
		JFrame f=new NewDocument(new Integer(_utils.project_no_zeros(
				   (String)ComboProject.getSelectedItem())).
				   intValue());
		windows.addElement(f);
		_utils.WindowsVector.addElement(f);
	}};	
	
	/**
	 * Calls the preferences window
	 * @see preferences
	 */
	
	ActionListener ToolPreferences = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener			
		PREF=true;
		_utils.WindowsVector.addElement(new winPrefs (_utils,3));
	}};	
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
		
		if (Row!=-1)
		{
		Schematic=new String((db.table.getValueAt (Row,4).
			      toString().trim()));			  
		_EffId=new Integer((db.table.getValueAt (Row,1).
			      toString().trim())).intValue();			  
		}
		else
		{
		Schematic=""; 
		_EffId=0;
		}
		JFrame f=new browser (Schematic,_EffId);
		windows.addElement(f);
		_utils.WindowsVector.addElement(f);
	}};
	
	
	/**
	 * Prints the data that was selected.
	 * @see print
	 */
	ActionListener ToolPrint = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener				
		new Print (frame, myTable, frame.getTitle(), "Project:" + 
		(String)ComboProject.getSelectedItem() + ";", null);	
	}};	
	
	
	/**
	 * Executes a SELECT. The DBAcc instance provides the methods needed to do it.
	 * It is selected all schematics
	 * @see DBAcc
	 */

	public void ShowData ()
	{
	
	   final String Effectivity = new String(TextEffectivity.getText()),
	   		Circuit     = new String(TextCircuit.getText()),
			Project     =  new String(_utils.project_no_zeros(
				    (String)ComboProject.getSelectedItem()));
           db.table.setColumnTitle(_utils.resHeaderDefinition);					   
	   db.table.select("SELECT DISTINCT s.id, s.effId, c.name, cir." +
	   	"code, RTRIM(s.Schematic), RTRIM(s.Filename), c.Lower, "   +
		"c.Upper, c.Version " +
		"FROM effSchematic s, Collection c, Circuit cir,Project p,"+
		 " BaseAirplane ba, Model m, Airplane a, AirplGroup ag "   +
		 "WHERE s.effId = c.id "			+ 
		  effectivity.InsertInSelect(Effectivity) + 
		 " AND cir.code LIKE '"+Circuit+"%' AND s.CirID=cir.ID AND " +
		 "p.Id = "+Project + " AND p.ID = ba.PrId AND ba.ID = "    +
		 "m.BaId AND m.Id = a.mId AND a.Id = ag.aId AND ag.cId = " +
		 "c.Id ORDER BY cir.code, c.Version, c.Lower,c.Upper",
		 _utils.resHeaderDefinition);
           db.table.initColumnSizes (myTable);
	   if (myTable.getRowCount()!=0)	   
		myTable.setRowSelectionInterval(0,0);  
            Preferences();
	}
	/**
	 * Calls the preferences window
	 * @see preferences
	 */
	private void Preferences()	
	{
	   int Column_Count=myTable.getColumnCount();
	  if (_utils.getProperty("SYS_ID").compareTo("False")==0)	  	
	  	myTable.removeColumn(myTable.getColumnModel().getColumn(0));
	  if (_utils.getProperty("SYS_EFFID").compareTo("False")==0)
	      myTable.removeColumn(myTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,1,myTable.getColumnCount())));
	  if (_utils.getProperty("SYS_EFFECTIVITY").compareTo("False")==0)
	      myTable.removeColumn(myTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,2,myTable.getColumnCount())));
	  if (_utils.getProperty("SYS_CIRCUIT").compareTo("False")==0)
	      myTable.removeColumn(myTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,3,myTable.getColumnCount())));
	  if (_utils.getProperty("SYS_SCHEMATIC").compareTo("False")==0)
	      myTable.removeColumn(myTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,4,myTable.getColumnCount())));
	  if (_utils.getProperty("SYS_FILENAME").compareTo("False")==0)
	      myTable.removeColumn(myTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,5,myTable.getColumnCount())));
	}
	/**
	 * Delete all schematics from the Database and all connections with them
	 */ 
	public void Delete ()
	{

	
	  if (myTable.getRowCount()!=0)
	  {
	  int Row=myTable.getSelectedRow();
	  int _EffId=new Integer((db.table.getValueAt (Row,1).
			toString().trim())).intValue();			  
	  int _SchId=new Integer((db.table.getValueAt (Row,0).
			toString().trim())).intValue();			  
	  db.Execute ("begin DelEffSchematic ("+_EffId+","+_SchId+"); end;");
	  ShowData ();
	  }
	  else 
		      JOptionPane.showMessageDialog(getGlassPane (),
                                    "No Data to delete!!!");
	  
	}
	/**
	 * Disposes the frame and all childs windows.
	 */ 
	public void dispose()
        {
		JFrame window;
		for (int i=0;i<windows.size();i++)
		{
		 try{
			window=(JFrame)windows.elementAt(i);
			window.dispose();
		}
		catch(java.lang.NullPointerException e)
		{ }
		}
		setVisible(false);
		super.dispose();
        } // close
	
	
}
