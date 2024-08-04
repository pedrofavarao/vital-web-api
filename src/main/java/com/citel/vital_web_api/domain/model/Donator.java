package com.citel.vital_web_api.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "donators")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Donator {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    @NotBlank
    private String firstName;

    @Column(name = "last_name")
    @NotBlank
    private String lastName;

    @Column(name = "age")
    @NotNull
    private int age;

    @Column(name = "height")
    @NotNull
    private int height;

    @Column(name = "weight")
    @NotNull
    private int weight;

    @Column(name = "blood_type")
    @NotBlank
    @NotNull
    private String bloodType;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id")
    @JsonManagedReference
    private Address address;
}
