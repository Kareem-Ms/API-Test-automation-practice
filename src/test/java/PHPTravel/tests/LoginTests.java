package PHPTravel.tests;

import PHPTravel.Pages.AccountVerificationPage;
import PHPTravel.apis.LoginApi;
import PHPTravel.apis.RegisterApi;
import Utiles.JsonFileManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.hamcrest.Matchers.equalTo;

@Epic("PHPTravel tests")
@Feature("Login tests")
public class LoginTests {

    // Variables Section
    WebDriver driver;
    JsonFileManager testData;
    LoginApi loginApi;
    RegisterApi registerApi;
    AccountVerificationPage accountVerificationPage;
    String email;
    String password;
    String UserId;
    String EmailCode;
    String currentTime;

    // Tests Section
    @Test(description = "Valid registration with unregistered email and password")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Registering a user using an email that hasn't been registered before")
    public void VerifyRegisteringUserWithValidData() {
        email = testData.getTestData("userInfo.email") + "_" + currentTime + testData.getTestData("userInfo.domain");
        password = testData.getTestData("userInfo.password");

        Response response = registerApi.RegisterUser(email, password
                , testData.getTestData("userInfo.first_name")
                , testData.getTestData("userInfo.last_name")
                , testData.getTestData("userInfo.phone")
                , testData.getTestData("userInfo.status")
                , testData.getTestData("userInfo.type")
                , testData.getTestData("userInfo.signup_token")
                , testData.getTestData("userInfo.phone_country_code"));

        response.then().assertThat().body("message", equalTo(testData.getTestData("messages.SucessfullRegister")));
        UserId = response.path("data[0].user_id");
        EmailCode = response.path("data[0].email_code");
    }

    @Test(dependsOnMethods = "VerifyRegisteringUserWithValidData", description = "Verify account through accessing verification link")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify account through accessing the url of activation link")
    public void VerifyAccountSuccessfully() {
        accountVerificationPage.verifyAccount(UserId, EmailCode);
        Assert.assertEquals(accountVerificationPage.getAccountActivatedMsg(), testData.getTestData("messages.AccountVerification"));

    }

    @Test(dependsOnMethods = "VerifyAccountSuccessfully", description = "Valid login with correct email and password")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Login to user account using a registered email and password")
    public void LoginWithValidCredintials() {
        Response response = loginApi.login(email, password);

        response.then().assertThat()
                .body("message", equalTo(testData.getTestData("messages.LoginVerification")))
                .body("status", equalTo(Boolean.parseBoolean(testData.getTestData("messages.status"))));

    }

    @Test(description = "Invalid login with incorrect email and password")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Login to user account using a non registered email and password")
    public void LoginWithInvalidCredintials() {
        Response response = loginApi.login(testData.getTestData("userInfo.UnregisteredEmail"), testData.getTestData("userInfo.UnregisteredPassword"));

        response.then().assertThat()
                .body("message", equalTo(testData.getTestData("messages.InvalidLogin")))
                .body("status", equalTo(Boolean.parseBoolean(testData.getTestData("messages.FailStatus"))));
    }

    // Configuration Section
    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        driver = new ChromeDriver(options);
        accountVerificationPage = new AccountVerificationPage(driver);
        loginApi = new LoginApi();
        registerApi = new RegisterApi();
        testData = new JsonFileManager("src/test/resources/TestData/PHPTravelTestData/LoginTestData.json");
        currentTime = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(Calendar.getInstance().getTime());
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

}
//activation = https://phptravels.net/account/activation/user_id/email_code