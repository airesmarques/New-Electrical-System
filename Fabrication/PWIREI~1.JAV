/*
 About this File:
	- This files shows the list of wires.
	- In the beginning of a secsion there are big titles for easy locating each component.
	- The big titles were made with the banner unix command.
	
	(c) Aires Marques, Tip2, DASA 1998
*/



import javax.swing.*;
import java.awt.*;
import java.lang.*;
import java.awt.event.*;
import java.util.*;



public class pWireInfo extends javax.swing.JFrame
{
	final static int HEIGHT=465;
	final static int WIDTH=570;

	private JToolBar	pWireInfoToolBar;
	
	private JPanel 		UpperPanel,
				DownPanel,
				ToolBarPanel;

	private utilities	_utils;
				
	
	GridBagLayout Layout;
	GridBagConstraints c;
	

	String 	projectStr,
		pWireStr,
		effectivityStr;



	 // Fields that are used in this window.
	// - Upper Panel -
	private JTextField 	ProjectTxt = new JTextField(5),
				WireNameTxt = new JTextField(8),
				DescriptionTxt = new JTextField(20),
				EffectivityTxt = new JTextField(8);
	// - Down Panel -
	private JTextField	ColorTxt	= new JTextField(9),
				TwistTxt	= new JTextField(9),
				ManLenTxt	= new JTextField(9),
				ChDateTxt	= new JTextField(9),
				StatusTxt	= new JTextField(3);
	private JCheckBox	ShortWireChkB	= new JCheckBox("ShortWire"),
				SensitivChkB	= new JCheckBox("Sensitiv");
	private JTextArea	RemarkArea	= new JTextArea();
	private JTextField	Fin1Txt		= new JTextField(8),
				Pin1Txt		= new JTextField(8),
				Fin2Txt		= new JTextField(8),
				Pin2Txt		= new JTextField(8);


	  // Description: This is the constructor for the List of Wires when 
	 // data is provided by the file epd1.txt1
	//
	public pWireInfo(String _project, String _pWire, String _effectivity, utilities Utilities)
	{
	 super("pWireInfo: Physical Wire Info");
	 _utils = Utilities;
	 
	 projectStr = new String(_project);
	 pWireStr = new String(_pWire);
	 effectivityStr = new String(_effectivity);
	 
	 
	 setBackground (Color.lightGray);
	 this.setSize(WIDTH, HEIGHT);
	 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	 setLocation (150,150);
	 this.setVisible(true);
	
	
	 // Create Layout and add ToolBar				
	 Layout = new GridBagLayout();
	 c = new GridBagConstraints();
	 this.getContentPane().setLayout(Layout);
	 ToolBarPanel = new JPanel();

	 ToolBarPanel.setBackground(Color.lightGray);
	 ToolBarPanel.setLayout(Layout);
	 c.gridwidth = GridBagConstraints.REMAINDER;			
	 c.weightx = 1.0;
	 c.fill = GridBagConstraints.HORIZONTAL;	//
	 c.insets = new Insets(0,5,0,5);
	 ToolBarPanel = addToolBar();
	 Layout.setConstraints(ToolBarPanel,c);
	 this.getContentPane().add(ToolBarPanel);
	 
	 // Prepare the Layout and "Stuff" for the next panel
	 c.fill = GridBagConstraints.HORIZONTAL;
	 c.weightx=2.0;
	 c.insets = new Insets(5,5,5,5);
	 
	 // Create the Upper_Panel
	 UpperPanel = Upper_Panel();
	 Layout.setConstraints(UpperPanel,c);
	 this.getContentPane().add(UpperPanel);	 
	 
	 
	 
	 c.gridwidth = GridBagConstraints.REMAINDER;
	 c.fill = GridBagConstraints.BOTH;
	 	 
	 // Create the Down_Panel
	 DownPanel = Down_Panel();
	 Layout.setConstraints(DownPanel,c);
	 this.getContentPane().add(DownPanel);

 
 	 FillFields();
	 
	 Menus();
	}
	
	
	
	
/*	#    #  #####   #####   ######  #####           #####     ##    #    #  ######  #
	#    #  #    #  #    #  #       #    #          #    #   #  #   ##   #  #	#
	#    #  #    #  #    #  #####   #    #  #####   #    #  #    #  # #  #  #####	#
	#    #  #####   #####   #       #####           #####   ######  #  # #  #	#
	#    #  #       #       #       #   #           #       #    #  #   ##  #	#
	 ####   #       #       ######  #    #          #       #    #  #    #  ######	##### 
*/
    
