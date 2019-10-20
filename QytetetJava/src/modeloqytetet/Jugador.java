////////////////////////////////////////////////////////////////////////////////
// Autor: Alejandro Manzanares Lemus
// 2º GII A1
////////////////////////////////////////////////////////////////////////////////
package modeloqytetet;
import java.util.ArrayList;

public class Jugador implements Comparable{
    //Atributos
    private boolean encarcelado;
    private String nombre;
    protected int saldo;
    private Sorpresa cartaLibertad;
    protected Casilla casillaActual;
    private ArrayList<TituloPropiedad> propiedades 
                                             = new ArrayList<TituloPropiedad>();
    
    //Constructor
    public Jugador(String nombre) {    
        this.nombre = nombre;
        saldo = 7500;
        setCasillaActual(new OtraCasilla(0, TipoCasilla.SALIDA));
    }
    //Jugador

    //Metodos
    boolean cancelarHipoteca(TituloPropiedad titulo){
        boolean cancelacion = false;
        if(tengoSaldo(titulo.calcularCosteCancelar())){
            modificarSaldo(-titulo.calcularCosteCancelar());
            titulo.cancelarHipoteca();
            cancelacion = true;
        }
        return cancelacion;
    }    
    boolean comprarTituloPropiedad(){
        int costeCompra = casillaActual.getCoste();
        boolean comprado = false;
        if (costeCompra < saldo){
            TituloPropiedad titulo = ((Calle)casillaActual).asignarPropietario(this);
            propiedades.add(titulo);
            modificarSaldo(-costeCompra);
            comprado = true;
        }
        return comprado;
    }    
    //Convertirme
    int cuantasCasasHotelesTengo(){
        int casasHoteles = 0;
        for(TituloPropiedad propiedad: propiedades){
            int numCasaHotelPropiedad = propiedad.getNumCasas() + propiedad.getNumHoteles();
            casasHoteles = casasHoteles + numCasaHotelPropiedad;
        }
        return casasHoteles;
    }  
    //deboIrACarcel
    boolean deboPagarAlquiler(){
        boolean deboPagar = false;
        TituloPropiedad titulo = casillaActual.getTitulo();
        if (!(esDeMiPropiedad(titulo))){
            boolean tienePropietario = titulo.tengoPropietario();
            if (tienePropietario){
                boolean encarcelado = titulo.propietarioEncarcelado();
                if (!(encarcelado)){
                    boolean estaHipotecada = titulo.isHipotecada();
                    if (!estaHipotecada){
                        deboPagar = true;
                    }
                }
            }
        }
        return deboPagar;
    }
    Sorpresa devolverCartaLibertad(){
        Sorpresa cartaLibertadDevuelta = cartaLibertad;
        cartaLibertad = null;
        
        return cartaLibertadDevuelta;
    }
    //edificarCasa
    //edificarHotel
    private void eliminarDeMisPropiedades(TituloPropiedad titulo){
        for (int i = 0; i < propiedades.size(); i++){
            if (propiedades.get(i) == titulo){
                propiedades.remove(i);
            }
        }
        titulo.setPropietario(null);
    }
    private boolean esDeMiPropiedad(TituloPropiedad titulo){
        boolean esMia = false;
        for(TituloPropiedad propiedad: propiedades){
            if(titulo.getNombre().equals(propiedad.getNombre())){
                esMia = true;
            }
        }
        
        return esMia;
    }
    boolean estoyEnCalleLibre(){
        throw new UnsupportedOperationException("Sin implementar");
    }
    Sorpresa getCartaLibertad(){
        return cartaLibertad;
    }
    Casilla getCasillaActual(){
        return casillaActual;
    }
    boolean getEncarcelado(){
        return encarcelado;
    }
    String getNombre(){
        return nombre;
    }
    ArrayList<TituloPropiedad> getPropiedades(){
        return propiedades;
    }
    public int getSaldo(){
        return saldo;
    }
    void hipotecarPropiedad(TituloPropiedad titulo){
        int costeHipoteca = titulo.hipotecar();
        modificarSaldo(costeHipoteca);
    }
    void irACarcel(Casilla casilla){
        casillaActual = casilla;
        encarcelado = true;
    }    
    int modificarSaldo(int cantidad){
        int saldoMod = saldo+cantidad;
        return saldoMod;
    }
    int obtenerCapital(){
        int capital = saldo;
        for(TituloPropiedad propiedad: propiedades){
            int numHoteles = propiedad.getNumHoteles();
            int numCasas = propiedad.getNumCasas();
            int precioEdificar = propiedad.getPrecioEdificar();
            int precioCompra = propiedad.getPrecioCompra();
            int valorPropiedad = precioCompra+(numHoteles+numCasas)*precioEdificar;
            if(propiedad.isHipotecada()){
                valorPropiedad = valorPropiedad - propiedad.getHipotecaBase();
            }
            capital = capital + valorPropiedad;
        }
        return capital;
    }
    ArrayList<TituloPropiedad> obtenerPropiedades(boolean hipotecada){
        ArrayList<TituloPropiedad> propiedadesHipotecadas = new ArrayList();
        for(TituloPropiedad propiedad: propiedades){
            if(propiedad.isHipotecada() == hipotecada){
                propiedadesHipotecadas.add(propiedad);
            }
        }
        
        return propiedadesHipotecadas;
    }
    void pagarAlquiler(){
        int costeAlquiler = ((Calle)casillaActual).pagarAlquiler();
        modificarSaldo(costeAlquiler);
    }    
    //pagarImpuesto
    void pagarLibertad(int cantidad){
        boolean tengoSaldo = tengoSaldo(cantidad);
        if (tengoSaldo){
            encarcelado = false;
            modificarSaldo(-cantidad);
        }
    }    
    //puedoEdificarCasa
    //puedoEdificarHotel
    void setCartaLibertad(Sorpresa carta){
        cartaLibertad = carta;
    }
    void setCasillaActual(Casilla casilla){
        casillaActual = casilla;
    }
    void setEncarcelado(boolean encarcelado){
        this.encarcelado = encarcelado;
    }
    boolean tengoCartaLibertad(){
        boolean laTengo = true;
        if(cartaLibertad == null){
            laTengo = false;
        }
        
        return laTengo;
    }
    //tengoSaldo
    void venderPropiedad(Casilla casilla){
        TituloPropiedad titulo = casilla.getTitulo();
        eliminarDeMisPropiedades(titulo);
        int precioVenta = titulo.calcularPrecioVenta();
        modificarSaldo(precioVenta);
    }
    @Override
    public String toString() {
        return "Jugador{" + 
                "encarcelado=" + encarcelado + 
                ", nombre=" + nombre + 
                ", saldo=" + saldo + 
                ", cartaLibertad=" + cartaLibertad + 
                ", casillaActual=" + casillaActual + 
                ", propiedades=" + propiedades +
                ", capital=" + obtenerCapital()+ '}';
    }
    @Override
    public int compareTo(Object otroJugador){
        int otroCapital = ((Jugador) otroJugador).obtenerCapital();
        return otroCapital - obtenerCapital();
    }
    
