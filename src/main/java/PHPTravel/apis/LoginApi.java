package PHPTravel.apis;

import Utiles.ApiActions;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.HashMap;

public class LoginApi {
    ApiActions actions;
    String baseUri = "https://phptravels.net/api/login";

    public LoginApi() {
        actions = new ApiActions(baseUri);
    }

    public Response login(String email, String password){
        //forming request body
        HashMap<String, String> reqBody = new HashMap<>();
        reqBody.put("email",email);
        reqBody.put("password",password);

        return actions.performRequest("POST",200, ContentType.MULTIPART,null,null,reqBody,null);
    }

    /*
    *   Properties props = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/phptravels.properties");
        props.load(fis);
        String baseUrii = props.getProperty("baseurl");
    *
    * */
}
