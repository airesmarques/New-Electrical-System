/*

*/



import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import java.util.*;

import javax.swing.*;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.event.TableModelEvent;

import javax.swing.table.*;
import Fabrication;



/**
 * A <code>updfab</code> is an extension of javax.swing.JFrame and implements the
 * list the subroutes for a selected project. The displayed subroutes can be filtered in the Textfields Effectivity and Route.
 * <p>
 * <center><img src="fabrication/updfab-1.gif" alt="updfab view" ></center>
 * 
 * @version 	1.01, 03/03/99
 * @author 	Aires Marques
 * @author 	Cristiana Areias
 * @see Fabrication
  * @see nes
 * @since       NES 1.0
 */
public class updfab extends javax.swing.JFrame 
{
	/**
	 * Window height 
	 * @see #WIDTH
	 */
	final static protected int HEIGHT=460;
	
	/**
	 * Window width 
	 * @see #HEIGHT
	 */
	final static protected int WIDTH=650;

	/** 
	 * Menubar from this class 
	 */
	public JMenuBar		myMenuBar;
	
	/**
	 * Panel with the text fields and combo boxes
	 * @see #MasterPanel
	 */
	public JPanel 		UpperPanel;
	
	/**
	 * Panel with the text fields and combo boxes
	 * @see #UpperPanel
	 */
	public JPanel 		MasterPanel;
	
	/**
	 * Panel with the toolbar
	 */
	public JPanel 		ToolBarPanel;
	
	/**
	 * Copy of this frame. Used to have access to the frame in the listeners.
	 */
	private JFrame		win;
	
	
	 /**
	  * Instance of the utilities class in this class. This instance is initialized in the constructor and it is provided by NES main window.
	  * @see utilities
	  * @see updfab
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
	public JTable		dbTable;
	
	/**
	 * Table with data from the database
	 */
	public JScrollPane	ListScroll;
	
	/**
	 * Toolbar for this frame.
	 */
	public JToolBar		FabricationToolBar;

	/**
	 * Constains objects that refer to child windows.
	 */
	public Vector		windows=new Vector();
	
	/**
	 * Project that is actually selected.
	 */
	public String		ProjectStr;
		


	/**
	* Constructs a new updfab module. When this constructor is called the database is updated.
	* <p>
	* The window is not initially visible. Call the <code>show</code> 
	* method to cause the window to become visible.
	* @param     _Effectivity the main application frame.
	* @param     _Version   The SubRoute version
	* @param     _PartName  The bundle name
	* @param     _SubRoute  Subroute
	* @param     _Project   The project
	* @param     Utilities  Utilities instance
	* @see       proEpWire
	* @since     NES 1.0
	*/
	public updfab(String _Effectivity, String _Version, String _PartName, String _SubRoute, String _Project, utilities Utilities)
	{
	 System.out.println("Executing the update!!");
	 _utils = Utilities;	
	 proEpWire WireUpdate = new
	 proEpWire(_Effectivity,_Version,_PartName,_SubRoute,new
	 Integer(_Project).intValue(), _utils);

	 JFrame frame=new Fabrication(	_Effectivity, 
					_Version, 
					_PartName, 
					_SubRoute, 
					_Project,
					_utils);
	}



	/**
	* Constructs a new updfab module. 
	* <p>
	* The window is not initially visible. Call the <code>show</code> 
	* method to cause the window to become visible.
	* @param     Utilities  Utilities instance
	* @param     _P         The selected element in the main window of NES
	* @see       nes
	* @since     NES 1.0
	*/
	public updfab(utilities Utilities,Object _P)

