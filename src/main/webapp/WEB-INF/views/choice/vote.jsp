<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Голосование</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <div class="user-info">
            <h1>🗳️ Голосование</h1>
            <div class="user-badge">
                👤 ${userName}
                <span class="role">${userRole eq 'ADMIN' ? '🔑 Администратор' : '👤 Пользователь'}</span>
            </div>
        </div>
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/">🏠 Главная</a></li>
                <li><a href="${pageContext.request.contextPath}/vote">📋 Голосования</a></li>
                <li><a href="${pageContext.request.contextPath}/choice">📊 Результаты</a></li>
                <li><a href="${pageContext.request.contextPath}/logout" class="logout">🚪 Выход</a></li>
            </ul>
        </nav>
    </header>

    <main>
        <div class="page-title">
            <h2>Участие в голосовании</h2>
            <p>Отдайте свой голос по доступным вопросам</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert error">
                <span>⚠️</span> ${error}
            </div>
        </c:if>
        <c:if test="${param.success == 'voted'}">
            <div class="alert success">
                <span>✅</span> Ваш голос успешно принят!
            </div>
        </c:if>

        <%-- Ваши уже отданные голоса --%>
        <c:if test="${not empty userChoices}">
            <section class="mb-xl">
                <h2 class="section-title">✅ Ваши голоса</h2>
                <c:forEach var="c" items="${userChoices}">
                    <div class="vote-card">
                        <h3>${c.question.content}</h3>
                        <p>📋 <strong>Голосование:</strong> ${c.question.vote.title}</p>
                        <p>✅ <strong>Ваш выбор:</strong> <span class="badge">${c.choiceUser}</span></p>
                    </div>
                </c:forEach>
            </section>
        </c:if>

        <%-- Форма голосования --%>
        <section>
            <h2 class="section-title">Отдать голос</h2>
            <div class="form-container">
                <form action="${pageContext.request.contextPath}/votePage" method="post">
                    <div class="form-group">
                        <label for="questionId">Выберите вопрос</label>
                        <select id="questionId" name="questionId" required>
                            <option value="">— Выберите вопрос —</option>
                            <c:forEach var="q" items="${questions}">
                                <c:set var="alreadyVoted" value="false"/>
                                <c:forEach var="uc" items="${userChoices}">
                                    <c:if test="${uc.question.id == q.id}">
                                        <c:set var="alreadyVoted" value="true"/>
                                    </c:if>
                                </c:forEach>
                                <option value="${q.id}" ${alreadyVoted ? 'disabled' : ''}>
                                    ${q.content} — ${q.vote.title}
                                    <c:if test="${alreadyVoted}">(уже проголосовали)</c:if>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="choiceUser">Ваш выбор</label>
                        <input type="text" id="choiceUser" name="choiceUser"
                               placeholder="Введите ваш вариант ответа" required>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            <span>🗳️</span> Отдать голос
                        </button>
                        <a href="${pageContext.request.contextPath}/choice" class="btn btn-outline">К результатам</a>
                    </div>
                </form>
            </div>
        </section>
    </main>

    <footer>
        <p>&copy; 2026 Система голосования. Курсовой проект по Java EE.</p>
    </footer>
</div>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
