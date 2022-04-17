
$("#request-absence-btn").on('click', function () {
    let request = openRestHttpPostRequest("/absences/request");
    let startDate = $("#absence-start-date-field").val();
    let endDate = $("#absence-end-date-field").val();
    let absenceType = $("#absence-type").find(':selected').attr('value');

    request.onload = function () {
        let response = new UserMessage(request.responseText);
        if (response.isSuccess()) {
            let createdRecord = response.responseData['createdRecord'];
            let html = getAbsenceRecordHtml(createdRecord);
            $('#absence-record-table-body').append(html);
        }
        $("#close-request-absence-window-btn").trigger('click');
    }
    request.send(JSON.stringify({
        "startDate": startDate,
        "endDate": endDate,
        "absenceType": absenceType
    }));
});

$(document).on('click', '.cancel-request-btn', function () {
    let absenceHash = $(this).closest('tr').attr('id');
    ConfirmModal.openConfirmWindow(absenceHash, "handleAbsenceRequestCancellation");
});

$('.approve-request-btn').on('click', function () {
    let absenceHash = $(this).closest('tr').attr('id');
    ConfirmModal.openConfirmWindow(absenceHash, "handleAbsenceRequestApproval");
});

function handleAbsenceRequestCancellation(absenceHash) {
    let request = openRestHttpPostRequest(`/absences/cancel?abs=${absenceHash}`);
    request.onload = function () {
        let response = new UserMessage(request.responseText);
        if (response.isSuccess()) {
            let recordContainer = $(`#${absenceHash}`);
            recordContainer.addClass("bg-warning");

            let btn = recordContainer.find('.cancel-request-btn')
            btn.addClass('disabled')
        }
        ConfirmModal.closeConfirmWindow();
    }
    request.send();
}

function handleAbsenceRequestApproval(absenceHash) {
    let request = openRestHttpPostRequest(`/absences/approve?abs=${absenceHash}`);
    request.onload = function () {
        let response = new UserMessage(request.responseText);
        if (response.isSuccess()) {
            $(`#${absenceHash}`).remove();
        }
        ConfirmModal.closeConfirmWindow();
    }
    request.send();
}

function getAbsenceRecordHtml(record) {

    let approvalIcon;
    if (record['isApproved']) {
        approvalIcon = '<i class="fas fa-check-circle text-success fa-lg"></i>';
    } else {
        approvalIcon = '<i class="fas fa-times-circle text-danger fa-lg"></i>';
    }

    let bgWarning = record['isCancelled'] ? 'bg-warning' : '';
    let disabled = record['isCancelled'] ? 'disabled' : '';

    return `<tr id="${record['absenceRecordHash']}" class="absence-record ${bgWarning}">
                <td>${approvalIcon}</td>
                <td>${record['startDate'].split('T')[0]}</td>
                <td>${record['endDate'].split('T')[0]}</td>
                <td>${record['absenceDays']}</td>
                <td class="fw-bold">${record['absenceType']['icon']} ${record['absenceType']['representation']}</td>
                <td>
                    <button id="cancel-request-btn"
                            type="button"
                            class="btn btn-sm btn-outline-danger ${disabled}">
                        <i class="fas fa-ban fa-xs"></i>
                    </button>
                </td>
            </tr>`;
}