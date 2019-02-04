package org.jaku8ka.petcompanion;

import java.util.Date;

public class Pet {

    private String name;
    private int type;
    private String dateOfBirth;
    private int sex;
    private String species;
    private String color;
    private String parasites;
    private String vaccination;
    private int nextPar;
    private int nextVac;
    private Date nextParasites;
    private Date nextVaccination;
    private String nextParasitesString;
    private String nextVaccinationString;
    private boolean selected;

    public boolean getSelected(){
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getNextParasitesString() {
        return nextParasitesString;
    }

    public void setNextParasitesString(String nextParasitesString) {
        this.nextParasitesString = nextParasitesString;
    }

    public String getNextVaccinationString() {
        return nextVaccinationString;
    }

    public void setNextVaccinationString(String nextVaccinationString) {
        this.nextVaccinationString = nextVaccinationString;
    }

    private String objectId;
    private Date created;
    private Date updated;
    private String userEmail;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getParasites() {
        return parasites;
    }

    public void setParasites(String parasites) {
        this.parasites = parasites;
    }

    public String getVaccination() {
        return vaccination;
    }

    public void setVaccination(String vaccination) {
        this.vaccination = vaccination;
    }

    public int getNextPar() {
        return nextPar;
    }

    public void setNextPar(int nextPar) {
        this.nextPar = nextPar;
    }

    public int getNextVac() {
        return nextVac;
    }

    public void setNextVac(int nextVac) {
        this.nextVac = nextVac;
    }

    public Date getNextParasites() {
        return nextParasites;
    }

    public void setNextParasites(Date nextParasites) {
        this.nextParasites = nextParasites;
    }

    public Date getNextVaccination() {
        return nextVaccination;
    }

    public void setNextVaccination(Date nextVaccination) {
        this.nextVaccination = nextVaccination;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