    	  //
	 //
	//
	private JPanel Upper_Panel(){
		JPanel Panel= new JPanel();
		GridBagLayout Layout = new GridBagLayout();
	 	GridBagConstraints c_up = new GridBagConstraints();
		c_up.fill = GridBagConstraints.HORIZONTAL;
		c_up.insets = new Insets(0,0,0,0);
		c_up.weightx=1.0;
		Panel.setLayout (Layout);
		Panel.setFont(new Font("Dialog", Font.PLAIN, 12));
		

		// Setting border and background
		Panel.setBorder(BorderFactory.createLineBorder(Color.black));
		Panel.setBackground(Color.lightGray);

		// Creating the Sub Panel Number 1
		JPanel SubPanel_1 = new JPanel();
		GridBagLayout SubLayout1 = new GridBagLayout();
		GridBagConstraints c_upSub1 = new GridBagConstraints();
		c_upSub1.fill = GridBagConstraints.HORIZONTAL;
		c_upSub1.insets = new Insets(5,5,5,5);
		c_upSub1.weightx=1.0;
		SubPanel_1.setLayout (SubLayout1);
		SubPanel_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		SubLayout1.setConstraints(SubPanel_1,c_up);
		Panel.add(SubPanel_1);
		
		
		// Creating and adding the Sub Panel number 2
		JPanel SubPanel_2 = new JPanel();
		GridBagLayout SubLayout2 = new GridBagLayout();
		GridBagConstraints c_upSub2 = new GridBagConstraints();
		c_upSub2.fill = GridBagConstraints.HORIZONTAL;
		c_upSub2.insets = new Insets(5,5,5,5);
		c_upSub2.weightx=2.0;
		SubPanel_2.setLayout (SubLayout2);
		SubPanel_2.setFont(new Font("Dialog", Font.PLAIN, 22));
		SubLayout2.setConstraints(SubPanel_2,c_up);
		Panel.add(SubPanel_2);



		// 	-----------
		//	SUB PANEL 1
		//	-----------
				
			// Project
		// Adding the JLabel "Project" 
		JLabel ProjectLabel = new JLabel("Project");
		ProjectLabel.setForeground(Color.black);
		SubLayout1.setConstraints(ProjectLabel,c_upSub1);
		SubPanel_1.add(ProjectLabel);
				
		c_upSub1.gridwidth = GridBagConstraints.REMAINDER;
				
		// Adding a JTextField Project
		ProjectTxt.setBackground(Color.white);
		SubLayout1.setConstraints(ProjectTxt,c_upSub1);
		SubPanel_1.add(ProjectTxt);
		//ProjectTxt.setText(ProjectStr);
		ProjectTxt.setDisabledTextColor(Color.black);
		ProjectTxt.setEnabled(false);

		c_upSub1.gridwidth = 1;		
			// Wire Name
		// Adding the JLabel "WireName"
		JLabel WireNameLabel = new JLabel("Wire Name");
		WireNameLabel.setForeground(Color.black);
		SubLayout1.setConstraints(WireNameLabel,c_upSub1);
		SubPanel_1.add(WireNameLabel);
		
		
		// Adding a JTextField WireName
		c_upSub1.gridwidth = GridBagConstraints.REMAINDER;
		WireNameTxt.setBackground(Color.white);
		SubLayout1.setConstraints(WireNameTxt,c_upSub1);
		SubPanel_1.add(WireNameTxt);
		//ProjectTxt.setText(WireNameStr);
		WireNameTxt.setDisabledTextColor(Color.black);
		WireNameTxt.setEnabled(false);
		
		
		
		
		// 	-----------
		//	SUB PANEL 2
		//	-----------
				
			// Description
		c_upSub2.gridwidth = 1;			
		// Adding the JLabel "Description" 
		JLabel DescriptionLabel = new JLabel("Description");
		DescriptionLabel.setForeground(Color.black);
		SubLayout2.setConstraints(DescriptionLabel,c_upSub2);
		SubPanel_2.add(DescriptionLabel);

		c_upSub2.gridwidth = GridBagConstraints.REMAINDER;
		// Adding a JTextField Description
		DescriptionTxt.setBackground(Color.white);
		SubLayout2.setConstraints(DescriptionTxt,c_upSub2);
		SubPanel_2.add(DescriptionTxt);
		//ProjectTxt.setText(DescriptionStr);
		DescriptionTxt.setDisabledTextColor(Color.black);
		DescriptionTxt.setEnabled(false);
		
		
		
			//Effectivity
		c_upSub2.gridwidth = 1;
			
		// Adding the JLabel "Effectivity"
		JLabel EffectivityLabel = new JLabel("Effectivity");
		EffectivityLabel.setForeground(Color.black);
		SubLayout2.setConstraints(EffectivityLabel,c_upSub2);
		SubPanel_2.add(EffectivityLabel);
		
		c_upSub2.gridwidth = GridBagConstraints.REMAINDER;
		// Adding a JTextField Effectivity
		EffectivityTxt.setBackground(Color.white);
		SubLayout2.setConstraints(EffectivityTxt,c_upSub2);
		SubPanel_2.add(EffectivityTxt);
		//ProjectTxt.setText(EffectivityStr);
		EffectivityTxt.setDisabledTextColor(Color.black);
		EffectivityTxt.setEnabled(false);
		
		

		// Creating the Remainder Panel
		// Description:	This  panel  avoids  that  the   others 
		// 		turn to big when the window is resized.
		JPanel RemainderPanel = new JPanel();
		GridBagLayout RamainderLayout = new GridBagLayout();
		GridBagConstraints c_upRemainder = new GridBagConstraints();
		
		c_up.gridwidth = GridBagConstraints.REMAINDER;
		c_up.fill = GridBagConstraints.BOTH;
		c_up.insets = new Insets(0,0,0,0);
		c_up.weightx=1.0;
		
		//RemainderPanel.setLayout (RamainderLayout);
		//RemainderPanel.setFont(new Font("Dialog", Font.PLAIN, 12));
		Layout.setConstraints(RemainderPanel,c_up);
		Panel.add(RemainderPanel);
		
		
		return Panel;
	} // private JPanel Upper_Panel()
	
	

/*
 	#####    ####   #    #  #    #          #####     ##    #    #  ######  #
 	#    #  #    #  #    #  ##   #          #    #   #  #   ##   #  #       #
 	#    #  #    #  #    #  # #  #  #####   #    #  #    #  # #  #  #####   #
 	#    #  #    #  # ## #  #  # #          #####   ######  #  # #  #       #
 	#    #  #    #  ##  ##  #   ##          #       #    #  #   ##  #       #
	#####    ####   #    #  #    #          #       #    #  #    #  ######  ######
*/
	
