// Author Samuel, Stijn, Mink
'use strict'

import {getToken, removeToken} from "./tokenUtils.js";

//use relative URL
const url = new URL(window.location.href)

const logoutButton = document.getElementById("logout-btn" )
logoutButton.addEventListener("click" , logout)

// fetch customer information
//todo rate zonder token op te halen?

Promise.alllh((err) => {
        console.log(err);
    });

function fillHeaders(json) {
    document.getElementById("welkom").innerHTML = json.firstName
    document.getElementById("iban").innerHTML = json.bankAccount.iban
    document.getElementById("saldo").innerHTML = `&#8364 ${json.bankAccount.balanceEur.toFixed(2)}`
}

function fillTable(data) {

    const table = document.getElementById("tableBody")

    for (let asset in data[0].wallet) {

        const amount = data[0].wallet[asset];
        const rate = data[1].find(e => e.assetName === asset).rate;
        const value = amount * rate;

        const rowNode = document.createElement("tr");
        
        const cellNode1 = document.createElement("td");
        cellNode1.innerHTML = asset;
        const cellNode2 = document.createElement("td")
        cellNode2.innerHTML = amount;
        const cellNode3 = document.createElement("td")
        cellNode3.innerHTML = `&#8364 ${rate.toFixed(2)}`;
        const cellNode4 = document.createElement("td")
        cellNode4.innerHTML = `&#8364 ${value.toFixed(2)}`;

        rowNode.appendChild(cellNode1)
        rowNode.appendChild(cellNode2)
        rowNode.appendChild(cellNode3)
        rowNode.appendChild(cellNode4)

        table.appendChild(rowNode)

    }

}

function logout() {
    console.log("logout");
    removeToken();
    window.location = "../index.html"
}