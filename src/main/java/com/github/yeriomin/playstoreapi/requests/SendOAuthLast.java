package com.github.yeriomin.playstoreapi.requests;

import com.github.yeriomin.playstoreapi.GooglePlayApiUpdate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.github.yeriomin.playstoreapi.GooglePlayApiUpdate.EMAIL;
import static com.github.yeriomin.playstoreapi.GooglePlayApiUpdate.SEND_OAUTH_HOST;


/**
 * Запрос(G)
 * Подтверждение
 *
 * @input * Ключи:
 * * gsfid - уникальный id смартфона
 * * email - почта
 * * Token - токен  для запроса (G)
 * @return Возвращает HasHMap, размерность 3.
 * * Ключи:
 * * Auth - Окончательный токен для запросов.
 */

public class SendOAuthLast extends Requests {


    public SendOAuthLast(GooglePlayApiUpdate googlePlayApiUpdate, HashMap<String, String> input) {
        super(googlePlayApiUpdate, input);
    }

    @Override
    public HashMap<String, String> make() throws IOException {
        HashMap<String, String> output = new HashMap<>();

        Map<String, String> headers = getAuthHeaders();
        headers.put("app", "com.android.vending");

        Map<String, String> body = getAuthBody();
        body.put("is_called_from_account_manager","1");
        body.put("oauth2_foreground","1");
        body.put("service","oauth2:https://www.googleapis.com/auth/googleplay");
        body.put("build_product",this.deviceInfoProvider.getProperties("Build.PRODUCT"));
        body.put("app","com.android.vending");
        body.put("_opt_is_called_from_account_manager","1");
        body.put("get_accountid","1");
        body.put("Token",input.get("Token"));
        body.put("Email",input.get(EMAIL));
        body.put("droidguardPeriodicUpdate","1");
        body.put("system_partition","1");
        body.put("token_request_options","CAA4AVAB");
        body.put("check_email","1");
        body.put("client_sig","38918a453d07199354f8b19af05ec6562ced5788");


        HashMap<String,byte[]> responseBytes = client.postUp(SEND_OAUTH_HOST, body, headers);

        String respince = new String(getBody(responseBytes));

        if (null != respince) {
            output.putAll(parsJSON(respince));
            return output;
        } else {
            throw new IOException("SendOAuhtLas failed!");
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
