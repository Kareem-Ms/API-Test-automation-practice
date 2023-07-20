package ContactList.tests;

import ContactList.PojoClasses.User;
import ContactList.apis.AddUserApi;
import ContactList.apis.GetUserProfileApi;
import Utiles.JsonFileManager;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

@Epic("ContactList tests")
@Feature("User profile tests")
public class UserProfileTests {

    // Variables Section
    AddUserApi addUserApi;
    GetUserProfileApi getUserProfileApi;
    JsonFileManager testData;
    String email;
    String password;
    User user;
    String currentTime;

    // Tests Section
    @Test(description = "Valid registration with unregistered email and password")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Registering a user using an email that hasn't been registered before")
    public void verifyAddingUserWithUnregisteredEmail(){
        email = testData.getTestData("UserInfo.email")+"_"+currentTime+testData.getTestData("UserInfo.domain");
        password = testData.getTestData("UserInfo.password");

        user = addUserApi.AddUser(email,testData.getTestData("UserInfo.firstName")
                ,testData.getTestData("UserInfo.lastName")
                ,password,201).as(User.class);

        Assert.assertEquals(user.getUserInfo().getEmail(),email);
        Assert.assertNotNull(user.getToken());

    }

    @Test(dependsOnMethods = "verifyAddingUserWithUnregisteredEmail",description = "Valid getting a registered user profile")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Getting registered user's profile using his token")
    public void verifyGettingUserProfile(){
        Response response = getUserProfileApi.getUserProfile(user.getToken(),200);

        response.then().assertThat().body("email",equalTo(email))
                                    .body("",hasKey("_id"));
    }

    @Test(description = "Invalid getting a non registered user profile")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Getting non registered user's profile without providing his token")
    public void verifyGettingUserProfileWithoutToken(){
        Response response = getUserProfileApi.getUserProfile(null, 401);

        response.then().assertThat().body("error",equalTo(testData.getTestData("messages.UnAuthenticated")));
    }

    // Configuration Section
    @BeforeClass
    public void setUp(){
        testData = new JsonFileManager("src/test/resources/TestData/ContactListTestData/GetUserProfileTestData.json");
        addUserApi = new AddUserApi();
        getUserProfileApi = new GetUserProfileApi();
        currentTime = new SimpleDateFormat("ddMMyyyyHHmmssSSS").format(new Date());
    }
}
