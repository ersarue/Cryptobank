'use strict'

export const includeNoOffersBox = () => {
    let element, file, xhttp;
    element = document.getElementById("offers-box");
    file = element.getAttribute("includeoffersbox");
    if (file) {
        /*make an HTTP request using the attribute value as the file name:*/
        xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState === 4) {
                if (this.status === 200) {element.innerHTML = this.responseText;}
                if (this.status === 404) {element.innerHTML = "Page not found.";}
            }
        }
        xhttp.open("GET", file, false);
        xhttp.send();
        /*exit the function:*/
        return;
    }
};