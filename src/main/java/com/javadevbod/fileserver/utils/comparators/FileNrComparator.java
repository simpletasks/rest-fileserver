package com.javadevbod.fileserver.utils.comparators;

import java.util.Comparator;

import com.javadevbod.fileserver.data.jpa.entities.File;

public class FileNrComparator implements Comparator<File> {

    @Override
    public int compare(File o1, File o2) {
        return o1.getFileVersionNr().compareTo(o2.getFileVersionNr());
    }

}