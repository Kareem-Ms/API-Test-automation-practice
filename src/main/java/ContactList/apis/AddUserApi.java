package ContactList.apis;

import Utiles.ApiActions;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;

public class AddUserApi {
    String Url = "https://thinking-tester-contact-list.herokuapp.com/users";
    ApiActions actions;

    public AddUserApi() {
        actions = new ApiActions(Url);
    }

    @Step("Register new user with Email: {email}, FirstName: {firstName}, LastName: {lastName}, Password: {password}")
    public Response AddUser(String email, String firstName, String lastName, String password, int expectedStatusCode) {
        HashMap<String, String> reqBody = new HashMap<>();
        reqBody.put("firstName", firstName);
        reqBody.put("lastName", lastName);
        reqBody.put("email", email);
        reqBody.put("password", password);

        return actions.performRequest("POST", expectedStatusCode, ContentType.JSON, null, null, null, reqBody);
    }

}
