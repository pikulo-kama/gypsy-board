const REMOVE_REFERENCE = 'user-remove-reference';
const ADD_REFERENCE = 'user-add-reference';

let selectedUsers = [];
let defaultUserPictures = [];

$('#remove-organization-btn').on('click', function () {
    let organizationHash = $('.organization-container').attr('id');
    ConfirmModal.openConfirmWindow(organizationHash, 'handleOrganizationDeletion');
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

$(document).ready(function () {
    $('.user-image').each(function () {
        $(this).attr('src', getRandomPicture());
    });
});

function onUserAddCallback(node) {
    let user = deserializeNode(node);
    $('#selected-user-list').append(getListItemString(user['username'], user['fullName'], REMOVE_REFERENCE))
    selectedUsers.push(user['username'])
    $(node).remove();
}

function onUserRemoveCallback(node) {
    let user = deserializeNode(node);
    // remove user
    selectedUsers = selectedUsers.filter(innerUser => innerUser !== user['username']);
    $(node).remove();
}

$('#user-lookup-input').on("change keyup paste", function () {
    let httpRequest = openRestHttpGetRequest('/users/lookup?input='.concat($(this).val()));

    httpRequest.onreadystatechange = function () {
        let userListSelector = $('#lookup-user-list');
        let users = [];
        if (httpRequest.responseText !== '') {
            users = JSON.parse(httpRequest.responseText);
        }

        userListSelector.empty();
        for (let user of users) {
            if (!selectedUsers.includes(user['username'])) {
                userListSelector.append(getListItemString(user['username'], user['fullName'], ADD_REFERENCE));
            }
        }
    }
    httpRequest.send();
});

$('#submit-organization').hover(function () {
    let selectedUsersNode = $('#selectedUsers');
    selectedUsersNode.val(selectedUsers.toString());
})

function getListItemString(username, fullName, identifier) {
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

function deserializeNode(node) {
    return {
        'username': $(node).attr('username'),
        'fullName': $(node).text()
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