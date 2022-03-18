// Author: Petra Coenen

"use strict";

document.addEventListener('DOMContentLoaded', () => {
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');

    // Fetch the form to apply custom Bootstrap validation styles
    let form = document.querySelectorAll('.needs-validation');
    // Get form and prevent submission if invalid
    Array.prototype.slice.call(form)
        .forEach(function (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                } else {
                    event.preventDefault();
                    login(emailInput, passwordInput);
                }
                form.classList.add('was-validated');
            }, false);
        })
})

const login = async (emailInput, passwordInput) => {
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
        const result = response.json()
            .then(result => {
                if (response.status === 200) {
                    localStorage.setItem("access-token", result);
                    // console.log(`localStorage set with token value: ${result}`)
                    window.location = "../html/homeScreenSamuel.html";
                } else if (response.status === 401) {
                    // Empty the form fields
                    document.getElementById('email').value = null;
                    document.getElementById('password').value = null;
                    // Show error message to user
                    document.getElementById('invalid-combination').style.visibility = 'visible';
                }
            })
    } catch (e) {
        console.log(e);
    }
}