    //Practica 4
    protected Especulador convertirme(int fianza){
        Especulador especulador = new Especulador(this,fianza);
        return especulador;
    }
    protected boolean deboIrACarcel(){
        return !tengoCartaLibertad();
    }
    boolean edificarCasa(TituloPropiedad titulo){
        boolean edificada = false;
        if (puedoEdificarCasa(titulo)){
            int costeEdificarCasa = titulo.getPrecioEdificar();
            boolean tengoSaldo = tengoSaldo(costeEdificarCasa);
            if (tengoSaldo){
                titulo.edificarCasa();
                modificarSaldo(-costeEdificarCasa);
                edificada = true;
            }
        }
        return edificada;
    }
    boolean edificarHotel(TituloPropiedad titulo){
        boolean edificada = false;
        if (puedoEdificarHotel(titulo)){
            int costeEdificarHotel = titulo.getPrecioEdificar();
            boolean tengoSaldo = tengoSaldo(costeEdificarHotel);
            if (tengoSaldo){
                titulo.edificarHotel();
                modificarSaldo(-costeEdificarHotel);
                edificada = true;
            }
        }
        return edificada;
    }
    protected Jugador(Jugador otroJugador){
        nombre = otroJugador.getNombre();
        saldo = otroJugador.getSaldo();
        setCasillaActual(otroJugador.getCasillaActual());
    }
    protected void pagarImpuesto(){
        saldo = saldo - casillaActual.getCoste();
    }
    protected boolean puedoEdificarCasa(TituloPropiedad titulo){
        boolean puedo = false;
        if(titulo.getNumCasas() < 4){
            puedo = true;
        }
        return puedo;
    }
    protected boolean puedoEdificarHotel(TituloPropiedad titulo){
        boolean puedo = false;
        if(titulo.getNumCasas() == 4 && titulo.getNumHoteles() < 4){
            puedo = true;
        }
        return puedo;
    }
    protected boolean tengoSaldo(int cantidad){
        boolean tengo =  false;
        if(saldo > cantidad){
            tengo = true;
        }
        
        return tengo;
    }
}
