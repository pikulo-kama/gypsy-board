// Prevents pressing enter in scope of form object
$(document).keypress((event) => {
    if (event.target.nodeName === 'INPUT' && event.which === 13 && event.target.type !== 'submit') {
        event.preventDefault();
    }
})

$('.username, .first-name, .last-name').on("change keyup paste",
    function(){
        if($(this).val()){
            $('.icon-paper-plane').addClass("next");
        } else {
            $('.icon-paper-plane').removeClass("next");
        }
    }
);

$('.password').on("change keyup paste",
    function(){
        if($(this).val()){
            $('.icon-lock').addClass("next");
        } else {
            $('.icon-lock').removeClass("next");
        }
    }
);

$('.repeat-password').on("change keyup paste",
    function(){
        if($(this).val()){
            $('.icon-repeat-lock').addClass("next");
        } else {
            $('.icon-repeat-lock').removeClass("next");
        }
    }
);

$('.next-button.username').click(
    function(){
        $('.username-section').addClass("fold-up");
        $('.first-name-section').removeClass("folded");
        $('.icon-paper-plane').removeClass("next");
    }
);

$('.next-button.first-name').click(
    function(){
        $('.first-name-section').addClass("fold-up");
        $('.last-name-section').removeClass("folded");
        $('.icon-paper-plane').removeClass("next");
    }
);

$('.next-button.last-name').click(
    function(){
        $('.last-name-section').addClass("fold-up");
        $('.password-section').removeClass("folded");
        $('.icon-paper-plane').removeClass("next");
    }
);

$('.next-button.password').click(
    function(){
        $('.password-section').addClass("fold-up");
        $('.repeat-password-section').removeClass("folded");
        $('.icon-paper-plane').removeClass("next");
    }
);

$('.next-button.repeat-password').click(
    function(){
        $('.repeat-password-section').addClass("fold-up");
        $('.success').css("marginTop", 0);
        $('.icon-paper-plane').removeClass("next");
        $('.register-user-button').trigger("click");
    }
);

$('.next-button').hover(
    function(){
        $(this).css('cursor', 'pointer');
    }
);
