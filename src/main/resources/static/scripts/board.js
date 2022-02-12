let columnNamesMap = {};
let boardNameHolder = '';


$('#remove-board-btn').on('click', function () {
   let boardHash = $('.board-name-field').attr('id');
   ConfirmModal.openConfirmWindow(boardHash, 'handleBoardRemoval');
});

function handleBoardRemoval(boardHash) {
    let request = openRestHttpPostRequest(`/boards/delete?b=${boardHash}`);
    request.onload = function () {
        ConfirmModal.closeConfirmWindow();
        window.location.href = '/organizations/active';
    }
    request.send();
}

$('#create-board-btn').on('click', function () {
    let boardName = $('#board-name-field').val();
    let request = openRestHttpPostRequest("/boards/create");
    request.onload = function () {
        let response = new UserMessage(request.responseText);
        if (response.isSuccess()) {
            let boardName = response.responseData['persistedBoard']['boardName'];
            let boardHash = response.responseData['persistedBoard']['boardHash'];
            let boardListContainer = $('#board-data-list')
            if (boardListContainer !== undefined) {
                $('#no-boards-list-item').remove();
            }
            boardListContainer.append(`<li><a href="/boards?b=${boardHash}">${boardName}</a></li>`);
            $('#close-board-window-btn').trigger('click');
        } else {
            console.log(response['message'])
        }
    }
    request.send(JSON.stringify({
        'boardName': boardName
    }));
});

$(document).on('click', '.remove-board-column-btn', function () {
    let boardHash = $(this).closest('.board-column').attr('id');
    ConfirmModal.openConfirmWindow(boardHash, 'handleBoardColumnDeletion');
});

function handleBoardColumnDeletion(boardColumnHash) {
    let request = openRestHttpPostRequest(`/columns/delete?c=${boardColumnHash}`);
    request.onload = function () {
        let response = new UserMessage(request.responseText);
        if (response.isSuccess()) {
            $(`#${boardColumnHash}`).remove();
            if (BoardColumn.getColumnsSize() === 0) BoardColumn.addDemoColumn();
        }
        ConfirmModal.closeConfirmWindow();
    }
    request.send();
}

$('#create-board-column-btn').on('click', function () {
    let boardColumnField = $('#board-column-name-field');
    let boardHash = $('.board-name-field').attr('id');
    let request = openRestHttpPostRequest('/columns/create');
    request.onload = function () {
        let response = new UserMessage(request.responseText);
        if (response.isSuccess()) {
            let boardColumn = response.responseData['boardColumn'];
            BoardColumn.addColumn(boardColumn['columnName'], boardColumn['columnHash']);
            // $('#board-columns-placeholder').append(getBoardColumnHtml(boardColumn['columnName'], boardColumn['columnHash']));
            let newBoardColumn = $(`#${boardColumn['columnHash']}`);
            newBoardColumn.sortable(getSortableProperties());
            newBoardColumn.droppable(getDroppableProperties());

            BoardColumn.removeDemoColumn();
        }
        $('#close-board-column-window-btn').trigger('click');
        boardColumnField.val('');
    }
    request.send(JSON.stringify({
        'boardHash': boardHash,
        'columnName': boardColumnField.val()
    }));
});

$(document).on('mouseenter mouseleave', '.draggable-task', function (e) {
    if (e.type === 'mouseenter') {
        $(this).find('span').first().css('border-left', '2px solid var(--sub-color)');
    } else if (e.type === 'mouseleave') {
        $(this).find('span').first().css('border-left', '2px solid var(--forth-color)');
    }
});

$('.column-name-field').on('focus', function () {
    let columnHash = $(this).closest('.board-column').attr('id');
    columnNamesMap[columnHash] = $(this).text();
});

$('.column-name-field').on('blur', function () {
    let columnHash = $(this).closest('.board-column').attr('id');
    let columnName = $(this).text();
    let previousColumnName = columnNamesMap[columnHash];

    if (columnName === '') {
        $(this).text(previousColumnName);
        return;
    }

    if (columnName !== previousColumnName) {
        let request = openRestHttpPostRequest('/columns/update');
        request.onload = function() {
            if (request.status === 200) {
                columnNamesMap[columnHash] = columnName;
                $(this).text(columnName);
            }
        }
        request.send(JSON.stringify({
            'columnName': columnName,
            'columnHash': columnHash
        }));
    }
});

