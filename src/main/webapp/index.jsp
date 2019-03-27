<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %> 
<html>
   <head>
    <script type="text/javascript">
            function servletCaller(param) {
                document.forms[0].action = "NamedEntityFirst?hashtag=" + param;
                document.forms[0].submit();
            }
        </script>
   </head>

   <body>
  
      <c:forEach items="${hashtags}"  var="employ" >
    <a href="#" onclick="servletCaller('\'+${employ.desc}+\'')">
      <div>${employ._id}) ${employ.desc}</div>
      </a>
       </c:forEach>
       
       
   </body>
</html>