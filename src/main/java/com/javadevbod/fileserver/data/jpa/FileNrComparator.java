package com.javadevbod.fileserver.data.jpa;

import java.util.Comparator;

public class FileNrComparator implements Comparator<File> {

    @Override
    public int compare(File o1, File o2) {
        return o1.getFileVersionNr().compareTo(o2.getFileVersionNr());
    }

}