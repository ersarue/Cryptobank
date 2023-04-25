// @author Ercan Ersaru

'use strict'

import {includeHTML, addLogout, logout} from "./includeHTML.js";
import {getToken} from "./tokenUtils.js";

// logout function
document.addEventListener('DOMContentLoaded', () => {
    includeHTML()
    addLogout()
})

const url = new URL(window.location.href);

// create and append an element to parent and fill content of the element
const createAndAppendElement = (elem, parent, content) => {
    elem = document.createElement(elem);
    elem.innerText = content;
    return parent.appendChild(elem);
};

// change date time format to Dutch local format
const getDutchDateFormat = (dateString) => {
    const date = new Date(dateString);
    const options = {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric',
        second: 'numeric'
    };
    return date.toLocaleDateString('nl-NL', options);
}

// retrieve transactions from database
function retrieveTransactions(transactions) {
    transactions.sort((a, b) => new Date(a.transactionTime) - new Date(b.transactionTime));
    const transactionsTable = document.querySelector("#transactionContent")
    for (let transaction in transactions) { // loop through transactions, create table element and its content
        const transactiedatum = transactions[transaction].transactionTime;
        const verkoper = transactions[transaction].assetGiver;
        const verkoperVolledigeNaam = verkoper.firstName + " " + verkoper.namePrefix + " " + verkoper.lastName;
        const koper = transactions[transaction].assetRecipient
        const koperVolledigeNaam = koper.firstName + " " + koper.namePrefix + " " + koper.lastName;
        const assetnaam = transactions[transaction].asset.assetName;
        const hoeveelheid = transactions[transaction].assetAmount;
        const bedrag = `€ ${transactions[transaction].eurAmount}`;
        const transactiekost = `€ ${transactions[transaction].eurFee}`;

        const rowElement = document.createElement('tr');
        createAndAppendElement('td', rowElement, getDutchDateFormat(transactiedatum));
        createAndAppendElement('td', rowElement, verkoperVolledigeNaam);
        createAndAppendElement('td', rowElement, koperVolledigeNaam);
        createAndAppendElement('td', rowElement, assetnaam);
        createAndAppendElement('td', rowElement, hoeveelheid);
        createAndAppendElement('td', rowElement, bedrag);
        createAndAppendElement('td', rowElement, transactiekost);
        transactionsTable.appendChild(rowElement);
    }
}

Promise.resolve(
    await fetch(`${url.origin}/history`, {
        method: 'GET',
        headers: {'Content-Type': 'application/json', 'Authorization': getToken()}
    })
)
    .then(response => {
            if (response.status === 401) {
                alert("Ongeldige sessie. U moet (opnieuw) inloggen.")
                logout()
            } else if (response.status === 200) {
                return response.json();
            } else {
                throw new Error("Er is iets fout gegaan bij het ophalen van historie" + response.status)
            }
        }
    )
    .then(transactions => {
        retrieveTransactions(transactions)
    })
    .catch(error => console.error("Er is iets fout gegaan. Historie kan niet opgehaald worden!", error));
