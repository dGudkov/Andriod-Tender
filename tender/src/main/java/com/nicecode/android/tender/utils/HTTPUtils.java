package com.nicecode.android.tender.utils;

import android.support.v4.util.Pair;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.nicecode.android.tender.BuildConfig;
import com.nicecode.android.tender.library.exception.ApplicationException;
import com.nicecode.android.tender.library.exception.HttpException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author Danil Gudkov <danil.gudkov@progforce.com>
 * @copyrights ProgForce, 2016
 * @since 18.03.16.
 */

public final class HTTPUtils {

    public static String TAG = "HTTPUtils";
    public static int DEFAULT_TIME_OUT = 15000; //

    public static <T> T doPost(String url, List<Pair<String, String>> requestProperty, Class<T> type, Object dataObject) throws ApplicationException {
        return doPost(url, DEFAULT_TIME_OUT, requestProperty, type, dataObject);
    }

    @SuppressWarnings("unchecked")
    public static <T> T doPost(String url, int timeout, List<Pair<String, String>> requestProperty,
                               Class<T> type,
                               Object dataObject) throws ApplicationException {


//        ObjectMapper mapper = new ObjectMapper();
        // TODO change to okhttp
        Gson gson = new Gson();
        HttpURLConnection conn = null;
        BufferedReader br = null;
        OutputStream os = null;

        try {
            URL urlConnection = new URL(url);
            conn = (HttpURLConnection) urlConnection.openConnection();
            conn.setDoOutput(true);
            conn.setConnectTimeout(timeout);
            conn.setRequestMethod("POST");
            for (Pair<String, String> entry : requestProperty) {
                conn.setRequestProperty(entry.first, entry.second);
            }

            String input;

            List<Pair<String, Object>> requestData = (List<Pair<String, Object>>) dataObject;

            StringBuilder resultS = new StringBuilder();
            boolean first = true;

            for (Pair<String, Object> pair : requestData) {
                if (first)
                    first = false;
                else
                    resultS.append("&");

                resultS.append(URLEncoder.encode(pair.first, "UTF-8"));
                resultS.append("=");
                if (pair.second instanceof Boolean) {
                    resultS.append(URLEncoder.encode(Boolean.toString((Boolean) pair.second), "UTF-8"));
                } else {
                    resultS.append(URLEncoder.encode(pair.second.toString(), "UTF-8"));
                }
            }
            input = resultS.toString();

            os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            int responseCode = conn.getResponseCode();

            if (BuildConfig.DEBUG) Log.d(TAG, "Returns HTTP error code : " + responseCode);

            String answer;
            switch (responseCode) {
//                case HttpURLConnection.HTTP_FORBIDDEN:
//                    if (BuildConfig.DEBUG) Log.d(TAG, "Return HTTP error code : " +  responseCode);
//                    answer = IOUtils.toString(conn.getErrorStream());
//                    break;
                case HttpURLConnection.HTTP_OK:
                    answer = IOUtils.toString(conn.getInputStream());
                    break;
                default:
                    throw new HttpException("Failed : HTTP error code : "
                            + conn.getResponseCode());
            }
            T result = gson.fromJson(answer, type);
            if (BuildConfig.DEBUG) Log.d(TAG, "Result: " + gson.toJson(result));
            return result;
        } catch (IOException e) {
            throw new HttpException("Error: " + e.getMessage(), e);
        } catch (JsonSyntaxException | JsonIOException je) {
            throw new ApplicationException("Error: " + je.getMessage(), je);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ignored) {
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ignored) {
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static <T> T doGet(
            String url,
            List<Pair<String, String>> requestParameters,
            List<Pair<String, String>> requestProperty,
            Class<T> type) throws ApplicationException {
        return doGet(url, DEFAULT_TIME_OUT, requestParameters, requestProperty, type);
    }

    public static <T> T doGet(String url, int timeout,
                              List<Pair<String, String>> requestParameters,
                              List<Pair<String, String>> requestProperty,
                              Class<T> type) throws ApplicationException {

        Gson gson = new Gson();
        HttpURLConnection conn = null;
        InputStream inputStream = null;

        try {
            if (BuildConfig.DEBUG)
                Log.d(TAG, "Calling doGet (" + url + ", " + timeout + ", " + type.getName() + ")");

            String fullUrl;
            if (requestParameters != null) {
                String sParams = "";

                for (Pair<String, String> entry : requestParameters) {
                    if (!sParams.equals("")) {
                        sParams += "&";
                    }
                    sParams += entry.first + "=" + entry.second;
                }

                fullUrl = url + (sParams.length() > 0 ? "?" + sParams : "");
            } else {
                fullUrl = url;
            }

            URL urlConnection = new URL(fullUrl);
            conn = (HttpURLConnection) urlConnection.openConnection();
            conn.setConnectTimeout(timeout);
            conn.setRequestMethod("GET");

            for (Pair<String, String> entry : requestProperty) {
                conn.setRequestProperty(entry.first, entry.second);
            }

            inputStream = conn.getInputStream();

            String answer = IOUtils.toString(inputStream);

            T result = gson.fromJson(answer, type);

            if (BuildConfig.DEBUG) Log.d(TAG, "Result: " + gson.toJson(result));

            return result;
        } catch (IOException e) {
            throw new HttpException("Error: " + e.getMessage(), e);
        } catch (JsonSyntaxException | JsonIOException je) {
            throw new ApplicationException("Error: " + je.getMessage(), je);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

//    public static Cookie getCookies(String cookies, String value) {
//        String[] fields = cookies.split(";\\s*");
//        for (int j = 0; j < fields.length; j++) {
//            String[] f = fields[j].split("=");
//            if (value.equalsIgnoreCase(f[0])) {
//                return new Cookie(f[0], f[1]);
//            }
//        }
//        return null;
//    }

}
