/*
 About this File:
	- 
	- In the beginning of a section there are big titles for easy locating each component.
	- The big titles were made with the banner unix command.
	
	- Called by nes.java
	- Cals Fabrication.java
	
	(c) Aires Marques, Tip2, DASA 1998
*/




import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import java.util.*;

import DAutil.dbase.DBAcc;				// Made in DASA
import DAutil.utilities;				// Made in DASA

import com.sun.java.swing.*;
import com.sun.java.swing.JTable;
import com.sun.java.swing.table.JTableHeader;
import com.sun.java.swing.table.TableColumn;
import com.sun.java.swing.table.TableColumnModel;
import com.sun.java.swing.table.TableModel;
import com.sun.java.swing.event.TableModelEvent;

import com.sun.java.swing.table.*;
import Fabrication;









public class updfab extends com.sun.java.swing.JFrame 
{
	final static int HEIGHT=500;
	final static int WIDTH=800;

	public JMenuBar		myMenuBar;
	public JPanel 		UpperPanel,
				ToolBarPanel;
	
	final public utilities	_utils= new utilities();
	public DBAcc		db = new DBAcc (_utils,false);
	public JTable		dbTable;
		
	public JScrollPane	ListScroll;
	public JToolBar		FabricationToolBar;		

	final public String IconDir="/entwicklung/java/app/resources/icons3d/";

	String  SubRouteStr,
		EffectivityStr,
		VersionStr,
		PartNameStr,
		ProjectStr;
		



	
	
	public updfab(String  _Effectivity, String _Version, String _PartName, String _SubRoute, String _Project)
	{
	 EffectivityStr = new String(_Effectivity);
	 VersionStr	= new String(_Version);
	 PartNameStr	= new String(_PartName);
	 SubRouteStr	= new String(_SubRoute);
	 ProjectStr	= new String(_Project);
	
	
	}
	
	
	public updfab()
	{
	 super(" Fabrication ");
	
	 setBackground (Color.lightGray);
	 this.setSize(WIDTH, HEIGHT);
	 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	 //setLocation (screenSize.width/2 - WIDTH/2, screenSize.height/2 - HEIGHT/2);
	 setLocation (50,50);
	 this.setVisible(true);	
	
	 // Create Layout and add ToolBar				
	 GridBagLayout Layout = new GridBagLayout();
	 GridBagConstraints c = new GridBagConstraints();
	 this.getContentPane().setLayout(Layout);
	 ToolBarPanel = new JPanel();

	 ToolBarPanel.setBackground(Color.lightGray);
	 ToolBarPanel.setLayout(Layout);
	 c.gridwidth = GridBagConstraints.REMAINDER;			
	 c.weightx = 1.0;
	 c.fill = GridBagConstraints.HORIZONTAL;
	 c.insets = new Insets(5,5,0,5);
	 ToolBarPanel = addToolBar();
	 Layout.setConstraints(ToolBarPanel,c);
	 this.getContentPane().add(ToolBarPanel);
	 ToolBarPanel.setBorder(BorderFactory.createLineBorder(Color.black));	
	
	
	 // Prepare the Layout and "Stuff" for the next panel
	 c.fill = GridBagConstraints.HORIZONTAL;
	 c.weightx=2.0;
	 c.insets = new Insets(5,5,5,5);

		
	 // Create Panel with ComboBox's and Labels and TextFields
	 UpperPanel = Upper_Panel();
	 Layout.setConstraints(UpperPanel,c);
	 this.getContentPane().add(UpperPanel);	 	 


	 // -- Create the grid with Data --
	 dbTable=db.createTable();	 
	 ListScroll = new JScrollPane(dbTable);

	 
	 dbTable.setAutoResizeMode (JTable.AUTO_RESIZE_LAST_COLUMN);	 
	 
  	 ListScroll.setBounds(10,124,664,303);
	 ListScroll.setFont(new Font("Dialog", Font.PLAIN, 12));
	 ListScroll.setForeground(new Color(0));
	 //dbTable.getTableHeader().setEnabled(false);
	 

 	 c.gridwidth = GridBagConstraints.REMAINDER;
	 c.weighty = 1;
	 c.fill = GridBagConstraints.BOTH;
	 c.insets = new Insets(0,5,0,5);
	 
	 Layout.setConstraints(ListScroll, c);
	 
	 ListScroll.setBorder(BorderFactory.createLineBorder(Color.black));
	 ListScroll.setBackground(Color.lightGray);
	 
	 this.getContentPane().add(ListScroll);

	 // Accessing data in the UpperPanel. Accessing Effectivity name and route
	 JTextField EffecText = (JTextField) UpperPanel.getComponent(3);
	 final String EffectivityStr = new String(EffecText.getText());
	 JTextField RouteText = (JTextField) UpperPanel.getComponent(5);
	 final String RouteStr = new String(RouteText.getText());
	 
	 final JComboBox myCombo = (JComboBox) UpperPanel.getComponent(1);
	 ProjectStr = new String((String)myCombo.getSelectedItem());
	 
	 selectData(ProjectStr,RouteStr,EffectivityStr);
	 
	 myCombo.addActionListener(actionPerformedOnInputFields);
	 EffecText.addActionListener(actionPerformedOnInputFields);
	 RouteText.addActionListener(actionPerformedOnInputFields);
	 
	 addMouseListenerInTable();
 	 
	 Menus();
	}
	
	
/*	#    #    #  ######   ####           #####     ##    #    #  ######  #
	#    ##   #  #       #    #          #    #   #  #   ##   #  #       #
	#    # #  #  #####   #    #  #####   #    #  #    #  # #  #  #####   #
	#    #  # #  #       #    #          #####   ######  #  # #  #       #
	#    #   ##  #       #    #          #       #    #  #   ##  #       #
	#    #    #  #        ####           #       #    #  #    #  ######  ######	

 - This is the panel that constains the Combo box and the TextFields
*/
    

