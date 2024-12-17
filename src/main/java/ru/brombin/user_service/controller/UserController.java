package ru.brombin.user_service.controller;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.brombin.user_service.service.UserService;
import ru.brombin.user_service.dto.UserDto;

@Slf4j
@Path("/api/users")
@RolesAllowed("ROLE_ADMIN")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @GET
    public Uni<Response> getAllUsers(@QueryParam("page") @DefaultValue("0") int page,
                                     @QueryParam("size") @DefaultValue("10") int size) {
        return userService.getAllUsers(page, size)
                .map(users -> Response.ok(users).build());
    }

    @GET
    @Path("/{id}")
    public Uni<Response> getUserById(@PathParam("id") Long id) {
        return userService.getUserById(id)
                .map(user -> Response.ok(user).build());
    }

    @POST
    public Uni<Response> createUser(@Valid UserDto userDto) {
        return userService.createUser(userDto)
                .map(createdUser -> Response.status(Response.Status.CREATED)
                        .entity(createdUser)
                        .build());
    }

    @PUT
    @Path("/{id}")
    public Uni<Response> updateUser(@PathParam("id") Long id, @Valid UserDto userDto) {
        return userService.updateUser(id, userDto)
                .map(updatedUser -> Response.ok(updatedUser).build());
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> deleteUser(@PathParam("id") Long id) {
        return userService.deleteUser(id)
                .replaceWith(Response.noContent().build());
    }
}


