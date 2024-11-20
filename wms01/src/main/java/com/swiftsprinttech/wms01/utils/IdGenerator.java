package com.swiftsprinttech.wms01.utils;

import java.util.UUID;

/**
 * @author jiawe
 */
public class IdGenerator {
    public static String generateId() {
        return UUID.randomUUID().toString();
    }
}
