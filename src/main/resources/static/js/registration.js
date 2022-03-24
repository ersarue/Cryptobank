// Author: Petra Coenen

"use strict";

// TODO: Refine validation, clean up code

const url = new URL(window.location.href);

// Selecting html-elements
const form = document.querySelector('form');
const inputFieldsArray = [...document.querySelectorAll('input')];
const firstNameInput = document.getElementById('firstName');
const namePrefixInput = document.getElementById('namePrefix');
const lastNameInput = document.getElementById('lastName');
const firstLastNameInputs = [...document.querySelectorAll('#firstName, #lastName')];
const dobInput = document.getElementById('dob');
const bsnInput = document.getElementById('bsn');
const houseNoInput = document.getElementById('houseNo');
const houseAddInput = document.getElementById('houseAdd');
const postalCodeInput = document.getElementById('postalCode');
const streetNameInput = document.getElementById('streetName');
const cityInput = document.getElementById('city');
const telInput = document.getElementById('telephone');
const emailInput = document.getElementById('email');
const passwordInput = document.getElementById('password');

const dobValidation = document.querySelector('.dob-validation');
const bsnValidation = document.querySelector('.bsn-validation');
const postalCodeValidation = document.querySelector('.postalCode-validation');
const houseNoValidation = document.querySelector('.houseNo-validation');
const telValidation = document.querySelector('.telephone-validation');
const emailValidation = document.querySelector('.email-validation');
const passwordValidation = document.querySelector('.password-validation');

const modalButton = document.getElementById('modal-button');
const modal = new bootstrap.Modal(document.getElementById('success-modal'));

document.addEventListener('DOMContentLoaded', () => {
    // Prevent submission of form if invalid (form.checkvalidity() checks the validations indicated in html only)
    form.addEventListener('submit', function (event) {
        trimInputFields(); // Removes whitespace from both ends of the input values
        if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            } else {
                event.preventDefault();
                register(); // Registration of new customer
            }
            form.classList.add('was-validated');
    }, false);
});

const trimInputFields = () => {
    inputFieldsArray.forEach((field) => field.value = field.value.trim());
}

inputFieldsArray.forEach((field) => {
    field.addEventListener('focus', () =>
        form.classList.remove('was.validated')); // Undo final form validation check if user modifies an input
});

const createCustomerObject = () => {
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
    console.log(createCustomerObject()); // TODO: Remove in final version
    const config = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(createCustomerObject())
    }
    try {
        const response = await fetch(`${url.origin}/users/register`, config);
        response.text().then(result => {
                if (response.status === 201) {
                    modal.show();
                } else if (response.status === 400) {
                    processBackendErrResponse(result);
                }
            })
    } catch (e) {
        console.log(e);
        // TODO: Refine error catching
    }
}

modalButton.addEventListener('click', () => {
    window.location = "../index.html"
});

const processBackendErrResponse = (response) => { // TODO: catch all of the possible errors
    if (response.includes('E-mail already in use')) {
        form.classList.remove('was-validated');
        emailInput.classList.add('is-invalid');
        emailValidation.innerHTML = 'Emailadres is al in gebruik';
    }
    if (response.includes('bsn already in use')) {
        form.classList.remove('was-validated');
        bsnInput.classList.add('is-invalid');
        bsnValidation.innerHTML = 'Bsn is al in gebruik';
    }
}

// Trim whitespaces of first and last name fields immediately after filling out field

firstLastNameInputs.forEach((field) => {
    field.addEventListener('blur', () => {
        field.value = field.value.trim()
    })
})

// Check validity of dob immediately after filling out field

dobInput.addEventListener('blur', (event) => {
    checkDob();
    if (!event.target.checkValidity()) {
        dobInput.classList.add('is-invalid')
    }
});

dobInput.addEventListener('focus', () => {
    dobInput.classList.remove('is-invalid');
});

const checkDob = () => { // TODO: Check for invalid dob-format
    const dateGiven = new Date(dobInput.value);
    const months = Date.now() - dateGiven.getTime();
    const newDate= new Date(months)
    const year = newDate.getUTCFullYear();
    const age = Math.abs(year - 1970);
    if (age < 18) {
        dobInput.classList.add('is-invalid');
        dobValidation.innerHTML="Je bent helaas te jong om klant te zijn bij Cryptomero";
    }
}

