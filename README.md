# Govtech Assignment

This project is intended to solve the  assignment and validate the THE OPPENHEIMER PROJECT flow 

## Test Data
Test data for the application is placed in app-data folder, sample data to populate Hero's data in CSV format

## Test Classes
Test classes are placed in test package

## Technology Stack
- Selenium WebDriver 3.141.59
- Java SE 8
- Testng 6.14
- Spring
- Maven
- Extent Report
- Eclipse IDE (you can choose any IDE of your choice)

## How to import the project
- Download the zip.
- Unzip the zip file.
- Open Eclipse
   - File -> Import -> Existing Maven Project -> Navigate to the folder where you unzipped the zip file
   - Select the right project
- You are all Set

## Plugins
- Eclipse Plugin for TestNg
- Eclipse Plugin for Maven

## Run tests from IDE
- Right click the testNg.xml file and `Run As>TestNG Suite`

## Run tests from Command line
- Go to the `Command Prompt`
- Navigate to the project folder where the `pom.xml` is placed
- Run command `mvn -version` to ensure you have Maven installed on your machine. After running this command: 
   - You should see Apache Maven version, Maven home, Java version (if Maven was already installed on your machine) 
   - If you get error like `mvn command is not recognized`, please make sure to install Maven before proceeding
     (You can refer to: https://www.baeldung.com/install-maven-on-windows-linux-mac)
- Execute the command `mvn clean test` to trigger the tests. Few points to note here:
   - Since we just have one `testNg.xml` file, it has been mentioned in the pom.xml file. So, no need to pass the testNg.xml file from command line.
   - To specify the browser on which you want to run the tests, you need to pass browser parameter from the command, for example:
   		- To run tests on Chrome: `mvn clean test -Dbrowser="Chrome"`
   		- To run tests on Firefox: `mvn clean test -Dbrowser="Firefox"`
   - If browser is not specified, then Chrome will be taken as the default browser.

## Test Report
- The test report is created using Extent Reports library.
- To view the test report, navigate to `test-report` folder within your project root directory.
- The Test Report gets updated with each test execution.
- For reference, sample Test Automation report has been committed under the `test-report` folder.
