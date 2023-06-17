package ContactList.tests;

import ContactList.apis.AddUserApi;
import ContactList.apis.GetUserProfileApi;
import Utiles.JsonFileManager;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

public class UserProfileTests {

    ///////////////Variables\\\\\\\\\\\\\\\\\
    AddUserApi addUserApi;
    GetUserProfileApi getUserProfileApi;
    JsonFileManager testData;
    String email;
    String token;
    String currentTime = new SimpleDateFormat("ddMMyyyyHHmmssSSS").format(new Date());

    ///////////////Tests\\\\\\\\\\\\\\\\\\\\\\
    @Test
    public void verifyAddingUserWithUnregisteredEmail(){
        email = testData.getTestData("UserInfo.email")+"_"+currentTime+testData.getTestData("UserInfo.domain");
        Response response = addUserApi.AddUser(email,testData.getTestData("UserInfo.firstName")
                ,testData.getTestData("UserInfo.lastName")
                ,testData.getTestData("UserInfo.password"),201);

        response.then().assertThat()
                .body("user.email",equalTo(email))
                .body("",hasKey("token"));

        token = response.path("token");
    }

    @Test(dependsOnMethods = "verifyAddingUserWithUnregisteredEmail")
    public void verifyGettingUserProfile(){
        Response response = getUserProfileApi.getUserProfile(token,200);

        response.then().assertThat()
                .body("email",equalTo(email))
                .body("",hasKey("_id"));
    }

    @Test
    public void verifyGettingUserProfileWithoutToken(){
        Response response = getUserProfileApi.getUserProfile(null, 401);

        response.then().assertThat().body("error",equalTo(testData.getTestData("messages.UnAuthenticated")));
    }

    ///////////////Configuration\\\\\\\\\\\\\\\
    @BeforeClass
    public void setUp(){
        testData = new JsonFileManager("src/test/resources/TestData/ContactListTestData/GetUserProfileTestData.json");
        addUserApi = new AddUserApi();
        getUserProfileApi = new GetUserProfileApi();
    }
}
