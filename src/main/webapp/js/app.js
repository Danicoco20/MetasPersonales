document.addEventListener("DOMContentLoaded", () => {
    const registro = document.getElementById("registerForm");
    if (registro) {
        const password = document.getElementById("registroPassword");
        const confirm = document.getElementById("confirmPassword");
        const soloLetras = /^[\p{L}\p{M}]+(?:[ '\-][\p{L}\p{M}]+)*$/u;
        ["nombres", "apellidos"].forEach((id) => {
            const campo = document.getElementById(id);
            campo.addEventListener("input", () => {
                campo.setCustomValidity(campo.value === "" || soloLetras.test(campo.value)
                    ? "" : "Este campo solo admite letras, espacios, apóstrofos y guiones.");
            });
        });
        registro.addEventListener("submit", (event) => {
            if (password.value !== confirm.value) {
                event.preventDefault();
                confirm.setCustomValidity("Las contraseñas no coinciden.");
                confirm.reportValidity();
            } else {
                confirm.setCustomValidity("");
            }
        });
        confirm.addEventListener("input", () => confirm.setCustomValidity(""));
    }

    const resetForm = document.getElementById("resetForm");
    if (resetForm) {
        const password = document.getElementById("newPassword");
        const confirm = document.getElementById("confirmNewPassword");
        resetForm.addEventListener("submit", (event) => {
            if (password.value !== confirm.value) {
                event.preventDefault();
                confirm.setCustomValidity("Las contraseñas no coinciden.");
                confirm.reportValidity();
            } else confirm.setCustomValidity("");
        });
        confirm.addEventListener("input", () => confirm.setCustomValidity(""));
    }
});

function onCaptchaVerified() {
    const button = document.getElementById("loginButton");
    if (button) button.disabled = false;
}
function onCaptchaExpired() {
    const button = document.getElementById("loginButton");
    if (button) button.disabled = true;
}
function handleGoogleCredential(response) {
    const input = document.getElementById("googleCredential");
    const form = document.getElementById("googleLoginForm");
    if (input && form && response && response.credential) {
        input.value = response.credential;
        form.submit();
    }
}
