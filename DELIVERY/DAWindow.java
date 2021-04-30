/*
 *  This class implements the delivery plan window
 *
 *
 *
 */


import java.awt.*;
import java.lang.*;
import java.util.*;
import java.awt.event.*;
import javax.java.swing.*;
import DAutil.utilities;
import DAutil.dbase.DBAcc;


//
// Window contents definition
//
public class DAWindow extends JFrame
{

  public Vector		 panelVector = new Vector ();
  public utilities	 _utils = new utilities ();
  public DBAcc		 db = new DBAcc (_utils);
  public JComboBox	 projectCBox;
  public JMenuBar	 myMenuBar;
  public JToolBar	 myToolBar;
  
  final public String IconDir="/entwicklung/java/app/resources/icons3d/";

  public JTable myTable;
  //   ----  Window Constructor  ----
  public DAWindow ()
  {
    super ("Window");
    setSize (800, 500);
    Dimension screenSize = Toolkit.getDefaultToolkit ().getScreenSize ();
    setLocation (screenSize.width/2 - 800/2, screenSize.height/2 - 500/2);
    buildMenus (); 
    buildContents ();
    this.addWindowListener (new WindowAdapter () {
                            public void windowClosing (WindowEvent e){
                                dispose ();
                            }});
  }

  public DAWindow (String windowTitle, int width, int height)
  {
    super (windowTitle);
    setSize (width, height);
    Dimension screenSize = Toolkit.getDefaultToolkit ().getScreenSize ();
    setLocation (screenSize.width/2 - width/2, screenSize.height/2 - height/2);
    dbConnect ();
    buildMenus (); 
    buildContents ();
    this.addWindowListener (new WindowAdapter () {
                            public void windowClosing (WindowEvent e){
                                quit ();
                            }});
  }
 

