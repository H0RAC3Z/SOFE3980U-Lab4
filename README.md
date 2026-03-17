Run the following command in the desired directory(MCC, SVCR, or SVBR)
```
mvn clean package assembly:single
```
Run SVCR
```
java -jar target/SVCR-1.0.0-jar-with-dependencies.jar
```
Run SVBR
```
java -jar target/SVBR-1.0.0-jar-with-dependencies.jar
```
Run MCC
```
java -jar target/MCC-1.0.0-jar-with-dependencies.jar
```