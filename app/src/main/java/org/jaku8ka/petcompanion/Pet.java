package org.jaku8ka.petcompanion;

import java.util.Date;

public class Pet {

    private String name;
    private int type;
    private Date dateOfBirth;
    private int sex;
    private String species;
    private String color;
    private Date parasites;
    private Date vaccination;
    private int nextOdc;
    private int nextVac;
    private Date nextParasites;

    public Pet() {
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
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

    public Date getParasites() {
        return parasites;
    }

    public void setParasites(Date parasites) {
        this.parasites = parasites;
    }

    public Date getVaccination() {
        return vaccination;
    }

    public void setVaccination(Date vaccination) {
        this.vaccination = vaccination;
    }

    public int getNextOdc() {
        return nextOdc;
    }

    public void setNextOdc(int nextOdc) {
        this.nextOdc = nextOdc;
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

    private Date nextVaccination;
}
