<#include "includes/header.ftl" >

<#include "includes/modal/create_board.ftl">

<div class="position-absolute w-100 h-100 d-flex justify-content-center align-content-center">
    <div id="${organization.organizationHash}" class="organization-container d-flex flex-column h-75 w-75 mt-5 mb-5 align-items-center justify-content-center">
        <div class="d-flex flex-column" style="width: 70vw">
            <p class="display-1 gradient-text text-center">${organization.organizationName}</p>

            <div class="d-flex flex-row scrollbar scrollbar-x scrollbar-rare-wind pb-5">
                <#list organization.organizationMembers as member>
                    <div class="position-relative user-image-container d-flex flex-column align-items-center">
                        <img class="user-image" src="/images/defaultAvatars/${member.imageName}">
                        <div class="user-full-name position-absolute text-center">${member.fullName}</div>
                    </div>
                </#list>
            </div>
        </div>

        <div class="d-flex flex-column w-100 h-50">
            <div class="available-data scrollbar scrollbar-y scrollbar-rare-wind">
                <ul id="board-data-list" class="data-list">
                    <#if organization.organizationBoards?size != 0>
                    <#list organization.organizationBoards as board>
                        <li><a href="/boards?b=${board.boardHash}">${board.boardName}</a></li>
                    </#list>
                    <#else >
                        <li id="no-boards-list-item">No boards available</li>
                    </#if>
                </ul>
            </div>
        </div>
        <div class="d-flex flex-row mt-3">
            <button id="create-board-btn" type="button" class="btn btn-outline-dark btn-rounded" data-mdb-ripple-color="dark" data-mdb-toggle="modal" data-mdb-target="#create-board-modal">Create Board</button>
            <button id="remove-organization-btn" type="button" class="btn btn-outline-danger btn-rounded ms-3"><i class="fas fa-recycle"></i></button>
        </div>
    </div>
</div>

<link rel="stylesheet" href="/css/dropdown.css">
<link rel="stylesheet" href="/css/form.css">
<link rel="stylesheet" href="/css/userimage.css">
<script type="text/javascript" src="/scripts/board.js"></script>
<script type="text/javascript" src="/scripts/organization.js"></script>

<#include "includes/footer.ftl" >
