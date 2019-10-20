////////////////////////////////////////////////////////////////////////////////
// Autor: Alejandro Manzanares Lemus
// 2º GII A1
////////////////////////////////////////////////////////////////////////////////
package modeloqytetet;

public class Sorpresa {
    //Atributos
    private String texto;    
    private TipoSorpresa tipo;      
    private int valor;
    
    //Constructor
    public Sorpresa(String desc, int val, TipoSorpresa type){
        texto = desc;
        valor = val;
        tipo = type;
    }
    
    //Metodos 
    String getTexto( ){
        return texto;
    }
    TipoSorpresa getTipo( ){
        return tipo;
    }
    int getValor( ){
        return valor;
    }
    @Override
    public String toString( ){
        return "Sorpresa{" + "\n"
            + "texto=" + texto + "\n"
            + "valor=" + Integer.toString(valor) + "\n"
            + "tipo=" + tipo
            + "}";
    }
}
