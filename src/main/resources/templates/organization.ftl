<#include "includes/header.ftl" >

<#include "includes/modal/create_board.ftl">
<#include "includes/modal/add_member.ftl">
<#include "includes/modal/create_shared_board.ftl">

<div class="position-absolute w-100 h-100 d-flex justify-content-start align-content-start ms-4">
    <div id="${organization.organizationHash}"
         class="organization-container d-flex flex-column h-75 w-100 mt-5 mb-5 align-items-start justify-content-start">
        <div class="d-flex flex-column align-items-start w-100">
            <p class="display-1 gradient-text text-center">${organization.organizationName}</p>

            <div class="d-flex flex-row mt-1">
                <a href="/documents?o=${organization.organizationHash}"
                   role="button"
                   class="btn btn-outline-primary ms-1"
                >
                    <i class="fas fa-file-alt fa-lg"></i> Organization Wiki
                </a>
                <a href="/absences?o=${organization.organizationHash}"
                   role="button"
                   class="btn btn-outline-primary ms-1"
                >
                    <i class="fas fa-coffee fa-lg"></i> Absence History
                </a>
                <a href="/absences/chart?o=${organization.organizationHash}"
                   role="button"
                   class="btn btn-outline-primary ms-1"
                >
                    <i class="fas fa-chart-area fa-lg"></i> Capacity Chart
                </a>
                <a href="/absences/manage?o=${organization.organizationHash}"
                   role="button"
                   class="manage-absences-btn btn btn-outline-info btn-rounded ms-5"
                >
                    <i class="fas fa-thumbs-up"></i> Manage Absences
                </a>
            </div>
            <div class="d-flex flex-row scrollbar scrollbar-x scrollbar-rare-wind pb-5" style="max-height: 40vh">
                <div class="d-flex flex-column w-50 h-100">
                    <div class="available-data scrollbar scrollbar-y scrollbar-rare-wind">
                        <ul id="board-data-list" class="data-list">
                            <#if organization.organizationBoards?size != 0>
                                <#list organization.organizationBoards as board>
                                    <#if board.isShared >
                                        <li><a href="/boards?b=${board.boardHash}" class="shared-organization" style="color: #fff;">${board.boardName} <small class="text-muted">shared by ${board.ownerOrganization.organizationName}</small></a></li>
                                    <#else >
                                        <li><a href="/boards?b=${board.boardHash}">${board.boardName}</a></li>
                                    </#if>
                                </#list>
                            <#else >
                                <li id="no-boards-list-item">No boards available</li>
                            </#if>
                        </ul>
                    </div>
                </div>

                <#--        Organization members        -->
                <div class="d-flex flex-column h-100" style="width: 40%">
                    <div class="available-data scrollbar scrollbar-y scrollbar-rare-wind">
                        <table id="organization-members-table" class="table text-center">
                            <#list organization.organizationMembers as member>
                                <tr>
                                    <td class="p-0 ps-1">
                                        <a role="button" class="fg-sub hover-fg-main">
                                            <#if member.isInvitationAccepted>
                                                <i class="fas fa-check-circle"></i>
                                            <#else >
                                                <i class="fas fa-times-circle"></i>
                                            </#if>
                                        </a>
                                    </td>
                                    <td class="member-name p-0" id="${member.userHash}">${member.fullName}</td>
                                    <td class="p-0">
                                        <select class="organization-role-select">
                                            <#list organizationRoles as role>
                                                <#if role.roleCode == member.activeRole>
                                                    <option value="${role.roleCode}" selected>${role.roleName}</option>
                                                <#else >
                                                    <option value="${role.roleCode}">${role.roleName}</option>
                                                </#if>
                                            </#list>
                                        </select>
                                    </td>
                                    <td class="p-0 pe-1">
                                        <a role="button" class="remove-organization-member-btn fg-sub hover-fg-main">
                                            <i class="fas fa-recycle"></i>
                                        </a>
                                    </td>
                                </tr>
                            </#list>
                        </table>
                    </div>
                </div>

            </div>
        </div>


        <div class="d-flex flex-column">
            <div class="d-flex flex-row gap-2">
                <button id="create-board-btn"
                        type="button"
                        class="btn btn-outline-dark btn-rounded"
                        data-mdb-ripple-color="dark"
                        data-mdb-toggle="modal"
                        data-mdb-target="#create-board-modal">
                    <i class="fas fa-clipboard-list fa-lg"></i> Create Board
                </button>
                <button id="create-shared-board-btn"
                        type="button"
                        class="btn btn-outline-dark btn-rounded"
                        data-mdb-ripple-color="dark"
                        data-mdb-toggle="modal"
                        data-mdb-target="#create-shared-board-modal">
                    <i class="fas fa-clipboard-list fa-lg"></i> Create Shared Board
                </button>
                <button id="add-members-btn"
                        type="button"
                        class="btn btn-outline-dark btn-rounded ms-1"
                        data-mdb-ripple-color="dark"
                        data-mdb-toggle="modal"
                        data-mdb-target="#add-members-modal">
                    <i class="fas fa-user fa-lg"></i> Add Members
                </button>
                <button id="remove-organization-btn"
                        type="button"
                        class="btn btn-outline-danger btn-rounded ms-3">
                    <i class="fas fa-recycle fa-lg"></i>
                </button>
            </div>
        </div>
    </div>
</div>

<link rel="stylesheet" href="/css/dropdown.css">
<link rel="stylesheet" href="/css/form.css">
<link rel="stylesheet" href="/css/userimage.css">
<link rel="stylesheet" href="/css/organization.css">
<script type="text/javascript" src="/scripts/board.js"></script>
<script type="text/javascript" src="/scripts/organization.js"></script>

<#include "includes/footer.ftl" >
