<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Результаты голосования</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <div class="user-info">
            <h1>Результаты голосования</h1>
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
        <div class="actions">
            <c:if test="${userRole eq 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/choice/new" class="btn btn-primary">Добавить результат</a>
            </c:if>
            <c:if test="${userRole ne 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/votePage" class="btn btn-primary">🗳️ Проголосовать</a>
            </c:if>
        </div>

        <c:if test="${not empty param.success}">
            <div class="alert success">Операция выполнена успешно</div>
        </c:if>

        <table class="data-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Вопрос</th>
                    <th>Пользователь</th>
                    <th>Выбор</th>
                    <th>Действия</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="choice" items="${choices}">
                    <tr>
                        <td>${choice.id}</td>
                        <td>${choice.question.content}</td>
                        <td>${choice.user.fullName}</td>
                        <td>${choice.choiceUser}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/choice/view/${choice.id}" class="btn small">Просмотр</a>
                            <c:if test="${userRole eq 'ADMIN'}">
                                <a href="${pageContext.request.contextPath}/choice/edit/${choice.id}" class="btn small">Редактировать</a>
                                <a href="${pageContext.request.contextPath}/choice/delete/${choice.id}" class="btn small danger"
                                   onclick="return confirm('Удалить результат?')">Удалить</a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </main>
</div>
</body>
</html>