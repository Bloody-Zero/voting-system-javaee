<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty question ? 'Добавление' : 'Редактирование'} вопроса</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <div class="user-info">
            <h1>📝 ${empty question ? 'Создание' : 'Редактирование'} вопроса</h1>
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
                    <li><a href="${pageContext.request.contextPath}/question" class="active">❓ Вопросы</a></li>
                    <li><a href="${pageContext.request.contextPath}/user">👥 Пользователи</a></li>
                </c:if>
                <li><a href="${pageContext.request.contextPath}/choice">📊 Результаты</a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="logout">🚪 Выход</a></li>
            </ul>
        </nav>
    </header>

    <main>
        <div class="form-container">
            <c:if test="${not empty error}">
                <div class="alert error">
                    <span>⚠️</span> ${error}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/question/save" method="post" class="data-form">
                <input type="hidden" name="id" value="${question.id}">

                <div class="form-group">
                    <label for="voteId">Голосование</label>
                    <select id="voteId" name="voteId" required>
                        <c:forEach var="vote" items="${votes}">
                            <option value="${vote.id}"
                                ${question != null && question.vote != null && question.vote.id == vote.id ? 'selected' : ''}>
                                ${vote.title}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="content">Содержание вопроса</label>
                    <textarea id="content" name="content" rows="4" placeholder="Введите текст вопроса" required>${question.content}</textarea>
                </div>

                <div class="form-group">
                    <label for="dateVote">Дата голосования</label>
                    <input type="datetime-local" id="dateVote" name="dateVote"
                           value="${not empty question ? question.dateVoteForInput : ''}" required>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <span>💾</span> Сохранить
                    </button>
                    <a href="${pageContext.request.contextPath}/question" class="btn btn-outline">Отмена</a>
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
