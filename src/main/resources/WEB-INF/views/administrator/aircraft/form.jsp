<%--
- form.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<%@taglib prefix='acmeans' tagdir="/WEB-INF/tags"%>

<acme:form>
	<acme:input-textbox code="administrator.aircraft.form.label.model" path="model"/>
	<acme:input-textbox code="administrator.aircraft.form.label.registrationNumber" path="registrationNumber"/>
	<acme:input-textbox code="administrator.aircraft.form.label.capacity" path="capacity"/>
	<acme:input-textbox code="administrator.aircraft.form.label.cargoWeight" path="cargoWeight"/>
	<acme:input-textbox code="administrator.aircraft.form.label.status" path="status" readonly="true"/>
	<acme:input-textbox code="administrator.aircraft.form.label.optionalDetails" path="optionalDetails"/>
	<acme:input-textbox code="administrator.aircraft.form.label.airline" path="airline"/>
	
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|disable')}">
			<acme:submit code="administrator.aircraft.form.button.update" action="/administrator/aircraft/update"/>
			<acme:submit code="administrator.aircraft.form.button.disable" action="/administrator/aircraft/disable"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="administrator.aircraft.form.button.create" action="/administrator/aircraft/create"/>
		</jstl:when>
	</jstl:choose>
		
</acme:form>