////////////////////////////////////////////////////////////////////////////////
// Autor: Alejandro Manzanares Lemus
// 2º GII A1
////////////////////////////////////////////////////////////////////////////////
package modeloqytetet;
import java.util.logging.Logger;
    
public class TituloPropiedad {
    //Atributos
    private int alquilerBase;
    private float factorRevalorizacion;
    private int hipotecaBase;
    private boolean hipotecada;
    private String nombre;
    private int numCasas;
    private int numHoteles;
    private int precioCompra;
    private int precioEdificar;
    private Jugador propietario;
    
    //Constructor
    public TituloPropiedad(String nom, int precioC, int alquiler, 
            float factor, int precioE, int hipoteca){
        hipotecada = false;
        numHoteles = 0;
        numCasas = 0;
        nombre = nom;
        precioCompra = precioC;
        alquilerBase = alquiler;
        factorRevalorizacion = factor;
        hipotecaBase = hipoteca;
        precioEdificar = precioE;
        propietario = null;
    }
    
    //Metodos
    int calcularCosteCancelar(){
        return (int) (calcularCosteHipotecar() + calcularCosteHipotecar() * 0.1);
    }
    int calcularCosteHipotecar(){
        int costeHipoteca = (int) (hipotecaBase + (numCasas*0.5*hipotecaBase+numHoteles*hipotecaBase));
        return costeHipoteca;
    }
    int calcularImporteAlquiler(){
        int costeAlquiler = (int)(alquilerBase + (numCasas*0.5+numHoteles*2));
        return costeAlquiler;
    }
    int calcularPrecioVenta(){
        int precioVenta = (int) (precioCompra + (numCasas+numHoteles) *precioEdificar *factorRevalorizacion);
        return precioVenta;
    }
    void cancelarHipoteca(){
        hipotecada = false;
    }
    void edificarCasa(){
        numCasas += 1;
    }
    void edificarHotel(){
        numHoteles += 1;
    }
    String getNombre() {
        return nombre;
    }
    boolean isHipotecada() {
        return hipotecada;
    }
    int getPrecioCompra() {
        return precioCompra;
    }
    int getAlquilerBase() {
        return alquilerBase;
    }
    float getFactorRevalorizacion() {
        return factorRevalorizacion;
    }
    int getHipotecaBase() {
        return hipotecaBase;
    }
    int getPrecioEdificar() {
        return precioEdificar;
    }
    int getNumCasas() {
        return numCasas;
    }
    int getNumHoteles() {
        return numHoteles;
    }
    Jugador getPropietario(){
        return propietario;
    }    
    int hipotecar(){
        hipotecada = true;
        int costeHipoteca = calcularCosteHipotecar();
        return costeHipoteca;
    }
    int pagarAlquiler(){
       int costeAlquiler = calcularImporteAlquiler();
       propietario.modificarSaldo(costeAlquiler);
       return costeAlquiler;
    }
    boolean propietarioEncarcelado(){
        return propietario.getEncarcelado();
    }
    boolean tengoPropietario(){
        return (propietario != null);
    }
    void setHipotecada(boolean hipoteca) {
        hipotecada = hipoteca;
    }
    void setPropietario(Jugador propietario){
        this.propietario = propietario;
    }
    @Override
    public String toString( ){
        String nombreprop;
        if(propietario != null){
            nombreprop = propietario.getNombre();
        }
        else{
            nombreprop = "No hay propietario";
        }
        return "TituloPropiedad{" + "\n"
            + " nombre=" + nombre + "\n"
            + " nombre propietario=" + nombreprop + "\n"
            + " hipotecado=" + Boolean.toString(hipotecada) + "\n"
            + " precioCompra=" + Integer.toString(precioCompra) + "\n"
            + " alquilerBase=" + Integer.toString(alquilerBase) + "\n"
            + " factorRevalorizacion=" + Float.toString(factorRevalorizacion)
                + "\n"
            + " hipotecaBase=" + Integer.toString(hipotecaBase) + "\n"
            + " precioEdificar=" + Integer.toString(precioEdificar) + "\n"
            + " numCasas=" + Integer.toString(numCasas) + "\n"
            + " numHoteles=" + Integer.toString(numHoteles)
            + "}";
    }

    //No salen en el diagrama
    void cobrarAlquiler(int coste){
        throw new UnsupportedOperationException("Sin implementar");
    }
}   
