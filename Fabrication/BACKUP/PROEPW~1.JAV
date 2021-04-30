/*

 (c) Aires Marques, 1998
 
*/


import java.io.*;
import java.lang.*;
import java.util.*;

import DAutil.dbase.DBAcc;  // DBAcc.java     -  /entwicklung/java/app/util/
import DAutil.utilities;    // utilities.java -  /entwicklung/java/app/util/

import EpWireData;

  //
 //
//

public class proEpWire {

	public utilities	_utils = new utilities();
	public DBAcc		db     = new DBAcc (_utils);
	
	private final int ID_NOT_FOUND=0;
		
	private final int WIRE_INSERT=1;
	private final int WIRE_UPDATE=2;
	private final int WIRE_DELETE=3;	
	
	// Needed ID's
	private int 		effSRId;
	private int 		effId;
	private int 		bundleId;
	
	// More needed stuff (this comes from the file)
	private String 		d_Effectivity;
	private String 		d_Version;		// Probably I wont use It
	private String 		d_PartName;		// This is the Bundle Name
	private String 		d_SubRoute;
	private int 		d_project;

	String filename;				// filename = PATH + d_PartName + ".txt"


	

/*   #####                  #######         #     #
    #     #  #####    ####  #        #####  #  #  #   #   #####   ######
    #     #  #    #  #    # #        #    # #  #  #   #   #    #  #
    ######   #    #  #    # #####    #    # #  #  #   #   #    #  #####
    #        #####   #    # #        #####  #  #  #   #   #####   #
    #        #   #   #    # #        #      #  #  #   #   #   #   #
    #        #    #   ####  #######  #       ## ##    #   #    #  ######
    
    DESCRIPTION: Constructor for the class
    IN:		 Effectivity, Version, PartName, SubRoute, Project
    		 These data come from a small file and are passed as parameters.
*/

	public proEpWire(String f_Effectivity,
			 String f_Version,		// Probably I wont use it
			 String f_PartName,		// This is the Bundle Name
			 String f_SubRoute,
			 int f_project)			// This is the Cad Model
	
	{
		Vector DataFile = new Vector();
		Vector DataDb	= new Vector();
				
		filename     = new String("/prdb/electric/cabling/" + f_PartName);		
				
		//System.out.println("The filename is: " + filename);
		
		StringBuffer Line = new StringBuffer();
		EpWireData wireData = new EpWireData();

		// Initialize the data that came from parameters
		d_Effectivity	= new String(f_Effectivity);
		d_Version	= new String(f_Version);	// I dont use it but it's nice to have it set
		d_PartName	= new String(f_PartName);	// This is the Bundle Name
		d_SubRoute	= new String(f_SubRoute);
		d_project	= f_project;			// This is the Cad Model
				
		try {
			FileInputStream inputFile   = new FileInputStream (filename + ".txt");
			BufferedReader  inputStream = new BufferedReader (new InputStreamReader(inputFile));		
			
			while(true)				// $IMPORTANT Main while cycle
			{
			 utilities.Str2StrBuffer(inputStream.readLine(), Line);
			 if (Line.length()==0) break;

			 wireData.Line2wireData(Line.toString());
			 DataFile.addElement(wireData.Data2Vector());
			 //wireData.displayWireData();		// $DISPLAY
			}
			inputStream.close();
			inputFile.close();			
		} catch (java.io.IOException ex) {
			System.out.println("ERROR: " + ex.toString() );				
		} // catch IOException
		
		if (CheckData()!=1) return;
		
		getData();
		
		DataDb=db2Vector();
		Compare(DataFile,DataDb);


		System.out.println("proEpWire finished.");
		
		return;
	} // public proEpWire






/*   #####     ##     #####    ##    #####     ##     ####   ######
     #    #   #  #      #     #  #   #    #   #  #   #       #
     #    #  #    #     #    #    #  #####   #    #   ####   #####
     #    #  ######     #    ######  #    #  ######       #  #
     #    #  #    #     #    #    #  #    #  #    #  #    #  #
     #####   #    #     #    #    #  #####   #    #   ####   ######
    
    DESCRIPTION: Above are the methods related to the Oracle DataBase
*/



