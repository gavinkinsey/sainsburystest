package com.akinsey;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IndexPage extends Parser {

    public IndexPage(String url) throws IOException {
        super(url);
    }

    public IndexPage(File file) throws IOException {
        super(file);
    }


    public List<String> findProductLinks()
    {
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
}
