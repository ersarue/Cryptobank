/**
 * Script for page doTrade performing a trade attempt with bank of customer
 * @author: Samuel Geurts, studentnr: 500893275 - MIW Cohort 26
 */

'use strict'

import {getToken} from "./tokenUtils.js";

export const addModalDropDown = () => {
//als de gebruiker klikt op dropdownmenu wordt de juiste koers opgehaald
    document.querySelector('#inputGroupSelect01').addEventListener('click', () => {
        zoekKoers(document.querySelector("#inputGroupSelect01").value)
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
    fetch('http://localhost:8080/trade/offer', {
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
    fetch('http://localhost:8080/trade/bank', {
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
                        alert("foutmelding")
                    }
                    return response.json()
                }
            )
            .then(data => {
                console.log(data)
            });
    }


//wordt opgeroepen als klant klikt op dropdownmenu
function zoekKoers(naamCrypto) {
    fetch('http://localhost:8080/rates/latest/'+naamCrypto, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
    })
        .then(response => {
                return response.json()
            }
        )
        .then(data => {
            document.querySelector("#koersMunt").value=data.rate
        });
}
//zoekt alle 20 crypto's uit de tabel 'rate'.
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
    document.querySelector("#koersMunt").value=data[0].rate
}

export const addTradeButtonsEventListeners = () => {
    // Note: html-elements need to be selected inside this method for the export to work
    const bankTradeBtn = document.getElementById('btnradio1');
    const marketPlaceTradeBtn = document.getElementById('btnradio2');
    const priceMarketPlaceTitle = document.getElementById('priceMarketPlaceTitle');
    const priceMarketPlace = document.querySelector('.price-market-place');
    bankTradeBtn.addEventListener('click', (e) => {
        if (e.target.checked) {
            priceMarketPlaceTitle.classList.add('invisible');
            priceMarketPlace.classList.add('invisible');
        }
    });
    marketPlaceTradeBtn.addEventListener('click', (e) => {
        if (e.target.checked) {
            priceMarketPlaceTitle.classList.remove('invisible');
            priceMarketPlace.classList.remove('invisible');
        }
    });
}