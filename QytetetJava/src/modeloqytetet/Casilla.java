////////////////////////////////////////////////////////////////////////////////
// Autor: Alejandro Manzanares Lemus
// 2º GII A1
////////////////////////////////////////////////////////////////////////////////
package modeloqytetet;

public abstract class Casilla {
    //Atributos
    private int coste = 0; 
    private int numeroCasilla;                                 
    
    //Constructor
    public Casilla(int numeroCasilla) {
        this.numeroCasilla = numeroCasilla;
    }
    
    //Metodos
    public int getCoste() {
        return coste;
    }
    public int getNumeroCasilla() {
        return numeroCasilla;
    }
    protected abstract TipoCasilla getTipo();
    protected abstract TituloPropiedad getTitulo();
    public void setCoste(int coste){
        this.coste = coste;
    }
    protected abstract boolean soyEdificable();
    @Override
    public String toString( ){
        return "Casilla{" + "\n"
            + "numeroCasilla=" + Integer.toString(numeroCasilla) + "\n"
            + "coste=" + Integer.toString(coste) + "\n"
            + "}";
    }
    
    //Fuera del grafico de la P4
//    public Casilla(int numero, TipoCasilla type, TituloPropiedad title){
//        numeroCasilla = numero;
//        tipo = type;
//        setTitulo(title);
//        coste = title.getPrecioCompra();
//    }
//    public Casilla(int numero, TipoCasilla type){
//        numeroCasilla = numero;
//        tipo = type;
//    }     
//    TituloPropiedad asignarPropietario(Jugador jugador){
//        titulo.setPropietario(jugador);
//        return titulo;
//    }
//    int pagarAlquiler(){
//        int costeAlquiler = titulo.pagarAlquiler();
//        return costeAlquiler;
//    }
//    Boolean propietarioEncarcelado(){
//        return titulo.propietarioEncarcelado();
//    }
//    Boolean tengoPropietario(){
//        return titulo.tengoPropietario();
//    }
//    private void setTitulo(TituloPropiedad title) {
//        titulo = title;
//    }
}
