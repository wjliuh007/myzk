<%@ page contentType="text/html; charset=UTF-8"%>
<html>

<body style="margin:0 auto;text-align: center;">
    <h2>请登录</h2>
    <h2>${msg }</h2>
    <form name="from1" action="<%=request.getContextPath()%>/user/login.do" method="post">
        <table width="300" style="margin:0 auto;text-align: center;background-color: #2D7664" >

          <tr>
              <td >用户名</td>
              <td ><input type="text" name="username" size="20"></td>
          </tr>
          <tr>
              <td >密码</td>
              <td ><input type="password" name="password" size="20"></td>
          </tr>
          <tr>
               <td colspan ="2"> <input type="submit" name="submit" value="登录">
          </tr>

        </table>

    </form>

<%--
<form name="from2" action="user/register.do" method="post">
<table width="300" border="1">

<tr>
    <td colspan="2"> 登录窗口</td>
</tr>
<tr>
    <td > 用户名</td>
    <td > <input type="text" name="username" size="10"></td>
</tr>

<tr>
    <td > 密码</td>
    <td > <input type="password" name="password" size="10"></td>
</tr>

<tr>
     <td colspan ="2"> <input type="submit" name="submit" value="注册">
</tr>

 </table>

</form>
 --%>


</body>
</html>
