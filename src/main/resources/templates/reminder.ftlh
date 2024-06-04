<#ftl encoding="UTF-8">
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Срок выполнения задачи истекает менее чем через 1 час</title>
    <style>
        #body {
            display: flex;
            flex-direction: column;
            font-family: "Segoe UI", serif;
            align-items: center;
            background-color: #607FF2;
            font-size: 20px;
            height: 100%;
        }

        #div {
            display: block;
            flex-direction: column;
            margin: 10%;
            font-family: "Segoe UI", serif;
            background-color: white;
            padding: 3%;
            border-radius: 20px;
            color: black;
        }
    </style>
</head>
<body>
<div id="body">
    <div id="div">
        <h1 style="text-align: center">Напоминание</h1>
        <p>Привет, ${name}!</p>
        <p>Это автоматическое оповещение, что у тебя есть задача, срок выполнения которой истекает менее чем через 1
            час.</p>
        <div>
            <hr>
            <p>Задача: ${title}</p>
            <#if description??>
                <p>Описание: ${description}</p>
            </#if>
            <hr>
        </div>
        <p>С уважением, <br> Tasklist Team</p>
    </div>
</div>
</body>
</html>