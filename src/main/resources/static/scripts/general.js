const API_ROUTE_PREFIX = '/api/v1';
const GET_REQUEST_TYPE = 'GET';
const POST_REQUEST_TYPE = 'POST';

$(document).ready(function () {
    let request = openRestHttpGetRequest('/organizations/restrictionSelector');
    request.onload = function () {
        let response = new UserMessage(request.responseText);
        if (response.isSuccess()) {
            let restrictedObjectsSelector = response.responseData['selector'];
            $(restrictedObjectsSelector).addClass('inactive');
        }
    }
    request.send();
});

function openRestHttpGetRequest(url) {
    let request = new XMLHttpRequest();
    request.open(GET_REQUEST_TYPE, API_ROUTE_PREFIX.concat(url));
    return request;
}

function openRestHttpPostRequest(url) {
    let request = new XMLHttpRequest();
    request.open(POST_REQUEST_TYPE, API_ROUTE_PREFIX.concat(url));
    request.setRequestHeader('Content-Type', 'application/json');
    request.setRequestHeader('Accept', 'application/json');
    request.setRequestHeader('X-CSRF-Token', getCsrfToken());
    return request;
}

function getCsrfToken() {
    return $('input[name="_csrf"]').val();
}