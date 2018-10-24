Smartphone-Performance-Test-Case run with appium.
---
In order to run the tests, you will need to install [Apache Maven](http://maven.apache.org),
and Appium (according to the Appium [installation instructions](https://github.com/appium/appium).


##Import Project
The project is setup using maven.
To use with NetBeans, go to File then Open Project and select the folder.
To use with Eclipse, go to File then Import and select Existing Maven Projects
To use with IntelliJ, go to File then Open and select the pom.xml
To use with the command line, mvn clean test will run all the tests.


##Run tests
You will then need to start appium, eg:

    grunt appium

To compile and run all tests, run:

    mvn clean test

To run a single test, run:

    mvn -Dtest=com.unicom.apptest.QQMusicTest test

##TestNG start command:
java -classpath "./target/test-classes" -Djava.ext.dirs=lib org.testng.TestNG -suitethreadpoolsize 1 testng.xml

