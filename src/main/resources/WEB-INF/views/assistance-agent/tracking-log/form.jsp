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

<acme:form>
	<acme:input-moment code="assistanceagent.trackinglog.form.label.updateTime" path="updateTime"/>
	<acme:input-textbox code="assistanceagent.trackinglog.form.label.step" path="step"/>
	<acme:input-integer code="assistanceagent.trackinglog.form.label.resolutionPercentage" path="resolutionPercentage"/>
	<acme:input-checkbox code="assistanceagent.trackinglog.form.label.indicator" path="indicator"/>
	<acme:input-textbox code="assistanceagent.trackinglog.form.label.resolution" path="resolution"/>
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">
			<acme:submit code="assistanceagent.trackinglog.form.button.update" action="/assistance-agent/tracking-log/update"/>
			<acme:submit code="assistanceagent.trackinglog.form.button.delete" action="/assistance-agent/tracking-log/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistanceagent.trackinglog.form.button.create" action="/assistance-agent/tracking-log/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>
