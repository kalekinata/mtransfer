function show_hide_password(target){
    var input = document.getElementById('password');
    if (input.getAttribute('type') == 'password') {
        target.classList.add('view');
        input.setAttribute('type', 'text');
    } else {
        target.classList.remove('view');
        input.setAttribute('type', 'password');
    }
    return false;
}
function show_hide_pass_confirm(target){
    var input = document.getElementById('password-confirm');
    if (input.getAttribute('type') == 'password') {
        target.classList.add('view');
        input.setAttribute('type', 'text');
    } else {
        target.classList.remove('view');
        input.setAttribute('type', 'password');
    }
    return false;
}

var passwordInput = document.getElementById('password');
var passwordConfirmInput = document.getElementById('password-confirm');
var passwordError = document.getElementById('password-error');

passwordConfirmInput.addEventListener('input', function() {
    if (passwordInput.value != passwordConfirmInput.value) {
        passwordError.textContent = 'Пароли не совпадают';
        passwordConfirmInput.style.borderColor = 'red';
    } else {
        passwordError.textContent = '';
        passwordConfirmInput.style.borderColor = '';
    }
});