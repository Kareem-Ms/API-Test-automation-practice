package ContactList.tests;

import ContactList.PojoClasses.User;
import ContactList.apis.AddUserApi;
import ContactList.apis.UpdateUserApi;
import Utiles.JsonFileManager;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;

@Epic("ContactList tests")
@Feature("Update user tests")
public class UpdateUserTests{

    // Variables Section
    AddUserApi addUserApi;
    UpdateUserApi updateUserApi;
    JsonFileManager testData;
    String email;
    String password;
    User user;
    String currentTime;

    // Tests Section
    @Test(description = "Valid registration with unregistered email and password")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Registering a user using an email that hasn't been registered before")
    public void verifyAddingUserWithUnregisteredEmail(){
        email = testData.getTestData("RegisteringUserInfo.email")+"_"+currentTime+testData.getTestData("RegisteringUserInfo.domain");
        password = testData.getTestData("RegisteringUserInfo.password");

        user = addUserApi.AddUser(email,testData.getTestData("RegisteringUserInfo.firstName")
                ,testData.getTestData("RegisteringUserInfo.lastName")
                ,password,201).as(User.class);

        Assert.assertEquals(user.getUserInfo().getEmail(),email);
        Assert.assertNotNull(user.getToken());

    }

    @Test(dependsOnMethods = "verifyAddingUserWithUnregisteredEmail",description = "Valid updating a registered user")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Updating a registered user with valid data using user's token")
    public void verifyUpdatingRegisteredUser(){
        Response response = updateUserApi.updateUser(user.getToken(),email,testData.getTestData("UpdatedUserInfo.firstName")
                                                          ,testData.getTestData("UpdatedUserInfo.lastName")
                                                          ,testData.getTestData("UpdatedUserInfo.password"),200);

        response.then().assertThat().body("_id",equalTo(user.getUserInfo().get_id()));

    }

    @Test(description = "Invalid updating unregistered user")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Updating unregistered user by not sending his token")
    public void verifyUpdatingUnRegisteredUser(){
        Response response = updateUserApi.updateUser(null,email,testData.getTestData("UpdatedUserInfo.firstName")
                                                          ,testData.getTestData("UpdatedUserInfo.lastName")
                                                          ,testData.getTestData("UpdatedUserInfo.password"),401);

        response.then().assertThat().body("error",equalTo(testData.getTestData("messages.UnAuthenticated")));

    }

    // Configuration Section
    @BeforeClass
    public void setUp(){
        testData = new JsonFileManager("src/test/resources/TestData/ContactListTestData/UpdateUserTestData.json");
        addUserApi = new AddUserApi();
        updateUserApi = new UpdateUserApi();
        currentTime = new SimpleDateFormat("ddMMyyyyHHmmssSSS").format(new Date());
    }
}
