<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty vote ? 'Добавление' : 'Редактирование'} голосования</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <div class="user-info">
            <h1>📝 ${empty vote ? 'Создание' : 'Редактирование'} голосования</h1>
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
            <c:if test="${not empty errors}">
                <div class="alert error">
                    <span>⚠️</span>
                    <ul style="margin: 0; padding-left: 1.2rem;">
                        <c:forEach var="error" items="${errors}">
                            <li>${error.value}</li>
                        </c:forEach>
                    </ul>
                </div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert error">
                    <span>⚠️</span> ${error}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/vote/save" method="post" class="data-form">
                <input type="hidden" name="id" value="${vote.id}">

                <div class="form-group">
                    <label for="title">Название голосования</label>
                    <input type="text" id="title" name="title" value="${vote.title}" placeholder="Введите название голосования" required>
                </div>

                <div class="grid grid-2" style="gap: 1rem;">
                    <div class="form-group">
                        <label for="dateStart">Дата и время начала</label>
                        <input type="datetime-local" id="dateStart" name="dateStart"
                               value="${vote.dateStartFormatted}" required>
                    </div>
                    <div class="form-group">
                        <label for="dateFinish">Дата и время окончания</label>
                        <input type="datetime-local" id="dateFinish" name="dateFinish"
                               value="${vote.dateFinishFormatted}" required>
                    </div>
                </div>

                <div class="form-group">
                    <label for="status">Статус голосования</label>
                    <select id="status" name="status" required>
                        <option value="ACTIVE" ${vote.status == 'ACTIVE' ? 'selected' : ''}>🟢 Активно</option>
                        <option value="COMPLETED" ${vote.status == 'COMPLETED' ? 'selected' : ''}>⚫ Завершено</option>
                        <option value="DRAFT" ${vote.status == 'DRAFT' ? 'selected' : ''}>🟡 Черновик</option>
                    </select>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <span>💾</span> Сохранить
                    </button>
                    <a href="${pageContext.request.contextPath}/vote" class="btn btn-outline">Отмена</a>
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
