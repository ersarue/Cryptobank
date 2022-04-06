// Author: Samuel, Stijn, Mink, Petra
'use strict'

import { getToken, removeToken } from "./tokenUtils.js";
import { includeHTML, addLogout } from "./includeHTML.js";
import { includeNoOffersBox } from "./includeNoOffersBox.js";
import { includeNoAssetsBox } from "./includeNoAssetsBox.js";

const url = new URL(window.location.href)

window.addEventListener( "DOMContentLoaded",  async () => {
    // Include navbar and footer
    includeHTML();
    addLogout();
    // Load the marketplace offers from the db
    await getOffers();
});

//todo rate zonder token op te halen?

Promise.all([
    fetch(`${url.origin}/portfolio/assets`, {
        method: 'GET',
        headers: {
            'Authorization': getToken(),
            'Content-Type': 'application/json',
        }
    }),
    fetch(`${url.origin}/rates/latest`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        }
    })
]).then(responses => {
    if (responses[0].status === 401){
        alert("Sessie verlopen. U moet opnieuw inloggen.")
        logout()
    } else if (responses[0].status === 200 && responses[1].status === 200) {
        return Promise.all(responses.map(function (response) {
            return response.json();
        }));
    } else {
        throw new Error("something is wrong")
    }
})
    .then(data => {
            fillHeaders(data[0])
            displayAssetInfo(data);
        }
    )
    .catch((err) => {
        console.log(err);
    });

// Retrieves all of the user's marketplace orders from the db
const getOffers = async () => {
    try {
        const response = await  fetch(`${url.origin}/trade/getoffers`, {
            method: 'GET',
            headers: {
                'Authorization': getToken(),
                'Content-Type': 'application/json'
            }
        });
        const result = await response.json();
        if (response.status === 200) {
            displayOfferInfo(result);
        } else if (response.status === 401) {
            alert("Sessie verlopen. U moet opnieuw inloggen.");
            logout();
        } else {
            throw new Error("Er is iets fout gegaan bij het ophalen van de offerdata");
        }
    } catch (e) {
        console.log(e);
    }
}

function fillHeaders(json) {
    document.getElementById("welkom").innerHTML = json.firstName
    document.getElementById("iban").innerHTML = json.bankAccount.iban
    document.getElementById("saldo").innerHTML = `&#8364 ${json.bankAccount.balanceEur.toFixed(2)}`
}

// Displays box with 'trade' button when the user has no crypto assets
const displayAssetInfo = (data) => {
    Object.keys(data[0].wallet).length === 0
    ? includeNoAssetsBox()
    : fillAssetTable(data);
}

// Displays box with 'go to marketplace' button when the user has no offers
const displayOfferInfo = (data) => {
    data.length === 0
    ? includeNoOffersBox()
    : fillOfferTable(data);
}

function fillAssetTable(data) {
    const table = document.getElementById("asset-table-body")

    for (let asset in data[0].wallet) {

        const amount = data[0].wallet[asset];
        const rate = data[1].find(e => e.asset.assetName === asset).rate;
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

// Fills the offer table with the user's offers from the db
const fillOfferTable = (data) => {
    const table = document.getElementById("offer-table-body");

    for (let offer of data) {
        const rowNode = document.createElement("tr");
        const cellNode1 = document.createElement("td");
        cellNode1.innerHTML = toLocaleDate(offer.timestampOffer);
        cellNode1.classList.add("align-middle");
        const cellNode2 = document.createElement("td");
        cellNode2.innerHTML = determineOfferType(offer.amountOffer);
        cellNode2.classList.add("align-middle");
        const cellNode3 = document.createElement("td")
        cellNode3.innerHTML = offer.assetOffer.assetName;
        cellNode3.classList.add("align-middle");
        const cellNode4 = document.createElement("td")
        cellNode4.innerHTML = `${Math.abs(offer.amountOffer.toFixed(2))}`;
        cellNode4.classList.add("align-middle");
        const cellNode5 = document.createElement("td")
        cellNode5.innerHTML = `&#8364 ${offer.priceOffer.toFixed(2)}`;
        cellNode5.classList.add("align-middle");
        const cellNode6 = document.createElement("td");

        rowNode.appendChild(cellNode1);
        rowNode.appendChild(cellNode2);
        rowNode.appendChild(cellNode3);
        rowNode.appendChild(cellNode4);
        rowNode.appendChild(cellNode5);
        cellNode6.appendChild(createDeleteBtn(offer.idOffer));
        rowNode.appendChild(cellNode6);
        table.appendChild(rowNode);
    }
}

// Converts an ISO 8601 date string into local Dutch date format
const toLocaleDate = (dateString) => {
    const date = new Date(dateString);
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return date.toLocaleDateString('nl-NL', options);
}

// Determines whether a marketplace offer is to buy or to sell (in order to display this in the table)
const determineOfferType = (data) => {
    let offerType;
    Math.sign(data) === 1
    ? offerType = 'aangeboden'
    : offerType = 'gevraagd';
    return offerType;
}

// Creates a delete button for a row in the offer table
const createDeleteBtn = (idOffer) => {
    const button = document.createElement("button");
    button.classList.add("btn", "btn-primary");
    button.setAttribute('id','delete-btn');
    button.innerHTML = "verwijder";
    // const trashBinIcon = document.createElement("i");
    // trashBinIcon.classList.add("bi", "bi-trash3");
    // button.appendChild(trashBinIcon);
    addDeleteEventListener(button, idOffer);
    return button;
}

// Adds event listener to a 'verwijder' button in the offer table so that it deletes the corresponding offer
const addDeleteEventListener = (button, idOffer) => {
    button.addEventListener('click', async (e) => {
        await deleteOffer(idOffer);
        const parentNode = e.target.parentNode.parentNode;
        parentNode.parentNode.removeChild(parentNode);
    })
}

// Deletes an offer from the db
const deleteOffer = async (idOffer) => {
    try {
        const response = await fetch(`${url.origin}/trade/deleteoffer/${idOffer}`, {
            method: 'DELETE',
            headers: {
                'Authorization': getToken(),
                'Content-Type': 'application/json'
            }
        });
        if (response.status === 200) {
            console.log("Verwijderen aanbieding gelukt!") // TODO: verwijder
        } else if (response.status === 401) {
            alert("Sessie verlopen. U moet opnieuw inloggen.")
        } else {
            alert("Er is iets fout gegaan bij het verwijderen van de aanbieding. Probeer het later nog eens.")
        }
    } catch (e) {
        console.log(e);
    }
}

// Logs out the current user and redirects user to landing page
function logout() {
    console.log("logout");
    removeToken();
    window.location = "../index.html";
}