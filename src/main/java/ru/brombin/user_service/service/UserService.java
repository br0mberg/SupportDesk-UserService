package ru.brombin.user_service.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.brombin.user_service.dto.UserDto;
import ru.brombin.user_service.entity.User;
import ru.brombin.user_service.mapper.UserMapper;
import ru.brombin.user_service.repository.UserRepository;
import ru.brombin.user_service.util.LogMessages;
import ru.brombin.user_service.util.exceptions.NotFoundException;

import java.util.List;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;

    public Uni<List<UserDto>> getAllUsers(int page, int size) {
        return userRepository.listAllPaged(page, size)
                .map(users -> users.stream()
                        .map(userMapper::toDto)
                        .toList()
                )
                .invoke(users -> logInfo(LogMessages.USERS_FETCHED, users.size()));
    }

    public Uni<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .onItem().ifNull().failWith(() -> new NotFoundException(LogMessages.USER_NOT_FOUND.getFormatted(id)))
                .invoke(() -> logInfo(LogMessages.USER_FETCHED, id));
    }

    public Uni<UserDto> createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        return userRepository.persistAndFlush(user)
                .invoke(created -> logInfo(LogMessages.USER_CREATED, created.getId()))
                .map(userMapper::toDto);
    }

    public Uni<UserDto> updateUser(Long id, UserDto userDto) {
        return userRepository.findById(id)
                .onItem().ifNull().failWith(() -> new NotFoundException(LogMessages.USER_NOT_FOUND.getFormatted(id)))
                .flatMap(existing -> {
                    User updated = userMapper.toEntity(userDto);
                    updated.setId(existing.getId());
                    return userRepository.persistAndFlush(updated);
                })
                .invoke(updated -> logInfo(LogMessages.USER_UPDATED, updated.getId()))
                .map(userMapper::toDto);
    }

    public Uni<Void> deleteUser(Long id) {
        return userRepository.findById(id)
                .onItem().ifNull().failWith(() -> new NotFoundException(LogMessages.USER_NOT_FOUND.getFormatted(id)))
                .flatMap(userRepository::delete)
                .invoke(() -> logInfo(LogMessages.USER_DELETED, id));
    }

    private void logInfo(LogMessages message, Object... args) {
        log.info(message.getFormatted(args));
    }
}