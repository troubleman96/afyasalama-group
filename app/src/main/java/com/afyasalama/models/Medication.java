package com.afyasalama.models;

public class Medication {
    private int id;
    private String name;
    private String dosage;
    private String time; // Format: HH:mm

    public Medication() {}

    public Medication(int id, String name, String dosage, String time) {
        this.id = id;
        this.name = name;
        this.dosage = dosage;
        this.time = time;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
