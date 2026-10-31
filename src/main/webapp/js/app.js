/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/javascript.js to edit this template
 */


document.addEventListener("DOMContentLoaded", () => {

    const registro = document.getElementById("registerForm");

    if (registro) {
        const password = document.getElementById("registroPassword");
        const confirm = document.getElementById("confirmPassword");

        registro.addEventListener("submit", (event) => {

            if (password.value !== confirm.value) {
                event.preventDefault();

                confirm.setCustomValidity(
                    "Las contraseñas no coinciden."
                );

                confirm.reportValidity();
            } else {
                confirm.setCustomValidity("");
                // El formulario continúa y se envía al Servlet.
            }
        });

        confirm.addEventListener("input", () => {
            confirm.setCustomValidity("");
        });
    }
});

