<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty user ? "Новый пользователь" : "Редактирование пользователя"}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <div class="user-info">
            <h1>${empty user ? "Добавление" : "Редактирование"} пользователя</h1>
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
        <div class="form-container">
            <form id="userForm" action="${pageContext.request.contextPath}/user/save" method="post">
                <input type="hidden" name="id" value="${user.id}">
                <div class="form-group">
                    <label>Имя *</label>
                    <input type="text" name="firstName" value="${user.firstName}" required>
                </div>
                <div class="form-group">
                    <label>Фамилия *</label>
                    <input type="text" name="lastName" value="${user.lastName}" required>
                </div>
                <div class="form-group">
                    <label>Email *</label>
                    <input type="email" name="email" id="email" value="${user.email}" required>
                </div>
                <div class="form-group">
                    <label>Телефон</label>
                    <input type="tel" name="phone" id="phone" value="${user.phone}">
                </div>
                <div class="form-group">
                    <label>Статус</label>
                    <select name="status">
                        <option value="Не голосовал" ${user.status eq 'Не голосовал' ? 'selected' : ''}>Не голосовал</option>
                        <option value="Голосовал" ${user.status eq 'Голосовал' ? 'selected' : ''}>Голосовал</option>
                    </select>
                </div>
                <c:if test="${not empty errors}">
                    <div class="alert error">
                        <c:forEach items="${errors}" var="err">${err.value}<br></c:forEach>
                    </div>
                </c:if>
                <button type="submit" class="btn btn-primary">Сохранить</button>
                <a href="${pageContext.request.contextPath}/user" class="btn">Отмена</a>
            </form>
        </div>
    </main>
</div>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>