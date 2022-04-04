/*
Author Mink Tielenius Kruijthoff
Script for trade page Cryptomero
 */

'use strict'

import {includeHTML, addLogout} from "./includeHTML.js";
import {getToken} from "./tokenUtils.js";

const url = new URL(window.location.href)

document.addEventListener('DOMContentLoaded', () => {
    includeHTML()
    addLogout()
})

Promise.resolve(
    fetch('http://localhost:8080/rates/latest', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    })
)
.then(response => response.json())
.then(data => {
    console.log(data);
    fillTable(data)
})
.catch((error) => {
    console.error('jammer joh', error);
})

function fillTable(data) {

    const table = document.getElementById("tableBody")

    for (let asset in data) {

        const assetName = data[asset].asset.assetName
        const timepoint = data[asset].timepoint
        const rate = data[asset].rate

        const rowNode = document.createElement("tr");

        const cellNode1 = document.createElement("th")
        cellNode1.innerHTML = assetName;
        const cellNode2 = document.createElement("td")
        cellNode2.innerHTML = timepoint;
        const cellNode3 = document.createElement("td")
        cellNode3.innerHTML = rate;

        rowNode.appendChild(cellNode1)
        rowNode.appendChild(cellNode2)
        rowNode.appendChild(cellNode3)

        table.appendChild(rowNode)
    }
}

