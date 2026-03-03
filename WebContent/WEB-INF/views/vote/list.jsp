<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Управление голосованиями</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <h1>Голосования</h1>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/">Главная</a></li>
                <li><a href="${pageContext.request.contextPath}/vote">Голосования</a></li>
                <li><a href="${pageContext.request.contextPath}/question">Вопросы</a></li>
                <li><a href="${pageContext.request.contextPath}/user">Пользователи</a></li>
                <li><a href="${pageContext.request.contextPath}/choice">Результаты</a></li>
                <li><a href="${pageContext.request.contextPath}/logout">Выход</a></li>
            </ul>
        </nav>
    </header>
    <main>
        <div class="actions">
            <a href="${pageContext.request.contextPath}/vote/new" class="btn btn-primary">Добавить голосование</a>
        </div>

        <c:if test="${not empty param.success}">
            <div class="alert success">Операция выполнена успешно</div>
        </c:if>

        <table class="data-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Название</th>
                    <th>Дата начала</th>
                    <th>Дата окончания</th>
                    <th>Статус</th>
                    <th>Действия</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="vote" items="${votes}">
                    <tr>
                        <td>${vote.id}</td>
                        <td>${vote.title}</td>
                        <td><fmt:formatDate value="${vote.dateStart}" pattern="dd.MM.yyyy HH:mm"/></td>
                        <td><fmt:formatDate value="${vote.dateFinish}" pattern="dd.MM.yyyy HH:mm"/></td>
                        <td>
                            <span class="status ${vote.status eq 'Активно' ? 'active' : 'finished'}">
                                ${vote.status}
                            </span>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/vote/view/${vote.id}" class="btn small">Просмотр</a>
                            <a href="${pageContext.request.contextPath}/vote/edit/${vote.id}" class="btn small">Редактировать</a>
                            <a href="${pageContext.request.contextPath}/vote/delete/${vote.id}" class="btn small danger"
                               onclick="return confirm('Удалить голосование?')">Удалить</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </main>
</div>
</body>
</html>