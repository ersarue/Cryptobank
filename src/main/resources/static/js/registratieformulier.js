document.querySelector('#btnStore').addEventListener('click', ()=>{
    maakCustomer()
})

document.querySelector('#gebdatum').addEventListener('focusout', ()=>{
    leeftijdChecker()
    voortgangsMeter()
})

document.querySelector('#email').addEventListener('focusout', ()=>{
    emailChecker()
    voortgangsMeter()
})
document.querySelector('#password').addEventListener('focusout', ()=>{
    passwordChecker()
    voortgangsMeter()
})
document.querySelector('#voornaam').addEventListener('focusout', ()=>{
    restChecker()
    voortgangsMeter()
})
document.querySelector('#voorvoegsel').addEventListener('focusout', ()=>{
    restChecker()
    voortgangsMeter()
})
document.querySelector('#achternaam').addEventListener('focusout', ()=>{
    restChecker()
    voortgangsMeter()
})
document.querySelector('#gebdatum').addEventListener('focusout', ()=>{
    leeftijdChecker()
    voortgangsMeter()
})
document.querySelector('#bsn').addEventListener('focusout', ()=>{
    bsnChecker()
    voortgangsMeter()
})
document.querySelector('#telefoon').addEventListener('focusout', ()=>{
    restChecker()
    voortgangsMeter()
})
document.querySelector('#huisnummer').addEventListener('focusout', ()=>{
    postcodeApi()
    voortgangsMeter()
})
document.querySelector('#postcode').addEventListener('focusout', ()=>{
    document.querySelector('#postcodeResult').innerHTML=""
    document.querySelector('#huisnummerResult').innerHTML=""
    if (postcodeRegEx()){
        postcodeApi()
        voortgangsMeter()
    }
    
})
function voortgangsMeter(){
    let voortgangsgetal=0
    passwordChecker()
    let tabelFoutTeksten = document.querySelectorAll('.progressClass')
    let tabelInputVelden = document.querySelectorAll('.progressClassInput')
    //console.log(tabelFoutTeksten.length)
    //console.log(tabelInputVelden.length)
    const extra= 100/tabelFoutTeksten.length
    for (let i=0;i<tabelInputVelden.length;i++){
        //console.log(tabelInputVelden[i].value)
        //console.log(tabelFoutTeksten[i].innerHTML.length)
        if(tabelFoutTeksten[i].innerHTML===""&&tabelInputVelden[i].value!=="")
        voortgangsgetal+=extra
        //console.log(voortgangsgetal)
    }
    
    // tabelFoutTeksten.forEach(e => {
    //     if (e.innerHTML===""){
    //         voortgangsgetal+=extra
    //     }
    // })
    //console.log(tabel.length)
    //voortgangsgetal = voortgangsgetal+ 10 
    document.getElementById("myBar").style.width=`${voortgangsgetal}%`
    if (voortgangsgetal>99){
        document.querySelector('#btnStore').disabled=false
    }
}
function restChecker(){
        const MAX_LENGTH_FIRST_NAME = 45;
        const MAX_LENGTH_NAME_PREFIX = 15;
        const MAX_LENGTH_LAST_NAME = 45;
        const MAX_LENGTH_TELEPHONE = 30;
        let voornaam = document.querySelector('#voornaam').value
        let voorvoegsel = document.querySelector('#voorvoegsel').value
        let achternaam = document.querySelector('#achternaam').value
        let telefoon = document.querySelector('#telefoon').value
        if (voornaam.length>=MAX_LENGTH_FIRST_NAME){
            document.querySelector('#voornaamResult').innerHTML="te lang"
        }
        else{
            document.querySelector('#voornaamResult').innerHTML=""
        }
        if (voorvoegsel.length>=MAX_LENGTH_NAME_PREFIX){
            document.querySelector('#voorvoegselResult').innerHTML="te lang"
        }
        else{
            document.querySelector('#voorvoegselResult').innerHTML=""
        }
        if (achternaam.length>=MAX_LENGTH_LAST_NAME){
            document.querySelector('#achternaamResult').innerHTML="te lang"
        }
        else{
            document.querySelector('#achternaamResult').innerHTML=""
        }
        if (telefoon.length>=MAX_LENGTH_TELEPHONE){
            document.querySelector('#telefoonResult').innerHTML="te lang"
        }
        else{
            document.querySelector('#telefoonResult').innerHTML=""
        }
}
function bsnChecker(){
    let bsn = document.querySelector('#bsn').value
    document.querySelector('#bsnResult').innerHTML=""
    const MIN_LENGTH = 8;
    const MAX_LENGTH = 9;
    if (bsn.length < MIN_LENGTH || bsn.length > MAX_LENGTH){
        document.querySelector('#bsnResult').innerHTML="bsn voldoet niet aan lengte eis"
    }
    let regex = new RegExp(/^[^a-zA-Z]+$/)
    if (!regex.test(bsn)){
        document.querySelector('#bsnResult').innerHTML="bsn heeft letters"
    }
    if (bsn.length == 8){
        bsn = "0" + bsn; //prepend 0 to ensure bsn consists of 9 numbers
    }
    const FACTORS = [9, 8, 7, 6, 5, 4, 3, 2, -1];
    const DIVISOR = 11
    let sum = 0;
    for (i = 0; i < bsn.length; i++) {
        let digit = bsn.substring(i, i + 1);
        sum += digit * FACTORS[i];
    }
    console.log(sum%DIVISOR);
    if (sum%DIVISOR !== 0){

        document.querySelector('#bsnResult').innerHTML="geen geldig bsn"
    }
    //document.querySelector('#voortgang').style.width=100


}
function leeftijdChecker(){
    let dateOfBirth = document.querySelector('#gebdatum').value
    const d = new Date(dateOfBirth);
    const maanden = Date.now() - d.getTime();
    const newDate= new Date(maanden)
    const year = newDate.getUTCFullYear();
    const age = Math.abs(year - 1970);
    if (age<18){
        document.querySelector('#dobResult').innerHTML="te jong"
    } else if (dateOfBirth.length===0){
        document.querySelector('#dobResult').innerHTML="geen geldige datum"
    }else{
        document.querySelector('#dobResult').innerHTML=""
    }

}
function passwordChecker(){
    let regex = new RegExp(/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\s).{8,64}$/i)
    let passwordLengte= document.querySelector('#password').value.length
    let passwordCheckerTekst=""
    document.querySelector('#passwordResult').innerHTML =""
    if (!regex.test(document.querySelector('#password').value)&&(passwordLengte>64||passwordLengte<8)){
        passwordCheckerTekst="passwordeisen 8-64 karakters, minimaal 1 kleine letter, 1 Hoofdletter 1 getal 1 special karakter"
    }
    else if(passwordLengte>64||passwordLengte<8){
        passwordCheckerTekst="password te lang64/kort8"
    }else if(!regex.test(document.querySelector('#password').value)){
        passwordCheckerTekst="1 Kleine, 1 Hoofdletter 1 getal 1 special karakter"
    }
    document.querySelector('#passwordResult').innerHTML =passwordCheckerTekst
}
function emailChecker() {
    let regex = new RegExp(/^[a-zA-Z0-9_+&*-]+(?:\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,7}$/i)
    let emailLengte= document.querySelector('#email').value.length
    let emailCheckerTekst=""
    if (!regex.test(document.querySelector('#email').value)&&emailLengte>30){
        emailCheckerTekst="email niet correct EN email te lang"
    }
    else if(emailLengte>30){
        emailCheckerTekst="email te lang"
    }else if(!regex.test(document.querySelector('#email').value)){
        emailCheckerTekst="email niet correct"
    }
    document.querySelector('#emailResult').innerHTML =emailCheckerTekst

}
function postcodeRegEx() {
    let regex = new RegExp(/^[1-9][0-9]{3}[\s]?[A-Za-z]{2}$/i);
    let postcode = document.querySelector('#postcode').value
    //console.log('pc is valide: ' + regex.test(postcode))
    if (regex.test(postcode)){
        document.querySelector('#postcodeResult').innerHTML=""
        return true
    }else{
        document.querySelector('#postcodeResult').innerHTML="postcode verkeerd"
        return false
    }
    
}
function maakCustomer() {
    const em =  document.querySelector('#email').value
    const pw =  document.querySelector('#password').value
    const vn =  document.querySelector('#voornaam').value
    const vv =  document.querySelector('#voorvoegsel').value
    const an =  document.querySelector('#achternaam').value
    const gb =  document.querySelector('#gebdatum').value
    const bsn =  document.querySelector('#bsn').value
    const tel =  document.querySelector('#telefoon').value
    const sn =  document.querySelector('#straatnaam').value
    const hn =  document.querySelector('#huisnummer').value
    const tvv =  document.querySelector('#toevoegsel').value
    const pc =  document.querySelector('#postcode').value
    const pn =  document.querySelector('#stad').value
    let data = {
        "idAccount": 1,
        "email": em,"password": pw,"firstName": vn,"namePrefix": vv,"lastName": an,
        "dob": gb,"bsn": bsn,"telephone": tel,
        "address": {"idAddress": 1,"streetName": sn,"houseNo": hn,"houseAdd": tvv,
            "postalCode": pc,"city": pn
        }
    };

    fetch('http://localhost:8080/users/register', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)  // moet worden omgezet naar een string
    })
        .then(response => {

                console.log(response)
                //const te = response.body
                //document.querySelector('#bericht').innerHTML = te.roman;
                return response.json()


            }
        )
        .then(data => {
            
            let welkomtekst="Welkom "+data.firstName + " "+ data.lastName
            document.querySelector('#welkomLayout').innerHTML = welkomtekst;
        })
    // .catch((error) => {
    //     console.error('Foutje', error);
    // })
    ;
    document.querySelector('#plaatje').src="../images/chickie_klein.png"
    document.querySelector('#plaatje').width="80"
    document.querySelector('#plaatje').height="80"
    document.querySelector('#btnStore').disabled=true

}

