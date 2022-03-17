// Author: Petra Coenen

"use strict";

export const getToken = () => {
    const token = localStorage.getItem("access-token");
    // console.log("Token retrieved from localStorage:" + token);
    return `Bearer ${token}`;
}