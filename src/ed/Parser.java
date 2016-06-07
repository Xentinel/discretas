package ed;

/**
 *
 * @author Sebastian Navarrete
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Parser {
    
    private static final String[] NEGACION = {"no ", "no ocurre que ",
        "no es cierto que ", "no es el caso que ", "es falso que ", "ni "};
    private static final String[] CONJUNCION = {" y "," y,", " pero ", " aunque ",
        " sin embargo ", " no obstante ", " a pesar de "};
    private static final String[] DISYUNCION = {" o ", " o,", " o bien ",
        " a menos que "};
    private static final String[] CONDICIONAL = {"si ", "sólo si ", "solo si ",
        " solo si ", " sólo si ", " es suficiente para ", " si ",
        " es necesario para ", " entonces "};
    private static final String[] BICONDICIONAL = {" si y sólo si ",
        " si y solo si ", " es necesario y suficiente para "};
    
    private static final String[] ATOM = {"A", "B", "C", "D", "E", "F", "G",
        "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
        "U", "V", "W", "X", "Y", "Z"};
    //Los conectivos están en orden de prioridad descendente
    private static final String[] CONECTIVOS = {"7", "^", "v", "->", "<->"};
    
    private int n = 0;
    private String[] atomicas = null;
    private String[] atomicasTemp = null;
    private String salida = "";
    private String temp = "";
    private String[] arrayTemp = null;
    private String[] expresiones = null;
    private boolean conclusion = false;
    
    public String parse(String entrada){
        String[] hipotesis = null;
        salida = "";
        int i = 0;
        conclusion = false;
        
        entrada = entrada.toLowerCase();
        
        if(entrada.contains("por lo tanto,")){
            conclusion = true;
            entrada = entrada.replace("por lo tanto, ", "");
        }
        
        if(entrada.contains(".")){
            hipotesis = entrada.split("\\.");
        } else hipotesis = entrada.split("\n");
        
        salida = salida + "Hipótesis:\n";
        
        for(i=0;i<hipotesis.length; i++){
            if(hipotesis[i].startsWith(" ") || hipotesis[i].endsWith(" "))
                hipotesis[i] = hipotesis[i].trim();
            if(conclusion == true && i == hipotesis.length - 1)
                salida = salida + "\nConclusión\nC: " + hipotesis[i] + "\n";
            else
                salida = salida + "H" + (i+1) + ": " + hipotesis[i] + "\n";
        }
        
        salida = salida + "\nAtómicas:\n";
        obtieneAtomicas(entrada);
        
        salida = salida + "\nExpresiones:\n";
        generaExpresiones(hipotesis);
        
        return salida;
    }
    
    private void obtieneAtomicas(String hipotesis) {
        int i = 0;
        
        for(i=0;i<6;i++){//Negaciones
            if(hipotesis.contains(NEGACION[i]))
                hipotesis = hipotesis.replace(NEGACION[i], "");
        }
        for(i=0;i<7;i++){//Conjunciones
            if(hipotesis.contains(CONJUNCION[i]))
                hipotesis = hipotesis.replace(CONJUNCION[i], "@");
        }
        for(i=0;i<4;i++){//Disyunciones
            if(hipotesis.contains(DISYUNCION[i]))
                hipotesis = hipotesis.replace(DISYUNCION[i], "@");
        }
        for(i=0;i<3;i++){//Condicional inicio (si)
            if(hipotesis.contains(CONDICIONAL[i]))
                hipotesis = hipotesis.replace(CONDICIONAL[i], "");
        }
        for(i=3;i<9;i++){//Condicional intermedio (entonces)
            if(hipotesis.contains(CONDICIONAL[i]))
                hipotesis = hipotesis.replace(CONDICIONAL[i], "@");
        }
        for(i=0;i<3;i++){//Bicondicional
            if(hipotesis.contains(BICONDICIONAL[i]))
                hipotesis = hipotesis.replace(BICONDICIONAL[i], "@");
        }
        if(hipotesis.contains(",")) {//Comas
            hipotesis = hipotesis.replace(",", "@");
        }
        if(hipotesis.contains(".")) {//Puntos
            hipotesis = hipotesis.replace(".", "@");
        }
        if(hipotesis.contains("@@")) {//Operadores consecutivos
            hipotesis = hipotesis.replace("@@", "@");
        }
        atomicasTemp = hipotesis.split("@");//Separa atomicas
        
        for(i=0;i<atomicasTemp.length; i++){//Limpia espacios en blanco al inicio y final
            if(atomicasTemp[i].startsWith(" ") || atomicasTemp[i].endsWith(" "))
                atomicasTemp[i] = atomicasTemp[i].trim();
        }
        
        //Limpia entradas duplicadas, respeta orden
        Set<String> set = new LinkedHashSet<>(Arrays.asList(atomicasTemp));
        atomicas = set.toArray(new String[set.size()]); //Nuevo arreglo sin atomicas repetidas
        
        for(i=0; i<atomicas.length; i++){
            salida = salida + ATOM[i] + ": " + atomicas[i] + "\n";
        }
    }
    
    private void generaExpresiones(String[] hipotesis) {
        int i = 0, j = 0;
        
        for(i=0; i<hipotesis.length; i++){//Avanza entre las hipótesis
            for(j=0;j<6;j++){//Negaciones
                if(hipotesis[i].contains(NEGACION[j]))
                    hipotesis[i] = hipotesis[i].replace(NEGACION[j], "7");
            }
            for(j=0;j<6;j++){//Conjunciones
            if(hipotesis[i].contains(CONJUNCION[j]))
                    hipotesis[i] = hipotesis[i].replace(CONJUNCION[j], " ^ ");
            }
            for(j=0;j<3;j++){//Disyunciones
                if(hipotesis[i].contains(DISYUNCION[j]))
                    hipotesis[i] = hipotesis[i].replace(DISYUNCION[j], " v ");
            }
            for(j=0;j<3;j++){//Condicional inicio (si)
                if(hipotesis[i].contains(CONDICIONAL[j]))
                    hipotesis[i] = hipotesis[i].replace(CONDICIONAL[j], "");
            }
            for(j=3;j<9;j++){//Condicional intermedio (entonces)
                if(hipotesis[i].contains(CONDICIONAL[j]))
                    hipotesis[i] = hipotesis[i].replace(CONDICIONAL[j], " -> ");
            }
            for(j=0;j<3;j++){//Bicondicional
                if(hipotesis[i].contains(BICONDICIONAL[j]))
                    hipotesis[i] = hipotesis[i].replace(BICONDICIONAL[j], " <-> ");
            }
            if(hipotesis[i].contains(","))
                hipotesis[i] = hipotesis[i].replace(",", "");
        }
        //Reemplaza proposiciones con literales (atomicas)
        for(i=0; i<hipotesis.length; i++) {
            for(j=0; j<atomicas.length; j++) {
                if(hipotesis[i].contains(atomicas[j]))
                    hipotesis[i] = hipotesis[i].replace(atomicas[j], ATOM[j]);
            }
        }
        
        expresiones = hipotesis;
        
        for(i=0; i<expresiones.length; i++){
            if(conclusion == true && i == expresiones.length - 1)
                salida = salida + "\nC: " + expresiones[i] + "\n";
            else
                salida = salida + "H" + (i+1) + ": " + expresiones[i] + "\n";
        }
        
    }

    String romperCadena(String salida) {
        int salidaLength = salida.length();
        int H1 = salida.lastIndexOf("H1");
        salida = salida.substring(H1,salidaLength);
        salida = salida.replace("H1:", " ");
        salida = salida.replace(":", "^");
        for (int i = 1; i < 10; i++) {
            if(salida.contains("H"+i)) {
            salida = salida.replace("H"+i, "");
            }
        }
        salida = salida.replace("\n","");
        System.out.println("Cadena rota: " + salida);
        return salida;
    }
        
}
