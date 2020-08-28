package com.github.yeriomin.playstoreapi.requests;

import com.github.yeriomin.playstoreapi.DeviceInfoProvider;
import com.github.yeriomin.playstoreapi.GooglePlayApiUpdate;
import com.github.yeriomin.playstoreapi.HttpClientAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

import static com.github.yeriomin.playstoreapi.GooglePlayApiUpdate.COOKIE;
import static com.github.yeriomin.playstoreapi.GooglePlayApiUpdate.GSFID;

/**
 * Общий класс для всех запросов
 */
abstract class Requests {

    protected HttpClientAdapter client;
    protected DeviceInfoProvider deviceInfoProvider;

    protected HashMap<String, String> input;

    public Requests(GooglePlayApiUpdate googlePlayApiUpdate) {
        this.client = googlePlayApiUpdate.getClient();
        this.deviceInfoProvider = googlePlayApiUpdate.getDeviceInfoProvider();
    }

    public Requests(GooglePlayApiUpdate googlePlayApiUpdate, HashMap<String, String> input) {
        this.client = googlePlayApiUpdate.getClient();
        this.deviceInfoProvider = googlePlayApiUpdate.getDeviceInfoProvider();
        this.input = input;
    }

    abstract public HashMap<String, String> make() throws IOException;



    /**
     * Создает дефолтный body
     *
     * @return
     */
    protected Map<String, String> getAuthBody() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("androidId", input.get(GSFID));
        headers.put("lang", "ru-RU");
        headers.put("google_play_services_version", "203019015");
        headers.put("sdk_version", "24");
        headers.put("callerPkg", "com.google.android.gms");
        headers.put("callerSig", "38918a453d07199354f8b19af05ec6562ced5788");
        return headers;
    }

    /**
     * Создает дефольный хедер для запросов
     *
     * @return
     */
    protected Map<String, String> getAuthHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("device", input.get(GSFID));
        headers.put("app", "com.android.vending");
        headers.put("User-Agent", this.deviceInfoProvider.getAuthUserAgentString());
        headers.put("Accept-Encoding", "gzip");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        return headers;
    }

    /**
     * Создает дефольный хедер для запросов
     *
     * @return
     */
    protected Map<String, String> getDefaultHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-same-domain", "1");
        headers.put("google-accounts-xsrf", "1");
        headers.put("User-Agent", this.deviceInfoProvider.getAuthUserAgentString());
        headers.put("accept", "*/*");
        headers.put("origin", "https://accounts.google.com");
        headers.put("x-requested-with", "com.google.android.gms");
        headers.put("sec-fetch-site", "same-origin");
        headers.put("sec-fetch-mode", "cors");
        headers.put("sec-fetch-dest", "empty");
        headers.put("accept-encoding", "gzip, deflate");
        headers.put("accept-language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        return headers;
    }

    /**
     * Получаем тело запроса
     *
     * @param responseBytes
     * @return
     */
    protected byte[] getBody(HashMap<String, byte[]> responseBytes) {
        return responseBytes.get("body");
    }


    /**
     * Получаем из мапы хедер
     *
     * @param responseBytes
     * @return
     */
    protected Map<String, List<String>> getHeader(HashMap<String, byte[]> responseBytes) {
        try {
            ByteArrayInputStream byteIn = new ByteArrayInputStream(responseBytes.get("header"));
            ObjectInputStream in = new ObjectInputStream(byteIn);
            Map<String, List<String>> output = (Map<String, List<String>>) in.readObject();
            return output;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (HashMap<String, List<String>>) Collections.EMPTY_MAP;
    }

    /**
     * Сохраняем куки
     *
     * @param map
     */
    protected HashMap<String, String> cookie(Map<String, List<String>> map) {
        HashMap<String, String> output = new HashMap<>();
        for (String input : Objects.requireNonNull(map.get("set-cookie"))) {

            if (input.contains("user_id")) {
                output.put("user_id", input.replace("user_id=", "").substring(0, 21));
                continue;
            }

            if (input.contains("oauth_token")) {
                output.put("oauth_token", input.replace("oauth_token=", "").substring(0, 96));
                continue;
            }

            String temp[] = input.split("=", 2);
            output.put(COOKIE + temp[0], temp[1]);
        }
        return output;
    }


    /**
     * Не понятно как генерируется это число
     *
     * @return
     */
    protected String getReqid() {
        Random random = new Random();
        int n = 100000 + random.nextInt(900000);
        return Integer.toString(n);
    }
}
