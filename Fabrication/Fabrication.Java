import javax.swing.*;
import java.awt.*;
import java.lang.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.table.JTableHeader;
import javax.swing.JComboBox;


/**
 * This class shows a list of wires for a certain bundle and effectivity either when the NES is started as an application or when it is started as an database "updater".
 * When one of the wires is selected (double click), it displays the details for that wire.
 * <p>
 * <center><img src="fabrication/Fabrication-1.gif" alt="updfab view" ></center>
 * @version 	1.01, 03/03/99
 * @author 	Aires Marques
 * @author 	Cristiana Areias
 * @see updfab
 * @see pWireInfo
  * @see nes
 * @see javax.swing.JFrame
 * @since       NES 1.0
 */
public class Fabrication extends javax.swing.JFrame 
{
	/**
	* Height of the window
	* @see #WIDTH
	*/
	final static int HEIGHT=460;
	
	/**
	* Width of the window
	* @see #HEIGHT
	*/
	final static int WIDTH=650;	

	/**
	 * Creates the panel above the list of wires. This has the combo boxes and filters that control the list of wi
	 */
	private JPanel 		UpperPanel;
	
	/**
	 * The toolbar for this frame.
	 */
	private JPanel 		ToolBarPanel;

	/**
	 * Instance of utilities inside this class. This is initialezed in the constructors.
	 * @see #Fabrication(java.lang.String, java.lang.String, utilities)
	 * @see #Fabrication(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String ,utilities)
	 */
	private utilities	_utils;
	
	/**
	 * Instance of DBAcc on this frame
	 * @see DBAcc
	 */
	private DBAcc		db;
	

	
	/**
	 * Table that is going to have the data for this frame.
	 * @see DBAcc
	 */
	private JTable		dbTable;
		
	
	/**
	 * This enables the scrolling for the data in dbTable
	 * <p>
	 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/scrollpane.html">See the documentation about how to use JScrollPane on java.sun.com</a>	 	 
	 * @see javax.swing.JScrollPane
	 */
	private JScrollPane	ListScroll;
	private JToolBar	FabricationToolBar;
		
	/**
	 * String that contains the Effectivity
	 */
	public String 		EffectivityStr;
	
	/**
	 * String that contains the Version
	 */
	public String 		VersionStr;

	/**
	 * String that contains the Part Name
	 */	
	public String 		PartnameStr;
	
	/**
	 * String that contains the SubRoute
	 */
	public String 		SubRouteStr;
	
	/**
	 * String that contains the Project
	 */
	public String 		ProjectStr;
	
	
	/**
	 * Combobox that permits to choose the effectivity
	 * @see javax.swing.JComboBox
	 */
	JComboBox 		PwEffectCombo;
	
	/**
	 * Combobox that permits to choose the bundle
	 * @see javax.swing.JComboBox	 
	 */
	JComboBox 		BundleCombo;

	JTextField 		JTF_Wires,
				JTF_SubRoute,
				JTF_Project;
				
	/**
	 * Vector that has references to all the windows opened on this window.
	 * This vector is usefull when we want to close/dispose all the childs of this window.
	 */
	public Vector	windows = new Vector();
	
	
	/**
	 * Layout for this frame
	 * @see java.awt.GridbagLayout
	 */
	public GridBagLayout Layout;
	
	/**
	 * Constraints for this frame
	 * @see java.awt.GridBagConstraints
	 */
	public GridBagConstraints c;
	

	/**
	 * Defines that the data is provided by the file gived in main window NES
	 * @see #data_provider
	 * @see #DATA_FROM_DATABASE
	 */
	final public static int DATA_FROM_THE_FILE = 1;
	
	/**
	 * Defines that the data is provided by the Oracle Database
	 * @see #data_provider
	 * @see #DATA_FROM_THE_FILE
	 */
	final public static int DATA_FROM_DATABASE = 2;
	
	/**
	 * Defines which is the source of the data
	 * @see #DATA_FROM_THE_FILE
	 * @see #DATA_FROM_DATABASE
	 */
	public static int data_provider;


