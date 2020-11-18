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
 * Запрос(D)
 * Отправляем пароль
 *
 * @input * Ключи:
 * * password - пароль
 * * gsfid - уникальный id смартфона
 * * freq - массив из запроса (A)
 * * azt - массив из запроса (A)
 * * gf.ttu - массои из запроса (С)
 * * cookie - куки ответа из запроса (C)
 * @return Возвращает HasHMap, размерность 2.
 * * Ключи:
 * * cookie - куки ответа
 * * gf.ttu - токен для запросов
 */
public class SendPass extends Requests {

    public SendPass(GooglePlayApiUpdate googlePlayApiUpdate, HashMap<String, String> input) {
        super(googlePlayApiUpdate, input);
    }

    public HashMap<String, String> make() throws IOException {
        HashMap<String, String> output = new HashMap<>();

        Map<String, String> headers = getDefaultHeaders();

        StringBuilder stringBuilder = new StringBuilder();
        input.forEach((x, y) -> {
            if (x.contains(COOKIE)) {
                stringBuilder.append(x.replaceFirst(COOKIE, "") +"="+ y).append(";");
            }
        });
        headers.put(COOKIE, stringBuilder.toString());

        Map<String, String> body = new HashMap<>();

        body.put("source", "com.android.vending");
        body.put("continue", "https://accounts.google.com/o/android/auth?lang=ru&cc&langCountry=ru_&xoauth_display_name=Android+Device&source=android&tmpl=new_account&return_user_id=true");
        body.put("f.req", "[\"" + input.get(FREQ) + "\",null,1,null,[1,null,null,null,[\"" + input.get(PASSWORD) + "\",null,true]]]");
        body.put("bgRequest", "[\"identifier\",\"!NjWlNRRCTL9baV5PDUtY9vUeYav7HAQCAAAFH1IAAABrmQHkoY69IJ4px8uE4OchQsSi_O4yKHnBjeWEBfJEZde8JM6oKOllVM7Q803UFG_YjKfpN7RXNgjGv5AZBc26Qr15W0e8f4saIzSCJiYvgTN_TCSoKDp2tdy0I84GKeUoxFCeetMUE6-JO36KZISc4aGhgYnAdkU3j4PsVmE4lQyIQfZzg2i1EZ8XNjGxV212F7hCoJbHNoN5S-iWvH8u48ohr86hVoyRKdxyn9diCkyDUm6y2SKDi91ZrT8gy0aLgJZr_XhpLJBJDySw-fk7nQqgA0LDhjgIeOuRa2WBloQI0e-p2mOrIRAtWgVXT_DesaBg2bF8w0Q4Unq-z1WN-bxELKGue11O-D1aPmk9kx71jyxnJ-WGyEoVNpgPqSB81oRafS0NqMUIkuz7mUJcz3n2A-V9L9Nnsm7XwriPce6ZqLf-q5Tfy3Aq1xISGRe31Cf1pVVU6a51HEt29Orm3GycDKfuR1sXeVC6B0T84wDqgMkabiT0dEB8MDm8qQxAeRBd0aP6JkssRugXPR-vBrbsNi2cXNDdOhpG1gDbg_bND1iBZ0Di3CPiAfWng70RRmP740-nSYTP_jJwhg0eSRGqwYse54jgnHSCcRE5SrRVVI-gEKVVCGPoe8-VHPnVALsCG64THQ\"]");
        body.put(AZT, input.get(AZT));
        body.put("cookiesDisabled", "false");
        body.put("deviceinfo", "[\"" + input.get(GSFID) + "\","+VERSIONSDK+"," + deviceInfoProvider.getPlayServicesVersion() + ",[],true,\""+RU+"\",0,null,[],\"EmbeddedSetupAndroid\",null,[0,null,[],null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,7,null,null,[],null,null,null,[],[]],1,null,null,null,2,null,false,2]");
        body.put("gmscoreversion", Integer.toString(deviceInfoProvider.getPlayServicesVersion()));


        HashMap<String, byte[]> responseBytes = client.postUp(SEND_PASS_HOST + "?hl="+ru+"&TL=" + input.get(GFTTU) + "&_reqid=" + getReqid() + "&rt=j", body, headers);
        String respince = new String(getBody(responseBytes));

        if (null != respince) {
            output.putAll(cookie(getHeader(responseBytes)));
            output.putAll(parsJSON(respince));
            return output;
        } else {
            throw new IOException("SendPass failed!");
        }
    }


    /**
     * Достаем значение gf.ttu
     *
     * @param input
     * @return
     */
    public HashMap<String, String> parsJSON(String input) throws IOException {
        input = input.replace(")]}'", "");

        if (input.contains("INCORRECT_ANSWER_ENTERED")){
            throw new IOException("incorrect pass");
        }

        if (input.contains("LOGIN_CHALLENGE")){
            throw new IOException("Phone confirmation needed");
        }

        HashMap<String, String> output = new HashMap<>();
        JSONArray reader = new JSONArray(input);

        String gfttu = reader.getJSONArray(0).getJSONArray(1).getString(2);
        output.put(GFTTU, gfttu);

        return output;
    }
}
