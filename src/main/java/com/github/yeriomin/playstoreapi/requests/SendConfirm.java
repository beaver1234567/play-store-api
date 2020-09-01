package com.github.yeriomin.playstoreapi.requests;

import com.github.yeriomin.playstoreapi.DeviceInfoProvider;
import com.github.yeriomin.playstoreapi.GooglePlayApiUpdate;
import com.github.yeriomin.playstoreapi.HttpClientAdapter;
import org.json.JSONArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.github.yeriomin.playstoreapi.GooglePlayApiUpdate.*;
import static com.github.yeriomin.playstoreapi.GooglePlayApiUpdate.AZT;

/**
 * Запрос(E)
 * Подтверждение
 *
 * @input * Ключи:
 * * gsfid - уникальный id смартфона
 * * freq - массив из запроса (A)
 * * ardt - массив из запроса (A)
 * * gf.ttu - массои из запроса (D)
 * * cookie - куки ответа из запроса (D)
 * @return Возвращает HasHMap, размерность 3.
 * * Ключи:
 * * cookie - куки ответа
 * * user_id - id из куков для запроса (G)
 * * oauth_token - токен из куков для запроса (F)
 */

public class SendConfirm extends Requests {

    public SendConfirm(GooglePlayApiUpdate googlePlayApiUpdate, HashMap<String, String> input) {
        super(googlePlayApiUpdate, input);
    }

    public HashMap<String, String> make() throws IOException {
        HashMap<String, String> output = new HashMap<>();

        Map<String, String> headers = getDefaultHeaders();
        StringBuilder stringBuilder = new StringBuilder();
        input.forEach((x, y) -> {
            if (x.contains(COOKIE)) {
                stringBuilder.append(x.replaceFirst(COOKIE, "") +"="+ y).append(";");
            }
        });
        headers.put(COOKIE, stringBuilder.toString());

        Map<String, String> body = new HashMap<>();

        body.put("source","com.android.vending");
        body.put("continue","https://accounts.google.com/o/android/auth?lang=ru&cc&langCountry=ru_&xoauth_display_name=Android+Device&source=android&tmpl=new_account&return_user_id=true");
        body.put("f.req","[\"gf.siesic\",1,\""+RU+"\",\""+ru+"\",\"default\",[\"X0FfAjryY6A=\",\"UpmUvzm8N0Y=\",\"R0MVauKaZ8Y=\",\"F7TABF8fS3E=\"]]");
        body.put(AZT,input.get(AZT));
        body.put("cookiesDisabled","false");
        body.put("deviceinfo","[\""+input.get(GSFID)+"\","+VERSIONSDK+","+deviceInfoProvider.getPlayServicesVersion() +",[],true,\""+RU+"\",0,null,[],\"EmbeddedSetupAndroid\",null,[0,null,[],null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,7,null,null,[],null,null,null,[],[]],1,null,null,null,2,null,false,2]");
        body.put("gmscoreversion",Integer.toString(deviceInfoProvider.getPlayServicesVersion()));
        HashMap<String,byte[]> responseBytes = client.postUp(
                SEND_CONFIRM_HOST + "?ardt="+input.get(ARDT)+"%3D&hl="+ru+"&TL="+input.get(GFTTU)+"&_reqid="+getReqid()+"&rt=j", body, headers);

        String respince = new String(getBody(responseBytes));

        if (null != respince) {
            output.putAll(cookie(getHeader(responseBytes)));
            return output;
        } else {
            throw new IOException("SendConfirm failed!");
        }
    }

    /**
     * Достаем значение gf.ttu
     * @param input
     * @return
     */
    public HashMap<String, String> parsJSON(String input){
        input = input.replace(")]}'", "");
        HashMap<String,String> output = new HashMap<>();
        JSONArray reader = new JSONArray(input);

        String gfttu = reader.getJSONArray(0).getJSONArray(1).getString(2);
        output.put(GFTTU,gfttu);

        return output;
    }
}
