package com.github.yeriomin.playstoreapi.requests;

import com.github.yeriomin.playstoreapi.AuthException;
import com.github.yeriomin.playstoreapi.DeviceInfoProvider;
import com.github.yeriomin.playstoreapi.GooglePlayApiUpdate;
import com.github.yeriomin.playstoreapi.HttpClientAdapter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.github.yeriomin.playstoreapi.GooglePlayApiUpdate.*;


/**
 * Запрос(A)
 * Получает HTML страницу через get запрос
 *
 * @input
 *
 *
 * @return Возвращает HasHMap, размерность 3.
 * Ключи:
 * cookie - куки ответа
 * freq - массив который используется в следующийх запросах под именем f.req
 * azt - токен для запросов
 * ardt = токен для запроса (E)
 *
 */

public class GetHtml extends Requests {


    public GetHtml(GooglePlayApiUpdate googlePlayApiUpdate) {
        super(googlePlayApiUpdate);
    }

    public HashMap<String, String> make() throws IOException {
        Map<String, String> params = new HashMap<>();
        HashMap<String, String> output = new HashMap<>();

        HashMap<String,byte[]> responseBytes = client.getUp(GET_HTML_HOST, params, getHTMLHeaders());
        String respince = new String(getBody(responseBytes));

        if (null != respince) {
            output.putAll(cookie(getHeader(responseBytes)));
            output.putAll(decomposition(respince));
            return output;
        } else {
            throw new AuthException("GetHtml failed!");
        }
    }

    /**
     * Создает хедер для получения html страницы
     * @return
     */
    private Map<String, String> getHTMLHeaders() {
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


    /**
     * Достаем ключ вида AFoagUWQhcLX-asJfTyobSpR0uudM8cMPA:1598519305098
     * @param input
     * @return
     */
    private HashMap<String, String> decomposition(String input){
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
        output.put(ARDT,input.substring(input.indexOf("AKH95"),input.indexOf("AKH95")+115));
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




}
