package com.example.voting.service;

import com.example.voting.dao.UserDAO;
import com.example.voting.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        User user1 = new User("Иван", "Иванов", "ivan@test.ru", "+79001234567", "NOT_VOTED");
        user1.setId(1L);
        User user2 = new User("Петр", "Петров", "petr@test.ru", "+79007654321", "VOTED");
        user2.setId(2L);

        when(userDAO.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
        assertEquals("Иванов", users.get(0).getLastName());
        assertEquals("Петров", users.get(1).getLastName());

        verify(userDAO).findAll();
    }

    @Test
    void testGetUser_Success() {
        User user = new User("Иван", "Иванов", "ivan@test.ru", "+79001234567", "NOT_VOTED");
        user.setId(1L);

        when(userDAO.findById(1L)).thenReturn(user);

        User found = userService.getUser(1L);
        assertNotNull(found);
        assertEquals(1L, found.getId());
        assertEquals("Иванов", found.getLastName());

        verify(userDAO).findById(1L);
    }

    @Test
    void testGetUser_NullId() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUser(null));
    }

    @Test
    void testGetUser_NotFound() {
        when(userDAO.findById(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> userService.getUser(999L));
    }

    @Test
    void testGetUserByEmail_Success() {
        User user = new User("Иван", "Иванов", "ivan@test.ru", "+79001234567", "NOT_VOTED");
        when(userDAO.findByEmail("ivan@test.ru")).thenReturn(user);

        User found = userService.getUserByEmail("ivan@test.ru");
        assertNotNull(found);
        assertEquals("ivan@test.ru", found.getEmail());
    }

    @Test
    void testGetUserByEmail_EmptyEmail() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUserByEmail(""));
        assertThrows(IllegalArgumentException.class, () -> userService.getUserByEmail(null));
    }

    @Test
    void testSaveUser_Success() {
        User user = new User("Иван", "Иванов", "ivan@test.ru", "+79001234567", "NOT_VOTED");
        when(userDAO.findByEmail("ivan@test.ru")).thenReturn(null);

        userService.saveUser(user);

        verify(userDAO).save(user);
    }

    @Test
    void testSaveUser_DuplicateEmail() {
        User existing = new User("Старый", "Пользователь", "ivan@test.ru", "+79001111111", "NOT_VOTED");
        existing.setId(1L);
        User newUser = new User("Новый", "Пользователь", "ivan@test.ru", "+79002222222", "NOT_VOTED");
        newUser.setId(2L);

        when(userDAO.findByEmail("ivan@test.ru")).thenReturn(existing);

        assertThrows(IllegalArgumentException.class, () -> userService.saveUser(newUser));
    }

    @Test
    void testSaveUser_EmptyFirstName() {
        User user = new User("", "Иванов", "ivan@test.ru", null, "NOT_VOTED");

        assertThrows(IllegalArgumentException.class, () -> userService.saveUser(user));
    }

    @Test
    void testDeleteUser_Success() {
        User user = new User("Иван", "Иванов", "ivan@test.ru", "+79001234567", "NOT_VOTED");
        user.setId(1L);
        when(userDAO.findById(1L)).thenReturn(user);

        userService.deleteUser(1L);

        verify(userDAO).delete(1L);
    }

    @Test
    void testUpdateUserStatus() {
        User user = new User("Иван", "Иванов", "ivan@test.ru", "+79001234567", "NOT_VOTED");
        user.setId(1L);
        when(userDAO.findById(1L)).thenReturn(user);

        userService.updateUserStatus(1L, "VOTED");

        assertEquals("VOTED", user.getStatus());
        verify(userDAO).save(user);
    }

    @Test
    void testMarkUserAsVoted() {
        User user = new User("Иван", "Иванов", "ivan@test.ru", "+79001234567", "NOT_VOTED");
        user.setId(1L);
        when(userDAO.findById(1L)).thenReturn(user);

        userService.markUserAsVoted(1L);

        assertEquals("Голосовал", user.getStatus());
    }

    @Test
    void testMarkUserAsNotVoted() {
        User user = new User("Иван", "Иванов", "ivan@test.ru", "+79001234567", "VOTED");
        user.setId(1L);
        when(userDAO.findById(1L)).thenReturn(user);

        userService.markUserAsNotVoted(1L);

        assertEquals("Не голосовал", user.getStatus());
    }

    @Test
    void testGetUsersByStatus() {
        User user = new User("Иван", "Иванов", "ivan@test.ru", "+79001234567", "NOT_VOTED");
        when(userDAO.findByStatus("NOT_VOTED")).thenReturn(List.of(user));

        List<User> users = userService.getUsersByStatus("NOT_VOTED");
        assertEquals(1, users.size());
        assertEquals("NOT_VOTED", users.get(0).getStatus());
    }

    @Test
    void testGetUsersByStatus_EmptyStatus() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUsersByStatus(""));
        assertThrows(IllegalArgumentException.class, () -> userService.getUsersByStatus(null));
    }

    @Test
    void testAuthenticate_Success() {
        // Создаём пользователя и хешируем пароль через тот же метод
        String password = "testPassword123";
        String hashedPassword = hashSha256(password);

        User user = new User("Иван", "Иванов", "ivan@test.ru", "+79001234567", "NOT_VOTED",
                              "ivanov", hashedPassword, "USER");
        user.setId(1L);
        when(userDAO.findByUsername("ivanov")).thenReturn(user);

        User authenticated = userService.authenticate("ivanov", password);
        assertNotNull(authenticated);
        assertEquals("ivanov", authenticated.getUsername());
    }

    private String hashSha256(String input) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAuthenticate_WrongPassword() {
        User user = new User("Иван", "Иванов", "ivan@test.ru", "+79001234567", "NOT_VOTED",
                              "ivanov", "somehash", "USER");
        when(userDAO.findByUsername("ivanov")).thenReturn(user);

        User authenticated = userService.authenticate("ivanov", "wrongpassword");
        assertNull(authenticated);
    }

    @Test
    void testAuthenticate_UserNotFound() {
        when(userDAO.findByUsername("nonexistent")).thenReturn(null);

        User authenticated = userService.authenticate("nonexistent", "password");
        assertNull(authenticated);
    }

    @Test
    void testRegisterUser_Success() {
        when(userDAO.findByEmail("new@test.ru")).thenReturn(null);
        when(userDAO.findByUsername("newuser")).thenReturn(null);

        User registered = userService.registerUser("Новый", "Пользователь", "new@test.ru",
                                                    "+79009998877", "newuser", "password123");

        assertNotNull(registered);
        assertEquals("newuser", registered.getUsername());
        assertEquals("USER", registered.getRole());
        assertEquals("NOT_VOTED", registered.getStatus());
        verify(userDAO).save(any(User.class));
    }

    @Test
    void testRegisterUser_DuplicateEmail() {
        User existing = new User("Старый", "Пользователь", "new@test.ru", "+79001111111", "NOT_VOTED");
        when(userDAO.findByEmail("new@test.ru")).thenReturn(existing);

        assertThrows(IllegalArgumentException.class, () ->
            userService.registerUser("Новый", "Пользователь", "new@test.ru", "+79009998877", "newuser", "password123"));
    }

    @Test
    void testRegisterUser_DuplicateUsername() {
        when(userDAO.findByEmail("new@test.ru")).thenReturn(null);
        User existing = new User("Старый", "Пользователь", "old@test.ru", "+79001111111", "NOT_VOTED",
                                  "newuser", "somehash", "USER");
        when(userDAO.findByUsername("newuser")).thenReturn(existing);

        assertThrows(IllegalArgumentException.class, () ->
            userService.registerUser("Новый", "Пользователь", "new@test.ru", "+79009998877", "newuser", "password123"));
    }

    @Test
    void testValidateUser_InvalidEmail() {
        User user = new User("Иван", "Иванов", "not-an-email", "+79001234567", "NOT_VOTED");

        assertThrows(IllegalArgumentException.class, () -> userService.saveUser(user));
    }
}
