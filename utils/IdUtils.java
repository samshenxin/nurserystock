package jdy.zsf.nurserystock.utils;

import java.security.SecureRandom;
import java.util.Random;

public class IdUtils
{
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }
    
    public static String simpleUUID() {
        return UUID.randomUUID().toString(true);
    }
    
    public static String fastUUID() {
        return UUID.fastUUID().toString();
    }
    
    public static String fastSimpleUUID() {
        return UUID.fastUUID().toString(true);
    }
    
    public static String getGUID() {
        final StringBuilder uid = new StringBuilder();
        final Random rd = new SecureRandom();
        for (int i = 0; i < 24; ++i) {
            final int type = rd.nextInt(2);
            switch (type) {
                case 0: {
                    uid.append(rd.nextInt(10));
                    break;
                }
                case 1: {
                    uid.append((char)(rd.nextInt(6) + 65));
                    break;
                }
            }
        }
        return uid.toString();
    }
    
    public static String getRandomValue(final int numSize) {
        String str = "";
        for (int i = 0; i < numSize; ++i) {
            char temp = '\0';
            final int key = (int)(Math.random() * 2.0);
            switch (key) {
                case 0: {
                    temp = (char)(Math.random() * 10.0 + 48.0);
                    break;
                }
                case 1: {
                    temp = (char)(Math.random() * 6.0 + 65.0);
                    break;
                }
            }
            str += temp;
        }
        return str;
    }
}
