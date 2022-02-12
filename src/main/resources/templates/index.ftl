<#include "./includes/header.ftl" >

<#include "./includes/modal/create_organization.ftl" >

<link rel="stylesheet" href="/css/dropdown.css">

<div class="d-flex flex-column mt-5 ps-5 pe-5">
    <div class="available-data scrollbar scrollbar-y scrollbar-rare-wind">
        <ul id="organizations-placeholder" class="data-list">
            <#if availableOrganizations?size != 0>
            <#list availableOrganizations as organization>
                <li><a href="/organizations?o=${organization.organizationHash}">${organization.organizationName}</a></li>
            </#list>
            <#else>
                <li id="no-organizations-list-item">No organization available</li>
            </#if>
        </ul>
    </div>
    <button type="button" class="btn btn-outline-dark btn-rounded" data-mdb-ripple-color="dark" data-mdb-toggle="modal" data-mdb-target="#create-organization-modal">Create organization</button>
</div>

<link rel="stylesheet" href="/css/dropdown.css">
<link rel="stylesheet" href="/css/form.css">
<script type="text/javascript" src="/scripts/organization.js"></script>

<#include "./includes/footer.ftl" >
