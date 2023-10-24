import java.util.Stack;

public class OperacionesLogicas {
    private static int contadorTemporales = 1;
    public static String generarTemporal() {
        return "t" + contadorTemporales++;
    }
    public static String crearOperacionesLogicas(String operacion) {
        Stack<Character> operadores = new Stack<>();
        Stack<String> operandos = new Stack<>();
        StringBuilder codigoTresDirecciones = new StringBuilder();
        String[] tokens = operacion.split("\\s+");
    
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
}