	   // Descriptiom: Checks the SR Table, checks de CADModel table, checks the effectSubRoute table
	  // and sets the effSRId.
	 // Pre: SubRoute and filename are set
	// Post: SubRoute.Id & CadModel.Id & EffecSubRoute.Id are Set or Get
	private int CheckData()
	{	 
	 int SRId;		// SubRoute ID
	 int CMId;		// CadModel ID
	 
	 // Check the SubRoute table
	 SRId=db.table.FindId("SELECT Id FROM SubRoute " +
	 	     "WHERE Name ='" + d_SubRoute + "'");
		          
	 if (SRId==ID_NOT_FOUND) {
	 	//System.out.println("proEpWire: SRId not found... Inserting a new SRId");
	 	SRId=db.table.NextId("SubRouteID");
		db.table.Sql_Operation("INSERT INTO SubRoute VALUES (" + SRId + ",'" + d_SubRoute + "')");
	 } // if SRId not found	 
	 
	 //System.out.println("proEpWire: SRId get or set to " + SRId);
	 
	 
	 // Check the CADModel table	 
	 CMId=db.table.FindId("SELECT Id FROM CadModel " +
	 	     "WHERE Filename='" + filename + "'");
	 
	 if (CMId==ID_NOT_FOUND) {
	 	CMId=db.table.NextId("CadModelID");
		//System.out.println("proEpWire: CadModelID not found... Inserting a new CadModelID");
		db.table.Sql_Operation( "INSERT INTO CadModel VALUES " 	+
					"("  + CMId +
					","  + "''" +
					",'" + filename + "')");
	 } // if CMId not found



	 //String Project = new String(d_project);
	 Integer ProjIntObj = new Integer (d_project);
	 
		
	 effectivity eff = new effectivity(d_Effectivity,filename,d_SubRoute, d_PartName, ProjIntObj.toString());		
	 effId=0;
	 
	 if((effId=eff.ExistsEffectivity(db))==-1)
	  effId=eff.CreateEffectivity(db);
	  
	 if(effId==-1) return 0;
	 
	 //System.out.println("The effId is:" + effId);	// $DELETE


	 // Check the effecSubRoute table
	 effSRId=db.table.FindId("SELECT Id FROM effecSubRoute WHERE mId=" + CMId 	+ 
	 		" AND srID=" + 	SRId 						+ 
			" AND effID=" + effId);

	 if (effSRId==ID_NOT_FOUND) {
	 	effSRId = db.table.NextId("effecSubRouteID");		
		//System.out.println("proEpWire: effecSubRouteID not found... new effecSubRouteID inserted :" + effSRId);
		db.table.Sql_Operation( "INSERT INTO effecSubRoute VALUES "  + 
					  "(" + effSRId + 
					  ", " + CMId  + 
					  ", " + SRId  +
					  ", " + effId + 
					  ",'')");
	 }
	 bundleId = prepareBundle(d_PartName);
	 return 1;
	}
	
	
	
	  //
	 //
	//
	private int PrepareWire(EpWireData data)
	{	
	 int pwId = db.table.FindId("SELECT Id FROM pWire WHERE Name='" + data.pwName + "'");	 
	 
	 if (pwId==ID_NOT_FOUND) {
	 	pwId = db.table.NextId("PWireID");
		//System.out.println("proEpWire: PWireID not found... new PWireID inserted :" + pwId);
		db.table.Sql_Operation( "INSERT INTO pWire VALUES "	+
					"("  + pwId			+
					",'" + data.pwName + "'" + ")" );
	 }
	 return pwId;
	} // void PrepareWire(EpWireData data)


	  //
	 //
	//
	private int PrepareLWire(EpWireData data)
	{
	 int lwId = db.table.FindId(	"SELECT elw.Id " 								+
	 				" FROM efflWire elw, lwire lw, Collection c1, Collection c2 " 		  	+
					"WHERE lw.Name ='"+  data.lwName  +"' AND lw.id=elw.lwid AND c1.id=elw.effid" 	+
					" AND c1.Version=c2.Version AND c2.Lower>=c1.Lower"			  	+
					" AND c2.Upper<=c1.Upper AND c2.Id="+ effId);
	
	 if (lwId == ID_NOT_FOUND) 
	 	System.out.println("ERROR: There's no Logical Wire in database for " +
				   "'" + data.pwName + "' physical wire");
	 return lwId;
	} // void PrepareLWire(EpWireData data)
	


