class ConfirmModal {

    static CONFIRM_WINDOW = $('#confirm-modal');

    static DISMISS_BUTTON = $('#dismiss-confirm-window-btn');
    static CONFIRM_BUTTON = $('#confirm-btn');

    static DATA_FIELD = $('#confirm-data-field');
    static HANDLER_FIELD = $('#confirm-handler-name-field');

    static openConfirmWindow(data, handler) {
        ConfirmModal.DATA_FIELD.val(data);
        ConfirmModal.HANDLER_FIELD.val(handler);

        ConfirmModal.CONFIRM_WINDOW.addClass("show")
        ConfirmModal.CONFIRM_WINDOW.show();
    }

    static closeConfirmWindow() {
        ConfirmModal.DATA_FIELD.val('');
        ConfirmModal.HANDLER_FIELD.val('');

        ConfirmModal.CONFIRM_WINDOW.removeClass("show")
        ConfirmModal.CONFIRM_WINDOW.hide();
    }
}

ConfirmModal.DISMISS_BUTTON.on('click', function () {
    ConfirmModal.closeConfirmWindow();
});

ConfirmModal.CONFIRM_BUTTON.on('click', function () {
    let data = ConfirmModal.DATA_FIELD.val();
    let handler = ConfirmModal.HANDLER_FIELD.val();

    let handlerFunction = eval(handler);
    handlerFunction(data);
});