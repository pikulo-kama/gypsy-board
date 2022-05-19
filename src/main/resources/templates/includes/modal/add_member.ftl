<div class="modal fade" id="add-members-modal" tabindex="-1" aria-labelledby="exampleModalLabel"
     aria-hidden="true" role="dialog">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header bg-forth">
                <h5 class="modal-title" id="exampleModalLabel" style="color: black">Add Members</h5>
                <button type="button" class="btn-close" data-mdb-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="form-container">
                    <div class="d-flex flex-column">
                        <div class="d-flex flex-column h-100">
                            <input name="${(_csrf.parameterName)!}" value="${(_csrf.applicationToken)!}" type="hidden"/>
                            <input id="selectedUsers" type="hidden" name="memberUsernames">
                        </div>

                        <div class="form-outline form-white mb-1 mt-3">
                            <input type="text" id="member-lookup-input" class="form-control" autocomplete="off"/>
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
                <button id="close-add-members-window-btn" type="button" class="btn bg-main" data-mdb-dismiss="modal">
                    Close
                </button>
                <button id="add-members-modal-btn" type="button" class="btn bg-sub">Add</button>
            </div>
        </div>
    </div>
</div>
