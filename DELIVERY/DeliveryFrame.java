/*
 *  This class implements the delivery plan window
 *
 *
 *
 */


import java.awt.*;
import java.lang.*;
import java.awt.event.*;
import javax.java.swing.*;
import java.util.*;
import DAutil.dbase.DBAcc;
import DAutil.utilities;

//
// Window contents definition
//
public class DeliveryFrame extends DAWindow
{
  public static int WIDTH = 800;
  public static int HEIGHT = 500;

  public JTextField customerTF;
  private boolean   isAscending = true;
  
  //   ----  Window Constructor  ----
  public DeliveryFrame ()
  {

    super ("Delivery Plan", WIDTH, HEIGHT);
    controlMenus();
    buildFields ();
  }
 
  


  /*
   * Control the menu bar
   *
   */
  protected void controlMenus ()
  {
    JMenu toolsMenu = myMenuBar.getMenu(2); //get Tools menu

    //Create new MenItem - sort
    JMenu itemSort = new JMenu ("Sort");
    itemSort.setMnemonic ('S');

    ActionListener itemSortListener = new ActionListener (){
      public void actionPerformed (ActionEvent e){
      if (myTable.getRowCount() != 0)
      {
	String actionString = e.paramString ();
	int index = actionString.indexOf ("=");
	String mySubMenu = new String(actionString.substring (index + 1));

	index = myTable.getTableHeader().getColumnModel().getColumnIndex(mySubMenu);
	db.sorter.sortByColumn(index, isAscending);
      }
      }};

      int i = 0;
      boolean scan = true;
      String string;
      while (scan)
      {
	String auxStr = "Column" + i;
	try
      {
        string = _utils.resHeaderGLP.getString (auxStr);
        StringBuffer strBuffer = new StringBuffer (string);
        JMenuItem newItem = new JMenuItem (string);
        newItem.setMnemonic (strBuffer.charAt (0));
        newItem.addActionListener (itemSortListener);
        itemSort.add (newItem);
      } catch (MissingResourceException e)
      {
        scan = false;
      }
      i++;
    }

    itemSort.add (new JSeparator ());

    ActionListener ascendingListener = new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	String actionString = e.paramString ();
	int index = actionString.indexOf ("=");
	String mySubMenu = new String(actionString.substring (index + 1));
	if (mySubMenu.equals ("Ascending"))
	{
	  isAscending = true;
	}
	else if (mySubMenu.equals ("Descending"))
	{
	  isAscending = false;
	}
      }
    };


    ButtonGroup group = new ButtonGroup ();
    JRadioButtonMenuItem ascending = new JRadioButtonMenuItem ("Ascending");
    ascending.setMnemonic ('A');
    group.add (ascending);
    ascending.setSelected (true);
    ascending.addActionListener (ascendingListener);
    itemSort.add (ascending);
    JRadioButtonMenuItem descending = new JRadioButtonMenuItem ("Descending");
    descending.setMnemonic ('D');
    descending.addActionListener (ascendingListener);
    group.add (descending);
    itemSort.add (descending);

    toolsMenu.add (itemSort);


  }



 
  // -------------------
  // Create Panel with data fields
  protected void buildFields ()
  {
    ActionListener custListener = new ActionListener (){
      public void actionPerformed (ActionEvent e){
        selectData ();
    }};

    customerTF = createTField ("Customer :", "Customer",
                             "Field to input customer key", true, custListener);
   
    ActionListener airpListener = new ActionListener (){
      public void actionPerformed (ActionEvent e){
    }};

    createTField ("Airplanes :", "Airplanes", 
                "Field to display the number of airplanes",false, airpListener);

    ActionListener projectListener = new ActionListener (){
      public void actionPerformed (ActionEvent e){
      selectData ();
    }};
    projectCBox.addActionListener (projectListener);
 
  }


  /*
   *
   *
   *
   */
  public void selectData ()
  {
    String customer = customerTF.getText ();
    String project = (String) projectCBox.getSelectedItem ();

    db.table.setColumnTitle (_utils.resHeaderGLP);

    db.table.select ("SELECT a.msn, a.rank, a.BeginAssembly, a.EndAssembly, " +
		     " a.Delivery, ba.code, s.code, a.StandardCounter," +
		     " RTRIM(c.name), c.code2 || LPAD(cv.counter,2,'0')," +
		     " a.VersionCounter " +
		     "FROM Airplane a, Model m, BaseAirplane ba, StaVersion s, " +
		     " Customer c, CusVersion cv, Project p " +
		     "WHERE ba.PrId=p.ID AND P.code=" + project + " AND" +
		     " c.code2 LIKE '" + customer + "%' AND ba.id = m.baid" +
		     " AND a.mid = m.id AND a.sid = s.id AND a.vid = cv.id AND" +
		     " cv.CustomerId = c.Id ", 0);

    JTextField jt = (JTextField) panelVector.elementAt (3);
    Integer rowCount = new Integer (myTable.getRowCount());
    String myRowCount = new String (rowCount.toString());
    jt.setText (myRowCount);
    db.table.initColumnSizes (myTable);
  }




  //   ----  print  ----
  public void print ()
  {
    JTextField text1 = (JTextField) panelVector.elementAt (2);
    Print printWindow = new Print (this, myTable, "Delivery Plan",
				  "Customer:" + text1.getText () + ";",
				  null);
  }

}
