import java.io.*;
import java.lang.*;
import java.util.*;

/**
 * This class defines the data model used to update the database.
 * There are also some useful methods that can extract and convert data.
 * @version 	1.01, 16/11/1998
 * @author 	Aires Marques
 * @author 	Cristiana Areias
 * @see proEpWire
 * @since       NES 1.0
 */

	// Structure of the needed data
	public class EpWireData {
		public int		Id;
		public int		pwId;
		public StringBuffer	pwName;
		public int		F1Id;
		public StringBuffer	F1Name;
		public int		T1Id;
		public StringBuffer	T1Name;
		public int		T2Id;
		public StringBuffer	T2Name;
		public int		F2Id;
		public StringBuffer	F2Name;
		public int		bundleId;
		public StringBuffer	Bundle;
		public int		lwId;
		public StringBuffer	lwName;
		public StringBuffer	Change_Date;
		public float		Man_Lenght;
		public StringBuffer	Remark;
					
		//
		//
		//
		public EpWireData(String Line) {		
			Line2wireData(Line);
		}
		
		
		// Description: This is for testing proposes only... 
		//
		//
		public EpWireData(Vector Vec)
		{
	 		Id = new Integer (Vec.elementAt(0).toString()).intValue();
			pwId = new Integer(Vec.elementAt(1).toString()).intValue();
			pwName = new StringBuffer (Vec.elementAt(2).toString().trim());
			F1Id = new Integer(Vec.elementAt(3).toString()).intValue();
			F1Name = new StringBuffer (Vec.elementAt(4).toString().trim());
			T1Id = new Integer(Vec.elementAt(5).toString()).intValue();
			T1Name = new StringBuffer (Vec.elementAt(6).toString().trim());
			T2Id = new Integer(Vec.elementAt(7).toString()).intValue();
			T2Name = new StringBuffer (Vec.elementAt(8).toString().trim());
			F2Id = new Integer(Vec.elementAt(9).toString()).intValue();
			F2Name = new StringBuffer (Vec.elementAt(10).toString().trim());
			bundleId = new Integer(Vec.elementAt(11).toString()).intValue();
			Bundle = new StringBuffer (Vec.elementAt(12).toString().trim());
			lwId = new Integer(Vec.elementAt(13).toString()).intValue();
			lwName = new StringBuffer (Vec.elementAt(14).toString().trim());
			if (Vec.elementAt(15)!=null) Change_Date = new StringBuffer (Vec.elementAt(15).toString().trim());
			Man_Lenght = new Float (Vec.elementAt(16).toString()).floatValue();
			if (Vec.elementAt(17)!=null) Remark = new StringBuffer (Vec.elementAt(17).toString().trim());
				
		}



		public EpWireData()
		{
		}

		
		
		//
		//
		//
		public void  Line2wireData(String Line)
		{
		  String element = new String("                              ");
		  String simbol = new String("  ");		  
		  int DelimeterIndexEnd      = Line.indexOf(simbol);
		
		  for (int i=0; i<7;i++)
		     {
   
			    element = Line.substring(0,DelimeterIndexEnd);
			    switch (i) {
  				    case 0: pwName 	= new StringBuffer(element);
				    	    break;
				    case 1: F1Name	= new StringBuffer(element.replace('_',' '));
				    	    break;
				    case 2: T1Name	= new StringBuffer(element);
					    break;
				    case 3: F2Name	= new StringBuffer(element.replace('_',' '));
				    	    break;
				    case 4: T2Name	= new StringBuffer(element);
				    	    break;
				   //case 5: Its a field that does not interest  				  
				    case 6: Man_Lenght =  new Float(element).floatValue();				    	    
				    	    break;
			    } // switch			    
			    if (i!=6) {
			        Line = Line.substring(element.length()+2,Line.length());
			    	DelimeterIndexEnd = Line.indexOf(simbol);
			    	if (DelimeterIndexEnd==-1) DelimeterIndexEnd = Line.length();
			    }  			    
		     }
		     lwName = pwName;
		 } // public void Line2wireData
						
		
		  //  Description: Display the contents of the Variables related to the wire.
		 //  This method can be used in the development fase for controling, however
		//  it is useless in the finished and operational version
		public void displayWireData() {
			String output_stuff = new String(
					Id		+ " " +
					pwId		+ " " +
					pwName		+ " " +
					F1Id		+ " " +
					F1Name		+ " " +
					T1Id		+ " " +
					T1Name		+ " " +
					T2Id		+ " " +
					T2Name		+ " " +
					F2Id		+ " " +
					F2Name		+ " " +
					bundleId	+ " " +
					Bundle		+ " " +
					lwId		+ " " +
					lwName		+ " " +
					Change_Date	+ " " +
					Man_Lenght	+ " " +
					Remark
					);										
			System.out.println(output_stuff);
		}



		  // Description: This method it is to be used when the data is provided by the database
		 // Pre: All fields are set
		// Pos: A vector with all fields
		private Vector Data2Vector_all(){
			Vector DataVec = new Vector();			
			
			DataVec.addElement(new Integer(Id));
			DataVec.addElement(new Integer(pwId));
			DataVec.addElement(pwName);
			DataVec.addElement(new Integer(F1Id));
			DataVec.addElement(F1Name);
			DataVec.addElement(new Integer(T1Id));
			DataVec.addElement(T1Name);
			DataVec.addElement(new Integer(T2Id));
			DataVec.addElement(T2Name);
			DataVec.addElement(new Integer(F2Id));
			DataVec.addElement(F2Name);
			DataVec.addElement(new Integer(bundleId));
			DataVec.addElement(Bundle);
			DataVec.addElement(new Integer(lwId));
			DataVec.addElement(lwName);
			DataVec.addElement(Change_Date);
			DataVec.addElement(new Float(Man_Lenght));
			DataVec.addElement(Remark);
			
			return DataVec;
		}

		  // Description: This is to be used when the origin of the vector is the data file
		 // Pre: pwName, F1Name, T1Name, F2Name, T2Name and Man_Lenght have values 
		// Pos: returns a vector with the above data fields
		private Vector Data2Vector_restricted(){
			Vector DataVec = new Vector();
			Integer nullInt = new Integer(0);
			
			DataVec.addElement(nullInt);
			DataVec.addElement(nullInt);
			DataVec.addElement(pwName);			//
			DataVec.addElement(nullInt);
			DataVec.addElement(F1Name);
			DataVec.addElement(nullInt);
			DataVec.addElement(T1Name);
			DataVec.addElement(nullInt);
			DataVec.addElement(T2Name);
			DataVec.addElement(nullInt);
			DataVec.addElement(F2Name);
			DataVec.addElement(nullInt);
			DataVec.addElement(null);
			DataVec.addElement(nullInt);
			DataVec.addElement(lwName);
			DataVec.addElement(null);
			DataVec.addElement(new Float(Man_Lenght));
			DataVec.addElement(null);			

			return DataVec;
		}
						
			
		public Vector Data2Vector() {
			Vector DataVec = new Vector();			
			if (Id!=0) return Data2Vector_all();
			 else return Data2Vector_restricted();
		} // Vector Data2Vector()		
		
	} // public class EpWireData
