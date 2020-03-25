package com.akinsey;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;


/**
 * Base class for HTML parsers
 */
public class Parser {
    protected Document doc;

    /**
     * Constructor using URL string
     *
     * @param url URL of document to parse
     * @throws IOException Error from JSoup
     */
    public Parser(String url) throws IOException {
        doc = Jsoup.connect(url).get();
    }

    /**
     * Constructor using local file
     *
     * @param file Local file containing HTML to parse
     * @throws IOException Error from JSoup
     */
    public Parser(File file) throws IOException {
        doc = Jsoup.parse(file, "UTF-8");
    }


    /**
     * Return the title of the parsed document
     * @return Page title
     */
    public String getTitle() {
        return doc.title();
    }

    /**
     * Return the title of the document with " | Sainsburys" removed
     * @return First part of page title
     */
    public String getTitleP1() {
        String rawTitle = getTitle();
        String[] splitTitle = rawTitle.split(" \\| ");
        if (splitTitle.length == 2) {
            return splitTitle[0];
        }
        return rawTitle;
    }
}