	{
	 super(" Fabrication ");	 
	 win = this;					// this is needed for printing
	 _utils = Utilities;
	 db = new DBAcc (_utils,false);
	 this.setSize(WIDTH, HEIGHT);
	 setLocation (50,50);
	 Menus();
	 
	 GridBagLayout Layout = new GridBagLayout();
	 GridBagConstraints c = new GridBagConstraints();
	 this.getContentPane().setLayout(Layout);
	 c.fill = GridBagConstraints.HORIZONTAL;
	 c.anchor = GridBagConstraints.NORTHWEST;
	 c.gridwidth = GridBagConstraints.REMAINDER;			
	 c.weightx = 1.0;
	 c.insets = new Insets(0,0,0,0);
	 ToolBarPanel = addToolBar();
	 ToolBarPanel.setOpaque(false);
	 Layout.setConstraints(ToolBarPanel,c);
	 this.getContentPane().add(ToolBarPanel);

	 // Create Panel with ComboBox's and Labels and TextFields
	 c.insets = new Insets(0,5,5,5);
	 UpperPanel = Upper_Panel(_P);
	 UpperPanel.setLayout (new FlowLayout (FlowLayout.LEFT, 10, 10));
	 UpperPanel.setBackground(Color.lightGray);	 
	 UpperPanel.setBorder(BorderFactory.createEtchedBorder());
	 Layout.setConstraints(UpperPanel,c);
	 this.getContentPane().add(UpperPanel);	 	 

	 // -- Create the grid with Data --
	 
 	 c.fill = GridBagConstraints.BOTH;
	 c.weighty = 13;
	 c.insets = new Insets(0,5,5,5);
	 dbTable=db.createTable();	 
	 ListScroll = new JScrollPane(dbTable);
	 dbTable.setAutoResizeMode (JTable.AUTO_RESIZE_OFF);	 
	 dbTable.setFont(_utils.defaultTableFont);
	 dbTable.getTableHeader().setFont(_utils.TableHeaderFont);
  	 ListScroll.setBounds(10,124,664,303);
	 ListScroll.setFont(new Font("Dialog", Font.PLAIN, 5));
	 ListScroll.setForeground(new Color(0));
	 
	 Layout.setConstraints(ListScroll, c);
	 
	 ListScroll.setBorder(BorderFactory.createLineBorder(Color.black));
	 ListScroll.setBackground(Color.white);	 
	 this.getContentPane().add(ListScroll);
	 

	 // Accessing data in the UpperPanel. Accessing Effectivity name and route
	 JTextField EffecText = (JTextField) MasterPanel.getComponent(3);
	 final String EffectivityStr = new String(EffecText.getText());
	 JTextField RouteText = (JTextField) MasterPanel.getComponent(5);
	 final String RouteStr = new String(RouteText.getText());
	 
	 final JComboBox myCombo = (JComboBox) MasterPanel.getComponent(1);
	 ProjectStr = new String(_utils.project_no_zeros((String)myCombo.getSelectedItem()));
	 
//	 selectData(ProjectStr,RouteStr,EffectivityStr);
	 	 this.setVisible(true);	

	 myCombo.addActionListener(actionPerformedOnInputFields);
	 EffecText.addActionListener(actionPerformedOnInputFields);
	 RouteText.addActionListener(actionPerformedOnInputFields);
	 
	 addMouseListenerInTable();
		this.addWindowListener (new WindowAdapter () {
					public void windowActivated (WindowEvent e) {
					selectData(ProjectStr,RouteStr,EffectivityStr);
					}});
 	 

	}
	
	
/*	#    #    #  ######   ####           #####     ##    #    #  ######  #
	#    ##   #  #       #    #          #    #   #  #   ##   #  #       #
	#    # #  #  #####   #    #  #####   #    #  #    #  # #  #  #####   #
	#    #  # #  #       #    #          #####   ######  #  # #  #       #
	#    #   ##  #       #    #          #       #    #  #   ##  #       #
	#    #    #  #        ####           #       #    #  #    #  ######  ######

 - This is the panel that constains the Combo box and the TextFields
*/
    


