package ru.brombin.user_service.controller;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.brombin.user_service.entity.User;
import ru.brombin.user_service.service.UserService;
import ru.brombin.user_service.dto.UserDto;

import java.net.URI;
import java.util.List;


@Slf4j
@Path("/api/users")
@RolesAllowed("admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @GET
    public Uni<List<User>> getAllUsers() {
        return userService.getAllUsers();
    }

    @GET
    @Path("/{id}")
    public Uni<User> getUserById(@PathParam("id") Long id) {
        return userService.getUserById(id);
    }

    @POST
    @Transactional
    public Uni<Response> createUser(@Valid UserDto userDto) {
        return userService.createUser(userDto)
                .map(createdUser -> Response.created(URI.create("/users/" + createdUser.id)).entity(createdUser).build());
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Uni<Response> updateUser(@PathParam("id") Long id, @Valid UserDto userDto) {
        return userService.updateUser(id, userDto)
                .map(updatedUser -> Response.ok(updatedUser).build());
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Uni<Response> deleteUser(@PathParam("id") Long id) {
        return userService.deleteUser(id)
                .onItem().ifNull().failWith(() -> new NotFoundException(String.format("User with ID '%d' not found", id)))
                .map(user -> Response.noContent().build())
                .onFailure().recoverWithItem(Response.status(Response.Status.NOT_FOUND).build());
    }
}


