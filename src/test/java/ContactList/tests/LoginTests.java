package ContactList.tests;

import ContactList.PojoClasses.User;
import ContactList.apis.AddUserApi;
import ContactList.apis.LoginApii;
import Utiles.JsonFileManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginTests {

    ///////////////Variables\\\\\\\\\\\\\\\\\
    AddUserApi addUserApi;
    LoginApii loginApi;
    JsonFileManager testData;
    String email;
    String password;
    User user;
    String currentTime = new SimpleDateFormat("ddMMyyyyHHmmssSSS").format(new Date());

    ///////////////Tests\\\\\\\\\\\\\\\\\\\\\\
    @Test
    public void VerifyAddingUserWithUnregisteredEmail(){
        email = testData.getTestData("UserInfo.email")+"_"+currentTime+testData.getTestData("UserInfo.domain");
        password = testData.getTestData("UserInfo.password");

        user = addUserApi.AddUser(email,testData.getTestData("UserInfo.firstName")
                ,testData.getTestData("UserInfo.lastName")
                ,password,201).as(User.class);

        Assert.assertEquals(user.getUser().getEmail(),email);
        Assert.assertNotNull(user.getToken());
    }

    @Test(dependsOnMethods = "VerifyAddingUserWithUnregisteredEmail")
    public void VerifyLoginWithCorrectEmailAndPw(){
       User LoggedInUser = loginApi.Login(email, password, 200).as(User.class);

       Assert.assertEquals(LoggedInUser.getUser().getEmail(),email);
       Assert.assertNotNull(LoggedInUser.getToken());

    }

    @Test
    public void VerifyLoginWithInCorrectEmailAndPw(){
        loginApi.Login(testData.getTestData("UserInfo.UnregisteredEmail"), testData.getTestData("UserInfo.password"), 401);
    }


    ///////////////Configuration\\\\\\\\\\\\\\\
    @BeforeClass
    public void setUp(){
        testData = new JsonFileManager("src/test/resources/TestData/ContactListTestData/LoginTestData.json");
        addUserApi = new AddUserApi();
        loginApi = new LoginApii();
    }

}
