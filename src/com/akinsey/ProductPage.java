package com.akinsey;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;


/**
 * Parser for individual product pages
 */
public class ProductPage extends Parser {
    private int kcal_per_100g;
    private int unitPriceInPence;
    private String description;


    /**
     * Constructor using URL string
     *
     * @param url URL of document to parse
     * @throws IOException Error from JSoup
     */
    public ProductPage(String url) throws IOException {
        super(url);
        extractData();
    }

    /**
     * Constructor using local file
     *
     * @param file Local file containing HTML to parse
     * @throws IOException Error from JSoup
     */
    public ProductPage(File file) throws IOException {
        super(file);
        extractData();
    }

    /**
     * Parse the required data out of the HTML
     */
    public void extractData() {
        parseKCal();
        parseDescription();
        parsePrice();
    }


    /**
     * Parse the KCal per 100g value out of the HTML if present
     *
     * If not found then set the value to -1
     */
    protected void parseKCal() {
        Elements nutritionTable = doc.getElementsByClass("nutritionTable");
        if (nutritionTable.size() == 0) {
            // No nutrition table found
            kcal_per_100g = -1;
            return;
        }

        Elements rows = nutritionTable.first().getElementsByTag("tr");
        for (Element row : rows) {
            Elements cells = row.getElementsByTag("td");
            for (Element cell : cells) {
                String cellText = cell.text();
                if (cellText.contains("kcal")) {
                    // This conversion could be made more robust
                    String numberPart = cellText.replace("kcal", "");
                    kcal_per_100g = Integer.parseInt(numberPart);
                    return;
                }
            }
        }
        kcal_per_100g = -1;
    }

    /**
     * Parse the description value out of the HTML
     */
    protected void parseDescription() {
        StringBuilder desc = new StringBuilder();
        Elements productDataEls = doc.getElementsByClass("productDataItemHeader");
        for (Element productData : productDataEls) {
            if ("Description".equals(productData.text())) {
                Element productTextDiv = productData.nextElementSibling();
                if (productTextDiv != null) {
                    for (Element el : productTextDiv.children()) {
                        desc.append(el.text());
                        if (desc.length() > 0)
                            break;
                    }
                }
            }
        }
        description = desc.toString();
    }

    /**
     * Parse the price value out of the HTML
     */
    protected void parsePrice() {
        Elements pricePerUnitEls = doc.getElementsByClass("pricePerUnit");
        if (pricePerUnitEls.size() == 0) {
            // No pricePerUnit element found
            unitPriceInPence = 0;
            return;
        }


        String unitPriceStr = pricePerUnitEls.first().text();

        // This conversion could be made more robust
        unitPriceStr = unitPriceStr.substring(1,5);
        double unitPriceFloat = Double.parseDouble(unitPriceStr);

        // Price is converted to pence to prevent rounding errors occurring when performing arithmetic on prices
        unitPriceInPence = (int)(unitPriceFloat * 100);
    }

    /**
     * Getter method
     * @return unitPriceInPence
     */
    public int getUnitPrice() {
        return unitPriceInPence;
    }

    /**
     * Generate JSON object for this product
     * @return JSON
     */
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();

        obj.put("title", getTitleP1());
        if (kcal_per_100g >= 0)
            obj.put("kcal_per_100g", kcal_per_100g);
        obj.put("unit_price", (double)unitPriceInPence / 100.0);
        obj.put("description", description);

        return obj;
    }
}
