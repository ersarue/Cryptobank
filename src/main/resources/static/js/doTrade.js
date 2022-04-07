/**
 * Script for page doTrade performing a trade attempt with bank of customer
 * @author: Samuel Geurts, studentnr: 500893275 - MIW Cohort 26
 */

'use strict'

import {getToken} from "./tokenUtils.js";

const url = new URL(window.location.href)

export const addModalDropDown = () => {
//als de gebruiker klikt op dropdownmenu wordt de juiste koers opgehaald
    document.querySelector('#inputGroupSelect01').addEventListener('change', async () => {
       await zoekKoers(document.querySelector("#inputGroupSelect01").value)
    })
}

export const addModalSubmitButton = () => {
//bepaalt eerst BANK of Marktplaats , alleen bank werkt 
    document.querySelector('#actieButton').addEventListener('click', () => {
        const radiobuttonBank = document.querySelector('#btnradio1')
        if (radiobuttonBank.checked) {
            storeBankTransactie()
        } else {
            storeOffer()
        }
    })
}
//er wordt gecheckt of er een koopooffer of verkoopoffer geplaatst wil worden
function samenstellenOffer(){
    const radiobuttonKoop = document.querySelector('#btnradio3')
    const assetName =  document.querySelector('#inputGroupSelect01').value
    let prijsOffer = document.querySelector('#offerPrijs').value
    let amount =  document.querySelector('#amountAsset').value * 1
    if (radiobuttonKoop.checked){
        amount = amount * -1
    }
    let data = {
        "assetNameOffer": assetName,
        "amountOffer": amount,
        "rateOffer": prijsOffer
        };
    console.log(data)
    return data
}
//de offer op marktplaats wordt gestored, ER zijn geen checks
function storeOffer(){
    let dataOffer = samenstellenOffer()
    fetch(`${url.origin}/trade/offer`, {
            method: 'POST',
            headers: {
                'Authorization': getToken(),
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(dataOffer)  // moet worden omgezet naar een string
        })
            .then(response => {
                    if (response.status===400){
                        alert("foutmelding")
                    }
                    return response.json()
                }
            )
            .then(data => {
                console.log(data)
            });
    }

//er wordt gecheckt of er een koop of verkoop met de bank plaatsvindt
function samenstellenDataBankTransactie(){
    const radiobuttonKoop = document.querySelector('#btnradio3')
    const assetName =  document.querySelector('#inputGroupSelect01').value
    let amount =  document.querySelector('#amountAsset').value
    if (radiobuttonKoop.checked){
        amount = amount * -1
    }
    let data = {
        "assetNameTrade": assetName,
        "amountTrade": amount
        };
    return data
}

//de transactie met de bank wordt gestored, ER zijn geen checks
function storeBankTransactie(){
    let data = samenstellenDataBankTransactie()
    fetch(`${url.origin}/trade/bank`, {
            method: 'POST',
            headers: {
                'Authorization': getToken(),
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)  // moet worden omgezet naar een string
        })
            .then(response => {
                    if (response.status===400){
                        response.text().then(function (text) {
                            alert(text)
                        })
                    } else {
                        return response.json()
                    }
                }
            )
            .then(data => {
                console.log(data)
            });
    }


//wordt opgeroepen als klant klikt op dropdownmenu
async function zoekKoers(naamCrypto) {
    const response = await fetch(`${url.origin}/rates/latest/${naamCrypto}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
    });
    const result = await response.json();
    document.querySelector("#koersMunt").value = `€ ${round(result.rate)}`;
}

// Rounds the rate amounts to a precise two decimals
const round = (num) => {
    const number = Number((Math.abs(num) * 100).toPrecision(15));
    return Math.round(number) / 100 * Math.sign(num);
}

//zoekt alle 20 crypto's uit de tabel 'rate'.
Promise.resolve(
    fetch(`${url.origin}/rates/latest`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    })
    )
        .then(response => response.json())
        .then(data => {
            fillDropDownMenu(data)
    })
        .catch((error) => {
        console.error('niet gelukt', error);
})

//Wordt gebruikt bij openen scherm, vult dropdownmenu en pakt de koers van geselecteerde crypto in menu
function fillDropDownMenu(data) {
    const dropDownMenu = document.querySelector("#inputGroupSelect01")
    for (let asset in data) {
        const assetName = data[asset].asset.assetName
        const option = document.createElement('option');
        option.text = option.value = assetName;
        dropDownMenu.add(option, 0);
    }
    // document.querySelector("#koersMunt").value=data[0].rate
}

export const addTradeButtonsEventListeners = () => {
    // Note: html-elements need to be selected inside this method for the export to work
    const bankTradeBtn = document.getElementById('btnradio1');
    const marketPlaceTradeBtn = document.getElementById('btnradio2');
    const priceMarketPlaceTitle = document.getElementById('priceMarketPlaceTitle');
    const priceMarketPlace = document.querySelector('.price-market-place');
    const buyBtn = document.getElementById('btnradio3');
    const sellBtn = document.getElementById('btnradio4');
    const buyOrSellQuestion = document.getElementById('buyOrSellQuestion');
    bankTradeBtn.addEventListener('click', (e) => {
        if (e.target.checked) {
            priceMarketPlaceTitle.classList.add('invisible');
            priceMarketPlace.classList.add('invisible');
            buyOrSellQuestion.innerHTML = 'Wil je cryptocoins kopen of verkopen aan de bank?';
            setTextSubmitBtn();
        }
    });
    marketPlaceTradeBtn.addEventListener('click', (e) => {
        if (e.target.checked) {
            priceMarketPlaceTitle.classList.remove('invisible');
            priceMarketPlace.classList.remove('invisible');
            buyOrSellQuestion.innerHTML = 'Wil je cryptocoins kopen of verkopen op de marktplaats?';
            setTextSubmitBtn();
        }
    });
    buyBtn.addEventListener('click', () => {
        priceMarketPlaceTitle.innerHTML = 'Voor welke maximumprijs per hele munt wil je deze cryptomunt kopen?';
    })
    sellBtn.addEventListener('click', () => {
        priceMarketPlaceTitle.innerHTML = 'Voor welke minimumprijs per hele munt wil je deze cryptomunt verkopen?';
    })
}

const setTextSubmitBtn = () => {
    const bankTradeBtn = document.getElementById('btnradio1');
    const marketPlaceTradeBtn = document.getElementById('btnradio2');
    const submitBtn = document.getElementById('actieButton');
    bankTradeBtn.checked
    ? submitBtn.innerHTML = 'Sluit deal met de bank'
    : submitBtn.innerHTML = 'Plaats aanvraag op de marktplaats';
}