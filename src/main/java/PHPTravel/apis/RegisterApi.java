package PHPTravel.apis;

import Utiles.ApiActions;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;

public class RegisterApi {
    ApiActions actions;
    String RegisterApiUrl = "https://phptravels.net/api/signup";

    public RegisterApi() {
        actions = new ApiActions(RegisterApiUrl);
    }

    @Step("Register user using Email: {email}, Password: {password}, FirstName: {firstName}, LastName: {lastName}, Phone: {phone}, Status: {status}, Type: {type}, SignUpToken: {signupToken}, Phone_country_code: {phone_country_code}")
    public Response RegisterUser(String email, String password, String firstName, String lastName, String phone, String status, String type, String signupToken, String phone_country_code) {
        //Forming request body
        HashMap<String, String> reqBody = new HashMap<>();
        reqBody.put("email", email);
        reqBody.put("password", password);
        reqBody.put("first_name", firstName);
        reqBody.put("last_name", lastName);
        reqBody.put("phone", phone);
        reqBody.put("status", status);
        reqBody.put("type", type);
        reqBody.put("signup_token", signupToken);
        reqBody.put("phone_country_code", phone_country_code);

        return actions.performRequest("POST", 200, ContentType.MULTIPART, null, null, reqBody, null);
    }

}
