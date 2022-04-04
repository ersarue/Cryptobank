/*
    Author Mink Tielenius Kruijthoff (with help from www.w3schools.com)
    Script for including common HTML elements in a Cryptomero page
*/

import {removeToken} from "./tokenUtils.js";

export const includeHTML = () => {
    var z, i, elmnt, file, xhttp;
    /*loop through a collection of all HTML elements:*/
    z = document.getElementsByTagName("*");
    for (i = 0; i < z.length; i++) {
        elmnt = z[i];
        /*search for elements with a certain atrribute:*/
        file = elmnt.getAttribute("includehtml");
        if (file) {
            /*make an HTTP request using the attribute value as the file name:*/
            xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function() {
                if (this.readyState == 4) {
                    if (this.status == 200) {elmnt.innerHTML = this.responseText;}
                    if (this.status == 404) {elmnt.innerHTML = "Page not found.";}
                    /*remove the attribute, and call this function once more:*/
                    elmnt.removeAttribute("includehtml");
                    includeHTML();
                }
            }
            xhttp.open("GET", file, false);
            xhttp.send();
            /*exit the function:*/
            return;
        }
    }
};

// Logout
export const addLogout = () => {
    const logoutButton = document.getElementById("logout-btn" )
    logoutButton.addEventListener("click" , logout)
}

const logout = () => {
    console.log("logout");
    removeToken();
    window.location = "../index.html"
}