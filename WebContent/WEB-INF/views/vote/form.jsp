<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${empty vote ? 'Добавление' : 'Редактирование'} голосования</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <h1>${empty vote ? 'Добавление' : 'Редактирование'} голосования</h1>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/">Главная</a></li>
                <li><a href="${pageContext.request.contextPath}/vote">Голосования</a></li>
            </ul>
        </nav>
    </header>
    <main>
        <c:if test="${not empty errors}">
            <div class="alert error">
                <ul>
                    <c:forEach var="error" items="${errors}">
                        <li>${error.value}</li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert error">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/vote/save" method="post" class="data-form">
            <input type="hidden" name="id" value="${vote.id}">

            <div class="form-group">
                <label for="title">Название голосования:</label>
                <input type="text" id="title" name="title" value="${vote.title}" required>
            </div>

            <div class="form-group">
                <label for="dateStart">Дата начала:</label>
                <input type="datetime-local" id="dateStart" name="dateStart"
                       value="${vote.dateStart != null ? vote.dateStart.toLocalDateTime() : ''}" required>
            </div>

            <div class="form-group">
                <label for="dateFinish">Дата окончания:</label>
                <input type="datetime-local" id="dateFinish" name="dateFinish"
                       value="${vote.dateFinish != null ? vote.dateFinish.toLocalDateTime() : ''}" required>
            </div>

            <div class="form-group">
                <label for="status">Статус:</label>
                <select id="status" name="status" required>
                    <option value="Активно" ${vote.status == 'Активно' ? 'selected' : ''}>Активно</option>
                    <option value="Завершено" ${vote.status == 'Завершено' ? 'selected' : ''}>Завершено</option>
                    <option value="Черновик" ${vote.status == 'Черновик' ? 'selected' : ''}>Черновик</option>
                </select>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Сохранить</button>
                <a href="${pageContext.request.contextPath}/vote" class="btn">Отмена</a>
            </div>
        </form>
    </main>
</div>
</body>
</html>