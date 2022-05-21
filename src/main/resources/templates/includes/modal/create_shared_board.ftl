<div class="modal fade" id="create-shared-board-modal" tabindex="-1" aria-labelledby="exampleModalLabel"
     aria-hidden="true" role="dialog">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header bg-forth">
                <h5 class="modal-title" id="exampleModalLabel" style="color: black">Create Shared Board</h5>
                <button type="button" class="btn-close" data-mdb-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="form-container">
                    <div class="d-flex flex-column">
                        <div class="d-flex flex-column h-100">
                            <div class="form-outline form-white mb-1">
                                <input type="text" id="shared-board-name-field" class="form-control"/>
                                <label class="form-label" for="shared-board-name-field">Board Name</label>
                            </div>
                            <input name="${(_csrf.parameterName)!}" value="${(_csrf.applicationToken)!}" type="hidden"/>
                            <input id="selectedOrganizations" type="hidden" name="collaboratorsNames">
                        </div>

                        <div class="form-outline form-white mb-1 mt-3">
                            <input type="text" id="organization-lookup-input" class="form-control"/>
                            <label class="form-label" for="organization-lookup-input">Search Collaborators Here</label>
                        </div>

                        <div class="d-flex flex-row justify-content-center">
                            <div class="available-data scrollbar scrollbar-y scrollbar-rare-wind w-50" style="max-height: 30vh">
                                <ul id="selected-organization-list" class="data-list"></ul>
                            </div>

                            <div class="available-data scrollbar scrollbar-y scrollbar-rare-wind w-50" style="max-height: 30vh">
                                <ul id="lookup-organization-list" class="data-list"></ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button id="close-shared-board-window-btn" type="button" class="btn bg-main" data-mdb-dismiss="modal">
                    Close
                </button>
                <button id="create-shared-board-btn" type="button" class="btn bg-sub">Save</button>
            </div>
        </div>
    </div>
</div>
