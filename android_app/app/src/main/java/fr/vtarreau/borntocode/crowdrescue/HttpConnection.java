package fr.vtarreau.borntocode.crowdrescue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.List;

public class HttpConnection {

    public final static String API_URL = "http://10.12.1.121:4567/";
    public final static Gson gson = new GsonBuilder().create();

    public static String makeRequest(String route, HttpMethod method, List<NameValuePair> params) {
        HttpURLConnection conn;
        String url = API_URL + route;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
            if (method == HttpMethod.POST) {
                HttpPost httpPost = new HttpPost(url);
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
                System.out.println(EntityUtils.toString(httpPost.getEntity()));
                System.out.println("Request : " + url);
                httpResponse = httpClient.execute(httpPost);
            } else if (method == HttpMethod.GET) {
                if (params != null) {
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    url += "?" + paramString;
                }
                System.out.println("Request : " + url);
                HttpGet httpGet = new HttpGet(API_URL + route);
                httpResponse = httpClient.execute(httpGet);
            }
            httpEntity = httpResponse.getEntity();
            String tmp = EntityUtils.toString(httpEntity);
            System.out.print("Reponse : " + tmp);
            return tmp;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public enum HttpMethod {
        POST,
        GET,
        PUT,
        DELETE
    }
}