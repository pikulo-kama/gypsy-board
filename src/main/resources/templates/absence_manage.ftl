<#include "includes/header.ftl">

<div class="position-absolute w-100 h-100 mt-5 mb-5 d-flex flex-column justify-content-start align-content-start">
    <table class="table table-sm text-center">
        <thead>
        <tr>
            <th scope="col">Member Name</th>
            <th scope="col">Start Date</th>
            <th scope="col">Last Date</th>
            <th scope="col">Total Days</th>
            <th scope="col">Type</th>
            <th scope="col">&nbsp;</th>
        </tr>
        </thead>
        <tbody id="absence-record-table-body">
        <#list requests as request>
            <tr id="${request.absenceRecordHash}" class="absence-request">
                <td>${request.organizationMember.fullName}</td>
                <td>${request.startDate?date?iso_utc}</td>
                <td>${request.endDate?date?iso_utc}</td>
                <td>${request.absenceDays}</td>
                <td class="fw-bold">${request.absenceType.icon} ${request.absenceType.representation}</td>
                <td>
                    <button type="button"
                            class="approve-request-btn btn btn-sm btn-outline-danger">
                        <i class="fas fa-thumbs-up"></i>
                    </button>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>

<link rel="stylesheet" href="/css/form.css">
<link rel="stylesheet" href="/css/absence.css">
<script type="text/javascript" src="/scripts/absence.js"></script>

<#include "includes/footer.ftl">
