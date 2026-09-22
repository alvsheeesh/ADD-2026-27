import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class Agenda {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcio = -1;

        while (opcio != 0) {
            System.out.println("\n===== AGENDA =====");
            System.out.println("1. Afegir contacte");
            System.out.println("2. Llistar contactes");
            System.out.println("3. Cercar per nom");
            System.out.println("0. Sortir");
            System.out.print("Opcio: ");

            try {
                opcio = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                opcio = -1;
            }

            switch (opcio) {
                case 1:
                    afegir(sc);
                    break;
                case 2:
                    llistar();
                    break;
                case 3:
                    cercar(sc);
                    break;
                case 0:
                    System.out.println("Adéu!");
                    break;
                default:
                    System.out.println("Opcio no vàlida.");
            }
        }
        sc.close();
    }

    public static void afegir(Scanner sc) {
        System.out.print("Nom: ");
        String nom = sc.nextLine();
        System.out.print("Telèfon: ");
        String telefon = sc.nextLine();
        System.out.print("Correu: ");
        String correu = sc.nextLine();

        try {
            FileWriter fw = new FileWriter("contactes.txt", true);
            fw.write(nom + ";" + telefon + ";" + correu + "\n");
            fw.close();
            System.out.println("Contacte afegit.");
        } catch (Exception e) {
            System.out.println("Error en escriure al fitxer: " + e.getMessage());
        }
    }

    public static void llistar() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("contactes.txt"));

            String linia;
            while ((linia = br.readLine()) != null) {
                String[] camps = linia.split(";");
                if (camps.length == 3) {
                    System.out.println("- " + camps[0] + " | Tel: " + camps[1] + " | " + camps[2]);
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("Encara no hi ha contactes. Afegeix-ne un primer.");
        } catch (Exception e) {
            System.out.println("Error en llegir el fitxer: " + e.getMessage());
        }
    }

    public static void cercar(Scanner sc) {
        System.out.print("Nom a cercar: ");
        String cerca = sc.nextLine().toLowerCase();

        try {
            BufferedReader br = new BufferedReader(new FileReader("contactes.txt"));

            String linia;
            boolean trobat = false;
            while ((linia = br.readLine()) != null) {
                String[] camps = linia.split(";");
                if (camps.length == 3 && camps[0].toLowerCase().contains(cerca)) {
                    System.out.println("- " + camps[0] + " | Tel: " + camps[1] + " | " + camps[2]);
                    trobat = true;
                }
            }
            br.close();

            if (!trobat) {
                System.out.println("No s'ha trobat cap contacte amb aquest nom.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Encara no hi ha contactes. Afegeix-ne un primer.");
        } catch (Exception e) {
            System.out.println("Error en llegir el fitxer: " + e.getMessage());
        }
    }
}