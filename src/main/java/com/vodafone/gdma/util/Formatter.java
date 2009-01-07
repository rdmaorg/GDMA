/*
 * Created on Mar 24, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.vodafone.gdma.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author RGILL
 * 
 *         To change the template for this generated type comment go to Window - Preferences - Java - Code Generation -
 *         Code and Comments
 */
public class Formatter {

    public static String dateFormat = "yyyy-MM-dd";

    public static String dateFormat2 = "dd-MM-yyyy";

    private static SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

    private static SimpleDateFormat sdf2 = new SimpleDateFormat(dateFormat2);

    public static String escapeQuotes(String in) {
        if (in == null)
            return null;
        else
            return in.replaceAll("[\"']", "\\\"");
    }

    public static String escapeNewLines(String in) {
        if (in == null)
            return null;
        else
            return in.replaceAll("[\r\n]+", "\\\\n");
    }

    public static String escapeAll(String in) {
        in = escapeQuotes(in);
        in = escapeNewLines(in);
        return in;
    }

    public static String formatDate(Date date) {
        if (date == null)
            return "";
        else
            return sdf.format(date);
    }

    // Try 2 ways of parsing
    // First - treat it as a long
    // Second - treat it as a string
    public static Date parseDate(String value) throws Exception {
        Date date = null;
        try {
            date = new Date(Long.parseLong(value));
        } catch (Exception e) {
            date = null;
        }
        if (date == null) {
            try {
                date = sdf.parse(value);
            } catch (Exception e) {
                try {
                    date = sdf2.parse(value);
                } catch (Exception e2) {
                    throw new Exception("Could not pase value [" + value + "] into a date");
                }
            }
        }

        return date;
    }

    public static String getDateStringFromTimestamp(String timestamp) throws ParseException {
        String value = "";

        if (timestamp != null)
            value = formatDate(new Date(Long.parseLong(timestamp)));

        return value;
    }

}
