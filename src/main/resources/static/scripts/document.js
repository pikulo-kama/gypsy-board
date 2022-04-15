var createQuill = new Quill('#create-document-editor', {
    placeholder: 'Please provide only up-to-date information...',
    theme: 'snow'
});

var editQuill = new Quill('#update-document-editor', {
    placeholder: 'Please provide only up-to-date information...',
    theme: 'snow'
});


$('#create-document-btn').on('click', function () {
    let request = openRestHttpPostRequest("/documents/create");
    request.onload = function () {
        let response = new UserMessage(request.responseText);
        if (response.isSuccess()) {
            let html = getDocumentBlock(response.responseData['createdDocument']);
            $('#document-placeholder').append(html);

            createQuill.setContents([{ insert: '\n' }]);
            $('#document-header-field').val('');
        }
        $('#close-create-document-window-btn').trigger('click');
    }
    request.send(JSON.stringify({
        "documentHeader": $('#document-header-field').val(),
        "documentData": JSON.stringify(createQuill.getContents())
    }));
});

$(document).on('click', '.document-container', function () {
    let documentHash = $(this).attr('id');

    let request = openRestHttpGetRequest(`/documents?doc=${documentHash}`);
    request.onload = function () {
        let response = new UserMessage(request.responseText);
        if (response.isSuccess()) {
            let document = response.responseData['document'];

            $('#document-hash-field').val(document['documentHash']);
            $('#update-modal-document-header-field').val(document['documentHeader']);
            editQuill.setContents(JSON.parse(document['documentData']));
        }
    }
    request.send();
});

$('#delete-document-btn').on('click', function () {
    let documentHash = $('#document-hash-field').val();
    ConfirmModal.openConfirmWindow(documentHash, "handleDocumentDeletion");
});

$('#update-document-btn').on('click', function () {
    let documentHash = $('#document-hash-field').val();
    let documentData = JSON.stringify(editQuill.getContents());
    let documentHeader = $('#update-modal-document-header-field').val();

    let request = openRestHttpPostRequest("/documents/update");
    request.onload = function () {
        let response = new UserMessage(request.responseText);
        if (response.isSuccess()) {
            let document = response.responseData['updatedDocument'];
            let container = $(`#${document['documentHash']}`);
            let headerContainer = container.children(':first');
            headerContainer.text(document['documentHeader']);
        }
        $('#close-edit-document-window-btn').trigger('click');
    }
    request.send(JSON.stringify({
        "documentHash": documentHash,
        "documentHeader": documentHeader,
        "documentData": documentData
    }))
});

function handleDocumentDeletion(documentHash) {
    let request = openRestHttpPostRequest(`/documents/delete?doc=${documentHash}`);
    request.onload = function () {
        let response = new UserMessage(request.responseText);
        if (response.isSuccess()) {
            $(`#${documentHash}`).remove();
        }
        ConfirmModal.closeConfirmWindow();
    }
    request.send();
}

function getDocumentBlock(document) {
    return `<div id="${document['documentHash']}" data-mdb-toggle="modal"
             data-mdb-target="#show-document-modal"
             class="document-container hover-bg-sub position-relative">
            <div class="w-100 h-25 text-sm-center font-weight-light font-italic bg-forth fg-sub">
                ${document['documentHeader']}
            </div>
            <div class="document-author-container position-absolute bottom-0 w-100 text-center">
                Created by ${document['authorName']}
            </div>
        </div>`;
}