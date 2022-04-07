// @author Ercan Ersaru

'use strict'

import {includeHTML, addLogout} from "./includeHTML.js";
import {getToken} from "./tokenUtils.js";

// logout function
document.addEventListener('DOMContentLoaded', () => {
    includeHTML()
    addLogout()
})

// create an element
const createNode = (elem) => {
    return document.createElement(elem);
};

// append an element to parent
const appendNode = (parent, child) => {
    parent.appendChild(child);
}

// fill content of element
const fillContent = (elem, content) => {
    elem.innerText = content;
}

// change date time format to Dutch local format
const getDutchDateFormat = (dateString) => {
    const date = new Date(dateString);
    const options = {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: 'numeric',
        minute: 'numeric',
        second: 'numeric'
    };
    return date.toLocaleDateString('nl-NL', options);
}

// retrieve transactions data from database
function retrieveTransactions(transactions) {
    const transactionsTable = document.querySelector("#transactionContent")
    for (let transaction in transactions) {
        const transactiedatum = transactions[transaction].transactionTime;
        const koper = transactions[transaction].assetRecipient
        const koperVolledigeNaam = koper.firstName + " " + koper.namePrefix + " " + koper.lastName;
        const verkoper = transactions[transaction].assetGiver;
        const verkoperVolledigeNaam = verkoper.firstName + " " + verkoper.namePrefix + " " + verkoper.lastName;
        const assetnaam = transactions[transaction].asset.assetName;
        const hoeveelheid = transactions[transaction].assetAmount;
        const bedrag = `€ ${transactions[transaction].eurAmount}`;
        const transactiekost = `€ ${transactions[transaction].eurFee}`;

        const rowElement = createNode("tr");
        const cellElement2 = createNode("td");
        fillContent(cellElement2, getDutchDateFormat(transactiedatum));

        const cellElement3 = createNode("td"); // verkoper
        fillContent(cellElement3, verkoperVolledigeNaam);
        const cellElement4 = createNode("td"); // koper
        fillContent(cellElement4, koperVolledigeNaam);
        const cellElement5 = createNode("td");
        fillContent(cellElement5, assetnaam);
        const cellElement6 = createNode("td");
        fillContent(cellElement6, hoeveelheid);
        const cellElement7 = createNode("td");
        fillContent(cellElement7, bedrag);
        const cellElement8 = createNode("td");
        fillContent(cellElement8, transactiekost);

        appendNode(rowElement, cellElement2)
        appendNode(rowElement, cellElement3)
        appendNode(rowElement, cellElement4)
        appendNode(rowElement, cellElement5)
        appendNode(rowElement, cellElement6)
        appendNode(rowElement, cellElement7)
        appendNode(rowElement, cellElement8)
        appendNode(transactionsTable, rowElement);
    }
}

const url = new URL(window.location.href)

Promise.resolve(
    await fetch(`${url.origin}/history`, {
        method: 'GET',
        headers: {'Content-Type': 'application/json', 'Authorization': getToken()}
    })
)
    .then(response => response.json())
    .then(transactions => {
        retrieveTransactions(transactions)
    })
    .catch(error => console.error("Something went wrong. Transactions cannot be loaded!", error));