	/**
	 * Creates the upper panel in the frame. This panel has the combobox and the textfields and controls the ListScroll that is located below.
	 */
	private JPanel Upper_Panel(Object _P){
		// Creating and "configuring" the Panel
		JPanel Combo_Panel = new JPanel();	
		MasterPanel = new JPanel();
		MasterPanel.setFont(_utils.defaultFont12);
		MasterPanel.setOpaque(false);
		// Setting border and background

			// Project
		// Adding the JLabel "Project" 
		JLabel ProjectLabel = new JLabel("  Project");
		ProjectLabel.setFont(_utils.defaultLabelFont);
		ProjectLabel.setForeground(Color.black);
		MasterPanel.add(ProjectLabel);

		// Adding the ProjectCombo of "Project"
		JComboBox ProjectCombo = new JComboBox();

	 	/*db.fillCBox (ProjectCombo,"SELECT ID, code, partletter, description " +
                             "FROM Project");*/
			     
		db.fillCBox (ProjectCombo,"SELECT code from PROJECT");		
			    
		MasterPanel.add(ProjectCombo);
		ProjectCombo.setSelectedItem(_P);     

		// Effectivity						// Effectivity {{
		// Adding the JLabel "Effectivity"
		JLabel EffectivityLabel = new JLabel("     Effectivity");
		EffectivityLabel.setFont(_utils.defaultLabelFont);
		ProjectCombo.setFont(_utils.defaultComboBoxFont);
		EffectivityLabel.setForeground(Color.black);
		MasterPanel.add(EffectivityLabel);		
		
		// Adding the JTextField Effectivity
		JTextField JTF_Effectivity = new JTextField(8);
		JTF_Effectivity.setFont(_utils.defaultTextFieldFont);
		JTF_Effectivity.setBackground(Color.white);
		MasterPanel.add(JTF_Effectivity);
		JTF_Effectivity.setDisabledTextColor(Color.black);		// }} Effectivity

		
			// Route						// Route {{
		// Adding the JLabel "Route"
		JLabel RouteLabel = new JLabel("     Route");
		RouteLabel.setFont(_utils.defaultLabelFont);
		RouteLabel.setForeground(Color.black);
		MasterPanel.add(RouteLabel);
		

		
		// Adding the JTextField Route
		JTextField JTF_Route = new JTextField(15);
		JTF_Route.setFont(_utils.defaultTextFieldFont);
		JTF_Route.setBackground(Color.white);
		MasterPanel.add(JTF_Route);
		JTF_Effectivity.setDisabledTextColor(Color.black);		// }} Route		
		Combo_Panel.add(MasterPanel);
		return Combo_Panel;
	}





	

/*	#    #  ######  #    #  #    #   ####
	##  ##  #       ##   #  #    #  #
	# ## #  #####   # #  #  #    #   ####
	#    #  #       #  # #  #    #       #
	#    #  #       #   ##  #    #  #    #
	#    #  ######  #    #   ####    ####		*/
	
	
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
	protected void Menus() {
		JMenuBar rootMenu = new JMenuBar();
		JMenu file	= buildFileMenu(); 
		JMenu edit	= buildEditMenu();
		JMenu options	= buildOptionsMenu();
		JMenu help	= buildHelpMenu();
		
		rootMenu.add(file);
		rootMenu.add(edit);
		rootMenu.add(options);
		rootMenu.add(help);
		
		setJMenuBar(rootMenu);
	
	
	}
	
	
	protected JMenu buildFileMenu() {
		JMenu file = new JMenu("File");
		file.setMnemonic ('F');
		
		JMenuItem NewItem 	= new JMenuItem("New");
		JMenuItem OpenItem	= new JMenuItem("Open");
		JMenuItem CloseItem	= new JMenuItem("Close");
		JMenuItem SaveItem	= new JMenuItem("Save");		
		JMenuItem PrintSetItem	= new JMenuItem("Print Settings");
		JMenuItem PrintItem	= new JMenuItem("Print");		
		JMenuItem QuitItem	= new JMenuItem("Quit");
		
			// -- Listeners --
			
		QuitItem.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e)
			{    
			_utils.quit((JComponent)win.getContentPane(),
			new Boolean(_utils.getProperty("GEN_CONFIRM_EXIT")).booleanValue());
		     } } );
		

		NewItem.setFont(_utils.defaultMenuFont);
		file.setFont(_utils.defaultMenuFont);
		OpenItem.setFont(_utils.defaultMenuFont);
		CloseItem.setFont(_utils.defaultMenuFont);
		SaveItem.setFont(_utils.defaultMenuFont);
		PrintSetItem.setFont(_utils.defaultMenuFont);
		PrintItem.setFont(_utils.defaultMenuFont);
		QuitItem.setFont(_utils.defaultMenuFont);
		file.add(NewItem);
		  NewItem.setEnabled(false);		  		  
		file.add(OpenItem);
		  OpenItem.setEnabled(false);
		file.add(CloseItem);
		file.add(new JSeparator());
		file.add(SaveItem);
		  SaveItem.setEnabled(false);
		file.add(new JSeparator());
		file.add(PrintSetItem);
		file.add(PrintItem);
		file.add(new JSeparator());
		file.add(QuitItem);
		
		return file;
		
	} // protected JMenu buildFileMenu()
	
	
	protected JMenu buildEditMenu(){
		JMenu edit = new JMenu("Edit");
		edit.setMnemonic('E');
		
		JMenuItem UndoItem	= new JMenuItem("Undo");
		JMenuItem RedoItem	= new JMenuItem("Redo");
		JMenuItem CutItem	= new JMenuItem("Cut");
		JMenuItem CopyItem	= new JMenuItem("Copy");
		JMenuItem PasteItem	= new JMenuItem("Paste");
		JMenuItem DeleteItem	= new JMenuItem("Delete");
		edit.setFont(_utils.defaultMenuFont);
		UndoItem.setFont(_utils.defaultMenuFont);
		RedoItem.setFont(_utils.defaultMenuFont);
		CutItem.setFont(_utils.defaultMenuFont);
		CopyItem.setFont(_utils.defaultMenuFont);
		PasteItem.setFont(_utils.defaultMenuFont);
		DeleteItem.setFont(_utils.defaultMenuFont);
		edit.add(UndoItem);
		  UndoItem.setEnabled(false);
		edit.add(RedoItem);
		  RedoItem.setEnabled(false);
		edit.add(new JSeparator());
		edit.add(CutItem);
		  CutItem.setEnabled(false);
		edit.add(CopyItem);
		  CopyItem.setEnabled(false);
		edit.add(PasteItem);
		  PasteItem.setEnabled(false);
		edit.add(DeleteItem);	
		  DeleteItem.setEnabled(true);
	
		return edit;	
	}
	
	
	protected JMenu buildOptionsMenu(){
		JMenu options = new JMenu("Options");
		JMenuItem PreferencesItem = new JMenuItem("Preferences");
		options.add(PreferencesItem);
		options.setFont(_utils.defaultMenuFont);
		PreferencesItem.setFont(_utils.defaultMenuFont);
		return options;
	
	} // protected JMenu buildOptionsMenu()
	
	
	
	protected JMenu buildHelpMenu() {
		JMenu help = new JMenu("Help");
		
		JMenuItem IntroItem	= new JMenuItem("Introduction");
		JMenuItem RefItem	= new JMenuItem("Reference");
		JMenuItem OnItem	= new JMenuItem("On Item");
		JMenuItem AboutItem	= new JMenuItem("About Aes");
		help.setFont(_utils.defaultMenuFont);
		IntroItem.setFont(_utils.defaultMenuFont);
		RefItem.setFont(_utils.defaultMenuFont);
		OnItem.setFont(_utils.defaultMenuFont);
		AboutItem.setFont(_utils.defaultMenuFont);		
		help.add(IntroItem); 
		help.add(RefItem);
		help.add(OnItem);
		help.add(new JSeparator());
		help.add(AboutItem);

		return help;
	} // protected JMenu buildHelpMenu()
	
	
	
