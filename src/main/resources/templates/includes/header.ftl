<!doctype html>
<html lang="uk">
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/3.10.2/mdb.min.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css"/>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap"/>

    <link href="https://fonts.googleapis.com/css2?family=Teko&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="/css/general.css">
    <link rel="stylesheet" href="/css/popup.css">

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>

    <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js" type="text/javascript"></script>
    <script src="https://kit.fontawesome.com/926f077d93.js" crossorigin="anonymous"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/mdb-ui-kit/3.10.2/mdb.min.js"></script>
    <script type="text/javascript" src="/scripts/popup.js"></script>
    <script type="text/javascript" src="/scripts/general.js"></script>
    <script type="text/javascript" src="/scripts/domain/usermessage.js"></script>
    <script type="text/javascript" src="/scripts/domain/boardcolumn.js"></script>

    <link rel="shortcut icon" href="/images/favicon.ico" type="image/ico">
    <input name="${(_csrf.parameterName)!}" value="${(_csrf.token)!}" type="hidden" />
    <title>Gypsy Board</title>
</head>
<body>
<#if error??>
    <script type="text/javascript">
        $(document).ready(function () {
            showPopup('${error.message}',
                '${error.userMessageType.primaryColor}',
                '${error.userMessageType.secondaryColor}');
        })
    </script>
</#if>

<#include "../includes/modal/confirm.ftl">

<div class="popup-container">
    <div class="popup-content">
        <div class="popup-close-button">&times;</div>
        <p class="popup-message"></p>
    </div>
</div>

<nav class="navbar navbar-expand-lg navbar-light fixed-top bg-forth">
    <div class="container-fluid">
        <img src="images/logo.svg" style="width: 4em" alt="">
        <a class="navbar-brand" style="font-family: 'Teko', sans-serif; font-size: 2em" href="/">Gypsy Board</a>

        <button class="navbar-toggler" type="button" data-mdb-toggle="collapse"
                data-mdb-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false"
                aria-label="Toggle navigation"><i class="fas fa-bars"></i></button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item"><a class="nav-link" href="/organizations/active"><i class="fas fa-bullseye"></i>  Active Organization</a></li>
            </ul>
            <ul class="navbar-nav d-flex flex-row me-1">
                <li class="nav-item me-3 me-lg-0">
                    <a class="nav-link btn btn-outline-dark bg-third" href="/logout"><i class="fas fa-door-open"></i></a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="mt-5 pt-3">