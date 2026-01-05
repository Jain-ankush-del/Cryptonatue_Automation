package framework.utils;

import framework.browser.Browser;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.response.Response;

import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static framework.browser.Browser.*;
import static io.restassured.RestAssured.given;

public class RestAssuredUtils {

    private static final int REQUEST_TIMEOUT = getRequestTimeoutLong();

    private static final Map.Entry<String, String> BASIC_AUTH = getBasicAuth();

    private RestAssuredUtils() {}

    public static int getResponseStatusCode(String url) throws UnsupportedEncodingException {
        String decodedURL = isUrl(url) ? URLDecoder.decode(url, StandardCharsets.UTF_8.toString()) : getCurrentUrl();
        Response response;

        try {
            response = getResponse(decodedURL, REQUEST_TIMEOUT);
        } catch (SocketTimeoutException exc) {
            return 408;
        } catch (URISyntaxException | IllegalArgumentException exc) {
            return 400;
        }

        return response.getStatusCode();
    }

    public static String checkEnv(String url){
        String staging = "staging:staging@";
        if(url.contains("staging.")){
            int insertPosition = url.indexOf("staging");
            url = url.substring(0, insertPosition)
                    + staging
                    + url.substring(insertPosition);
        }
        return url;
    }

    public static int getResponseStatusCode(String url, int timeout) throws UnsupportedEncodingException {
        String decodedURL = isUrl(url) ? URLDecoder.decode(url, StandardCharsets.UTF_8.toString()) : getCurrentUrl();
        String URL = checkEnv(decodedURL);
        Response response;

        try {
            response = getResponse(URL, timeout);
        } catch (SocketTimeoutException exc) {
            return 408;
        } catch (URISyntaxException | IllegalArgumentException exc) {
            return 400;
        }

        return response.getStatusCode();
    }

    private static Response getResponse(String url, int timeout) throws SocketTimeoutException, URISyntaxException, IllegalArgumentException {
        return given().header("User-Agent", Browser.getUserAgent())
                .auth().basic(BASIC_AUTH.getKey(), BASIC_AUTH.getValue())
                .config(RestAssured.config().httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.socket.timeout", timeout)
                        .setParam("http.connection.timeout", timeout)))
                .when().get(url);
    }

    private static boolean isUrl(String stringToCheck) {
        try {
            new URI(stringToCheck);
            return true;
        } catch (URISyntaxException exc) {
            return false;
        }
    }
}