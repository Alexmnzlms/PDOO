////////////////////////////////////////////////////////////////////////////////
// Autor: Alejandro Manzanares Lemus
// 2º GII A1
////////////////////////////////////////////////////////////////////////////////
package modeloqytetet;

public class Calle extends Casilla {
    //Atributos 
    private TituloPropiedad titulo; 

    //Constructor
    public Calle(int numero, TituloPropiedad titulo) {
        super(numero);
        setCoste(titulo.getPrecioCompra());
        this.titulo = titulo;
    }
        
    //Metodos
    public TituloPropiedad asignarPropietario(Jugador jugador){
        titulo.setPropietario(jugador);
        return titulo;
    }
    @Override
    protected TipoCasilla getTipo() {
        return TipoCasilla.CALLE;
    }
    @Override
    protected TituloPropiedad getTitulo() {
        return titulo;
    }
    public int pagarAlquiler(){
        int costeAlquiler = titulo.pagarAlquiler();
        return costeAlquiler;
    }
    private void setTitulo(TituloPropiedad title) {
        titulo = title;
    }
    @Override
    protected boolean soyEdificable(){
        return true;
    }
    public boolean tengoPropietario(){
        return titulo.tengoPropietario();
    }
    @Override
    public String toString( ){
        return super.toString() + "Tipo: " + TipoCasilla.CALLE + "\n"
                + "Titulo: " + titulo;
    }
}
