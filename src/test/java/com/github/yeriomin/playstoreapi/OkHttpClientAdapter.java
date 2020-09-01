package com.github.yeriomin.playstoreapi;

import okhttp3.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Authenticator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

class OkHttpClientAdapter extends HttpClientAdapter {

    OkHttpClient client;

    public OkHttpClientAdapter() {

        try {

            /*
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("yLIRMJ0hBN", "EHwb6oyOkd".toCharArray());
                }
            });

            Proxy proxy= new Proxy(Proxy.Type.SOCKS,new InetSocketAddress("176.53.172.115", 45488));*/
            Proxy proxy= new Proxy(Proxy.Type.HTTP,new InetSocketAddress(InetAddress.getLocalHost(), 8080));
            client = new OkHttpClient.Builder().proxy(proxy).addInterceptor(new UnzippingInterceptor()).build();
        }
        catch (Exception e){
            System.err.println("Error while creating OkHttpClientAdapter object");
            e.printStackTrace();
        }
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public byte[] getEx(String url, Map<String, List<String>> params, Map<String, String> headers) throws IOException {
        return request(new Request.Builder().url(buildUrlEx(url, params)).get(), headers);
    }

    @Override
    public byte[] get(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        return request(new Request.Builder().url(buildUrl(url, params)).get(), headers);
    }

    @Override
    public HashMap<String, byte[]> getUp(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        return requestUp(new Request.Builder().url(buildUrl(url, params)).get(), headers);
    }

    @Override
    public byte[] postWithoutBody(String url, Map<String, String> urlParams, Map<String, String> headers) throws IOException {
        return post(buildUrl(url, urlParams), new HashMap<String, String>(), headers);
    }

    @Override
    public byte[] post(String url, Map<String, String> params, Map<String, String> headers) throws IOException {


        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (null != params && !params.isEmpty()) {
            for (String name: params.keySet()) {
                bodyBuilder.add(name, params.get(name));
            }
        }

        Request.Builder requestBuilder = new Request.Builder()
            .url(url)
            .post(bodyBuilder.build());

        return post(url, requestBuilder, headers);
    }

    @Override
    public byte[] post(String url, byte[] body, Map<String, String> headers) throws IOException {

        Request.Builder requestBuilder = new Request.Builder()
            .url(url)
            .post(RequestBody.create(MediaType.parse("application/x-protobuf"), body));

        return post(url, requestBuilder, headers);
    }

    byte[] post(String url, Request.Builder requestBuilder, Map<String, String> headers) throws IOException {
        requestBuilder.url(url);
        return request(requestBuilder, headers);
    }

    HashMap<String,byte[]> postUp(String url, Request.Builder requestBuilder, Map<String, String> headers) throws IOException {
        requestBuilder.url(url);
        return requestUp(requestBuilder, headers);
    }

    @Override
    public HashMap<String,byte[]> postUp(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (null != params && !params.isEmpty()) {
            for (String name: params.keySet()) {
                bodyBuilder.add(name, params.get(name));
            }
        }

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(bodyBuilder.build());

        return postUp(url, requestBuilder, headers);
    }

    byte[] request(Request.Builder requestBuilder, Map<String, String> headers) throws IOException {
        Request request = requestBuilder
            .headers(Headers.of(headers))
            .build();
        System.out.println("Requesting: " + request.url().toString());

        Response response = client.newCall(request).execute();

        int code = response.code();
        byte[] content = response.body().bytes();

        if (code >= 400) {
            GooglePlayException e = new GooglePlayException("Malformed request", code);
            if (code == 401 || code == 403) {
                e = new AuthException("Auth error", code);
                Map<String, String> authResponse = GooglePlayAPI.parseResponse(new String(content));
                if (authResponse.containsKey("Error") && authResponse.get("Error").equals("NeedsBrowser")) {
                    ((AuthException) e).setTwoFactorUrl(authResponse.get("Url"));
                }
            } else if (code >= 500) {
                e = new GooglePlayException("Server error", code);
            }
            e.setRawResponse(content);
            throw e;
        }

        return content;
    }

    HashMap<String, byte[]> requestUp(Request.Builder requestBuilder, Map<String, String> headers) throws IOException {
        Request request = requestBuilder
                .headers(Headers.of(headers))
                .build();
        System.out.println("Requesting: " + request.url().toString());

        Response response = client.newCall(request).execute();

        int code = response.code();
        byte[] content = response.body().bytes();

        if (code >= 400) {
            GooglePlayException e = new GooglePlayException("Malformed request", code);
            if (code == 401 || code == 403) {
                e = new AuthException("Auth error", code);
                Map<String, String> authResponse = GooglePlayAPI.parseResponse(new String(content));
                if (authResponse.containsKey("Error") && authResponse.get("Error").equals("NeedsBrowser")) {
                    ((AuthException) e).setTwoFactorUrl(authResponse.get("Url"));
                }
            } else if (code >= 500) {
                e = new GooglePlayException("Server error", code);
            }
            e.setRawResponse(content);
            throw e;
        }

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(response.headers().toMultimap());

        HashMap<String,byte[]> hashMap = new HashMap();
        hashMap.put("body", content);
        hashMap.put("header", byteOut.toByteArray());

        return hashMap;
    }

    public String buildUrl(String url, Map<String, String> params) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (null != params && !params.isEmpty()) {
            for (String name: params.keySet()) {
                urlBuilder.addQueryParameter(name, params.get(name));
            }
        }
        return urlBuilder.build().toString();
    }

    public String buildUrlEx(String url, Map<String, List<String>> params) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (null != params && !params.isEmpty()) {
            for (String name: params.keySet()) {
                for (String value: params.get(name)) {
                    urlBuilder.addQueryParameter(name, value);
                }
            }
        }
        return urlBuilder.build().toString();
    }
}
