window.addEventListener('load', onLoad)
function onLoad(){
  let xValues = ["","","","","",""];
  let yValues = [0,23,1,19,33,4];
  new Chart("myChart", {
    type: "line",
    data: {
      labels: xValues,
      datasets: [{
        fill: false,
        lineTension: 0,
        backgroundColor: "rgba(0,0,255,1.0)",
        borderColor: "rgba(0,0,255,0.1)",
        data: yValues
    }]
  },
    options: {
      legend: {display: false},
      scales: {
      yAxes: [{ticks: {min: 0, max:50}}],
    }
  }
});
}
document.querySelector('#bitcoinPicture').addEventListener('click', ()=>{
  maakCustomer("Bitcoin")
})
document.querySelector('#ethereumPicture').addEventListener('click', ()=>{
  maakCustomer("Ethereum")
})
document.querySelector('#tetherPicture').addEventListener('click', ()=>{
  maakCustomer("Tether")
})

function maakCustomer(naamCrypto) {
    let formData = `name=${naamCrypto}`
    fetch('http://localhost:8080/rates/history?'+formData+'&interval=day&datapoints=4', {
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
            let minimaleWaarde = Math.min(data1[0],data1[1],data1[2],data1[3])
            let maximaleWaarde = Math.max.apply(null, data1);
            let minYscale = 0;
            let maxYscale = 0;
            if (maximaleWaarde<1){
              minYscale=0
              maxYscale=Math.round(maximaleWaarde/0.1)*0.1+0.1
            }else if (maximaleWaarde<10){
              minYscale=Math.max(Math.round(minimaleWaarde/1)*1-1,0);
              maxYscale=Math.round(maximaleWaarde/1)*1+1
            }
            else if (maximaleWaarde<100){
              minYscale=Math.max(Math.round(minimaleWaarde/10)*10-10,0);
              maxYscale=Math.round(maximaleWaarde/10)*10+10
            }
            else if (maximaleWaarde<1000){
              minYscale=Math.max(Math.round(minimaleWaarde/100)*100-100,0);
              maxYscale=Math.round(maximaleWaarde/100)*100+100
            }
            else{
              minYscale=Math.max(Math.round(minimaleWaarde/1000)*1000-1000,0);
              maxYscale=Math.round(maximaleWaarde/1000)*1000+1000
            }
            
            document.querySelector("#koers1").innerHTML=data1[0]
            document.querySelector("#koers2").innerHTML=data1[1]
            document.querySelector("#koers3").innerHTML=data1[2]
            document.querySelector("#koers4").innerHTML=data1[3]
            let yValues = [data1[0],data1[1],data1[2],data1[3]];
            let xValues = [data2[0].substring(0,10),data2[1].substring(0,10),data2[2].substring(0,10),data2[3].substring(0,10)]
            new Chart("myChart", {
              type: "line",
              data: {
                labels: xValues,
                datasets: [{
                fill: false,
                lineTension: 0,
                backgroundColor: "rgba(0,0,255,1.0)",
                borderColor: "rgba(0,0,255,0.1)",
                data: yValues
              }]
            },
              options: {
                legend: {display: false},
                scales: {
                  yAxes: [{ticks: {min: minYscale, max: maxYscale}}],
                }
              }
            });
      });
        
}