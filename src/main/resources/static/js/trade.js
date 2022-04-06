/*
Author Mink Tielenius Kruijthoff
Script for trade page Cryptomero
 */

'use strict'

import {includeHTML, includeHTMLgraph, includeHTMLTradeModal, addLogout, logout} from "./includeHTML.js";
import {getToken} from "./tokenUtils.js";
import {addGraphs, maakGrafiek} from "./grafiekenGenerator.js";
import {addModalDropDown, addModalSubmitButton} from "./doTrade.js";

const url = new URL(window.location.href)

document.addEventListener('DOMContentLoaded', () => {
    includeHTML()
    addLogout()
    includeHTMLTradeModal()
    addModalDropDown()
    addModalSubmitButton()
})

Promise.resolve(
    fetch('http://localhost:8080/rates/latest/overview', {
        method: 'GET',
        headers: {
            'Authorization': getToken(),
            'Content-Type': 'application/json',
        },
    })
)
.then(response => {
    if (response.status === 401) {
        alert("U bent uitgelogd")
        logout()
    } else if (response.status === 200) {
        return response.json();
    } else {
        throw new Error("jammer joh" + response.status)
    }
})
.then(data => {
    fillTable(data)
    includeHTMLgraph()
    addGraphs()
})
.catch((error) => {
    console.error('something is wrong', error);
})

function fillTable(data) {

    const table = document.getElementById("tableBody")

    for (let asset in data) {

        const assetName = data[asset].asset.assetName
        // const timepoint = getTimepoint(data[asset].timepoint)
        const rate = data[asset].rate

        const rowNode = document.createElement("tr");
        rowNode.setAttribute("id",assetName);

        const cellNode1 = document.createElement("td")
        cellNode1.innerHTML = assetName;
        const cellNode2 = document.createElement("td")
        cellNode2.innerHTML = rate;
        // const cellNode3 = document.createElement("td")
        // cellNode3.innerHTML = timepoint;

        rowNode.appendChild(cellNode1)
        rowNode.appendChild(cellNode2)
        // rowNode.appendChild(cellNode3)

        table.appendChild(rowNode)
        addClickRow(assetName)
    }
}

function addClickRow(id) {
    const startGraph = document.getElementById(id)
    startGraph.onclick = () => {
        maakGrafiek(id, 7, "day")
    }
}

// function getTimepoint(timepoint) {
//     const timepointArray = timepoint.split("T")
//     const dateArray = timepointArray[0].split("-")
//     const date = dateArray[2] + "/" + dateArray[1] + "/" + dateArray[0]
//     const time = "(" + timepointArray[1] + ")"
//     return date + " " + time
// }