function postcodeApi(){

    let postcode = document.querySelector('#postcode').value
    //let postcode = "2513ST"
    let huisnummer = document.querySelector('#huisnummer').value
    if (huisnummer===""||postcode===""){
        document.querySelector('#postcodeResult').innerHTML="postcode en huisnummer allebei invullen"
        document.querySelector('#huisnummerResult').innerHTML="postcode en huisnummer allebei invullen"
        return
    }
    //let huisnummer = 178
    // als postcode een valide postcode is nummer niet leeg, dan
    // console.log('pc is valide: ' + regex.test(postcode))


    let formData = `postcode=${postcode}&number=${huisnummer}` //postcode=1234AB&nr=15

    fetch("https://postcode.tech/api/v1/postcode?" + formData , {
        headers: {
            'Authorization': 'Bearer 5cc336e3-c924-44d6-a44f-d8b9e5e0ddb9',
        },
    })
        .then(response => {

                console.log(response)
                //const te = response.body
                //document.querySelector('#bericht').innerHTML = te.roman;
                return response.json()


            }
        )
        //.then(json => console.log(json.message))
        .then(json => {
            console.log(json.street)
            if (json.street===undefined){
                document.getElementById('straatnaam').value = null;
                document.getElementById('stad').value = null;
                document.querySelector('#postcodeResult').innerHTML="dit is geen geldige huisnummer/postcode combinatie"
            }else{
                document.querySelector('#postcodeResult').innerHTML=""
            document.querySelector('#straatnaam').value=(json.street)
            document.querySelector('#stad').value=json.city
            }
            

        })
}
