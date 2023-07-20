package ContactList.tests;

import ContactList.PojoClasses.User;
import ContactList.apis.AddUserApi;
import ContactList.apis.LoginApii;
import Utiles.JsonFileManager;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

@Epic("ContactList tests")
@Feature("Login tests")
public class LoginTests {

    // Variables Section
    AddUserApi addUserApi;
    LoginApii loginApi;
    JsonFileManager testData;
    String email;
    String password;
    User user;
    String currentTime;

    
    // Tests Section
    @Test(description = "Valid registration with unregistered email and password")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Registering a user using an email that hasn't been registered before")
    public void VerifyAddingUserWithUnregisteredEmail(){
        email = testData.getTestData("UserInfo.email")+"_"+currentTime+testData.getTestData("UserInfo.domain");
        password = testData.getTestData("UserInfo.password");

        user = addUserApi.AddUser(email,testData.getTestData("UserInfo.firstName")
                ,testData.getTestData("UserInfo.lastName")
                ,password,201).as(User.class);

        Assert.assertEquals(user.getUserInfo().getEmail(),email);
        Assert.assertNotNull(user.getToken());
    }

    @Test(dependsOnMethods = "VerifyAddingUserWithUnregisteredEmail",description = "Valid login with correct email and password")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Login to user account using a registered email and password")
    public void VerifyLoginWithCorrectEmailAndPw(){
       User LoggedInUser = loginApi.Login(email, password, 200).as(User.class);

       Assert.assertEquals(LoggedInUser.getUserInfo().getEmail(),email);
       Assert.assertNotNull(LoggedInUser.getToken());

    }

    @Test(description = "Invalid login with incorrect email and password")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Login to user account using a non registered email and password")
    public void VerifyLoginWithInCorrectEmailAndPw(){
        loginApi.Login(testData.getTestData("UserInfo.UnregisteredEmail"), testData.getTestData("UserInfo.password"), 401);
    }


    // Configuration Section
    @BeforeClass
    public void setUp(){
        testData = new JsonFileManager("src/test/resources/TestData/ContactListTestData/LoginTestData.json");
        addUserApi = new AddUserApi();
        loginApi = new LoginApii();
        currentTime = new SimpleDateFormat("ddMMyyyyHHmmssSSS").format(new Date());
    }

}
