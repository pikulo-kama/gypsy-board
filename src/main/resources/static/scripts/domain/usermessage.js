class UserMessage {

    static SUCCESS = 'SUCCESS';
    static ERROR = 'ERROR';

    message = '';
    messageType = '';
    responseData = {};

    constructor(response) {
        let isString = typeof response === 'string' || response instanceof String;
        let isDictionary = response.constructor === Object;

        if (!isString && !isDictionary) {
            throw new Error("Insufficient type provided. Expected string or dict.");
        } else if (isString) {
            response = JSON.parse(response);
        }

        this.message = response['message'];
        this.messageType = response['userMessageType'];
        this.responseData = response['responseData'];
    }

    isSuccess() {
        return this.messageType === UserMessage.SUCCESS;
    }

    isError() {
        return this.messageType === UserMessage.ERROR;
    }
}