package PHPTravel.apis;

import Utiles.ApiActions;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;

public class LoginApi {
    ApiActions actions;
    String baseUri = "https://phptravels.net/api/login";

    public LoginApi() {
        actions = new ApiActions(baseUri);
    }

    @Step("Login using Email: {email}, Password: {password}")
    public Response login(String email, String password) {
        //forming request body
        HashMap<String, String> reqBody = new HashMap<>();
        reqBody.put("email", email);
        reqBody.put("password", password);

        return actions.performRequest("POST", 200, ContentType.MULTIPART, null, null, reqBody, null);
    }

}
