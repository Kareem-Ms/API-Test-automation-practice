package ContactList.tests;

import ContactList.apis.AddUserApi;
import Utiles.JsonFileManager;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.*;

public class AddUserTests {

    ///////////////Variables\\\\\\\\\\\\\\\\\
    AddUserApi addUserApi;
    JsonFileManager testData;
    String email;
    String currentTime = new SimpleDateFormat("ddMMyyyyHHmmssSSS").format(new Date());

    ///////////////Tests\\\\\\\\\\\\\\\\\\\\\\
    @Test
    public void VerifyAddingUserWithUnregisteredEmail(){
        email = testData.getTestData("UserInfo.email")+"_"+currentTime+testData.getTestData("UserInfo.domain");
        Response response = addUserApi.AddUser(email,testData.getTestData("UserInfo.firstName")
                                                    ,testData.getTestData("UserInfo.lastName")
                                                    ,testData.getTestData("UserInfo.password"),201);

        response.then().assertThat().body("user.email",equalTo(email))
                                    .body("",hasKey("token"));
    }

    @Test(dependsOnMethods = "VerifyAddingUserWithUnregisteredEmail")
    public void VerifyAddingUserWithRegisteredEmail(){

        Response response = addUserApi.AddUser(email,testData.getTestData("UserInfo.firstName")
                                                    ,testData.getTestData("UserInfo.lastName")
                                                    ,testData.getTestData("UserInfo.password"),400);

        response.then().assertThat().body("message",equalTo(testData.getTestData("messages.EmailTaken")));
    }

    ///////////////Configuration\\\\\\\\\\\\\\\
    @BeforeClass
    public void setUp(){
        testData = new JsonFileManager("src/test/resources/TestData/ContactListTestData/AddUserTestData.json");
        addUserApi = new AddUserApi();
    }

}
