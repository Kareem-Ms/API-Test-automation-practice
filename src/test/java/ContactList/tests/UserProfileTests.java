package ContactList.tests;

import ContactList.PojoClasses.User;
import ContactList.apis.AddUserApi;
import ContactList.apis.GetUserProfileApi;
import Utiles.JsonFileManager;
import io.restassured.response.Response;
import org.testng.Assert;
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
    String password;
    User user;
    String currentTime = new SimpleDateFormat("ddMMyyyyHHmmssSSS").format(new Date());

    ///////////////Tests\\\\\\\\\\\\\\\\\\\\\\
    @Test
    public void verifyAddingUserWithUnregisteredEmail(){
        email = testData.getTestData("UserInfo.email")+"_"+currentTime+testData.getTestData("UserInfo.domain");
        password = testData.getTestData("UserInfo.password");

        user = addUserApi.AddUser(email,testData.getTestData("UserInfo.firstName")
                ,testData.getTestData("UserInfo.lastName")
                ,password,201).as(User.class);

        Assert.assertEquals(user.getUser().getEmail(),email);
        Assert.assertNotNull(user.getToken());

    }

    @Test(dependsOnMethods = "verifyAddingUserWithUnregisteredEmail")
    public void verifyGettingUserProfile(){
        Response response = getUserProfileApi.getUserProfile(user.getToken(),200);

        response.then().assertThat().body("email",equalTo(email))
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
