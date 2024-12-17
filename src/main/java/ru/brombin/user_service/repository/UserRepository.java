package ru.brombin.user_service.repository;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import ru.brombin.user_service.entity.User;

import java.util.List;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<User, Long> {

    public Uni<List<User>> listAllPaged(int page, int size) {
        return findAll().page(page, size).list();
    }
}