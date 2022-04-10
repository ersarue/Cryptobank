// @author Marcel Brachten, studentnr: 500893228 - MIW Cohort 26

const MAX_LENGTH_FIRST_NAME = 10;
const MAX_LENGTH_NAME_PREFIX = 15;
const MAX_LENGTH_LAST_NAME = 10;
const MAX_LENGTH_TELEPHONE = 12;
const MAX_LENGTH_PASSWORD = 64;
const MIN_LENGTH_PASSWORD = 8;
const MIN_LENGTH_BSN = 8;
const MAX_LENGTH_BSN = 9;
const FACTORS = [9, 8, 7, 6, 5, 4, 3, 2, -1];
const DIVISOR = 11

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
    if (bsnPreCheck()){
        bsnChecker()
        voortgangsMeter()
    }
})
document.querySelector('#telefoon').addEventListener('focusout', ()=>{
    restChecker()
    voortgangsMeter()
})
document.querySelector('#huisnummer').addEventListener('focusout', ()=>{
    if (postcodeHuisnummerChecker()){
        postcodeApi()
        voortgangsMeter()
    }
})
document.querySelector('#postcode').addEventListener('focusout', ()=>{
    if (postcodeHuisnummerChecker()){
        postcodeApi()
        voortgangsMeter()
    }
})

function voortgangsMeter(){
    let voortgangsgetal=0
    let tabelFoutTeksten = document.querySelectorAll('.progressClass')
    let tabelInputVelden = document.querySelectorAll('.progressClassInput')
    const extra= 100/tabelFoutTeksten.length
    for (let i=0;i<tabelInputVelden.length;i++){
        if(tabelFoutTeksten[i].innerHTML===""&&tabelInputVelden[i].value!=="")
            voortgangsgetal+=extra
    }
    document.getElementById("myBar").style.width=`${voortgangsgetal}%`
    if (voortgangsgetal>=100){
        document.querySelector('#btnStore').disabled=false
    }
}

function restCheckerStartWaarden(){
    let vvs = document.querySelector('#voorvoegsel').value
    let ans = document.querySelector('#achternaam').value
    let tels = document.querySelector('#telefoon').value
    let vns = document.querySelector('#voornaam').value
    return [vns,ans,vvs,tels]
}
function restCheckerStartWaardenResults(){
    let vvr = document.querySelector('#voorvoegselResult')
    let anr = document.querySelector('#achternaamResult')
    let telr = document.querySelector('#telefoonResult')
    let vnr = document.querySelector('#voornaamResult')
    return [vnr,anr,vvr,telr]
}
// deze methode roept eerst twee hulpmethode aan, daarna zal deze de lengte van vier velden testen
function restChecker(){
        let restVelden = restCheckerStartWaarden()
        const constanten=[MAX_LENGTH_FIRST_NAME,MAX_LENGTH_LAST_NAME,MAX_LENGTH_NAME_PREFIX,MAX_LENGTH_TELEPHONE]
        let restVeldenResults = restCheckerStartWaardenResults()
        let vvResult=document.querySelector('#voorvoegselResult')
        for (let i=0;i<constanten.length;i++){
            if (restVelden[i].length>constanten[i]){
                restVeldenResults[i].innerHTML="de ingevoerde waarde is te lang"
        }
        else{
            restVeldenResults[i].innerHTML=""
        }
    }
}
function bsnPreCheck(){
    let bsn = document.querySelector('#bsn').value
    document.querySelector('#bsnResult').innerHTML=""
    if (bsn.length < MIN_LENGTH_BSN || bsn.length > MAX_LENGTH_BSN){
        document.querySelector('#bsnResult').innerHTML="Bsn voldoet niet aan lengte eis"
        return false
    }
    let regex = new RegExp(/^[^a-zA-Z]+$/)
    if (!regex.test(bsn)){
        document.querySelector('#bsnResult').innerHTML="Bsn heeft letters"
        return false
    }
    return true
}

function bsnChecker(){
    let bsn = document.querySelector('#bsn').value
    if (bsn.length == 8){
        bsn = "0" + bsn; //prepend 0 to ensure bsn consists of 9 numbers
    }
    let sum = 0;
    for (i = 0; i < bsn.length; i++) {
        let digit = bsn.substring(i, i + 1);
        sum += digit * FACTORS[i];
    }
    if (sum%DIVISOR !== 0){
        document.querySelector('#bsnResult').innerHTML="Geen geldig bsn"
    }
}

