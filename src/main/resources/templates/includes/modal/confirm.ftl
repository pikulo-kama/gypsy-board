<div class="modal fade" id="confirm-modal" tabindex="-1" aria-labelledby="confirm-modal-label" aria-hidden="true" role="dialog">
    <div class="modal-dialog modal-sm">
        <div class="modal-content text-center">
            <div class="modal-header bg-danger text-white d-flex justify-content-center">
                <h5 class="modal-title" id="confirm-modal-label">Are you sure?</h5>
            </div>
            <div class="modal-body">
                <i class="fas fa-times fa-3x text-danger"></i>
            </div>
            <div class="modal-footer d-flex justify-content-center">
                <input id="confirm-data-field" type="hidden">
                <input id="confirm-handler-name-field" type="hidden">
                <button id="dismiss-confirm-window-btn" type="button" class="btn btn-danger" data-mdb-dismiss="modal">No</button>
                <button id="confirm-btn" type="button" class="btn btn-outline-danger">
                    Yes
                </button>
            </div>
        </div>
    </div>
</div>