package com.javadevbod.fileserver.data.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Table(name = "files")
@Entity
public class File {

    @JsonIgnore
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "file_id", unique = true, length = 250)
    private String fileId;

    @Column(name = "filename", length = 250)
    private String filename;

    @Column(name = "file_duplicate_nr")
    private Integer fileVersionNr;

}
