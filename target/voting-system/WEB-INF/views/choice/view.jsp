<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Результат голосования -- Детали</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <div class="user-info">
            <h1>Результат голосования</h1>
            <div class="user-badge">
                👤 ${userName}
                <span class="role">${userRole eq 'ADMIN' ? '🔑 Администратор' : '👤 Пользователь'}</span>
            </div>
        </div>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/">🏠 Главная</a></li>
                <li><a href="${pageContext.request.contextPath}/vote">📋 Голосования</a></li>
                <c:if test="${userRole eq 'ADMIN'}">
                    <li><a href="${pageContext.request.contextPath}/question">❓ Вопросы</a></li>
                    <li><a href="${pageContext.request.contextPath}/user">👥 Пользователи</a></li>
                </c:if>
                <li><a href="${pageContext.request.contextPath}/choice">📊 Результаты</a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="logout">🚪 Выход</a></li>
            </ul>
        </nav>
    </header>
    <main>
        <div class="detail-card">
            <h2>Информация о результате</h2>
            <table class="detail-table">
                <tr>
                    <th>ID</th>
                    <td>${choice.id}</td>
                </tr>
                <tr>
                    <th>Вопрос</th>
                    <td>${choice.question.content}</td>
                </tr>
                <tr>
                    <th>Пользователь</th>
                    <td>${choice.user.fullName}</td>
                </tr>
                <tr>
                    <th>Выбор</th>
                    <td><span class="badge">${choice.choiceUser}</span></td>
                </tr>
            </table>
        </div>
        <div class="form-actions">
            <c:if test="${userRole eq 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/choice/edit/${choice.id}" class="btn">Редактировать</a>
            </c:if>
            <a href="${pageContext.request.contextPath}/choice" class="btn btn-secondary">К списку</a>
        </div>
    </main>
</div>
</body>
</html>
