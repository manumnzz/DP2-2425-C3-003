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
	<jstl:if  test="${acme:anyOf(_command, 'show|update|publish|delete') }">
	<acme:input-moment	code="assistance-agent.form.label.registration-moment"	path="moment" readonly="true"/>
	</jstl:if>
	<acme:input-textarea code="assistance-agent.form.label.description" path="description"/>
	<acme:input-textbox code="assistance-agent.form.label.passenger-email" path="passengerEmail"/>
	<acme:input-select code="assistance-agent.form.label.claim-type" path="typeClaim" choices="${typeClaim}"/>
	<jstl:if  test="${acme:anyOf(_command, 'show|update|publish|delete') }">
		<acme:input-textarea code="assistance-agent.form.label.status" path="status" readonly="true"/>
	</jstl:if>
	
	
	<acme:input-select code="assistance-agent.form.label.leg" path="leg" choices="${legs}"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode}">
			<acme:input-checkbox code="assistance-agent.claim.form.label.confirmation" path="confirmation"/>
			<acme:submit code="assistance-agent.claim.form.button.update" action="/assistance-agent/claim/update"/>
			<acme:submit code="assistance-agent.claim.form.button.delete" action="/assistance-agent/claim/delete"/>
			<acme:submit code="assistance-agent.claim.form.button.publish" action="/assistance-agent/claim/publish"/>
			<acme:button code="assistance-agent.claim.form.button.tracking-log" action="/assistance-agent/tracking-log/list?masterId=${id}"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="assistance-agent.claim.form.label.confirmation" path="confirmation"/>
			<acme:submit code="assistance-agent.claim.form.button.create" action="/assistance-agent/claim/create"/>
		</jstl:when>	
			<jstl:when test="${_command == 'show'}">
			<acme:button code="assistance-agent.claim.form.button.tracking-log" action="/assistance-agent/tracking-log/list?masterId=${id}"/>			
		</jstl:when>	
	</jstl:choose>
</acme:form>