	  // Description: Gets the BundleId or sets a new one if it dont exists
	 // In: Bundle.Name
	// Out: Bundle.Id
	private int prepareBundle(String BName)
	{
	 int _BundleId;	 
	 _BundleId=db.table.FindId("SELECT Id FROM Bundle WHERE Name='" + BName + "'");
	 
	 if (_BundleId==ID_NOT_FOUND) {
	 	_BundleId=db.table.NextId("BundleID");
		
		db.table.Sql_Operation( "INSERT INTO Bundle VALUES " 	+
					"("  + _BundleId 		+
					",'" + BName + "')");
	 }
	 //System.out.println("BundleId get or set to: " + _BundleId);
	 return _BundleId;
	} // private void prepareBundle(String BName)
	

	
	private int PrepareFin(String Name)
	{
	 int ID;
	 ID=db.table.FindId("SELECT id FROM Fin WHERE Name='" + Name + "'");
	 
	 if (ID == ID_NOT_FOUND) {
	 	ID = db.table.NextId("FinID");
		
		db.table.Sql_Operation( "INSERT INTO Fin VALUES " 	+
					"("  + ID 			+ 
					",'" + Name + "'"		+
					","  + "''"			+
					","  + "''"			+
					","  + "'A'"			+
					",NULL)"			);
	 }	 
	 return ID;	 
	}



	  // Description:
	 //
	//		
	private int PreparePin(int finId, String Name)
	{
	 int ID; 
	 
	 ID=db.table.FindId("SELECT Id FROM lTerminal WHERE Name='" + Name + "' AND fId=" + finId);
	 
	 if (ID == ID_NOT_FOUND) {
	 	//System.out.println("ID not found");
	 	ID = db.table.NextId("lTerminalId");
		
		db.table.Sql_Operation(	"INSERT INTO lTerminal VALUES "	+
					"("  + ID			+
					",'" + Name + "'"		+
					","  + finId + ")");
	 }
	 //System.out.println("ID get or set to: " + ID);
	 return ID;
	}
	
	

	  // Description: Updates an existing wire
	 // In: 2 Vectors. OldVec is provided by the database and NewVec is provided by the file.
	// Action: The database is updated
	public void update(Vector OldVec, Vector NewVec)
	{

	 EpWireData updateData = new EpWireData();

		       //new Integer(VdbaseVec.elementAt(0).toString()).intValue()
	 updateData.Id = new Integer(   OldVec.elementAt(0).toString()).intValue();
	 
	 updateData.pwName = new StringBuffer(NewVec.elementAt(2).toString());
	 updateData.F1Name = new StringBuffer(NewVec.elementAt(4).toString());
	 updateData.F2Name = new StringBuffer(NewVec.elementAt(10).toString());
	 updateData.T1Name = new StringBuffer(NewVec.elementAt(6).toString());
	 updateData.T2Name = new StringBuffer(NewVec.elementAt(8).toString());
	 updateData.lwName = new StringBuffer(NewVec.elementAt(14).toString());	 
	 
	 updateData.pwId = PrepareWire(updateData);
	 updateData.lwId = PrepareLWire(updateData);
	 if (updateData.lwId==-1) return;	//It is not possible to map with logical	 
	 updateData.F1Id = PrepareFin(updateData.F1Name.toString());
	 updateData.F2Id = PrepareFin(updateData.F2Name.toString());
	 updateData.T1Id = PreparePin(updateData.F1Id, updateData.T1Name.toString());	 	 
	 updateData.T2Id = PreparePin(updateData.F2Id, updateData.T2Name.toString());
	 updateData.Bundle = new StringBuffer(d_PartName);
	 updateData.bundleId = bundleId;	 	 

	 if (OldVec.elementAt(15)!=null) utilities.Str2StrBuffer(OldVec.elementAt(15).toString(),updateData.Change_Date);
	  else updateData.Change_Date = new StringBuffer();
	 updateData.Man_Lenght = new Float(NewVec.elementAt(16).toString()).floatValue();
	 if (OldVec.elementAt(17)!=null) updateData.Remark = new StringBuffer(OldVec.elementAt(17).toString());
	  else updateData.Remark = new StringBuffer();
	 
	 db.table.Sql_Operation( "UPDATE effpWire SET RightltID=" + updateData.T2Id + 
	 			 ", LeftltID=" + updateData.T1Id +
				 ", bID=" + updateData.bundleId +
				 ", efflwID=" + updateData.lwId +
				 ", chDate='" + updateData.Change_Date + "'" +
				 ", manufacturerLength=" + updateData.Man_Lenght +
				 ", Remark='" + updateData.Remark + "' " +
				 "WHERE ID=" + updateData.Id);
				 
	}


