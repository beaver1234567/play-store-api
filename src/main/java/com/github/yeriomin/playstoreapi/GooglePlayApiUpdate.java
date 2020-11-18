package com.github.yeriomin.playstoreapi;

import com.github.yeriomin.playstoreapi.requests.*;
import com.github.yeriomin.playstoreapi.requests.temp.GetOPENGOOGLEPLAY;
import com.github.yeriomin.playstoreapi.requests.temp.GetOPENGOOGLEPLAY0;
import com.github.yeriomin.playstoreapi.requests.temp.GetOPENGOOGLEPLAY1;
import com.github.yeriomin.playstoreapi.requests.temp.GetOPENGOOGLEPLAY2;

import java.io.IOException;
import java.util.*;

public class GooglePlayApiUpdate {

    private DeviceInfoProvider deviceInfoProvider;

    private HttpClientAdapter client;


    /**
     * Константы которые можно заменить набором системных, пока ну времени их искать поэтому тут
     */
    private static final String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9";

    private static final String SCHEME = "https://";

    /**
     * Поля для авторизации
     */
    public static final String ACCOUNTS_HOST = "accounts.google.com";

    public static final String AUTH_HOST = "android.googleapis.com";

    public static final String GET_TEMP_HOST = SCHEME + ACCOUNTS_HOST + "/_/kids/signup/eligible?hl=ru&_reqid=39693&rt=j";

    public static final String GET_HTML_HOST = SCHEME + ACCOUNTS_HOST + "/embedded/setup/android?source=com.android.vending&xoauth_display_name=Android%20Phone&canFrp=1&canSk=1&lang=ru&langCountry=ru_ru&hl=ru-RU&cc=ru&use_native_navigation=0";

    public static final String SEND_EMAIL_HOST = SCHEME + ACCOUNTS_HOST + "/_/lookup/accountlookup";

    public static final String SEND_PASS_HOST = SCHEME + ACCOUNTS_HOST + "/_/signin/challenge";

    public static final String SEND_CONFIRM_HOST = SCHEME + ACCOUNTS_HOST + "/_/signin/speedbump/embeddedsigninconsent";

    public static final String SEND_OAUTH_HOST = SCHEME + AUTH_HOST + "/auth";

    public static final String CHECKIN_URL = SCHEME + AUTH_HOST + "/checkin";

    public static final String GET_GOOGLE_PLAY0 = "https://android.clients.google.com/fdfe/selfUpdate?ex=1&susp=EAEYASAB&nocache_qos=lt";
    public static final String GET_GOOGLE_PLAY1 = "https://android.clients.google.com/fdfe/deviceSettings";
    public static final String GET_GOOGLE_PLAY2 = "https://android.clients.google.com/fdfe/toc?nocache_isui=true";


    public static final String GET_GOOGLE_PLAY = "https://android.clients.google.com/fdfe/subnavHome?c=3&nocache_isui=true&nocache_pwr=true";



    /**
     * Ключи для map
     */
    public static final String COOKIE = "cookie";
    public static final String AZT = "azt";
    public static final String FREQ = "freq";
    public static final String GFTTU = "gf.ttu";
    public static final String ARDT = "ardt";

    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String GSFID = "gsfid";


    /**
     * Версия sdk с которой был реверснут логин.
     * Думаю что лучше еге, не брать из properties
     */
    public static final String VERSIONSDK = "24";

    /**
     * Это Google Services Framework
     * также в запросах иногда называется gmscoreversion
     */
    public static final String GSF = "11975434"; //203915015

    /**
     * Русский язык в запросах, не использую его в accept-language
     */
    public static final String ru = "ru";
    public static final String RU = "RU";
    public static final String RU_RU = "ru-RU";

    public static final String bgRequest = "ru-RU";

    public GooglePlayApiUpdate() {

    }

    /**
     * Authenticates
     */
    public String getGOOGLEPLAY() throws IOException {

        String email = "svetatrena@gmail.com";
        String password = "asdfghjkl1455";
        String gsfid = "3dc11bda753b6e73";

        HashMap<String, String> main = new HashMap<String, String> ();
        main.put(EMAIL, email);
        main.put(PASSWORD, password);
        main.put(GSFID, gsfid);

        //main.putAll(new GetOPENGOOGLEPLAY0(this,main).make());
        //main.putAll(new GetOPENGOOGLEPLAY1(this,main).make());
        //main.putAll(new GetOPENGOOGLEPLAY2(this,main).make());
        main.putAll(new GetOPENGOOGLEPLAY(this,main).make());

        return "";
    }

    /**
     * Authenticates
     */
    public String getTokenForVending() throws IOException {

        String email = "svetatrena@gmail.com";
        String password = "asdfghjkl1455";
        String gsfid = "3dc11bda753b6e73";

        HashMap<String, String> main = new HashMap<String, String> ();
        main.put(EMAIL, email);
        main.put(PASSWORD, password);
        main.put(GSFID, gsfid);

        main.put("Token","aas_et/AKppINbQi1Cj3RWeYfNx1Ld-lOwUdYKpSFBWJzrtt6to_TxyLdtwCtdhkJ0TvRc7RXlRjzMf-qz0M0CdHqNKCLz2OSX-bLRgrBcR__soZ9o31k97_OWCoBEkGqNw--MzloPemNiErIhWP1-MBcOq0DO4TSNbrB5fKUd0AqNDHfpKeb2a_fIQiPCBJqkhFSyC5jPPGLsW7HCvmWRNjA1qJ68=");

        main.putAll(new SendOAuthLast(this,main).make());

        return "";
    }

    /**
     * Authenticates
     */
    public String generateToken(String email, String password, String gsfid) throws IOException {

        HashMap<String, String> main = new HashMap<String, String> ();
        main.put(EMAIL, email);
        main.put(PASSWORD, password);

        if(!gsfid.equals("")){
            main.put(GSFID, gsfid);
        }else {
            main.putAll(new GetGsfidOld(this).make());
        }


        main.putAll(new GetHtml(this).make());
        main.putAll(new GetCookie(this,main).make());
        main.putAll(new SendEmail(this,main).make());
        main.putAll(new SendPass(this,main).make());
        main.putAll(new SendConfirm(this,main).make());
        main.putAll(new SendOAuth(this,main).make());
        main.putAll(new SendOAuthLast(this,main).make());

        return "";
    }



    public void setDeviceInfoProvider(DeviceInfoProvider deviceInfoProvider) {
        this.deviceInfoProvider = deviceInfoProvider;
    }

    public DeviceInfoProvider getDeviceInfoProvider() {
        return deviceInfoProvider;
    }

    public void setClient(HttpClientAdapter httpClient) {
        this.client = httpClient;
    }

    public HttpClientAdapter getClient() {
        return client;
    }


}
