<div class="modal fade" id="show-task-modal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true"
     role="dialog">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header bg-forth">
                <h5 class="modal-title" id="exampleModalLabel" style="color: black">Task Page</h5>
                <button type="button" class="btn-close" data-mdb-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="form-container">
                    <div class="d-flex flex-column p-1">
                        <input type="hidden" id="task-hash-field">
                        <div class="form-outline form-white mb-2">
                            <input type="text" id="task-name-field" class="form-control"/>
                            <label class="form-label" for="task-name-field">Task</label>
                        </div>
                        <div class="form-outline form-white mb-2">
                            <div id="task-toolbar"></div>
                            <div id="task-editor"></div>
                        </div>
                        <div class="d-flex flex-column">
                            <div class="form-outline form-white mb-2 bg-sub rounded">
                                <input type="text" id="assignee-input-area" class="form-control"/>
                                <label class="form-label" for="assignee-input-area">Look for assignee here</label>
                            </div>
                            <div class="d-flex flex-row justify-content-center">
                                <div class="w-50">
                                    <select multiple size="5" name="assigneeUserName" id="assignee-select-box"
                                            class="available-data scrollbar scrollbar-y scrollbar-rare-wind d-block user-select-none text-center w-100 h-50"></select>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button id="close-task-window-btn" type="button" class="btn bg-main" data-mdb-dismiss="modal">
                    Close
                </button>
                <button id="delete-task-btn" type="button" class="btn btn-danger" data-mdb-dismiss="modal">Delete</button>
                <button id="update-task-btn" type="button" class="btn bg-sub">Save</button>
            </div>
        </div>
    </div>
</div>
