package br.com.provabase;

import br.com.provabase.auth.AppUser;
import br.com.provabase.auth.AppUserRepository;
import br.com.provabase.auth.Role;
import br.com.provabase.item.Item;
import br.com.provabase.item.ItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@SpringBootApplication
public class ProvaBaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProvaBaseApplication.class, args);
    }

    @Bean
    CommandLineRunner seed(AppUserRepository users, ItemRepository items, PasswordEncoder encoder) {
        return args -> {
            if (users.findByEmail("admin@teste.com").isEmpty()) {
                users.save(new AppUser(null, "Administrador", "admin@teste.com", encoder.encode("123456"), Role.ADMIN));
            }
            if (items.count() == 0) {
                items.save(new Item(null, "Item de exemplo", "Troque esta entidade na prova", new BigDecimal("10.00"), true));
            }
        };
    }
}
