package com.yasinvolved.supdater.utils;

import java.io.File;
import java.io.FilenameFilter;

public class STxtFilter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String name) {
        return name.startsWith("sosnowiecki-txt");
    }
}
