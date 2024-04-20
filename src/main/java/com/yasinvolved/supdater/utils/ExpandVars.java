package com.yasinvolved.supdater.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpandVars {
    static Pattern pv = Pattern.compile("\\$\\{(\\w+)\\}");

    public static String expanduser(String path) {
        String user = System.getProperty("user.home");
        return path.replaceFirst("~", user);
    }

    public static String expandvars(String path) {
        String result = new String(path);

        Matcher m = pv.matcher(path);

        while(m.find()) {
            String var = m.group(1);
            String value = System.getenv(var);
            if (value != null) result=result.replace("${"+var+"}", value);
        }

        return result;
    }
}
