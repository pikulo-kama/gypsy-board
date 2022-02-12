<#include "../includes/header.ftl" >

<div class="back"></div>
<div class="registration-form">
    <header>
        <h1>Sign Up</h1>
        <p>Input you information</p>
    </header>
    <form action="/auth/register-user" method="POST">
        <div class="input-section username-section">
            <input class="username" name="username" type="text" placeholder="ENTER YOUR USERNAME HERE" autocomplete="off"/>
            <div class="animated-button"><span class="icon-paper-plane"><i class="fa fa-envelope-o">
                    </i></span><span class="next-button username"><i class="fa fa-arrow-up"></i></span>
            </div>
        </div>
        <div class="input-section first-name-section folded">
            <input class="first-name" name="firstName" type="text" placeholder="ENTER YOUR FIRST NAME HERE" autocomplete="off"/>
            <div class="animated-button"><span class="icon-paper-plane"><i class="fa fa-envelope-o">
                    </i></span><span class="next-button first-name"><i class="fa fa-arrow-up"></i></span>
            </div>
        </div>
        <div class="input-section last-name-section folded">
            <input class="last-name" name="lastName" type="text" placeholder="ENTER YOUR LAST NAME HERE" autocomplete="off"/>
            <div class="animated-button"><span class="icon-paper-plane"><i class="fa fa-envelope-o">
                    </i></span><span class="next-button last-name"><i class="fa fa-arrow-up"></i></span>
            </div>
        </div>
        <div class="input-section password-section folded">
            <input class="password" name="password" type="password" placeholder="ENTER YOUR PASSWORD HERE"/>
            <div class="animated-button"><span class="icon-lock">
                    <i class="fa fa-lock"></i></span><span class="next-button password">
                    <i class="fa fa-arrow-up"></i></span>
            </div>
        </div>
        <div class="input-section repeat-password-section folded">
            <input class="repeat-password" name="passwordRepeat" type="password" placeholder="REPEAT YOUR PASSWORD HERE"/>
            <div class="animated-button"><span class="icon-repeat-lock">
                    <i class="fa fa-lock"></i></span><span class="next-button repeat-password">
                    <i class="fa fa-paper-plane"></i></span>
            </div>
        </div>
        <input name="${(_csrf.parameterName)!}" value="${(_csrf.token)!}" type="hidden" />
        <button type="submit" class="app-button register-user-button" style="display: none;"></button>
        <div class="success">
            <p>ACCOUNT CREATED</p>
        </div>
    </form>
</div>

<link rel="stylesheet" href="/css/register.css">
<script type="text/javascript" src="/scripts/register.js"></script>

<#include "../includes/footer.ftl" >

