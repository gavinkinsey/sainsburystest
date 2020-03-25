package com.akinsey;

import java.io.IOException;


/**
 * Main class for application
 */
public class Main {
    /**
     * Entry point for application
     */
    public static void main(String[] args) {
        try {
            IndexPage indexPage = new IndexPage("https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html");
            System.out.println(indexPage.generateJSON());
        }
        catch (IOException err) {
            System.out.println("Error retrieving main page");
        }
    }
}
