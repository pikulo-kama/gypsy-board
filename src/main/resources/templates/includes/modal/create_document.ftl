<div class="modal fade" id="create-document-modal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true"
     role="dialog">
    <div class="modal-dialog modal-fullscreen modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header bg-forth">
                <h5 class="modal-title" id="exampleModalLabel" style="color: black">Create New Document</h5>
                <button type="button" class="btn-close" data-mdb-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="form-container">
                    <div class="d-flex flex-column p-1">
                        <div class="form-outline form-white mb-2">
                            <input required type="text" id="document-header-field" class="form-control"/>
                            <label class="form-label" for="document-header-field">Header</label>
                        </div>
                        <div class="form-outline form-white mb-2">
                            <div id="create-document-toolbar"></div>
                            <div id="create-document-editor" style="min-height: 50vh"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button id="close-create-document-window-btn" type="button" class="btn bg-main" data-mdb-dismiss="modal">
                    Close
                </button>
                <button id="create-document-btn" type="button" class="btn bg-sub">Create</button>
            </div>
        </div>
    </div>
</div>


