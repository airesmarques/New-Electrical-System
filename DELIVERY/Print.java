/*
 * This class implements the print window
 *
 *
 *
 */


import java.awt.*;
import java.lang.*;
import java.util.*;
import java.io.*;
import java.awt.event.*;
import javax.java.swing.*;
import javax.java.swing.preview.*;
import javax.java.swing.preview.filechooser.*;


//
// Window contents definition
//
public class Print extends JDialog
{
  public final int WIDTH = 420;
  public final int HEIGHT = 380;

  public JTable myTable;
  public Vector panel1Vector = new Vector ();
  public Vector panel2Vector = new Vector ();

  public String title;
  public String fields;
  public String colRange;

  //   ----  Window Constructor  ----
  public Print (JFrame owner, JTable table, String myTitle, String myFields,
                String myColRange)
  {

    super (owner, "Print", true);
    myTable = table;
        //If there's something to print
    if (myTable.getRowCount () != 0)
    {
      title = myTitle;
      colRange = myColRange;
      fields = myFields;
      setSize (WIDTH, HEIGHT);
      Dimension screenSize = Toolkit.getDefaultToolkit ().getScreenSize ();
      setLocation (screenSize.width/2 - WIDTH/2, screenSize.height/2 - HEIGHT/2);
      buildContents ();
      this.addWindowListener (new WindowAdapter () {
			      public void windowClosing (WindowEvent e){
				  dispose ();
			      }});
      this.show ();
    }
    else
    {
      JOptionPane.showMessageDialog(getGlassPane (),
                                    "There is nothing to print !!!");
      dispose ();
    }

  }

  private void buildContents ()
  {
    // Set window Layout
    GridBagLayout gbLayout = new GridBagLayout ();
    GridBagConstraints c = new GridBagConstraints ();
    this.getContentPane().setLayout (gbLayout);

    //Define Layout beavior (resize)
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 1.0;
    c.weighty = 1.0;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.insets = new Insets (10, 10, 10, 10);

    JPanel dataPanel = buildPanel ();
    gbLayout.setConstraints (dataPanel, c);
    buildPanelData (dataPanel);
   
    c.insets = new Insets (0, 10, 0, 10);
    JPanel optionsPanel = buildPanel ();
    gbLayout.setConstraints (optionsPanel, c);
    buildPanelOptions (optionsPanel);

    c.weighty = 0.0;
    JPanel buttonsPanel = buildButtonsPanel ();
    gbLayout.setConstraints (buttonsPanel, c);

    getContentPane ().add(dataPanel);
    getContentPane ().add(optionsPanel);
    getContentPane ().add(buttonsPanel);

  }

