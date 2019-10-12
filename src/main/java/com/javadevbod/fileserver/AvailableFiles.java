package com.javadevbod.fileserver;

import java.util.ArrayList;
import java.util.List;

import com.javadevbod.fileserver.data.jpa.File;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AvailableFiles {

    private List<File> owned = new ArrayList<File>();
    private List<File> shared = new ArrayList<File>();
}