/*	  #####     ##     #####    ##
	  #    #   #  #      #     #  #
	  #    #  #    #     #    #    #
	  #    #  ######     #    ######
	  #    #  #    #     #    #    #
	  #####   #    #     #    #    #	*/

	
	/**
	 * Executes a SELECT. The DBAcc instance provides the methods needed to do it.
	 * @see DBAcc
	 */
        public void selectData(String ProjectNo, String srname,String EffectivityName) {

		db.table.setColumnTitle(_utils.resHeaderUpdfab);
		db.table.select("SELECT DISTINCT SR.ID, SR.Name "				+
		  		"FROM SubRoute SR, effecSubRoute effSR, Collection C,"		+
		  		" Effectivity E, Project P, CADModel CAD,"			+
		  		" BaseAirplane BA, Model M, Airplane A, AirplGroup AG "		+
		  		"WHERE SR.Name LIKE '" + srname + "%' AND C.ID=E.ID AND"	+
		  		" E.ID=effSR.effID AND effSR.srID=SR.ID AND P.ID=" + ProjectNo 	+ 
				" AND"								+
		  		" BA.prID=P.ID AND effSR.mID=CAD.ID AND M.baID=BA.ID AND"	+
		  		" A.mID=M.ID AND AG.aID=A.ID AND C.ID=AG.cID"			+
                  		" AND C.Name LIKE '" +  EffectivityName + "%'",_utils.resHeaderUpdfab);
		db.table.initColumnSizes (dbTable);
	   	if (dbTable.getRowCount()!=0)
			dbTable.setRowSelectionInterval(0,0);
		Preferences();
	  } // selectData()
	

	/**
	 * Aplies the preferences to the frame.
	 */
	private void Preferences()	
	{
	   int Column_Count=dbTable.getColumnCount();
	  if (_utils.getProperty("UPD_ID").compareTo("False")==0)	  	
	  	dbTable.removeColumn(dbTable.getColumnModel().getColumn(0));
	  if (_utils.getProperty("UPD_SUBROUTE").compareTo("False")==0)
	      dbTable.removeColumn(dbTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,1,dbTable.getColumnCount())));
	}

