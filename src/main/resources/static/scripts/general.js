const API_ROUTE_PREFIX = '/api/v1';
const GET_REQUEST_TYPE = 'GET';
const POST_REQUEST_TYPE = 'POST';


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