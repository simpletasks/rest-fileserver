package com.javadevbod.fileserver;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class FileShareInvite {

    private String email;
    private String fileId;
}
