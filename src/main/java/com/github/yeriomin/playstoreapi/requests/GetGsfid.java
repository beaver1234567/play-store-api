package com.github.yeriomin.playstoreapi.requests;

import com.github.yeriomin.playstoreapi.AndroidCheckinRequest;
import com.github.yeriomin.playstoreapi.AndroidCheckinResponse;
import com.github.yeriomin.playstoreapi.GooglePlayApiUpdate;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static com.github.yeriomin.playstoreapi.GooglePlayApiUpdate.CHECKIN_URL;
import static com.github.yeriomin.playstoreapi.GooglePlayApiUpdate.GSFID;

public class GetGsfid extends Requests {

    public GetGsfid(GooglePlayApiUpdate googlePlayApiUpdate) {
        super(googlePlayApiUpdate);
    }

    @Override
    public HashMap<String, String> make() throws IOException {
        HashMap<String, String> output = new HashMap<>();
        // this first checkin is for generating gsf id
        AndroidCheckinRequest request = this.deviceInfoProvider.generateAndroidCheckinRequest();
        AndroidCheckinResponse checkinResponse1 = checkin(request.toByteArray());

        String gsfId = BigInteger.valueOf(checkinResponse1.getAndroidId()).toString(16);
        output.put(GSFID,gsfId);

        return output;
    }

    /**
     * Posts given check-in request content and returns
     * {@link AndroidCheckinResponse}.
     */
    private AndroidCheckinResponse checkin(byte[] request) throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-protobuffer");;
        headers.put("Content-encoding","gzip");
        headers.put("Accept-encoding","gzip");
        headers.put("User-Agent",this.deviceInfoProvider.getAuthUserAgentString());

        byte[] content = client.post(CHECKIN_URL, request, headers);
        return AndroidCheckinResponse.parseFrom(content);
    }
}
