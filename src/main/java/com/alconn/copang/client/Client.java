package com.alconn.copang.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(name = "uk__username", columnNames = {"username"})}
)
public class Client {

    @Id
    @GeneratedValue
    protected Long clientId;

    //    @Column(unique = true)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    private String realName;

    private String phone;

    private String description;

    private String profileImage;

    //    @Column(updatable = false, nullable = false)
//    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul", shape = JsonFormat.Shape.STRING)
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime signInDate;

    private Role role;


    public void updateInfo(String phone, String realName) {
        this.phone = phone == null ? this.phone : phone;
        this.realName = realName == null ? this.realName : realName;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateUserName(String username) {
        this.username = username;
    }
}