	  //
	 //
	//
	private JPanel Down_Panel() {
		JPanel Panel= new JPanel();
		GridBagLayout Layout = new GridBagLayout();
	 	GridBagConstraints c_down = new GridBagConstraints();
		c_down.fill = GridBagConstraints.HORIZONTAL;
		c_down.insets = new Insets(10,10,10,10);
		c_down.weightx=1.0;
		
		Panel.setLayout (Layout);
		Panel.setFont(new Font("Dialog", Font.PLAIN, 12));
		

		// Setting border and background
		Panel.setBorder(BorderFactory.createLineBorder(Color.black));
		Panel.setBackground(Color.lightGray);


		//	----------------
		//	PROPERTIES PANEL
		//	----------------		
		JLabel 		PropertiesLab	= new JLabel("Properties"),
				ColorLab	= new JLabel("Color"),
				TwistLab	= new JLabel("Twist"),
				ManLenLab	= new JLabel("Manufacturer Length"),
				ChDateLab	= new JLabel("Change Date"),
				StatusLab	= new JLabel("Status"),
				RemarkLab	= new JLabel("Remark");
				
		JPanel PropertiesPanel = new JPanel();
		GridBagLayout PropertiesLayout = new GridBagLayout();
		GridBagConstraints c_Prop = new GridBagConstraints();
		c_Prop.fill = GridBagConstraints.HORIZONTAL;
		c_Prop.insets = new Insets(5,5,5,5);
		c_Prop.weightx=1.0;
		c_Prop.anchor = GridBagConstraints.NORTHWEST;
		PropertiesPanel.setLayout (PropertiesLayout);
		PropertiesPanel.setFont(new Font("Dialog", Font.PLAIN, 12));
		PropertiesPanel.setBackground(Color.lightGray);
		Layout.setConstraints(PropertiesPanel,c_down);
		Panel.add(PropertiesPanel);


		// Adding the JLabel "Properties" - The Title of this Panel
		c_Prop.gridwidth = GridBagConstraints.REMAINDER;
		c_Prop.insets = new Insets(0,0,0,0);
		PropertiesLab.setForeground(Color.black);
		PropertiesLayout.setConstraints(PropertiesLab,c_Prop);
		PropertiesPanel.add(PropertiesLab);
		PropertiesLab.setBorder(BorderFactory.createLineBorder(Color.black));
		
		// Restore the my default insets
		c_Prop.insets = new Insets(5,5,5,5);
		c_Prop.fill = GridBagConstraints.EAST;


		//--------------------------------------------------------------		
		// This Panel owns the next 2 subpanels above
		c_Prop.anchor = GridBagConstraints.NORTHWEST;
		c_Prop.gridwidth = 1;
		JPanel PropSP_OwnerPanel = new JPanel();
		GridBagLayout PropSP_OwnerLayout = new GridBagLayout();
		GridBagConstraints c_PropSP_Owner = new GridBagConstraints();
		c_PropSP_Owner.anchor = GridBagConstraints.NORTHWEST;		
		c_PropSP_Owner.fill = GridBagConstraints.HORIZONTAL;
		c_PropSP_Owner.insets = new Insets(5,5,5,5);
		c_PropSP_Owner.weightx=1.0;
		PropSP_OwnerPanel.setLayout (PropSP_OwnerLayout);
		PropSP_OwnerPanel.setFont(new Font("Dialog", Font.PLAIN, 12));
		PropertiesLayout.setConstraints(PropSP_OwnerPanel,c_Prop);
		PropSP_OwnerPanel.setBackground(Color.lightGray);
		PropertiesPanel.add(PropSP_OwnerPanel);
		
		//PropertiesPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		//PropSP_OwnerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		//---------------------------------------------------------------

				
		
		//	PropSP11
		//	PROPERTIES SubPanel_11 ((
		// 
		c_PropSP_Owner.gridwidth = 1;
		c_PropSP_Owner.anchor = GridBagConstraints.NORTHWEST;
		JPanel PropSP11Panel = new JPanel();
		GridBagLayout PropSP11Layout = new GridBagLayout();
		GridBagConstraints c_PropSP11 = new GridBagConstraints();
		c_PropSP11.fill = GridBagConstraints.HORIZONTAL;
		c_PropSP11.insets = new Insets(5,5,5,5);
		c_PropSP11.weightx=1.0;
		PropSP11Panel.setLayout (PropSP11Layout);
		PropSP11Panel.setFont(new Font("Dialog", Font.PLAIN, 12));		
		PropSP_OwnerLayout.setConstraints(PropSP_OwnerPanel,c_PropSP_Owner);
		PropSP11Panel.setBackground(Color.lightGray);
		//PropSP11Panel.setBorder(BorderFactory.createLineBorder(Color.green));
				
		PropSP_OwnerPanel.add(PropSP11Panel);
		// {{
					
			 // Beginning of introducing stuff in the Panel PropSP11
		   	 c_PropSP11.gridwidth = GridBagConstraints.REMAINDER;
		   
			 // Creating the Panel for the Color Label and JTextField
			 JPanel ColorPanel = new JPanel();
			 GridBagLayout PropSP11ColorLayout = new GridBagLayout();
			 GridBagConstraints c_PropColor = new GridBagConstraints();
			 c_PropColor.fill = GridBagConstraints.HORIZONTAL;
			 c_PropColor.insets = new Insets(0,0,0,0);
			 c_PropColor.weightx=1.0;
			 
			 ColorPanel.setLayout (PropSP11ColorLayout);
			 ColorPanel.setFont(new Font("Dialog", Font.PLAIN, 12));
			 PropSP11ColorLayout.setConstraints(ColorPanel,c_PropColor);
			 ColorPanel.setBackground(Color.lightGray);
			
			 PropSP11Layout.setConstraints(ColorPanel,c_PropSP11);
		   	 PropSP11Panel.add(ColorPanel); 
				   
			 // Adding the JLabel "Color"
			 ColorLab.setForeground(Color.black);
			 PropSP11ColorLayout.setConstraints(ColorLab,c_PropColor);
			 ColorPanel.add(ColorLab);

			 // Adding the JTextField of "Color"
			 ColorTxt.setBackground(Color.white);
			 c_PropColor.gridwidth = GridBagConstraints.REMAINDER;
			 c_PropColor.insets = new Insets(0,5,0,0);
			 PropSP11ColorLayout.setConstraints(ColorTxt,c_PropColor);
			 ColorPanel.add(ColorTxt);
			 ColorTxt.setDisabledTextColor(Color.black);
			 ColorTxt.setEnabled(false);


			 

		   // Adding the CheckBox of "ShortWire"
		   c_PropSP11.gridwidth = 1;
		   PropSP11Layout.setConstraints(ShortWireChkB,c_PropSP11);
		   PropSP11Panel.add(ShortWireChkB);
		   ShortWireChkB.setOpaque(false);
		   //ShortWireChkB.setDisabledTextColor(Color.black);
		   //ShortWireChkB.setEnabled(false);
		   
		// }}  
		// )) PROPERTIES SubPanel 11


		//	PropSP12
		//	PROPERTIES SubPanel_12 ((
		// 
		c_PropSP_Owner.insets = new Insets(5,5,5,5);
		c_PropSP_Owner.gridwidth = 1;
		JPanel PropSP12Panel = new JPanel();
		GridBagLayout PropSP12Layout = new GridBagLayout();
		GridBagConstraints c_PropSP12 = new GridBagConstraints();
		c_PropSP12.fill = GridBagConstraints.HORIZONTAL;
		c_PropSP12.insets = new Insets(5,0,5,5);
		c_PropSP12.weightx=1.0;
		PropSP12Panel.setLayout (PropSP12Layout);
		PropSP12Panel.setFont(new Font("Dialog", Font.PLAIN, 12));
		PropertiesLayout.setConstraints(PropSP_OwnerPanel,c_PropSP_Owner);
		PropSP12Panel.setBackground(Color.lightGray);
		PropSP_OwnerPanel.add(PropSP12Panel);
		//PropSP12Panel.setBorder(BorderFactory.createLineBorder(Color.green));
		
		
		
			 // Beginning of introducing stuff in the Panel PropSP12
		   	 c_PropSP12.gridwidth = GridBagConstraints.REMAINDER;
		   
			 // Creating the Panel for the Color Label and JTextField
			 JPanel TwistPanel = new JPanel();
			 GridBagLayout PropSP12TwistLayout = new GridBagLayout();
			 GridBagConstraints c_PropTwist = new GridBagConstraints();
			 c_PropTwist.fill = GridBagConstraints.HORIZONTAL;
			 c_PropTwist.insets = new Insets(0,0,0,0);
			 c_PropTwist.weightx=1.0;
			 
			 TwistPanel.setLayout (PropSP12TwistLayout);
			 TwistPanel.setFont(new Font("Dialog", Font.PLAIN, 12));
			 PropSP12TwistLayout.setConstraints(TwistPanel,c_PropTwist);
			 TwistPanel.setBackground(Color.lightGray);
			
			 PropSP12Layout.setConstraints(TwistPanel,c_PropSP12);
		   	 PropSP12Panel.add(TwistPanel); 
		
			 // Adding the JLabel "Twist"
			 TwistLab.setForeground(Color.black);
			 PropSP12TwistLayout.setConstraints(TwistLab,c_PropTwist);
			 TwistPanel.add(TwistLab);

			 // Adding the JTextField of "Twist"
			 TwistTxt.setBackground(Color.white);
			 c_PropTwist.gridwidth = GridBagConstraints.REMAINDER;
			 c_PropTwist.insets = new Insets(0,5,0,0);
			 PropSP12TwistLayout.setConstraints(TwistTxt,c_PropTwist);
			 TwistPanel.add(TwistTxt);
			 TwistTxt.setDisabledTextColor(Color.black);
			 TwistTxt.setEnabled(false);		

			
		   // Adding the CheckBox of "Sensitiv"
		   c_PropSP12.weightx=2.0;
		   PropSP12Layout.setConstraints(SensitivChkB,c_PropSP12);
		   PropSP12Panel.add(SensitivChkB);
		   SensitivChkB.setOpaque(false);
		   //SensitivChkB.setDisabledTextColor(Color.black);
		   //SensitivChkB.setEnabled(false);
		// ))
		
		
		c_PropSP_Owner.gridwidth = GridBagConstraints.REMAINDER;
		JPanel PropSP999Panel = new JPanel();
		PropSP_OwnerLayout.setConstraints(PropSP999Panel,c_PropSP_Owner);
		PropSP_OwnerPanel.add(PropSP999Panel);
		PropSP999Panel.setBackground(Color.lightGray);
		//PropSP999Panel.setBorder(BorderFactory.createLineBorder(Color.green));
		
		

		//	PropSP13
		//	PROPERTIES SubPane_13
		//	Description: This panel takes the rest of the Space
		// ((
		  c_Prop.gridwidth = GridBagConstraints.REMAINDER;
		  JPanel PropSP13Panel = new JPanel();
		  GridBagLayout PropSP13Layout = new GridBagLayout();
		  GridBagConstraints c_PropSP13 = new GridBagConstraints();
		  c_PropSP13.fill = GridBagConstraints.EAST;
		  c_PropSP13.insets = new Insets(0,0,0,0);
		  c_PropSP13.weightx=1.0;
		  PropSP13Panel.setLayout (PropSP13Layout);
		  PropertiesLayout.setConstraints(PropSP13Panel,c_Prop);
		  PropertiesPanel.add(PropSP13Panel);
		// ))




		//	PropSP21
		//	PROPERTIES SubPanel_21 ((
		// 	Description: It owns the Manufacturer Length JLabel and JTextField
		c_Prop.gridwidth = GridBagConstraints.REMAINDER;
		JPanel PropSP21Panel = new JPanel();
		GridBagLayout PropSP21Layout = new GridBagLayout();
		GridBagConstraints c_PropSP21 = new GridBagConstraints();
		c_PropSP21.fill = GridBagConstraints.HORIZONTAL;
		c_PropSP21.insets = new Insets(5,5,5,5);
		c_PropSP21.weightx=1.0;
		PropSP21Panel.setLayout (PropSP21Layout);
		PropSP21Panel.setFont(new Font("Dialog", Font.PLAIN, 21));
		PropSP21Panel.setBackground(Color.lightGray);
		PropertiesLayout.setConstraints(PropSP21Panel,c_Prop);
		PropertiesPanel.add(PropSP21Panel);
		// {{		
		
		    // Adding the JLabel "Manufacturer Length"
		    c_PropSP21.gridwidth = 1;
		    ManLenLab.setForeground(Color.black);
		    PropSP21Layout.setConstraints(ManLenLab,c_PropSP21);
		    PropSP21Panel.add(ManLenLab);

		    // Adding the JTextField of "Manufacturer Length"
		    ManLenTxt.setBackground(Color.white);
		    PropSP21Layout.setConstraints(ManLenTxt,c_PropSP21);
		    PropSP21Panel.add(ManLenTxt);
		    ManLenTxt.setDisabledTextColor(Color.black);
		    ManLenTxt.setEnabled(false);
		// }}
		// ))
		
		





		//	PropSP31
		//	PROPERTIES SubPanel_31 ((
		// 	Description: It owns the Manufacturer Length JLabel and JTextField
		c_Prop.gridwidth = GridBagConstraints.REMAINDER;
		JPanel PropSP31Panel = new JPanel();
		GridBagLayout PropSP31Layout = new GridBagLayout();
		GridBagConstraints c_PropSP31 = new GridBagConstraints();
		c_PropSP31.fill = GridBagConstraints.HORIZONTAL;
		c_PropSP31.insets = new Insets(5,5,5,5);
		c_PropSP31.weightx=1.0;
		PropSP31Panel.setLayout (PropSP31Layout);
		PropSP31Panel.setFont(new Font("Dialog", Font.PLAIN, 12));
		PropSP31Panel.setBackground(Color.lightGray);
		PropertiesLayout.setConstraints(PropSP31Panel,c_Prop);
		PropertiesPanel.add(PropSP31Panel);
		// {{		
		
		    // Adding the JLabel "Change Date"
		    c_PropSP31.gridwidth = 1;
		    ChDateLab.setForeground(Color.black);
		    PropSP31Layout.setConstraints(ChDateLab,c_PropSP31);
		    PropSP31Panel.add(ChDateLab);

		    // Adding the JTextField of "Change Date"
		    ChDateTxt.setBackground(Color.white);
		    PropSP31Layout.setConstraints(ChDateTxt,c_PropSP31);
		    PropSP31Panel.add(ChDateTxt);
		    ChDateTxt.setDisabledTextColor(Color.black);
		    ChDateTxt.setEnabled(false);
		// }}
		// ))





		//	PropSP41
		//	PROPERTIES SubPanel_41 ((
		// 	Description: It owns the Manufacturer Length JLabel and JTextField
		c_Prop.gridwidth = GridBagConstraints.REMAINDER;
		JPanel PropSP41Panel = new JPanel();
		GridBagLayout PropSP41Layout = new GridBagLayout();
		GridBagConstraints c_PropSP41 = new GridBagConstraints();
		c_PropSP41.fill = GridBagConstraints.HORIZONTAL;
		c_PropSP41.insets = new Insets(5,5,5,5);
		c_PropSP41.weightx=1.0;
		PropSP41Panel.setLayout (PropSP41Layout);
		PropSP41Panel.setFont(new Font("Dialog", Font.PLAIN, 12));
		PropSP41Panel.setBackground(Color.lightGray);
		PropertiesLayout.setConstraints(PropSP41Panel,c_Prop);
		PropertiesPanel.add(PropSP41Panel);
		// {{		
		
		    // Adding the JLabel "Status"
		    c_PropSP41.gridwidth = 1;
		    StatusLab.setForeground(Color.black);
		    PropSP41Layout.setConstraints(StatusLab,c_PropSP41);
		    PropSP41Panel.add(StatusLab);

		    // Adding the JTextField of "Status"
		    StatusTxt.setBackground(Color.white);
		    PropSP41Layout.setConstraints(StatusTxt,c_PropSP41);
		    PropSP41Panel.add(StatusTxt);
		    StatusTxt.setDisabledTextColor(Color.black);
		    StatusTxt.setEnabled(false);
		// }}
		// ))
		
		
		//	PropSP51
		//	PROPERTIES SubPanel_51 ((
		// 	Description: It owns the Manufacturer Length JLabel and JTextField
		c_Prop.gridwidth = GridBagConstraints.REMAINDER;
		JPanel PropSP51Panel = new JPanel();
		GridBagLayout PropSP51Layout = new GridBagLayout();
		GridBagConstraints c_PropSP51 = new GridBagConstraints();
		c_PropSP51.fill = GridBagConstraints.HORIZONTAL;
		c_PropSP51.insets = new Insets(5,5,5,5);
		c_PropSP51.weightx=1.0;
		PropSP51Panel.setLayout (PropSP51Layout);
		PropSP51Panel.setFont(new Font("Dialog", Font.PLAIN, 12));
		PropSP51Panel.setBackground(Color.lightGray);
		PropertiesLayout.setConstraints(PropSP51Panel,c_Prop);
		PropertiesPanel.add(PropSP51Panel);
		// {{		
		
		    // Adding the JLabel "Remark"
		    c_PropSP51.gridwidth = 1;
		    RemarkLab.setForeground(Color.black);
		    PropSP51Layout.setConstraints(RemarkLab,c_PropSP51);
		    PropSP51Panel.add(RemarkLab);

		    // Adding the Remark JTextArea
		    RemarkArea.setRows(2);
		    RemarkArea.setColumns(18);
		    RemarkArea.setForeground(Color.black);
		    PropSP51Layout.setConstraints(RemarkArea,c_PropSP51);
		    PropSP51Panel.add(RemarkArea);
		    RemarkArea.setEnabled(false);
		    RemarkArea.setLineWrap(true);
		    RemarkArea.setWrapStyleWord(true);
		    RemarkArea.setDisabledTextColor(Color.black);
		    
		// }}
		// ))


		
		
				
		
		
		//	----------------
		//	CONNECTION PANEL
		//	----------------
		JLabel		ConnectionLab	= new JLabel("Connection"),
				Fin1Lab		= new JLabel("Fin1"),
				Pin1Lab		= new JLabel("Pin1"),
				Fin2Lab		= new JLabel("Fin2"),
				Pin2Lab		= new JLabel("Pin2");
				
		c_down.gridwidth = GridBagConstraints.REMAINDER;
		c_down.fill=GridBagConstraints.NONE;
		c_down.anchor = GridBagConstraints.NORTHWEST;
		JPanel ConnectionPanel = new JPanel();
		GridBagLayout ConnectionLayout = new GridBagLayout();
		GridBagConstraints c_Conn = new GridBagConstraints();
		c_Conn.fill = GridBagConstraints.HORIZONTAL;
		c_Conn.insets = new Insets(5,5,5,5);
		c_Conn.weightx=1.0;
		c_Conn.anchor = GridBagConstraints.NORTHWEST;
		ConnectionPanel.setLayout (ConnectionLayout);
		ConnectionPanel.setFont(new Font("Dialog", Font.PLAIN, 12));
		ConnectionPanel.setBackground(Color.lightGray);
		Layout.setConstraints(ConnectionPanel,c_down);
		Panel.add(ConnectionPanel);

		// Adding the JLabel "Connection" - The Title of this Panel
		c_Conn.gridwidth = GridBagConstraints.REMAINDER;
		c_Conn.fill = GridBagConstraints.HORIZONTAL;
		c_Conn.insets = new Insets(0,0,0,0);
		ConnectionLab.setForeground(Color.black);
		ConnectionLayout.setConstraints(ConnectionLab,c_Conn);
		ConnectionPanel.add(ConnectionLab);
		ConnectionLab.setBorder(BorderFactory.createLineBorder(Color.black));
						
		c_Conn.insets = new Insets(5,5,5,5);
		c_Conn.gridwidth = 1;
		c_Conn.fill = GridBagConstraints.NONE;
				
		// Adding the JLabel "Fin1"
		Fin1Lab.setForeground(Color.black);
		ConnectionLayout.setConstraints(Fin1Lab,c_Conn);
		ConnectionPanel.add(Fin1Lab);


		// Adding the JTextField of "Fin1"
		Fin1Txt.setBackground(Color.white);
		c_Conn.gridwidth = GridBagConstraints.REMAINDER;
		c_Conn.insets = new Insets(5,5,5,5);
		ConnectionLayout.setConstraints(Fin1Txt,c_Conn);
		ConnectionPanel.add(Fin1Txt);
		Fin1Txt.setDisabledTextColor(Color.black);
		Fin1Txt.setEnabled(false);
		
		
		// Adding the JLabel "Pin1"
		c_Conn.gridwidth = 1;
		Pin1Lab.setForeground(Color.black);
		ConnectionLayout.setConstraints(Pin1Lab,c_Conn);
		ConnectionPanel.add(Pin1Lab);
		
		// Adding the JTextField of "Pin1"
		Pin1Txt.setBackground(Color.white);
		c_Conn.gridwidth = GridBagConstraints.REMAINDER;
		c_Conn.insets = new Insets(5,5,5,5);
		ConnectionLayout.setConstraints(Pin1Txt,c_Conn);
		ConnectionPanel.add(Pin1Txt);
		Pin1Txt.setDisabledTextColor(Color.black);
		Pin1Txt.setEnabled(false);
		
		// Adding the JLabel "Fin2"
		c_Conn.gridwidth = 1;
		Fin2Lab.setForeground(Color.black);
		ConnectionLayout.setConstraints(Fin2Lab,c_Conn);
		ConnectionPanel.add(Fin2Lab);
		
		// Adding the JTextField of "Fin2"
		Fin2Txt.setBackground(Color.white);
		c_Conn.gridwidth = GridBagConstraints.REMAINDER;
		c_Conn.insets = new Insets(5,5,5,5);
		ConnectionLayout.setConstraints(Fin2Txt,c_Conn);
		ConnectionPanel.add(Fin2Txt);
		Fin2Txt.setDisabledTextColor(Color.black);
		Fin2Txt.setEnabled(false);
		
		// Adding the JLabel "Pin2"
		c_Conn.gridwidth = 1;
		Pin2Lab.setForeground(Color.black);
		ConnectionLayout.setConstraints(Pin2Lab,c_Conn);
		ConnectionPanel.add(Pin2Lab);
		
		
		// Adding the JTextField of "Pin2"
		Pin2Txt.setBackground(Color.white);
		c_Conn.gridwidth = GridBagConstraints.REMAINDER;
		c_Conn.insets = new Insets(5,5,5,5);
		ConnectionLayout.setConstraints(Pin2Txt,c_Conn);
		ConnectionPanel.add(Pin2Txt);
		Pin2Txt.setDisabledTextColor(Color.black);
		Pin2Txt.setEnabled(false);
		
		
		//
		// Panel that takes the rest of the space
		//
		/*c_down.gridwidth = GridBagConstraints.REMAINDER;
		c_down.fill=GridBagConstraints.BOTH;
		JPanel FinalRemainderPanel = new JPanel();
		GridBagLayout FinalRemainderLayout = new GridBagLayout();
		Layout.setConstraints(FinalRemainderPanel,c_down);
		Panel.add(FinalRemainderPanel);
		FinalRemainderPanel.setBorder(BorderFactory.createLineBorder(Color.green));
		*/

			
	
		return Panel;
	} // private JPanel Down_Panel()
	

	
	

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
			{    quit();     } } );
		
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
		  DeleteItem.setEnabled(false);
	
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
	  #####   #    #     #    #    #
	  
 $DATA - This line is for finding proposes only. */
 
	  
        public void FillFields() {

		DBAcc db = new DBAcc (_utils);
		
		db.table.select("SELECT Description FROM Project WHERE ID=" + projectStr);

		//auxData.Id = new Integer (db.table.getValueAt (i,0).toString()).intValue();

		ProjectTxt.setText(projectStr);
		WireNameTxt.setText(pWireStr);
		DescriptionTxt.setText(db.table.getValueAt(0,0).toString());
		EffectivityTxt.setText(effectivityStr);
		
		db.table.select("SELECT F1.Name, P1.Name, F2.Name, P2.Name," +
                       		" epW.manufacturerLength, epW.chDate, epW.status," +
                       		" epW.remark FROM Fin F1, Fin F2, lTerminal P1," +
                       		" lTerminal P2, effpWire epW, pWire pW," +
                     		" Effectivity E, collection C, AirplGroup AG," +
                       		" Airplane A, Model M, BaseAirplane BA, Project P " +
                       		"WHERE P.ID=" + projectStr + " AND BA.prID=P.ID AND M.baID=BA.ID" +
                       		" AND A.mID=M.ID AND AG.aID=A.ID AND C.ID=AG.cID" +
                       		" AND pW.Name='" + pWireStr+ "' AND pW.ID=epW.pwID AND epW.effID=E.ID" +
                       		" AND E.ID=C.ID AND C.Name='" + effectivityStr+ "' AND epW.leftltID=P1.ID" +
                       		" AND epW.rightltID=P2.ID AND P1.fID=F1.ID AND" +
                       		" P2.fID=F2.ID");
				
		Fin1Txt.setText(db.table.getValueAt(0,0).toString());
		Pin1Txt.setText(db.table.getValueAt(0,1).toString());
		Fin2Txt.setText(db.table.getValueAt(0,2).toString());
		Pin2Txt.setText(db.table.getValueAt(0,3).toString());
		ManLenTxt.setText(db.table.getValueAt(0,4).toString());
		if(db.table.getValueAt(0,5)!=null)
			ChDateTxt.setText(db.table.getValueAt(0,5).toString());
		if(db.table.getValueAt(0,6)!=null)
			StatusTxt.setText(db.table.getValueAt(0,6).toString());
		if(db.table.getValueAt(0,7)!=null)
			RemarkArea.insert(db.table.getValueAt(0,7).toString(),0);
				
	}
	


