import java.util.Stack;

public class OperacionesMatematicas {

    private static int contadorTemporales = 1;
     public static String crearOperaciones(String operacion) {
        Stack<String> temporales = new Stack<>();
        Stack<Character> operadores = new Stack<>();

        StringBuilder operacionesOrdenadas = new StringBuilder();

        for (int i = 0; i < operacion.length(); i++) {
            char token = operacion.charAt(i);

            if (Character.isDigit(token)) {
                StringBuilder numero = new StringBuilder();
                while (i < operacion.length() && (Character.isDigit(token) || token == '.')) {
                    numero.append(token);
                    i++;
                    if (i < operacion.length()) {
                        token = operacion.charAt(i);
                    } else {
                        break;
                    }
                }
                i--;
                temporales.push(numero.toString());
            } else if (token == '(') {
                operadores.push(token);
            } else if (token == ')') {
                while (operadores.peek() != '(') {
                    procesarOperador(operadores.pop(), temporales, operacionesOrdenadas);
                }
                operadores.pop(); // Sacar el '('
            } else if (token == 's') {
                // Si encontramos "sqrt", procesamos la expresión dentro de los paréntesis
                StringBuilder expresion = new StringBuilder();
                i += 4; // Saltar "sqrt("
                while (i < operacion.length() && operacion.charAt(i) != ')') {
                    expresion.append(operacion.charAt(i));
                    i++;
                }
                // Crear temporal para la expresión dentro de sqrt
                String temporal = "t" + contadorTemporales + " = sqrt(" + expresion.toString() + ")";
                temporales.push("t" + contadorTemporales);
                operacionesOrdenadas.append(temporal).append("\n");
                contadorTemporales++;
            } else if (token == '^') {
                operadores.push(token);
            } else if (token == '+' || token == '-' || token == '*' || token == '/') {
                while (!operadores.isEmpty() && operadores.peek() != '(' && precedencia(token) <= precedencia(operadores.peek())) {
                    procesarOperador(operadores.pop(), temporales, operacionesOrdenadas);
                }
                operadores.push(token);
            }
        }

        while (!operadores.isEmpty()) {
            procesarOperador(operadores.pop(), temporales, operacionesOrdenadas);
        }

        return operacionesOrdenadas.toString();
    }

    public static void procesarOperador(char operador, Stack<String> temporales, StringBuilder operacionesOrdenadas) {
        if (operador == '(' || operador == ')') {
            return;
        }
        if (operador == 's') {
            String numero = temporales.pop();
            String temporal = "t" + contadorTemporales + " = sqrt(" + numero + ")";
            temporales.push("t" + contadorTemporales);
            operacionesOrdenadas.append(temporal).append("\n");
            contadorTemporales++;
        } else if (operador == '^') {
            String operando2 = temporales.pop();
            String operando1 = temporales.pop();
            String temporal = "t" + contadorTemporales + " = " + operando1 + operador + operando2;
            temporales.push("t" + contadorTemporales);
            operacionesOrdenadas.append(temporal).append("\n");
            contadorTemporales++;
        } else {
            String operando2 = temporales.pop();
            String operando1 = temporales.pop();
            String temporal = "t" + contadorTemporales + " = " + operando1 + operador + operando2;
            temporales.push("t" + contadorTemporales);
            operacionesOrdenadas.append(temporal).append("\n");
            contadorTemporales++;
        }
    }

    public static int precedencia(char operador) {
        if (operador == '^') {
            return 4;
        } else if (operador == '*' || operador == '/') {
            return 3;
        } else if (operador == '+' || operador == '-') {
            return 2;
        }
        return -1;
    }
}
