package ContactList.apis;

import Utiles.ApiActions;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;

public class LoginApii {
    ApiActions actions;
    String Url = "https://thinking-tester-contact-list.herokuapp.com/users/login";

    public LoginApii(){
        actions = new ApiActions(Url);
    }

    public Response Login(String email, String password, int expectedStatusCode){
        HashMap<String,String> reqBody = new HashMap<>();
        reqBody.put("email",email);
        reqBody.put("password",password);

        return actions.performRequest("POST",expectedStatusCode, ContentType.JSON,null,null,null,reqBody);
    }
}
