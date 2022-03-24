// Author: Petra Coenen

"use strict";

let url = new URL(window.location.href);

const form = document.querySelector('form');
const inputFields = document.querySelectorAll('input');
const invalidCredWarning = document.getElementById('invalid-combination');
const emailInput = document.getElementById('email');
const passwordInput = document.getElementById('password');

document.addEventListener('DOMContentLoaded', () => {
    // Prevent submission of form if invalid
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
    // Hide invalid credentials warning when user starts entering new data
    inputFields.forEach((field) => {
        field.addEventListener('input', hideInvalidCredWarning);
    })
})

const login = async (emailInput, passwordInput) => {
    const loginObject = {"email": emailInput.value, "password": passwordInput.value };
    const config = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(loginObject)
    }
    try {
        const response = await fetch(`${url.origin}/users/authenticate`, config);
        const result = response.json()
            .then(result => {
                if (response.status === 200) {
                    localStorage.setItem("access-token", result);
                    // console.log(`localStorage set with token value: ${result}`)
                    window.location = "../html/profile.html";
                } else if (response.status === 401) {
                    // Empty the form fields
                    document.getElementById('email').value = null;
                    document.getElementById('password').value = null;
                    // Show error message to user
                    invalidCredWarning.style.visibility = "visible";
                }
            })
    } catch (e) {
        console.log(e);
    }
}

const hideInvalidCredWarning = () => {
    if (invalidCredWarning.style.visibility === "visible") {
        invalidCredWarning.style.visibility = "hidden";
    }
}