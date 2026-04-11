<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Пользователи</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <div class="user-info">
            <h1>👥 Пользователи</h1>
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
        <div class="page-title">
            <h2>Все пользователи</h2>
            <p>Управление пользователями системы (${fn:length(users)} чел.)</p>
        </div>

        <div class="actions">
            <c:if test="${userRole eq 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/user/new" class="btn btn-primary">
                    <span>➕</span> Добавить пользователя
                </a>
            </c:if>
        </div>

        <c:if test="${param.success == 'created'}">
            <div class="alert success">
                <span>✅</span> Пользователь успешно добавлен
            </div>
        </c:if>
        <c:if test="${param.success == 'deleted'}">
            <div class="alert success">
                <span>✅</span> Пользователь удалён
            </div>
        </c:if>

        <c:if test="${empty users}">
            <div class="empty-state">
                <div class="empty-icon">👥</div>
                <h3>Пользователей пока нет</h3>
                <p>Добавьте первого пользователя в систему</p>
                <c:if test="${userRole eq 'ADMIN'}">
                    <a href="${pageContext.request.contextPath}/user/new" class="btn btn-primary">
                        <span>➕</span> Добавить пользователя
                    </a>
                </c:if>
            </div>
        </c:if>

        <c:if test="${not empty users}">
            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Пользователь</th>
                            <th>Email</th>
                            <th>Телефон</th>
                            <th>Статус</th>
                            <th>Действия</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${users}">
                            <tr>
                                <td data-label="ID">${u.id}</td>
                                <td data-label="Пользователь">
                                    <div class="flex items-center gap-md">
                                        <div class="avatar">${u.firstName.substring(0, 1)}</div>
                                        <div>
                                            <strong>${u.lastName} ${u.firstName}</strong>
                                        </div>
                                    </div>
                                </td>
                                <td data-label="Email">📧 ${u.email}</td>
                                <td data-label="Телефон">📱 ${u.phone}</td>
                                <td data-label="Статус">
                                    <span class="status ${u.status eq 'VOTED' ? 'voted' : 'not-voted'}">
                                        <c:choose>
                                            <c:when test="${u.status == 'VOTED'}">✅ Голосовал</c:when>
                                            <c:when test="${u.status == 'NOT_VOTED'}">⏳ Не голосовал</c:when>
                                            <c:otherwise>${u.status}</c:otherwise>
                                        </c:choose>
                                    </span>
                                </td>
                                <td data-label="Действия">
                                    <div class="flex gap-sm">
                                        <a href="${pageContext.request.contextPath}/user/view/${u.id}" class="btn small btn-outline">
                                            👁️
                                        </a>
                                        <c:if test="${userRole eq 'ADMIN'}">
                                            <a href="${pageContext.request.contextPath}/user/edit/${u.id}" class="btn small btn-primary">
                                                ✏️
                                            </a>
                                            <a href="${pageContext.request.contextPath}/user/delete/${u.id}" class="btn small btn-danger"
                                               onclick="return confirm('Удалить пользователя?')">
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
