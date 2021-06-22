package com.alconn.copang.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

    private String realName;

    private String mobile;

    private String description;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime signInDate;

    private Role role;




}
