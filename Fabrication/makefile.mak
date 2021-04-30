all: Fabrication proEpWire EpWireData updfab pWireInfo

Fabrication: Fabrication.java
	javac -nowarn -classpath $(CLASSPATH):${PROJECT}/java/lib:${PROJECT}/java/lib/resources Fabrication.java

EpWireData: EpWireData.java
	javac -nowarn -classpath $(CLASSPATH):${PROJECT}/java/lib/:${PROJECT}/java/lib/resources EpWireData.java

proEpWire: proEpWire.java 
	javac -nowarn -classpath $(CLASSPATH):${PROJECT}/java/lib:${PROJECT}/java/lib/resources:${PROJECT}/java/app/definition proEpWire.java

updfab: updfab.java
	javac -nowarn -classpath $(CLASSPATH):${PROJECT}/java/lib:${PROJECT}/java/lib/resources updfab.java

pWireInfo: pWireInfo.java
	javac -nowarn -classpath $(CLASSPATH):${PROJECT}/java/lib:${PROJECT}/java/lib/resources pWireInfo.java


jar: Fabrication proEpWire EpWireData updfab pWireInfo
	jar cf FabModul.jar *.class	
	rm *.class	

clean:	
	rm *.class


backup: 
	MONTH=`date | cut -c5-7`
	DAY=`date | cut -c9-10`
	TIME=`date | cut -c12-19`

	cp *.java backup

