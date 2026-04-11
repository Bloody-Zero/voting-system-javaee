<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Вход в систему</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-page">
<div class="auth-wrapper">
    <div class="auth-card">
        <div class="auth-logo">
            <div class="logo-icon">🗳️</div>
        </div>
        <h1>Добро пожаловать</h1>
        <p class="auth-subtitle">Войдите для продолжения работы</p>

        <c:if test="${not empty error}">
            <div class="alert error">
                <span>⚠️</span> ${error}
            </div>
        </c:if>
        <c:if test="${param.logged_out != null}">
            <div class="alert success">
                <span>✅</span> Вы успешно вышли из системы.
            </div>
        </c:if>
        <c:if test="${not empty param.registered}">
            <div class="alert success">
                <span>✅</span> Регистрация прошла успешно! Войдите в систему.
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post" class="data-form">
            <div class="form-group">
                <label for="username">Логин</label>
                <input type="text" id="username" name="username" placeholder="Введите ваш логин" required autocomplete="username">
            </div>
            <div class="form-group">
                <label for="password">Пароль</label>
                <input type="password" id="password" name="password" placeholder="Введите ваш пароль" required autocomplete="current-password">
            </div>
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">
                    <span>🔑</span> Войти
                </button>
                <a href="${pageContext.request.contextPath}/register" class="btn btn-outline">
                    Создать аккаунт
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
