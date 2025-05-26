
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<acme:form>	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|delete')}">
			<acme:input-select code="technician.maintenanceTask.form.label.maintenanceRecord.id" choices="${mrs}" path="maintenanceRecord" readonly="true"/>
			<acme:input-select code="technician.maintenanceTask.form.label.task.id" path="task" choices="${tasks}" readonly="true"/>
			<acme:submit code="technician.maintenanceTask.form.button.delete" action="/technician/maintenance-task/delete"/>
			
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
 			<acme:input-select code="technician.maintenanceTask.form.label.maintenanceRecord.id" path="maintenanceRecord" choices="${mrs}"/>
			<acme:input-select code="technician.maintenanceTask.form.label.task.id" path="task" choices="${tasks}"/>
			<acme:submit code="technician.maintenanceTask.form.button.create" action="/technician/maintenance-task/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>
