import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    
    public static String[] dividirSeudocodigo(String seudocodigo) {
        return seudocodigo.split("\\r?\\n");
    }

    public static String leerSeudocodigoDesdeArchivo(String rutaArchivo) throws IOException {
        StringBuilder contenido = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
        }
        return contenido.toString();
    }

    public static void identificarEstrucuras(String[] lineas) {
        for (String linea : lineas) {
            if (linea.matches(".*\\bSi\\b.*")) {
                System.out.println("Estructura de control 'if' encontrada en la línea: " + linea);
            } else if (linea.matches(".*\\bPara\\b.*")) {
                System.out.println("Estructura de control 'for' encontrada en la línea: " + linea);
            } else if (linea.matches(".*\\bHacer\\s*\\{\\b.*")) {
                System.out.println("Estructura de control 'do-while' encontrada en la línea: " + linea);
            } else if (linea.matches(".*\\bHasta Que\\b.*")) {
                System.out.println("Estructura de control 'while' encontrada en la línea: " + linea);
            } else if (linea.matches(".*\\bLeer\\b.*")) {
                System.out.println("Operación de entrada 'Leer' encontrada en la línea: " + linea);
            } else if (linea.matches(".*\\bEscribir\\b.*")) {
                System.out.println("Operación de salida 'Escribir' encontrada en la línea: " + linea);
            } else if (linea.matches(".*\\bDefinir\\b.*")) {
                System.out.println("Variable encontrada 'Definir' encontrada en la línea: " + linea);
            } else if (linea.matches(".*\\b\\+\\b.*|.*\\b\\-\\b.*|.*\\b\\*\\b.*|.*\\b\\/\\b.*|.*\\b\\%\\b.*|.*\\b\\^\\b.*|.*\\bsqrt\\b.*")) {
              //  String operacionesOrdenadas = crearOperaciones(linea);
               // System.out.println("Operaciones Aritméticas Ordenadas:");
               // System.out.println(operacionesOrdenadas);
            }
        }
    }


    public static void main(String[] args) throws IOException {
        String rutaArchivo = "I:\\cicloif.txt";

        try {
            String seudocodigo = leerSeudocodigoDesdeArchivo(rutaArchivo);
            String[] lineas = dividirSeudocodigo(seudocodigo);

          /*   for (String linea : lineas) {
                System.out.println("Linea: " + linea);
            }*/

            // Identificar estructuras de control y llamar a los métodos correspondientes
          //  System.out.println(EstructurasControl.procesarSi(seudocodigo, rutaArchivo, seudocodigo));
            // EstructurasControl.procesarSi(condicion, cuerpo, cuerpoSino);
            // OperacionesMatematicas.crearOperaciones(linea);
        //  System.out.println(OperacionesMatematicas.crearOperaciones(seudocodigo));
            
            // OperacionesLogicas.crearOperacionesLogicas(linea);
         //   System.out.println(OperacionesLogicas.crearOperacionesLogicas(seudocodigo));
            
            String codigoDeTresDirecciones = cicloIf.generarCodigoTresDirecciones(lineas);
            System.out.println(codigoDeTresDirecciones);


        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }

   
}
