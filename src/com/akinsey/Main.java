package com.akinsey;

import java.io.IOException;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        try {
            IndexPage indexPage = new IndexPage("https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html");
            System.out.println(indexPage.getTitle());

            List<String> productUrls = indexPage.findProductLinks();
            for (String url : productUrls) {
                ProductPage productPage = new ProductPage(url);

                String productJson = productPage.toJSON();
                System.out.println(productJson);
            }
        }
        catch (IOException err) {
            System.out.println("IOException");
        }
    }
}
