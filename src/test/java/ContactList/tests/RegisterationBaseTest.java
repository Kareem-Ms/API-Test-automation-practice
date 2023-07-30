package ContactList.tests;

import ContactList.PojoClasses.User;
import ContactList.apis.AddUserApi;
import Utiles.JsonFileManager;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;

import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterationBaseTest {
    AddUserApi addUserApi = new AddUserApi();
    JsonFileManager testData = new JsonFileManager("src/test/resources/TestData/ContactListTestData/AddUserTestData.json");
    String currentTime = new SimpleDateFormat("ddMMyyyyHHmmssSSS").format(new Date());

    @BeforeTest(description = "Valid registration with unregistered email and password")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Registering a user using an email that hasn't been registered before")
    public void VerifyAddingUserWithUnregisteredEmail(ITestContext context) {
        String email = testData.getTestData("UserInfo.email") + "_" + currentTime + testData.getTestData("UserInfo.domain");
        String password = testData.getTestData("UserInfo.password");

        User user = addUserApi.AddUser(email, testData.getTestData("UserInfo.firstName")
                , testData.getTestData("UserInfo.lastName")
                , password, 201).as(User.class);

        Assert.assertEquals(user.getUserInfo().getEmail(), email);
        Assert.assertNotNull(user.getToken());
        user.getUserInfo().setPassword(password);
        context.setAttribute("RegisteredUser", user);
    }

}
