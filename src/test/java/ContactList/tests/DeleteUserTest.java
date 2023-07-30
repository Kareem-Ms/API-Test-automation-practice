package ContactList.tests;

import ContactList.PojoClasses.User;
import ContactList.apis.AddUserApi;
import ContactList.apis.DeleteUserApi;
import ContactList.apis.LoginApii;
import Utiles.JsonFileManager;
import io.qameta.allure.*;

import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

@Epic("ContactList tests")
@Feature("Delete user test")
public class DeleteUserTest extends RegisterationBaseTest {

    // Variables Section
    AddUserApi addUserApi;
    DeleteUserApi deleteUserApi;
    LoginApii loginApi;
    JsonFileManager testData;
    String currentTime;

    // Tests Section
    @Test(description = "Valid deleting registered user")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Deleting a registered user using the token assigned to him")
    public void VerifyDeletingUserSuccessfully(ITestContext context) {
        User registeredUser = (User) context.getAttribute("RegisteredUser");
        deleteUserApi.deleteUser(registeredUser.getToken(), 200);

        loginApi.Login(registeredUser.getUserInfo().getEmail(), registeredUser.getUserInfo().getPassword(), 401);
    }

    // Configuration Section
    @BeforeClass
    public void setUp() {
        testData = new JsonFileManager("src/test/resources/TestData/ContactListTestData/AddUserTestData.json");
        addUserApi = new AddUserApi();
        deleteUserApi = new DeleteUserApi();
        loginApi = new LoginApii();
        currentTime = new SimpleDateFormat("ddMMyyyyHHmmssSSS").format(new Date());
    }
}
