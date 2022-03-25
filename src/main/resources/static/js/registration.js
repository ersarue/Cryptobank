// Author: Petra Coenen

"use strict";

// TODO: Refine validation, clean up and refactor code!

const url = new URL(window.location.href);

const form = document.querySelector('form');
const inputFieldsArray = [...document.querySelectorAll('input')];
const firstNameInput = document.getElementById('firstName');
const namePrefixInput = document.getElementById('namePrefix');
const lastNameInput = document.getElementById('lastName');
const nameInputs = [...document.querySelectorAll('#firstName, #namePrefix, #lastName')];
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
    form.addEventListener('submit',(e) => {
        trimInputFields();
        // Prevent submission of form if invalid
        if (!form.checkValidity()) {
                e.preventDefault();
                e.stopPropagation();
            } else {
                e.preventDefault();
                register(); // Registration of new customer
            }
            form.classList.add('was-validated');
    }, false);
});

const trimInputFields = () => { // TODO: Will become obsolete (remove at later stage)
    inputFieldsArray.forEach((field) => field.value = field.value.trim());
}

inputFieldsArray.forEach((field) => {
    field.addEventListener('input', () =>
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

const processBackendErrResponse = (response) => { // TODO: catch all of the possible errors & refactor
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
    if (response.includes('This password has been seen')) {
        form.classList.remove('was-validated');
        passwordInput.classList.add('is-invalid');
        passwordValidation.innerHTML = 'Wachtwoord te zwak, is bekend bij enge criminelen';
    }
}

// Trim whitespaces of first name, name prefix and last name fields immediately after quitting field

nameInputs.forEach((field) => {
    field.addEventListener('blur', () => {
        field.value = field.value.trim()
    })
})

// Check validity of first name, name prefix and last name fields immediately after filling out field

nameInputs.forEach((field) => {
    field.addEventListener('blur', () => {
        if (!field.checkValidity()) {
            field.classList.add('is-invalid')
        }
    })
})

nameInputs.forEach((field) => {
    field.addEventListener('focus', () => {
        field.classList.remove('is-invalid');
    })
})

// Check validity of dob immediately after filling out field

dobInput.addEventListener('blur', (e) => {
    checkDob();
    if (!e.target.checkValidity()) {
        e.target.classList.add('is-invalid')
    }
});

dobInput.addEventListener('focus', (e) => {
    e.target.classList.remove('is-invalid');
});

const checkDob = () => {
    if (!dobInput.checkValidity()) {
        dobInput.classList.add('is-invalid');
        if (!(dobInput.value.trim().length === 0)) {
            dobValidation.innerHTML = 'Vul een geldige geboortedatum in'; // Show explicit error message only when field contains data
        }
    } else {
        const age = calculateAge();
        if(age < 18) {
        dobInput.classList.add('is-invalid');
        dobValidation.innerHTML = "Je moet min. 18 jaar zijn om klant te worden bij Cryptomero";
        }
    }
}

const calculateAge = () => {
    // Author function: Marcel
    const givenDate = new Date(dobInput.value);
    const epochSeconds = Date.now() - givenDate.getTime();
    const newDate = new Date(epochSeconds);
    const year = newDate.getUTCFullYear();
    return Math.abs(year - 1970);
}

// Check validity of bsn immediately after filling out field
// TODO: Find way to avoid 'clash' with form validity checker

bsnInput.addEventListener('blur', (e) => {
    checkBsn();
    if (!(e.target.value.trim().length === 0)) { // Show explicit error message only when field contains data
        bsnValidation.innerHTML = 'Vul een geldig bsn in';
    }
    e.target.value = e.target.value.trim();
});

bsnInput.addEventListener('focus', (e) => {
    e.target.classList.remove('is-invalid');
    bsnValidation.innerHTML = null;
});

const checkBsn = () => {
    // Author function: Marcel
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
    if (sum % DIVISOR !== 0 || !bsnInput.checkValidity()) { // Execute '11-proef' validity check & checks indicated in html
        bsnInput.classList.add('is-invalid');
    }
}

// Check validity of postal code format immediately after filling out field (and complete the address if possible)

postalCodeInput.addEventListener('blur', (e) => {
    e.target.value = e.target.value.trim();
    if (!e.target.checkValidity()) {
        e.target.classList.add('is-invalid');
        if (!(e.target.value.length === 0)) { // Show explicit error message only when field contains data
            postalCodeValidation.innerHTML = 'Vul een geldige postcode in';
        }
    } else {
        completeAddressFields();
    }
});

postalCodeInput.addEventListener('focus', (e) => {
    e.target.classList.remove('is-invalid');
    streetNameInput.value = '';
    cityInput.value = '';
});

// Check validity of house number format immediately after filling out field (and complete the address if possible)

houseNoInput.addEventListener('blur', (e) => {
    e.target.value = e.target.value.trim();
    if (!e.target.checkValidity()) {
        e.target.classList.add('is-invalid');
        if (e.target.value.length !== 0) { // Show explicit error message only when field contains data
            houseNoValidation.innerHTML = 'Vul een nummer in';
        }
    } else {
        completeAddressFields();
    }
});

houseNoInput.addEventListener('focus', (e) => {
    e.target.classList.remove('is-invalid');
    streetNameInput.value = '';
    cityInput.value = '';
});

// Check validity of telephone format immediately after filling out field

telInput.addEventListener('blur', (e) => {
    if (!e.target.checkValidity()) {
        e.target.classList.add('is-invalid');
        if (e.target.value.trim().length !== 0) {
            telValidation.innerHTML = 'Vul een geldig telefoonnummer in'; // Show explicit error message only when field contains data
        }
    }
    e.target.value = e.target.value.trim();
});

telInput.addEventListener('focus', (e) => {
    e.target.classList.remove('is-invalid');
});

// Check validity of email immediately after filling out field

emailInput.addEventListener('blur', (e) => {
    if (!e.target.checkValidity()) {
        e.target.classList.add('is-invalid');
        if (e.target.value.trim().length !== 0) {
            emailValidation.innerHTML = 'Vul een geldig emailadres in'; // Show explicit error message only when field contains data
        }
    }
    e.target.value = e.target.value.trim();
});

emailInput.addEventListener('focus', (e) => {
    e.target.classList.remove('is-invalid');
});

// Check validity of password immediately after filling out field

passwordInput.addEventListener('blur', (e) => {
    checkRepPassword();
    if (!e.target.checkValidity()) {
        e.target.classList.add('is-invalid');
        if (e.target.value.trim().length !== 0) { // Show explicit error message only when field contains data
            passwordValidation.innerHTML = 'Wachtwoord moet minimaal 8 tekens lang zijn';
        }
    }
    e.target.value = e.target.value.trim();
});

passwordInput.addEventListener('focus', (e) => {
    e.target.classList.remove('is-invalid');
});

const checkRepPassword = () => {
    // Regex patterns match those of the check done in the backend
    const regexPatChar = /(.)\1\1\1/g;
    const regexGroup1 = /(.{4,7})\1/g;
    const regexGroup2 = /(.{2,3})\1\1/g;
    const regexPatNum = /\d{5}/g;
    if (regexPatChar.test(passwordInput.value) || regexGroup1.test(passwordInput.value) || regexGroup2.test(passwordInput.value)
        || regexPatNum.test(passwordInput.value)) {
        passwordInput.classList.add('is-invalid');
        passwordValidation.innerHTML = 'Wachtwoord mag niet teveel herhaling bevatten';
    }
}

// Automatic address completion

const completeAddressFields = async () => {
    if (postalCodeInput.value !== '' && houseNoInput.value !== '') {
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