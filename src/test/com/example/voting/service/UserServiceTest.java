package com.example.voting.service;

import com.example.voting.dao.UserDAO;
import com.example.voting.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userDAO);
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User("John", "Doe", "john@example.com", "+1234567890", "NOT_VOTED");
        user1.setId(1L);
        User user2 = new User("Jane", "Smith", "jane@example.com", "+0987654321", "VOTED");
        user2.setId(2L);

        when(userDAO.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userDAO).findAll();
    }

    @Test
    void testGetUser_Success() {
        User user = new User("John", "Doe", "john@example.com", "+1234567890", "NOT_VOTED");
        user.setId(1L);

        when(userDAO.findById(1L)).thenReturn(user);

        User found = userService.getUser(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        assertEquals("John", found.getFirstName());
        verify(userDAO).findById(1L);
    }

    @Test
    void testGetUser_NullId() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUser(null));
        verify(userDAO, never()).findById(any());
    }

    @Test
    void testGetUser_NotFound() {
        when(userDAO.findById(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> userService.getUser(999L));
    }

    @Test
    void testGetUserByEmail_Success() {
        User user = new User("John", "Doe", "john@example.com", "+1234567890", "NOT_VOTED");
        when(userDAO.findByEmail("john@example.com")).thenReturn(user);

        User found = userService.getUserByEmail("john@example.com");

        assertNotNull(found);
        assertEquals("john@example.com", found.getEmail());
        verify(userDAO).findByEmail("john@example.com");
    }

    @Test
    void testGetUserByEmail_EmptyEmail() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUserByEmail(""));
        assertThrows(IllegalArgumentException.class, () -> userService.getUserByEmail(null));
    }

    @Test
    void testGetUserWithChoices_Success() {
        User user = new User("John", "Doe", "john@example.com", "+1234567890", "NOT_VOTED");
        user.setId(1L);

        when(userDAO.findByIdWithChoices(1L)).thenReturn(user);

        User found = userService.getUserWithChoices(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        verify(userDAO).findByIdWithChoices(1L);
    }

    @Test
    void testGetUserWithChoices_NullId() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUserWithChoices(null));
    }

    @Test
    void testGetUserWithChoices_NotFound() {
        when(userDAO.findByIdWithChoices(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> userService.getUserWithChoices(999L));
    }

    @Test
    void testSaveUser_Success() {
        User user = new User("John", "Doe", "john@example.com", "+1234567890", "NOT_VOTED");
        user.setId(1L);

        when(userDAO.findByEmail("john@example.com")).thenReturn(null);

        userService.saveUser(user);

        verify(userDAO).findByEmail("john@example.com");
        verify(userDAO).save(user);
    }

    @Test
    void testSaveUser_NullUser() {
        assertThrows(IllegalArgumentException.class, () -> userService.saveUser(null));
        verify(userDAO, never()).save(any());
    }

    @Test
    void testSaveUser_EmptyFirstName() {
        User user = new User("", "Doe", "john@example.com", "+1234567890", "NOT_VOTED");
        user.setId(1L);

        when(userDAO.findByEmail("john@example.com")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> userService.saveUser(user));
    }

    @Test
    void testSaveUser_EmptyLastName() {
        User user = new User("John", "", "john@example.com", "+1234567890", "NOT_VOTED");
        user.setId(1L);

        when(userDAO.findByEmail("john@example.com")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> userService.saveUser(user));
    }

    @Test
    void testSaveUser_EmptyEmail() {
        User user = new User("John", "Doe", "", "+1234567890", "NOT_VOTED");
        user.setId(1L);

        when(userDAO.findByEmail("")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> userService.saveUser(user));
    }

    @Test
    void testSaveUser_InvalidEmail() {
        User user = new User("John", "Doe", "invalid-email", "+1234567890", "NOT_VOTED");
        user.setId(1L);

        when(userDAO.findByEmail("invalid-email")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> userService.saveUser(user));
    }

    @Test
    void testSaveUser_InvalidPhone() {
        User user = new User("John", "Doe", "john@example.com", "invalid", "NOT_VOTED");
        user.setId(1L);

        when(userDAO.findByEmail("john@example.com")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> userService.saveUser(user));
    }

    @Test
    void testSaveUser_EmptyStatus() {
        User user = new User("John", "Doe", "john@example.com", "+1234567890", "");
        user.setId(1L);

        when(userDAO.findByEmail("john@example.com")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> userService.saveUser(user));
    }

    @Test
    void testSaveUser_DuplicateEmail() {
        User user = new User("John", "Doe", "john@example.com", "+1234567890", "NOT_VOTED");
        user.setId(1L);

        User existingUser = new User("Jane", "Smith", "john@example.com", "+0987654321", "VOTED");
        existingUser.setId(2L);

        when(userDAO.findByEmail("john@example.com")).thenReturn(existingUser);

        assertThrows(IllegalArgumentException.class, () -> userService.saveUser(user));
        verify(userDAO, never()).save(any());
    }

    @Test
    void testSaveUser_SameEmailForSameUser() {
        User user = new User("John", "Doe", "john@example.com", "+1234567890", "NOT_VOTED");
        user.setId(1L);

        User existingUser = new User("John", "Doe", "john@example.com", "+1234567890", "VOTED");
        existingUser.setId(1L);

        when(userDAO.findByEmail("john@example.com")).thenReturn(existingUser);

        assertDoesNotThrow(() -> userService.saveUser(user));
        verify(userDAO).save(user);
    }

    @Test
    void testDeleteUser_Success() {
        User user = new User("John", "Doe", "john@example.com", "+1234567890", "NOT_VOTED");
        user.setId(1L);

        when(userDAO.findById(1L)).thenReturn(user);

        userService.deleteUser(1L);

        verify(userDAO).findById(1L);
        verify(userDAO).delete(1L);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userDAO.findById(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(999L));
        verify(userDAO, never()).delete(any());
    }

    @Test
    void testUpdateUserStatus_Success() {
        User user = new User("John", "Doe", "john@example.com", "+1234567890", "NOT_VOTED");
        user.setId(1L);

        when(userDAO.findById(1L)).thenReturn(user);

        userService.updateUserStatus(1L, "VOTED");

        assertEquals("VOTED", user.getStatus());
        verify(userDAO).save(user);
    }

    @Test
    void testMarkUserAsVoted() {
        User user = new User("John", "Doe", "john@example.com", "+1234567890", "NOT_VOTED");
        user.setId(1L);

        when(userDAO.findById(1L)).thenReturn(user);

        userService.markUserAsVoted(1L);

        assertEquals("Голосовал", user.getStatus());
        verify(userDAO).save(user);
    }

    @Test
    void testMarkUserAsNotVoted() {
        User user = new User("John", "Doe", "john@example.com", "+1234567890", "VOTED");
        user.setId(1L);

        when(userDAO.findById(1L)).thenReturn(user);

        userService.markUserAsNotVoted(1L);

        assertEquals("Не голосовал", user.getStatus());
        verify(userDAO).save(user);
    }

    @Test
    void testGetUsersByStatus_Success() {
        User user1 = new User("John", "Doe", "john@example.com", "+1234567890", "VOTED");
        User user2 = new User("Jane", "Smith", "jane@example.com", "+0987654321", "VOTED");

        when(userDAO.findByStatus("VOTED")).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getUsersByStatus("VOTED");

        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userDAO).findByStatus("VOTED");
    }

    @Test
    void testGetUsersByStatus_EmptyStatus() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUsersByStatus(""));
        assertThrows(IllegalArgumentException.class, () -> userService.getUsersByStatus(null));
    }

    @Test
    void testRegisterUser_Success() {
        String firstName = "John";
        String lastName = "Doe";
        String email = "john@example.com";
        String phone = "+1234567890";
        String username = "johndoe";
        String password = "password123";

        when(userDAO.findByEmail(email)).thenReturn(null);
        when(userDAO.findByUsername(username)).thenReturn(null);

        User registeredUser = userService.registerUser(firstName, lastName, email, phone, username, password);

        assertNotNull(registeredUser);
        assertEquals(firstName, registeredUser.getFirstName());
        assertEquals(lastName, registeredUser.getLastName());
        assertEquals(email, registeredUser.getEmail());
        assertEquals("USER", registeredUser.getRole());
        assertNotNull(registeredUser.getPassword());
        assertNotEquals(password, registeredUser.getPassword()); // пароль должен быть захеширован
        verify(userDAO).save(any(User.class));
    }

    @Test
    void testRegisterUser_DuplicateEmail() {
        User existingUser = new User("Jane", "Smith", "john@example.com", "+0987654321", "VOTED");
        when(userDAO.findByEmail("john@example.com")).thenReturn(existingUser);

        assertThrows(IllegalArgumentException.class, () ->
            userService.registerUser("John", "Doe", "john@example.com", "+1234567890", "johndoe", "password123")
        );
        verify(userDAO, never()).save(any());
    }

    @Test
    void testRegisterUser_DuplicateUsername() {
        User existingUser = new User("Jane", "Smith", "jane@example.com", "+0987654321", "VOTED", "johndoe", "hash", "USER");
        when(userDAO.findByEmail("john@example.com")).thenReturn(null);
        when(userDAO.findByUsername("johndoe")).thenReturn(existingUser);

        assertThrows(IllegalArgumentException.class, () ->
            userService.registerUser("John", "Doe", "john@example.com", "+1234567890", "johndoe", "password123")
        );
        verify(userDAO, never()).save(any());
    }

    @Test
    void testAuthenticate_Success() {
        String username = "johndoe";
        String password = "password123";
        String hashedPassword = hashPassword(password);

        User user = new User("John", "Doe", "john@example.com", "+1234567890", "NOT_VOTED",
                            username, hashedPassword, "USER");
        when(userDAO.findByUsername(username)).thenReturn(user);

        User authenticated = userService.authenticate(username, password);

        assertNotNull(authenticated);
        assertEquals(username, authenticated.getUsername());
    }

    @Test
    void testAuthenticate_WrongPassword() {
        String username = "johndoe";
        String hashedPassword = hashPassword("correctpassword");

        User user = new User("John", "Doe", "john@example.com", "+1234567890", "NOT_VOTED",
                            username, hashedPassword, "USER");
        when(userDAO.findByUsername(username)).thenReturn(user);

        User authenticated = userService.authenticate(username, "wrongpassword");

        assertNull(authenticated);
    }

    @Test
    void testAuthenticate_UserNotFound() {
        when(userDAO.findByUsername("nonexistent")).thenReturn(null);

        User authenticated = userService.authenticate("nonexistent", "password");

        assertNull(authenticated);
    }

    @Test
    void testAuthenticate_EmptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> userService.authenticate("", "password"));
        assertThrows(IllegalArgumentException.class, () -> userService.authenticate(null, "password"));
    }

    @Test
    void testAuthenticate_EmptyPassword() {
        assertThrows(IllegalArgumentException.class, () -> userService.authenticate("user", ""));
        assertThrows(IllegalArgumentException.class, () -> userService.authenticate("user", null));
    }

    /**
     * Вспомогательный метод для хеширования пароля (такой же как в UserService).
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Алгоритм SHA-256 не найден", e);
        }
    }
}
