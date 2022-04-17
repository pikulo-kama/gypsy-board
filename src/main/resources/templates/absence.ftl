<#include "includes/header.ftl">

<#include "includes/modal/request_absence.ftl">


<div class="position-absolute w-100 h-100 mt-5 mb-5 d-flex flex-column justify-content-start align-content-start">
    <button id="request-absence-btn"
            type="button"
            class="w-25 btn btn-outline-secondary btn-rounded ms-4"
            data-mdb-ripple-color="dark"
            data-mdb-toggle="modal"
            data-mdb-target="#request-absence-modal">
        <i class="fas fa-coffee fa-lg"></i> Request
    </button>
    <table class="table table-sm text-center">
        <thead>
        <tr>
            <th scope="col">Approved</th>
            <th scope="col">Start Date</th>
            <th scope="col">Last Date</th>
            <th scope="col">Total Days</th>
            <th scope="col">Type</th>
            <th scope="col">&nbsp;</th>
        </tr>
        </thead>
        <tbody id="absence-record-table-body">
        <#list absenceRecords as record>
            <tr id="${record.absenceRecordHash}" class="absence-record <#if record.isCancelled>bg-warning</#if>">
                <td>
                    <#if record.isApproved>
                        <i class="fas fa-check-circle text-success fa-lg"></i>
                    <#else >
                        <i class="fas fa-times-circle text-danger fa-lg"></i>
                    </#if>
                </td>
                <td>${record.startDate?date?string.iso}</td>
                <td>${record.endDate?date?string.iso}</td>
                <td>${record.absenceDays}</td>
                <td class="fw-bold">${record.absenceType.icon} ${record.absenceType.representation}</td>
                <td>
                    <button type="button"
                            class="cancel-request-btn btn btn-sm btn-outline-danger <#if record.isCancelled>disabled</#if>">
                        <i class="fas fa-ban fa-xs"></i>
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
