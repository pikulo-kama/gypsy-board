<#include "../includes/header.ftl">
<div class="d-flex flex-column mt-5 align-items-center text-center">
    <p class="display-4 text-uppercase">Login</p>
    <form method="post" action="/login">

        <#if success_message??>
            <div class="alert alert-success" role="alert">${success_message}</div>
        </#if>

        <#if error_message??>
            <div class="alert alert-danger" role="alert">${error_message}</div>
        </#if>

        <#--    Username    -->
        <div class="form-outline mb-4">
            <input required name="username" type="text" id="username-address-field" class="form-control bg-third" />
            <label class="form-label" for="username-address-field">Username</label>
        </div>

        <!-- Password -->
        <div class="form-outline mb-4">
            <input required name="password" type="password" id="password-field" class="form-control bg-third" />
            <label class="form-label" for="password-field">Password</label>
        </div>

        <!-- Submit button -->
        <input name="${(_csrf.parameterName)!}" value="${(_csrf.token)!}" type="hidden" />
        <button type="submit" class="btn btn-primary btn-block mb-4 bg-sub">Login</button>
    </form>
</div>

<#include "../includes/footer.ftl">