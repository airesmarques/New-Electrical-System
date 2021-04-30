/*
 * Class that write a postscript file to send to the printer
 *
 *
 */

import java.lang.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import javax.java.swing.*;


public class PostscriptPrint
{
  public DataOutputStream os;
  public boolean	errorApened = false;


  private int 		scaleFactor = 0;
  private int 		numPages = 0;
  private int 		totalRecs = 0;
  private int 		curRec = 0;
  private int 		curPage = 0;
  private int 		nCopies = 0;
  private int 		numOfRows = 10;
  private int 		paperWidth = 598;
  private int 		paperHeight = 845;
  private boolean 	isLandscape = false;
  private JTable	myTable;
  private String	title;
  private String	fieldsString;
  private int[]		colRange;
 
  private PaperPositions pp;

  /*
   *  Constructor
   *
   */
  public PostscriptPrint(String fileName, boolean land, JTable table,
                   int copies, String myTitle, String myFields, String cRange)
  {
    isLandscape = land;
    myTable = table;
    nCopies = copies;
    title = new String (myTitle);
    fieldsString = new String (myFields);
    setColumnRange (cRange);
    if (initializeVariables (fileName))
    {
      if (!createPostscriptFile ())
      {
         message();
         errorApened = true;
      }
    }
    else
    {
      message ();
      errorApened = true;
    }
  }

  private void message ()
  {
    JOptionPane.showMessageDialog (null , 
                                  "ERROR: Can not print. Data missing!!!");
  }
  

  private boolean initializeVariables (String fileName)
  {
    // Create output stream
    try
    {
      FileOutputStream fos = new FileOutputStream (fileName);
      os = new DataOutputStream (fos);
    } catch (java.io.IOException ex) {
	System.out.println ("ERROR : can't open file!!!");
        return false;
    }

    pp = new PaperPositions (isLandscape);
    //Calculate scale factor.
    if (isLandscape)
    {
      int paperWidth = pp.ps.paperSize.height - pp.ps.paperMargins.bottom -
                       pp.ps.paperMargins.top;
      int totalColumnWidth = getTotalColumnWidth ();
      if (totalColumnWidth > paperWidth)
        scaleFactor = (paperWidth * 100) / totalColumnWidth;
      else
        scaleFactor = 100;
    }
    else
    {
      int paperWidth = pp.ps.paperSize.width - pp.ps.paperMargins.left -
                       pp.ps.paperMargins.right;
      int totalColumnWidth = getTotalColumnWidth ();
      if (totalColumnWidth > paperWidth)
        scaleFactor = (paperWidth * 100) / totalColumnWidth;
      else
        scaleFactor = 100;

    }
    numOfRows = (int) ((pp.bodyVLine - 4) / 10);
    numOfRows = (numOfRows * 100) / scaleFactor;
    pp.bodyVLine = (pp.bodyVLine * 100) / scaleFactor;
    return true;
  }




  /*
   *
   *
   */
  private boolean createPostscriptFile ()
  {
    if (myTable.getRowCount() != 0)
    {
      setNumberOfPages (); 
      if (!createFileHeader ())
	return false;
      if (!createPageProlog ())
	return false;

      for (int i=1 ; i<=numPages ; i++)
      {
         curPage++;
         if (!createPagesBody ())
           return false;
      }
      if (!createFileTail ())
        return false;
    }
    return true;
  }




  /*
   *
   *
   */
  private void setNumberOfPages ()
  {
    totalRecs = myTable.getRowCount ();
    numPages = totalRecs / numOfRows;
    if ((myTable.getRowCount () % numOfRows) > 0 )
      numPages++;
  }



