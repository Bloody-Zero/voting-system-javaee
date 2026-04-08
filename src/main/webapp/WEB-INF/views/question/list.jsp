<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Управление вопросами</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <div class="user-info">
            <h1>Вопросы</h1>
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
                <a href="${pageContext.request.contextPath}/question/new" class="btn btn-primary">Добавить вопрос</a>
            </c:if>
        </div>

        <c:if test="${not empty param.success}">
            <div class="alert success">Операция выполнена успешно</div>
        </c:if>

        <table class="data-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Голосование</th>
                    <th>Содержание</th>
                    <th>Дата</th>
                    <th>Действия</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="question" items="${questions}">
                    <tr>
                        <td>${question.id}</td>
                        <td>${question.vote.title}</td>
                        <td>${question.content}</td>
                        <td>${question.dateVoteFormatted}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/question/view/${question.id}" class="btn small">Просмотр</a>
                            <c:if test="${userRole eq 'ADMIN'}">
                                <a href="${pageContext.request.contextPath}/question/edit/${question.id}" class="btn small">Редактировать</a>
                                <a href="${pageContext.request.contextPath}/question/delete/${question.id}" class="btn small danger"
                                   onclick="return confirm('Удалить вопрос?')">Удалить</a>
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