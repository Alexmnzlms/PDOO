////////////////////////////////////////////////////////////////////////////////
// Autor: Alejandro Manzanares Lemus
// 2º GII A1
////////////////////////////////////////////////////////////////////////////////
package controladorqytetet;

import java.util.ArrayList;
import modeloqytetet.EstadoJuego;
import modeloqytetet.MetodoSalirCarcel;
import modeloqytetet.Qytetet;
import modeloqytetet.Jugador;


public class ControladorQytetet {
//SIGLETON
    private ArrayList<String> nombreJugadores = new ArrayList<String>();
    private Qytetet modelo = Qytetet.getInstance();

    private static final ControladorQytetet instance = new ControladorQytetet();
    private ControladorQytetet(){}
    public static ControladorQytetet getInstance(){
        return instance;
    }
    
    public void setNombreJugadores(ArrayList<String> nombreJugadores){
        this.nombreJugadores = nombreJugadores;
    }
    public ArrayList<Integer> obtenerOperacionesJuegoValidas(){
        ArrayList<Integer> operaciones = new ArrayList<>();
        EstadoJuego estado = modelo.getEstadoJuego();
        if( modelo.getJugadores().isEmpty()){
            operaciones.add(OpcionMenu.INICIARJUEGO.ordinal());
        }
        else{
            if(estado == EstadoJuego.ALGUNJUGADORENBANCAROTA){
                operaciones.add(OpcionMenu.OBTENERRANKING.ordinal());
            }
            else if(estado == EstadoJuego.JA_ENCARCELADO){
                operaciones.add(OpcionMenu.PASARTURNO.ordinal());
            }
            else if(estado == EstadoJuego.JA_PREPARADO){
                operaciones.add(OpcionMenu.JUGAR.ordinal());
            }
            else if(estado == EstadoJuego.JA_ENCARCELADOCONOPCIONDELIBERTAD){
                operaciones.add(OpcionMenu.INTENTARSALIRCARCELPAGANDOLIBERTAD.ordinal());
                operaciones.add(OpcionMenu.INTENTARSALIRCARCELTIRANDODADO.ordinal());
            }
            else if(estado == EstadoJuego.JA_PUEDEGESTIONAR){
                operaciones.add(OpcionMenu.HIPOTECARPROPIEDAD.ordinal());
                operaciones.add(OpcionMenu.CANCELARHIPOTECA.ordinal());
                operaciones.add(OpcionMenu.EDIFICARCASA.ordinal());
                operaciones.add(OpcionMenu.EDIFICARHOTEL.ordinal());
                operaciones.add(OpcionMenu.VENDERPROPIEDAD.ordinal());
                operaciones.add(OpcionMenu.PASARTURNO.ordinal());
            }
            else if(estado == EstadoJuego.JA_PUEDECOMPRAROGESTIONAR){
                operaciones.add(OpcionMenu.COMPRARTITULOPROPIEDAD.ordinal());
                operaciones.add(OpcionMenu.HIPOTECARPROPIEDAD.ordinal());
                operaciones.add(OpcionMenu.CANCELARHIPOTECA.ordinal());
                operaciones.add(OpcionMenu.EDIFICARCASA.ordinal());
                operaciones.add(OpcionMenu.EDIFICARHOTEL.ordinal());
                operaciones.add(OpcionMenu.VENDERPROPIEDAD.ordinal());
                operaciones.add(OpcionMenu.PASARTURNO.ordinal());
            }
            else if(estado == EstadoJuego.JA_CONSORPRESA){
                operaciones.add(OpcionMenu.APLICARSORPRESA.ordinal());
            }
            operaciones.add(OpcionMenu.TERMINARJUEGO.ordinal());
            operaciones.add(OpcionMenu.MOSTRARJUGADORACTUAL.ordinal());
            operaciones.add(OpcionMenu.MOSTRARJUGADORES.ordinal());
            operaciones.add(OpcionMenu.MOSTRARTABLERO.ordinal());
        }
        return operaciones;
        
    }
    public boolean necesitaElegirCasilla(int opcionMenu){
        boolean necesita = false;
        ArrayList<Integer> aux = new ArrayList<>();
        aux.add(OpcionMenu.HIPOTECARPROPIEDAD.ordinal());
        aux.add(OpcionMenu.CANCELARHIPOTECA.ordinal());
        aux.add(OpcionMenu.EDIFICARCASA.ordinal());
        aux.add(OpcionMenu.EDIFICARHOTEL.ordinal());
        aux.add(OpcionMenu.VENDERPROPIEDAD.ordinal());
        
        if(aux.contains(opcionMenu)){
            necesita = true;
        }
        return necesita;
    }
    public ArrayList<Integer> obtenerCasillasValidas(int opcionMenu){
        ArrayList<Integer> aux = new ArrayList<>();
        if(opcionMenu == OpcionMenu.HIPOTECARPROPIEDAD.ordinal() ||
           opcionMenu == OpcionMenu.EDIFICARCASA.ordinal() ||
           opcionMenu == OpcionMenu.EDIFICARHOTEL.ordinal() ||
           opcionMenu == OpcionMenu.VENDERPROPIEDAD.ordinal()){
            aux= modelo.obtenerPropiedadesJugadorSegunEstadoHipoteca(false);
        }
        else if(opcionMenu == OpcionMenu.CANCELARHIPOTECA.ordinal()){
            aux = modelo.obtenerPropiedadesJugadorSegunEstadoHipoteca(true);
        }
        
        return aux;
    }
    public String realizarOperacion(int opcionElegida, int casillaElegida){
        OpcionMenu opcion = OpcionMenu.values()[opcionElegida];
        String mensaje = "";
        
        if(opcion == OpcionMenu.INICIARJUEGO){
           modelo.inicializarJuego(nombreJugadores);
           mensaje += realizarOperacion(OpcionMenu.MOSTRARJUGADORES.ordinal(),0);
           mensaje += "\n\n";
           mensaje += "Empieza el jugador:\n";
           mensaje += modelo.getJugadorActual();
           mensaje += "\n\n";
        }
        else if(opcion == OpcionMenu.JUGAR){
           modelo.jugar();
           mensaje += "Valor del dado: ";
           mensaje += modelo.obtenerValorDado();
           mensaje += "\n\n";
           mensaje += modelo.getJugadorActual();
           mensaje += "\n\n";
        }
        else if(opcion == OpcionMenu.APLICARSORPRESA){
            mensaje += "La carta sorpresa es:\n";
            mensaje += modelo.getCartaActual();
            modelo.aplicarSorpresa();
            mensaje += "\n\n";
        }
        else if(opcion == OpcionMenu.INTENTARSALIRCARCELPAGANDOLIBERTAD){
            boolean aux = modelo.intentarSalirCarcel(MetodoSalirCarcel.PAGANDOLIBERTAD);
            if(aux){
                mensaje += "Has salido de la carcel, enhorabuena\n";
                mensaje += "\n\n";
            }
            else{
                mensaje += "Te quedas donde estas majo\n";
                mensaje += "\n\n";
            }
        }
        else if(opcion == OpcionMenu.INTENTARSALIRCARCELTIRANDODADO){
            boolean aux = modelo.intentarSalirCarcel(MetodoSalirCarcel.TIRANDODADO);
            if(aux){
                mensaje += "Has salido de la carcel, enhorabuena\n";
                mensaje += "\n\n";
            }
            else{
                mensaje += "Te quedas donde estas majo\n";
                mensaje += "\n\n";
            }
        }
        else if(opcion == OpcionMenu.COMPRARTITULOPROPIEDAD){
            boolean aux = modelo.comprarTituloPropiedad();
            if(aux){
                mensaje += "Acabas de adquirir una propiedad, enhorabuena\n";
                mensaje += "\n\n";
            }
            else{
                mensaje += "Te quedas como estas majo\n";
                mensaje += "\n\n";
            }
        }
        else if(opcion == OpcionMenu.HIPOTECARPROPIEDAD){
            modelo.hipotecarPropiedad(casillaElegida);
            mensaje += "\n\n";
        }
        else if(opcion == OpcionMenu.CANCELARHIPOTECA){
            boolean aux = modelo.cancelarHipoteca(casillaElegida);
            if(aux){
                mensaje += "Acabas de cancelar una hipoteca, enhorabuena\n";
                mensaje += "\n\n";
            }
            else{
                mensaje += "Te quedas como estas majo\n";
                mensaje += "\n\n";
            }
        }
        else if(opcion == OpcionMenu.EDIFICARCASA){
           boolean aux = modelo.edificarCasa(casillaElegida);
            if(aux){
                mensaje += "Acabas de edificar una casa enhorabuena\n";
                mensaje += "\n\n";
            }
            else{
                mensaje += "Te quedas como estas majo\n";
                mensaje += "\n\n";
            }
        }
        else if(opcion == OpcionMenu.EDIFICARHOTEL){
            boolean aux = modelo.edificarHotel(casillaElegida);
            if(aux){
                mensaje += "Acabas de edificar un hotel, enhorabuena\n";
                mensaje += "\n\n";
            }
            else{
                mensaje += "Te quedas como estas majo\n";
                mensaje += "\n\n";
            }
        }
        else if(opcion == OpcionMenu.VENDERPROPIEDAD){
            modelo.venderPropiedad(casillaElegida);
            mensaje += "\n\n";
        }
        else if(opcion == OpcionMenu.PASARTURNO){
            mensaje += "Pasando el turno al jugador\n";
            modelo.siguienteJugador();
            mensaje += modelo.getJugadorActual();
            mensaje += "\n\n";
        }
        else if(opcion == OpcionMenu.OBTENERRANKING){
            mensaje += "\n\n";
            modelo.obtenerRanking();
            mensaje += realizarOperacion(OpcionMenu.MOSTRARJUGADORES.ordinal(),0);
            mensaje += "\n\n";
        }
        else if(opcion == OpcionMenu.TERMINARJUEGO){
            mensaje += "\n\n";
            System.out.println("GRACIAS POR JUGAR A QYTETET (Esta version esta en fase alpha, disculpen las molestias)");
            System.exit(0);
        }
        else if(opcion == OpcionMenu.MOSTRARJUGADORACTUAL){
            mensaje += "\n\n";
            mensaje += modelo.getJugadorActual();
            mensaje += "\n\n";
        }
        else if(opcion == OpcionMenu.MOSTRARJUGADORES){
            mensaje += "\n\n";
            for(Jugador jugador: modelo.getJugadores()){
               mensaje += jugador;
               mensaje += "\n";
            }
        }
        else if(opcion == OpcionMenu.MOSTRARTABLERO){
            mensaje += "\n\n";
            mensaje += modelo.getTablero();
            mensaje += "\n\n";
        }

        return mensaje;
    }
    
}
