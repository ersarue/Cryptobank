// @author Ercan Ersaru

// 'use strict'

import {removeToken} from "./tokenUtils"

const logoutButton = document.getElementById("logout-btn")
logoutButton.addEventListener("click", logout)

function logout() {
    console.log("logout");
    removeToken();
    window.location = "../index.html"
}