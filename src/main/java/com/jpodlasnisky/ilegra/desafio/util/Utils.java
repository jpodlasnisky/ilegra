package com.jpodlasnisky.ilegra.desafio.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static File[] findAll(String dirName, String fileExtension) {
        File dir = new File(dirName);

        return dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(fileExtension);
            }
        });
    }

    public static File findFirst(String dirName, String fileExtension) {
        File[] files = findAll(dirName, fileExtension);

        if (ArrayUtils.isNotEmpty(files)) {
            return files[0];
        }

        return null;
    }

    public static String removeExtension(String filename) {
        if(filename == null) {
            throw new IllegalArgumentException("filename is null");
        }

        Pattern pattern = Pattern.compile("^(.*)(\\..*)$");
        Matcher matcher = pattern.matcher(filename);

        if(matcher.find()) {
            return StringUtils.remove(filename, matcher.group(2));
        }

        return filename;
    }
}
