<jsp:root xmlns:c="http://java.sun.com/jsp/jstl/core" xmlns:fn="http://java.sun.com/jsp/jstl/functions" xmlns:util="urn:jsptagdir:/WEB-INF/tags/util" xmlns:spring="http://www.springframework.org/tags" xmlns:form="http://www.springframework.org/tags/form" xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0">
  <jsp:output omit-xml-declaration="yes" />

  <jsp:directive.attribute name="id" type="java.lang.String" required="true" rtexprvalue="true" description="The identifier for this tag (do not change!)" />
  <jsp:directive.attribute name="property" type="java.lang.String" required="true" rtexprvalue="true" description="The property (field name) of the dataset to be displayed in a column (required)." />
  <jsp:directive.attribute name="maxLength" type="java.lang.Integer" required="false" rtexprvalue="true" description="Max displayed text length (default '10'). Unlimited if negative" />
  <jsp:directive.attribute name="label" type="java.lang.String" required="false" rtexprvalue="true" description="The column label to be used in the table (optional)." />
  <jsp:directive.attribute name="render" type="java.lang.Boolean" required="false" rtexprvalue="true" description="Indicate if the contents of this tag and all enclosed tags should be rendered (default 'true')" />
  <jsp:directive.attribute name="z" type="java.lang.String" required="false" description="Used for checking if element has been modified (to recalculate simply provide empty string value)" />

  <c:if test="${empty render or render}">
    <c:if test="${empty label}">
      <spring:message code="label_${fn:toLowerCase(fn:substringAfter(id,'_'))}" var="label" htmlEscape="false" />
    </c:if>
    <c:choose>
      <c:when test="${empty columnProperties and empty columnLabels}">
        <c:set var="columnProperties" value="${property}" scope="request" />
        <c:set var="columnLabels" value="${label}" scope="request" />
        <c:set var="columnMaxLengths" value="${empty maxLength ? 10 : maxLength}" scope="request" />
      </c:when>
      <c:otherwise>
        <c:set var="columnProperties" value="${columnProperties},${property}" scope="request" />
        <c:set var="columnLabels" value="${columnLabels},${label}" scope="request" />
        <c:set var="columnMaxLengths" value="${columnMaxLengths},${empty maxLength ? 10 : maxLength}" scope="request" />
      </c:otherwise>
    </c:choose>
  </c:if>
</jsp:root>