package ru.brombin.user_service.repository;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import ru.brombin.user_service.entity.User;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
}