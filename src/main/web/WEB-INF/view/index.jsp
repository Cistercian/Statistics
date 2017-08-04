<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en-gb" lang="en-gb" dir="ltr">
<head>
    <meta charset="utf-8">
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Работа с БД</title>

    <!-- css -->
    <spring:url value="/resources/css/bootstrap.min.css" var="css"/>
    <link rel="stylesheet" href="${css}">
    <spring:url value="/resources/css/style.css" var="css"/>
    <link rel="stylesheet" href="${css}">
    <link href="https://fonts.googleapis.com/css?family=Roboto+Slab" rel="stylesheet">

    <!-- js -->
    <spring:url value="/resources/js/jquery.js" var="js"/>
    <script src="${js}"></script>
    <spring:url value="/resources/js/bootstrap.min.js" var="js"/>
    <script src="${js}"></script>
    <!--waitingDialog-->
    <spring:url value="/resources/js/waitingDialog.js" var="js"/>
    <script src="${js}"></script>

    <!--custom functions-->
    <spring:url value="/resources/js/web.account.functions.js" var="js"/>
    <script src="${js}"></script>
</head>
<body>

<!-- навигационная панель и модальное окно -->
<%--<jsp:include page="/WEB-INF/view/tags/nav-panel.jsp"></jsp:include>--%>

<script language="javascript" type="text/javascript">
    function testParsing() {
        $.ajax({
            url: '/testParsing',
            type: 'GET',
            data: {},
            dataType: 'json',
            beforeSend: function () {
                displayLoader();
            },
            success: function (data) {
                hideLoader();

                displayMessage('message', data.message);
            }
        });
    }
    function displayMessage(type, message) {
        ClearModalPanel();

        var bodyContent, footerContent;
        if (type == 'delete') {
            bodyContent =
                    "<div class='col-xs-12'>" +
                    "<h4><strong>Вы действительно хотите удалить запись?</strong></h4>" +
                    "</div>";
            footerContent =
                    "<div class='col-xs-12 col-md-4 col-md-offset-4 wam-not-padding'>" +
                    "<button type='button' class='btn btn-danger btn-lg btn-block ' " +
                    "onclick=\"$('#modal').modal('hide');deleteEntity('" + $('#id').val() + "');\">" +
                    "Да" +
                    "</button>" +
                    "</div>" +
                    "<div class='col-xs-12 col-md-4 wam-not-padding'>" +
                    "<button type='button' class='btn btn-primary btn-lg btn-block ' data-dismiss='modal'>" +
                    "Нет" +
                    "</button>" +
                    "</div>";
        } else if (type == 'find') {
            bodyContent =
                    "<div class='col-xs-12'>" +
                    "<h4><strong>Введите строку для поиска</strong></h4>" +
                    "<input id='query' type='text' class='form-control form input-lg'/>" +
                    "</div>";
            footerContent =
                    "<div class='col-xs-12 col-md-4 col-md-offset-4 wam-not-padding'>" +
                    "<button type='button' class='btn btn-primary btn-lg btn-block ' " +
                    "onclick=\"$('#modal').modal('hide');findEntity();\">" +
                    "Поиск" +
                    "</button>" +
                    "</div>" +
                    "<div class='col-xs-12 col-md-4 wam-not-padding'>" +
                    "<button type='button' class='btn btn-default btn-lg btn-block ' data-dismiss='modal'>" +
                    "Отмена" +
                    "</button>" +
                    "</div>";
        } else if (type == 'message') {
            bodyContent =
                    "<div class='col-xs-12'>" +
                    "<h4><strong>" + message + "</strong></h4>" +
                    "</div>";
            footerContent =
                    "<div class='col-xs-12 col-md-4 col-md-offset-4 wam-not-padding'>" +
                    "<button type='button' class='btn btn-primary btn-lg btn-block ' " +
                    "onclick=\"$('#modal').modal('hide')\">" +
                    "ОК" +
                    "</button>" +
                    "</div>";
        }
        $('#modalBody').append(bodyContent);
        $('#modalFooter').append(footerContent);

        $('#modal').modal('show');
    }
</script>

<div class="content container-fluid wam-radius wam-min-height-0">
    <input id="_csrf_token" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <textarea id="response" name="response" style="display: none;">${response}</textarea>
    <div class='row'>
        <div class="container-fluid wam-not-padding-xs">
            <div class="panel panel-default wam-margin-left-1 wam-margin-right-1 wam-margin-top-1 ">
                <div class="panel-heading wam-page-title">
                    <h3 class="wam-margin-bottom-0 wam-margin-top-0">${title}</h3>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-xs-12 col-md-6">
                            <button class="btn-lg btn-default btn-block wam-btn-2" type="submit"
                                    onclick="testParsing();return false;">
                                <span class='wam-font-size-2'>Парсинг</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>