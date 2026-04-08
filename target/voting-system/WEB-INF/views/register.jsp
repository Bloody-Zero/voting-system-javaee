<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Регистрация</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-page">
<div class="auth-wrapper" style="max-width:520px;">
    <div class="auth-card">
        <h1>🗳️ Регистрация</h1>
        <p class="auth-subtitle">Создайте учётную запись</p>

        <c:if test="${not empty error}">
            <div class="alert error">${error}</div>
        </c:if>
        <c:if test="${not empty errors}">
            <div class="alert error">
                <ul style="margin-left:1.2rem;">
                    <c:forEach var="err" items="${errors}">
                        <li>${err.value}</li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="post" class="data-form">
            <div class="form-group">
                <label for="firstName">Имя:</label>
                <input type="text" id="firstName" name="firstName" value="${firstName}" placeholder="Иван" required>
            </div>
            <div class="form-group">
                <label for="lastName">Фамилия:</label>
                <input type="text" id="lastName" name="lastName" value="${lastName}" placeholder="Иванов" required>
            </div>
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" value="${email}" placeholder="ivan@example.com" required>
            </div>
            <div class="form-group">
                <label for="phone">Телефон:</label>
                <input type="tel" id="phone" name="phone" value="${phone}" placeholder="+79001234567">
            </div>
            <div class="form-group">
                <label for="username">Логин:</label>
                <input type="text" id="username" name="username" value="${username}" placeholder="Минимум 3 символа" required minlength="3">
            </div>
            <div class="form-group">
                <label for="password">Пароль:</label>
                <input type="password" id="password" name="password" placeholder="Минимум 6 символов" required minlength="6">
            </div>
            <div class="form-group">
                <label for="confirmPassword">Подтверждение пароля:</label>
                <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Повторите пароль" required minlength="6">
            </div>
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Зарегистрироваться</button>
                <a href="${pageContext.request.contextPath}/login" class="btn btn-secondary" style="text-align:center;">Уже есть аккаунт? Войти</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
