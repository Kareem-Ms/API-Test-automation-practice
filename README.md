# API-Test-automation-practice
This repository contains two projects, each with its own separate folder of tests, on which I have been practicing API test automation: PHPTravels and ContactList.
# The main frameworks used in the project
- RestAssured
- TestNG
- Selenium webDriver (used in only one test case in PHPTravel for email verification)
# Test Cases included 
Under the main folder of the project, you can find two files containing test cases: "ContactListTestCases.txt" and "PHPTravelTestCases.txt". These files provide a description of the implemented test cases.
# Project design 
- Page Object Model design pattern.
- Implement the ApiActions class, which is located in the "Utiles/ApiActions.java" file. This class is used for interacting with APIs and performing different requests on them.
- Implement the JsonFileManager class, located in the "Utiles/JsonFileManager.java" file. This class is used for reading data from JSON files that contain test data.
- Preparing test data during execution
