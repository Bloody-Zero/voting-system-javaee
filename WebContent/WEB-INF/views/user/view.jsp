<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Просмотр пользователя</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <h1>Пользователь: ${user.fullName}</h1>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/">Главная</a></li>
                <li><a href="${pageContext.request.contextPath}/user">Пользователи</a></li>
            </ul>
        </nav>
    </header>
    <main>
        <div class="detail-card">
            <p><strong>ID:</strong> ${user.id}</p>
            <p><strong>Фамилия:</strong> ${user.lastName}</p>
            <p><strong>Имя:</strong> ${user.firstName}</p>
            <p><strong>Email:</strong> ${user.email}</p>
            <p><strong>Телефон:</strong> ${user.phone}</p>
            <p><strong>Статус:</strong> <span class="status ${user.status eq 'Голосовал' ? 'voted' : 'not-voted'}">${user.status}</span></p>
        </div>

        <h2>Голоса пользователя</h2>
        <c:if test="${empty user.choices}">
            <p>Пользователь ещё не голосовал</p>
        </c:if>
        <c:forEach var="choice" items="${user.choices}">
            <div class="choice-item">
                <p><strong>Вопрос:</strong> ${choice.question.content} (голосование: ${choice.question.vote.title})</p>
                <p><strong>Выбор:</strong> ${choice.choiceUser}</p>
            </div>
        </c:forEach>

        <div class="actions">
            <a href="${pageContext.request.contextPath}/user/edit/${user.id}" class="btn">Редактировать</a>
            <a href="${pageContext.request.contextPath}/user/delete/${user.id}" class="btn danger"
               onclick="return confirm('Удалить пользователя?')">Удалить</a>
            <a href="${pageContext.request.contextPath}/user" class="btn">К списку</a>
        </div>
    </main>
</div>
</body>
</html>