	 //
	//
	public void update(int Action, Vector in)
	{
	 int ID;
	 
	 EpWireData In = new EpWireData();	 	 		 
	 
	 In.pwName = new StringBuffer(in.elementAt(2).toString());	 	 
	 In.F1Name = new StringBuffer(in.elementAt(4).toString());
	 In.F2Name = new StringBuffer(in.elementAt(10).toString());
	 In.T1Name = new StringBuffer(in.elementAt(6).toString());
	 In.T2Name = new StringBuffer(in.elementAt(8).toString());
	 In.lwName = new StringBuffer(in.elementAt(14).toString());	 
	 In.pwId = PrepareWire(In);
	 if (In.lwId==-1) return;	//It is not possible to map with logical	 
	 In.lwId = PrepareLWire(In);
	 In.F1Id = PrepareFin(In.F1Name.toString());
	 In.F2Id = PrepareFin(In.F2Name.toString());
	 In.T1Id = PreparePin(In.F1Id, In.T1Name.toString());	 	 
	 In.T2Id = PreparePin(In.F2Id, In.T2Name.toString());
	 In.Bundle = new StringBuffer(d_PartName);
	 In.bundleId = bundleId;
	 
	 
	 if (in.elementAt(15)!=null) utilities.Str2StrBuffer(in.elementAt(15).toString(),In.Change_Date);


	 In.Man_Lenght = new Float(in.elementAt(16).toString()).floatValue();			
	 if (in.elementAt(17)!=null) In.Remark = new StringBuffer(in.elementAt(17).toString());	 
	 
	 
	 switch (Action) {
	 	case WIRE_INSERT: 	  // {{ case WIRE_INSERT			
			ID = db.table.NextId("effpWireID");
			
			if (In.Change_Date==null) In.Change_Date = new StringBuffer("");
			if (In.Remark==null) In.Remark= new StringBuffer("NA");
			
			db.table.Sql_Operation( "INSERT INTO effpWire VALUES"	+
						"(" + ID			+
						"," + In.bundleId		+
						"," + In.pwId			+
						"," + effId			+
						",'"+ In.Change_Date + "'"	+
						"," + In.Man_Lenght		+
						"," + "''"			+
						",'"+ In.Remark + "'"		+
						"," + In.T1Id			+
						"," + In.T2Id			+
						"," + In.lwId + ")");
												
			db.table.Sql_Operation( "INSERT INTO SRpWire VALUES" 	+
						"(" + effSRId + 
						"," + ID + ")");					
			break;		 // }} case WIRE_INSERT

			
		case WIRE_DELETE: 	 // {{ case WIRE_DELETE
			if (in.elementAt(0)!=null) In.Id = new Integer(in.elementAt(0).toString()).intValue();
		
			db.table.Sql_Operation("DELETE FROM effpWire WHERE ID=" + In.Id);
			db.table.Sql_Operation("DELETE FROM SRpWire WHERE SRID=" + effSRId + " AND epwID=" + In.Id);
			break;		 // }} case WIRE_DELETE
	 
	 }
	} // public void update()
	



