package com.odonto.odonto_system.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public Page<UserResponse> list(
            @RequestParam(required=false) UserRole role,
            @RequestParam(required=false) Boolean active,
            Pageable pageable
    ) {
        return userService.list(role, active, pageable);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse create(@RequestBody @Valid UserCreateRequest req
    ) {
        return userService.create(req);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse update(@PathVariable UUID id,
                               @RequestBody @Valid UserUpdateRequest req
    ) {
        return userService.update(id, req);
    }


    @PatchMapping("/{id}/password")
    @PreAuthorize("hasRole('ADMIN')")
    public void resetPassword(@PathVariable UUID id,
                              @RequestBody PasswordResetRequest req
    ) {
        userService.resetPassword(id, req);
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse deactivate(@PathVariable UUID id
    ) {
        return userService.deactivate(id);
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse activate(@PathVariable UUID id) {
        return userService.activate(id);
    }

}
