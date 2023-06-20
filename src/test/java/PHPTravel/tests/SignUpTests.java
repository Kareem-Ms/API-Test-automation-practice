package PHPTravel.tests;

import PHPTravel.apis.RegisterApi;
import Utiles.JsonFileManager;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.text.SimpleDateFormat;
import static org.hamcrest.Matchers.equalTo;

public class SignUpTests {

    //////////////Variables\\\\\\\\\\\\\\\\\
    JsonFileManager testData;
    RegisterApi registerApi;
    String email;
    String password;
    String currentTime = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

    ///////////////Tests\\\\\\\\\\\\\\\\\\\\
    @Test
    public void VerifyRegisteringUserWithUnregisteredEmail(){
        email = testData.getTestData("userInfo.email")+"_"+currentTime+testData.getTestData("userInfo.domain");
        password = testData.getTestData("userInfo.password");

        Response response = registerApi.RegisterUser(email,password
                ,testData.getTestData("userInfo.first_name")
                ,testData.getTestData("userInfo.last_name")
                ,testData.getTestData("userInfo.phone")
                ,testData.getTestData("userInfo.status")
                ,testData.getTestData("userInfo.type")
                ,testData.getTestData("userInfo.signup_token")
                ,testData.getTestData("userInfo.phone_country_code"));

        response.then().assertThat()
                .body("message",equalTo(testData.getTestData("messages.SucessfullRegister")))
                .body("status",equalTo(Boolean. parseBoolean(testData.getTestData("messages.SucessStatus"))));;
    }

    @Test(dependsOnMethods = "VerifyRegisteringUserWithUnregisteredEmail")
    public void VerifyRegisteringUserWithRegisteredEmail(){
        Response response = registerApi.RegisterUser(email,password
                ,testData.getTestData("userInfo.first_name")
                ,testData.getTestData("userInfo.last_name")
                ,testData.getTestData("userInfo.phone")
                ,testData.getTestData("userInfo.status")
                ,testData.getTestData("userInfo.type")
                ,testData.getTestData("userInfo.signup_token")
                ,testData.getTestData("userInfo.phone_country_code"));

        response.then().assertThat()
                .body("message",equalTo(testData.getTestData("messages.EmailRegisteredBefore")))
                .body("status",equalTo(Boolean. parseBoolean(testData.getTestData("messages.FailStatus"))));
    }

    ///////////////Configuration\\\\\\\\\\\\
    @BeforeClass
    public void setUp(){
        registerApi = new RegisterApi();
        testData = new JsonFileManager("src/test/resources/TestData/PHPTravelTestData/SignUpTestData.json");
    }
}