function leeftijdChecker(){
    let dateOfBirth = document.querySelector('#gebdatum').value
    const d = new Date(dateOfBirth);
    const maanden = Date.now() - d.getTime();
    const newDate= new Date(maanden)
    const year = newDate.getUTCFullYear();
    const age = Math.abs(year - 1970);
    if (age<18){
        document.querySelector('#dobResult').innerHTML="De minimumleeftijd is 18 jaar"
    }else if (dateOfBirth.length===0){
        document.querySelector('#dobResult').innerHTML="Geen geldige datum"
    }else{
        document.querySelector('#dobResult').innerHTML=""
    }

}
//regex eisen volgens Product Owner, daarna database lengte velden testen, doel duidelijk info aan nieuwe klant
function regexEisenPassword(){
    let regexLG = new RegExp(/^(?=.*?[a-z]{2})(?=.*?[A-Z]{2}).{0,65}$/i)
    let regexGS = new RegExp(/^(?=.*[0-9])(?=.*\W).{0,65}$/i)
    let regexMinL = new RegExp(/^.{8,}$/i)
    let regexMaxL = new RegExp(/^.{0,15}$/i)
    return [regexLG, regexGS, regexMinL, regexMaxL]
}
function foutmeldingenPassword(){
    let lettergrootteTekst="Minimaal 1 A-Z en 1 a-z;"
    let speciaalTekst="Minimaal een 0-9 en een speciaal teken (%$#@); "
    let teKortTekst="Meer dan 8 karakters;"
    let teLangTekst="Minder dan 15 karakters;"
    return [lettergrootteTekst,speciaalTekst,teKortTekst,teLangTekst]
}
function passwordChecker(){
    let pws = document.querySelector('#password').value
    let pwr = document.querySelector('#passwordResult')
    let regexVelden= regexEisenPassword()
    let foutTekstenPassword=foutmeldingenPassword()
    let passwordCheckerTekst=""
    for (let j=0;j<regexVelden.length;j++){
        if (!regexVelden[j].test(pws)){
            passwordCheckerTekst+=foutTekstenPassword[j]
        }
    }
    pwr.innerHTML =passwordCheckerTekst
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
function postcodeHuisnummerChecker() {
    let regex = new RegExp(/^[1-9][0-9]{3}[\s]?[A-Za-z]{2}$/i);
    let postcode = document.querySelector('#postcode').value
    let huisnummer = document.querySelector('#huisnummer').value
    if (regex.test(postcode)&&(postcode!=="")&&huisnummer!==""){
        document.querySelector('#postcodeResult').innerHTML=""
        return true
    }else{
        document.querySelector('#postcodeResult').innerHTML="Postcode formaat 1234DE? Huisnummer ingevuld?"
        return false
    }
}
function samenstellenJsonCustomer(){
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
    return data = {
        "idAccount": 1,
        "email": em,"password": pw,"firstName": vn,"namePrefix": vv,"lastName": an,
        "dob": gb,"bsn": bsn,"telephone": tel,
        "address": {"idAddress": 1,"streetName": sn,"houseNo": hn,"houseAdd": tvv,
            "postalCode": pc,"city": pn
        }
    };
}
function maakCustomer() {
    let data = samenstellenJsonCustomer()
    fetch('http://localhost:8080/users/register', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)  // moet worden omgezet naar een string
    })
        .then(response => {
                if (response.status===400){
                    alert("email of bsn wordt al gebruikt")
                }
                return response.json()
            }
        )
        .then(data => {
            let welkomtekst="Welkom "+data.firstName + " "+ data.lastName
            document.querySelector('#welkomLayout').innerHTML = welkomtekst;
        });
        opmaakschermNaRegistreren()
}

function opmaakschermNaRegistreren(){
    document.querySelector('#plaatje').src="../images/chickie_klein.png"
    document.querySelector('#plaatje').width="80"
    document.querySelector('#plaatje').height="80"
    document.querySelector('#btnStore').disabled=true
}
function postcodeApi(){
    let postcode = document.querySelector('#postcode').value
    let huisnummer = document.querySelector('#huisnummer').value
    let formData = `postcode=${postcode}&number=${huisnummer}`
    fetch("https://postcode.tech/api/v1/postcode?" + formData , {
        headers: {
            'Authorization': 'Bearer 5cc336e3-c924-44d6-a44f-d8b9e5e0ddb9',
        },
    })
        .then(response => {
                return response.json()
            }
        )
        .then(json => {
            aanvullenAdresgegevens(json.street, json.city)
        })
}
function aanvullenAdresgegevens(responseStreet, responseCity){
    if (responseStreet===undefined){
        document.getElementById('straatnaam').value = null;
        document.getElementById('stad').value = null;
        document.querySelector('#postcodeResult').innerHTML="dit is geen geldige huisnummer/postcode combinatie"
    }else{
        document.querySelector('#postcodeResult').innerHTML=""
        document.querySelector('#straatnaam').value=responseStreet
        document.querySelector('#stad').value=responseCity
    }
}
