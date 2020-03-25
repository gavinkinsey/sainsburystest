package com.akinsey;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;


public class Main {

    private static String url = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";


    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect(url).get();
            System.out.println(doc.title());
        }
        catch (IOException err) {
            System.out.println("Error");
        }
    }
}