	   // Description: "Grabs" the data from the table
	  // Pre:
	 // Pos: We have a vector with that contains the given table
	// Return: The vector with the table told in the line above
	public Vector db2Vector()
	{
	 int nRows = db.table.getRowCount();                                                                                                  
	 Vector DataContainer = new Vector();	 
	 EpWireData auxData = new EpWireData();	 	 
	 
	 //System.out.println("-- No of Lines retrieved from Database: " + nRows + " --");

	 for (int i=0; i < nRows; i++){

	 	auxData.Id = new Integer (db.table.getValueAt (i,0).toString()).intValue();		
		auxData.pwId = new Integer(db.table.getValueAt (i,1).toString()).intValue();
		auxData.pwName = new StringBuffer (db.table.getValueAt (i,2).toString().trim());
		auxData.F1Id = new Integer(db.table.getValueAt(i,3).toString()).intValue();
		auxData.F1Name = new StringBuffer (db.table.getValueAt (i,4).toString().trim());
		auxData.T1Id = new Integer(db.table.getValueAt (i,5).toString()).intValue();
		auxData.T1Name = new StringBuffer (db.table.getValueAt (i,6).toString().trim());
		auxData.T2Id = new Integer(db.table.getValueAt (i,7).toString()).intValue();
		auxData.T2Name = new StringBuffer (db.table.getValueAt (i,8).toString().trim());
		auxData.F2Id = new Integer(db.table.getValueAt (i,9).toString()).intValue();
		auxData.F2Name = new StringBuffer (db.table.getValueAt (i,10).toString().trim());
		auxData.bundleId = new Integer(db.table.getValueAt (i,11).toString()).intValue();
		auxData.Bundle = new StringBuffer (db.table.getValueAt (i,12).toString().trim());
		auxData.lwId = new Integer(db.table.getValueAt (i,13).toString()).intValue();
		auxData.lwName = new StringBuffer (db.table.getValueAt (i,14).toString().trim());
		if (db.table.getValueAt(i,15)!=null) auxData.Change_Date = new StringBuffer (db.table.getValueAt (i,15).toString().trim());
		auxData.Man_Lenght = new Float (db.table.getValueAt (i,16).toString()).floatValue();
		if (db.table.getValueAt(i,17)!=null) auxData.Remark = new StringBuffer (db.table.getValueAt (i,17).toString().trim());

		DataContainer.addElement(auxData.Data2Vector());
		//System.out.println("THE AUXDATA.ID IS " + auxData.Id);			// $DISPLAY
		//auxData.displayWireData();							// $DISPLAY
	 } // for
	 return DataContainer;
	}


