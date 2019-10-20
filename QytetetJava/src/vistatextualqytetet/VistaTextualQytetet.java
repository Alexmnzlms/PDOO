////////////////////////////////////////////////////////////////////////////////
// Autor: Alejandro Manzanares Lemus
// 2º GII A1
////////////////////////////////////////////////////////////////////////////////
package vistatextualqytetet;

import controladorqytetet.*;
import java.util.Scanner;
import java.util.ArrayList;
import modeloqytetet.Qytetet;

        
public class VistaTextualQytetet {
    ControladorQytetet controlador  = ControladorQytetet.getInstance();
    private static final Scanner in = new Scanner (System.in);

    public VistaTextualQytetet() {
    }
    
    public ArrayList<String> obtenerNombreJugadores(){
        System.out.println("Introduce el numero de jugadores: ");
        ArrayList<String> numero_jugadores_valido = new ArrayList<>();
        for(int i = 2; i <= Qytetet.MAX_JUGADORES; i++){
            numero_jugadores_valido.add(Integer.toString(i));
        }
        
        int num_jugadores = Integer.valueOf(leerValorCorrecto(numero_jugadores_valido));
        
        ArrayList<String> nombres = new ArrayList<>();
        for(int i = 0; i < num_jugadores; i++){
            System.out.println("Introduce el nombre del jugador: ");
            nombres.add(in.nextLine());
        }
        return nombres;
    }   
    public Integer elegirOperacion(){
        ArrayList<Integer> aux = controlador.obtenerOperacionesJuegoValidas();
        ArrayList<String> aux_str = new ArrayList<>();
        for(Integer numero: aux){
            aux_str.add(Integer.toString(numero));
        }
        
        return Integer.valueOf(leerValorCorrecto(aux_str));
    }
    public Integer elegirCasilla(int opcionMenu){
        ArrayList<Integer> aux;
        aux = controlador.obtenerCasillasValidas(opcionMenu);
        if(aux.isEmpty()){
            return -1;
        }
        else{
            ArrayList<String> aux_str = new ArrayList<String>();
            for(Integer numero: aux){
                aux_str.add(Integer.toString(numero));
            }
            return Integer.valueOf(leerValorCorrecto(aux_str));
        }
    }
    public String leerValorCorrecto(ArrayList<String> valoresCorrectos){
        String entrada;
        do{
            entrada = in.nextLine();
        }while(!valoresCorrectos.contains(entrada));
        
        return entrada;
    }    
    
    public void imprimirOperacionesJuegoValidas(){
        ArrayList<Integer> aux = controlador.obtenerOperacionesJuegoValidas();
        for(int i: aux){
            System.out.print(i);
            System.out.print(" ");
            System.out.println(OpcionMenu.values()[i]);
        }
    }
    
    public static void main(String args[]){
        VistaTextualQytetet ui = new VistaTextualQytetet();
        ui.controlador.setNombreJugadores(ui.obtenerNombreJugadores());
        int operacionElegida, casillaElegida = 0;
        boolean necesitaElegirCasilla;
        
        do {
            ui.imprimirOperacionesJuegoValidas();
            operacionElegida = ui.elegirOperacion();
            necesitaElegirCasilla = ui.controlador.necesitaElegirCasilla(operacionElegida);
            if (necesitaElegirCasilla){
                System.out.println("Por favor, elige la casilla:\n");
                casillaElegida = ui.elegirCasilla(operacionElegida);
            }
            if (!necesitaElegirCasilla || casillaElegida >= 0)
            System.out.println(ui.controlador.realizarOperacion(operacionElegida,
            casillaElegida));
        } while(true);
    }
}


