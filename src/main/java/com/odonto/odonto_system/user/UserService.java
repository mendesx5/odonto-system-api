package com.odonto.odonto_system.user;

import com.odonto.odonto_system.shared.exception.ConflictException;
import com.odonto.odonto_system.shared.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Listagem com os filtros opcionais
    @Transactional
    public Page<UserResponse> list(UserRole role, Boolean active, Pageable pageable) {
        Page<User> usersPage;

        if (role != null && active != null) {
            usersPage = userRepository.findByRoleAndActive(role, active, pageable);
        } else if (role != null) {
            usersPage = userRepository.findByRole(role, pageable);
        } else if (active != null) {
            usersPage = userRepository.findByActive(active, pageable);
        } else {
            usersPage = userRepository.findAll(pageable);
        }

        return usersPage.map(UserResponse::new);
    }

    @Transactional
    public UserResponse create(UserCreateRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new ConflictException("E-mail já cadastrado no sistema.");
        }
        User user = User.builder()
                .fullName(req.fullName())
                .email(req.email())
                .password(passwordEncoder.encode(req.password()))
                .role(req.role())
                .active(true)
                .build();
        return new UserResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse update(UUID id, UserUpdateRequest req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        // Valida duplicidade de email se estiver mudando para um e-mail diferente
        if (!user.getEmail().equalsIgnoreCase(req.email()) && userRepository.existsByEmail(req.email())) {
            throw new ConflictException("Este e-mail já está em uso por outro funcionário.");
        }

        user.setFullName(req.fullName());
        user.setEmail(req.email());
        user.setRole(req.role());
        return new UserResponse(userRepository.save(user));
    }

    @Transactional
    public void resetPassword(UUID id, PasswordResetRequest req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
        user.setPassword(passwordEncoder.encode(req.newPassword()));
        userRepository.save(user);
    }

    @Transactional
    public UserResponse deactivate(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
        user.setActive(false);
        return new UserResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse activate(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
        user.setActive(true);
        return new UserResponse(userRepository.save(user));
    }

}
