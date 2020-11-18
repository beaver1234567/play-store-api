package com.github.yeriomin.playstoreapi.requests.temp;

import com.github.yeriomin.playstoreapi.GooglePlayApiUpdate;
import com.github.yeriomin.playstoreapi.requests.Requests;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.github.yeriomin.playstoreapi.GooglePlayApiUpdate.*;
import static com.github.yeriomin.playstoreapi.GooglePlayApiUpdate.GET_TEMP_HOST;

public class GetOPENGOOGLEPLAY2 extends Requests {

    public GetOPENGOOGLEPLAY2(GooglePlayApiUpdate googlePlayApiUpdate, HashMap<String, String> input) {
        super(googlePlayApiUpdate, input);
    }

    @Override
    public HashMap<String, String> make() throws IOException {
        HashMap<String, String> output = new HashMap<>();

        Map<String, String> headers = new HashMap<>();
        headers.put("x-dfe-content-filters", "");
        headers.put("x-dfe-network-type", "4");
        headers.put("x-dfe-device-config-token", "unknown");
        headers.put("user-agent", "Android-Finsky/22.3.26-21%20%5B0%5D%20%5BPR%5D%20337322549 (api=3,versionCode=82232610,sdk=24,device=S0320WW,hardware=sp9832a_2h11,product=BLU_S1,platformVersionRelease=7.0,model=BLU_S1,buildId=NRD90M,isWideScreen=0,supportedAbis=armeabi-v7a;armeabi)");
        headers.put("x-dfe-client-id", "am-android-micromax");
        headers.put("x-limit-ad-tracking-enabled", "false");
        headers.put("x-dfe-device-id", "3d21d2f2e087e598");
        headers.put("x-dfe-request-params", "timeoutMs=4000");
        headers.put("accept-language", "ru-RU");
        headers.put("authorization", "Bearer ya29.a0AfH6SMBIT4qbAkPgLzHZB1a61SU2VWWf2Ep15OGzxYI-MxVW5c0uUr3WYb5zym1fFVHddUfqrr3FxviZwqR8M5HAG5nXSzOg8Acq3aZIdE1BBBJFn4P1gr9pM-Ofra0k22XGLnzq85zCF_9Y1kkVdHwzbvQZB85G885VZr7ZGnnMeTW1PBBoTbJvDAuPXSyVPwTo3iaLGOa35S4DlBf6IOZq-5HtkYswxZhQEq4C6wI3VrKspAXInDBnmerbG8A-ASJd24uAtDy8kaYtYZEOVfLKvWSOGy_0BkOMz0nGBw9yE9Ym0mgpniG1jwO1mCJVeKbIHoY");
        headers.put("accept-encoding", "gzip, deflate, br");


        Map<String, String> body = new HashMap<>();

        HashMap<String,byte[]> responseBytes = client.getUp(GET_GOOGLE_PLAY2, body, headers);
        String respince = new String(getBody(responseBytes));
        System.out.println(respince);
        return output;
    }
}