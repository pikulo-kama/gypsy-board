<#include "../includes/header.ftl">
<div class="d-flex flex-column mt-5 align-items-center text-center">
    <p class="display-4 text-uppercase">Registration</p>
    <form method="POST" action="/auth/register-user">

        <#--    Username    -->
        <div class="form-outline mb-4">
            <input required name="username" type="text" id="username-address-field" class="form-control bg-third" />
            <label class="form-label" for="username-address-field">Username</label>
        </div>

        <!-- Email input -->
        <div class="form-outline mb-4">
            <input required name="email" type="email" id="email-address-field" class="form-control bg-third" />
            <label class="form-label" for="email-address-field">Email address</label>
        </div>

        <!-- 2 column grid layout with text inputs for the first and last names -->
        <div class="row mb-4">
            <div class="col">
                <div class="form-outline">
                    <input required name="firstName" type="text" id="first-name-field" class="form-control bg-third" />
                    <label class="form-label" for="first-name-field">First name</label>
                </div>
            </div>
            <div class="col">
                <div class="form-outline">
                    <input required name="lastName" type="text" id="last-name-field" class="form-control bg-third" />
                    <label class="form-label" for="last-name-field">Last name</label>
                </div>
            </div>
        </div>

        <!-- Password and confirmation -->
        <div class="row mb-4">
            <div class="col">
                <div class="form-outline">
                    <input required name="password" type="password" id="password-field" class="form-control bg-third" />
                    <label class="form-label" for="password-field">Password</label>
                </div>
            </div>
            <div class="col">
                <div class="form-outline">
                    <input required name="passwordRepeat" type="password" id="password-repeat-field" class="form-control bg-third" />
                    <label class="form-label" for="password-repeat-field">Confirmation</label>
                </div>
            </div>
        </div>

        <!-- Submit button -->
        <input name="${(_csrf.parameterName)!}" value="${(_csrf.token)!}" type="hidden" />
        <button type="submit" class="btn btn-primary btn-block mb-4 bg-sub">Sign up</button>
    </form>
</div>
<#include "../includes/footer.ftl">