package ContactList.tests;

import ContactList.apis.AddUserApi;
import ContactList.apis.LoginApi;
import Utiles.JsonFileManager;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

public class LoginTests {

    ///////////////Variables\\\\\\\\\\\\\\\\\
    AddUserApi addUserApi;
    LoginApi loginApi;
    JsonFileManager testData;
    String email;
    String password;
    String currentTime = new SimpleDateFormat("ddMMyyyyHHmmssSSS").format(new Date());

    ///////////////Tests\\\\\\\\\\\\\\\\\\\\\\
    @Test
    public void VerifyAddingUserWithUnregisteredEmail(){
        email = testData.getTestData("UserInfo.email")+"_"+currentTime+testData.getTestData("UserInfo.domain");
        password = testData.getTestData("UserInfo.password");

        Response response = addUserApi.AddUser(email,testData.getTestData("UserInfo.firstName")
                ,testData.getTestData("UserInfo.lastName")
                ,password,201);

        response.then().assertThat().body("user.email",equalTo(email))
                .body("",hasKey("token"));
    }

    @Test(dependsOnMethods = "VerifyAddingUserWithUnregisteredEmail")
    public void VerifyLoginWithCorrectEmailAndPw(){
       Response response = loginApi.Login(email, password, 200);

       response.then().body("user.email", equalTo(email))
                      .body("",hasKey("token"));
    }

    @Test
    public void VerifyLoginWithInCorrectEmailAndPw(){
        Response response = loginApi.Login(testData.getTestData("UserInfo.UnregisteredEmail"), password, 401);
    }


    ///////////////Configuration\\\\\\\\\\\\\\\
    @BeforeClass
    public void setUp(){
        testData = new JsonFileManager("src/test/resources/TestData/ContactListTestData/LoginTestData.json");
        addUserApi = new AddUserApi();
        loginApi = new LoginApi();
    }

}
