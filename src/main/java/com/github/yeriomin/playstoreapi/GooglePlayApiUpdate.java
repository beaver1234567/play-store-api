package com.github.yeriomin.playstoreapi;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

public class GooglePlayApiUpdate {


    /**
     * Константы которые можно заменить набором системных, пока ну времени их искать поэтому тут
     */
    private static final String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9";

    private static final String SCHEME = "https://";

    /**
     * Поля для авторизации
     */
    private static final String AUTH_HOST = "accounts.google.com";

    private static final String GET_HTML_HOST = SCHEME + AUTH_HOST + "/embedded/setup/android?source=com.android.vending&xoauth_display_name=Android%20Phone&canFrp=1&canSk=1&lang=ru&langCountry=ru_ru&hl=ru-RU&cc=ru&use_native_navigation=0";

    private static final String SEND_EMAIL_HOST = SCHEME + AUTH_HOST + "/_/lookup/accountlookup";

    private static final String SEND_PASS_HOST = SCHEME + AUTH_HOST + "/_/signin/challenge";


    /**
     * Ключи для map
     */
    private static final String COOKIE = "cookie";
    private static final String AZT = "azt";
    private static final String FREQ = "freq";
    private static final String GFTTU = "gf.ttu";


    private DeviceInfoProvider deviceInfoProvider;

    private HttpClientAdapter client;


    public GooglePlayApiUpdate() {

    }

    /**
     * Authenticates
     */
    public String generateToken(String email, String password, String gsfid) throws IOException {

        HashMap<String, String> main = new HashMap<String, String> ();
        main.put("email", email);
        main.put("password", password);
        main.put("gsfid", gsfid);

        main.putAll(getHTML());
        main.putAll(sendEmail(main));
        sendPass(main);

        return "";
    }

    /**
     * Запрос(A)
     * Получает HTML страницу через get запрос
     *
     * @return Возвращает HasHMap, размерность 3.
     * Ключи:
     * cookie - куки ответа
     * freq - массив который используется в следующийх запросах под именем f.req
     * azt - токен для запросов
     *
     */
    public HashMap<String, String> getHTML() throws IOException {
        Map<String, String> params = new HashMap<>();
        HashMap<String, String> output = new HashMap<>();

        HashMap<String,byte[]> responseBytes = client.getUp(GET_HTML_HOST, params, getHTMLHeaders());
        String respince = new String(getBody(responseBytes));

        if (null != respince) {
            output.put(COOKIE,cookie(getHeader(responseBytes)));

            HashMap<String, String> temp = parsHTML(respince);
            output.putAll(temp);
            return output;
        } else {
            throw new AuthException("Authentication failed! (login)");
        }
    }

    /**
     * Запрос(С)
     * отправляем email
     *
     * @return Возвращает HasHMap, размерность 4.
     *      * Ключи:
     *      * cookie - куки ответа
     *      * gf.alr - массив который используется в следующийх запросах под именем f.req
     *      * gf.ttu - токен для запросов
     *      * status -
     *      1 = правильный адрес почты
     *      2 = error
     *      5 = капча
     *      7 = не правильный адрес почты
     *      9 = аккаунт удален
     *      12 = Multiple Found
     *      13 = Login Redirect
     */
    public HashMap<String, String> sendEmail(HashMap<String, String> input) throws IOException {
        HashMap<String, String> output = new HashMap<>();

        Map<String, String> headers = getDefaultHeaders();
        headers.put(COOKIE,input.get(COOKIE));

        Map<String, String> body = new HashMap<>();

        body.put("source","com.android.vending");
        body.put("continue","https://accounts.google.com/o/android/auth?lang=ru&cc&langCountry=ru_&xoauth_display_name=Android+Device&source=android&tmpl=new_account&return_user_id=true");
        body.put("f.req","[\""+ input.get("email") +"\",\"" + input.get(FREQ) +"\",[],null,\"RU\",null,null,2,true,true,[null,null,[1,1,0,1,\"https://accounts.google.com/EmbeddedSetup\",null,[],4,[],\"EmbeddedSetupAndroid\",null,[]],2,[0,null,[],null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,7,null,null,[],null,null,null,[],[]],null,null,null,false],\""+ input.get("email") +"\",null,null,null,true,true,[]]");

        body.put("bgRequest","[\"identifier\",\"!NjWlNRRCTL9baV5PDUtY9vUeYav7HAQCAAAFH1IAAABrmQHkoY69IJ4px8uE4OchQsSi_O4yKHnBjeWEBfJEZde8JM6oKOllVM7Q803UFG_YjKfpN7RXNgjGv5AZBc26Qr15W0e8f4saIzSCJiYvgTN_TCSoKDp2tdy0I84GKeUoxFCeetMUE6-JO36KZISc4aGhgYnAdkU3j4PsVmE4lQyIQfZzg2i1EZ8XNjGxV212F7hCoJbHNoN5S-iWvH8u48ohr86hVoyRKdxyn9diCkyDUm6y2SKDi91ZrT8gy0aLgJZr_XhpLJBJDySw-fk7nQqgA0LDhjgIeOuRa2WBloQI0e-p2mOrIRAtWgVXT_DesaBg2bF8w0Q4Unq-z1WN-bxELKGue11O-D1aPmk9kx71jyxnJ-WGyEoVNpgPqSB81oRafS0NqMUIkuz7mUJcz3n2A-V9L9Nnsm7XwriPce6ZqLf-q5Tfy3Aq1xISGRe31Cf1pVVU6a51HEt29Orm3GycDKfuR1sXeVC6B0T84wDqgMkabiT0dEB8MDm8qQxAeRBd0aP6JkssRugXPR-vBrbsNi2cXNDdOhpG1gDbg_bND1iBZ0Di3CPiAfWng70RRmP740-nSYTP_jJwhg0eSRGqwYse54jgnHSCcRE5SrRVVI-gEKVVCGPoe8-VHPnVALsCG64THQ\"]");
        body.put(AZT,input.get(AZT));
        body.put("cookiesDisabled","false");
        body.put("deviceinfo","[\""+input.get("gsfid")+"\",24,"+deviceInfoProvider.getPlayServicesVersion() +",[],true,\"RU\",0,null,[],\"EmbeddedSetupAndroid\",null,[0,null,[],null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,7,null,null,[],null,null,null,[],[]],1,null,null,null,2,null,false,2]");
        body.put("gmscoreversion",Integer.toString(deviceInfoProvider.getPlayServicesVersion()));


        HashMap<String,byte[]> responseBytes = client.postUp(SEND_EMAIL_HOST + "?hl=ru&_reqid="+getReqid()+"&rt=j", body, headers);
        String respince = new String(getBody(responseBytes));

        if (null != respince) {
            output.put(COOKIE,cookie(getHeader(responseBytes)));

            HashMap<String, String> temp = parsJSON(respince);
            output.putAll(temp);
            return output;
        } else {
            throw new IOException("SendEmail failed!");
        }
    }

