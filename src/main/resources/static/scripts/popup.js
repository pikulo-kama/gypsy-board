$(document).ready(function () {
    $('.popup-close-button').click(function(){
        $('.popup-container').hide();
    });
});

function showPopup(message , primaryColor, secondaryColor) {
    $('.popup-content').css('background-image', 'linear-gradient(#233323, black), ' +
        `linear-gradient(to left, ${primaryColor}, ${secondaryColor})`);
    $('.popup-message').css('background', `-webkit-linear-gradient(${primaryColor}, ${secondaryColor})`);
    $('.popup-message').css('-webkit-background-clip', 'text');
    $('.popup-message').css('-webkit-text-fill-color', 'transparent');
    $('.popup-close-button').css('color', primaryColor)
                            .hover(() => {
                                $(this).css('color', secondaryColor);
                            });

    $('.popup-message').text(message);
    $('.popup-container').css('cursor', 'pointer');
    $('.popup-container').show();
}
