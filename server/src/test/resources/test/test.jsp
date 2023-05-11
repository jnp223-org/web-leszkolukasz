<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
<body>
Hello world ${year}
<br>
Hello world ${(Integer)( request.getAttribute("year") ) + 54}
<br>
<%-- comment --%>
<%! int x = 10; %>
<% out.println(x * 2); %>
<br>
<% for (int i = 0; i < 5; i++) {%>
<%= i - 1 %> <br>
<% } %>
<% List<Integer> l = new ArrayList<>();
    l.add(10);
    l.add(20);
    l.add(30);
    out.println(l.size());
%>
</body>
</html>