<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Вход в систему</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-page">
<div class="auth-wrapper">
    <div class="auth-card">
        <h1>🗳️ Система голосования</h1>
        <p class="auth-subtitle">Войдите для продолжения работы</p>

        <c:if test="${not empty error}">
            <div class="alert error">${error}</div>
        </c:if>
        <c:if test="${param.logged_out != null}">
            <div class="alert success">Вы успешно вышли из системы.</div>
        </c:if>
        <c:if test="${not empty param.registered}">
            <div class="alert success">Регистрация прошла успешно! Войдите в систему.</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post" class="data-form">
            <div class="form-group">
                <label for="username">Логин:</label>
                <input type="text" id="username" name="username" placeholder="Введите логин" required autocomplete="username">
            </div>
            <div class="form-group">
                <label for="password">Пароль:</label>
                <input type="password" id="password" name="password" placeholder="Введите пароль" required autocomplete="current-password">
            </div>
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Войти</button>
                <a href="${pageContext.request.contextPath}/register" class="btn btn-secondary" style="text-align:center;">Регистрация</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
