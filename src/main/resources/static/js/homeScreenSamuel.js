// Author Samuel, Stijn, Mink

import {getToken} from "./tokenUtils.js";

//use relative URL
let url = new URL(window.location.href)


// fetch customer information
//todo rate zonder token op te halen?

Promise.all([
    fetch(`${url.origin}/portfolio/assets`, {
        method: 'GET',
        headers: {
            'Authorization': getToken(),
            'Content-Type': 'application/json',
        }
    }),
    fetch(`${url.origin}/assets`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        }
    })
]).then(responses => {
        if (responses[0].status === 401){
            alert("Token niet meer geldig, u moet opnieuw inloggen")
        } else if (responses[0].status === 200 && responses[1].status === 200) {
            return Promise.all(responses.map(function (response) {
                return response.json();
            }));
        } else {
            throw new Error("something is wrong" + response.status)
        }
    })
    .then(data => {
            fillHeaders(data[0])
            fillTable(data)
        }
    )
    .catch((err) => {
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

