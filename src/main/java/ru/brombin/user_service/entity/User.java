package ru.brombin.user_service.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Table(name = "users")
@FieldDefaults(level= AccessLevel.PRIVATE)
public class User extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

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