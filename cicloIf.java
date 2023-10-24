import java.util.Stack;

public class cicloIf {

    public static String generarOperacionLeer(String variable){
        return variable + " = Leer";
    }

    public static String generarOperacionEscribir(String mensaje){
        return mensaje + " Escribir \"" + mensaje + "\"";
    }

    public static String generarEtiqueta() {
        return "L" + contadorEtiquetas++;
    }

     public static String generarTemporal() {
        return "t" + contadorTemporal++;
    }
    
    private static int contadorTemporal = 1;
    private static int contadorEtiquetas = 1;

    public static String procesarSi(String condicion, String cuerpo, String cuerpoSino){
        String codigo = "";
        String etiquetaVerdadero = generarEtiqueta();
        String etiquetaFalso = generarEtiqueta();
        String etiquetaFin = generarEtiqueta();

        String resultadoCondicion = procesarCondicion(condicion);
        codigo += "if-t " + resultadoCondicion + " goto " + etiquetaVerdadero + "\n";
        codigo += "goto " + etiquetaFalso + "\n";
        codigo += etiquetaVerdadero + ":\n";
        // Procesar el cuerpo del 'Si'
        codigo += cuerpo + "\n";
        codigo += "goto " + etiquetaFin + "\n";
        codigo += etiquetaFalso + ":\n";
        // Procesar el cuerpo del 'Sino'
        codigo += cuerpoSino + "\n";
        codigo += etiquetaFin + ":\n";

        return codigo;
    }

    public static String procesarCondicion(String condicion) {
        String[] tokens = condicion.split("\\s+");
        Stack<String> operandos = new Stack<>();
        Stack<Character> operadores = new Stack<>();
        StringBuilder codigoTresDirecciones = new StringBuilder();
    
        for (String token : tokens) {
            if (token.matches("\\bY\\b|\\bO\\b|\\bNO\\b")) {
                // Si el token es un operador lógico (AND, OR, NOT)
                char operador = token.charAt(0);
                while (!operadores.isEmpty() && esOperadorLogico(operadores.peek()) && precedenciaOperadorLogico(operador) <= precedenciaOperadorLogico(operadores.peek())) {
                    // Procesar operadores lógicos con mayor o igual precedencia
                    char operadorAnterior = operadores.pop();
                    String operando2 = operandos.pop();
                    String operando1 = operandos.pop();
                    String temporal = generarTemporal();
                    codigoTresDirecciones.append(temporal).append(" = ").append(operando1).append(" ").append(operadorAnterior).append(" ").append(operando2).append("\n");
                    operandos.push(temporal);
                }
                operadores.push(operador);
            } else {
                // Si el token es un operando
                operandos.push(token);
            }
        }
    
        while (!operadores.isEmpty()) {
            char operador = operadores.pop();
            String operando2 = operandos.pop();
            String operando1 = operandos.pop();
            String temporal = generarTemporal();
            codigoTresDirecciones.append(temporal).append(" = ").append(operando1).append(" ").append(operador).append(" ").append(operando2).append("\n");
            operandos.push(temporal);
        }
    
        return operandos.pop();  // El resultado final estará en el tope de la pila de operandos
    }

    public static boolean esOperadorLogico(char operador) {
        return operador == 'Y' || operador == 'O' || operador == 'N';
    }

    public static int precedenciaOperadorLogico(char operador) {
        if (operador == 'N') {
            return 3;
        } else if (operador == 'Y') {
            return 2;
        } else if (operador == 'O') {
            return 1;
        }
        return 0;  // Valor predeterminado para otros operadores
    }

