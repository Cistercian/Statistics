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
<jsp:include page="/WEB-INF/view/tags/nav-panel.jsp"></jsp:include>


<div class="content container-fluid wam-radius wam-min-height-0">
    <input id="_csrf_token" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <div class='row'>
        <div class="container-fluid wam-not-padding-xs">

            <div class="panel panel-default wam-margin-panel">
                <div class="panel-heading wam-page-title">
                    <h3 class="wam-margin-bottom-0 wam-margin-top-0">Таблица пользователей</h3>
                </div>
                <div class="panel-body wam-not-padding">
                    <table class="table table-striped table-bordered table-text wam-margin-top-2 dataTable no-footer ">
                        <thead>
                        <tr>
                            <td onclick="location.href='?sort=username,${sortDirection}';" class="wam-cursor">Имя
                                пользователя
                            </td>
                            <td onclick="location.href='?sort=rating,${sortDirection}';" class="wam-cursor">Рейтинг</td>
                            <td onclick="location.href='?sort=countWrittenTopics,${sortDirection}';" class="wam-cursor">
                                Кол-во топиков
                            </td>
                            <td onclick="location.href='?sort=countComments,${sortDirection}';" class="wam-cursor">
                                Кол-во комментариев
                            </td>
                        </tr>
                        </thead>
                        <tbody>
                        <c:set var="step" value="0" scope="page"/>
                        <c:forEach items="${users}" var="user">
                            <tr class="">
                                <td><a href="${user.getProfile()}">${user.getUsername()}</a></td>
                                <td>${user.getRating()}</td>
                                <td>${user.getCountWrittenTopics()}</td>
                                <td>${user.getCountComments()}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <div class="pagination">
                        <li class="paginate_button previous ${currentPage > 1 ? '' : 'disabled'} " id="stats_previous">
                            <a href="?page=1&sort=${sort}">Первая</a>
                        </li>

                        <c:if test='${currentPage > 3}'>
                            <li class="paginate_button disabled"><span>...</span></li>
                        </c:if>


                        <c:if test='${currentPage > 2}'>
                            <li class="paginate_button"><a
                                    href="?page=${currentPage - 1}&sort=${sort}">${currentPage - 1}</a></li>
                        </c:if>

                        <li class="paginate_button active"><span>${currentPage}</span></li>

                        <c:if test='${totalPage - currentPage >= 2}'>
                            <li class="paginate_button"><a
                                    href="?page=${currentPage + 1}&sort=${sort}">${currentPage + 1}</a></li>
                        </c:if>
                        <c:if test='${totalPage - currentPage >= 3}'>
                            <li class="paginate_button disabled"><span>...</span></li>
                        </c:if>
                        <c:if test='${currentPage < totalPage}'>
                            <li class="paginate_button"><a href="?page=${totalPage}&sort=${sort}">Последняя</a></li>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>