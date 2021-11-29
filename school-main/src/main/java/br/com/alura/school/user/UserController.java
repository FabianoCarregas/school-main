package br.com.alura.school.user;

import static java.lang.String.format;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.school.support.validation.ResourceNotFoundException;

@RestController
class UserController {

    private final UserRepository userRepository;

    UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users/{username}")
    ResponseEntity<UserResponse> userByUsername(@PathVariable("username") String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(username));
        return ResponseEntity.ok(new UserResponse(user));
    }

    @PostMapping("/users")
    ResponseEntity<Void> newUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        userRepository.save(newUserRequest.toEntity());
        URI location = URI.create(format("/users/%s", newUserRequest.getUsername()));
        return ResponseEntity.created(location).build();
    }

}
