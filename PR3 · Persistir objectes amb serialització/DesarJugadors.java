import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DesarJugadors {

    public static void main(String[] args) {
        ArrayList<Jugador> jugadors = new ArrayList<>();
        jugadors.add(new Jugador("Iker Casillas", "Porter", 1, 0, "Móstoles"));
        jugadors.add(new Jugador("Carles Puyol", "Defensa", 5, 1, "La Pobla de Segur"));
        jugadors.add(new Jugador("Gerard Piqué", "Defensa", 3, 0, "Barcelona"));
        jugadors.add(new Jugador("Xavi Hernández", "Migcampista", 8, 0, "Terrassa"));
        jugadors.add(new Jugador("Andrés Iniesta", "Migcampista", 6, 2, "Fuentealbilla"));
        jugadors.add(new Jugador("David Villa", "Davanter", 7, 5, "Tuilla"));

        File fitxer1 = new File("jugadors_un_a_un.dat");
        File fitxer2 = new File("jugadors_llista.dat");

        // Opció 1: objecte a objecte
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fitxer1))) {
            for (Jugador j : jugadors) {
                oos.writeObject(j);
            }
        } catch (IOException e) {
            System.err.println("Error !!! " + e.getMessage());
        }

        // Opció 2: tot l'ArrayList d'un sol cop
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fitxer2))) {
            oos.writeObject(jugadors);
        } catch (IOException e) {
            System.err.println("Error !!! " + e.getMessage());
        }

        System.out.println("Jugadors desats.");
        System.out.println(fitxer1.getName() + ": " + fitxer1.length() + " bytes");
        System.out.println(fitxer2.getName() + ": " + fitxer2.length() + " bytes");
    }
}
