package com.github.yeriomin.playstoreapi.requests;

import com.github.yeriomin.playstoreapi.DeviceInfoProvider;
import com.github.yeriomin.playstoreapi.GooglePlayApiUpdate;
import com.github.yeriomin.playstoreapi.HttpClientAdapter;
import org.json.JSONArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.github.yeriomin.playstoreapi.GooglePlayApiUpdate.*;


/**
 * Запрос(С)
 * отправляем email
 * @input
 *      * Ключи:
 *      * email - адрес
 *      * freq - массив из запроса (A)
 *      * azt - массив из запроса (A)
 *      * cookie - куки ответа из запроса (A)
 *
 * @return Возвращает HasHMap, размерность 4.
 *      * Ключи:
 *      * cookie - куки ответа
 *      * gf.alr - массив который используется в следующийх запросах под именем f.req
 *      * gf.ttu - токен для запроса D
 *      * status -
 *      1 = правильный адрес почты
 *      2 = error
 *      5 = капча
 *      7 = не правильный адрес почты
 *      9 = аккаунт удален
 *      12 = Multiple Found
 *      13 = Login Redirect
 */

public class SendEmail extends Requests {

    public SendEmail(GooglePlayApiUpdate googlePlayApiUpdate, HashMap<String, String> input) {
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

        body.put("source","com.android.vending");
        body.put("continue","https://accounts.google.com/o/android/auth?lang=ru&cc&langCountry=ru_&xoauth_display_name=Android+Device&source=android&tmpl=new_account&return_user_id=true");
        body.put("f.req","[\""+ input.get(EMAIL) +"\",\"" + input.get(FREQ) +"\",[],null,\""+RU+"\",null,null,2,true,true,[null,null,[1,1,0,1,\"https://accounts.google.com/EmbeddedSetup\",null,[],4,[],\"EmbeddedSetupAndroid\",null,[]],2,[0,null,[],null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,7,null,null,[],null,null,null,[],[]],null,null,null,false],\""+ input.get(EMAIL) +"\",null,null,null,true,true,[]]");
        body.put("bgRequest","[\"identifier\",\"!FxSlFDVC4Y-tmGRKjj5YKmkRW6653rYCAAAFZ1IAAABiCgEBT4bpN81zXAx_1rahHUUN2qS3JVWeWmlmw9MIxyFGBUw9rgDsGPX68Kewk8rQygUcHluyBC2Q-0Kdw8wnnmGlUq8083cQEFgabu8IEKxsP8tV5b-fmAudf1JauVmypz_FgjGKmZyJSpvz4IY1z4lTAa1KkWtSCGkJgWu7salAJOalUCb-fTWaq-JxupzwLa_SxhEVGfLePDeR_hzOtlrnvPRTFH7BgUrip2XsH9HfYgG0ffSs-a-sarRolm1ffOaijT9H2KJO09W2DyCcWTXfPcT9ybpboiTKWSXNggPzzi-3jGP32GS01YwpVgIsdI_uMS8qS-cmaj_-OihIc03LqyqZAessyyXX_jw5-fyXUIyzPMrwhm6MWIzVuaZcTmNJ7Cjrd5hvUrl_1lTQertBPBPAY8asLhbvIOBxBr4A5n6K7A-kKibFtx81xNk7GwBRZQdykiZdkSR22axu7NBQQr2iwPSAp37TuNE9zqI09o8khJc0YC7vPYgGJ8Co3zvF-g8MTp6OsLV4vuC6J26pxK36bgGBjaM6YHivF8UbWsN4XlAV40iOs9MfTKzRJ7UsTQSEEwVGvFp4MySYnxmwCw5MErcxdbICvr26iv2Y0b-WS-CUBFytKcxPyu-L0fnI0O6AqzQG7JIX_-WgERKo25l1Qtg9nwxH4oOgVxw74pTqGR27eYWCq2it7cJfdTa2sK0K35JhN27U3YHvA1Ahd3WBW8OxkJqYNkJnX8V2KGh-cDekbYT4EDIhH21irc-O57gbHjCua_17V37vjgEJxm8EjLN5O7Todc2HnzXg8yHU8pkHqCGaBw18MsAWmo-hhASSmBNrQjsqVl9gUmVtDuKmdiiEkDjfG9PfwN72eu5XDWjahx_s6edLPL6bQzgCYHlfOcqrs1HutgLsku4tDCoLFV5jiqjJiz2GO7CSqVCTyjyg-TUv63GXOJXiJeKX5oi-g1rw8VeMOGoqBaHp0AtWDcyH_9f5eVCSzhd9HA\"]");
        body.put(AZT,input.get(AZT));
        body.put("cookiesDisabled","false");
        body.put("deviceinfo","[\""+input.get(GSFID)+"\","+VERSIONSDK+","+deviceInfoProvider.getPlayServicesVersion() +",[],true,\""+RU+"\",0,null,[],\"EmbeddedSetupAndroid\",null,[0,null,[],null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,7,null,null,[],null,null,null,[],[]],1,null,null,null,2,null,false,2]");
        body.put("gmscoreversion",Integer.toString(deviceInfoProvider.getPlayServicesVersion()));


        HashMap<String,byte[]> responseBytes = client.postUp(SEND_EMAIL_HOST + "?hl=ru&_reqid="+getReqid()+"&rt=j", body, headers);
        String respince = new String(getBody(responseBytes));

        if (null != respince) {
            output.putAll(cookie(getHeader(responseBytes)));
            output.putAll(parsJSON(respince));
            return output;
        } else {
            throw new IOException("SendEmail failed!");
        }
    }


    /**
     * Достаем значение gf.alr и gf.ttu
     * @param input
     * @return
     */
    private HashMap<String, String> parsJSON(String input) throws IOException {
        input = input.replace(")]}'", "");
        HashMap<String,String> output = new HashMap<>();
        JSONArray reader = new JSONArray(input);
        JSONArray jsonArray = reader.getJSONArray(0).getJSONArray(0);
        /**
         * https://www.blackhillsinfosec.com/g-suite-is-the-soft-underbelly-of-your-environment/
         *
         * 5 = капча
         * 1 = правильный адрес почты
         * 7 = не правильный адрес почты
         *
         */
        int status = jsonArray.getInt(1);
        //case 5: throw new IOException("CAPSHA");


        switch(status) {
            case 1:
                System.out.println(" mailing address is correct");
                break;
            case 5:
                throw new IOException("captcha");
            case 7:
                throw new IOException("Incorrect email address");
            default:
                throw new IOException("EMAIL ERROR");
        }


        String freq = jsonArray.getString(2);
        /**
         * Часть для следующего url запроса
         */
        String gfttu = reader.getJSONArray(0).getJSONArray(1).getString(2);

        output.put(FREQ,freq);
        output.put("status",Integer.toString(status));
        output.put(GFTTU,gfttu);

        return output;
    }

}
