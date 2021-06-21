package com.alconn.copang.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uk__username", columnNames = {"username"})}
)
public class Client {

    @Id @GeneratedValue
    private Long id;

    private String username;

    @Column(nullable = false)
    private String password;

    @Embedded
    private Address address;

    private String description;

    private LocalDateTime signInDate;

    private Role role;




}
