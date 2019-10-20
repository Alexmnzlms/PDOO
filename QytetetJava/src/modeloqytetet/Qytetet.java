////////////////////////////////////////////////////////////////////////////////
// Autor: Alejandro Manzanares Lemus
// 2º GII A1
////////////////////////////////////////////////////////////////////////////////
package modeloqytetet;
import java.util.ArrayList;
import static java.util.Collections.shuffle;
import static java.util.Collections.sort;
import java.util.Random;

public class Qytetet {
//SIGLETON
    //Atributos
    public static int MAX_JUGADORES = 4;
    static int NUM_SORPRESAS = 10;
    public static int NUM_CASILLAS = 20;
    static int PRECIO_LIBERTAD = 200;
    static int SALDO_SALIDA = 1000;
    
    private Sorpresa cartaActual;
    private ArrayList<Sorpresa> mazo = new ArrayList<Sorpresa>();
    private ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
    private Jugador jugadorActual;
    private Tablero tablero;
    Dado dado = Dado.getInstance();
    private EstadoJuego estadoJuego;
    
    //Constructor SINGLETON
    private static final Qytetet instance = new Qytetet();
    private Qytetet(){}
    public static Qytetet getInstance(){
        return instance;
    }
    
    //Metodos
    void actuarSiEnCasillaEdificable(){
        boolean deboPagar = jugadorActual.deboPagarAlquiler();
        if (deboPagar){
            jugadorActual.pagarAlquiler();
            if (jugadorActual.getSaldo() <= 0){
                estadoJuego = EstadoJuego.ALGUNJUGADORENBANCAROTA;
            }
        }
        Casilla casilla = obtenerCasillaJugadorActual();
        boolean tengoPropietario = ((Calle)casilla).tengoPropietario();
        if(estadoJuego != EstadoJuego.ALGUNJUGADORENBANCAROTA){
            if(tengoPropietario){
                estadoJuego = EstadoJuego.JA_PUEDEGESTIONAR;
            }
            else{
                estadoJuego = EstadoJuego.JA_PUEDECOMPRAROGESTIONAR;
            }
        }
    }
    void actuarSiEnCasillaNoEdificable(){
        estadoJuego = EstadoJuego.JA_PUEDEGESTIONAR;
        Casilla casillaActual = jugadorActual.getCasillaActual();
        if (casillaActual.getTipo() == TipoCasilla.IMPUESTO){
            jugadorActual.pagarImpuesto();
            if(jugadorActual.getSaldo() <= 0){
                estadoJuego = EstadoJuego.ALGUNJUGADORENBANCAROTA;
            }
        }
        else{
            if (casillaActual.getTipo() == TipoCasilla.JUEZ){
                encarcelarJugador();
            }
            else if (casillaActual.getTipo() == TipoCasilla.SORPRESA){
                cartaActual = mazo.get(0);
                mazo.remove(0);
                estadoJuego = EstadoJuego.JA_CONSORPRESA;
            }
        }
    }
    public void aplicarSorpresa(){
        estadoJuego = EstadoJuego.JA_PUEDEGESTIONAR;
        if (cartaActual.getTipo() == TipoSorpresa.SALIRCARCEL){
            jugadorActual.setCartaLibertad(cartaActual);
        }
        else{
            if (cartaActual.getTipo() == TipoSorpresa.PAGARCOBRAR){
                jugadorActual.modificarSaldo(cartaActual.getValor());
                if (jugadorActual.getSaldo() <= 0){
                    estadoJuego = EstadoJuego.ALGUNJUGADORENBANCAROTA;
                }
            }
            else if (cartaActual.getTipo() == TipoSorpresa.IRACASILLA){
                int valor = cartaActual.getValor();
                boolean casillaCarcel = tablero.esCasillaCarcel(valor);
                if (casillaCarcel){
                    encarcelarJugador();
                }
                else{
                    mover(valor);
                }
            }
            else if (cartaActual.getTipo() == TipoSorpresa.PORCASAHOTEL){
                int cantidad = cartaActual.getValor();
                int numeroTotal = jugadorActual.cuantasCasasHotelesTengo();
                jugadorActual.modificarSaldo(cantidad * numeroTotal);
                if (jugadorActual.getSaldo() <= 0){
                    estadoJuego = EstadoJuego.ALGUNJUGADORENBANCAROTA;
                }
            }
            else if (cartaActual.getTipo() == TipoSorpresa.PORJUGADOR){
                for(int i = 0; i < jugadores.size(); i++){
                    if (jugadores.get(i) != jugadorActual){
                        jugadores.get(i).modificarSaldo(cartaActual.getValor());
                        if (jugadores.get(i).getSaldo() <= 0){
                            estadoJuego = EstadoJuego.ALGUNJUGADORENBANCAROTA;
                        }
                        jugadorActual.modificarSaldo(-cartaActual.getValor());
                        if (jugadorActual.getSaldo() <= 0){
                            estadoJuego = EstadoJuego.ALGUNJUGADORENBANCAROTA;
                        }
                    }
                }
            }
            else if (cartaActual.getTipo() == TipoSorpresa.CONVERTIRME){
                int fianza = cartaActual.getValor();
                for(int i = 0; i < jugadores.size(); i++){
                    if(jugadorActual == jugadores.get(i)){
                        jugadores.set(i,jugadorActual.convertirme(fianza));
                    }
                }
            }
        }
    }    
    public boolean cancelarHipoteca(int numeroCasilla){
        Casilla casilla = tablero.obtenerCasillaNumero(numeroCasilla);
        TituloPropiedad titulo = casilla.getTitulo();
        boolean cancelacion = jugadorActual.cancelarHipoteca(titulo);
        estadoJuego = EstadoJuego.JA_PUEDEGESTIONAR;
        return cancelacion;
    }    
    public boolean comprarTituloPropiedad(){
        boolean comprado = jugadorActual.comprarTituloPropiedad();
        if (comprado){
            estadoJuego = EstadoJuego.JA_PUEDEGESTIONAR;
        }
        return comprado;
    }
    public boolean edificarCasa(int numeroCasilla){
        Casilla casilla = tablero.obtenerCasillaNumero(numeroCasilla);
        TituloPropiedad titulo = casilla.getTitulo();
        boolean edificada = jugadorActual.edificarCasa(titulo);
        if (edificada){
            estadoJuego = EstadoJuego.JA_PUEDEGESTIONAR;
        }
        return edificada;
    }
    public boolean edificarHotel(int numeroCasilla){
        Casilla casilla = tablero.obtenerCasillaNumero(numeroCasilla);
        TituloPropiedad titulo = casilla.getTitulo();
        boolean edificada = jugadorActual.edificarHotel(titulo);
        if (edificada){
            estadoJuego = EstadoJuego.JA_PUEDEGESTIONAR;
        }
        return edificada;
    }
    private void encarcelarJugador(){
        if (jugadorActual.deboIrACarcel()){
            Casilla casillaCarcel = tablero.getCarcel();
            jugadorActual.irACarcel(casillaCarcel);
            estadoJuego = EstadoJuego.JA_ENCARCELADO;
        }
        else{
            Sorpresa carta = jugadorActual.devolverCartaLibertad();
            mazo.add(carta);
            estadoJuego = EstadoJuego.JA_PUEDEGESTIONAR;
        }
    }    
    public Sorpresa getCartaActual(){
        return cartaActual;
    }
    Dado getDado(){
        return dado;
    }
    public EstadoJuego getEstadoJuego() {
        return estadoJuego;
    }    
    public Jugador getJugadorActual(){
        return jugadorActual;
    }
    public ArrayList<Jugador> getJugadores(){
        return jugadores;
    }    
    ArrayList<Sorpresa> getMazo(){
        return mazo;
    } 
    public Tablero getTablero() {
        return tablero;
    }   
    public void hipotecarPropiedad(int numeroCasilla){
        Casilla casilla = tablero.obtenerCasillaNumero(numeroCasilla);
        TituloPropiedad titulo = casilla.getTitulo();
        jugadorActual.hipotecarPropiedad(titulo);
        estadoJuego = EstadoJuego.JA_PUEDEGESTIONAR;
    }    
    private void inicializarCartasSorpresa(){
        mazo.add(new Sorpresa ("Level up, ahora eres especulador", 3000, 
                TipoSorpresa.CONVERTIRME));
        mazo.add(new Sorpresa ("Max. level up, eres un super-especulador", 5000, 
                TipoSorpresa.CONVERTIRME));
        mazo.add(new Sorpresa ("Has escuchado que en la casilla 2 estan"
                + " vendiendo una switch en una subasta"
                + " ve a probar suerte", 1, 
                TipoSorpresa.IRACASILLA));
        mazo.add(new Sorpresa ("Te tropiezas con una piel de platano"
                + " taaaan fuerte que acabas en la casila 13, mala suerte", 13, 
                TipoSorpresa.IRACASILLA));
        mazo.add(new Sorpresa ("Ups quizas no deberias haber apostado tu"
                + " libertad a esa ultima mano de poker"
                + ". Ve a la carcel", 10, 
                TipoSorpresa.IRACASILLA));
        mazo.add(new Sorpresa ("Te encuentras una cartera llena de dinero"
                + " por la calle, la entregas en una comisaria pero"
                + " misteriosamente el dinero ha desaparecido"
                + ", cosas que pasan", 2500,  
                TipoSorpresa.PAGARCOBRAR));
        mazo.add(new Sorpresa ("¿Dices que un hombre vestido de santa claus"
                + " te ha atracado? si hombre y seguro"
                + " que los reyes magos le esperaban en el trineo", -1350, 
                TipoSorpresa.PAGARCOBRAR));
        mazo.add(new Sorpresa ("Un empresario chino quiere absorver "
                + " tus propiedades te da un adelanto para que lo pienses", 100, 
                TipoSorpresa.PORCASAHOTEL));
        mazo.add(new Sorpresa ("Vaya, el nuevo equipo de gobierno ha aprobado"
                + " un nuevo impuesto de propiedades, una pena", -150, 
                TipoSorpresa.PORCASAHOTEL));
        mazo.add(new Sorpresa ("Has perdido una apuesta con tus amigos,"
                + " ya te dije yo que el mono de marco se llamaba"
                + " amedio y no amelio" , -50, 
                TipoSorpresa.PORJUGADOR));
        mazo.add(new Sorpresa ("Creas un crowfunding y consigues que tus"
                + " colegas se rasquen el bolsillo para ayudar", 25, 
                TipoSorpresa.PORJUGADOR));
        mazo.add(new Sorpresa ("¿Me estas diciendo enserio que te saco de"
                + " la carcel un chico con un sombrero de paja?", 
                tablero.getCarcel().getNumeroCasilla(), 
                TipoSorpresa.SALIRCARCEL));
        shuffle(mazo);
    }    
    public void inicializarJuego(ArrayList<String> nombres){
        inicializarTablero();
        inicializarCartasSorpresa();
        inicializarJugadores(nombres);
        salidaJugadores();
    }
    private void inicializarJugadores(ArrayList<String> nombres){
        for( String nombre: nombres){
            jugadores.add(new Jugador(nombre));
        }
    }
    private void inicializarTablero(){
        tablero = new Tablero();
    }    
    public boolean intentarSalirCarcel(MetodoSalirCarcel metodo){
        if(metodo == MetodoSalirCarcel.TIRANDODADO){
            int resultado = tirarDado();
            if (resultado >= 5){
                jugadorActual.setEncarcelado(false);
            }
        }
        else if (metodo == MetodoSalirCarcel.PAGANDOLIBERTAD){
            jugadorActual.pagarLibertad(PRECIO_LIBERTAD);
        }
        boolean encarcelado = jugadorActual.getEncarcelado();
        if (encarcelado){
            estadoJuego = EstadoJuego.JA_ENCARCELADO;
        }
        else{
            estadoJuego = EstadoJuego.JA_PREPARADO;
        }
        return !encarcelado;
    }
    public void jugar(){
        int tirada = dado.tirar();
        Casilla casilla_tirada = tablero.obtenerCasillaFinal(jugadorActual.getCasillaActual(), tirada);
        mover(casilla_tirada.getNumeroCasilla());
    }
    void mover(int numCasillaDestino){
        Casilla casillaInicial = jugadorActual.getCasillaActual();
        Casilla casillaFinal = tablero.obtenerCasillaNumero(numCasillaDestino);
        jugadorActual.setCasillaActual(casillaFinal);
        if (numCasillaDestino < casillaInicial.getNumeroCasilla()){
            jugadorActual.modificarSaldo(SALDO_SALIDA);
        }
        if (casillaFinal.soyEdificable()){
            actuarSiEnCasillaEdificable();
        }
        else{
            actuarSiEnCasillaNoEdificable();
        }
    }
    public Casilla obtenerCasillaJugadorActual(){
        return jugadorActual.getCasillaActual();
    }
    public ArrayList<Casilla> obtenerCasillasTablero(){
        return tablero.getCasillas();
    }
    public ArrayList<Integer> obtenerPropiedadesJugador(){
        ArrayList<Integer> numCasillaJugadorActual = new ArrayList();
        for (TituloPropiedad titulo: jugadorActual.getPropiedades()){
            for (Casilla casilla: tablero.getCasillas()){
                if(titulo == casilla.getTitulo()){
                    numCasillaJugadorActual.add(casilla.getNumeroCasilla());
                }
            }
        }
        
        return numCasillaJugadorActual;
    }
    public ArrayList<Integer> obtenerPropiedadesJugadorSegunEstadoHipoteca(boolean estadoHipoteca){
        ArrayList<Integer> numCasillaEstadoHipotecaJugadorActual = new ArrayList();
        ArrayList<TituloPropiedad> propiedadesJugador = jugadorActual.obtenerPropiedades(estadoHipoteca);
        for (TituloPropiedad titulo: propiedadesJugador){
            for (Casilla casilla: tablero.getCasillas()){
                if(titulo == casilla.getTitulo()){
                    numCasillaEstadoHipotecaJugadorActual.add(casilla.getNumeroCasilla());
                }
            }
        }
        
        return numCasillaEstadoHipotecaJugadorActual;
    }
    public void obtenerRanking(){
        sort(jugadores);
    }
    public int obtenerSaldoJugadorActual(){
        return jugadorActual.getSaldo();
    }
    public int obtenerValorDado(){
        return dado.getValor();
    }
    private void salidaJugadores(){
        estadoJuego = EstadoJuego.JA_PREPARADO;
        for(int i = 0; i < jugadores.size(); i++){
            jugadores.get(i).setCasillaActual(tablero.obtenerCasillaNumero(0));
        Random actual = new Random();
        jugadorActual = jugadores.get(actual.nextInt(jugadores.size())); 
        }
    }
    private void setCartaActual(Sorpresa cartaActual){
        this.cartaActual = cartaActual;
    }
    public void setEstadoJuego(EstadoJuego estadoJuego) {
        this.estadoJuego = estadoJuego;
    }
    public void siguienteJugador(){
        int pos = 0;
        while(jugadores.get(pos) != jugadorActual){
            pos++;
        }
        jugadorActual = jugadores.get((pos+1)%jugadores.size());
        if(jugadorActual.getEncarcelado()){
            estadoJuego = EstadoJuego.JA_ENCARCELADOCONOPCIONDELIBERTAD;
        }
        else{
            estadoJuego = EstadoJuego.JA_PREPARADO;
        }
    }
    int tirarDado(){
       return dado.tirar();
    }
    public void venderPropiedad(int numeroCasilla){
        Casilla casilla = tablero.obtenerCasillaNumero(numeroCasilla);
        jugadorActual.venderPropiedad(casilla);
        estadoJuego = EstadoJuego.JA_PUEDEGESTIONAR;
    }
    @Override
    public String toString() {
        return "Qytetet{" + 
                "cartaActual=" + cartaActual + ", "
                + "mazo=" + mazo + 
                ", jugadores=" + jugadores + 
                ", jugadorActual=" + jugadorActual + 
                ", tablero=" + tablero + 
                ", dado=" + dado + '}';
    } 
    
    //No salen en el diagrama
    public boolean jugadorActualEnCalleLibre(){
        return (jugadorActual.getCasillaActual().soyEdificable() && !(((Calle)jugadorActual.getCasillaActual()).tengoPropietario()));
    }
    public boolean jugadorActualEncarcelado(){
        return jugadorActual.getEncarcelado();
    }
}