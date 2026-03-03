<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${empty choice ? 'Добавление' : 'Редактирование'} результата</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <h1>${empty choice ? 'Добавление' : 'Редактирование'} результата</h1>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/">Главная</a></li>
                <li><a href="${pageContext.request.contextPath}/choice">Результаты</a></li>
            </ul>
        </nav>
    </header>
    <main>
        <c:if test="${not empty error}">
            <div class="alert error">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/choice/save" method="post" class="data-form">
            <input type="hidden" name="id" value="${choice.id}">

            <div class="form-group">
                <label for="questionId">Вопрос:</label>
                <select id="questionId" name="questionId" required>
                    <c:forEach var="question" items="${questions}">
                        <option value="${question.id}" ${choice.question.id == question.id ? 'selected' : ''}>
                            ${question.content} (голосование: ${question.vote.title})
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="userId">Пользователь:</label>
                <select id="userId" name="userId" required>
                    <c:forEach var="user" items="${users}">
                        <option value="${user.id}" ${choice.user.id == user.id ? 'selected' : ''}>
                            ${user.fullName} (${user.email})
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="choiceUser">Выбор:</label>
                <input type="text" id="choiceUser" name="choiceUser" value="${choice.choiceUser}" required>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Сохранить</button>
                <a href="${pageContext.request.contextPath}/choice" class="btn">Отмена</a>
            </div>
        </form>
    </main>
</div>
</body>
</html>