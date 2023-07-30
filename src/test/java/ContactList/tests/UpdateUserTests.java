package ContactList.tests;

import ContactList.PojoClasses.User;
import ContactList.apis.AddUserApi;
import ContactList.apis.UpdateUserApi;
import Utiles.JsonFileManager;
import io.qameta.allure.*;
import io.restassured.response.Response;

import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;

@Epic("ContactList tests")
@Feature("Update user tests")
public class UpdateUserTests extends RegisterationBaseTest {

    // Variables Section
    AddUserApi addUserApi;
    UpdateUserApi updateUserApi;
    JsonFileManager testData;
    String currentTime;

    // Tests Section

    @Test(description = "Valid updating a registered user")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Updating a registered user with valid data using user's token")
    public void verifyUpdatingRegisteredUser(ITestContext context) {
        User registeredUser = (User) context.getAttribute("RegisteredUser");

        Response response = updateUserApi.updateUser(registeredUser.getToken()
                , registeredUser.getUserInfo().getEmail()
                , testData.getTestData("UpdatedUserInfo.firstName")
                , testData.getTestData("UpdatedUserInfo.lastName")
                , testData.getTestData("UpdatedUserInfo.password"), 200);

        response.then().assertThat().body("_id", equalTo(registeredUser.getUserInfo().get_id()));

    }

    @Test(description = "Invalid updating unregistered user")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Updating unregistered user by not sending his token")
    public void verifyUpdatingUnRegisteredUser() {
        Response response = updateUserApi.updateUser(null
                , testData.getTestData("UpdatedUserInfo.updatedEmail")
                , testData.getTestData("UpdatedUserInfo.firstName")
                , testData.getTestData("UpdatedUserInfo.lastName")
                , testData.getTestData("UpdatedUserInfo.password"), 401);

        response.then().assertThat().body("error", equalTo(testData.getTestData("messages.UnAuthenticated")));

    }

    // Configuration Section
    @BeforeClass
    public void setUp() {
        testData = new JsonFileManager("src/test/resources/TestData/ContactListTestData/UpdateUserTestData.json");
        addUserApi = new AddUserApi();
        updateUserApi = new UpdateUserApi();
        currentTime = new SimpleDateFormat("ddMMyyyyHHmmssSSS").format(new Date());
    }
}
