////////////////////////////////////////////////////////////////////////////////
// Autor: Alejandro Manzanares Lemus
// 2º GII A1
////////////////////////////////////////////////////////////////////////////////
package modeloqytetet;

public class Especulador extends Jugador{
    //Atributos
    private int fianza;
    
    //Constructor
    public Especulador(Jugador jugador, int fianza){
        super(jugador);
        this.fianza = fianza;
    }
    
    //Metodos
    @Override
    protected Especulador convertirme(int fianza){
        return this;
    }
    @Override
    protected boolean deboIrACarcel(){
        boolean debo = false;
        if(!pagarFianza() && super.deboIrACarcel()){
            debo = true;
        }
        return debo;
    }
    protected boolean pagarFianza(){
        boolean pago = false;
        if(tengoSaldo(fianza)){
            pago = true;
            modificarSaldo(fianza);
        }
        return pago;
    }
    @Override
    protected void pagarImpuesto(){
        saldo = (saldo - casillaActual.getCoste() / 2);
    }
    @Override
    protected boolean puedoEdificarCasa(TituloPropiedad titulo){
        boolean puedo = false;
        if(titulo.getNumCasas() < 8){
            puedo = true;
        }
        return puedo;
    }
    @Override
    protected boolean puedoEdificarHotel(TituloPropiedad titulo){
        boolean puedo = false;
        if(titulo.getNumCasas() == 4 && titulo.getNumHoteles() < 8){
            puedo = true;
        }
        return puedo;
    }
    @Override
    public String toString() {
        return super.toString() + "fianza=" + fianza;
    }
}
