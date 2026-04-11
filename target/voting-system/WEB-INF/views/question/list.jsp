<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Управление вопросами</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <div class="user-info">
            <h1>❓ Вопросы</h1>
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
        <div class="page-title">
            <h2>Все вопросы</h2>
            <p>Управление вопросами в голосованиях</p>
        </div>

        <div class="actions">
            <c:if test="${userRole eq 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/question/new" class="btn btn-primary">
                    <span>➕</span> Создать вопрос
                </a>
            </c:if>
        </div>

        <c:if test="${not empty param.success}">
            <div class="alert success">
                <span>✅</span> Операция выполнена успешно
            </div>
        </c:if>

        <c:if test="${empty questions}">
            <div class="empty-state">
                <div class="empty-icon">❓</div>
                <h3>Вопросов пока нет</h3>
                <p>Создайте первый вопрос для голосования</p>
                <c:if test="${userRole eq 'ADMIN'}">
                    <a href="${pageContext.request.contextPath}/question/new" class="btn btn-primary">
                        <span>➕</span> Создать вопрос
                    </a>
                </c:if>
            </div>
        </c:if>

        <c:if test="${not empty questions}">
            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Голосование</th>
                            <th>Содержание</th>
                            <th>Дата</th>
                            <th>Действия</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="question" items="${questions}">
                            <tr>
                                <td data-label="ID">${question.id}</td>
                                <td data-label="Голосование">
                                    <span class="tag primary">${question.vote.title}</span>
                                </td>
                                <td data-label="Содержание"><strong>${question.content}</strong></td>
                                <td data-label="Дата">${question.dateVoteFormatted}</td>
                                <td data-label="Действия">
                                    <div class="flex gap-sm">
                                        <a href="${pageContext.request.contextPath}/question/view/${question.id}" class="btn small btn-outline">
                                            👁️
                                        </a>
                                        <c:if test="${userRole eq 'ADMIN'}">
                                            <a href="${pageContext.request.contextPath}/question/edit/${question.id}" class="btn small btn-primary">
                                                ✏️
                                            </a>
                                            <a href="${pageContext.request.contextPath}/question/delete/${question.id}" class="btn small btn-danger"
                                               onclick="return confirm('Удалить вопрос?')">
                                                🗑️
                                            </a>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
    </main>

    <footer>
        <p>&copy; 2026 Система голосования. Курсовой проект по Java EE.</p>
    </footer>
</div>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
