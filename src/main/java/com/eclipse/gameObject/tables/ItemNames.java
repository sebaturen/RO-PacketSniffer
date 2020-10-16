package com.eclipse.gameObject.tables;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ItemNames {

    private static final String ITEMS_PROPERTIES = "items.properties";
    private static final Properties items = new Properties();

    static {
        try {
            ClassLoader classLoader = ItemNames.class.getClassLoader();
            try (InputStream stream = classLoader.getResourceAsStream(ITEMS_PROPERTIES)) {
                items.load(stream);
            }
        } catch(IOException e) {
            System.out.println(e.toString());
        }
    }

    public static String getItemId(int itemId) {

        return items.getProperty(itemId+"");

    }
}
