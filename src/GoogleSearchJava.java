import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoogleSearchJava {

    public static final String GOOGLE_SEARCH_URL = "https://www.google.com/search";
    public static final String BING_SEARCH_URL="https://www.bing.com/search";
    public static final String YANDEX_SEARCH_URL="https://yandex.com/search/";

    public static void main(String[] args) throws IOException {

//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Please enter the search term.");
//        String searchTerm = scanner.nextLine();
//        System.out.println("Please enter the number of results. Example: 5");
//        int num = scanner.nextInt();
//        scanner.close();

        String searchTerm = "Apple";
        int num = 3;

        String searchURL = GOOGLE_SEARCH_URL + "?q="+searchTerm+"&num="+num;
        String searchURLbing = BING_SEARCH_URL +"?q="+searchTerm;
        String searchURLYandex = YANDEX_SEARCH_URL +"?text="+searchTerm;

        String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36";

        Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();
        Document docBing = Jsoup.connect(searchURLbing).userAgent(USER_AGENT).get();
        Document docYandex = Jsoup.connect(searchURLYandex).userAgent(USER_AGENT).get();



        Elements results = doc.select("h3.r > a");
        Elements resultsBing = docBing.select("h2>a");
        Elements resultsYandex = docYandex.select("h2>a");

        List<String> googleList = new ArrayList<>();
        List<String> bingList = new ArrayList<>();
        List<String> yandexList = new ArrayList<>();

        int i=1;
        System.out.println("------------Google----------------");
        for (Element result : results) {
            String linkHref = result.attr("href");
            System.out.println("URL::" + linkHref.substring(6, linkHref.indexOf("&")).replaceAll("=", ""));
            googleList.add(linkHref.substring(6, linkHref.indexOf("&")).replaceAll("=",""));
            if(i==num){break;}
            else{++i;}
        }

        System.out.println("------------Bing-----------------");
        i=1;
        for (Element result : resultsBing) {
            String linkHrefBing = result.attr("abs:href");
            System.out.println("URL::" + linkHrefBing);
            bingList.add(linkHrefBing);
            if(i==num){break;}
            else{++i;}
        }

        System.out.println("------------Yandex-----------------");
        i=1;
        for (Element result : resultsYandex) {
            String linkHrefYandex = result.attr("abs:href");
            System.out.println("URL::" + linkHrefYandex);
            yandexList.add(linkHrefYandex);
            if(i==num){break;}
            else{++i;}
        }


        System.out.println("----------Result table--------------");

        FileWriter fw = null;
        try {
            fw = new FileWriter("/Users/limon/Desktop/TestSearch/test.csv");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder builder = new StringBuilder();
        String ColumnNamesList = "Num,Google,Bing,Yandex";
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-5s%-40s%-40s%-40s%-2s%n","|Num","|Google","|Bing","|Yandex","|");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        builder.append(ColumnNamesList + "\n");
        for(i=0;i<num;i++) {
            builder.append((i+1)+","+googleList.get(i)+","+bingList.get(i)+","+yandexList.get(i)+"\n");
            System.out.printf("%-1s%-4d%-1s%-39s%-1s%-39s%-1s%-39s%-2s%n","|",(i+1),"|",googleList.get(i),"|",bingList.get(i),"|",yandexList.get(i),"|");
//            System.out.printf("%-1s%-4d%-1s%-39s%n","|",i,"|",googleList.get(i));
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");
        fw.write(builder.toString());
        fw.close();

    }
}
