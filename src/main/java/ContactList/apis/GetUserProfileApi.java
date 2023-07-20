package ContactList.apis;

import Utiles.ApiActions;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;

public class GetUserProfileApi {
    String Url = "https://thinking-tester-contact-list.herokuapp.com/users/me";
    ApiActions actions;

    public GetUserProfileApi(){
        actions = new ApiActions(Url);
    }

    @Step("Get user's profile using user token: {token}")
    public Response getUserProfile(String token, int expectedStatusCode){
        HashMap<String,String> header = new HashMap<>();
        header.put("Authorization","Bearer "+token);

        return actions.performRequest("GET",expectedStatusCode, ContentType.JSON, header,null,null,null);
    }
}
