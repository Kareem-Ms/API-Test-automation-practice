package PHPTravel.tests;

import PHPTravel.apis.RegisterApi;
import Utiles.JsonFileManager;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.hamcrest.Matchers.equalTo;

@Epic("PHPTravel tests")
@Feature("SignUp tests")
public class SignUpTests {

    // Variables Section
    JsonFileManager testData;
    RegisterApi registerApi;
    String email;
    String password;
    String currentTime ;

    // Tests Section
    @Test(description = "Valid registration with unregistered email and password")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Registering a user using an email that hasn't been registered before")
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
                .body("status",equalTo(Boolean. parseBoolean(testData.getTestData("messages.SucessStatus"))));
    }

    @Test(dependsOnMethods = "VerifyRegisteringUserWithUnregisteredEmail",description = "Invalid registration with email that have been registered before")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Registering user using an email that have been registered before")
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

    // Configuration Section
    @BeforeClass
    public void setUp(){
        registerApi = new RegisterApi();
        testData = new JsonFileManager("src/test/resources/TestData/PHPTravelTestData/SignUpTestData.json");
        currentTime = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(Calendar.getInstance().getTime());
    }
}
