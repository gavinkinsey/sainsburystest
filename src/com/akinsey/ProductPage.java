package com.akinsey;

import java.io.File;
import java.io.IOException;

public class ProductPage extends Parser {
    public ProductPage(String url) throws IOException {
        super(url);
    }

    public ProductPage(File file) throws IOException {
        super(file);
    }
}
