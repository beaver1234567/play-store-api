package com.github.yeriomin.playstoreapi;

import okhttp3.Request;
import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class GooglePlayAPITest {

    public static final String EMAIL = "zoniakling9@gmail.com";
    public static final String PASSWORD = "wAIjrcVEFd3LCFCF";
    public static final String GSFID = "3d552543d30101e8";

    private GooglePlayApiUpdate api;

    @Before
    public void setUp() throws Exception {
        api = initApi();
    }

    @Test
    public void kek() throws Exception {
        String input = ")]}'\n" +
                "[[[\"gf.sicr\",null,null,2,null,null,null,null,null,[\"gf.sisb\",2,[11,\"https://accounts.google.com/speedbump/embeddedsigninconsent?continue\\\\u003dhttps%3A%2F%2Faccounts.google.com%2Fo%2Fandroid%2Fauth%3Flang%3Dru%26cc%26langCountry%3Dru_%26xoauth_display_name%3DAndroid%2BDevice%26source%3Dandroid%26tmpl%3Dnew_account%26return_user_id%3Dtrue\\\\u0026TL\\\\u003dAM3QAYYs_03ERxGDwkrEvFqNSOF7IuVFbf06EbkMLUpj0Tz45BEkLeswpUdZtcs-\"]\n" +
                "]\n" +
                "]\n" +
                ",[\"gf.ttu\",0,\"AM3QAYYs_03ERxGDwkrEvFqNSOF7IuVFbf06EbkMLUpj0Tz45BEkLeswpUdZtcs-\"]\n" +
                ",[\"e\",3,null,null,535]\n" +
                "]]";
        input = input.replace(")]}'", "");
        JSONArray reader = new JSONArray(input);
        JSONArray jsonArray = reader.getJSONArray(0).getJSONArray(0);
        System.out.println(jsonArray.getJSONArray(9).getJSONArray(2).get(0));
    }

    @Test
    public void google() throws Exception {
        GooglePlayApiUpdate googlePlayAPI = initApi();
        googlePlayAPI.getGOOGLEPLAY();
    }

    @Test
    public void dell() throws Exception {
        GooglePlayApiUpdate googlePlayAPI = initApi();
        googlePlayAPI.getTokenForVending();
    }

    @Test
    public void temp() throws Exception {
        GooglePlayApiUpdate googlePlayAPI = initApi();
        googlePlayAPI.generateToken(EMAIL,PASSWORD,GSFID);
    }

    private GooglePlayApiUpdate initApi() {
        Properties properties = new Properties();
        try {
            properties.load(ClassLoader.getSystemResourceAsStream("device2.properties"));
        } catch (IOException e) {
            System.out.println("device-irbis.properties not found");
            return null;
        }

        PropertiesDeviceInfoProvider deviceInfoProvider = new PropertiesDeviceInfoProvider();
        deviceInfoProvider.setProperties(properties);
        deviceInfoProvider.setLocaleString(Locale.ENGLISH.toString());
        deviceInfoProvider.setTimeToReport(1482626488L);
        GooglePlayApiUpdate api = new MockGooglePlayAPI();
        api.setClient(new MockOkHttpClientAdapter());
        api.setDeviceInfoProvider(deviceInfoProvider);

        return api;
    }
}
