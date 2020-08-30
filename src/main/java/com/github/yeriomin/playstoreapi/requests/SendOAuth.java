package com.github.yeriomin.playstoreapi.requests;

import com.github.yeriomin.playstoreapi.DeviceInfoProvider;
import com.github.yeriomin.playstoreapi.GooglePlayApiUpdate;
import com.github.yeriomin.playstoreapi.HttpClientAdapter;
import org.json.JSONArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.github.yeriomin.playstoreapi.GooglePlayApiUpdate.*;
import static com.github.yeriomin.playstoreapi.GooglePlayApiUpdate.GFTTU;


/**
 * Запрос(F)
 * Подтверждение
 *
 * @input * Ключи:
 * * gsfid - уникальный id смартфона
 * * email - почта
 * * oauth_token - токен из куков для запроса (E)
 * * cookie - куки ответа из запроса (E)
 * @return Возвращает HasHMap, размерность 2.
 * * Ключи:
 * * Token - токен для запроса (G)
 *
 *
 * * Auth - Окончательный токен для запросов.
 */

public class SendOAuth extends Requests {


    public SendOAuth(GooglePlayApiUpdate googlePlayApiUpdate, HashMap<String, String> input) {
        super(googlePlayApiUpdate, input);
    }

    @Override
    public HashMap<String, String> make() throws IOException {
        HashMap<String, String> output = new HashMap<>();

        Map<String, String> headers = getAuthHeaders();
        headers.put("app","com.google.android.gms");

        Map<String, String> body = getAuthBody();
        body.put("build_device",this.deviceInfoProvider.getProperties("Build.DEVICE"));
        body.put("build_brand",this.deviceInfoProvider.getProperties("Build.BRAND"));
        body.put("build_fingerprint",this.deviceInfoProvider.getProperties("Build.FINGERPRINT"));
        body.put("build_product",this.deviceInfoProvider.getProperties("Build.PRODUCT"));
        body.put("service","ac2dm");
        body.put("Token",input.get("oauth_token"));
        body.put("ACCESS_TOKEN","1");
        body.put("Email",input.get(EMAIL));
        body.put("get_accountid","1");
        body.put("device_country","ru");


        HashMap<String,byte[]> responseBytes = client.postUp(SEND_OAUTH_HOST, body, headers);

        String respince = new String(getBody(responseBytes));

        if (null != respince) {
            output.putAll(parsJSON(respince));
            return output;
        } else {
            throw new IOException("SendOAuth failed!");
        }
    }


    public HashMap<String, String> parsJSON(String input) {
        HashMap<String, String> output = new HashMap();
        String temp[] = input.split("\n");

        for (int i = 0; i < temp.length; i++) {
            String tmp2[] = temp[i].split("=", 2);
            output.put(tmp2[0], tmp2[1]);
        }
        return output;
    }
}
