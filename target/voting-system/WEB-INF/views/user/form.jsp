<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
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
            <h1>👤 ${empty user ? "Создание" : "Редактирование"} пользователя</h1>
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
                    <li><a href="${pageContext.request.contextPath}/user" class="active">👥 Пользователи</a></li>
                </c:if>
                <li><a href="${pageContext.request.contextPath}/choice">📊 Результаты</a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="logout">🚪 Выход</a></li>
            </ul>
        </nav>
    </header>

    <main>
        <div class="form-container">
            <c:if test="${not empty errors}">
                <div class="alert error">
                    <span>⚠️</span>
                    <ul style="margin: 0; padding-left: 1.2rem;">
                        <c:forEach var="err" items="${errors}">
                            <li>${err.value}</li>
                        </c:forEach>
                    </ul>
                </div>
            </c:if>

            <form id="userForm" action="${pageContext.request.contextPath}/user/save" method="post" class="data-form">
                <input type="hidden" name="id" value="${user.id}">
                
                <div class="grid grid-2" style="gap: 1rem;">
                    <div class="form-group">
                        <label for="firstName">Имя *</label>
                        <input type="text" id="firstName" name="firstName" value="${user.firstName}" placeholder="Иван" required>
                    </div>
                    <div class="form-group">
                        <label for="lastName">Фамилия *</label>
                        <input type="text" id="lastName" name="lastName" value="${user.lastName}" placeholder="Иванов" required>
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="email">Email *</label>
                    <input type="email" id="email" name="email" value="${user.email}" placeholder="ivan@example.com" required>
                </div>
                
                <div class="form-group">
                    <label for="phone">Телефон</label>
                    <input type="tel" id="phone" name="phone" value="${user.phone}" placeholder="+7 (900) 123-45-67">
                </div>
                
                <div class="form-group">
                    <label for="status">Статус голосования</label>
                    <select id="status" name="status">
                        <option value="NOT_VOTED" ${user.status eq 'NOT_VOTED' || empty user.status ? 'selected' : ''}>⏳ Не голосовал</option>
                        <option value="VOTED" ${user.status eq 'VOTED' ? 'selected' : ''}>✅ Голосовал</option>
                    </select>
                </div>
                
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <span>💾</span> Сохранить
                    </button>
                    <a href="${pageContext.request.contextPath}/user" class="btn btn-outline">Отмена</a>
                </div>
            </form>
        </div>
    </main>

    <footer>
        <p>&copy; 2026 Система голосования. Курсовой проект по Java EE.</p>
    </footer>
</div>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
