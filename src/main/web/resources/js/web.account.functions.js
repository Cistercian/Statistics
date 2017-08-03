/**
 * Created by Olaf on 30.04.2017.
 */
/**
 * функция очистки модального окна bootstrap
 * @constructor
 */
function ClearModal() {
    //удаляем прежние amount
    $('[id^="modalBody"]').each(function () {
        $(this).empty();
    });
    $('#modalHeader').empty();
    $('#modalHeader').append(
        "<div class='login-panel panel panel-default wam-not-padding '>" +
        "<div id='header' class='panel-heading'>" +
        "</div>" +
        "</div>");

    //рисуем структуру вывода данных
    $('#modalBody').append(
        "<div class='login-panel panel panel-default wam-not-padding '>" +
        "<div class='panel-body wam-not-padding'>" +
        "<div id='parentBars'" +
        "</div>" +
        "</div>" +
        "</div>");
}
/**
 * Функция вызова waitingDialog.js при выполнении ajax запросов
 */
function displayLoader() {
    waitingDialog.show('Загрузка...', {dialogSize: 'sm', progressType: 'warning'});
}
function hideLoader() {
    waitingDialog.hide();
}
/**
 * Функция очистки модального окна bootstrap
 *
 */
function ClearModalPanel() {
    //$('#modal').modal('hide');

    $('#modalTitle').text("");
    $('[id^="modalBody"]').each(function () {
        $(this).empty();
    });
    $('[id^="modalFooter"]').each(function () {
        $(this).empty();
    });
    //гарантированно чистим остатки всплывающего окна
    $('.modal-backdrop').each(function () {
        $(this).remove();
    });
    //гарантированно-гарантированно чистим остатки всплывающего окна
    $('body').removeClass('modal-open');

}
function setModalSize(type){
    if (type.indexOf("auto") !== -1) {
        //форматируем модальное окно под авторазмер
        $('.modal-lg').addClass('wam-modal-dialog');
        $('.modal-lg').removeClass('modal-dialog');
    } else {
        $('.modal-lg').addClass('modal-dialog');
        $('.modal-lg').removeClass('wam-modal-dialog');
    }
}