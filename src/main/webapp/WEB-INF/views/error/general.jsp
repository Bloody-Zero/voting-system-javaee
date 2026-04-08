<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ошибка</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <h1>Ошибка</h1>
    </header>
    <main>
        <div class="error-page">
            <h2>Произошла ошибка</h2>
            <p>${exception.message}</p>
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">На главную</a>
        </div>
    </main>
</div>
</body>
</html>