    public static String generarCodigoTresDirecciones(String[] lineas) {
        StringBuilder codigoTresDirecciones = new StringBuilder();
    
        for (String linea : lineas) {
            if (linea.contains("Escribir")) {
                // Generar código de tres direcciones para operación de salida
                String mensaje = linea.substring(linea.indexOf("\"") + 1, linea.lastIndexOf("\""));
                codigoTresDirecciones.append(generarOperacionEscribir(mensaje)).append("\n");
            } else if (linea.contains("Leer")) {
                // Generar código de tres direcciones para operación de entrada
                String variable = linea.split(" ")[1];
                codigoTresDirecciones.append(generarOperacionLeer(variable)).append("\n");
            } else if (linea.contains("Si") && linea.contains("Entonces")) {
                // Verificar si la línea contiene tanto "Si" como "Entonces" antes de procesarla
                // Procesar estructura condicional 'Si'
                // Obtener la condición y los cuerpos del 'Si' y 'Sino'
                String condicion = linea.substring(linea.indexOf("Si") + 3, linea.indexOf("Entonces")).trim();
                String cuerpoSi = obtenerCuerpoSi(lineas);
                String cuerpoSino = obtenerCuerpoSino(lineas);
                // Generar código de tres direcciones para la estructura condicional 'Si'
                codigoTresDirecciones.append(procesarSi(condicion, cuerpoSi, cuerpoSino)).append("\n");
            }
        }
    
        return codigoTresDirecciones.toString();
    }
    

    public static String obtenerCuerpoSi(String[] lineas) {
        boolean enBloqueSi = false;
        StringBuilder cuerpoSi = new StringBuilder();
    
        for (String linea : lineas) {
            if (enBloqueSi) {
                // Verificar si la línea pertenece al bloque Si (por indentación o espacio inicial)
                if (linea.trim().isEmpty() || !linea.trim().startsWith("  ")) {
                    // La línea no está indentada, por lo tanto, no pertenece al bloque Si
                    break;
                }
                // La línea pertenece al bloque Si, agrégala al cuerpo
                cuerpoSi.append(linea).append("\n");
            } else if (linea.matches(".*\\bSi\\b.*")) {
                // Encontramos el inicio del bloque Si
                enBloqueSi = true;
                // Agrega la línea actual al cuerpo del Si
                cuerpoSi.append(linea).append("\n");
            }
        }
    
        return cuerpoSi.toString().trim();
    }

    public static String obtenerCuerpoSino(String[] lineas) {
        boolean enBloqueSino = false;
    boolean enBloqueSi = false; // Variable para evitar capturar el 'Sino' del bloque 'Si'
    StringBuilder cuerpoSino = new StringBuilder();

    for (String linea : lineas) {
        // Si ya estamos dentro de un bloque 'Si', evitamos capturar el 'Sino' del mismo bloque
        if (enBloqueSi) {
            if (linea.matches(".*\\bSino\\b.*")) {
                // Si encontramos 'Sino', marcamos que estamos dentro del bloque 'Sino'
                enBloqueSino = true;
                enBloqueSi = false; // Marcamos que no estamos más dentro del bloque 'Si'
                continue;
            } else if (linea.matches(".*\\bFinSi\\b.*")) {
                // Si encontramos 'FinSi', salimos del bloque 'Sino'
                break;
            }
        }

        // Si estamos dentro de un bloque 'Sino', agregamos las líneas al cuerpo
        if (enBloqueSino) {
            // Verificar si la línea pertenece al bloque 'Sino' (por indentación o espacio inicial)
            if (linea.trim().isEmpty() || !linea.trim().startsWith("  ")) {
                // La línea no está indentada, por lo tanto, no pertenece al bloque 'Sino'
                break;
            }
            // La línea pertenece al bloque 'Sino', agrégala al cuerpo
            cuerpoSino.append(linea).append("\n");
        } else if (linea.matches(".*\\bSino\\b.*")) {
            // Encontramos el inicio del bloque 'Sino'
            enBloqueSino = true;
            // Agrega la línea actual al cuerpo del 'Sino'
            cuerpoSino.append(linea).append("\n");
        }
    }

    return cuerpoSino.toString().trim();
    }
}
