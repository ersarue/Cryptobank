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