    /**
     * Запрос(D)
     * Отправляем пароль
     *
     * @return Возвращает HasHMap, размерность 3.
     *
     */
    public HashMap<String, String> sendPass(HashMap<String, String> input) throws IOException {
        HashMap<String, String> output = new HashMap<>();

        Map<String, String> headers = getDefaultHeaders();
        headers.put(COOKIE,input.get(COOKIE));

        Map<String, String> body = new HashMap<>();

        body.put("source","com.android.vending");
        body.put("continue","https://accounts.google.com/o/android/auth?lang=ru&cc&langCountry=ru_&xoauth_display_name=Android+Device&source=android&tmpl=new_account&return_user_id=true");
        body.put("f.req","[\""+FREQ+"\",null,1,null,[1,null,null,null,[\""+output.get("password")+"\",null,true]]]");
        body.put("bgRequest","[\"identifier\",\"!NjWlNRRCTL9baV5PDUtY9vUeYav7HAQCAAAFH1IAAABrmQHkoY69IJ4px8uE4OchQsSi_O4yKHnBjeWEBfJEZde8JM6oKOllVM7Q803UFG_YjKfpN7RXNgjGv5AZBc26Qr15W0e8f4saIzSCJiYvgTN_TCSoKDp2tdy0I84GKeUoxFCeetMUE6-JO36KZISc4aGhgYnAdkU3j4PsVmE4lQyIQfZzg2i1EZ8XNjGxV212F7hCoJbHNoN5S-iWvH8u48ohr86hVoyRKdxyn9diCkyDUm6y2SKDi91ZrT8gy0aLgJZr_XhpLJBJDySw-fk7nQqgA0LDhjgIeOuRa2WBloQI0e-p2mOrIRAtWgVXT_DesaBg2bF8w0Q4Unq-z1WN-bxELKGue11O-D1aPmk9kx71jyxnJ-WGyEoVNpgPqSB81oRafS0NqMUIkuz7mUJcz3n2A-V9L9Nnsm7XwriPce6ZqLf-q5Tfy3Aq1xISGRe31Cf1pVVU6a51HEt29Orm3GycDKfuR1sXeVC6B0T84wDqgMkabiT0dEB8MDm8qQxAeRBd0aP6JkssRugXPR-vBrbsNi2cXNDdOhpG1gDbg_bND1iBZ0Di3CPiAfWng70RRmP740-nSYTP_jJwhg0eSRGqwYse54jgnHSCcRE5SrRVVI-gEKVVCGPoe8-VHPnVALsCG64THQ\"]");
        body.put(AZT,input.get(AZT));
        body.put("cookiesDisabled","false");
        body.put("deviceinfo","[\""+input.get("gsfid")+"\",24,"+deviceInfoProvider.getPlayServicesVersion() +",[],true,\"RU\",0,null,[],\"EmbeddedSetupAndroid\",null,[0,null,[],null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,7,null,null,[],null,null,null,[],[]],1,null,null,null,2,null,false,2]");
        body.put("gmscoreversion",Integer.toString(deviceInfoProvider.getPlayServicesVersion()));


        HashMap<String,byte[]> responseBytes = client.postUp(SEND_PASS_HOST + "?hl=ru&TL=" + input.get(GFTTU) + "&_reqid="+getReqid()+"&rt=j", body, headers);
        String respince = new String(getBody(responseBytes));

        if (null != respince) {
            output.put(COOKIE,cookie(getHeader(responseBytes)));

            HashMap<String, String> temp = parsJSON(respince);
            output.putAll(temp);
            return output;
        } else {
            throw new IOException("SendEmail failed!");
        }
    }

