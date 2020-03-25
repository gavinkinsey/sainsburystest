package com.akinsey;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;


public class Parser {
    protected Document doc;

    public Parser(String url) throws IOException {
        doc = Jsoup.connect(url).get();
    }

    public Parser(File file) throws IOException {
        doc = Jsoup.parse(file, "UTF-8");
    }


    public String getTitle() {
        return doc.title();
    }

    public String getTitleP1() {
        String rawTitle = doc.title();
        String[] splitTitle = rawTitle.split(" \\| ");
        if (splitTitle.length == 2) {
            return splitTitle[0];
        }
        return rawTitle;
    }
}
