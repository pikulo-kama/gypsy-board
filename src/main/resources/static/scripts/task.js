let organizationMembers = [];
let taskHashField = $('#task-hash-field');
let taskNameField = $('#task-name-field');
let taskDescriptionField = $('#task-description-field');
let assigneeInputField = $('#assignee-input-area');
let assigneeField = $('#assignee-select-box');


var quill = new Quill('#task-editor', {
    placeholder: 'Describe your task...',
    theme: 'snow'
});

$(document).ready(function () {
    let request = openRestHttpGetRequest('/users/activeOrganizationMembers');
    request.onreadystatechange = function () {
        if (request.responseText !== '') {
            organizationMembers = JSON.parse(request.responseText);
            visualizeMembers(organizationMembers);
        }
    }
    request.send();
});

assigneeInputField.on('change keyup paste', function () {
    let filteredMembers = organizationMembers.filter(member => {
        return member['username'].includes($(this).val()) ||
            member['fullName'].includes($(this).val());
    });
    visualizeMembers(filteredMembers);
});

$(document).on('change keyup paste', '.add-task-name-field', function () {
    let content = $(this).val();
    let button =  $(this).closest('.add-task-area').find('.add-task-btn');

    if (content.length === 0 || !content.trim()) {
        button.addClass('anchor-disabled');
    } else {
        button.removeClass('anchor-disabled');
    }
})

$('#update-task-btn').on('click', function () {

    let request = openRestHttpPostRequest('/tasks/update');
    request.send(JSON.stringify({
        "taskHash": taskHashField.val(),
        "taskName": taskNameField.val(),
        "taskDescription": JSON.stringify(quill.getContents()),
        "assigneeUserName": assigneeField.find(':selected').val()
    }));

    $(`#${taskHashField.val()}`).text(taskNameField.val());
    cleanupFields();
    $('#close-task-window-btn').trigger('click');
});

$('#delete-task-btn').on('click', function () {
   let taskHash = taskHashField.val();
    ConfirmModal.openConfirmWindow(taskHash, "handleRemoveTaskBtnClick");
});

$(document).on('click', '.add-task-btn', function () {
    let boardColumn = $(this).closest('.board-column');
    let boardColumnList = boardColumn.find('ul.list-unstyled').first();
    let taskField = $(this).closest('.add-task-area').find('.add-task-name-field').first();

    let request = openRestHttpPostRequest('/tasks/create');
    request.onload = function () {
        let response = JSON.parse(request.responseText);
        let taskName = String(response['taskName']);
        let taskHash = response['taskHash'];
        // if (taskName.length > 22) {
        //     taskName = taskName.substring(0, 22) + '...';
        // }
        boardColumnList.append(getTaskHtml(taskHash, trimTaskName(taskName)));
        taskField.val('');
    }
    request.send(JSON.stringify({
        "columnHash": boardColumn.attr('id'),
        "taskName": taskField.val()
    }));
});

$(document).on('click', '.draggable-task-modal-link', function() {
    let taskHash = $(this).attr(`id`);
    let request = openRestHttpGetRequest(`/tasks?t=${taskHash}`);
    request.onload = function () {
        if (request.status !== 200) {
            return;
        }
        let response = JSON.parse(request.responseText);
        cleanupFields();

        taskHashField.val(taskHash);
        taskNameField.val(response['taskName']);
        quill.setContents(JSON.parse(response['taskDescription']));
        visualizeMembers(organizationMembers);

        let options = assigneeField.children();
        for (let option of options) {
            if ($(option).val() === response['assignee']['username']) {
                $(option).attr('selected', 'selected');
            }
        }
    }
    request.send();
});

function cleanupFields() {
    taskHashField.val("");
    taskNameField.val("");
    taskDescriptionField.val("");
    assigneeInputField.val("");
}

function visualizeMembers(members) {
    let assigneeSelectBox = $('#assignee-select-box');
    assigneeSelectBox.empty();
    for (let member of members) {
        assigneeSelectBox.append(`<option value="${member['username']}">${member['fullName']}</option>`);
    }
}

function handleRemoveTaskBtnClick(taskHash) {
    let request = openRestHttpPostRequest(`/tasks/delete?t=${taskHash}`);
    request.onload = function () {
        if (request.status !== 200) {
            return;
        }
        $(`#${taskHash}`).closest('.draggable-task').remove();
        ConfirmModal.closeConfirmWindow();
    }
    request.send();
}

function trimTaskName(taskName) {
    let maxLength = 22;

    if (taskName.split(' ').length === 1 && taskName.length > maxLength) {
        return taskName.substring(0, 22) + '...';
    }

    let trimmedString = taskName.substr(0, maxLength);
    return trimmedString.substr(0, Math.min(trimmedString.length, trimmedString.lastIndexOf(" "))) + ' ...';
}
