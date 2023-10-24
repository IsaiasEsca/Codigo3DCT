import java.util.Stack;

public class EstructurasControl {
    private static int contadorEtiquetas = 1;

     public static String procesarSi(String condicion, String cuerpo, String cuerpoSino) {
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
     public static String generarTemporal() {
        return "t" + contadorTemporales++;
    }
    private static int contadorTemporales = 1;
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
    public static String generarEtiqueta() {
        return "L" + contadorEtiquetas++;
    }
}