    /**
     * Создает дефольный хедер для запросов
     * @return
     */
    protected Map<String, String> getDefaultHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-same-domain", "1");
        headers.put("google-accounts-xsrf", "1");
        headers.put("User-Agent", this.deviceInfoProvider.getAuthUserAgentString());
        headers.put("accept", "*/*");
        headers.put("origin", "https://accounts.google.com");
        headers.put("x-requested-with", "com.google.android.gms");
        headers.put("sec-fetch-site", "same-origin");
        headers.put("sec-fetch-mode", "cors");
        headers.put("sec-fetch-dest", "empty");
        headers.put("accept-encoding", "gzip, deflate");
        headers.put("accept-language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        return headers;
    }

    /**
     * Создает хедер для получения html страницы
     * @return
     */
    protected Map<String, String> getHTMLHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("upgrade-insecure-requests", "1");
        headers.put("User-Agent", this.deviceInfoProvider.getAuthUserAgentString());
        headers.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        headers.put("x-requested-with", "com.google.android.gms");
        headers.put("sec-fetch-site", "none");
        headers.put("sec-fetch-mode", "navigate");
        headers.put("sec-fetch-user", "?1");
        headers.put("sec-fetch-dest", "document");
        headers.put("accept-encoding", "deflate");
        headers.put("accept-language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
        return headers;
    }


    public void setDeviceInfoProvider(DeviceInfoProvider deviceInfoProvider) {
        this.deviceInfoProvider = deviceInfoProvider;
    }

    public void setClient(HttpClientAdapter httpClient) {
        this.client = httpClient;
    }

    public HttpClientAdapter getClient() {
        return client;
    }


    /**
     * Some methods instead of a protobuf return key-value pairs on each string
     *
     * @param response
     */
    public Map<String, String> parseResponse(String response) {
        Map<String, String> keyValueMap = new HashMap<String, String>();
        StringTokenizer st = new StringTokenizer(response, "\n\r");
        while (st.hasMoreTokens()) {
            String[] keyValue = st.nextToken().split("=", 2);
            if (keyValue.length >= 2) {
                keyValueMap.put(keyValue[0], keyValue[1]);
            }
        }
        return keyValueMap;
    }

    /**
     * Получаем тело запроса
     * @param responseBytes
     * @return
     */
    private byte[] getBody(HashMap<String,byte[]> responseBytes){
        return responseBytes.get("body");
    }

    /**
     * Получаем из мапы хедер
     * @param responseBytes
     * @return
     */

    private Map<String, List<String>> getHeader(HashMap<String,byte[]> responseBytes){
        try {
            ByteArrayInputStream byteIn = new ByteArrayInputStream(responseBytes.get("header"));
            ObjectInputStream in = new ObjectInputStream(byteIn);
            Map<String, List<String>> output = (Map<String, List<String>>) in.readObject();
            return output;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (HashMap<String, List<String>>) Collections.EMPTY_MAP;
    }

    /**
     * Сохраняем куки
     * @param map
     */
    private String cookie(Map<String, List<String>> map){
        StringBuilder output = new StringBuilder();
        for (String input : Objects.requireNonNull(map.get("set-cookie"))) {
            output.append(input).append(";");
        }
        return output.toString();
    }



    /**
     * Достаем ключ вида AFoagUWQhcLX-asJfTyobSpR0uudM8cMPA:1598519305098
     * @param input
     * @return
     */
    public HashMap<String, String> parsHTML(String input){
        HashMap<String, String> output = new HashMap<String, String>();

        String temp = parsingJSOUP(input);
        for(String in : temp.split("\"")){
            if(in.contains("AFoag")){
                output.put(AZT,in.substring(0,48));
                break;
            }
        }

        /**
         * Данный массив сложно достать из html страницы, пока сделаю так
         */
        output.put(FREQ,input.substring(input.indexOf("AEThL"),input.indexOf("AEThL")+204));

        return output;
    }

    /**
     * Парсим Html документ который к нам пришел
     * @param input
     * @return
     */
    private String parsingJSOUP(String input){
        Document document = Jsoup.parse(input);
        Elements paragraphs = document.getElementsByTag("script");
        for(Element paragraph : paragraphs){
            for(DataNode datanode : paragraph.dataNodes()){
                if(datanode.getWholeData().contains("window.WIZ_global_data")){
                    return datanode.getWholeData();
                }
            }
        }
        //TODO Добавить исключение
        return "";
    }

    /**
     * Достаем значение gf.alr и gf.ttu
     * @param input
     * @return
     */
    public HashMap<String, String> parsJSON(String input){
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

    /**
     * Не понятно как генерируется это число
     * @return
     */
    public String getReqid(){
        Random random = new Random();
        int n = 100000 + random.nextInt(900000);
        return Integer.toString(n);
    }

}
