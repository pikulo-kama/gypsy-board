<#include "../includes/header.ftl">
<div class="d-flex flex-column mt-5 align-items-center text-center">
    <p class="display-4 text-uppercase">Are you sure you want to log out?</p>
    <form method="post">

        <!-- Submit button -->
        <input name="${(_csrf.parameterName)!}" value="${(_csrf.token)!}" type="hidden" />
        <button type="submit" class="btn btn-primary btn-lg btn-block mb-4 bg-sub" style="width: 100vw">Log Out</button>
    </form>
</div>

<#include "../includes/footer.ftl">