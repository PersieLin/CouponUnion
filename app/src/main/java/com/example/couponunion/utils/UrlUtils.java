package com.example.couponunion.utils;

public class UrlUtils {

    private static final String HTTPS_PRE = "https:";

    public static String createUrl(int materialId, int page) {
        return "discovery/" + materialId +"/" + page;
    }

    public static String getCoverPath(String pict_url, int size) {
        return HTTPS_PRE + pict_url + "_" + size + "x" + size + ".jpg";
    }

    public static String getCoverPath(String pict_url) {
        return HTTPS_PRE + pict_url;
    }

    public static String getTicketUrl(String url) {
        if (url.startsWith("http:") || url.startsWith("https:")){
            return url;
        }
        else
            return HTTPS_PRE + url;
    }

    public static String getSelectedContentUrl(int categoryId) {
        return "recommend/" + categoryId;
    }

    public static String getOnSellPageUrl(int currentPage) {
        return "onSell/" + currentPage;
    }

}
