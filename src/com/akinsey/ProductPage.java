package com.akinsey;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;


public class ProductPage extends Parser {
    private int kcal_per_100g;
    private int unitPriceInPence;
    private String description;


    public ProductPage(String url) throws IOException {
        super(url);
        extractData();
    }

    public ProductPage(File file) throws IOException {
        super(file);
        extractData();
    }

    public void extractData() {
        parseKCal();
        parseDescription();
        parsePrice();
    }


    protected void parseKCal() {
        Elements nutritionTable = doc.getElementsByClass("nutritionTable");
        if (nutritionTable.size() == 0) {
            kcal_per_100g = -1;
            return;
        }

        Elements rows = nutritionTable.first().getElementsByTag("tr");
        for (Element row : rows) {
            Elements cells = row.getElementsByTag("td");
            for (Element cell : cells) {
                String cellText = cell.text();
                if (cellText.contains("kcal")) {
                    String numberPart = cellText.replace("kcal", "");
                    kcal_per_100g = Integer.parseInt(numberPart);
                    return;
                }
            }
        }
        kcal_per_100g = -1;
    }

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

    protected void parsePrice() {
        Elements pricePerUnitEls = doc.getElementsByClass("pricePerUnit");
        if (pricePerUnitEls.size() == 0) {
            unitPriceInPence = 0;
            return;
        }

        String unitPriceStr = pricePerUnitEls.first().text();
        unitPriceStr = unitPriceStr.substring(1,5);
        double unitPriceFloat = Double.parseDouble(unitPriceStr);
        unitPriceInPence = (int)(unitPriceFloat * 100);
    }

    public int getUnitPrice() {
        return unitPriceInPence;
    }

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