  // connect to oracle database
  protected void dbConnect ()
  {
    // Connect to the database
    String driver = "oracle.jdbc.driver.OracleDriver";
    String url    = "jdbc:oracle:thin:@hp064328:1521:neurondb";
    String user   = "elsy";
    String passw  = "elsy";
    db.Connect (driver, url, user, passw);

  }


 
  //   ----  Construct Window Contents (panel, listbox and combobox)  ----
  protected void buildContents ()
  {
    // Set window Layout
    GridBagLayout gbLayout = new GridBagLayout ();
    GridBagConstraints c = new GridBagConstraints ();
    this.getContentPane().setLayout (gbLayout);
  
    //Define Layout beavior (resize)
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 1.0;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.insets = new Insets (10, 10, 10, 10);


    // add toolbar to the window
    JPanel toolPanel = addToolBar ();
    gbLayout.setConstraints (toolPanel, c);
    getContentPane ().add(toolPanel);

    c.insets = new Insets (5, 5, 5, 5);
 
    //Create panel with data fields
    JPanel dataPanel = buildPanel ();
    gbLayout.setConstraints (dataPanel, c);


    myTable = db.createTable ();
    JScrollPane listScroll = new JScrollPane (myTable, 
                                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

    //myTable.setPreferredScrollableViewportSize(new Dimension(900, 500));
    myTable.setAutoResizeMode (JTable.AUTO_RESIZE_OFF);

    //Define Layout beavior (resize)
    c.weighty = 1.0;
    gbLayout.setConstraints (listScroll, c);

    //Add panel and scrollPanel to window
    getContentPane ().add(dataPanel);
    getContentPane ().add(listScroll);

  }



  /*
   * Add toolbar to window
   *
   *
   */
  public JPanel addToolBar()
  {
    // add toolbar to the window
    JPanel toolPanel = new JPanel ();
    GridBagLayout extGb = new GridBagLayout ();
    toolPanel.setLayout (extGb);
    GridBagConstraints extC = new GridBagConstraints ();
    extC.fill = GridBagConstraints.HORIZONTAL;
    extC.weightx = 1.0;
    extC.insets = new Insets (0, 0, 0, 0);
  
    toolPanel.setBackground (SystemColor.control);
    myToolBar = new JToolBar ();
    myToolBar.setFloatable (false);
    
    myToolBar.addSeparator ();
    
    addTool(myToolBar,"Close Window","Close",true,ToolClose);
    addTool(myToolBar,"Find","Find",true,ToolFind);
    addTool(myToolBar,"Filter","Filter",true,ToolFilter);
    addTool(myToolBar,"Print the Current View","Print",true,ToolPrint);
    addTool(myToolBar,"Preferences for this Window","Preferences",true,ToolPreferences);
    addTool(myToolBar,"Open Context Help","Help",true,ToolContext); 
   

    extGb.setConstraints (myToolBar, extC);

    myToolBar.putClientProperty ("JToolBar.isRolover", Boolean.FALSE);

    extGb.setConstraints (myToolBar, extC);
    toolPanel.add (myToolBar);
    
    return toolPanel;
  }



  /*
   * Add item to toolbar
   *
   *
   */
   
  public void add(JToolBar toolBar,String ToolTip, String name, boolean enable,ActionListener listener)
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
   


  // -------------------
  // Create Panel with combobox 
  protected JPanel buildPanel ()
  {
    JPanel myPanel = new JPanel();
    panelVector.addElement (myPanel);
    myPanel.setBackground (SystemColor.control);

    // Set pannel Layout
    GridBagLayout gb = new GridBagLayout ();
    myPanel.setLayout (gb);
    myPanel.getAccessibleContext ().setAccessibleName ("Panel");
    myPanel.getAccessibleContext ().setAccessibleDescription ("Panel that" +
                                   " contains data fields");
    myPanel.setBorder ( BorderFactory.createEtchedBorder ()); 

    projectCBox = createCBox ("Project :", "SELECT code FROM Project", 
                              "Project",
                              "combo box that displays available projects");   

    return myPanel;
  }


  public JComboBox createCBox (String label, String selectStatement,
                               String name, String description)
  {
    StringBuffer lb = new StringBuffer(label);
    
    //Get exterior panel
    JPanel extPanel = (JPanel) panelVector.elementAt (0);
    GridBagLayout extGb = (GridBagLayout) extPanel.getLayout ();
    GridBagConstraints extC = new GridBagConstraints ();
    extC.fill = GridBagConstraints.HORIZONTAL;
    extC.weightx = 1.0;
    extC.insets = new Insets (10, 20, 10, 60);
    
    //panel to hold the combobox and the label (internal panel)
    JPanel myPanel = new JPanel ();
    myPanel.setBackground (SystemColor.control);

    // Set internal pannel Layout
    GridBagLayout gb = new GridBagLayout ();
    GridBagConstraints c = new GridBagConstraints ();
    myPanel.setLayout (gb);
    myPanel.getAccessibleContext ().setAccessibleName ("Panel " + name);
    myPanel.getAccessibleContext ().setAccessibleDescription ("Panel " +
                                                              description);
    c.fill = GridBagConstraints.HORIZONTAL;

    //Create label
    Font myFont = new Font ("Serif", Font.BOLD, 10);
    JLabel myLabel = new JLabel (label);
    myLabel.setFont (myFont);
    myLabel.setDisplayedMnemonic (lb.charAt (0));
    gb.setConstraints (myLabel, c);
    myPanel.add (myLabel);
   
    //Create combobox 
    c.weightx = 1.0; //resize comboBox
    JComboBox myCBox = new JComboBox ();
    panelVector.addElement (myCBox);
    myCBox.setMinimumSize (new Dimension (45, 25));
    myCBox.setFont (myFont);
    myCBox.getAccessibleContext ().setAccessibleName (name);
    myCBox.getAccessibleContext ().setAccessibleDescription (description);
    myCBox.setToolTipText ("Available Projects");
    myLabel.setLabelFor (myCBox);  //set the label for combobox
    gb.setConstraints (myCBox, c);
    myPanel.add (myCBox);
    if (selectStatement.compareTo ("") != 0)
      db.fillCBox (myCBox, selectStatement);
    
    extGb.setConstraints (myPanel, extC);
    extPanel.add (myPanel);

    return myCBox;
  }


  public JTextField createTField (String label, String name, String description,
                              boolean editable, ActionListener listener)
  {
    StringBuffer lb = new StringBuffer(label);

    //Get exterior panel
    JPanel extPanel = (JPanel) panelVector.elementAt (0);
    GridBagLayout extGb = (GridBagLayout) extPanel.getLayout ();
    GridBagConstraints extC = new GridBagConstraints ();
    extC.fill = GridBagConstraints.HORIZONTAL;
    extC.weightx = 1.0;
    extC.insets = new Insets (10, 20, 10, 20);

    //panel to hold the textfield and the label (internal panel)
    JPanel myPanel = new JPanel ();
    myPanel.setBackground (SystemColor.control);

    // Set pannel Layout
    GridBagLayout gb = new GridBagLayout ();
    GridBagConstraints c = new GridBagConstraints ();
    myPanel.setLayout (gb);
    myPanel.getAccessibleContext ().setAccessibleName ("Panel " + name);
    myPanel.getAccessibleContext ().setAccessibleDescription ("Panel " +
                                                              description);
    c.fill = GridBagConstraints.HORIZONTAL;

    Font myFont = new Font ("Serif", Font.BOLD, 10);
    JLabel myLabel = new JLabel (label);
    myLabel.setFont (myFont);
    myLabel.setDisplayedMnemonic (lb.charAt (0));
    gb.setConstraints (myLabel, c);
    myPanel.add (myLabel);

    c.weightx = 1.0; //resize textfield
    JTextField myText = new JTextField ();
    panelVector.addElement (myText);
    myText.setFont (myFont);
    myText.getAccessibleContext ().setAccessibleName (name);
    myText.getAccessibleContext ().setAccessibleDescription (description);
    myLabel.setLabelFor (myText);  //set the label for textfield
    myText.addActionListener (listener);
    myText.setEditable (editable);
    gb.setConstraints (myText, c);
    myPanel.add (myText);

    extGb.setConstraints (myPanel, extC);
    extPanel.add (myPanel);

    return myText;
  }


/*******************************************************************************
**
**                           Menu handling
*******************************************************************************/

  //   ----  Construct Window menus  ----
  protected void buildMenus ()
  {
    myMenuBar = new JMenuBar ();
    myMenuBar.setOpaque (true);
    JMenu file = buildFileMenu ();
    JMenu edit = buildEditMenu ();
    JMenu tools = buildToolsMenu ();
    JMenu help = buildHelpMenu ();
    myMenuBar.add (file);
    myMenuBar.add (edit);
    myMenuBar.add (tools);
    myMenuBar.add (help);
    setJMenuBar (myMenuBar);
  }

  //   ----  Build menu File  ---
  protected JMenu buildFileMenu ()
  {
    JMenu file = new JMenu ("File");
    file.setMnemonic ('F');
    JMenuItem itemNew = new JMenuItem ("New");
    itemNew.setMnemonic ('N');
    JMenuItem itemOpen = new JMenuItem ("Open");
    itemOpen.setMnemonic ('O');
    JMenuItem itemClose = new JMenuItem ("Close");
    itemClose.setMnemonic ('C');
    JMenuItem itemSave = new JMenuItem ("Save");
    itemSave.setMnemonic ('S');
    JMenuItem itemPSet = new JMenuItem ("Print Settings...");
    itemPSet.setMnemonic ('r');
    JMenuItem itemPrint = new JMenuItem ("Print...");
    itemPrint.setMnemonic ('P');
    JMenuItem itemQuit = new JMenuItem ("Quit");
    itemQuit.setMnemonic ('Q');
    //  Listeners
    itemClose.addActionListener (new ActionListener () {
                                public void actionPerformed (ActionEvent e)
                                {dispose ();}});
    itemQuit.addActionListener (new ActionListener () {
                                public void actionPerformed (ActionEvent e)
                                { quit ();}});
    itemPrint.addActionListener (new ActionListener () {
                                public void actionPerformed (ActionEvent e)
                                { print ();}});
    itemNew.setEnabled (false);
    itemSave.setEnabled (false);
    file.add (itemNew);
    file.add (itemOpen);
    file.add (itemClose);
    file.add (new JSeparator ());
    file.add (itemSave);
    file.add (new JSeparator ());
    file.add (itemPSet);
    file.add (itemPrint);
    file.add (new JSeparator ());
    file.add (itemQuit);
    return file;
  }

  //   ----  Build menu Edit  ---
  protected JMenu buildEditMenu ()
  {
    JMenu edit = new JMenu ("Edit");
    edit.setMnemonic ('E');
    JMenuItem itemUndo = new JMenuItem ("Undo");
    itemUndo.setMnemonic ('U');
    JMenuItem itemCut = new JMenuItem ("Cut");
    itemCut.setMnemonic ('u');
    JMenuItem itemCopy = new JMenuItem ("Copy");
    itemCopy.setMnemonic ('C');
    JMenuItem itemPaste = new JMenuItem ("Paste");
    itemPaste.setMnemonic ('P');
    JMenuItem itemDelete = new JMenuItem ("Delete");
    itemDelete.setMnemonic ('D');
    JMenuItem itemClear = new JMenuItem ("Clear");
    itemClear.setMnemonic ('l');
    JMenuItem itemPreferences = new JMenuItem ("Preferences ...");
    itemPreferences.setMnemonic ('r');
    //  Listeners
/*
    itemClose.addActionListener (new ActionListener () {
                                public void actionPerformed (ActionEvent e)
                                {clear ();}});
*/
    itemUndo.setEnabled (false);
    itemCut.setEnabled (false);
    itemCopy.setEnabled (false);
    itemPaste.setEnabled (false);
    itemDelete.setEnabled (false);
    edit.add (itemUndo);
    edit.add (new JSeparator ());
    edit.add (itemCut);
    edit.add (itemCopy);
    edit.add (itemPaste);
    edit.add (itemDelete);
    edit.add (itemClear);
    edit.add (new JSeparator ());
    edit.add (itemPreferences);
    return edit;
  }

  //   ----  Build menu Tools  ---
  protected JMenu buildToolsMenu ()
  {
    JMenu tools = new JMenu ("Tools");
    tools.setMnemonic ('T');
    JMenuItem itemFind = new JMenuItem ("Find");
    itemFind.setMnemonic ('F');
    JMenuItem itemFindAgain = new JMenuItem ("Find Again");
    itemFindAgain.setMnemonic ('A');
    JMenuItem itemFilter = new JMenuItem ("Filter ...");
    itemFilter.setMnemonic ('i');

    tools.add (itemFind);
    tools.add (itemFindAgain);
    tools.add (new JSeparator ());
    tools.add (itemFilter);
    return tools;
  }

  //   ----  Build menu Help  ---
  protected JMenu buildHelpMenu ()
  {
    JMenu help = new JMenu ("Help");
    help.setMnemonic ('H');
    JMenuItem itemIntroduction = new JMenuItem ("Introduction");
    itemIntroduction.setMnemonic ('I');
    JMenuItem itemReference = new JMenuItem ("Reference");
    itemReference.setMnemonic ('R');
    JMenuItem itemItem = new JMenuItem ("On item");
    itemItem.setMnemonic ('O');
    JMenuItem itemAbout = new JMenuItem ("About ...");
    itemAbout.setMnemonic ('A');

  // Menus with icons...
  //  Icon daimlerIcon = new ImageIcon ("logo.gif");
  //  JMenuItem itemAbout = new JMenuItem ("About ...", daimlerIcon);

    help.add (itemIntroduction);
    help.add (new JSeparator ());
    help.add (itemReference);
    help.add (itemItem);
    help.add (new JSeparator ());
    help.add (itemAbout);
    return help;
  }

  
  public void close()
  {
    setVisible(false);
    dispose();
  } // close



    //	
   //
  // Exits from program
  public void quit() {
    System.exit(0);
  }
  
  
  
  //
  // ---- RUI OLD CODE ----
  //
  //   ----  print  ----
  public void print ()
  {
  }
  
  //   ----  clear  ----
  public void clear ()
  {

  }
  
  
/*	#          #     ####    #####  ######  #    #  ######  #####    ####
	#          #    #          #    #       ##   #  #       #    #  #
	#          #     ####      #    #####   # #  #  #####   #    #   ####
	#          #         #     #    #       #  # #  #       #####        #
	#          #    #    #     #    #       #   ##  #       #   #   #    #
	######     #     ####      #    ######  #    #  ######  #    #   ####
*/
	ActionListener ToolClose = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener			
		close();    
	}};
	
	ActionListener ToolFind = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener			
	}};  
	
	ActionListener ToolFilter = new ActionListener () {
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
  
}
