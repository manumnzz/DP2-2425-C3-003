<%--
- list.jsp
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

<acme:list>
	<acme:list-column code="airlinemanager.flight.list.label.tag" path="tag" width="5%"/>
	<acme:list-column code="airlinemanager.flight.list.label.originCity" path="originCity" width="25%"/>
	<acme:list-column code="airlinemanager.flight.list.label.destinationCity" path="destinationCity" width="25%"/>
	<acme:list-column code="airlinemanager.flight.list.label.scheduledDeparture" path="scheduledDeparture" width="15%"/>
	<acme:list-column code="airlinemanager.flight.list.label.scheduledArrival" path="scheduledArrival" width="15%"/>
	<acme:list-column code="airlinemanager.flight.list.label.cost" path="cost" width="15%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<acme:button code="airlinemanager.flight.form.button.create" action="/airline-manager/flight/create"/>
