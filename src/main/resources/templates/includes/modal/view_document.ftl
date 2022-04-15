<div class="modal fade" id="show-document-modal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true"
     role="dialog">
    <div class="modal-dialog modal-fullscreen modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header bg-forth">
                <h5 class="modal-title" id="exampleModalLabel" style="color: black">Document Page</h5>
                <button type="button" class="btn-close" data-mdb-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="form-container">
                    <div class="d-flex flex-column p-1">
                        <input type="hidden" id="document-hash-field">
                        <div class="form-outline form-white mb-2">
                            <input type="text" id="update-modal-document-header-field" class="form-control"/>
                            <label class="form-label" for="task-name-field">Task</label>
                        </div>
                        <div class="form-outline form-white mb-2">
                            <div id="update-document-toolbar"></div>
                            <div id="update-document-editor" style="min-height: 50vh"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button id="close-edit-document-window-btn" type="button" class="btn bg-main" data-mdb-dismiss="modal">
                    Close
                </button>
                <button id="delete-document-btn" type="button" class="btn btn-danger" data-mdb-dismiss="modal">Delete</button>
                <button id="update-document-btn" type="button" class="btn bg-sub">Save</button>
            </div>
        </div>
    </div>
</div>
