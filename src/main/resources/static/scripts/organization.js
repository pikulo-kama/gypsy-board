const REMOVE_REFERENCE = 'user-remove-reference';
const ADD_REFERENCE = 'user-add-reference';

let selectedUsers = [];
let selectedOrganizations = [];
let defaultUserPictures = [];
let userRoles = {};

$('#remove-organization-btn').on('click', function () {
    let organizationHash = $('.organization-container').attr('id');
    ConfirmModal.openConfirmWindow(organizationHash, 'handleOrganizationDeletion');
});

$(document).on('change', '.organization-role-select', function () {

    let selectObject = $(this);
    let roleCode = selectObject.find(":selected").attr('value');
    let userHash = selectObject.closest('tr').find(".member-name").attr('id');

    let request = openRestHttpPostRequest("/organizations/updateUserRole");
    request.onload = function () {
        let response = new UserMessage(request.responseText);
        selectObject.val(response.responseData['newRole']);
    }
    request.send(JSON.stringify({
        'roleCode': roleCode,
        'userHash': userHash
    }));
});

function handleOrganizationDeletion(organizationHash) {
    let request = openRestHttpPostRequest(`/organizations/delete?o=${organizationHash}`);
    request.onload = function () {
        let response = new UserMessage(request.responseText);
        if (response.isSuccess()) {
            window.location.href = '/';
        }
        ConfirmModal.closeConfirmWindow();
    }
    request.send();
}

$('#add-members-modal-btn').on('click', function () {

    let members = [];
    for (let user of $('#selected-user-list').children()) {
        members.push($(user).attr('username'));
    }

    let request = openRestHttpPostRequest("/organizations/addMembers");
    request.onload = function () {
        let response = new UserMessage(request.responseText);
        if (response.isSuccess()) {

            let members = response.responseData['organizationMembers'];
            let roles = response.responseData['organizationRoles'];

            members.forEach(function (member) {
                let options = '';

                roles.forEach(function (userRole) {
                    options += `<option value="${userRole.roleCode}" 
                                            ${member.activeRole === userRole.roleCode ? 'selected' : ''}>
                                            ${userRole.roleName}</option>`;
                });

                let isInvitationAcceptedHtml = member.isInvitationAccepted ?
                    '<i class="fas fa-check-circle"></i>' : '<i class="fas fa-times-circle"></i>';

                let tableRow = `<tr>
                                    <td class="p-0 ps-1">
                                        <a role="button" class="fg-sub hover-fg-main">
                                            ${isInvitationAcceptedHtml}
                                        </a>
                                    </td>
                                    <td class="member-name p-0" id="${member.userHash}">${member.fullName}</td>
                                    <td class="p-0">
                                        <select class="organization-role-select">
                                            ${options}
                                        </select>
                                    </td>
                                    <td class="p-0 pe-1">
                                        <a role="button" class="remove-organization-member-btn fg-sub hover-fg-main">
                                            <i class="fas fa-recycle"></i>
                                        </a>
                                    </td>
                                </tr>`;

                $('#organization-members-table').append(tableRow);
            });
        }
        $('#close-add-members-window-btn').trigger('click');
    }
    request.send(JSON.stringify({
        "organizationMembers": members
    }));
});

$(document).on('click', '.remove-organization-member-btn', function () {

    let memberContainer = $(this).closest('tr').find('td.member-name');
    let memberHash = memberContainer.attr('id');

    ConfirmModal.openConfirmWindow(memberHash, "handleMemberDeletion");
});

function handleMemberDeletion(memberHash) {
    let request = openRestHttpPostRequest("/organizations/removeMember");
    request.onload = function () {
        let response = new UserMessage(request.responseText);
        if (response.isSuccess()) {
            let removedMemberHash = response.responseData['removedMemberHash'];
            $(`#${removedMemberHash}`).closest('tr').remove();
        }
        ConfirmModal.closeConfirmWindow();
    };
    request.send(JSON.stringify({
        "memberHash": memberHash
    }));
}

$('#create-organization-btn').on('click', function () {
    let organizationName = $('#organization-name-field').val();
    let userList = [];
    for (let user of $('#selected-user-list').children()) {
        userList.push($(user).attr('username'));
    }

    let request = openRestHttpPostRequest('/organizations/create');
    request.onload = function () {
        let response = new UserMessage(request.responseText);
        if (response.isSuccess()) {
            let name = response.responseData['persistedOrganization']['organizationName'];
            let hash = response.responseData['persistedOrganization']['organizationHash'];
            let emptyOrganizationItem = $('#no-organizations-list-item');
            if (emptyOrganizationItem !== undefined) {
                emptyOrganizationItem.remove();
            }

            $('#organizations-placeholder').append(`<li><a href="/organizations?o=${hash}">${name}</a></li>`)
        }
        $('#close-organization-window-btn').trigger('click');
    }



    request.send(JSON.stringify({
        "organizationName": organizationName,
        "memberUsernames": userList
    }));
})

