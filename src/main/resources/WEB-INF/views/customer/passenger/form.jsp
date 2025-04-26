<%-- 
- form.jsp 
- 
- Copyright (C) 2012-2025 Rafael Corchuelo.
- 
- In keeping with the traditional purpose of furthering education and research, it is 
- the policy of the copyright owner to permit non-commercial use and redistribution of 
- this software. It has been tested carefully, but it is not guaranteed for any particular 
- purposes. The copyright owner does not offer any warranties or representations, nor do 
- they accept any liabilities with respect to them. 
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>

<jstl:if test="${draftMode==true}">
    <acme:input-textbox code="customer.passenger.form.label.fullName" path="fullName"/>
    <acme:input-textbox code="customer.passenger.form.label.email" path="email"/>
    <acme:input-textbox code="customer.passenger.form.label.passportNumber" path="passportNumber"/>
    <acme:input-textbox code="customer.passenger.form.label.dateOfBirth" path="dateOfBirth"/>
    <acme:input-textarea code="customer.passenger.form.label.specialNeeds" path="specialNeeds"/>
	<acme:input-textbox code="customer.passenger.form.label.customer" path="customer" readonly="true"/>
	<acme:input-checkbox code="customer.passenger.form.label.draftMode" path="draftMode" readonly="true"/>

	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update') && draftMode == true}">
			<acme:submit code="customer.passenger.form.button.update" action="/customer/passenger/update"/>
			<acme:submit code="customer.passenger.form.button.publish" action="/customer/passenger/publish"/>
			
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.passenger.form.button.create" action="/customer/passenger/create"/>
		</jstl:when>		
	</jstl:choose>
</jstl:if>

<jstl:if test="${draftMode==false}">
	<acme:input-textbox code="customer.passenger.form.label.fullName" path="fullName" readonly="true"/>
	    <acme:input-textbox code="customer.passenger.form.label.email" path="email" readonly="true"/>
	    <acme:input-textbox code="customer.passenger.form.label.passportNumber" path="passportNumber" readonly="true"/>
	    <acme:input-textbox code="customer.passenger.form.label.dateOfBirth" path="dateOfBirth" readonly="true"/>
	    <acme:input-textarea code="customer.passenger.form.label.specialNeeds" path="specialNeeds" readonly="true"/>
		<acme:input-textbox code="customer.passenger.form.label.customer" path="customer" readonly="true"/>
		<acme:input-checkbox code="customer.passenger.form.label.draftMode" path="draftMode" readonly="true"/>
</jstl:if>

</acme:form>
