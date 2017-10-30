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

##Test Case Introduction
    1. QQMusic Test Steps:
        1) Start QQMusic.
        2) Find "skip" button and click to skip splash window.
        3) Find "more" button and click "exit" button to exit.
    2. WeChat Test Steps:
        1) Start WeChat.
        2) Find and click "login" button.
        3) Find "phonenumber" EditText and input 18600000000.
        4) Exit app.