package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.Address;
import nl.miwteam2.cryptomero.domain.CustomerDto;
import nl.miwteam2.cryptomero.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * class responsible for all storing 3000 customers
 * @author Marcel Brachten, studentnr: 500893228 - MIW Cohort 26
 */
@Service
public class StoreService {
    private CustomerService customerService;
    private CustomerRepository customerRepository;
    private AddressService addressService;

    private static final String[] firstNames = { "Eva", "Karel", "Willem", "Marie", "Saskia", "Annet", "Sergio", "Michel", "Ria" };
    private static final String[] lastNames = { "Jansen", "Pietersen", "Willemsen", "Smit", "Jong", "Vries", "Bakker", "Bos" };
    private static final String PREFIX = "";
    private static final String PASSWORD = "ValidPassword";
    private static final String NETNUMMER = "06";
    private static final int PHONENUMBERMIN = 10000000;
    private static final int PHONENUMBERMAX = 99999999;
    private static final String STARTEMAIL = "gast";
    private static final String ENDEMAIL = "@yahoo.com";
    private static final LocalDate localDate = LocalDate.of(1960, 06, 22);
    @Autowired
    public StoreService(CustomerRepository customerRepository, CustomerService customerService, AddressService addressService) {
        this.customerRepository = customerRepository;
        this.customerService = customerService;
        this.addressService = addressService;
    }


    public CustomerDto storeExtraCustomers(int aantal) throws Exception {
        ArrayList<String> lijstBsnNummers = inlezenBsnNummers();
        ArrayList<Address> adressen = addressListStore();
        Random rng = new Random();
        CustomerDto customerDto = new CustomerDto();
        for (int i=0;i<aantal;i++){
            String email = STARTEMAIL + i + ENDEMAIL;
            LocalDate newDate = localDate.plusDays(i);
            customerDto = setAddress(adressen, customerDto, rng);
            customerDto = setOverigeGegevens(customerDto, rng);
            customerDto.setDob(newDate);
            customerDto.setBsn(lijstBsnNummers.get(i));
            customerDto.setEmail(email);
            if (customerService.isValidBsn(lijstBsnNummers.get(i))){
                customerService.storeOne(customerDto);
            }
        }
        return customerDto;
    }

    private CustomerDto setAddress(ArrayList<Address> addressesList, CustomerDto customerDto, Random rng){
        Address adres = addressesList.get(rng.nextInt(addressesList.size()));
        customerDto.setAddress(adres);
        return customerDto;
    }

    private CustomerDto setOverigeGegevens(CustomerDto customerDto, Random rng){
        String voornaam = firstNames[rng.nextInt(firstNames.length)];
        String achternaam = lastNames[rng.nextInt(lastNames.length)];
        String telefoonNummer = NETNUMMER + rng.nextInt(PHONENUMBERMIN,PHONENUMBERMAX);
        customerDto.setFirstName(voornaam);
        customerDto.setNamePrefix(PREFIX);
        customerDto.setLastName(achternaam);
        customerDto.setTelephone(telefoonNummer);
        customerDto.setPassword(PASSWORD);
        return customerDto;
    }


    private ArrayList<Address> addressListStore(){
        ArrayList<String> regelsUitBestandAdres = new ArrayList<>();
        InputStream in = getClass().getResourceAsStream("/adressenLijst.csv");
        try {
            Scanner invoer = new Scanner(in);
            while (invoer.hasNextLine()) {
                regelsUitBestandAdres.add(invoer.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<Address> adressen = new ArrayList<>();
        if (regelsUitBestandAdres.size() > 0) {
            adressen = addressArrayList(regelsUitBestandAdres);
        }
        return adressen;
    }


    public ArrayList<Address> addressArrayList(ArrayList<String> bestandsregels) {
        ArrayList<Address> addresses = new ArrayList<>();
        for (int i = 0; i < bestandsregels.size(); i++) {
            String[] regelArray = bestandsregels.get(i).split(";");
            String postcodeCijfers = regelArray[0];
            String postcodeLetters = regelArray[1];
            String postcode = postcodeCijfers + postcodeLetters;
            int huisnummer = Integer.parseInt(regelArray[2]);
            String plaatsnaam = regelArray[3];
            String straatnaam = regelArray[4];
            Address adres = new Address(straatnaam, huisnummer, PREFIX, postcode, plaatsnaam);
            if (addressService.storeAddress(adres) != 0) {
                int addressId = addressService.storeAddress(adres);
                adres.setIdAddress(addressId);
                addresses.add(adres);
            }
        }
        return addresses;
    }

    private ArrayList<String> inlezenBsnNummers(){
        ArrayList<String> regelsUitBestand = new ArrayList<>();
        ArrayList<String> bsnLijst = new ArrayList<>();
        InputStream in = getClass().getResourceAsStream("/bsnLijst.csv");
        Scanner invoer = new Scanner(in);
        while (invoer.hasNextLine()) {
            regelsUitBestand.add(invoer.nextLine());
        }
        if (regelsUitBestand.size() > 0) {
            for (int i = 0; i < regelsUitBestand.size(); i++) {
                bsnLijst.add(regelsUitBestand.get(i));
            }
        }
        return bsnLijst;
    }
}
