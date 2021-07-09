package com.alconn.copang.utils;

import java.util.regex.Pattern;

public class StringUtils {

    private static final Pattern SP_CHAR =
        Pattern.compile("[!@#$%^&*()-_=+:;\"'{}\\[\\]<>,./?₩~`]");

    public static String filterSpChar(String target) {
        return SP_CHAR.matcher(target).replaceAll("");
    }

    public static String[] filterAndSplitByEmpty(String target) {
        return filterSpChar(target.replaceAll("[+]", " ")).split(" ");
    }

    public static boolean isEmpty(String target) {
        return target == null || target.equals("");
    }

}