/*
	 #####   ####    ####   #       #####     ##    #####
	   #    #    #  #    #  #       #    #   #  #   #    #
	   #    #    #  #    #  #       #####   #    #  #    #
	   #    #    #  #    #  #       #    #  ######  #####
	   #    #    #  #    #  #       #    #  #    #  #   #
	   #     ####    ####   ######  #####   #    #  #    #
*/    
	/**
	 * Adds the toolbar to this frame.
	 */
	public JPanel addToolBar()
	{

		JPanel Panel_ToolBar = new JPanel ();	
		GridBagLayout Bar_Layout=new GridBagLayout();		
		Panel_ToolBar.setLayout (Bar_Layout);
		GridBagConstraints c=new GridBagConstraints();								
		
		c.fill = GridBagConstraints.HORIZONTAL;			


		c.insets = new Insets (4,4,3,5);								
		c.weightx =1.0;			
		

		
		Panel_ToolBar.setBackground(Color.lightGray);	
		
				
		// Create the toolbar
		
		FabricationToolBar= new JToolBar();		
		FabricationToolBar.setBorderPainted(false);
		FabricationToolBar.setFloatable (false);
		
		// Call addTool Function
		
		addTool(FabricationToolBar,"Close Window","Close",true,ToolClose);
		FabricationToolBar.addSeparator ();
		addTool(FabricationToolBar,"Show list of wires","wirelist",true,ToolWires);
		addTool(FabricationToolBar,"Browser View","browserView",true,ToolBrowserView);
		FabricationToolBar.addSeparator ();
		addTool(FabricationToolBar,"Print the current view","Print",true,ToolPrint);
		addTool(FabricationToolBar,"Show Preferences","Preferences",true,ToolPreferences);
		addTool(FabricationToolBar,"Open Context Help","Help",true,ToolContext);

						
		Bar_Layout.setConstraints(FabricationToolBar,c);				
		FabricationToolBar.putClientProperty ("JToolBar.isRolover",Boolean.FALSE);
		Bar_Layout.setConstraints(FabricationToolBar,c);				
		Panel_ToolBar.add(FabricationToolBar);		
		
		return Panel_ToolBar;
		
		
	}
	// Add item to toolbar



	/** 
	 * Add an item to toolbar
	 */
	public void addTool(JToolBar toolBar,String ToolTip, String name, boolean enable,ActionListener listener)
	{
	
		JButton but = new JButton(new ImageIcon(_utils.IconDir + name + "_nc"+ ".jpg",name));
		ImageIcon clicBut =new ImageIcon(_utils.IconDir + name + "_c.jpg",name);
		but.setPressedIcon(clicBut);
		but.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		toolBar.add(but);
		but.setToolTipText(ToolTip);
		but.setMargin(new Insets(0,0,0,0));		
		but.setEnabled(enable);
		but.getAccessibleContext().setAccessibleName(name);
		but.addActionListener (listener);
		
	}




