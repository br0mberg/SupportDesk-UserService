package ru.brombin.user_service.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "users")
@FieldDefaults(level= AccessLevel.PRIVATE)
public class User extends PanacheEntity {

    String login;

    String fullname;

    @Column(name="phone_number")
    String phoneNumber;

    String email;

    @Column(name="workplace_location")
    String workplaceLocation;

    @Enumerated(EnumType.STRING)
    UserRole role;
}