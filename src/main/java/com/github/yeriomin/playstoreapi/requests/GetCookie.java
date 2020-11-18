package com.github.yeriomin.playstoreapi.requests;

import com.github.yeriomin.playstoreapi.AuthException;
import com.github.yeriomin.playstoreapi.GooglePlayApiUpdate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.github.yeriomin.playstoreapi.GooglePlayApiUpdate.*;

public class GetCookie extends Requests {

    public GetCookie(GooglePlayApiUpdate googlePlayApiUpdate, HashMap<String, String> input) {
        super(googlePlayApiUpdate, input);
    }

    public HashMap<String, String> make() throws IOException {
        HashMap<String, String> output = new HashMap<>();

        Map<String, String> headers = getDefaultHeaders();
        headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        StringBuilder stringBuilder = new StringBuilder();
        input.forEach((x, y) -> {
            if (x.contains(COOKIE)) {
                stringBuilder.append(x.replaceFirst(COOKIE, "") +"="+ y).append(";");
            }
        });
        headers.put(COOKIE, stringBuilder.toString());

        Map<String, String> body = new HashMap<>();

        body.put("hl","ru-RU");
        body.put("source","com.android.vending");
        body.put("continue","https://accounts.google.com/o/android/auth?lang=ru&cc&langCountry=ru_&xoauth_display_name=Android+Device&source=android&tmpl=new_account&return_user_id=true");
        body.put("f.req","[\""+input.get(FREQ)+"\"]");
        body.put(AZT,input.get(AZT));
        body.put("cookiesDisabled","false");
        body.put("deviceinfo","[\""+input.get(GSFID)+"\","+VERSIONSDK+","+deviceInfoProvider.getPlayServicesVersion() +",[],true,\""+RU+"\",0,null,[],\"EmbeddedSetupAndroid\",null,[0,null,[],null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,7,null,null,[],null,null,null,[],[]],1,null,null,null,2,null,false,2]");
        body.put("gmscoreversion",Integer.toString(deviceInfoProvider.getPlayServicesVersion()));


        HashMap<String,byte[]> responseBytes = client.postUp(GET_TEMP_HOST, body, headers);
        String respince = new String(getBody(responseBytes));

        if (null != respince) {
            output.putAll(cookie(getHeader(responseBytes)));
            return output;
        } else {
            throw new IOException("SendEmail failed!");
        }
    }
}
