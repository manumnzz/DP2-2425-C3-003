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
    
    
	<jstl:choose>
		<jstl:when test="${_command == 'show'}">
			<acme:input-textbox code="customer.bookingPassenger.label.booking" path="booking"/>
			<acme:input-textbox code="customer.bookingPassenger.label.passenger" path="passenger"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-select code="customer.bookingPassenger.label.booking.id" path="booking" choices="${bookings}"/>
			<acme:input-select code="customer.bookingPassenger.label.passenger.id" path="passenger" choices="${passengers}"/>
			<acme:submit code="customer.bookingPassenger.form.button.create" action="/customer/booking-passenger/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>
