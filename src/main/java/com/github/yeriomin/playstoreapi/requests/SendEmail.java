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

        /**
         * bgRequest Данный параметр можно получить из за запроса С.
         */
        body.put("bgRequest", "[\"identifier\",\"!TE-lT2bNAAUrmx8XK0LFC9zpFlVkC1hMC81leyHyQgIAAASqUgAAAXJoAQcKAI1lu-yfaTDGrYQRpQEFIoFGOA-WYj50Qnj7xW54agRhv4gQmVxpwV1jCAlwbFO7UyJztKYl7i4CGssmjODKVp95MRzfVyiKRd2i72TzG4qaCcvuGb5CWNBHba6apajnDVz-7tOyFBYgLASg_WM0jOpmhezMR6zwXkykQzlxGJmmpGJ4YxW4weHl8y9W4cGZAb5CJ7YoCTsbjvFJjTKczb8oB_N_pYjlKYiDIYxEuh85g1z6iJmzmGQBXkrJhr0MI2dBxuOY5lOMVrlAxQZ_-YqEE44PBTxcYiway7etN80V40DL1cROx-ekSSphBGI3aeXettHLNucOXE7kvVUDGt8yAG7Ptgo3frQnmVUHmPa8UbQcdkD3W2AR09frAujJNYsbxM52Kja1uIJ24eQAPpXZMY0fCCxBoZGZU_5gQQQOAMzjFaysWLaIsZJFliE9KnhYx-NglPAD2VLc4tAr9AwhcMKSQ0yL0Xb7vCa-xlgWC8saa67V6HV70VBR2nQ5qQDPHW2dO8uP08SMNOobAWbb06MHy84babArnP9weVoXB_ACjTEkVSfaoyN1jzi_X22bZUGNG1-R5KhAHFKv4QJbaPfv8tL4UiNidjf5h3gSV9JdMcIk-P1ftbQaf0zBBs4bHpAA0A8Vr-Cdhn82aCBHn2N93HLZ5mFZVBYa_DNdMGTgrm2lPLkGGM6_v0redDA-1Pmkw_7NsgpWPEFFtDfhYuft3ItuP-N-cLVo6iHoMPONnr9z3M3pOEbpEx2EHTEO6xq7EA3f76UIbhSE3g\"]");


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
                System.out.println("mailing address is correct");
                break;
            case 5:
                throw new IOException("captcha");
            case 7:
                throw new IOException("Incorrect email address");
            case 16:
                throw new IOException("EMAIL ERROR");
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
