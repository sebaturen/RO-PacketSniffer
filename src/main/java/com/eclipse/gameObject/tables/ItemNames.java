package com.eclipse.gameObject.tables;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class ItemNames {

    private static final String ITEMS_PROPERTIES = "items.properties";
    private static final Properties items = new Properties();

    static {
        try {
            ClassLoader classLoader = ItemNames.class.getClassLoader();
            InputStream is = new FileInputStream(Objects.requireNonNull(classLoader.getResource(ITEMS_PROPERTIES)).getFile());
            items.load(is);
        } catch(IOException e) {
            System.out.println(e.toString());
        }
    }

    public static String getItemId(int itemId) {

        return items.getProperty(itemId+"");

    }
}
