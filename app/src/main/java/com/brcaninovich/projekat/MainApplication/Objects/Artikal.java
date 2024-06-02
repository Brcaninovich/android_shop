package com.brcaninovich.projekat.MainApplication.Objects;

import java.util.List;

public class Artikal {

    private String id;
    private String nazivArtikla;
    private List<String> slike;
    private String opisArtikla;
    private String cijena;
    private String kategorija;
    private String stanje;
    private String lokacija;
    private String dodaoKorisnik;

    public Artikal() {
        // Default constructor required for calls to DataSnapshot.getValue(Artikal.class)
    }

    public Artikal(String id, String nazivArtikla, List<String> slike, String opisArtikla, String cijena, String kategorija, String stanje, String lokacija, String dodaoKorisnik) {
        this.id = id;
        this.nazivArtikla = nazivArtikla;
        this.slike = slike;
        this.opisArtikla = opisArtikla;
        this.cijena = cijena;
        this.kategorija = kategorija;
        this.stanje = stanje;
        this.lokacija = lokacija;
        this.dodaoKorisnik = dodaoKorisnik;
    }

    public String getDodaoKorisnik() {
        return dodaoKorisnik;
    }

    public void setDodaoKorisnik(String dodaoKorisnik) {
        this.dodaoKorisnik = dodaoKorisnik;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNazivArtikla() {
        return nazivArtikla;
    }

    public void setNazivArtikla(String nazivArtikla) {
        this.nazivArtikla = nazivArtikla;
    }

    public List<String> getSlike() {
        return slike;
    }

    public void setSlike(List<String> slike) {
        this.slike = slike;
    }

    public String getOpisArtikla() {
        return opisArtikla;
    }

    public void setOpisArtikla(String opisArtikla) {
        this.opisArtikla = opisArtikla;
    }

    public String getCijena() {
        return cijena;
    }

    public void setCijena(String cijena) {
        this.cijena = cijena;
    }

    public String getKategorija() {
        return kategorija;
    }

    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }

    public String getStanje() {
        return stanje;
    }

    public void setStanje(String stanje) {
        this.stanje = stanje;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }
}
