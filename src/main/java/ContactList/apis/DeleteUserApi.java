package ContactList.apis;

import Utiles.ApiActions;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;

public class DeleteUserApi {
    ApiActions actions;
    String url = "https://thinking-tester-contact-list.herokuapp.com/users/me";

    public DeleteUserApi() {
        actions = new ApiActions(url);
    }

    public Response deleteUser(String token , int expectedStatusCode){
        HashMap<String,String> header = new HashMap<>();
        header.put("Authorization","Bearer "+token);

       return actions.performRequest("DELETE", expectedStatusCode, ContentType.JSON,header,null,null,null);
    }
}
