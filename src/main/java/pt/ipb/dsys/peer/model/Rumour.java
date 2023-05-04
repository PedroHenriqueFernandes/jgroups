package pt.ipb.dsys.peer.model;

import java.io.Serializable;

public class Rumour implements Serializable {

    private int told;

    private String rumour;

    public Rumour(String rumour) {
        this.rumour = rumour;
        told = 0;
    }

    public String getRumour() {
        return rumour;
    }

    public int told() {
        return told;
    }

    public void increaseRumour() {
        told++;
    }

}
