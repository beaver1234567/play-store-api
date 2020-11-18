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

public class GetGsfidOld extends Requests {

    public GetGsfidOld(GooglePlayApiUpdate googlePlayApiUpdate) {
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

    private AndroidCheckinResponse checkin(byte[] request) throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("x-dfe-userlanguages", "ru_RU");
        headers.put("x-dfe-content-filters","");
        headers.put("x-dfe-network-type","4");
        headers.put("x-dfe-request-params","timeoutMs=30000");
        headers.put("x-dfe-encoded-targets","CAESpwJX6pSBBqYK0QJCAtgDAQEUkgeAAqQItQFYQJkBuwQykgHpCpgBugEyhgEvaPIC3QEn3AEW+wu4AwECzwWuA5oTNdEIvAHbELYBAaUDngMBLyjjC8MCowKtA7AC9AOvDbgC0wHfBlcBqgKbAssBUYMDF272AeUBTIgCGALlAQIUswEHYkJLYgHXCg2hBNwBQE4BYRP6AS1dMvMCogKAA80CtgGrBMgB3gQKwQGHAZMCYgnaAmwPiAJjMQEizQLmAYYCvgEB3QEOE7kBqgHEA9cCHAelAQHFAToBA/MBiQGOAQEH5QGWBANGAQYHCOsBygFXyQHlAQUcMbsCZ5sBlAKQAjjfAgElbI4KkwVwRYIBggc1kwE5KtAB1gN6jwU2RckBsQScAtENGqQHEQEBAQEBAskBHCvOAe0BAgMEawMEAS+A088CgruxAwEBAgMECQgJAQIIBAECAQYBAQQFBAgNBgIMAwMDAQ0BAQEFAQEBxgEBEgQEAg0mwQF9LwIcAQEKkAEMMxcBIQoUDwYHIjeEAQ4MFk0JWH8RERgBA4sBgQEUECMIEXBkEQ9fC6MBwAKEAQSIAYoBGRgLKxYWBQcBKmUCAiUocxQnLfQEMQ43GIUBjQG0AVlCjgEeMiQQJi9b1AFiA3cJAQowrgF5qgEMAyxkngEEgQF0UEXUAYoBzAIFBQnNAQQBRaABCDE4pwJgNS7OAQ1yqwEgiwM/+wImlwMeQ60ChAZ24wWCBAkE9gMWc5wBVW0BCTwB3gUgEA57VV6VAYYGKxjYAQEhAQcCIAgSHQemAzgBGkaEAQG7AnV3MBgBIgKjAhIBARgWD8YLHYABhwGEAsoBAQIBwwEn6wIBOQHbAVLnA0H1AsIBdQETKQSLAbIDSpsBBhI/RDgUK1VFU48CgwIKDgcvXBdSGrkBDvcBtwEqFAHSA98DlwEE6wGHAWIu0wEGExILWigkAQIChAW1AQ0GI1konwEyHhgBBCACVgEjApABHRIbJ36JAV0MD/0BIyYiBAEiKh6AAj8EGwMXIIoBUj2yAcoCCxixAiV+G1q7AQyIASV3iwGBAUcBKwU3AlQBYqQCITABDUUDngMdsQFxfxBmvQQL7AEHOIwBHgyNAwFxAQIVoAFragI6UQgCCYoEFBQCAwExMlMYAgPKAZkBOgEBBleEATumAgosyQEWWzZHiQEZOCYOXjIRNJ8BP0ZGvwIEKCZhERw/iQEcJVMGV5EBMgEKngLSAgQSTSUCjAGDARF1IDKQAgzKAQICAgcEAQQCBgQDBgUHBAIGBgQCBAIGBQICAgYEAwQe0wF+VTkhJB8oNgEBCCkBaTt0BAIEAQYEAwSyAbACJoQCBgcGBhUCKx0SBAoBbQYGAwICBjgIPg0JOGkbCJEBdw4NAz0uhAEGARGEAQ0hCAJE1wE8IcIBAYcBQQEJXR4eBgMWGitnKywePhcDRAgKjQEUPEsNXjk6BQcFBgcEAQYEAwVADiUEAQcGBgQfDIYBsgMpBTsCBQIKWSYHGv0BBQMJLg5YAiEJCk45FgIjCBUMIRoCJARXnAFCNwcEAQYGMFcbKnm5AhAJHgMKLy4ZBQMCAQIDAkMj1AEIqQMFBREJNheoAQurAQECJCGDARIyARFDBgYGBAM");
        headers.put("User-Agent",this.deviceInfoProvider.getAuthUserAgentString());
        headers.put("x-dfe-client-id","am-android-google");
        headers.put("accept-language","ru-RU");
        headers.put("content-type","application/x-protobuf");
        headers.put("accept-encoding","gzip");

        byte[] content = client.post(CHECKIN_URL, request, headers);
        return AndroidCheckinResponse.parseFrom(content);
    }
}
