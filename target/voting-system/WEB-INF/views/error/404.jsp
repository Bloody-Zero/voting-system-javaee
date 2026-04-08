<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>404 — Страница не найдена</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <h1>404</h1>
    </header>
    <main>
        <div class="error-page">
            <h2>Страница не найдена</h2>
            <p>Запрашиваемая страница не существует или была удалена.</p>
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">На главную</a>
        </div>
    </main>
</div>
</body>
</html>
