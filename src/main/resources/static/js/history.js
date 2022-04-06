// @author Ercan Ersaru

'use strict'

import {includeHTML, addLogout} from "./includeHTML.js";
import {getToken, removeToken} from "./tokenUtils.js";

// logout function
document.addEventListener('DOMContentLoaded', () => {
    includeHTML()
    addLogout()
})

const url = 'http://localhost:8080/history';

// fetch(url)
//     .then(res => res.json())
//     .then(data => {
//         // code to handle the response
//         data.map((transaction)=>{
//             let tr = createNode('tr'),
//                 th = createNode('th'),
//                 td = createNode('td');
//             th.innerText = transaction.transactienummer;
//             td.innerText = transaction.transactiedatum;
//             td.innerText = transaction.gever;
//             td.innerText = transaction.ontvanger;
//             td.innerText = transaction.assetnaam;
//             td.innerText = transaction.hoeveelheid;
//             td.innerText = transaction.bedrag;
//             td.innerText = transaction.transactiekost;
//             appendNode(tr, th);
//             appendNode(tr, td);
//             appendNode(tr, td);
//             appendNode(tr, td);
//             appendNode(tr, td);
//             appendNode(tr, td);
//             appendNode(tr, td);
//             appendNode(tr, td);
//         })
//     }).catch(err => {
//     console.error('Error: ', err);
// });

// create an element
const createNode = (elem) => {
    return document.createElement(elem);
};

// append an element to parent
const appendNode = (parent, elem) => {
    parent.appendChild(elem);
}

function retrieveTransactions(transactions) {
    for (let transaction in transactions) {
        // console.log(transactions[transaction].idTransaction);
        const transactienummer = transactions[transaction].idTransaction;
        const transactiedatum = transactions[transaction].transactionTime;
        const gever = transactions[transaction].assetGiver.firstName + " " + transactions[transaction].assetGiver.lastName;
        const ontvanger = transactions[transaction].assetRecipient.firstName + " " + transactions[transaction].assetRecipient.lastName;
        const assetnaam = transactions[transaction].asset.assetName;
        const hoeveelheid = transactions[transaction].assetAmount;
        const bedrag = transactions[transaction].eurAmount;
        const transactiekost = transactions[transaction].eurFee;

        const rowElement = document.createElement("tr");
        const cellElement1 = document.createElement("th");
        cellElement1.innerHTML = transactienummer;
        const cellElement2 = document.createElement("td");
        cellElement2.innerHTML = transactiedatum;
        const cellElement3 = document.createElement("td");
        cellElement3.innerHTML = gever;
        const cellElement4 = document.createElement("td");
        cellElement4.innerHTML = ontvanger;
        const cellElement5 = document.createElement("td");
        cellElement5.innerHTML = assetnaam;
        const cellElement6 = document.createElement("td");
        cellElement6.innerHTML = hoeveelheid;
        const cellElement7 = document.createElement("td");
        cellElement7.innerHTML = bedrag;
        const cellElement8 = document.createElement("td");
        cellElement8.innerHTML = transactiekost;

        rowElement.appendChild(cellElement1)
        rowElement.appendChild(cellElement2)
        rowElement.appendChild(cellElement3)
        rowElement.appendChild(cellElement4)
        rowElement.appendChild(cellElement5)
        rowElement.appendChild(cellElement6)
        rowElement.appendChild(cellElement7)
        rowElement.appendChild(cellElement8)

        const transactionsTable = document.querySelector("#transactionContent")
        transactionsTable.appendChild(rowElement);

    }
}

Promise.resolve(
    await fetch(url, {
        method: 'GET',
        headers: {'Content-Type': 'application/json', 'Authorization': getToken()}
    })
)
    .then(r => r.json())
    .then(transactions => {
        // console.log(transactions);
        retrieveTransactions(transactions)
    })
    .catch(error => console.error("Transactions cannot be loaded!", error))
