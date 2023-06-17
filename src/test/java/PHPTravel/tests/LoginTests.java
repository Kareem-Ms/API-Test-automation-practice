package PHPTravel.tests;

import PHPTravel.Pages.AccountVerificationPage;
import PHPTravel.apis.LoginApi;
import PHPTravel.apis.RegisterApi;
import Utiles.JsonFileManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.response.Response;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.text.SimpleDateFormat;

import static org.hamcrest.Matchers.equalTo;

public class LoginTests {

    ///////////////Variables\\\\\\\\\\\\\\\\\
    WebDriver driver;
    JsonFileManager testData;
    LoginApi loginApi;
    RegisterApi registerApi;
    AccountVerificationPage accountVerificationPage;
    String email;
    String password;
    String UserId;
    String EmailCode;
    String currentTime = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

    ///////////////Tests\\\\\\\\\\\\\\\\\
    @Test
    public void VerifyRegisteringUserWithValidData(){
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

       response.then().assertThat().body("message",equalTo(testData.getTestData("messages.SucessfullRegister")));
       UserId = response.path("data[0].user_id");
       EmailCode = response.path("data[0].email_code");
    }

    @Test(dependsOnMethods = "VerifyRegisteringUserWithValidData")
    public void VerifyAccountSuccessfully(){
        accountVerificationPage.verifyAccount(UserId, EmailCode);
        Assert.assertEquals(accountVerificationPage.getAccountActivatedMsg(),testData.getTestData("messages.AccountVerification"));

    }

    @Test(dependsOnMethods = "VerifyAccountSuccessfully")
    public void LoginWithValidCredintials(){
       Response response =  loginApi.login(email,password);

       response.then().assertThat()
               .body("message",equalTo(testData.getTestData("messages.LoginVerification")))
               .body("status",equalTo(Boolean. parseBoolean(testData.getTestData("messages.status"))));

    }

    @Test
    public void LoginWithInvalidCredintials(){
        Response response =  loginApi.login(testData.getTestData("userInfo.UnregisteredEmail"),testData.getTestData("userInfo.UnregisteredPassword"));

        response.then().assertThat()
                .body("message",equalTo(testData.getTestData("messages.InvalidLogin")))
                .body("status",equalTo(Boolean. parseBoolean(testData.getTestData("messages.FailStatus"))));
    }

    ///////////////Configuration\\\\\\\\\\\\\\\\\
    @BeforeClass
    public void setUp(){
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        driver = new ChromeDriver(options);
        accountVerificationPage = new AccountVerificationPage(driver);
        loginApi = new LoginApi();
        registerApi = new RegisterApi();
        testData = new JsonFileManager("src/test/resources/TestData/PHPTravelTestData/LoginTestData.json");
    }

    @AfterClass
    public void tearDown(){
        driver.quit();
    }

}
//activation = https://phptravels.net/account/activation/user_id/email_code