	//
	//
	//
	public void getData () {		
		// effSRId, effId, bundleId; ---> Global variables
		
		db.table.select( "SELECT DISTINCT epw.ID, pw.ID, pw.Name, F1.ID, F1.Name, T1.ID, T1.Name," 		+
				 " T2.ID, T2.Name, F2.ID, F2.Name, B.ID, B.Name, elw.ID, lw.Name," 		+
				 " epw.chDate, epw.manufacturerLength, epw.Remark " 				+
			 	 "FROM effpWire epw, pWire pw, Bundle B, Fin F1, lTerminal T1," 		+
				 " lTerminal T2, Fin F2, efflWire elw, lWire lw, effectivity E,"		+
				 " effecSubRoute eSR, SRpWire srpw "						+
				 "WHERE eSR.ID='" + effSRId + "' AND eSR.ID=srpw.srID AND srpw.EpwID=epw.ID"	+
				 " AND epw.effID='" + effId + "' AND epw.pwID=pw.ID AND epw.bID=B.ID"		+
				 " AND epw.leftltID=T1.ID AND T1.fID=F1.ID AND epw.rightltID=T2.ID"		+
				 " AND T2.fID=F2.ID AND epw.efflwID=elw.ID AND elw.lwID=lw.ID"			+
				 " AND B.ID='" + bundleId + "'"							);
	} // public void getData ()
	
	
	
	
	  // Description: Compares 2 Vectors and updates the database
	 // In: 2 Vectors
	// Out: void
	public void Compare(Vector Vfile, Vector Vdbase)
	{
	  Vector 	FileVec,VdbaseVec;
	  int 		i,j;				// Counters
	  boolean 	FOUND=false;
	  

	  //System.out.println("It seems that I have " + Vdbase.size() +"/" + Vfile.size() + " elements to work on...");

	  for (i=0; i < Vfile.size();i++) 
	  {
	     FileVec = (Vector)Vfile.elementAt(i);
	     FOUND=false;
	     for (j=0; j < Vdbase.size(); j++)
	     {
	       VdbaseVec = (Vector)Vdbase.elementAt(j);
	       //System.out.println("FOUND State set to false");
	       
	       /*System.out.println("Nice Wires, let me see if they have the same values...");	       	       	       
	       System.out.println("->"+ FileVec.elementAt(2).toString() + "|" + FileVec.elementAt(4).toString() + "|" +
	       				FileVec.elementAt(6).toString() + "|" + FileVec.elementAt(10).toString() + "|" +
					FileVec.elementAt(8).toString() + "|" + FileVec.elementAt(14).toString());	       
	       System.out.println("->"+ VdbaseVec.elementAt(2).toString() + "|" + VdbaseVec.elementAt(4).toString() + "|" + 
	       				VdbaseVec.elementAt(6).toString() + "|" + VdbaseVec.elementAt(10).toString() + "|" + 
					VdbaseVec.elementAt(8).toString() + "|" + VdbaseVec.elementAt(14).toString());*/

	       //System.out.println("... and let me check the ID to: " + new Integer(VdbaseVec.elementAt(0).toString()).intValue());
	       
	       
		if( FileVec.elementAt(2).toString().compareTo(VdbaseVec.elementAt(2).toString())==0	&&
		    FileVec.elementAt(4).toString().compareTo(VdbaseVec.elementAt(4).toString())==0	&&
		    FileVec.elementAt(6).toString().compareTo(VdbaseVec.elementAt(6).toString())==0	&&
		    FileVec.elementAt(10).toString().compareTo(VdbaseVec.elementAt(10).toString())==0	&&
		    FileVec.elementAt(8).toString().compareTo(VdbaseVec.elementAt(8).toString())==0	&&
		    FileVec.elementAt(14).toString().compareTo(VdbaseVec.elementAt(14).toString())==0)
		      {	 
		        //System.out.println("FOUND State set to true");
		   	FOUND = true;
			//System.out.println("Cool...I found the Wire, let me take a look at it");
			
			//System.out.println("File MLenght: " + FileVec.elementAt(16).toString());
			//System.out.println("Db   MLenght: " + VdbaseVec.elementAt(16).toString());
			
			if (FileVec.elementAt(16).toString().compareTo(VdbaseVec.elementAt(16).toString())!=0)
			  {
				//System.out.println("Hmmmm... something is different, it is better to update");
				update(VdbaseVec, FileVec);
				Vdbase.removeElementAt(j);
				j--;
			  }
			 else { 
			 	//System.out.println("...and I shall leave the wire as it is");
				Vdbase.removeElementAt(j);
				j--;
			 }
			 break;
				
		      } 		
		   

	    } // for j	    

	    if(!FOUND) {			// Insert a non-existing wire (from the file to the database)
	    	    //System.out.println("-- Hmmmm... a new wire is here, it is better to insert");		    
		    update(WIRE_INSERT,FileVec);
	    }	       	    
	    
	  } // for i


	  //System.out.println("It seems that I have " + Vdbase.size() + " wires to delete...");	  
	  
	  // Now deletes from the database the remaining elements
	    for (j=0; j < Vdbase.size(); j++) {
	    	    VdbaseVec = (Vector)Vdbase.elementAt(j);
	    	    /*System.out.println( VdbaseVec.elementAt(2).toString() + "|" + VdbaseVec.elementAt(4).toString() + "|" + 
					VdbaseVec.elementAt(6).toString() + "|" + VdbaseVec.elementAt(10).toString() + "|" + 
					VdbaseVec.elementAt(8).toString() + "|" + VdbaseVec.elementAt(14).toString());*/
	    	
	    	    //System.out.println("-- Hmmmm... this wire is not used anymore, perhaps it's better to delete it");
	  	    update(WIRE_DELETE, (Vector)Vdbase.elementAt(j));
	    }		    
	  
	  Vfile.removeAllElements();
	  Vdbase.removeAllElements();
	}
	

	

/*  #    #    ##      #    #    #
    ##  ##   #  #     #    ##   #
    # ## #  #    #    #    # #  #
    #    #  ######    #    #  # #
    #    #  #    #    #    #   ##
    #    #  #    #    #    #    #  */


	static public void main(String[] args)
	{
	
	  proEpWire xxx=new proEpWire("SQ01-1-10","001","5555VB","1m-em",80);
	  System.exit(0);
	}
	
	
}
