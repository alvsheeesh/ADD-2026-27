import java.io.Serializable;

public class Jugador implements Serializable {
    private String nom;
    private String posicio;
    private int dorsal;
    private int gols;
    private String ciutat; // atribut nou (pas 5)

    public Jugador(String nom, String posicio, int dorsal, int gols, String ciutat) {
        this.nom = nom;
        this.posicio = posicio;
        this.dorsal = dorsal;
        this.gols = gols;
        this.ciutat = ciutat;
    }

    public String getNom() {
        return nom;
    }

    public String getPosicio() {
        return posicio;
    }

    public int getDorsal() {
        return dorsal;
    }

    public int getGols() {
        return gols;
    }

    public String getCiutat() {
        return ciutat;
    }
}
