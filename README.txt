Maven Commands
==============

mvn clean
    clean the code base
    
mvn compile
    compile code
    
mvn war:inplace 
    compile and build an exploded war (in /GDMA/src/main/GDMA.war)
    (see /GDMA/src/main/tomcat/GDMA.xml for deploy file)
    
mvn clean compile war:war
    build the war file        
        
