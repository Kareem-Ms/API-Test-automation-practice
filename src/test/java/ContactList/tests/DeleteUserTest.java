package ContactList.tests;

import ContactList.PojoClasses.User;
import ContactList.apis.AddUserApi;
import ContactList.apis.DeleteUserApi;
import ContactList.apis.LoginApii;
import Utiles.JsonFileManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DeleteUserTest {

    // Variables Section
    AddUserApi addUserApi;
    DeleteUserApi deleteUserApi;
    LoginApii loginApi;
    JsonFileManager testData;
    String email;
    String password;
    User user;
    String currentTime = new SimpleDateFormat("ddMMyyyyHHmmssSSS").format(new Date());

    // Tests Section
    @Test
    public void VerifyAddingUserWithUnregisteredEmail(){
        email = testData.getTestData("UserInfo.email")+"_"+currentTime+testData.getTestData("UserInfo.domain");
        password = testData.getTestData("UserInfo.password");

        user = addUserApi.AddUser(email,testData.getTestData("UserInfo.firstName")
                ,testData.getTestData("UserInfo.lastName")
                ,password,201).as(User.class);

        Assert.assertEquals(user.getUserInfo().getEmail(),email);
        Assert.assertNotNull(user.getToken());
    }

    @Test(dependsOnMethods = "VerifyAddingUserWithUnregisteredEmail")
    public void VerifyDeletingUserSuccessfully(){
        deleteUserApi.deleteUser(user.getToken(),200);

        loginApi.Login(user.getUserInfo().getEmail(), password, 401);
    }

    // Configuration Section
    @BeforeClass
    public void setUp(){
        testData = new JsonFileManager("src/test/resources/TestData/ContactListTestData/AddUserTestData.json");
        addUserApi = new AddUserApi();
        deleteUserApi = new DeleteUserApi();
        loginApi = new LoginApii();
    }
}
