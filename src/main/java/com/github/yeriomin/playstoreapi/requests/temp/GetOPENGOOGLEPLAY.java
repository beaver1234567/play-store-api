package com.github.yeriomin.playstoreapi.requests.temp;

import com.github.yeriomin.playstoreapi.GooglePlayApiUpdate;
import com.github.yeriomin.playstoreapi.requests.Requests;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.github.yeriomin.playstoreapi.GooglePlayApiUpdate.*;
import static com.github.yeriomin.playstoreapi.GooglePlayApiUpdate.GET_TEMP_HOST;

public class GetOPENGOOGLEPLAY extends Requests {

    public GetOPENGOOGLEPLAY(GooglePlayApiUpdate googlePlayApiUpdate, HashMap<String, String> input) {
        super(googlePlayApiUpdate, input);
    }

    @Override
    public HashMap<String, String> make() throws IOException {
        HashMap<String, String> output = new HashMap<>();

        Map<String, String> headers = new HashMap<>();
        headers.put("x-ad-id", "13939299-78ca-4201-9124-df385e3c12ce");
        headers.put("x-dfe-content-filters", "");
        headers.put("x-dfe-network-type", "4");
        headers.put("x-dfe-device-config-token", "unknown");
        headers.put("user-agent", "Android-Finsky/21.3.14-16+%5B0%5D+%5BPR%5D+325140660 (api=3,versionCode=82131400,sdk=24,device=Q402Plus,hardware=sp9832a_2h11,product=Q402Plus_RU,platformVersionRelease=9,model=Micromax+Q402%2B,buildId=PKQ1.180917.001,isWideScreen=0,supportedAbis=armeabi-v7a;armeabi)");
        headers.put("x-dfe-client-id", "am-android-xiaomi");
        headers.put("x-limit-ad-tracking-enabled", "false");
        headers.put("x-dfe-device-id", "3d21d2f2e087e598");
        headers.put("x-dfe-request-params", "timeoutMs=4000");
        headers.put("accept-language", "en-US");
        headers.put("x-dfe-userlanguages", "en_US,ru_BY");
        headers.put("authorization", "Bearer ya29.a0AfH6SMC37NtAXoIGupBHafJyOKGE5s1oZgBVxPnKZDrl0lkoX80CCOtpJFxvR5ppbZmjVBWD6vtol7A9M82Ldi4Wu9DTi24YsgUP-DAb4SJ67vG2YsqLX3V0a2OdiehPuK8_JUXmEfM_2KvIuR2c5u-Cb8yriIYD54dKUXPRHW56lqSmC79PWKeJhJ9en-9U9kMzH82_vSbfKKKz8Mlke6gNa0lgt22J0G1yp5mncx65X0aC3uYVW5WKdv89VgOnnSbENv7KIsQusSrD6FCx_kTI4shMxQJ92kbGPKULul5Ka3QGNgok85X_HpErIuBjuKR7x5Q");
        headers.put("accept-encoding", "gzip, deflate, br");


        Map<String, String> body = new HashMap<>();

        HashMap<String,byte[]> responseBytes = client.getUp(GET_GOOGLE_PLAY, body, headers);
        String respince = new String(getBody(responseBytes));

        if (null != respince) {
            output.putAll(cookie(getHeader(responseBytes)));
            return output;
        } else {
            throw new IOException("SendEmail failed!");
        }
    }
}
