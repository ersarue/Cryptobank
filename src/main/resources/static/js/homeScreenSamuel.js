// Author Samuel, Stijn, Mink

//use relative URL
let url = new URL(window.location.href)


// fetch customer information
//todo update url - portfolio/assets
//todo rate zonder token op te halen?
//todo iets met Acces-control-allow-origin?

Promise.all([
    fetch(`${url.origin}/users/1`, {
        method: 'GET',
        headers: {
            'Authorization': localStorage.getItem("token"),
            'Content-Type': 'application/json',
            //'Access-Control-Allow-Origin': '*'
        }
    }),
    fetch(`${url.origin}/assets`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            //'Access-Control-Allow-Origin': '*'
        }
    })
]).then(responses => {
        console.log(responses)
        if (responses[0].status === 401){
            alert("Token niet meer geldig, u moet opnieuw inloggen")
        } else if (responses[0].status === 200 && responses[1].status === 200) {
            return Promise.all(responses.map(function (response) {
                return response.json();
            }));
        } else {
            throw new Error("something is wrong" + response.status)
        }
    })
    .then(data => {
            console.log(data)
            fillHeaders(data[0])
            fillTable(data)
        }
    )
    .catch((err) => {
        console.log(err);
    });

function fillHeaders(json) {
    document.getElementById("welkom").innerHTML = json.firstName
    document.getElementById("iban").innerHTML = json.bankAccount.iban
    document.getElementById("saldo").innerHTML = json.bankAccount.balanceEur
}

function fillTable(data) {

    const table = document.getElementById("tableBody")

    const assetNames = Object.keys(data[0].wallet);
    const assetValues = Object.values(data[0].wallet);


    for (let i = 0; i < assetNames.length; i++) {

        console.log(assetNames[i])
        console.log(data[1])

        let rate = 0;

        //todo fix probleem dat de juiste rate niet gevonden wordt
        data[1].forEach(element => {
            if (element.assetName.toLowerCase === assetNames[i].toLowerCase) {
                rate = element.rate
                console.log(rate)
            }
        })

        let rowNode = document.createElement("tr")
        let cellNode1 = document.createElement("td")
        let cellNode2 = document.createElement("td")
        let cellNode3 = document.createElement("td")
        let cellNode4 = document.createElement("td")
        let textNode1 = document.createTextNode(assetNames[i])
        let textNode2 = document.createTextNode(assetValues[i])
        let textNode3 = document.createTextNode(rate)
        let textNode4 = document.createTextNode(assetValues[i]*rate)

        cellNode1.appendChild(textNode1)
        cellNode2.appendChild(textNode2)
        cellNode3.appendChild(textNode3)
        cellNode4.appendChild(textNode4)

        rowNode.appendChild(cellNode1)
        rowNode.appendChild(cellNode2)
        rowNode.appendChild(cellNode3)
        rowNode.appendChild(cellNode4)

        table.appendChild(rowNode)

    }




}