  /*
   *
   */
  private boolean createFileHeader ()
  {
    try {
      os.writeBytes ("%!PS-Adobe-3.0\n" +
                     "%%BoundingBox: 0 0 " + paperWidth + " "
                                           + paperHeight + "\n" +
                     "%%Creator: " + System.getProperty ("user.name") + "\n" +
                     "%%Title: Listbox printing\n" +
                     "%%CreationDate: " + new Date().toString() + "\n");
      if ( isLandscape )
        os.writeBytes ("%%Orientation: Landscape\n");
      else
        os.writeBytes ("%%Orientation: Portrait\n");

      os.writeBytes ("%%Pages: " + numPages + "\n" +
                     "%%PageOrder: Ascend\n" +
                     "%%EndComments\n\n\n" +
                     "%%BeginProlog\n\n");

    } catch (java.io.IOException ex) {
	System.out.println ("ERROR : can't write file!!!");
        return false;
    }
    return true;
  }



  /*
   *
   */
  private boolean createPageProlog ()
  {
    try {
      os.writeBytes ("/DrawHeader{\n" +
                     "  2 setlinewidth\n" +
                     "  newpath\n" +
                     "    " + pp.topLine.x + " " + pp.topLine.y + " moveto\n" +
                     "    " + pp.topLine.width + " " + pp.topLine.height +
                                                   " lineto\n" +
                     "    stroke\n" +
                     "}def\n\n" +
                     "/DrawBottom{\n" +
                     "  2 setlinewidth\n" +
                     "  newpath\n" +
                     "    " + pp.bottomLine.x + " " + pp.bottomLine.y +
                                                   " moveto\n" +
                     "    " + pp.bottomLine.width + " " + pp.bottomLine.height +
                                                   " lineto\n" +
                     "    stroke\n" +
                     "}def\n\n" +
                     "/DrawTitle{\n" +
                     "  /Times-Bold findfont 15 scalefont setfont\n");
      if (isLandscape)
      {
        os.writeBytes ("  " + pp.topTitle.x + "\n" +
                       "  " + pp.topTitle.y + "\n" +
                       "  (" + title + ") stringwidth pop sub\n" +
                       "  2 div\n" +
                       "  translate\n" +
                       "  90 rotate\n");
      }
      else
      {
        os.writeBytes ("  " + pp.topTitle.x + "\n" +
                       "  (" + title + ") stringwidth pop sub\n" +
                       "  2 div\n" +
                       "  " + pp.topTitle.y + " translate\n"
                      );
      }
      os.writeBytes ("  0 0 moveto\n" +
                     "  (" + title + ") show\n");

      if (isLandscape)
      {
        os.writeBytes ("  270 rotate\n" +
                       "  -" + pp.topTitle.x + "\n" +
                       "  -" + pp.topTitle.y + "\n" +
                       "  (" + title + ") stringwidth pop add\n" +
                       "  2 div \n" +
                       "  translate\n" +
                       "}def\n\n");
      }
      else
      {
        os.writeBytes ("  -" + pp.topTitle.x + "\n" +
                       "  (" + title + ") stringwidth pop add\n" +
                       "  2 div \n" +
                       "  -" + pp.topTitle.y + " translate\n" +
                       "}def\n\n");

      }

      os.writeBytes ("/DrawOrder{\n" +
                     "  /Times-Bold findfont 10 scalefont setfont\n" +
                     "  " + pp.bottomOrder.x + " " + pp.bottomOrder.y +
                                                 " translate\n");
       
      if (isLandscape)
        os.writeBytes ("  90 rotate\n");

      os.writeBytes ("  0 0 moveto\n" +
                     "  (Order: ) show\n" +
                     "  /Times-Roman findfont 10 scalefont setfont\n" +
                     "  (" + System.getProperty ("user.name") +  ") show\n");

      if (isLandscape)
        os.writeBytes ("  270 rotate\n");

      os.writeBytes ("  " + -pp.bottomOrder.x + " " + -pp.bottomOrder.y +
                                                    " translate\n" +
                     "}def\n\n" +
                     "/DrawDate{\n" +
                     "  /Times-Bold findfont 10 scalefont setfont\n" +
                     "  " + pp.bottomDate.x + " " + pp.bottomDate.y +
                                                   " translate\n");

      if (isLandscape)
        os.writeBytes ("  90 rotate\n");

      os.writeBytes ("  0 0 moveto\n" +
                     "  (Date: ) show\n" +
                     "  /Times-Roman findfont 10 scalefont setfont\n" +
                     "  (" + new Date().toString() + ") show\n");

      if (isLandscape)
        os.writeBytes ("  270 rotate\n");

      os.writeBytes ("  " + -pp.bottomDate.x + " " + -pp.bottomDate.y +
                                                   " translate\n" +
                     "}def\n\n" +
                     "/DrawPageHeaderLine{\n" +
                     "  1 setlinewidth\n" +
                     "  newpath\n" +
                     "    " + pp.bodyLine.x + " " + pp.bodyLine.y +
                                                  " moveto\n" +
                     "    " + pp.bodyLine.width + " " + pp.bodyLine.height +
                                                  " lineto\n" +
                     "    stroke\n" +
                     "}def\n\n" +
                     "/DrawPageHeader{\n" +
                     "  /Times-Bold findfont " +
                                myTable.getTableHeader().getFont().getSize() +
                                " scalefont setfont\n"); 

      if (isLandscape)
        os.writeBytes ("  90 rotate\n");

      os.writeBytes ("  0 0 moveto\n");

      boolean first = true;
      int auxPosition = 0;
      int position = 0;
      for (int i=0 ; i < myTable.getColumnCount () ; i++)
      {
        if (colRange[i] == 1)
        {
          if (first)
          {
            os.writeBytes ("  (" +
               myTable.getTableHeader().getColumnModel().getColumn(i).getHeaderValue() +
              ") show\n");
            first = false;
          }
          else
          {
            os.writeBytes ("  newpath\n" +
                           "    " + auxPosition + " 15 moveto\n" +
                           "    " + auxPosition + " -" + pp.bodyVLine +
                                                      " lineto\n" +
                           "    stroke\n" +
                           "  " + position + " 0 moveto\n" +
                           "  (" + myTable.getTableHeader().getColumnModel().getColumn(i).getHeaderValue() + ") show\n");
          }
          position = position +
              myTable.getTableHeader().getColumnModel().getColumn(i).getWidth();
          auxPosition = position - 4;
        }
      }

      if (isLandscape)
        os.writeBytes ("  270 rotate\n");
      
      os.writeBytes ("}def\n\n" +
                     "/DrawFields{\n" +
                     "  " + pp.topHeader.x + " " + pp.topHeader.y +
                                                 " translate\n");
    
      if (isLandscape)
        os.writeBytes ("  90 rotate\n");

      os.writeBytes ("  0 0 moveto\n");
      
      /* Retriev fields that make restrictions ***********************/
      String buffer1;
      String buffer2;
      int lastIndex = 0;
      int index = 0;
      boolean search = true;
      first = true;
      while (search)
      {
        if ((index = fieldsString.indexOf (":", lastIndex)) != -1)
        {
	   if (first)
	     first = false;
	   else
	     lastIndex = lastIndex + 1; 

           buffer1 = fieldsString.substring (lastIndex, index);
           lastIndex = index;
           if ((index = fieldsString.indexOf (";", lastIndex)) != -1)
           {
	     lastIndex = lastIndex + 1; 
	     buffer2 = fieldsString.substring (lastIndex, index);
	     lastIndex = index;
             
             os.writeBytes ("  /Times-Bold findfont 12 scalefont setfont\n" +
                            "  20 0 rmoveto\n" +
                            "  (" + buffer1 + ": ) show\n" +
                            "  /Times-Roman findfont 12 scalefont setfont\n" +
                            "  (" + buffer2 + ") show\n");
           }
           else
             search = false;
        }
        else
          search = false;

      }

      if (isLandscape)
        os.writeBytes ("  270 rotate\n");

      os.writeBytes ("  " + -pp.topHeader.x + " " + -pp.topHeader.y +
                                                   " translate\n" +
                      "}def\n\n" +
                      "/BeginEPSF{\n" +
                      "  /b4_Inc_state save def\n" +
                      "  /dict_count countdictstack def\n" +
                      "  /op_count count 1 sub def\n" +
                      "  userdict begin\n" +
                      "  /showpage { } def\n" +
                      "  0 setgray 0 setlinecap\n" +
                      "  1 setlinewidth 0 setlinejoin\n" +
		      "  10 setmiterlimit [ ] 0 setdash newpath\n" +
		      "  /languagelevel where\n" +
		      "  {pop languagelevel\n" +
		      "  1 ne\n" +
		      "    {false setstrokeadjust false setoverprint\n" +
		      "    }if\n" +
		      "  }if\n" +
		      "} bind def\n\n" +
                      "/EndEPSF {\n" +
		      "  count op_count sub {pop} repeat\n" +
		      "  countdictstack dict_count sub {end} repeat\n" +
		      "  b4_Inc_state restore\n" +
		      "} bind def\n\n\n" +
                      "/InsertEPSFiles{\n\n");


      if (!InsertEPSFiles ("TopLogo.eps", pp.topLogo.x, pp.topLogo.y,
                                                     pp.topLogo.width))
        return false;

      if (!InsertEPSFiles ("BottomLogo.eps", pp.bottomLogo.x, pp.bottomLogo.y,
                                                     pp.bottomLogo.width))
        return false;

      os.writeBytes ( "}def\n\n"+
		      "%%EndProlog\n\n" +
		      "%%BeginSetup\n" +
		      "  /#copies " + nCopies + " def\n" +
		      "%%EndSetup\n\n");


    } catch (java.io.IOException ex) {
	System.out.println ("ERROR : " + ex.toString ());
        return false;
    }
    return true;
  }


