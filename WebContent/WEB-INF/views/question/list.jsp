<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Управление вопросами</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <h1>Вопросы</h1>
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
            <a href="${pageContext.request.contextPath}/question/new" class="btn btn-primary">Добавить вопрос</a>
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
                    <th>Дата голосования</th>
                    <th>Действия</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="question" items="${questions}">
                    <tr>
                        <td>${question.id}</td>
                        <td>${question.vote.title}</td>
                        <td>${question.content}</td>
                        <td><fmt:formatDate value="${question.dateVote}" pattern="dd.MM.yyyy HH:mm"/></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/question/view/${question.id}" class="btn small">Просмотр</a>
                            <a href="${pageContext.request.contextPath}/question/edit/${question.id}" class="btn small">Редактировать</a>
                            <a href="${pageContext.request.contextPath}/question/delete/${question.id}" class="btn small danger"
                               onclick="return confirm('Удалить вопрос?')">Удалить</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </main>
</div>
</body>
</html>