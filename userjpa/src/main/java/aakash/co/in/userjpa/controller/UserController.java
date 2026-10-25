package aakash.co.in.userjpa.controller;

import aakash.co.in.userjpa.model.User;
import aakash.co.in.userjpa.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "APIs for managing users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    @Operation(summary = "Create a user", description = "Create a new user in the system")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        logger.info("Request received to create user: {}", user);
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Fetch all users from the system")
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("Request received to fetch all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Delete a user by their ID")
    public ResponseEntity<String> deleteUser(@PathVariable  Long id) {
        logger.info("Request received to delete user with id: {}", id);
        try {
           userService.deleteUser(id);
            logger.info("User deleted successfully with id: {}", id);
            return ResponseEntity.ok("user deleted with id "+id);
        }
        catch (RuntimeException ex) {
            logger.error("Error deleting user with id: {}. Error: {}", id, ex.getMessage());
            return ResponseEntity.status(404).body(ex.getMessage());
        }
    }
}