/*	#          #     ####    #####  ######  #    #  ######  #####    ####
	#          #    #          #    #       ##   #  #       #    #  #
	#          #     ####      #    #####   # #  #  #####   #    #   ####
	#          #         #     #    #       #  # #  #       #####        #
	#          #    #    #     #    #       #   ##  #       #   #   #    #
	######     #     ####      #    ######  #    #  ######  #    #   ####	*/



	 /** This ActionListener has the actions to be performed when the user
	  *  selects something from the project combobox or, imputs something in the
	  * effectivity JTextField or the Route JTextField
	  */
	  
	ActionListener actionPerformedOnInputFields = new ActionListener () 
	{
	  public void actionPerformed (ActionEvent e) 
	     {
		 JTextField myText_ = (JTextField) MasterPanel.getComponent(3);
		 final String EffectivityStr_ = new String(myText_.getText());
		 myText_ = (JTextField) MasterPanel.getComponent(5);
		 final String RouteStr_ = new String(myText_.getText());

		 final JComboBox myCombo_ = (JComboBox) MasterPanel.getComponent(1);
		 ProjectStr = new String(_utils.project_no_zeros((String)myCombo_.getSelectedItem()));
		 selectData(ProjectStr,RouteStr_,EffectivityStr_);
		 dbTable.repaint();
	     }	     
	};

	  


	/** Description: This calls the List of Wires when the user clicks over one occurence in
	 * the displayed table. The list of wires is another window, therefore is class too.
	 */  
	public void addMouseListenerInTable() {
              MouseAdapter listMouseListener = new MouseAdapter() {	      
        	  public void mouseClicked(MouseEvent e) {
                      if(e.getClickCount() == 2 && dbTable.getSelectedRow() !=-1) {
		          int Row=dbTable.getSelectedRow();
			  String _SubRouteStr = new String(db.table.getValueAt(Row,1).toString().trim());
			  JFrame f=new Fabrication(_SubRouteStr, ProjectStr, _utils);
			windows.addElement(f);
			_utils.WindowsVector.addElement(f);

                     }
        	   }
               };
              dbTable.addMouseListener(listMouseListener);
	  }


	/**
	 * Closes and disposes the frame.
	 */
	ActionListener ToolClose = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener			
		dispose();
	}};
	
	
	/**
	 * Calls the list of wires view. 
	 * @see Fabrication
	 */
	ActionListener ToolWires = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener
		int Row = dbTable.getSelectedRow();
		if ( Row == -1) 
			JOptionPane.showMessageDialog(getGlassPane (),"No Row selected!!!");
		else {
			String _SubRouteStr = new String(db.table.getValueAt(Row,1).toString().trim());
			if (_utils==null) _utils = new utilities();
			JFrame frame=new Fabrication(_SubRouteStr, ProjectStr, _utils);
		}

	}};
	
	/**
	 * Calls the browser view.
	 * @see browser
	 */
	ActionListener ToolBrowserView = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener			

	}};
	

	/**
	 * Prints the data that was selected.
	 * @see print
	 */
	ActionListener ToolPrint = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener			
		//new Print (win, dbTable, "Fabrication", "Effectivity:                Route" + ";", null);

	}};

	/**
	 * Calls the preferences window
	 * @see preferences
	 */
	ActionListener ToolPreferences = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener			
		_utils.WindowsVector.addElement(new winPrefs (_utils,6));
	}};
	
	/**
	 * Not functional until this date.
	 */
	ActionListener ToolContext = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener			

	}};	
	
	
	/**
	 * Disposes the frame.
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
		super.dispose();
        } // dispose}

	

	/** 
	 * Exits from program
	 */
	public void quit() {
		//SetVisible(false);
		dispose();
		System.exit(0);
	}
	


	
	/*
	static public void main(String[] args)
	{
		Vector data_file=new Vector();
			
		javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme(new javax.swing.plaf.metal.DefaultMetalTheme ());		
		
		//JFrame updfab = new updfab(_Effectivity, _Version, _PartName, _SubRoute, _Project);
		
		JFrame updfab = new updfab();
		
	  	updfab.setVisible(true);
	}*/
	

} // public class Fabrication extends javax.swing.JFrame
