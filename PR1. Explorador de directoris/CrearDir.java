import java.io.File;

public class CrearDir {        
        public static void main (String[] args) {

            File dir = new File("./prueba");
            File archivo1 = new File(dir, "archivo1.txt");
            File archivo2 = new File(dir, "archivo2.txt");

            try { 
                archivo1.createNewFile();
                archivo2.createNewFile();
            } catch (Exception e) {
                System.out.println("Error al crear los archivos: " + e.getMessage());
            }


            if (dir.exists()) {
                System.out.println("La carpeta ya existe: " + dir.getName());
            } else if (dir.mkdir()) {
                System.out.println("Carpeta creada exitosamente: " + dir.getName());
            } else {
                System.out.println("No se pudo crear la carpeta: " + dir.getName());
            }
        }
}
