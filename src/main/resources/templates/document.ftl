<#include "includes/header.ftl">

<#include "includes/modal/view_document.ftl">
<#include "includes/modal/create_document.ftl">

<div class="position-absolute w-100 h-100 mt-5 mb-5 d-flex flex-column justify-content-start align-content-start ms-4">
    <button id="add-members-btn"
            type="button"
            class="w-25 btn btn-outline-dark btn-rounded ms-1 mb-3"
            data-mdb-ripple-color="dark"
            data-mdb-toggle="modal"
            data-mdb-target="#create-document-modal">
        <i class="fas fa-file-alt fa-lg"></i> Create Document
    </button>
    <div id="document-placeholder" class="d-flex flex-wrap h-75 w-100 align-items-start justify-content-start">
        <#list documents as document>
            <div id="${document.documentHash}"
                 data-mdb-toggle="modal"
                 data-mdb-target="#show-document-modal"
                 class="document-container hover-bg-sub position-relative">
                <div class="w-100 h-25 text-sm-center font-weight-light font-italic bg-forth fg-sub">
                    ${document.documentHeader}
                </div>
                <div class="ms-1 mt-1">
                    <i class="far fa-file-alt fa-8x"></i>
                </div>
                <div class="document-author-container position-absolute bottom-0 w-100 text-center">
                    Created by ${document.authorName}
                </div>
            </div>
        </#list>
    </div>
</div>

<link rel="stylesheet" href="/css/document.css">
<link rel="stylesheet" href="/css/form.css">
<script type="text/javascript" src="/scripts/document.js"></script>


<#include "includes/footer.ftl" >