  private JPanel buildPanel ()
  {
    JPanel myPanel = new JPanel();
    //panelVector.addElement (myPanel);
    myPanel.setBackground (SystemColor.control);

    // Set pannel Layout
    GridBagLayout gb = new GridBagLayout ();
    myPanel.setLayout (gb);
    myPanel.getAccessibleContext ().setAccessibleName ("Panel");
    myPanel.getAccessibleContext ().setAccessibleDescription ("Panel that" +
                                   " contains data fields");
    myPanel.setBorder ( BorderFactory.createEtchedBorder ());

    return myPanel;
  }

/*
 *
 */
  private void buildPanelData (JPanel myPanel)
  {
    //Define Layout beavior (resize)
    GridBagLayout gb = (GridBagLayout) myPanel.getLayout ();
    GridBagConstraints c = new GridBagConstraints ();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 1.0;
    c.insets = new Insets (5, 5, 5, 5);

    Font myFont = new Font ("Serif", Font.BOLD, 10);
    JLabel printTo = new JLabel ("Print To :");
    panel1Vector.addElement (printTo);
    printTo.setFont (myFont);
    printTo.setToolTipText ("Choose where to print to");
    printTo.setDisplayedMnemonic ('P');
    gb.setConstraints (printTo, c);
    myPanel.add (printTo);


    ButtonGroup group = new ButtonGroup ();
    JRadioButton printerRB = new JRadioButton ("Printer", true);
    panel1Vector.addElement (printerRB);
    printerRB.addItemListener (radioButtonsListener);
    printTo.setLabelFor (printerRB);
    printerRB.setFont (myFont);
    group.add (printerRB);
    gb.setConstraints (printerRB, c);
    myPanel.add (printerRB);

    JRadioButton fileRB = new JRadioButton ("File");
    fileRB.addItemListener (radioButtonsListener);
    fileRB.setFont (myFont);
    group.add (fileRB);
    gb.setConstraints (fileRB, c);
    myPanel.add (fileRB);

    c.gridwidth = GridBagConstraints.REMAINDER;
    JPanel auxPanel = new JPanel ();
    auxPanel.setBackground (SystemColor.control);
    gb.setConstraints (auxPanel, c);
    myPanel.add (auxPanel);

    c.gridwidth = 1;
    JLabel printCmd = new JLabel ("Print Command :");
    panel1Vector.addElement (printCmd);
    printCmd.setFont (myFont);
    printCmd.setToolTipText ("Specifie the print command");
    printCmd.setDisplayedMnemonic ('C');
    gb.setConstraints (printCmd, c);
    myPanel.add (printCmd);

    c.gridwidth = GridBagConstraints.REMAINDER;
    JTextField printCmdTF = new JTextField ();
    panel1Vector.addElement (printCmdTF);
    printCmd.setLabelFor (printCmdTF);
    printCmdTF.setFont (myFont);
    printCmdTF.setToolTipText ("Specifie the print command");
    printCmdTF.setText ("lp");
    gb.setConstraints (printCmdTF, c);
    myPanel.add (printCmdTF);

    c.gridwidth = 1;
    JLabel fileName = new JLabel ("File Name :");
    panel1Vector.addElement (fileName);
    fileName.setFont (myFont);
    fileName.setToolTipText ("Specifie the file to print on");
    fileName.setDisplayedMnemonic ('F');
    fileName.setEnabled (false);
    gb.setConstraints (fileName, c);
    myPanel.add (fileName);

    c.gridwidth = GridBagConstraints.RELATIVE;
    JTextField fileNameTF = new JTextField ();
    panel1Vector.addElement (fileNameTF);
    fileName.setLabelFor (fileNameTF);
    fileNameTF.setFont (myFont);
    fileNameTF.setEnabled (false);
    fileNameTF.setToolTipText (" Specifie the file to print on");
    gb.setConstraints (fileNameTF, c);
    myPanel.add (fileNameTF);

    c.gridwidth = GridBagConstraints.REMAINDER;
    JButton browse = new JButton ("Browse...");
    panel1Vector.addElement (browse);
    browse.setFont (myFont);
    browse.setToolTipText ("Select one specific file");
    browse.setMnemonic ('B');
    browse.setEnabled (false);
    browse.addActionListener (new ActionListener (){
         public void actionPerformed (ActionEvent e)
         {
           JFileChooser chooser = new JFileChooser ();
           int retval = chooser.showDialog (getGlassPane(), null);
           if(retval == JFileChooser.APPROVE_OPTION)
           {
             File theFile = chooser.getSelectedFile();
             if(theFile != null)
             {
                if(theFile.isDirectory())
                {
                    JOptionPane.showMessageDialog(
                        getGlassPane (), "You chosed one Directory: " + 
                        chooser.getSelectedFile().getAbsolutePath());
                }
                else
                {
                  JTextField tfFile = (JTextField) panel1Vector.elementAt (5); 
                  tfFile.setText (chooser.getSelectedFile().getAbsolutePath());
                }
             }
           }
         }});

    gb.setConstraints (browse, c);
    myPanel.add (browse);
  }


/*
 *
 */
  private void buildPanelOptions (JPanel myPanel)
  {

    //Define Layout beavior (resize)
    GridBagLayout gb = (GridBagLayout) myPanel.getLayout ();
    GridBagConstraints c = new GridBagConstraints ();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 1.0;
    c.insets = new Insets (5, 5, 5, 5);

    Font myFont = new Font ("Serif", Font.BOLD, 10);
    JLabel orientation = new JLabel ("Orientation :");
    orientation.setFont (myFont);
    orientation.setToolTipText ("Orientation of paper");
    orientation.setDisplayedMnemonic ('O');
    gb.setConstraints (orientation, c);
    myPanel.add (orientation);

    ButtonGroup group = new ButtonGroup ();
    JRadioButton portrait = new JRadioButton ("Portrait", true);
    panel2Vector.addElement (portrait);
    orientation.setLabelFor (portrait);
    portrait.setFont (myFont);
    group.add (portrait);
    gb.setConstraints (portrait, c);
    myPanel.add (portrait);

    c.gridwidth = GridBagConstraints.REMAINDER;
    JRadioButton landscape = new JRadioButton ("Landscape");
    panel2Vector.addElement (landscape);
    landscape.setFont (myFont);
    group.add (landscape);
    gb.setConstraints (landscape, c);
    myPanel.add (landscape);

    c.gridwidth = 1;
    JLabel paperSize = new JLabel ("Paper Size :");
    paperSize.setFont (myFont);
    paperSize.setToolTipText ("Size of paper");
    paperSize.setDisplayedMnemonic ('S');
    gb.setConstraints (paperSize, c);
    myPanel.add (paperSize);

    c.gridwidth = GridBagConstraints.REMAINDER;
    JTextField paperSizeTF = new JTextField();
    panel2Vector.addElement (paperSizeTF);
    paperSize.setLabelFor (paperSizeTF);
    paperSizeTF.setFont (myFont);
    paperSizeTF.setToolTipText ("Size of paper");
    paperSizeTF.setEditable (false);
    paperSizeTF.setText ("A4 - 698x945 points");
    gb.setConstraints (paperSizeTF, c);
    myPanel.add (paperSizeTF);
    
    c.gridwidth = 1;
    JLabel copies = new JLabel ("Num. Copies :");
    copies.setFont (myFont);
    copies.setToolTipText("Number of copies to print.");
    copies.setDisplayedMnemonic ('N');
    gb.setConstraints (copies, c);
    myPanel.add (copies);

    JTextField copiesTF = new JTextField();
    panel2Vector.addElement (copiesTF);
    copies.setLabelFor (copiesTF);
    copiesTF.setFont (myFont);
    copiesTF.setToolTipText("Number of copies to print.");
    gb.setConstraints (copiesTF, c);
    myPanel.add (copiesTF);
    
  
  }


/*
 *
 */
  private JPanel buildButtonsPanel ()
  {
    FlowLayout fl = new FlowLayout (FlowLayout.CENTER, 10, 10);
    //Define Layout beavior (resize)

    JPanel myPanel = new JPanel ();
    myPanel.setLayout (fl);
    myPanel.getAccessibleContext ().setAccessibleName ("Panel3");
    myPanel.getAccessibleContext ().setAccessibleDescription ("Panel that" +
                                   " contains print and cancel buttons");

    Font myFont = new Font ("Serif", Font.BOLD, 11);

    JButton print = new JButton ("Print");
    print.setPreferredSize (new Dimension (70, 30));
    print.setFont (myFont);
    print.addActionListener (buttonsListener);
    myPanel.add (print);

    JButton cancel = new JButton ("Cancel");
    cancel.setPreferredSize (new Dimension (80, 30));
    cancel.setFont (myFont);
    cancel.addActionListener (new ActionListener (){
                             public void actionPerformed (ActionEvent e)
                             {
                               dispose ();
                             }});
    myPanel.add (cancel);

    return myPanel;
  }