  /*
   * function that writes final instructions to the file
   *
   *
   */
  private boolean InsertEPSFiles (String filename, int logoX,
                                  int logoY, int width)
  {
    try {
      FileInputStream fis = new FileInputStream (filename);
      BufferedReader is = new BufferedReader (new InputStreamReader (fis));
   
      os.writeBytes ("  %Encapsulated Postscript File\n" +
                     "  BeginEPSF\n" +
                     "  " + logoX + " " + logoY +
                                              " translate\n");

      if (isLandscape)
        os.writeBytes ("90 rotate\n");

      String line = new String ();
      boolean goOn = true;
      while (goOn)
      {
        line = is.readLine ();
        int index = 0;
        if ((index = line.indexOf ("%BoundingBox")) != -1)
        {
          goOn = false;
          String auxString = line.substring (index);
          index = auxString.indexOf (" ");
          line = auxString.substring (index + 1);
          index = line.indexOf (" ");
          auxString = line.substring (index + 1);
          index = auxString.indexOf (" ");
          line = auxString.substring (index + 1);
          int index1 = line.indexOf (" ");
          Integer x = new Integer (line.substring (index, index1));
          Integer y = new Integer (line.substring (index1 + 1));
	  double scalex = (double) width / x.intValue ();
	  if (scalex > 1)
	    scalex = 1;

	  os.writeBytes (scalex + " " + scalex + " scale\n" +
			 "%%BeginDocument: " + filename + "\n");
        }
      }

      is.close ();
      fis.close ();

      fis = new FileInputStream (filename);
      is = new BufferedReader (new InputStreamReader (fis));
      
      goOn = true;
      while (goOn)
      {
        line = is.readLine ();
        if (line == null)
        {
          goOn = false;
        }
        else
        {
          os.writeBytes (line + "\n");
        }
      }
      os.writeBytes ("\n  %%EndDocument\n" +
                     "  EndEPSF\n\n");
      is.close ();
      fis.close ();

    } catch (java.io.IOException ex) {
	System.out.println ("ERROR : " + ex.toString ());
        return false;
    }
    return true;

  }