/*
	 #####   ####    ####   #       #####     ##    #####
	   #    #    #  #    #  #       #    #   #  #   #    #
	   #    #    #  #    #  #       #####   #    #  #    #
	   #    #    #  #    #  #       #    #  ######  #####
	   #    #    #  #    #  #       #    #  #    #  #   #
	   #     ####    ####   ######  #####   #    #  #    #
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
		
							
		Panel_ToolBar.setBackground(Color.lightGray);	
				
		// Create the toolbar
		
		pWireInfoToolBar= new JToolBar();		
		pWireInfoToolBar.setBorderPainted(false);
		pWireInfoToolBar.setFloatable (false);
		
		// Call addTool Function
		
		addTool(pWireInfoToolBar,"Close Window","Close",true,ToolClose);
		addTool(pWireInfoToolBar,"Print","Print",true,ToolPrint);
		addTool(pWireInfoToolBar,"Preferences","Preferences",true,ToolPreferences);
		addTool(pWireInfoToolBar,"Open Context Help","Help",true,ToolHelp);	
		
		Bar_Layout.setConstraints(pWireInfoToolBar,c);		
		pWireInfoToolBar.putClientProperty ("JToolBar.isRolover",Boolean.FALSE);
		Bar_Layout.setConstraints(pWireInfoToolBar,c);
		Panel_ToolBar.add(pWireInfoToolBar);		
		return Panel_ToolBar;
		
		
	}
	// Add item to toolbar



	// Add item to toolbar
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



	ActionListener ToolClose = new ActionListener () {
        public void actionPerformed (ActionEvent e) {    
		// Code Listener			
		close();    
	}};
	//  DarkCode - Insert here the rest of the listeners 


	ActionListener ToolPrint = new ActionListener() {
	public void actionPerformed (ActionEvent e) {    
		// Code Listener			
	}};
	
	ActionListener ToolPreferences = new ActionListener() {
	public void actionPerformed (ActionEvent e) {    
		// Code Listener			
	}};
	
	ActionListener ToolHelp = new ActionListener() {
	public void actionPerformed (ActionEvent e) {    
		// Code Listener			
	}};
	



	  //
	 //
	//
	private void close()
        {
		setVisible(false);
		dispose();
        } // close
	

	
	  //	
	 //
	// Exits from program
	private void quit() {
		//SetVisible(false);
		dispose();
		System.exit(0);
	}
	


		
		
		
	  // Description: This function is for testing only and is to be called by the main...
	 //  This file is not to be a standalone aplication, therefore there is not need of
	//   an existing main(String[] args) rather then for testing.
	static void init()
	{
	
	        javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme(new javax.swing.plaf.metal.DefaultMetalTheme ());

	        JFrame pWireInfo = new pWireInfo("80","6056","SQ01-1-10",new utilities());
  	        pWireInfo.setVisible(true);
	}
	
	/*static public void main(String[] args)
	{
		
		init();
	}*/
	

} // public class pWireInfo extends javax.swing.JFrame
