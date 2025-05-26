<%--
- list.jsp
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column path="maintenanceDate" code="technician.maintenanceRecord.list.label.maintenanceDate" width="10%"/>
	<acme:list-column path="status" code="technician.maintenanceRecord.list.label.status" width="10%"/>
	<acme:list-column path="nextInspectionDue" code="technician.maintenanceRecord.list.label.nextInspectionDue" width="10%"/>
	<acme:list-column path="estimatedCost" code="technician.maintenanceRecord.list.label.estimatedCost" width="10%"/>
	<acme:list-column path="technician" code="technician.maintenanceRecord.list.label.technician" width="10%"/>
	<acme:list-column path="aircraft" code="technician.maintenanceRecord.list.label.aircraft" width="10%"/>
	<acme:list-column path="draftMode" code="technician.maintenanceRecord.list.label.draftMode" width="10%"/>
	
</acme:list>

<acme:button code="technician.maintenanceRecord.form.button.create" action="/technician/maintenance-record/create"/>
