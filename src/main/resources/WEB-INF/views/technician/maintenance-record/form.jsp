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

<jstl:if test="${draftMode==true}">



	<acme:input-textbox code="technician.maintenanceRecord.form.label.maintenanceDate" path="maintenanceDate"/>
	<acme:input-textbox code="technician.maintenanceRecord.form.label.nextInspectionDue" path="nextInspectionDue"/>
	<acme:input-money code="technician.maintenanceRecord.form.label.estimatedCost" path="estimatedCost"/>
	<acme:input-textbox code="technician.maintenanceRecord.form.label.notes" path="notes"/>
	<acme:input-select code="technician.maintenanceRecord.form.label.status" path="status" choices="${status}"/>
	<acme:input-select code="technician.maintenanceRecord.form.label.aircraft" path="aircraft" choices="${aircrafts}"/>
	<acme:input-checkbox code="technician.maintenanceRecord.form.label.draftMode" path="draftMode" readonly="true"/>
	

	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">
			<acme:submit code="technician.maintenanceRecord.form.button.update" action="/technician/maintenance-record/update"/>
			<acme:submit code="technician.maintenanceRecord.form.button.delete" action="/technician/maintenance-record/delete"/>
			<acme:submit code="technician.maintenanceRecord.form.button.publish" action="/technician/maintenance-record/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="technician.maintenanceRecord.form.button.create" action="/technician/maintenance-record/create"/>
		</jstl:when>
	</jstl:choose>
	
</jstl:if>

<jstl:if test="${draftMode==false}">
	<acme:input-textbox code="technician.maintenanceRecord.form.label.maintenanceDate" path="maintenanceDate" readonly="true"/>
	<acme:input-textbox code="technician.maintenanceRecord.form.label.nextInspectionDue" path="nextInspectionDue" readonly="true"/>
	<acme:input-money code="technician.maintenanceRecord.form.label.estimatedCost" path="estimatedCost" readonly="true"/>
	<acme:input-textbox code="technician.maintenanceRecord.form.label.notes" path="notes" readonly="true"/>
	<acme:input-select code="technician.maintenanceRecord.form.label.status" path="status" choices="${status}" readonly="true"/>
	<acme:input-select code="technician.maintenanceRecord.form.label.aircraft" path="aircraft" choices="${aircrafts}" readonly="true"/>
	<acme:input-textbox code="technician.maintenanceRecord.form.label.draftMode" path="draftMode" readonly="true"/>
</jstl:if>



</acme:form>


