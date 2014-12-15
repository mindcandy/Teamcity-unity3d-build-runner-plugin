<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<div class="parameter">
  <c:choose>
    <c:when test="${not empty propertiesBean.properties['unity.executable']}">
      Working Directory: <strong><props:displayValue name="unity.executable" /></strong>
    </c:when>
  </c:choose>
</div>

<div class="parameter">
  <c:choose>
    <c:when test="${not empty propertiesBean.properties['teamcity.build.workingDir']}">
      Working Directory: <strong><props:displayValue name="teamcity.build.workingDir" /></strong>
    </c:when>
  </c:choose>
</div>


<div class="parameter">
  Batch Mode: <strong><props:displayValue name="argument.batch_mode" /></strong>
</div>

<div class="parameter">
  No Graphics: <strong><props:displayValue name="argument.no_graphics" /></strong>
</div>

<div class="parameter">
  Project Path: <strong><props:displayValue name="argument.project_path" /></strong>
</div>

<div class="parameter">
  Line List Path: <strong><props:displayValue name="argument.line_list_path" /></strong>
</div>

<div class="parameter">
  Build player: <strong><props:displayValue name="argument.build_player" /></strong>
</div>

<div class="parameter">
  Warnings As Errors: <strong><props:displayValue name="argument.warnings_as_errors" /></strong>
</div>

<div class="parameter">
  Build Path: <strong><props:displayValue name="argument.build_path" /></strong>
</div>

<div class="parameter">
  Build Extra: <strong><props:displayValue name="argument.build_extra" /></strong>
</div>

<div class="parameter">
  <c:choose>
    <c:when test="${not empty propertiesBean.properties['argument.execute_method']}">
      Execute Method: <strong><props:displayValue name="argument.execute_method" /></strong>
    </c:when>
  </c:choose>
</div>

<div class="parameter">
  Quit after build: <strong><props:displayValue name="argument.quit" /></strong>
</div>



<div class="parameter">
  Clear Output Before: <strong><props:displayValue name="argument.clear_output_before" /></strong>
</div>


<div class="parameter">
  Clean Output After: <strong><props:displayValue name="argument.clean_output_after" /></strong>
</div>

<div class="parameter">
  <c:choose>
    <c:when test="${propertiesBean.properties['argument.log_ignore']}">
      Ignore Errors Before: <strong><props:displayValue name="argument.log_ignore_text" /></strong>
    </c:when>
  </c:choose>
</div>
