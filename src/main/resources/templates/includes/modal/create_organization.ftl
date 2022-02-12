<div class="modal fade" id="create-organization-modal" tabindex="-1" aria-labelledby="exampleModalLabel"
     aria-hidden="true" role="dialog">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header bg-forth">
                <h5 class="modal-title" id="exampleModalLabel" style="color: black">Create Organization</h5>
                <button type="button" class="btn-close" data-mdb-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="form-container">
                    <div class="d-flex flex-column">
                        <div class="d-flex flex-column h-100">
                            <div class="form-outline form-white mb-1">
                                <input type="text" id="organization-name-field" class="form-control"/>
                                <label class="form-label" for="organization-name-field">Organization Name</label>
                            </div>
                            <input name="${(_csrf.parameterName)!}" value="${(_csrf.token)!}" type="hidden"/>
                            <input id="selectedUsers" type="hidden" name="memberUsernames">
                        </div>

                        <div class="form-outline form-white mb-1 mt-3">
                            <input type="text" id="user-lookup-input" class="form-control"/>
                            <label class="form-label" for="user-lookup-input">Lookup Users Here</label>
                        </div>

                        <div class="d-flex flex-row justify-content-center">
                            <div class="available-data scrollbar scrollbar-y scrollbar-rare-wind w-50" style="max-height: 30vh">
                                <ul id="selected-user-list" class="data-list"></ul>
                            </div>

                            <div class="available-data scrollbar scrollbar-y scrollbar-rare-wind w-50" style="max-height: 30vh">
                                <ul id="lookup-user-list" class="data-list"></ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button id="close-organization-window-btn" type="button" class="btn bg-main" data-mdb-dismiss="modal">
                    Close
                </button>
                <button id="create-organization-btn" type="button" class="btn bg-sub">Save</button>
            </div>
        </div>
    </div>
</div>
