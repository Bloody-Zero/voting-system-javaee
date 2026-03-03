<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Вход в систему</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <h1>Вход в систему голосования</h1>
    </header>
    <main>
        <c:if test="${not empty error}">
            <div class="alert error">${error}</div>
        </c:if>
        <form action="${pageContext.request.contextPath}/login" method="post" class="data-form">
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
            </div>
            <!-- В демо-версии пароль не требуется, но можно добавить при необходимости -->
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Войти</button>
            </div>
        </form>
    </main>
</div>
</body>
</html>