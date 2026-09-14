import java.io.File;

public class ExploradorDirectoris {
	public static void main(String[] args) {
		if (args.length == 0) { System.out.println("Ús: java ExploradorDirectoris <ruta>"); return; }
		File carpeta = new File(args[0]);
		if (!carpeta.exists()) { System.out.println("La ruta no existeix: " + args[0]); return; }
		if (!carpeta.isDirectory()) { System.out.println("La ruta existeix però no és un directori"); return; }

		System.out.println("Informe de: " + carpeta.getAbsolutePath());
		System.out.println("--------------------------------------------------");
		long[] resum = {0,0};
		explorar(carpeta,0,resum);
		System.out.println("--------------------------------------------------");
		System.out.println("Total d'elements: " + resum[0]);
		System.out.println("Total bytes: " + resum[1] + " bytes");
	}

	static void explorar(File carpeta, int nivell, long[] resum) {
		File[] contingut = carpeta.listFiles();
		if (contingut == null) { System.out.println("No es pot llistar: " + carpeta.getAbsolutePath()); return; }
		String sagnat = "  ".repeat(nivell);
		for (File element : contingut) {
			String tipus = element.isDirectory()? "CARPETA" : "FITXER";
			long mida = element.isFile()? element.length() : 0;
			boolean escriptura = element.canWrite();
			System.out.printf("%s%-30s %-8s %10d bytes  escriptura: %s%n", sagnat, element.getName(), tipus, mida, escriptura? "si" : "no");
			resum[0]++;
			resum[1] += mida; // mida ja és 0 per a carpetes, no cal comprovar
			if (element.isDirectory()) explorar(element, nivell + 1, resum);
		}
	}
}
