package com.monitor.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "role",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<User> users;

}