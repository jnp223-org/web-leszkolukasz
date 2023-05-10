<%@ page import="java.util.List" %>
<%@ page import="library.orm.Book" %>
<html>
<head>
    <title>Book Table</title>
</head>
<body>
<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Author</th>
    </tr>
    </thead>
    <tbody>
    <%
        List<Book> books = (List<Book>) request.getAttribute("books");
        if (books != null) {
            for (Book book : books) {
    %>
    <tr>
        <td><%= book.id() %></td>
        <td><%= book.name() %></td>
        <td><%= book.author() %></td>
    </tr>
    <%
        }
    } else {
    %>
    <tr>
        <td colspan="3">No books found.</td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>
</body>
</html>