  /*
   * function that writes final instructions to the file
   *
   *
   */
  private boolean createPagesBody ()
  {
    try {
      os.writeBytes ("\n%%Page: " + curPage + " " + curPage + "\n" +
                     "%%BeginPageSetup\n" +
                     "/saveobj save def\n\n" +
                     "%%EndPageSetup\n\n" +
                     "InsertEPSFiles\n" +
                     "\n/Times-Bold findfont 10 scalefont setfont\n" +
                     pp.bottomPage.x + " " + pp.bottomPage.y + " translate\n");

     if (isLandscape)
        os.writeBytes ("  90 rotate\n");

      os.writeBytes ("0 0 moveto\n" +
                     "(Page number: ) show\n" +
                     "/Times-Roman findfont 10 scalefont setfont\n" +
                     "(" + curPage + "/" + numPages + ") show\n");

      if (isLandscape)
        os.writeBytes ("  270 rotate\n");

      os.writeBytes (-pp.bottomPage.x + " " + -pp.bottomPage.y +
                                               " translate\n" +
                     "DrawHeader\n" +
                     "DrawBottom\n" +
                     "DrawTitle\n" +
                     "DrawOrder\n" +
                     "DrawDate\n" +
                     "DrawFields\n" +
                     "DrawPageHeaderLine\n" +
                     pp.bodyHeader.x + " " + pp.bodyHeader.y + " translate\n");
      
      if (scaleFactor == 100) 
        os.writeBytes ("1 1 scale\n" +
                       "DrawPageHeader\n\n");
      else
        os.writeBytes ("." + scaleFactor + " ." + scaleFactor + " scale\n" +
                       "DrawPageHeader\n\n" +
                       "/Times-Roman findfont 10 scalefont setfont\n");

      if (isLandscape)
        os.writeBytes ("20 0 translate\n" +
                       "90 rotate\n");
      else
        os.writeBytes ("0 -20 translate\n");

      os.writeBytes ("0 0 moveto\n");

      int cols = myTable.getColumnCount ();
      int loopRec = 0;
      while ((loopRec < numOfRows) && (curRec != totalRecs))
      {
        curRec++;
        loopRec++;
        boolean first = true;
        int position = 0;
        for (int i=0 ; i<cols ; i++)
        {
          if (colRange[i] == 1)
          {
	    if (first)
	    {
	      first = false;
	      os.writeBytes ("  (" +
		 changeCaracters ( (String) myTable.getModel().getValueAt (curRec - 1, i)) +
				 ") show\n");
	    }
	    else
	    {
	      os.writeBytes ("  " + position + " 0 moveto\n" +
			     "  (" +
		 changeCaracters ( (String) myTable.getModel().getValueAt (curRec - 1, i)) +
				 ") show\n");
	    }
	    position = position +
	      myTable.getTableHeader().getColumnModel().getColumn(i).getWidth();
          }
        }
        if ((loopRec != numOfRows) && (curRec != totalRecs))
        {
          os.writeBytes ("0 -10 translate\n" +
                         "0 0 moveto\n");
        }
      }
     
      os.writeBytes ("\n\nshowpage\n" +
                     "saveobj restore\n\n");

    } catch (java.io.IOException ex) {
	System.out.println ("ERROR : can't write file!!!");
        return false;
    }
    return true;
  }