$('.board-name-field').on('focus', function () {
    boardNameHolder = $(this).text();
});

$('.board-name-field').on('blur', function () {
    let boardHash = $(this).attr('id');
    let boardName = $(this).text();
    let previousBoardName = boardNameHolder;

    if (boardName === '') {
        $(this).text(previousBoardName);
        return;
    }

    if (boardName !== previousBoardName) {
        let request = openRestHttpPostRequest('/boards/update');
        request.onload = function() {
            if (request.status === 200) {
                boardNameHolder = boardName;
                $(this).text(boardName);
            }
        }
        request.send(JSON.stringify({
            'boardHash': boardHash,
            'boardName': boardName
        }));
    }
});

$('.board-column').sortable(getSortableProperties());

$('.board-column').droppable(getDroppableProperties());

function getTaskHtml(taskId, taskName) {
    return `<li class="draggable-task">
                <span class="d-inline-block" style="border-left: 2px solid var(--forth-color); height: 10px"></span>
                <a role="button" class="draggable-task-modal-link fg-forth hover-fg-sub" href="#" data-mdb-toggle="modal" id="${taskId}" data-mdb-target="#show-task-modal">${taskName}</a>
            </li>`;
}

function syncBoardColumn(sourceColumn, targetColumn) {
    let sourceColumnContainer = $(sourceColumn);
    let sourceColumnHash = sourceColumnContainer.attr('id');
    let targetColumnHash = $(targetColumn).attr('id');
    let taskHashList = [];

    let taskList = sourceColumnContainer.find('li a');
    for (let task of taskList) {
        taskHashList.push($(task).attr('id'));
    }

    let request = openRestHttpPostRequest("/tasks/sync");
    request.send(JSON.stringify({
        "sourceColumnHash": sourceColumnHash,
        "targetColumnHash": targetColumnHash,
        "taskHashList": taskHashList
    }))
}

function getSortableProperties() {
    return {
        items: "li.draggable-task",
            connectWith: "li",
        sort: function () {
        $(this).removeClass("ui-state-default");
    },
        stop: function () {
            let length = $(this).children('li').length;
            syncBoardColumn(this, this);
        }
    };
}

function getDroppableProperties() {
    return {
        tolerance: 'intersect',
        drop: function (event, ui) {
            let targetContainer = $(this);
            let sourceContainer = $(ui.draggable).closest('.board-column');
            if (targetContainer.attr('id') === sourceContainer.attr('id')) {
                event.stopPropagation();
                return;
            }

            let foreignTask = $(ui.draggable);

            let taskId = foreignTask.find('a').first().attr('id');
            let taskName = foreignTask.find('a').first().html();

            targetContainer.find('.list-unstyled').first().append(getTaskHtml(taskId, taskName));
            foreignTask.remove();

            syncBoardColumn(sourceContainer, targetContainer);
        }
    };
}

function getBoardColumnHtml(columnName, columnHash) {
    return `<div class="d-flex flex-column board-column justify-content-evenly position-relative" id="${columnHash}">
                    <div class="position-absolute top-0 end-0 me-2 mt-2">
                        <a role="button" class="remove-board-column-btn fg-forth hover-fg-sub">
                            <i class="fas fa-recycle"></i>
                        </a>
                    </div>
                    <div class="column-name-field fg-sub text-center text-uppercase font-weight-bold"
                         contenteditable="true">${columnName}</div>
                    <div class="board-tasks-container scrollbar scrollbar-y scrollbar-rare-wind force-overflow w-100 ms-1">
                        <ul class="list-unstyled">
                                <li class="draggable-task"></li>
                        </ul>
                    </div>
                    <div class="add-task-area d-flex flex-row-reverse justify-content-evenly">
                        <div class="d-flex align-self-center">
                            <a role="button" class="anchor-disabled add-task-btn fg-forth hover-fg-sub"><i
                                        class="fas fa-plus-square fa-lg"></i></a>
                        </div>
                        <div class="form-outline mt-1 mb-2 w-75">
                            <input type="text" id="formControlSm"
                                   class="add-task-name-field form-control form-control-sm"
                                   style="border: 1px solid var(--sub-color)"/>
                            <label class="form-label" for="formControlSm">Task</label>
                        </div>
                    </div>
                </div>`;
}

function demoBoardColumnHtml() {
    return `
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
    `;
}