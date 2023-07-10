package ContactList.tests;

import ContactList.PojoClasses.User;
import ContactList.apis.AddUserApi;
import Utiles.JsonFileManager;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.*;

public class AddUserTests {

    ///////////////Variables\\\\\\\\\\\\\\\\\
    AddUserApi addUserApi;

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
    public void VerifyAddingUserWithRegisteredEmail(){
        Response response = addUserApi.AddUser(email,user.getUser().getFirstName(), user.getUser().getLastName(),password ,400);

        response.then().assertThat().body("message",equalTo(testData.getTestData("messages.EmailTaken")));
    }

    ///////////////Configuration\\\\\\\\\\\\\\\
    @BeforeClass
    public void setUp(){
        testData = new JsonFileManager("src/test/resources/TestData/ContactListTestData/AddUserTestData.json");
        addUserApi = new AddUserApi();
    }

}
