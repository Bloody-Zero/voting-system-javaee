<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Пользователи</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <div class="user-info">
            <h1>Управление пользователями</h1>
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
                <a href="${pageContext.request.contextPath}/user/new" class="btn btn-primary">+ Добавить пользователя</a>
            </c:if>
        </div>
        <c:if test="${param.success == 'created'}"><div class="alert success">Пользователь добавлен</div></c:if>
        <c:if test="${param.success == 'deleted'}"><div class="alert success">Пользователь удалён</div></c:if>
        <table class="data-table">
            <thead><tr><th>ID</th><th>ФИО</th><th>Email</th><th>Телефон</th><th>Статус</th><th>Действия</th></tr></thead>
            <tbody>
            <c:forEach var="u" items="${users}">
                <tr>
                    <td data-label="ID">${u.id}</td>
                    <td data-label="ФИО">${u.lastName} ${u.firstName}</td>
                    <td data-label="Email">${u.email}</td>
                    <td data-label="Телефон">${u.phone}</td>
                    <td data-label="Статус">
                        <span class="status ${u.status eq 'VOTED' ? 'active' : 'finished'}">
                            <c:choose>
                                <c:when test="${u.status == 'VOTED'}">Голосовал</c:when>
                                <c:when test="${u.status == 'NOT_VOTED'}">Не голосовал</c:when>
                                <c:otherwise>${u.status}</c:otherwise>
                            </c:choose>
                        </span>
                    </td>
                    <td data-label="Действия">
                        <c:if test="${userRole eq 'ADMIN'}">
                            <a href="${pageContext.request.contextPath}/user/view?id=${u.id}" class="btn small">Просмотр</a>
                            <a href="${pageContext.request.contextPath}/user/edit?id=${u.id}" class="btn small">Редактировать</a>
                            <a href="${pageContext.request.contextPath}/user/delete?id=${u.id}" class="btn small btn-danger delete-btn">Удалить</a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </main>
</div>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>