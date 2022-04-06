/**
 * Script for page doTrade performing a trade attempt with bank of customer
 * @author: Samuel Geurts, studentnr: 500893275 - MIW Cohort 26
 */

'use strict'

import {includeHTML, addLogout} from "./includeHTML.js";
import {getToken} from "./tokenUtils.js";

const url = new URL(window.location.href)

document.addEventListener('DOMContentLoaded', () => {
    includeHTML()
    addLogout()
})
//als de gebruiker klikt op dropdownmenu wordt de juiste koers opgehaald
document.querySelector('#inputGroupSelect01').addEventListener('click', ()=>{
    zoekKoers(document.querySelector("#inputGroupSelect01").value)
})
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
