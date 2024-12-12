package ru.brombin.user_service.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.brombin.user_service.dto.UserDto;
import ru.brombin.user_service.entity.User;
import ru.brombin.user_service.mapper.UserMapper;
import ru.brombin.user_service.repository.UserRepository;
import ru.brombin.user_service.util.exception.NotFoundException;

import java.util.List;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;

    public Uni<List<User>> getAllUsers() {
        return userRepository.listAll()
                .invoke(users -> log.info("Successfully fetched {} users", users.size()));
    }

    public Uni<User> getUserById(Long id) {
        return userRepository.findById(id)
                .onItem().ifNull().failWith(() -> new NotFoundException(String.format("User with ID '%d' not found", id)))
                .onItem().invoke(() -> log.info("Successfully fetched user with ID: {}", id));
    }

    @Transactional
    public Uni<User> createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);

        return userRepository.persistAndFlush(user)
                .onItem().invoke(createdUser -> log.info("User with ID: {} created successfully", createdUser.id));
    }

    @Transactional
    public Uni<User> updateUser(Long id, UserDto userDto) {

        return userRepository.findById(id)
                .onItem().ifNull().failWith(() -> new NotFoundException(String.format("User with ID '%d' not found", id)))
                .flatMap(existingUser -> {
                    User updatedUser = userMapper.toEntity(userDto);
                    updatedUser.id = existingUser.id;

                    return userRepository.persistAndFlush(updatedUser);
                })
                .onItem().invoke(updatedUser -> log.info("User with ID: {} updated successfully", updatedUser.id));
    }

    @Transactional
    public Uni<Void> deleteUser(Long id) {

        return userRepository.findById(id)
                .onItem().ifNull().failWith(() -> new NotFoundException(String.format("User with ID '%d' not found", id)))
                .flatMap(user -> userRepository.delete(user)
                        .onItem().invoke(() -> log.info("User with ID: {} deleted successfully", id)));
    }
}

