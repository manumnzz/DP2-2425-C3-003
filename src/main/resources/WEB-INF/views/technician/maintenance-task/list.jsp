

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column path="maintenanceRecord" code="technician.maintenanceTask.list.label.maintenanceRecord" width="10%"/>
	<acme:list-column path="task" code="technician.maintenanceTask.list.label.task" width="10%"/>

</acme:list>

<acme:button code="technician.maintenanceTask.form.button.create" action="/technician/maintenance-task/create"/>