  ActionListener buttonsListener = new ActionListener ()
  {
    public void actionPerformed (ActionEvent e)
    {
      Integer numCopies;
      JTextField tf = (JTextField) panel2Vector.elementAt (3);
      String copies = new String (tf.getText ());
      if (copies.compareTo ("") == 0)
        numCopies = new Integer (1);
      else
        numCopies = new Integer (copies);
      
      JRadioButton bt = (JRadioButton) panel2Vector.elementAt (1);
      boolean isLandscape = false;
      if (bt.isSelected ())
        isLandscape = true;
      else
        isLandscape = false;
     
      String file; 
      JRadioButton printerRB = (JRadioButton) panel1Vector.elementAt (1);
      if (printerRB.isSelected ()) 
      {
        file = new String ("print.ps");
        PostscriptPrint pr = new PostscriptPrint (file, isLandscape,
                                     myTable, numCopies.intValue(), title, 
                                     fields, colRange);
	if (!pr.errorApened)
	{
  System.out.println ("You must finish the code to send to the printer!!!!");
  // send file to printer using something like system commands.
	  dispose ();
	}
      }
      else
      {
        JTextField tfFile = (JTextField) panel1Vector.elementAt (5); 
        file = new String (tfFile.getText ());
        if (file.compareTo ("") != 0)
        {
          if (file.indexOf (".") == -1)
          {
            String auxFile = file;
            file = new String (auxFile + ".ps");
          }
          PostscriptPrint pr = new PostscriptPrint (file, isLandscape,
                                     myTable, numCopies.intValue(), title, 
                                     fields, colRange);
	  dispose ();
        }
        else
        {
          JOptionPane.showMessageDialog(getGlassPane (),
                                    "Choosing 'Print to File'" +
                                    " You must specify one file!!!");
        }
      }
    }
  };


/*
 *JRadioButton Listeners
 */
  ItemListener radioButtonsListener = new ItemListener ()
  {
    public void itemStateChanged (ItemEvent e)
    {
      JRadioButton rb = (JRadioButton) e.getSource ();
      if ((rb.getText ().equals ("Printer")) && 
          (e.getStateChange() == ItemEvent.SELECTED))
      {
        JLabel jl = (JLabel) panel1Vector.elementAt (2);
        jl.setEnabled (true);
        JTextField jt = (JTextField) panel1Vector.elementAt (3);
        jt.setEnabled (true);
        JLabel jl1 = (JLabel) panel1Vector.elementAt (4);
        jl1.setEnabled (false);
        JTextField jt1 = (JTextField) panel1Vector.elementAt (5);
        jt1.setEnabled (false);
        JButton jb = (JButton) panel1Vector.elementAt (6);
        jb.setEnabled (false);
        //to force the repaint
        jl.invalidate ();
        jl.validate ();
        jl.repaint ();
        jl1.invalidate ();
        jl1.validate ();
        jl1.repaint ();
      }
      else if ((rb.getText ().equals ("File")) &&
              (e.getStateChange() == ItemEvent.SELECTED))
      {
        JLabel jl = (JLabel) panel1Vector.elementAt (2);
        jl.setEnabled (false);
        JTextField jt = (JTextField) panel1Vector.elementAt (3);
        jt.setEnabled (false);
        JLabel jl1 = (JLabel) panel1Vector.elementAt (4);
        jl1.setEnabled (true);
        JTextField jt1 = (JTextField) panel1Vector.elementAt (5);
        jt1.setEnabled (true);
        JButton jb = (JButton) panel1Vector.elementAt (6);
        jb.setEnabled (true);
        //to force the repaint
        jl.invalidate ();
        jl.validate ();
        jl.repaint ();
        jl1.invalidate ();
        jl1.validate ();
        jl1.repaint ();
      }
    }
  };

}
