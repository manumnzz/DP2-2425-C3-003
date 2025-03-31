<%--
- list.jsp
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column path="type" code="technician.task.list.label.type" width="10%"/>
	<acme:list-column path="priority" code="technician.task.list.label.priority" width="10%"/>
	<acme:list-column path="estimatedDuration" code="technician.task.list.label.estimatedDuration" width="10%"/>
	<acme:list-column path="maintenanceRecord" code="technician.task.list.label.maintenanceRecord" width="10%"/>
	<acme:list-column path="technician" code="technician.task.list.label.technician" width="10%"/>
	<acme:list-column path="aircraft" code="technician.task.list.label.aircraft" width="10%"/>
	<acme:list-column path="draftMode" code="technician.task.list.label.draftMode" width="10%"/>
	
</acme:list>

<acme:button code="technician.task.form.button.create" action="/technician/task/create"/>
