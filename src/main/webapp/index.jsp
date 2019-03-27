<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %> 
<html>
   <head>
       </head>

   <body>
  
      <c:forEach items="${hashtags}"  var="employ" >
  
  <a href="NamedEntityFirst?param=${employ.desc}">
      
<div>${employ._id}) ${employ.desc}</div>
      </a>

       </c:forEach>


       
       
   </body>
</html>