package Utiles;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.fail;

public class ApiActions {

    //the overall idea that i want to form the request and put it in request specification object
    //then we are gonna send that request wether it's get or post or whatever
    //then we will return the response

    private RequestSpecification request;
    private Response response;
    private  String ApiUrl;

    public ApiActions(String ApiUrl) {
        this.ApiUrl = ApiUrl;
    }

    public Response performRequest(String RequestType, int expectedStatusCode, ContentType contentType, HashMap<String, String> headers, HashMap<String, String> queryParams,HashMap<String,String> FormParams ,HashMap<String,String> body) {

        request = RestAssured.given().log().all();

        try {
            //forming the request with the given parameters
            if (contentType != null) {
                request.contentType(contentType);
            }
            if (headers != null) {
                request.headers(headers);
            }
            if (queryParams != null) {
                request.queryParams(queryParams);
            }
            if (FormParams != null) {
                for (HashMap.Entry<String,String> entry : FormParams.entrySet())
                {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    request.multiPart(key, value);
                }
            }
            if (body != null) {
                request.body(body);
            }

            //check request type
            if (RequestType.equalsIgnoreCase("POST")) {
                response = request.when().post(ApiUrl);
            }
            if (RequestType.equalsIgnoreCase("GET")) {
                response = request.when().get(ApiUrl);
            }
            if (RequestType.equalsIgnoreCase("PUT")) {
                response = request.when().put(ApiUrl);
            }
            if (RequestType.equalsIgnoreCase("DELETE")) {
                response = request.when().delete(ApiUrl);
            }
            if (RequestType.equalsIgnoreCase("PATCH")) {
                response = request.when().patch(ApiUrl);
            }

            response.then().log().all().assertThat().statusCode(expectedStatusCode);

        } catch (Exception e) {
            fail(e.getMessage());
        }

        return response;
    }

}
