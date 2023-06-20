package ContactList.tests;

import ContactList.apis.AddUserApi;
import ContactList.apis.UpdateUserApi;
import Utiles.JsonFileManager;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

public class UpdateUserTests{

    ///////////////Variables\\\\\\\\\\\\\\\\\
    AddUserApi addUserApi;
    UpdateUserApi updateUserApi;
    JsonFileManager testData;
    String email;
    String token;
    String userID;
    String currentTime = new SimpleDateFormat("ddMMyyyyHHmmssSSS").format(new Date());

    ///////////////Tests\\\\\\\\\\\\\\\\\\\\\\
    @Test
    public void verifyAddingUserWithUnregisteredEmail(){
        email = testData.getTestData("RegisteringUserInfo.email")+"_"+currentTime+testData.getTestData("RegisteringUserInfo.domain");
        Response response = addUserApi.AddUser(email,testData.getTestData("RegisteringUserInfo.firstName")
                                              ,testData.getTestData("RegisteringUserInfo.lastName")
                                              ,testData.getTestData("RegisteringUserInfo.password"),201);

        response.then().assertThat().body("user.email",equalTo(email))
                                    .body("",hasKey("token"));

        token = response.path("token");
        userID = response.path("_id");
    }

    @Test(dependsOnMethods = "verifyAddingUserWithUnregisteredEmail")
    public void verifyUpdatingRegisteredUser(){
        Response response = updateUserApi.updateUser(token,email,testData.getTestData("UpdatedUserInfo.firstName")
                                                          ,testData.getTestData("UpdatedUserInfo.lastName")
                                                          ,testData.getTestData("UpdatedUserInfo.password"),200);

        response.then().assertThat().body("user._id",equalTo(userID));

    }
    @Test
    public void verifyUpdatingUnRegisteredUser(){
        Response response = updateUserApi.updateUser(null,email,testData.getTestData("UpdatedUserInfo.firstName")
                                                          ,testData.getTestData("UpdatedUserInfo.lastName")
                                                          ,testData.getTestData("UpdatedUserInfo.password"),401);

        response.then().assertThat().body("error",equalTo(testData.getTestData("messages.UnAuthenticated")));

    }

    ///////////////Configuration\\\\\\\\\\\\\\\
    @BeforeClass
    public void setUp(){
        testData = new JsonFileManager("src/test/resources/TestData/ContactListTestData/UpdateUserTestData.json");
        addUserApi = new AddUserApi();
        updateUserApi = new UpdateUserApi();
    }
}
