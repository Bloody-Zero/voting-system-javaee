<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Регистрация</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-page">
<div class="auth-wrapper">
    <div class="auth-card">
        <div class="auth-logo">
            <div class="logo-icon">👤</div>
        </div>
        <h1>Создание аккаунта</h1>
        <p class="auth-subtitle">Заполните форму для регистрации</p>

        <c:if test="${not empty error}">
            <div class="alert error">
                <span>⚠️</span> ${error}
            </div>
        </c:if>
        <c:if test="${not empty errors}">
            <div class="alert error">
                <span>⚠️</span>
                <ul style="margin: 0; padding-left: 1.2rem;">
                    <c:forEach var="err" items="${errors}">
                        <li>${err.value}</li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="post" class="data-form">
            <div class="grid grid-2" style="gap: 1rem;">
                <div class="form-group">
                    <label for="firstName">Имя *</label>
                    <input type="text" id="firstName" name="firstName" value="${firstName}" placeholder="Иван" required>
                </div>
                <div class="form-group">
                    <label for="lastName">Фамилия *</label>
                    <input type="text" id="lastName" name="lastName" value="${lastName}" placeholder="Иванов" required>
                </div>
            </div>
            <div class="form-group">
                <label for="email">Email *</label>
                <input type="email" id="email" name="email" value="${email}" placeholder="ivan@example.com" required>
            </div>
            <div class="form-group">
                <label for="phone">Телефон</label>
                <input type="tel" id="phone" name="phone" value="${phone}" placeholder="+7 (900) 123-45-67">
            </div>
            <div class="form-group">
                <label for="username">Логин *</label>
                <input type="text" id="username" name="username" value="${username}" placeholder="Минимум 3 символа" required minlength="3">
            </div>
            <div class="grid grid-2" style="gap: 1rem;">
                <div class="form-group">
                    <label for="password">Пароль *</label>
                    <input type="password" id="password" name="password" placeholder="Минимум 6 символов" required minlength="6">
                </div>
                <div class="form-group">
                    <label for="confirmPassword">Подтверждение *</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Повторите пароль" required minlength="6">
                </div>
            </div>
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">
                    <span>✨</span> Зарегистрироваться
                </button>
                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline">
                    Уже есть аккаунт? Войти
                </a>
            </div>
        </form>
    </div>
    <div class="auth-footer">
        <p>&copy; 2026 Система голосования. Курсовой проект по Java EE.</p>
    </div>
</div>
</body>
</html>
