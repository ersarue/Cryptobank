// window.addEventListener('load', onLoad)
export const getCryptomeroGrafiek = () => {
    let xValues = ["","","","","",""];
    let yValues = [0,23,1,19,33,4];
    grafiek(xValues,yValues,0,50)
}

export const addGraphs = () => {
    document.querySelector('#dag').addEventListener('click', ()=>{
        maakGrafiek(document.querySelector("#naamCrypto").innerHTML, 4, "daypart")
    })
    document.querySelector('#week').addEventListener('click', ()=>{
        maakGrafiek(document.querySelector("#naamCrypto").innerHTML, 7, "day")
    })
    document.querySelector('#maand').addEventListener('click', ()=>{
        maakGrafiek(document.querySelector("#naamCrypto").innerHTML, 28, "day")
    })
}

export const maakGrafiek = (naamCrypto, aantalDagen, interval) => {
    let datapointsAantal=`datapoints=${aantalDagen}`
    let nameCryptomunt = `name=${naamCrypto}`
    let intervalKeuze = `&interval=${interval}&`

    fetch('http://localhost:8080/rates/history?'+nameCryptomunt+intervalKeuze+datapointsAantal, {
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
            const data2 = Object.keys(data).map(key => key);
            const data1 = Object.values(data).map(value => value);
            let minimaleWaarde = Math.min.apply(null, data1);
            let maximaleWaarde = Math.max.apply(null, data1);
            let schaal = schaalYasBerekenen(maximaleWaarde,minimaleWaarde)
            let yValues=[]
            let xValues=[]
            for (let i=0; i<data1.length;i++){
                yValues.push(data1[i])
                xValues.push(data2[i].substring(0,10))
            }
            grafiek(xValues,yValues,schaal[0],schaal[1])
            document.querySelector("#naamCrypto").innerHTML=naamCrypto
        });
}

function grafiek(xWaarden, yWaarden, schaalMin, schaalMax){
    new Chart("myChart", {
        type: "line",
        data: {
            labels: xWaarden,
            datasets: [{
                fill: false,
                lineTension: 0,
                backgroundColor: "rgb(255,0,59)",
                borderColor: "rgba(1,21,243,0.38)",
                data: yWaarden,
            }]
        },
        options: {
            legend: {display: false},
            scales: {
                yAxes: [{ticks: {min: schaalMin, max: schaalMax}}],
            }
        }
    });
}

function schaalYasBerekenen(hoogsteWaarde, laagsteWaarde){
    let minYscale=0
    let maxYscale=0
    if (hoogsteWaarde<1){
        minYscale=0
        maxYscale=Math.round(hoogsteWaarde/0.1)*0.1+0.1
    }else if (hoogsteWaarde<10){
        minYscale=Math.max(Math.round(laagsteWaarde/1)*1-1,0);
        maxYscale=Math.round(hoogsteWaarde/1)*1+1
    }
    else if (hoogsteWaarde<100){
        minYscale=Math.max(Math.round(laagsteWaarde/10)*10-10,0);
        maxYscale=Math.round(hoogsteWaarde/10)*10+10
    }
    else if (hoogsteWaarde<1000){
        minYscale=Math.max(Math.round(laagsteWaarde/100)*100-100,0);
        maxYscale=Math.round(hoogsteWaarde/100)*100+100
    }
    else{
        minYscale=Math.max(Math.round(laagsteWaarde/1000)*1000-1000,0);
        maxYscale=Math.round(hoogsteWaarde/1000)*1000+1000
    }
    return [minYscale,maxYscale]
}