package ContactList.apis;

import Utiles.ApiActions;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;

public class UpdateUserApi {
    ApiActions actions;
    String Url = "https://thinking-tester-contact-list.herokuapp.com/users/me";

    public UpdateUserApi(){
        actions = new ApiActions(Url);
    }

    @Step("Update user with token: {token}, with data Email: {email}, FirstName: {firstName}, LastName: {lastName}, Password: {password}")
    public Response updateUser(String token , String email, String firstName, String lastName, String password, int expectedStatusCode){
        HashMap<String,String> headers = new HashMap<>();
        headers.put("Authorization","Bearer "+token);

        HashMap<String,String> reqBody = new HashMap<>();
        reqBody.put("firstName", firstName);
        reqBody.put("lastName", lastName);
        reqBody.put("email", email);
        reqBody.put("password", password);

        return actions.performRequest("PATCH",expectedStatusCode,ContentType.JSON,headers,null,null,reqBody);
    }
}
