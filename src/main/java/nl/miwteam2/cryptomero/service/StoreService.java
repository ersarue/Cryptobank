package nl.miwteam2.cryptomero.service;

import nl.miwteam2.cryptomero.domain.Address;
import nl.miwteam2.cryptomero.domain.CustomerDto;
import nl.miwteam2.cryptomero.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

@Service
public class StoreService {
    private CustomerService customerService;
    private CustomerRepository customerRepository;
    private AddressService addressService;
    private static final String[] firstNames = { "Eva", "Karel", "Willem", "Marie", "Saskia", "Annet", "Sergio", "Michel", "Ria" };
    private static final String[] lastNames = { "Jansen", "Pietersen", "Willemsen", "Smit", "Jong", "Vries", "Bakker", "Bos" };

    @Autowired
    public StoreService(CustomerRepository customerRepository, CustomerService customerService, AddressService addressService) {
        this.customerRepository = customerRepository;
        this.customerService = customerService;
        this.addressService = addressService;
    }
    public CustomerDto storePractise(CustomerDto customerDTO) throws Exception {
        ArrayList<String> regelsUitBestand = new ArrayList<>();
        ArrayList<String> bsnLijst = new ArrayList<>();
        File bsnBestand = new File("src/main/resources/oefenBsn.csv");
        try {
            Scanner invoer = new Scanner(bsnBestand);
            while (invoer.hasNextLine()) {
                regelsUitBestand.add(invoer.nextLine());
            }
        } catch (FileNotFoundException nietGevonden) {
            System.out.println("Het bestand is niet gevonden.");
        }
        if (regelsUitBestand.size() > 0) {

            for (int i = 0; i < regelsUitBestand.size(); i++) {
                bsnLijst.add(regelsUitBestand.get(i));
            }
        }

        ArrayList<String> regelsUitBestandAdres = new ArrayList<>();
        File adresBestand = new File("src/main/resources/oefenAdres1.csv");
        try {
            Scanner invoer = new Scanner(adresBestand);
            while (invoer.hasNextLine()) {
                regelsUitBestandAdres.add(invoer.nextLine());
            }
        } catch (FileNotFoundException nietGevonden) {
            System.out.println("Het bestand is niet gevonden.");
        }
        ArrayList<Address> adressen = new ArrayList<>();
        if (regelsUitBestandAdres.size() > 0) {

            for (int i = 0; i < regelsUitBestandAdres.size(); i++) {
                String[] regelArray = regelsUitBestandAdres.get(i).split(";");
                String postcodeCijfers = regelArray[0];
                String postcodeLetters = regelArray[1];
                String postcode = postcodeCijfers + postcodeLetters;
                int huisnummer = Integer.parseInt(regelArray[2]);
                String plaatsnaam = regelArray[3];
                String straatnaam = regelArray[4];
                Address adres = new Address(straatnaam, huisnummer, "", postcode, plaatsnaam);
                if (addressService.storeAddress(adres)!=0){
                    int addressId = addressService.storeAddress(adres);
                    adres.setIdAddress(addressId);
                    adressen.add(adres);
                }


            }
        }
        //System.out.println(bsnLijst);
        String bsn1 = bsnLijst.get(8);
        System.out.println(adressen.get(0));
        Random rng = new Random();
        CustomerDto customerDto = new CustomerDto();
        //System.out.println(customerService.isValidBsn("699920036"));
        for (int i=0;i<100;i++){
            Address adres = adressen.get(0);
            String voornaam = firstNames[rng.nextInt(firstNames.length)];
            String achternaam = lastNames[rng.nextInt(lastNames.length)];
            customerDto.setAddress(adres);
            customerDto.setFirstName(voornaam);
            customerDto.setNamePrefix("");
            customerDto.setLastName(achternaam);
            customerDto.setDob(LocalDate.parse("2000-01-01"));
            customerDto.setBsn(bsnLijst.get(i));
            customerDto.setTelephone("0610066586");
            String email = "gast" + i + "@yahoo.com";
            customerDto.setEmail(email);
            customerDto.setPassword("ValidPassword");
            customerService.storeOne(customerDTO);
//            if (customerService.isValidBsn(bsnLijst.get(i))){
//                customerService.storeOne(customerDto);
//            }

        }


            return customerDto;



    }
}
