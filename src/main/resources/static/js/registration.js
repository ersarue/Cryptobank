// Author: Petra Coenen
"use strict";

const url = new URL(window.location.href);

const form = document.querySelector('form');
const firstNameInput = document.getElementById('firstName');
const namePrefixInput = document.getElementById('namePrefix');
const lastNameInput = document.getElementById('lastName');
const dobInput = document.getElementById('dob');
const bsnInput = document.getElementById('bsn');
const telInput = document.getElementById('telephone');
const emailInput = document.getElementById('email');
const houseNoInput = document.getElementById('houseNo');
const houseAddInput = document.getElementById('houseAdd');
const postalCodeInput = document.getElementById('postalCode');
const streetNameInput = document.getElementById('streetName');
const cityInput = document.getElementById('city');
const passwordInput = document.getElementById('password');

const bsnValidation = document.querySelector('.bsn-validation');
const passwordValidation = document.querySelector('.password-validation');

document.addEventListener('DOMContentLoaded', () => {
    // Prevent submission of form if invalid
    form.addEventListener('submit', function (event) {
        if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        } else {
            event.preventDefault();
            register();
        }
        form.classList.add('was-validated');
    }, false);
})

const makeCustomerObject = () => {
    return {
        "email": emailInput.value,
        "password": passwordInput.value,
        "firstName": firstNameInput.value,
        "namePrefix": namePrefixInput.value,
        "lastName": lastNameInput.value,
        "dob": dobInput.value,
        "bsn": bsnInput.value,
        "telephone": telInput.value,
        "address": {
            "streetName": streetNameInput.value,
            "houseNo": houseNoInput.value,
            "houseAdd": houseAddInput.value,
            "postalCode": postalCodeInput.value,
            "city": cityInput.value
        }
    }
}

const register = async () => {
    console.log(makeCustomerObject());
    const config = {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(makeCustomerObject())
    }
    try {
        const response = await fetch(`http://localhost:8080/users/register`, config);
        response.text().then(result => {
                if (response.status === 200) {
                    window.location = "../html/login.html";
                } else if (response.status === 401) {
                    // Empty the form fields
                }
            })
    } catch (e) {
        console.log(e);
    }
}

const runCustomValidators = () => {
    checkBsn();
}

// TODO: Check validity of dob

// Check validity of bsn immediately after filling out field

bsnInput.addEventListener('blur', (event) => {
    checkBsn();
    if (!(bsnInput.value.length === 0)) { // Show explicit error message only when field has not been touched yet
        bsnValidation.innerHTML = 'Vul een geldig bsn in';
    }
});

bsnInput.addEventListener('focus', (event) => {  bsnInput.classList.remove('is-invalid'); });

const checkBsn = () => {
    const FACTORS = [9, 8, 7, 6, 5, 4, 3, 2, -1];
    const DIVISOR = 11;
    let bsn = bsnInput.value;
    if (bsn.length === 8){
        bsn = "0" + bsn; // Prepend 0 to ensure bsn consists of 9 digits
    }
    const bsnArray = Array.from(bsn);
    let sum = 0;
    for (let i = 0; i < bsnArray.length; i++) {
        const digit = parseInt(bsnArray[i]);
        sum += digit * FACTORS[i];
    }
    if (sum % DIVISOR !== 0) {
        bsnInput.classList.add('is-invalid');
    }
}

// Check validity of postal code format immediately after filling out field

postalCodeInput.addEventListener('blur', (event) => {
    if (!postalCodeInput.checkValidity()) {
        postalCodeInput.classList.add('is-invalid');
    }
})

postalCodeInput.addEventListener('focus', (event) =>
{  postalCodeInput.classList.remove('is-invalid'); });

// Check validity of house number format immediately after filling out field

houseNoInput.addEventListener('blur', (event) => {
    if (!houseNoInput.checkValidity()) {
        houseNoInput.classList.add('is-invalid');
    }
})

houseNoInput.addEventListener('focus', (event) =>
{  houseNoInput.classList.remove('is-invalid'); });

// Check validity of telephone format immediately after filling out field

telInput.addEventListener('blur', (event) => {
    if (!telInput.checkValidity()) {
        telInput.classList.add('is-invalid');
    }
})

telInput.addEventListener('focus', (event) =>
{  telInput.classList.remove('is-invalid'); });

// Check validity of email immediately after filling out field

emailInput.addEventListener('blur', (event) => {
    if (!emailInput.checkValidity()) {
        emailInput.classList.add('is-invalid');
    }
})

emailInput.addEventListener('focus', (event) =>
{  emailInput.classList.remove('is-invalid'); });

// Check validity of password immediately after filling out field

passwordInput.addEventListener('blur', (event) => {
    if (!passwordInput.checkValidity()) {
        passwordInput.classList.add('is-invalid');
        if (!(passwordInput.value.length === 0)) { // Show explicit error message only when field is not empty
            passwordValidation.innerHTML = 'Wachtwoord moet minimaal 8 tekens lang zijn';
        }
    }
})

passwordInput.addEventListener('focus', (event) =>
    {  passwordInput.classList.remove('is-invalid'); });

// TODO: automatic address completion