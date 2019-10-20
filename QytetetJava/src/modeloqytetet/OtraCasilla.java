////////////////////////////////////////////////////////////////////////////////
// Autor: Alejandro Manzanares Lemus
// 2º GII A1
////////////////////////////////////////////////////////////////////////////////
package modeloqytetet;

public class OtraCasilla extends Casilla {
    //Atributos
    private TipoCasilla tipo;
    //Constructor

    public OtraCasilla(int numero, TipoCasilla type) {
        super(numero);
        tipo = type;
    }
    
    //Metodos
    @Override
    protected TipoCasilla getTipo() {
        return tipo;
    }
    @Override
    protected boolean soyEdificable(){
        return false;
    }
    @Override
    protected TituloPropiedad getTitulo() {
        return null;
    }
    @Override
    public String toString( ){
        return super.toString() + "Tipo: " + tipo + "\n";
    }
}
