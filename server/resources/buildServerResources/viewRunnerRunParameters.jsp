<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<div class="parameter">
  <c:choose>
    <c:when test="${not empty propertiesBean.properties['script.namespaces']}">
      Additional namespaces: <strong><props:displayValue name="script.namespaces" /></strong>
    </c:when>
  </c:choose>
</div>
<div class="parameter">
  <c:choose>
    <c:when test="${not empty propertiesBean.properties['script.references']}">
      Additional references: <strong><props:displayValue name="script.references" /></strong>
    </c:when>
  </c:choose>
</div>

<div class="parameter">
  C# code: <strong><props:displayValue name="script.content" /></strong>
</div>