$('#create-shared-board-btn').on('click', function () {
    let boardName = $('#shared-board-name-field').val();
    let collaboratorList = [];
    for (let collaborator of $('#selected-organization-list').children()) {
        collaboratorList.push($(collaborator).attr('hash'));
    }

    let request = openRestHttpPostRequest('/boards/shared/create');
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
        }
        $('#close-shared-board-window-btn').trigger('click');
    }

    request.send(JSON.stringify({
        "boardName": boardName,
        "collaboratorList": collaboratorList
    }));
})

function onUserAddCallback(node) {
    let user = deserializeUserNode(node);
    $('#selected-user-list').append(getUserItemString(user['username'], user['fullName'], REMOVE_REFERENCE))
    selectedUsers.push(user['username'])
    $(node).remove();
}

function onUserRemoveCallback(node) {
    let user = deserializeUserNode(node);
    // remove user
    selectedUsers = selectedUsers.filter(innerUser => innerUser !== user['username']);
    $(node).remove();
}

function onOrganizationAddCallback(node) {
    let organization = deserializeOrganizationNode(node);
    $('#selected-organization-list').append(getOrganizationItemString(organization['hash'], organization['name'], REMOVE_REFERENCE))
    selectedOrganizations.push(organization['name']);
    $(node).remove();
}

function onOrganizationRemoveCallback(node) {
    let organization = deserializeOrganizationNode(node);
    // remove organization
    selectedOrganizations = selectedOrganizations.filter(collaborator => collaborator !== organization['name']);
    $(node).remove();
}

$('#member-lookup-input').on("change keyup paste", function () {
    let httpRequest = openRestHttpGetRequest('/users/lookupMembers?input='.concat($(this).val()));

    httpRequest.onload = function () {
        let response = new UserMessage(httpRequest.responseText);
        if (response.isSuccess()) {
            let userListSelector = $('#lookup-user-list');
            let users = response.responseData["users"];

            userListSelector.empty();
            for (let user of users) {
                if (!selectedUsers.includes(user['username'])) {
                    userListSelector.append(getUserItemString(user['username'], user['fullName'], ADD_REFERENCE));
                }
            }
        }

    }
    httpRequest.send();
});


$('#user-lookup-input').on("change keyup paste", function () {
    let httpRequest = openRestHttpGetRequest('/users/lookup?input='.concat($(this).val()));

    httpRequest.onload = function () {
        let userListSelector = $('#lookup-user-list');
        let users = [];
        if (httpRequest.responseText !== '') {
            users = JSON.parse(httpRequest.responseText);
        }

        userListSelector.empty();
        for (let user of users) {
            if (!selectedUsers.includes(user['username'])) {
                userListSelector.append(getUserItemString(user['username'], user['fullName'], ADD_REFERENCE));
            }
        }
    }
    httpRequest.send();
});

$('#organization-lookup-input').on("change keyup paste", function () {
    let httpRequest = openRestHttpGetRequest('/organizations/lookup?input='.concat($(this).val()));

    httpRequest.onload = function () {
        let collaboratorListSelector = $('#lookup-organization-list');
        let organizations = [];
        if (httpRequest.responseText !== '') {
            organizations = JSON.parse(httpRequest.responseText);
        }

        collaboratorListSelector.empty();
        for (let organization of organizations) {
            if (!selectedOrganizations.includes(organization['organizationName'])) {
                collaboratorListSelector.append(getOrganizationItemString(organization['organizationHash'], organization['organizationName'], ADD_REFERENCE));
            }
        }
    }
    httpRequest.send();
});

$('#submit-organization').hover(function () {
    let selectedUsersNode = $('#selectedUsers');
    selectedUsersNode.val(selectedUsers.toString());
})

function getUserItemString(username, fullName, identifier) {
    let callback;
    if (identifier === ADD_REFERENCE) {
        callback = 'onUserAddCallback(this)';
    } else if (identifier === REMOVE_REFERENCE) {
        callback = 'onUserRemoveCallback(this)';
    } else {
        return '';
    }
    return `<li style="cursor:pointer;" class="${identifier}" username="${username}" onclick="${callback}">${fullName}</li>`;
}

function getOrganizationItemString(hash, name, identifier) {
    let callback;
    if (identifier === ADD_REFERENCE) {
        callback = 'onOrganizationAddCallback(this)';
    } else if (identifier === REMOVE_REFERENCE) {
        callback = 'onOrganizationRemoveCallback(this)';
    } else {
        return '';
    }
    return `<li style="cursor:pointer;" class="${identifier}" hash="${hash}" onclick="${callback}">${name}</li>`;
}

function deserializeUserNode(node) {
    return {
        'username': $(node).attr('username'),
        'fullName': $(node).text()
    }
}

function deserializeOrganizationNode(node) {
    return {
        'name': $(node).text(),
        'hash': $(node).attr('hash')
    }
}

function getRandomPicture() {
    if (defaultUserPictures.length === 0) {
        loadPicturesFromServer();
    }
    let randomPicture = defaultUserPictures[Math.floor(Math.random() * defaultUserPictures.length)];
    return randomPicture['imageName'];
}

function loadPicturesFromServer() {
    let request = openRestHttpGetRequest('/res/defaultImageList');
    request.onreadystatechange = function () {
        defaultUserPictures = JSON.parse(request.responseText);
    }
    request.send();
}