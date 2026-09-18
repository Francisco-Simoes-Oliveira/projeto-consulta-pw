package br.com.loja.auth;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final AppUserRepository repository;
    public UserController(AppUserRepository repository) { this.repository = repository; }

    @GetMapping("/me")
    public UserView me(Authentication auth) {
        AppUser u = repository.findByEmail(auth.getName()).orElseThrow();
        return UserView.from(u);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserView> list() {
        return repository.findAll().stream().map(UserView::from).toList();
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public UserView changeRole(@PathVariable Long id, @RequestBody RoleRequest request, Authentication auth) {
        AppUser u = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        if (u.getEmail().equals(auth.getName()) && request.role() != Role.ADMIN)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O administrador logado não pode remover o próprio acesso de administrador");
        u.setRole(request.role());
        return UserView.from(repository.save(u));
    }

    public record RoleRequest(Role role) {}
    public record UserView(Long id, String name, String email, Role role) {
        static UserView from(AppUser u) { return new UserView(u.getId(), u.getName(), u.getEmail(), u.getRole()); }
    }
}
