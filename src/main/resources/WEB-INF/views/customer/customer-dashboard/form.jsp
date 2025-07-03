<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" uri="http://acme-framework.org/" %>


<div class="container" style="margin-top: 2rem;">
    <div class="text-center mb-4">
        <h2>
            <fmt:message key="client.dashboard.form.title.general-indicators"/>
        </h2>
    </div>

    <table class="table table-sm table-bordered">
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.last-five-destinations-last-year"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${lastFiveDestinations != null}">
                        <acme:print value="${lastFiveDestinations}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.spent-money-last-year"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${moneyInBookingsLastYear != null}">
                        <acme:print value="${moneyInBookingsLastYear}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        
        <!-- Indicadores de reservas -->
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.economy-bookings"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${totalEconomyBookings != null}">
                        <acme:print value="${totalEconomyBookings}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.business-bookings"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${totalBusinessBookings != null}">
                        <acme:print value="${totalBusinessBookings}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.booking-count-cost"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${countCostBookingLastYear != null}">
                        <acme:print value="${countCostBookingLastYear}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.booking-average-cost"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${averageCostBookingLastYear != null}">
                        <acme:print value="${averageCostBookingLastYear}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.booking-minimum-cost"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${minimumCostBookingLastYear != null}">
                        <acme:print value="${minimumCostBookingLastYear}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.booking-maximum-cost"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${maximumCostBookingLastYear != null}">
                        <acme:print value="${maximumCostBookingLastYear}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.booking-deviation-cost"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${standardDeviaCostBookingLastYear != null}">
                        <acme:print value="${standardDeviaCostBookingLastYear}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.booking-count-passengers"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${countPassengers != null}">
                        <acme:print value="${countPassengers}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.booking-average-passengers"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${averagePassengers != null}">
                        <acme:print value="${averagePassengers}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.booking-minimum-passengers"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${minimumPassengers != null}">
                        <acme:print value="${minimumPassengers}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.booking-maximum-passengers"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${maximumPassengers != null}">
                        <acme:print value="${maximumPassengers}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.booking-deviation-passengers"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${standardDeviationPassengers != null}">
                        <acme:print value="${standardDeviationPassengers}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
    </table>
    
    <jstl:choose>
        <jstl:when test="${totalEconomyBookings != 0 || totalBusinessBookings != 0}">
            <h3>Bookings Distribution</h3>
<div class="dashboard-chart" style="height: 500px;">
    <canvas id="chartBookings"></canvas>
</div>
<script type="text/javascript">
    document.addEventListener("DOMContentLoaded", function() {
        var data = {
            labels: ["Economy", "Business"], // Cambiadas las etiquetas
            datasets: [{
                data: [
                    <jstl:out value="${totalEconomyBookings}"/>,
                    <jstl:out value="${totalBusinessBookings}"/>
                ],
                backgroundColor: [
                    'rgba(174, 214, 241, 0.7)',
                    'rgba(245, 203, 167, 0.7)'
                ],
                borderColor: [
                    'rgba(174, 214, 241, 1)',
                    'rgba(245, 203, 167, 1)'
                ],
                borderWidth: 1
            }]
        };
        var ctx = document.getElementById("chartBookings").getContext("2d");
        new Chart(ctx, {
            type: "doughnut",
            data: data,
            options: {
                responsive: true,
                maintainAspectRatio: false, 
                title: {
                    display: true,
                    text: "Bookings Distribution"
                }
            }
        });
    });
</script>
        </jstl:when>
        <jstl:otherwise>
            <h3>
                <fmt:message key="client.dashboard.form.label.bookings.distribution"/>
            </h3>
            <acme:print value="N/A"/>
        </jstl:otherwise>
    </jstl:choose>
        
    
</div>