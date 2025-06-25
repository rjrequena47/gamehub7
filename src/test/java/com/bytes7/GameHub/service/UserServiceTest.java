// package com.bytes7.GameHub.service;

// import com.bytes7.GameHub.dto.response.UserResponse;
// import com.bytes7.GameHub.model.entity.User;
// import com.bytes7.GameHub.model.enums.Role;
// import com.bytes7.GameHub.repository.UserRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.*;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.authentication.TestingAuthenticationToken;

// import java.util.Optional;
// import java.util.UUID;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// public class UserServiceTest {

//     @Mock
//     private UserRepository userRepository;

//     @InjectMocks
//     private UserService userService;

//     private final UUID userId = UUID.randomUUID();

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     void getCurrentUser_ShouldReturnUserResponse() {
//         // Arrange
//         String username = "ricardo";
//         User user = new User();
//         user.setId(userId);
//         user.setUsername(username);
//         user.setEmail("ricardo@gamehub.com");
//         user.setRole(Role.PLAYER);
//         user.setPoints(10);

//         SecurityContextHolder.getContext().setAuthentication(
//             new TestingAuthenticationToken(username, null)
//         );

//         when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

//         // Act
//         UserResponse response = userService.getCurrentUser();

//         // Assert
//         assertNotNull(response);
//         assertEquals(username, response.getUsername());
//         assertEquals(Role.PLAYER, response.getRole());
//     }

//     @Test
//     void getUserById_ShouldReturnUserResponse() {
//         // Arrange
//         User user = new User();
//         user.setId(userId);
//         user.setUsername("playerTest");
//         user.setEmail("test@gamehub.com");
//         user.setRole(Role.PLAYER);
//         user.setPoints(5);

//         when(userRepository.findById(userId)).thenReturn(Optional.of(user));

//         // Act
//         UserResponse response = userService.getUserById(userId);

//         // Assert
//         assertNotNull(response);
//         assertEquals(userId, response.getId());
//         assertEquals("playerTest", response.getUsername());
//     }
// }
