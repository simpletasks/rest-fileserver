package com.javadevbod.fileserver.data.jpa;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Table(name = "users")
@Entity
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Transient
    private String[] roles;

    @Email
    @Column(name = "email", unique = true, length = 250)
    private String username;

    @Column(name = "password", length = 250)
    private String password;

    @OneToMany(mappedBy = "user")
    List<SharedFile> sharedFiles;
}
