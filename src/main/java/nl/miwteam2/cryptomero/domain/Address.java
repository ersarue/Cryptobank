package nl.miwteam2.cryptomero.domain;

import java.util.Objects;

/**
* @author Petra Coenen
* @version 1.0
*/

public class Address {
    private int idAddress;
    private String streetName;
    private int houseNo;
    private String houseAdd;
    private String postalCode;
    private String city;

    public Address(int idAddress, String streetName, int houseNo, String houseAdd, String postalCode, String city) {
        this.idAddress = idAddress;
        this.streetName = streetName;
        this.houseNo = houseNo;
        this.houseAdd = houseAdd;
        this.postalCode = postalCode;
        this.city = city;
    }

    public Address(int idAddress, String streetName, int houseNo, String postalCode, String city) {
        this(idAddress, streetName, houseNo, null, postalCode, city);
    }

    public Address() { }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return idAddress == address.idAddress && houseNo == address.houseNo && streetName.equals(address.streetName) && Objects.equals(houseAdd, address.houseAdd) && postalCode.equals(address.postalCode) && city.equals(address.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAddress, streetName, houseNo, houseAdd, postalCode, city);
    }

    @Override
    public String toString() {
        return "Address{" +
                "idAdres=" + idAddress +
                ", straatnaam='" + streetName + '\'' +
                ", huisnr=" + houseNo +
                ", toevoeging='" + houseAdd + '\'' +
                ", postcode='" + postalCode + '\'' +
                ", woonplaats='" + city + '\'' +
                '}';
    }

    public int getIdAddress() {
        return idAddress;
    }

    public String getStreetName() {
        return streetName;
    }

    public int getHouseNo() {
        return houseNo;
    }

    public String getHouseAdd() {
        return houseAdd;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }
}