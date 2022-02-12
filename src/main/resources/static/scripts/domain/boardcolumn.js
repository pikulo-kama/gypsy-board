class BoardColumn {

    static BOARD_COLUMN_SELECTOR = '.board-column';
    static BOARD_COLUMN_PLACEHOLDER_SELECTOR = '#board-columns-placeholder';
    static BOARD_COLUMN_HTML = `<div class="d-flex flex-column board-column justify-content-evenly position-relative" id="{columnHash}">
                    <div class="position-absolute top-0 end-0 me-2 mt-2">
                        <a role="button" class="remove-board-column-btn fg-forth hover-fg-sub">
                            <i class="fas fa-recycle"></i>
                        </a>
                    </div>
                    <div class="column-name-field fg-sub text-center text-uppercase font-weight-bold"
                         contenteditable="true">{columnName}</div>
                    <div class="board-tasks-container scrollbar scrollbar-y scrollbar-rare-wind force-overflow w-100 ms-1">
                        <ul class="list-unstyled">
                                <li class="draggable-task"></li>
                        </ul>
                    </div>
                    <div class="add-task-area d-flex flex-row-reverse justify-content-evenly">
                        <div class="d-flex align-self-center">
                            <a role="button" class="anchor-disabled add-task-btn fg-forth hover-fg-sub"><i
                                        class="fas fa-plus-square fa-lg"></i></a>
                        </div>
                        <div class="form-outline mt-1 mb-2 w-75">
                            <input type="text" id="formControlSm"
                                   class="add-task-name-field form-control form-control-sm"
                                   style="border: 1px solid var(--sub-color)"/>
                            <label class="form-label" for="formControlSm">Task</label>
                        </div>
                    </div>
                </div>`;

    static DEMO_BOARD_COLUMN_ID = 'demo-board-column';
    static DEMO_BOARD_COLUMN_HTML = `<div class="d-flex flex-column justify-content-evenly board-column opacity-50 user-select-none" id="{demoColumnId}">
                <div class="fg-sub text-center text-uppercase font-weight-bold">.......</div>
                <div class="scrollbar scrollbar-y scrollbar-rare-wind force-overflow w-100 ms-1">
                    <ul class="list-unstyled">
                        <li>
                            <span class="d-inline-block" style="border-left: 2px solid var(--forth-color); height: 10px"></span>
                            <a role="button" class="fg-forth hover-fg-sub" href="#">...</a>
                        </li>
                        <li>
                            <span class="d-inline-block" style="border-left: 2px solid var(--forth-color); height: 10px"></span>
                            <a role="button" class="fg-forth hover-fg-sub" href="#">...</a>
                        </li>
                        <li>
                            <span class="d-inline-block" style="border-left: 2px solid var(--forth-color); height: 10px"></span>
                            <a role="button" class="fg-forth hover-fg-sub" href="#">...</a>
                        </li>
                    </ul>
                </div>
                <div class="d-flex flex-row-reverse justify-content-evenly">
                    <div class="d-flex align-self-center">
                        <a role="button" class="anchor-disabled add-task-btn fg-forth hover-fg-sub"><i
                                    class="fas fa-plus-square fa-lg"></i></a>
                    </div>
                    <div class="form-outline mt-1 mb-2 w-75">
                        <input disabled type="text" id="formControlSm" class="add-task-name-field form-control form-control-sm"
                               style="border: 1px solid var(--sub-color)"/>
                        <label class="form-label" for="formControlSm">Task</label>
                    </div>
                </div>
            </div>`;

    static getColumns() {
        return $(BoardColumn.BOARD_COLUMN_SELECTOR);
    }

    static getColumnsSize() {
        return this.getColumns().length;
    }

    static addColumn(columnName, columnHash) {
        let boardColumn = BoardColumn.BOARD_COLUMN_HTML.replace("{columnHash}", columnHash)
                                                       .replace("{columnName}", columnName);
        $(BoardColumn.BOARD_COLUMN_PLACEHOLDER_SELECTOR).append(boardColumn);
    }

    static addDemoColumn() {
        let demoColumnHtml = BoardColumn.DEMO_BOARD_COLUMN_HTML.replace('{demoColumnId}', BoardColumn.DEMO_BOARD_COLUMN_ID);
        $(BoardColumn.BOARD_COLUMN_PLACEHOLDER_SELECTOR).append(demoColumnHtml);
    }

    static removeDemoColumn() {
        let demoColumn = $(`#${BoardColumn.DEMO_BOARD_COLUMN_ID}`);
        if (demoColumn !== undefined) {
            demoColumn.remove();
        }
    }
}