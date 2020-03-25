package com.akinsey;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;


public class ProductPage extends Parser {
    private String kcal_per_100g;
    private String unit_price;
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
        getKCal();
        getDescription();
        getPrice();
    }


    public void getKCal() {
        Elements nutritionTable = doc.getElementsByClass("nutritionTable");
        if (nutritionTable.size() == 0) {
            kcal_per_100g = "";
            return;
        }

        Elements rows = nutritionTable.first().getElementsByTag("tr");
        for (Element row : rows) {
            Elements cells = row.getElementsByTag("td");
            for (Element cell : cells) {
                String cellText = cell.text();
                if (cellText.contains("kcal")) {
                    kcal_per_100g = cellText;
                    return;
                }
            }
        }
        kcal_per_100g = "";
    }

    public void getDescription() {
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

    public void getPrice() {
        Elements pricePerUnitEls = doc.getElementsByClass("pricePerUnit");
        if (pricePerUnitEls.size() == 0) {
            unit_price = "";
            return;
        }

        unit_price = pricePerUnitEls.first().text();
    }

    public String toJSON() {
        JSONObject obj = new JSONObject();

        obj.put("title", getTitleP1());
        if (!kcal_per_100g.isEmpty())
            obj.put("kcal_per_100g", kcal_per_100g);
        obj.put("unit_price", unit_price);
        obj.put("description", description);

        return obj.toString();
    }
}
