package com.akinsey;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser for index pages containing product lists
 */
public class IndexPage extends Parser {
    private static final double vatRate = 0.2;


    /**
     * Constructor using URL string
     *
     * @param url URL of document to parse
     * @throws IOException Error from JSoup
     */
    public IndexPage(String url) throws IOException {
        super(url);
    }

    /**
     * Constructor using local file
     *
     * @param file Local file containing HTML to parse
     * @throws IOException Error from JSoup
     */
    public IndexPage(File file) throws IOException {
        super(file);
    }


    /**
     * Search for links to products and return a list of URLs
     * @return List of product URLs found
     */
    public List<String> findProductLinks() {
        List<String> links = new ArrayList<>();
        Elements productDivs = doc.getElementsByClass("productNameAndPromotions");
        for (Element el : productDivs) {
            Elements linkEls= el.getElementsByTag("a");
            for (Element link : linkEls) {
                links.add(link.attr("abs:href"));
            }
        }
        return links;
    }

    /**
     * Generate JSON for products on the page
     * @return JSON string
     */
    public String generateJSON() {
        JSONObject json = new JSONObject();
        JSONArray productsJSON = new JSONArray();
        int grossTotal = 0;

        List<String> productUrls = findProductLinks();
        for (String url : productUrls) {
            try {
                ProductPage productPage = new ProductPage(url);

                productsJSON.add(productPage.toJSON());
                grossTotal += productPage.getUnitPrice();
            }
            catch (IOException err) {
                System.out.println("Error retrieving page: " + url);
            }
        }

        JSONObject totalsJSON = new JSONObject();
        totalsJSON.put("gross", (double)grossTotal / 100.0);
        totalsJSON.put("vat", ((double)grossTotal * vatRate) / 100.0);

        json.put("results", productsJSON);
        json.put("total", totalsJSON);

        return json.toString();
    }
}
