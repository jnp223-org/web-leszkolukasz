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
        // Retrieve the list of books from the request attribute
        List<Book> books = (List<Book>) request.getAttribute("books");
        if (books != null) {
            // Loop through each book and display its details in a table row
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
