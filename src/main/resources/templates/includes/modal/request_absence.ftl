<div class="modal fade" id="request-absence-modal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true"
     role="dialog">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header bg-forth">
                <h5 class="modal-title" id="exampleModalLabel" style="color: black">Request Absence Page</h5>
                <button type="button" class="btn-close" data-mdb-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="form-container">
                    <div class="d-flex flex-column p-1">
                        <input type="hidden" id="task-hash-field">
                        <div class="form-outline form-white mb-2">
                            <input type="date" id="absence-start-date-field" class="form-control"/>
                            <label class="form-label" for="absence-start-date-field">Start Date</label>
                        </div>
                        <div class="form-outline form-white mb-2">
                            <input type="date" id="absence-end-date-field" class="form-control"/>
                            <label class="form-label" for="absence-end-date-field">End Date</label>
                        </div>
                        <div class="form-group form-white mb-2">
                            <label class="form-label" for="absence-type">Absence Type</label>
                            <select name="absenceType" id="absence-type" class="form-control">
                                <option value="VACATION">Vacation</option>
                                <option value="SICKNESS">Sickness</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button id="close-request-absence-window-btn" type="button" class="btn bg-main" data-mdb-dismiss="modal">
                    Close
                </button>
                <button id="request-absence-btn" type="button" class="btn bg-sub">Request</button>
            </div>
        </div>
    </div>
</div>
