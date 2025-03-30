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
	<acme:list-column path="flightNumber" code="airlinemanager.leg.list.label.flightNumber" width="10%"/>
	<acme:list-column path="scheduledDeparture" code="airlinemanager.leg.list.label.scheduledDeparture" width="20%"/>
	<acme:list-column path="scheduledArrival" code="airlinemanager.leg.list.label.scheduledArrival" width="20%"/>
	<acme:list-column path="departureAirport.name" code="airlinemanager.leg.list.label.departureAirport.name" width="20%"/>
	<acme:list-column path="arrivalAirport.name" code="airlinemanager.leg.list.label.arrivalAirport.name" width="20%"/>
	<acme:list-column path="draftMode" code="airlinemanager.leg.list.label.draftMode" width="10%"/>
</acme:list>

<acme:button code="airlinemanager.leg.form.button.create" action="/airline-manager/leg/create"/>