
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>JobGit</title>
  </head>
  <body>
<%--  <table style=" width: 100%; border: 4px double black;">--%>
<%--    <tr>--%>
<%--      <td style="border: 1px solid black; text-align: center">--%>
<%--        <a href="/add.jsp">Add user</a>--%>
<%--      </td>--%>
<%--      <td style="border: 1px solid black; text-align: center">--%>
<%--        <a href="/list.jsp">List user</a>--%>
<%--      </td>--%>
<%--    </tr>--%>
<%--  </table>--%>

  <form action="/add" method="POST">
    name: <input type="text" name="name" />
    password: <input type="password" name="password" />
    money:<input type="text" name="money" />
    <input type="submit" value="Add User">


  </form>

  <p>Users: ${emailReg1}</p>

  </body>
</html>
