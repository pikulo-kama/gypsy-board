<#include "includes/header.ftl">

<#include "includes/modal/view_task.ftl">
<#include "includes/modal/create_board_column.ftl">

<div class="d-flex flex-column mt-5 mb-2 ps-3 align-items-center">
    <div class="d-flex flex-row align-self-start pb-1 mb-2 align-items-center">
        <a role="button" id="remove-board-btn" class="fg-forth hover-fg-sub bg-third me-1 p-2 rounded">
            <i class="fas fa-recycle fa-2x"></i>
        </a>
        <p class="board-name-field fs-3 fg-third bg-forth p-1 m-0" id="${board.boardHash}" contenteditable="true">${board.boardName}</p>
    </div>
    <div id="board-columns-placeholder" class="d-flex flex-row h-100 align-self-start scrollbar scrollbar-x scrollbar-rare-wind"
         style="width: 85%">
        <#if board.boardColumns?size != 0>
            <#list board.boardColumns as boardColumn>
                <div class="d-flex flex-column board-column justify-content-evenly position-relative" id="${boardColumn.columnHash}">
                    <div class="position-absolute top-0 end-0 me-2 mt-2">
                        <a role="button" class="remove-board-column-btn fg-forth hover-fg-sub">
                            <i class="fas fa-recycle"></i>
                        </a>
                    </div>
                    <div class="column-name-field fg-sub text-center text-uppercase font-weight-bold"
                         contenteditable="true">${boardColumn.columnName}</div>
                    <div class="board-tasks-container scrollbar scrollbar-y scrollbar-rare-wind force-overflow w-100 ms-1">
                        <ul class="list-unstyled">
                            <#list boardColumn.tasks as task>
                                <li class="draggable-task">
                                    <span class="d-inline-block" style="border-left: 2px solid var(--forth-color); height: 10px"></span>
                                    <a role="button" class="draggable-task-modal-link fg-forth hover-fg-sub" href="#"
                                       data-mdb-toggle="modal" id="${task.taskHash}"
                                       data-mdb-target="#show-task-modal">${task.taskName?truncate(25, '...')}</a>
                                </li>
                            </#list>
                        </ul>
                    </div>
                    <div class="add-task-area d-flex flex-row-reverse justify-content-evenly">
                        <div class="d-flex align-self-center">
                            <a role="button" class="anchor-disabled add-task-btn fg-forth hover-fg-sub"><i
                                        class="fas fa-plus-square fa-lg"></i>
                            </a>
                        </div>
                        <div class="form-outline mt-1 mb-2 w-75">
                            <input type="text" id="formControlSm"
                                   class="add-task-name-field form-control form-control-sm"
                                   style="border: 1px solid var(--sub-color)"/>
                            <label class="form-label" for="formControlSm">Task</label>
                        </div>
                    </div>
                </div>
            </#list>
        <#else >
            <div class="d-flex flex-column justify-content-evenly board-column opacity-50 user-select-none" id="demo-board-column">
                <div class="fg-sub text-center text-uppercase font-weight-bold">.......</div>
                <div class="scrollbar scrollbar-y scrollbar-rare-wind force-overflow w-100 ms-1">
                    <ul class="list-unstyled">
                        <li>
                            <span class="d-inline-block" style="border-left: 2px solid var(--forth-color); height: 10px"></span>
                            <a role="button" class="fg-forth hover-fg-sub" href="#">...</a>
                        </li>
                        <li>
                            <span class="d-inline-block" style="border-left: 2px solid var(--forth-color); height: 10px"></span>
                            <a role="button" class="fg-forth hover-fg-sub" href="#">...</a>
                        </li>
                        <li>
                            <span class="d-inline-block" style="border-left: 2px solid var(--forth-color); height: 10px"></span>
                            <a role="button" class="fg-forth hover-fg-sub" href="#">...</a>
                        </li>
                    </ul>
                </div>
                <div class="d-flex flex-row-reverse justify-content-evenly">
                    <div class="d-flex align-self-center">
                        <a role="button" class="anchor-disabled add-task-btn fg-forth hover-fg-sub"><i
                                    class="fas fa-plus-square fa-lg"></i></a>
                    </div>
                    <div class="form-outline mt-1 mb-2 w-75">
                        <input disabled type="text" id="formControlSm" class="add-task-name-field form-control form-control-sm"
                               style="border: 1px solid var(--sub-color)"/>
                        <label class="form-label" for="formControlSm">Task</label>
                    </div>
                </div>
            </div>
        </#if>
    </div>
    <div class="d-flex flex-row align-self-start">
        <a role="button" id="add-column-btn" class="fg-main hover-fg-forth bg-sub me-1 p-2 rounded text-uppercase"
           href="#" data-mdb-toggle="modal" data-mdb-target="#create-board-column-modal">
            <i class="fas fa-align-justify fa-lg"></i> Add column
        </a>
    </div>
</div>

<link rel="stylesheet" href="/css/board.css">
<link rel="stylesheet" href="/css/task.css">
<link rel="stylesheet" href="/css/form.css">
<link rel="stylesheet" href="/css/dropdown.css">
<script type="text/javascript" src="/scripts/task.js"></script>
<script type="text/javascript" src="/scripts/board.js"></script>

<#include "includes/footer.ftl" >
