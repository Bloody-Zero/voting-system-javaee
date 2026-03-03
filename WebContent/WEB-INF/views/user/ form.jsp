<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${empty user ? 'Добавление' : 'Редактирование'} пользователя</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <h1>${empty user ? 'Добавление' : 'Редактирование'} пользователя</h1>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/">Главная</a></li>
                <li><a href="${pageContext.request.contextPath}/user">Пользователи</a></li>
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

        <form action="${pageContext.request.contextPath}/user/save" method="post" class="data-form">
            <input type="hidden" name="id" value="${user.id}">

            <div class="form-group">
                <label for="lastName">Фамилия:</label>
                <input type="text" id="lastName" name="lastName" value="${user.lastName}" required>
            </div>

            <div class="form-group">
                <label for="firstName">Имя:</label>
                <input type="text" id="firstName" name="firstName" value="${user.firstName}" required>
            </div>

            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" value="${user.email}" required>
            </div>

            <div class="form-group">
                <label for="phone">Телефон:</label>
                <input type="tel" id="phone" name="phone" value="${user.phone}">
            </div>

            <div class="form-group">
                <label for="status">Статус:</label>
                <select id="status" name="status" required>
                    <option value="Не голосовал" ${user.status == 'Не голосовал' ? 'selected' : ''}>Не голосовал</option>
                    <option value="Голосовал" ${user.status == 'Голосовал' ? 'selected' : ''}>Голосовал</option>
                </select>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Сохранить</button>
                <a href="${pageContext.request.contextPath}/user" class="btn">Отмена</a>
            </div>
        </form>
    </main>
</div>
</body>
</html>