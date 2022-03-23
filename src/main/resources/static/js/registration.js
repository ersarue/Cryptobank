// Author: Petra Coenen
"use strict";

// TODO: Form validation not finished yet

const url = new URL(window.location.href);

const form = document.querySelector('form');
const inputFieldsArray = [...document.querySelectorAll('input')];
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
const modalButton = document.getElementById('modal-button');
const modal = new bootstrap.Modal(document.getElementById('success-modal'));

const bsnValidation = document.querySelector('.bsn-validation');
const passwordValidation = document.querySelector('.password-validation');
const addressValidation = document.querySelector('.address-validation');
const postalCodeValidation = document.querySelector('.postalCode-validation');
const houseNoValidation = document.querySelector('.houseNo-validation');
const dobValidation = document.querySelector('.dob-validation');
const emailValidation = document.querySelector('.email-validation');

document.addEventListener('DOMContentLoaded', () => {
    // Prevent submission of form if invalid per requirements indicated in html
    form.addEventListener('submit', function (event) {
        trimInputFields();
        //
        // const invalid = (field) => field.classList.contains('is-invalid');
        // if (inputFieldsArray.some(invalid)) {
        //     event.preventDefault();
        //     event.stopPropagation();
        //     console.log("HOI")
        if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            } else {
                event.preventDefault();
                register();
            }
            form.classList.add('was-validated');
    }, false);
});

const trimInputFields = () => {
    inputFieldsArray.forEach((field) => field.value = field.value.trim());
}

inputFieldsArray.forEach((field) => {
    field.addEventListener('focus', (event) =>
        form.classList.remove('was.validated'));
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
    console.log(createCustomerObject());
    const config = {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(createCustomerObject())
    }
    try {
        const response = await fetch(`http://localhost:8080/users/register`, config);
        response.text().then(result => {
                if (response.status === 201) {
                    modal.show();
                } else if (response.status === 400) {
                    processBackendErrResponse(result);
                }
            })
    } catch (e) {
        console.log(e);
    }
}

modalButton.addEventListener('click', () => {
    window.location = "../index.html"
})

const processBackendErrResponse = (response) => {
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

// Check validity of dob immediately after filling out field

dobInput.addEventListener('blur', (event) => {
    checkDob();
    if (!event.target.checkValidity()) {
        dobInput.classList.add('is-invalid')
    }
});

dobInput.addEventListener('focus', (event) => {
    dobInput.classList.remove('is-invalid');
});

// TODO: Change function

const checkDob = () => {
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

bsnInput.addEventListener('blur', (event) => {
    checkBsn();
    if (!(bsnInput.value.trim().length === 0)) { // Show explicit error message only when field has not been touched yet
        bsnValidation.innerHTML = 'Vul een geldig bsn in';
    }
    if (!event.target.checkValidity()) {
        bsnInput.classList.add('is-invalid')
    }
});

bsnInput.addEventListener('focus', (event) => {
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

postalCodeInput.addEventListener('blur', (event) => {
    if (!postalCodeInput.checkValidity()) {
        postalCodeInput.classList.add('is-invalid');
    } else {
        completeAddressFields();
    }
});

postalCodeInput.addEventListener('focus', (event) =>
{  postalCodeInput.classList.remove('is-invalid'); });

// Check validity of house number format immediately after filling out field (and complete the address if possible)

houseNoInput.addEventListener('blur', (event) => {
    if (!houseNoInput.checkValidity()) {
        houseNoInput.classList.add('is-invalid');
        houseNoValidation.innerHTML = '';
        if (houseNoInput !== '') {
            houseNoValidation.innerHTML = 'Vul een nummer in';
        }
    } else {
        completeAddressFields();
    }
});

houseNoInput.addEventListener('focus', (event) =>
{  houseNoInput.classList.remove('is-invalid'); });

// Check validity of telephone format immediately after filling out field

telInput.addEventListener('blur', (event) => {
    if (!telInput.checkValidity()) {
        telInput.classList.add('is-invalid');
    }
});

telInput.addEventListener('focus', (event) => {
    telInput.classList.remove('is-invalid');
});

// Check validity of email immediately after filling out field

emailInput.addEventListener('blur', (event) => {
    if (!emailInput.checkValidity()) {
        emailInput.classList.add('is-invalid');
    }
});

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
});

passwordInput.addEventListener('focus', (event) => {
    passwordInput.classList.remove('is-invalid');
});

// Automatic address completion

const completeAddressFields = async () => {
    if (postalCodeInput.value !== '' && houseNoInput.value !== '') { // Undefined or null does not imply empty here
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
                if (response.street === undefined) {
                    postalCodeInput.classList.add('is-invalid');
                    houseNoInput.classList.add('is-invalid');
                    postalCodeValidation.innerHTML = "Vul een geldige huisnummer/postcodecombinatie in";
                } else {
                    streetNameInput.value = response.street;
                    cityInput.value = response.city;
                }
            });
        } catch (e) {
            console.log(e);
        }
    }
}