  /*
   * function that put scape caracters in some special caracters
   *
   *
   */
  private String changeCaracters (String string)
  {

    String auxString1;
    if (string != null)
    {
      //Change "(" for "\("
      int index = 0;
      String auxString;
      StringBuffer str1;
      if (( index = string.indexOf ("(")) != -1)
      {
	str1 = new StringBuffer(string.substring (0, index));
	str1.append ("\\");
	str1.append (string.substring (index));
        auxString = new String (str1.toString ());
      }
      else
      {
	auxString = new String(string);
      }

      //Change ")" for "\)"

      index = 0;
      StringBuffer str2;
      if (( index = auxString.indexOf (")")) != -1)
      {
	str2 = new StringBuffer(auxString.substring (0, index));
	str2.append ("\\");
	str2.append (auxString.substring (index));
        auxString1 = new String (str2.toString ());
      }
      else
      {
	auxString1 = new String(auxString);
      }
      return auxString1;
    }
    else
      return null;
  }



  /*
   * function that writes final instructions to the file
   *
   *
   */
  private boolean createFileTail ()
  {
    try {
      os.writeBytes ("%%Trailer\n" +
                     "%%Pages: " + numPages + "\n" +
                     "%%EOF\n");

    } catch (java.io.IOException ex) {
	System.out.println ("ERROR : can't write file!!!");
        return false;
    }
    return true;
  }



