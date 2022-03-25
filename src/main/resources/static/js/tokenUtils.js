// Author: Petra Coenen

"use strict";

// Getting the access token from the browser local storage, including 'Bearer ' prefix, to be used in Authorization header
export const getToken = () => {
    const token = localStorage.getItem("access-token");
    // console.log("Token retrieved from localStorage:" + token);
    return `Bearer ${token}`;
}

// Removing the access token from the browser local storage
export const removeToken = () => {
    localStorage.removeItem("access-token");
}