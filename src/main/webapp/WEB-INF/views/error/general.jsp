<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ошибка</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="error-page">
<div class="error-page">
    <div class="error-icon">❌</div>
    <h2>Произошла ошибка</h2>
    <p>${exception.message}</p>
    <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
        <span>🏠</span> На главную
    </a>
</div>
</body>
</html>
