package com.github.yeriomin.playstoreapi;

import okhttp3.Request;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class GooglePlayAPITest {

    public static final String EMAIL = "kar.gggg.kar@gmail.com";
    public static final String PASSWORD = "qweasdzxc24)";
    public static final String GSFID = "3f1abe856b0fa7fd";
    public static final String TOKEN = "jwSyrOU2RHDv2d82095MoHKOUHhO9KxBbkAoLCMkCKWqB9RUHbvq8VIWufBJcxwRn3_DGQ.";

    private GooglePlayApiUpdate api;

    @Before
    public void setUp() throws Exception {
        api = initApi();
    }


    @Test
    public void temp() throws Exception {
        GooglePlayApiUpdate googlePlayAPI = initApi();
        googlePlayAPI.generateToken(EMAIL,PASSWORD,GSFID);


    }

    private GooglePlayApiUpdate initApi() {
        Properties properties = new Properties();
        try {
            properties.load(ClassLoader.getSystemResourceAsStream("device-irbis.properties"));
        } catch (IOException e) {
            System.out.println("device-honami.properties not found");
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