	private JPanel Upper_Panel(){			
		// Creating and "configuring" the Panel
		JPanel Panel_Combo = new JPanel();
		GridBagLayout Layout = new GridBagLayout();
	 	GridBagConstraints c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.insets = new Insets(10,5,5,5);
				
		c2.weightx=1.0;
		
		Panel_Combo.setLayout (Layout);
		Panel_Combo.setFont(new Font("Dialog", Font.PLAIN, 12));
		

		// Setting border and background
		Panel_Combo.setBorder(BorderFactory.createLineBorder(Color.black));
		Panel_Combo.setBackground(Color.lightGray);


			// Project
		// Adding the JLabel "Project" 
		JLabel ProjectLabel = new JLabel("Project");
		ProjectLabel.setForeground(Color.black);
		Layout.setConstraints(ProjectLabel,c2);
		Panel_Combo.add(ProjectLabel);


		// Adding the ProjectCombo of "Project"
		JComboBox ProjectCombo = new JComboBox();

	 	db.fillCBox (ProjectCombo,"SELECT ID, code, partletter, description " +
                             "FROM Project");
			     
		Panel_Combo.add(ProjectCombo);
			     

			// Effectivity						// Effectivity {{
		// Adding the JLabel "Effectivity"
		JLabel EffectivityLabel = new JLabel(" Effectivity");
		EffectivityLabel.setForeground(Color.black);
		Layout.setConstraints(EffectivityLabel,c2);		
		Panel_Combo.add(EffectivityLabel);		
		
		// Adding the JTextField Effectivity
		JTextField JTF_Effectivity = new JTextField(20);
		JTF_Effectivity.setBackground(Color.white);
		Layout.setConstraints(JTF_Effectivity,c2);
		Panel_Combo.add(JTF_Effectivity);
		JTF_Effectivity.setDisabledTextColor(Color.black);		// }} Effectivity

		c2.gridwidth = 1;
		
			// Route						// Route {{
		// Adding the JLabel "Route"
		JLabel RouteLabel = new JLabel("Route");
		RouteLabel.setForeground(Color.black);
		Layout.setConstraints(RouteLabel,c2);
		Panel_Combo.add(RouteLabel);
		
		c2.gridwidth = GridBagConstraints.REMAINDER;
		
		// Adding the JTextField Route
		JTextField JTF_Route = new JTextField(20);
		JTF_Route.setBackground(Color.white);
		Layout.setConstraints(JTF_Route,c2);
		Panel_Combo.add(JTF_Route);
		JTF_Effectivity.setDisabledTextColor(Color.black);		// }} Route
		
		return Panel_Combo;
	}
	

/*	#    #  ######  #    #  #    #   ####
	##  ##  #       ##   #  #    #  #
	# ## #  #####   # #  #  #    #   ####
	#    #  #       #  # #  #    #       #
	#    #  #       #   ##  #    #  #    #
	#    #  ######  #    #   ####    ####		*/
	
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
			{    db.quit();     } } );
		
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
		
		return options;
	
	} // protected JMenu buildOptionsMenu()
	
	
	
	protected JMenu buildHelpMenu() {
		JMenu help = new JMenu("Help");
		
		JMenuItem IntroItem	= new JMenuItem("Introduction");
		JMenuItem RefItem	= new JMenuItem("Reference");
		JMenuItem OnItem	= new JMenuItem("On Item");
		JMenuItem AboutItem	= new JMenuItem("About Aes");
		
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

	
	//public void selectData() throws java.lang.ArrayIndexOutOfBoundsException {	
        public void selectData(String ProjectNo, String srname,String EffectivityName) {


		db.table.select("SELECT DISTINCT SR.ID, SR.Name "				+
		  		"FROM SubRoute SR, effecSubRoute effSR, Collection C,"		+
		  		" Effectivity E, Project P, CADModel CAD,"			+
		  		" BaseAirplane BA, Model M, Airplane A, AirplGroup AG "		+
		  		"WHERE SR.Name LIKE '" + srname + "%' AND C.ID=E.ID AND"	+
		  		" E.ID=effSR.effID AND effSR.srID=SR.ID AND P.ID=" + ProjectNo 	+ 
				" AND"								+
		  		" BA.prID=P.ID AND effSR.mID=CAD.ID AND M.baID=BA.ID AND"	+
		  		" A.mID=M.ID AND AG.aID=A.ID AND C.ID=AG.cID"			+
                  		" AND C.Name LIKE '" +  EffectivityName + "%'");
	} // selectData()
	


/*
	 #####   ####    ####   #       #####     ##    #####
	   #    #    #  #    #  #       #    #   #  #   #    #
	   #    #    #  #    #  #       #####   #    #  #    #
	   #    #    #  #    #  #       #    #  ######  #####
	   #    #    #  #    #  #       #    #  #    #  #   #
	   #     ####    ####   ######  #####   #    #  #    #
*/    
	public JPanel addToolBar()
	{

		JPanel Panel_ToolBar = new JPanel ();	
		GridBagLayout Bar_Layout=new GridBagLayout();		
		Panel_ToolBar.setLayout (Bar_Layout);
		GridBagConstraints c=new GridBagConstraints();								
		
		c.fill = GridBagConstraints.HORIZONTAL;			
		c.insets = new Insets (0,0,0,0);								
		c.weightx =1.0;			
		

		Panel_ToolBar.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		Panel_ToolBar.setBackground(Color.lightGray);	
		
				
		// Create the toolbar
		
		FabricationToolBar= new JToolBar();		
		FabricationToolBar.setFloatable (false);
		
		// Call addTool Function
		
		addTool(FabricationToolBar,"Close Window","Close",true,ToolClose);
		addTool(FabricationToolBar,"Show list of wires","wirelist",true,ToolWires);
		addTool(FabricationToolBar,"Browser View","browserView",true,ToolBrowserView);
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



	// Add item to toolbar
	public void addTool(JToolBar toolBar,String ToolTip, String name, boolean enable,ActionListener listener)
	{
	
		JButton but = new JButton(new ImageIcon(IconDir + name + "_nc"+ ".jpg",name));
		ImageIcon clicBut =new ImageIcon(IconDir + name + "_c.jpg",name);
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



	  // Description: This ActionListener has the actions to be performed when the user
	 // selects something from the project combobox or, imputs something in the
	// effectivity JTextField or the Route JTextField
	ActionListener actionPerformedOnInputFields = new ActionListener () 
	{
	  public void actionPerformed (ActionEvent e) 
	     {
		 JTextField myText_ = (JTextField) UpperPanel.getComponent(3);
		 final String EffectivityStr_ = new String(myText_.getText());
		 myText_ = (JTextField) UpperPanel.getComponent(5);
		 final String RouteStr_ = new String(myText_.getText());

		 final JComboBox myCombo_ = (JComboBox) UpperPanel.getComponent(1);
		 ProjectStr = new String((String)myCombo_.getSelectedItem());
		 selectData(ProjectStr,RouteStr_,EffectivityStr_);

		 //JFrame aux=(JFrame) Fr.elementAt (0);
		 //aux.repaint();
		 dbTable.repaint();
	     }	     
	};

	  


	  // Description: This calls the List of Wires when the user clicks over one occurence in
	 // the displayed table. The list of wires is another window, therefore is class too.
	//	  
	public void addMouseListenerInTable() {
              MouseAdapter listMouseListener = new MouseAdapter() {	      
        	  public void mouseClicked(MouseEvent e) {
                      if(e.getClickCount() == 2 && dbTable.getSelectedRow() !=-1) {
		          int Row=dbTable.getSelectedRow();
			  String _SubRouteStr		= new String(db.table.getValueAt(Row,1).toString().trim());
			  JFrame frame=new Fabrication(_SubRouteStr, ProjectStr);
                     }
        	   }
               };
              dbTable.addMouseListener(listMouseListener);
	  }



	ActionListener ToolClose = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener			
		close();    
	}};
	
	
	ActionListener ToolWires = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener			

	}};
	
	
	ActionListener ToolBrowserView = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener			

	}};
	

	ActionListener ToolPrint = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener			

	}};

	ActionListener ToolPreferences = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener			

	}};
	
	ActionListener ToolContext = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener			

	}};	
	
	
	//  DarkCode - Insert here the rest of the listeners 


	  //
	 //
	//
	public void close()
        {
		setVisible(false);
		dispose();
        } // close
	

	
	  //	
	 //
	// Exits from program
	public void quit() {
		//SetVisible(false);
		dispose();
		System.exit(0);
	}
	
	
	
/*	static public void main(String[] args)
	{
		Vector data_file=new Vector();
			
		com.sun.java.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme(new com.sun.java.swing.plaf.metal.DefaultMetalTheme ());

		JFrame updfab = new updfab();
	  	updfab.setVisible(true);
	}*/

} // public class Fabrication extends com.sun.java.swing.JFrame
