// Author: Petra Coenen

"use strict";

document.addEventListener('DOMContentLoaded', () => {
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const submitButton = document.getElementById('submit-button');

    // Fetch the form to apply custom Bootstrap validation styles
    let form = document.querySelectorAll('.needs-validation')
    // Get form and prevent submission if invalid
    Array.prototype.slice.call(form)
        .forEach(function (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault()
                    event.stopPropagation()
                } else {
                    event.preventDefault();
                    sendLoginRequest(emailInput, passwordInput);
                }
                form.classList.add('was-validated')
            }, false)
        })
})

const sendLoginRequest = async (emailInput, passwordInput) => {
    const loginObject = {"email": emailInput.value, "password": passwordInput.value };
    const config = {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(loginObject)
    }
    try {
        const response = await fetch(`http://localhost:8080/users/authenticate`, config);
        const result = await response.json();
        console.log(result);
    } catch (e) {
        console.log(e);
        // Empty the form fields
        document.getElementById('email').value = null;
        document.getElementById('password').value = null;
        // Show error message to user
        document.getElementById('invalid-combination').style.visibility = 'visible';
    }
}