  /*
   * function creates one array that contains the range of columns to print 
   *
   *
   */
  private void setColumnRange (String cRange)
  {
    int columns = myTable.getColumnCount ();
    colRange = new int[columns];
    if (cRange == null)
    {
      for (int i=0 ; i<columns ; i++)
         colRange[i] = 1;
    }
    else
    {
      boolean scan = true;
      int index = 0;
      int index1 = 0;
      while (scan)
      {
        index = index1;
        if ((index1 = cRange.indexOf ("[", index)) == -1)
          break;

        index = index1;
        if ((index1 = cRange.indexOf ("...", index)) != -1)
        {
          Integer fNum = new Integer (cRange.substring (index + 1, index1));
          index = index1;
          if (( index1 = cRange.indexOf ("]", index)) != -1)
          {
            Integer sNum = new Integer (cRange.substring (index + 3, index1));
            for (int j=fNum.intValue() ; j<=sNum.intValue() ; j++)
              colRange[j] = 1; 
          }
          else
          {
            for (int j=fNum.intValue() ; j<columns ; j++)
              colRange[j] = 1;
            break;
          } 
        }
        else
          break;
      }

    }

  }

  /*
   * function that counts the width of the columns that are in colRange 
   *
   *
   */
  private int getTotalColumnWidth ()
  {
    int columns = myTable.getColumnCount ();
    int totalWidth = 0;
  
    for (int i=0 ; i<columns ; i++)
    {
      if (colRange[i] == 1)
	totalWidth = totalWidth +
            myTable.getTableHeader().getColumnModel().getColumn(i).getWidth();
    } 
    return totalWidth;
  }




  /*
   *Class that sets the positions of all elements of the report in the paper
   *
   *
   */
  public class PaperPositions
  {

    public Rectangle 	bottomLine = new Rectangle ();
    public Rectangle 	bottomLogo = new Rectangle ();
    public Point	bottomOrder = new Point ();
    public Point	bottomDate = new Point ();
    public Point	bottomPage = new Point ();
   
    public Rectangle	topLine = new Rectangle ();
    public Rectangle	topLogo = new Rectangle ();
    public Point	topTitle = new Point ();
    public Point	topHeader = new Point ();

    public Rectangle	bodyLine = new Rectangle ();
    public Point	bodyHeader = new Point ();
    public Point	bodyText = new Point ();
    public int		bodyVLine = 0;

    public PaperSettings ps;

