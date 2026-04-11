<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Результаты голосования</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <div class="user-info">
            <h1>📊 Результаты голосования</h1>
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
                <li><a href="${pageContext.request.contextPath}/choice" class="active">📊 Результаты</a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="logout">🚪 Выход</a></li>
            </ul>
        </nav>
    </header>

    <main>
        <div class="page-title">
            <h2>Все результаты</h2>
            <p>Просмотр результатов голосований (${fn:length(choices)} записей)</p>
        </div>

        <div class="actions">
            <c:if test="${userRole eq 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/choice/new" class="btn btn-primary">
                    <span>➕</span> Добавить результат
                </a>
            </c:if>
            <c:if test="${userRole ne 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/votePage" class="btn btn-primary">
                    <span>🗳️</span> Проголосовать
                </a>
            </c:if>
        </div>

        <c:if test="${not empty param.success}">
            <div class="alert success">
                <span>✅</span> Операция выполнена успешно
            </div>
        </c:if>

        <c:if test="${empty choices}">
            <div class="empty-state">
                <div class="empty-icon">📊</div>
                <h3>Результатов пока нет</h3>
                <p>Результаты голосований появятся после проведения голосования</p>
            </div>
        </c:if>

        <c:if test="${not empty choices}">
            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Вопрос</th>
                            <th>Пользователь</th>
                            <th>Выбор</th>
                            <th>Действия</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="choice" items="${choices}">
                            <tr>
                                <td data-label="ID">${choice.id}</td>
                                <td data-label="Вопрос">
                                    <strong>${choice.question.content}</strong>
                                </td>
                                <td data-label="Пользователь">
                                    <div class="flex items-center gap-sm">
                                        <div class="avatar">${choice.user.fullName.substring(0, 1)}</div>
                                        <span>${choice.user.fullName}</span>
                                    </div>
                                </td>
                                <td data-label="Выбор">
                                    <span class="badge">${choice.choiceUser}</span>
                                </td>
                                <td data-label="Действия">
                                    <div class="flex gap-sm">
                                        <a href="${pageContext.request.contextPath}/choice/view/${choice.id}" class="btn small btn-outline">
                                            👁️
                                        </a>
                                        <c:if test="${userRole eq 'ADMIN'}">
                                            <a href="${pageContext.request.contextPath}/choice/edit/${choice.id}" class="btn small btn-primary">
                                                ✏️
                                            </a>
                                            <a href="${pageContext.request.contextPath}/choice/delete/${choice.id}" class="btn small btn-danger"
                                               onclick="return confirm('Удалить результат?')">
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