// Check validity of bsn immediately after filling out field

bsnInput.addEventListener('blur', () => {
    checkBsn();
    if (!(bsnInput.value.trim().length === 0)) { // Show explicit error message only when field has not been touched yet
        bsnValidation.innerHTML = 'Vul een geldig bsn in';
    }
});

bsnInput.addEventListener('focus', () => {
    bsnInput.classList.remove('is-invalid');
    bsnValidation.innerHTML = null;
});

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

// Check validity of postal code format immediately after filling out field (and complete the address if possible)

postalCodeInput.addEventListener('blur', () => {
    if (!postalCodeInput.checkValidity()) {
        postalCodeInput.classList.add('is-invalid');
        if (!(postalCodeInput.value.trim().length === 0)) { // Show explicit error message only when field is not empty
            postalCodeValidation.innerHTML = 'Vul een geldige postcode in';
        }
    } else {
        completeAddressFields();
    }
});

postalCodeInput.addEventListener('focus', () => {
    postalCodeInput.classList.remove('is-invalid');
    streetNameInput.value = '';
    cityInput.value = '';
});

// Check validity of house number format immediately after filling out field (and complete the address if possible)

houseNoInput.addEventListener('blur', () => {
    if (!houseNoInput.checkValidity()) {
        houseNoInput.classList.add('is-invalid');
        if (houseNoInput.value !== '') {
            houseNoValidation.innerHTML = 'Vul een nummer in';
        }
    } else {
        completeAddressFields();
    }
});

houseNoInput.addEventListener('focus', () => {
    houseNoInput.classList.remove('is-invalid');
    streetNameInput.value = '';
    cityInput.value = '';
});

// Check validity of telephone format immediately after filling out field

telInput.addEventListener('blur', () => {
    if (!telInput.checkValidity()) {
        telInput.classList.add('is-invalid');
        if (!(telInput.value.trim().length === 0)) {
            telValidation.innerHTML = 'Vul een geldig telefoonnummer in'; // // Show explicit error message only when field is not empty
        }
    }
});

telInput.addEventListener('focus', () => {
    telInput.classList.remove('is-invalid');
});

// Check validity of email immediately after filling out field

emailInput.addEventListener('blur', () => {
    if (!emailInput.checkValidity()) {
        emailInput.classList.add('is-invalid');
        if (!(emailInput.value.trim().length === 0)) {
            emailValidation.innerHTML = 'Vul een geldig emailadres in'; // // Show explicit error message only when field is not empty
        }
    }
});

emailInput.addEventListener('focus', () => {
    emailInput.classList.remove('is-invalid');
});

// Check validity of password immediately after filling out field

passwordInput.addEventListener('blur', () => {
    if (!passwordInput.checkValidity()) {
        passwordInput.classList.add('is-invalid');
        if (!(passwordInput.value.trim().length === 0)) { // Show explicit error message only when field is not empty
            passwordValidation.innerHTML = 'Wachtwoord moet minimaal 8 tekens lang zijn';
        }
    }
});

passwordInput.addEventListener('focus', () => {
    passwordInput.classList.remove('is-invalid');
});

// Automatic address completion

const completeAddressFields = async () => {
    if (postalCodeInput.value !== '' && houseNoInput.value !== '') { // Note: Undefined or null does not imply empty
        const trimmedPostalCode = postalCodeInput.value.trim();
        const postalCodeHouseNo = `postcode=${trimmedPostalCode}&number=${houseNoInput.value}`;
        const config = {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer 5cc336e3-c924-44d6-a44f-d8b9e5e0ddb9'
            }
        }

        try {
            const response = await fetch(`https://postcode.tech/api/v1/postcode?${postalCodeHouseNo}`, config);
            response.json().then(response => {
                console.log(response);
                if (response.street === undefined) { // TODO: Make more robust
                    postalCodeInput.classList.add('is-invalid');
                    houseNoInput.classList.add('is-invalid');
                    postalCodeValidation.innerHTML = "Vul een geldige huisnummer/postcodecombinatie in";
                } else {
                    streetNameInput.value = response.street;
                    cityInput.value = response.city;
                }
            });
        } catch (e) {
            console.log(e); // TODO: Refine error catching
        }
    }
}