    public PaperPositions (boolean isLandscape)
    {
      ps = new PaperSettings ();
  
      if (isLandscape)
      {
        bottomLine.x = ps.paperSize.width - ps.paperMargins.right - 40;
        bottomLine.y = ps.paperSize.height - ps.paperMargins.top;
        bottomLine.width = ps.paperSize.width - ps.paperMargins.right - 40;
        bottomLine.height = ps.paperMargins.bottom;
        bottomLogo.width = 160;
        bottomLogo.height = 40;
        bottomLogo.x = ps.paperSize.width - ps.paperMargins.right;
        bottomLogo.y = ps.paperSize.height - ps.paperMargins.top -
                       bottomLogo.width;
        bottomOrder.x = ps.paperSize.width - ps.paperMargins.right - 26;
        bottomOrder.y = ps.paperMargins.bottom + 20;
        bottomDate.x = ps.paperSize.width - ps.paperMargins.right - 13;
        bottomDate.y = ps.paperMargins.bottom + 20;
        bottomPage.x = ps.paperSize.width - ps.paperMargins.right;
        bottomPage.y = ps.paperMargins.bottom + 20;

        topLine.x = ps.paperMargins.left + 60;
        topLine.y = ps.paperSize.height - ps.paperMargins.top;
        topLine.width = ps.paperMargins.left + 60;
        topLine.height = ps.paperMargins.bottom;
        topLogo.width = 140;
        topLogo.height = 20;
        topLogo.x = ps.paperMargins.left + 20;
        topLogo.y = ps.paperMargins.bottom;
        topTitle.x = ps.paperMargins.left + 30;
        topTitle.y = ps.paperSize.height;
        topHeader.x = ps.paperMargins.left + 50;
        topHeader.y = ps.paperMargins.bottom;

        bodyLine.x = ps.paperMargins.left + 82;
        bodyLine.y = ps.paperMargins.bottom;
        bodyLine.width = ps.paperMargins.left +82;
        bodyLine.height = ps.paperSize.height - ps.paperMargins.top;
        bodyVLine = ps.paperSize.width - ps.paperMargins.right -
                    ps.paperMargins.left - 126;
        bodyHeader.x = ps.paperMargins.left + 80;
        bodyHeader.y = ps.paperMargins.bottom;
        bodyText.x = ps.paperMargins.left + 100;
        bodyText.y = ps.paperMargins.bottom;

      }
      else
      {
        bottomLine.x = ps.paperMargins.left;
        bottomLine.y = ps.paperMargins.bottom + 40;
        bottomLine.width = ps.paperSize.width - ps.paperMargins.right;
        bottomLine.height = ps.paperMargins.bottom + 40;
        bottomLogo.width = 160;
        bottomLogo.height = 40;
        bottomLogo.x = ps.paperSize.width - ps.paperMargins.right -
                       bottomLogo.width;
        bottomLogo.y = ps.paperMargins.bottom;
        bottomOrder.x = ps.paperMargins.left + 20;
        bottomOrder.y = ps.paperMargins.bottom + 26;
        bottomDate.x = ps.paperMargins.left + 20;
        bottomDate.y = ps.paperMargins.bottom + 13;
        bottomPage.x = ps.paperMargins.left + 20;
        bottomPage.y = ps.paperMargins.bottom;

        topLine.x = ps.paperMargins.left;
        topLine.y = ps.paperSize.height - ps.paperMargins.top - 60;
        topLine.width = ps.paperSize.width - ps.paperMargins.right;
        topLine.height = ps.paperSize.height - ps.paperMargins.top - 60;
        topLogo.width = 140;
        topLogo.height = 20;
        topLogo.x = ps.paperMargins.left;
        topLogo.y = ps.paperSize.height - ps.paperMargins.top - 20;
        topTitle.x = ps.paperSize.width;
        topTitle.y = ps.paperSize.height - ps.paperMargins.top - 30;
        topHeader.x = ps.paperMargins.left;
        topHeader.y = ps.paperSize.height - ps.paperMargins.top - 50;

        bodyLine.x = ps.paperMargins.left;
        bodyLine.y = ps.paperSize.height - ps.paperMargins.top - 82;
        bodyLine.width = ps.paperSize.width - ps.paperMargins.right;
        bodyLine.height = ps.paperSize.height - ps.paperMargins.top - 82;
        bodyVLine = ps.paperSize.height - ps.paperMargins.top -
                    ps.paperMargins.bottom - 126;
        bodyHeader.x = ps.paperMargins.left;
        bodyHeader.y = ps.paperSize.height - ps.paperMargins.top - 80;
        bodyText.x = ps.paperMargins.left;
        bodyText.y = ps.paperSize.height - ps.paperMargins.top - 100;

      } 
    }

  }
   
  /*
   *  Define paper settings (size, margins..)
   *
   *
   */ 
  public class PaperSettings
  {
    public Dimension	paperSize = new Dimension ();
    public Margins	paperMargins = new Margins ();

    public PaperSettings ()
    {
      paperSize.width = 598;
      paperSize.height = 845;

      paperMargins.top = 50;
      paperMargins.bottom = 50;
      paperMargins.left = 50;
      paperMargins.right = 50;

    }

  }

  /*
   *  Class that define margins fields
   *
   *
   */
  public class Margins
  {
    public int top;
    public int bottom;
    public int right;
    public int left;
   
    public Margins ()
    {
      top = 0;
      bottom = 0;
      right = 0;
      left = 0;
    }
  }


}