 	/**
	 * Executes the first part of the general execution
	 */
	private void General_execution_step1() 
	{
	 setBackground (_utils.panelBackgroundColor);
	 this.setSize(WIDTH, HEIGHT);
	 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	 setLocation (100,100);
	
	
	 // Create Layout and add ToolBar				
	 Layout = new GridBagLayout();
	 c = new GridBagConstraints();
	 this.getContentPane().setLayout(Layout);
	 ToolBarPanel = new JPanel();

	 ToolBarPanel.setBackground(_utils.toolBarBackgroundColor);
	 ToolBarPanel.setLayout(Layout);
	 c.gridwidth = GridBagConstraints.REMAINDER;			
	 c.weightx = 1.0;
	 c.fill = GridBagConstraints.HORIZONTAL;
	 c.insets = new Insets(5,5,0,5);
	 ToolBarPanel = addToolBar();
	 Layout.setConstraints(ToolBarPanel,c);
	 this.getContentPane().add(ToolBarPanel);
	
	 // Prepare the Layout and "Stuff" for the next panel
	 c.fill = GridBagConstraints.HORIZONTAL;
	 c.weightx=2.0;
	 c.insets = new Insets(5,5,5,5);
	}
	
	
	/**
	 * Executes the first part of the general execution
	 */
	private void General_execution_step2() 
	{
	
	 // -- Create the grid with Data -- 	 
	 dbTable=db.createTable();
	 ListScroll = new JScrollPane(dbTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
	 			      ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

	 dbTable.setAutoResizeMode (JTable.AUTO_RESIZE_OFF);
	 dbTable.setFont(_utils.defaultTableFont);
	 dbTable.getTableHeader().setFont(_utils.TableHeaderFont);
			
//	 SelectData();
	 	 
	 ListScroll.setFont(new Font("Dialog", Font.PLAIN, 12));
	 ListScroll.setForeground(new Color(0));		
	 dbTable.getTableHeader().setEnabled(false);	
	
	 c.gridwidth = GridBagConstraints.REMAINDER;
	 c.weighty = 1;
	 c.fill = GridBagConstraints.BOTH;
	 c.insets = new Insets(0,5,0,5);
	 
	 Layout.setConstraints(ListScroll, c);	 
	 
	 ListScroll.setBackground(_utils.tableBackgroundColor);
	 
	 this.getContentPane().add(ListScroll);
	 
	 
	 addMouseListenerInTable();
	 
	 Menus();
	
	}




	/** 
	 * This is the constructor for the List of Wires when 
	 * there is no data provided. The data from the comboboxes comes from the database.
	 */
	public Fabrication(String SubRouteStrIn, String ProjectStrIn, utilities Utilities)
	{
	 super(" List Of Wires ");
	 _utils = Utilities;
	 db = new DBAcc (_utils);
	 data_provider=DATA_FROM_DATABASE;

 	 SubRouteStr	= SubRouteStrIn;
	 ProjectStr	= ProjectStrIn;	
	 
	 General_execution_step1();
	 //Create Panel with ComboBox's and Labels and TextFields
	 UpperPanel = Upper_Panel();
	 Layout.setConstraints(UpperPanel,c);
	 this.getContentPane().add(UpperPanel);
 
	 General_execution_step2();
	 this.setVisible(true);
	 //System.out.println("SubRouteStr:" + SubRouteStr);
	 //System.out.println("ProjectStr:" + ProjectStr);
	}



	/**Description: This is the constructor for the List of Wires when 
	 * data is provided by the file epd1.txt1
	 */
	public Fabrication(	String _Effectivity, 
				String _Version, 
				String _PartName, 
				String _SubRoute, 
				String _Project,
				utilities Utilities)
	{
	 super(" List Of Wires ");
	 _utils = Utilities;
	 db = new DBAcc (_utils);
	 data_provider=DATA_FROM_THE_FILE;
	 
	 //System.out.println("List of wires with update");
	 
	 // Grabs and saves the strings from the imput vector for further use
	 EffectivityStr = _Effectivity;
	 VersionStr 	= _Version;
	 PartnameStr 	= _PartName;
	 SubRouteStr 	= _SubRoute;
	 ProjectStr 	= _Project;
	
	 General_execution_step1();
	 
	 // Create Panel with ComboBox's and Labels and TextFields
	 UpperPanel = Upper_Panel();
	 Layout.setConstraints(UpperPanel,c);
	 this.getContentPane().add(UpperPanel);
 
	 General_execution_step2();
	 SelectData();
	 Integer RowCount = new Integer(dbTable.getRowCount());				// Get the number of Wires
	 String RowCountStr = new String(RowCount.toString());
	 JTF_Wires.setText(RowCountStr);						// }} Wire fill
	 this.setVisible(true);	 
	 System.out.println("Execution is over...");
	}
	
	
	
    
	/**
	 * This is the panel that constains the Combo boxes and the TextFields 			
	 */
	private JPanel Upper_Panel(){
	
		// Creating and "configuring" the Panel
		JPanel MasterDataPanel = new JPanel();
		GridBagLayout Layout = new GridBagLayout();
	 	GridBagConstraints c2 = new GridBagConstraints();
		c2.gridwidth = GridBagConstraints.REMAINDER;
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.weightx=1.0;
		MasterDataPanel.setLayout (Layout);
		MasterDataPanel.setBorder(_utils.mainDataPanelBorder);
		MasterDataPanel.setBackground(_utils.mainDataPanelBackColor);

		// Creating and "configuring" the Panel containing the first line
		c2.insets = new Insets(0,5,0,5);
		JPanel MasterDataPanel_line1 = new JPanel();
		MasterDataPanel_line1.setLayout (new FlowLayout (FlowLayout.LEFT, 0, 5));
		MasterDataPanel_line1.setFont(_utils.defaultFont12B);
		Layout.setConstraints(MasterDataPanel_line1,c2);
		MasterDataPanel.add(MasterDataPanel_line1);
		MasterDataPanel_line1.setBackground(_utils.mainDataPanelBackColor);
		// Creating and "configuring" the Panel containing the second line
		JPanel MasterDataPanel_line2 = new JPanel();
		MasterDataPanel_line2.setLayout (new FlowLayout (FlowLayout.LEFT, 5, 5));
		MasterDataPanel_line2.setFont(_utils.defaultFont12B);
		Layout.setConstraints(MasterDataPanel_line2,c2);
		MasterDataPanel.add(MasterDataPanel_line2);
		MasterDataPanel_line2.setBackground(_utils.mainDataPanelBackColor);


			// SubRoute
		// Adding the JLabel "SubRoute" 
		JLabel SubRouteLabel = new JLabel("  SubRoute ");		
		SubRouteLabel.setFont(_utils.defaultLabelFont);
		SubRouteLabel.setForeground(_utils.defaultTextColor);
		MasterDataPanel_line1.add(SubRouteLabel);

		// Adding a JTextField SubRoute 
		JTF_SubRoute = new JTextField(15);
		JTF_SubRoute.setFont(_utils.defaultTextFieldFont);
		JTF_SubRoute.setBackground(_utils.TextFieldBackgroundColorActive);
		MasterDataPanel_line1.add(JTF_SubRoute);
		JTF_SubRoute.setText(SubRouteStr);
		JTF_SubRoute.setDisabledTextColor(_utils.defaultTextColor);
		JTF_SubRoute.setEnabled(false);
		
	
			// Project		
		// Adding the JLabel "Project"
		JLabel ProjectLabel = new JLabel("     Project ");
		ProjectLabel.setFont(_utils.defaultLabelFont);
		ProjectLabel.setForeground(_utils.defaultTextColor);
		MasterDataPanel_line1.add(ProjectLabel);
						
		// Adding the JTextField Project
		JTF_Project = new JTextField(5);
		JTF_Project.setFont(_utils.defaultTextFieldFont);
		JTF_Project.setBackground(_utils.TextFieldBackgroundColorActive);
		MasterDataPanel_line1.add(JTF_Project);
		JTF_Project.setText("00" + ProjectStr + "0");
		JTF_Project.setDisabledTextColor(_utils.defaultTextColor);
		JTF_Project.setEnabled(false);
		
	
			// Wires						// Wires {{
		// Adding the JLabel "Wires"
		JLabel WiresLabel = new JLabel("                         Wires ");
		WiresLabel.setFont(_utils.defaultLabelFont);
		WiresLabel.setForeground(_utils.defaultTextColor);
		MasterDataPanel_line1.add(WiresLabel);		
		
		c2.gridwidth = GridBagConstraints.REMAINDER;
		
		// Adding the JTextField Wires
		JTF_Wires = new JTextField(5);
		JTF_Wires.setFont(_utils.defaultTextFieldFont);
		JTF_Wires.setBackground(_utils.TextFieldBackgroundColorActive);
		MasterDataPanel_line1.add(JTF_Wires);					// }} Wires
		JTF_Wires.setDisabledTextColor(_utils.defaultTextColor);
		JTF_Wires.setEnabled(false);		

		c2.gridwidth = 1;
		c2.insets = new Insets(0,5,0,5);


		// ----------------------------- SECOND LINE ----------------------------------

			// PW Effect
		// Adding the JLabel "PW Effect"
		JLabel ComboLabel = new JLabel("PW Effect");
		ComboLabel.setFont(_utils.defaultLabelFont);
		ComboLabel.setForeground(_utils.defaultTextColor);
		MasterDataPanel_line2.add(ComboLabel);

		// Adding the PwEffectCombo of "PW Effect"
		PwEffectCombo = new JComboBox();
		PwEffectCombo.setFont(_utils.defaultComboBoxFont);
		if (data_provider==DATA_FROM_DATABASE)
	 	  db.fillCBox (PwEffectCombo,
			"SELECT DISTINCT C.Name, C.Lower, C.Upper, E.ID " 			+
			"FROM Collection C, Model M, Project P, Airplane A," 			+
			" BaseAirplane BA, AirplGroup AG, Effectivity E," 			+
			" SubRoute SR, effecSubRoute effSR," 					+
			" SRpWire srpw, effpWire EpW " 						+
			"WHERE P.ID=" + ProjectStr + " AND BA.prID=P.ID AND M.baID=BA.ID" 	+
			" AND A.mID=M.ID AND AG.aID=A.ID AND C.ID=AG.cID" 			+
			" AND SR.Name='" + SubRouteStr + "' AND effSR.srID=SR.ID AND" 		+
			" srpw.srID=effSR.ID AND EpW.ID=srpw.epwID AND" 			+
			" Epw.effID=E.ID AND E.ID=C.ID " 					+
			"ORDER BY C.Lower, C.Upper");

		// Code for filling the combobox with the EffectivityStr var			
		if (data_provider==DATA_FROM_THE_FILE) PwEffectCombo.addItem(EffectivityStr);

		PwEffectCombo.addActionListener(new ActionListener () {
				public void actionPerformed (ActionEvent e)
				{
					//Refreshing the BundleCombo Combobox
					// The BundleCombo refreshes also the Table and the number of wires
					if (BundleCombo.getSelectedIndex()==-1) { return; }
					DBAcc newdb = new DBAcc (_utils);
					BundleCombo.removeAllItems();
					String effname="";
					if (PwEffectCombo.getSelectedIndex()!=-1)
					 effname= new String(PwEffectCombo.getSelectedItem().toString());					 
					if (data_provider==DATA_FROM_DATABASE)
					
					
					
					  newdb.fillComboBox (BundleCombo,
							"SELECT DISTINCT B.Name, B.id " +
							"FROM Collection C, Bundle B," +
							" SubRoute SR, effecSubRoute effSR," +
							" SRpWire srpw, effpWire EpW, Effectivity E " +
							"WHERE" +
							" SR.Name='" + SubRouteStr + "' AND effSR.srID=SR.ID" +
							" AND srpw.srID=effSR.ID AND EpW.ID=srpw.epwID AND " +
							" EpW.effID=E.ID AND E.ID=C.ID AND C.NAME='" + effname.trim() + "' AND EpW.bID=B.ID " +
							"ORDER BY B.ID");				
					if (data_provider==DATA_FROM_THE_FILE) BundleCombo.addItem(PartnameStr);
					//BundleCombo.addItem("Ultimo");	//Esta linha prova que o problema provem do resultSet
					
				 } } );
		
		MasterDataPanel_line2.add(PwEffectCombo);

		
				// Bundle
		// Adding the Jlabel "Bundle" 
		JLabel BundleLabel = new JLabel("                Bundle");
		BundleLabel.setFont(_utils.defaultLabelFont);
		BundleLabel.setForeground(Color.black);
		BundleLabel.setForeground(_utils.defaultTextColor);
		MasterDataPanel_line2.add(BundleLabel);
		
		String effname;
		try { effname= new String(PwEffectCombo.getSelectedItem().toString());
		 } catch(NullPointerException e) { 
		 	effname= ""; }
	BundleCombo = new JComboBox();
	BundleCombo.setFont(_utils.defaultComboBoxFont);
		if (data_provider==DATA_FROM_DATABASE)
		  db.fillComboBox (BundleCombo,
				"SELECT DISTINCT B.Name, B.id " +
				"FROM Collection C, Bundle B," +
				" SubRoute SR, effecSubRoute effSR," +
				" SRpWire srpw, effpWire EpW, Effectivity E " +
				"WHERE" +
				" SR.Name='" + SubRouteStr + "' AND effSR.srID=SR.ID" +
				" AND srpw.srID=effSR.ID AND EpW.ID=srpw.epwID AND " +
				" EpW.effID=E.ID AND E.ID=C.ID AND C.NAME='" + effname.trim() + "' AND EpW.bID=B.ID " +
				"ORDER BY B.ID");				
		if (data_provider==DATA_FROM_THE_FILE) BundleCombo.addItem(PartnameStr);
		
		BundleCombo.addActionListener(new ActionListener () {
				public void actionPerformed (ActionEvent e)
				{
					// Look if the BundleCombo has something in it
					if (BundleCombo.getSelectedIndex()==-1) { 
						//System.out.println("Elements queue is empty!!");
						return;
					}
					
					// Refreshing the table
					SelectData();
					dbTable.repaint();
					
					// Refreshing the number of wires
					Integer RowCount = new Integer(dbTable.getRowCount());
					String RowCountStr = new String(RowCount.toString());
					JTF_Wires.setText(RowCountStr);
				 } } );
		MasterDataPanel_line2.add(BundleCombo);				 
		this.addWindowListener (new WindowAdapter () {
			public void windowActivated (WindowEvent e) {
			SelectData();
	 		Integer RowCount = new Integer(dbTable.getRowCount());				// Get the number of Wires
		 	String RowCountStr = new String(RowCount.toString());
		 	JTF_Wires.setText(RowCountStr);						// }} Wire fill
			
		}});
 	 

		
		return MasterDataPanel;
	}
	

	

/*	#    #  ######  #    #  #    #   ####
	##  ##  #       ##   #  #    #  #
	# ## #  #####   # #  #  #    #   ####
	#    #  #       #  # #  #    #       #
	#    #  #       #   ##  #    #  #    #
	#    #  ######  #    #   ####    ####		*/

	/**
	 * Creates all the menus for this frame
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
			{    quit();     } } );
		
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
		  DeleteItem.setEnabled(false);
	
		return edit;	
	}
	
	
	protected JMenu buildOptionsMenu(){
		JMenu options = new JMenu("Options");
		
		JMenuItem PreferencesItem = new JMenu("Preferences");
		JMenuItem HightlightItem = new JMenu("Hightlight");
		options.setFont(_utils.defaultMenuFont);
		PreferencesItem.setFont(_utils.defaultMenuFont);
		HightlightItem.setFont(_utils.defaultMenuFont);
		options.add(PreferencesItem);
		options.add(HightlightItem);
		
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
	 * Selects the desired data from the database
	 */  
        public void SelectData() {
		db.table.setColumnTitle(_utils.resHeaderFabrication);
		String EffecName;
		try {
		 EffecName= new String(PwEffectCombo.getSelectedItem().toString());
		 } catch(NullPointerException e)
		 {
		 EffecName= "";
		 }
		String Bundle;
		try {
		 Bundle = new String(BundleCombo.getSelectedItem().toString().trim());
		 } catch(NullPointerException e)
		 {
		 Bundle= "";
		 }		String CModel = new String("/prdb/electric/cabling/" + Bundle);
				
		db.table.select("SELECT DISTINCT EpW.ID, lW.Name, pW.Name, B.Name, F1.Name, P1.Name, " +
				"P2.Name, F2.Name, EpW.chDate, EpW.manufacturerLength, EpW.Status, " +
				"EpW.Remark " +
				
				"FROM Collection C, effpWire EpW, SubRoute sr, CADModel cad," +
				" Effectivity E, Bundle B, effecSubRoute R," +
				" pWire pW, efflWire ElW, lWire lW, lTerminal P1," +
				" lTerminal P2, Fin F1, Fin F2, SrpWire srpw " +
				
				"WHERE E.ID=C.ID AND C.Name='" + EffecName.trim() + "'" + 
				" AND EpW.effID=E.ID" 					+
				" AND EpW.ID=srpw.epwID" 				+ 
				" AND R.ID=srpw.SRID" 					+
				" AND EpW.leftltID=P1.ID" 				+ 
				" AND EpW.rightltID=P2.ID" 				+
				" AND P1.fID=F1.ID AND P2.fID=F2.ID AND EpW.efflwID=ElW.ID" +
				" AND ElW.lwID=lW.ID AND EpW.pwID=pW.ID AND B.ID=EpW.bID" +
				" AND B.Name='" + Bundle + "' AND R.srID=sr.ID AND sr.Name='" + SubRouteStr + "'" + 
				" AND cad.Filename='" + CModel + "'" +
				" AND cad.ID=R.mID AND R.effID=C.ID " +
				
				"ORDER BY B.Name, pW.Name ",_utils.resHeaderFabrication);

                db.table.initColumnSizes (dbTable);
		if (dbTable.getRowCount()!=0)
			dbTable.setRowSelectionInterval(0,0);
		Preferences();
	}
	
	
	/**
	 * Takes care that only the desired fields are shown
	 * @see Preferences
	 */
	private void Preferences()	
	{
	   int Column_Count=dbTable.getColumnCount();
	  if (_utils.getProperty("FAB_ID").compareTo("False")==0)	  	
	  	dbTable.removeColumn(dbTable.getColumnModel().getColumn(0));
	  if (_utils.getProperty("FAB_LOGWIRE").compareTo("False")==0)
	      dbTable.removeColumn(dbTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,1,dbTable.getColumnCount())));
	  if (_utils.getProperty("FAB_PHYSWIRE").compareTo("False")==0)
	      dbTable.removeColumn(dbTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,2,dbTable.getColumnCount())));
	  if (_utils.getProperty("FAB_BUNDLE").compareTo("False")==0)
	      dbTable.removeColumn(dbTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,3,dbTable.getColumnCount())));
	  if (_utils.getProperty("FAB_FIN1").compareTo("False")==0)
	      dbTable.removeColumn(dbTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,4,dbTable.getColumnCount())));
	  if (_utils.getProperty("FAB_P1").compareTo("False")==0)
	      dbTable.removeColumn(dbTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,5,dbTable.getColumnCount())));
	  if (_utils.getProperty("FAB_P2").compareTo("False")==0)
	      dbTable.removeColumn(dbTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,6,dbTable.getColumnCount())));
	  if (_utils.getProperty("FAB_FIN2").compareTo("False")==0)
	      dbTable.removeColumn(dbTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,7,dbTable.getColumnCount())));
	  if (_utils.getProperty("FAB_CHANGEDATE").compareTo("False")==0)
	      dbTable.removeColumn(dbTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,8,dbTable.getColumnCount())));
	  if (_utils.getProperty("FAB_MANUFLENGTH").compareTo("False")==0)
	      dbTable.removeColumn(dbTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,9,dbTable.getColumnCount())));
	  if (_utils.getProperty("FAB_STATUS").compareTo("False")==0)
	      dbTable.removeColumn(dbTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,10,dbTable.getColumnCount())));
	  if (_utils.getProperty("FAB_REMARK").compareTo("False")==0)
	      dbTable.removeColumn(dbTable.getColumnModel().getColumn(
	      db.DeleteColumn(Column_Count,11,dbTable.getColumnCount())));
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
	 * Adds the a ToolBar to this frame
	 */
	private JPanel addToolBar()
	{

		JPanel Panel_ToolBar = new JPanel ();	
		GridBagLayout Bar_Layout=new GridBagLayout();		
		Panel_ToolBar.setLayout (Bar_Layout);
		GridBagConstraints c=new GridBagConstraints();								
		
		c.fill = GridBagConstraints.HORIZONTAL;			
		c.insets = new Insets (0,0,0,0);								
		c.weightx =1.0;			
		
							
		Panel_ToolBar.setBackground(_utils.toolBarBackgroundColor);	
		
				
		// Create the toolbar
		
		FabricationToolBar= new JToolBar();		
		FabricationToolBar.setBorderPainted(false);		
		FabricationToolBar.setFloatable (false);
		
		// Call addTool Function
		
		addTool(FabricationToolBar,"Close Window","Close",true,ToolClose);
		FabricationToolBar.addSeparator ();
		addTool(FabricationToolBar,"Show Detail Info.","ShowInf",true,ToolShowInf);
		addTool(FabricationToolBar,"Open Browser View","browserView",true,ToolBrowser);
		FabricationToolBar.addSeparator ();
		addTool(FabricationToolBar,"Highlight Component","HighlightComp",true,ToolHighlight);
		addTool(FabricationToolBar,"Stop Highlighting","Stop",true,StopHighlight);
		FabricationToolBar.addSeparator ();
		addTool(FabricationToolBar,"Print Current View","Print",true,ToolPrint);
		addTool(FabricationToolBar,"Show Preferences for this Window","Preferences",true,ToolPreferences);
		addTool(FabricationToolBar,"Open Context Help","Help",true,ToolHelp);						
		
		Bar_Layout.setConstraints(FabricationToolBar,c);		
		FabricationToolBar.putClientProperty ("JToolBar.isRolover",Boolean.FALSE);
		Bar_Layout.setConstraints(FabricationToolBar,c);
		Panel_ToolBar.add(FabricationToolBar);		
		return Panel_ToolBar;
		
		
	}
	// Add item to toolbar



	/**
	 * Add item to toolbar
	 */
	private void addTool(JToolBar toolBar,String ToolTip, String name, boolean enable,ActionListener listener)
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




	 /**
	  * This calls the physical wire information when the user clicks over 
	  * one occurence of the displayed table. The physical wire information is another window,
	  * therefore is class too -- pWireInfo.java --
	  */
	public void addMouseListenerInTable() {
              MouseAdapter listMouseListener = new MouseAdapter() {	      
        	  public void mouseClicked(MouseEvent e) {
                      if(e.getClickCount() == 2 && dbTable.getSelectedRow() !=-1) {
		          int Row=dbTable.getSelectedRow();
			  String _pWireStr = new String(db.table.getValueAt(Row,2).toString());
			  String EffectivityStr = new String(PwEffectCombo.getSelectedItem().toString());
			  JFrame f=new pWireInfo(ProjectStr,_pWireStr,EffectivityStr,_utils);
			windows.addElement(f);
			_utils.WindowsVector.addElement(f);
                     }
        	   }
               };
              dbTable.addMouseListener(listMouseListener);
	  }


	ActionListener ToolClose = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		dispose();    
	}};
	//  DarkCode - Insert here the rest of the listeners 



	ActionListener ToolShowInf = new ActionListener () {
	public void actionPerformed (ActionEvent e) {    
		int Row = dbTable.getSelectedRow();
		if (Row == -1)
			JOptionPane.showMessageDialog(getGlassPane (),"No Row selected!!!");
		else {
		    String _pWireStr = new String(db.table.getValueAt(Row,2).toString());
		    String EffectivityStr = new String(PwEffectCombo.getSelectedItem().toString());
		    JFrame f=new pWireInfo(ProjectStr,_pWireStr,EffectivityStr,_utils);
			windows.addElement(f);
			_utils.WindowsVector.addElement(f);

		}
	}};


	ActionListener ToolBrowser = new ActionListener() {
	public void actionPerformed (ActionEvent e) {    
		// Code Listener			
	}};
	
	ActionListener ToolHighlight = new ActionListener() {
	public void actionPerformed (ActionEvent e) {    
		// Code Listener			
	}};
	
	
	ActionListener StopHighlight = new ActionListener() {
	public void actionPerformed (ActionEvent e) {    
		// Code Listener			
	}};
	
	
	ActionListener ToolPrint = new ActionListener() {public void actionPerformed (ActionEvent e) {    
		// Code Listener			
	}};
	
	ActionListener ToolPreferences = new ActionListener() {
	public void actionPerformed (ActionEvent e) {    
		// Code Listener			
		_utils.WindowsVector.addElement(new winPrefs (_utils,7));
	}};
	
	ActionListener ToolHelp = new ActionListener() {
	public void actionPerformed (ActionEvent e) {    
		// Code Listener			
	}};
	



	/**
	 * Disposes this frame
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
		if (data_provider==DATA_FROM_THE_FILE) System.exit(0);
		super.dispose();
        } // dispose}
	

	
	/**	
	 *  Exits from program
	 */
	private void quit() {
		//SetVisible(false);
		dispose();
		System.exit(0);
	}
	


		
		
	  // Description: This function is for testing only and is to be called by the main...
	 //  This file is not to be a standalone aplication, therefore there is not need of
	//   an existing main(String[] args) rather then for testing.
	/*static void init(String args)
	{
		Vector data_file=new Vector();

	        extract_string strx=new extract_string(args,data_file);
	
	        javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme(new javax.swing.plaf.metal.DefaultMetalTheme ());

	        JFrame fabrication = new Fabrication(data_file);
  	        fabrication.setVisible(true);
	}
	*/
	
	/*static public void main(String[] argv)
	{
		
		init("epd.txt1");
	}*/
	

} // public class Fabrication extends javax.swing.JFrame
