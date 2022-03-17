const url = "http://localhost:8080/users/1"


fetch(url, {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json'
        //'Access-Control-Allow-Origin': '*'
    }
})
    .then(response => {
        if (response.status === 200) {
            return response.json()
        } else {
            throw new Error("something is wrong" + response.status)
        }
    })
    .then(json => {
            console.log(json)
            fillPage(json)
            fillTable(json)
        }
    )
    .catch((err) => {
        console.log(err);
    });

function fillPage(json) {
    document.getElementById("welkom").innerHTML = json.firstName
    document.getElementById("iban").innerHTML = json.bankAccount.iban
    document.getElementById("saldo").innerHTML = json.bankAccount.balanceEur
}

function fillTable(json) {

    const table = document.getElementById("tableBody")

    const assetNames = Object.keys(json.wallet);
    const assetValues = Object.values(json.wallet);

    for (let i = 0; i < assetNames.length; i++) {

        let rowNode = document.createElement("tr")
        let cellNode1 = document.createElement("td")
        let cellNode2 = document.createElement("td")
        let textNode1 = document.createTextNode(assetNames[i])
        let textNode2 = document.createTextNode(assetValues[i])

        cellNode1.appendChild(textNode1)
        cellNode2.appendChild(textNode2)

        rowNode.appendChild(cellNode1)
        rowNode.appendChild(cellNode2)

        table.appendChild(rowNode)

    }


}

