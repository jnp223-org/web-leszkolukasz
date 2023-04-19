<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <title>Add Book</title>
</head>
<body>
<h1>Add Book</h1>
<form action="/app/books/add" method="post">
    <label for="name">Name:</label>
    <input type="text" id="name" name="name" required><br>
    <label for="author">Author:</label>
    <input type="text" id="author" name="author" required><br>
    <input type="submit" value="Add Book">
</form>
</body>
</html>
