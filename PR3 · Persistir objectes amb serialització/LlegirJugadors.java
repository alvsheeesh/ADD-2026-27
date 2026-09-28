import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class LlegirJugadors {

    public static void main(String[] args) {
        File fitxer1 = new File("jugadors_un_a_un.dat");
        File fitxer2 = new File("jugadors_llista.dat");

        System.out.println("--- Objecte a objecte ---");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fitxer1))) {
            while (true) {
                Jugador j = (Jugador) ois.readObject();
                System.out.println("Nom: " + j.getNom() + ", Posició: " + j.getPosicio() + ", Dorsal: " + j.getDorsal()
                        + ", Gols: " + j.getGols() + ", Ciutat: " + j.getCiutat());
            }
        } catch (EOFException e) {
            System.out.println("Fi del fitxer.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error !!! " + e);
        }

        System.out.println("--- ArrayList sencer ---");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fitxer2))) {
            ArrayList<Jugador> jugadors = (ArrayList<Jugador>) ois.readObject();
            for (Jugador j : jugadors) {
                System.out.println("Nom: " + j.getNom() + ", Posició: " + j.getPosicio() + ", Dorsal: " + j.getDorsal()
                        + ", Gols: " + j.getGols() + ", Ciutat: " + j.getCiutat());
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error !!! " + e);
        }
    }
}
