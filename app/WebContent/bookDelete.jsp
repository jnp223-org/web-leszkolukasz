<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <title>Delete Book</title>
</head>
<body>
<h1>Delete Book</h1>
<form action="/app/books/delete" method="post">
    <label for="id">Name:</label>
    <input type="text" id="id" name="id" required><br>
    <input type="submit" value="Delete Book">
</form>
</body>
</html>
