////////////////////////////////////////////////////////////////////////////////
// Autor: Alejandro Manzanares Lemus
// 2º GII A1
////////////////////////////////////////////////////////////////////////////////
package modeloqytetet;

import java.util.Random;

public class Dado {
//SIGLETON
    //Atributos
    private int valor;
    
    //Constructor SIGLETON
    private static final Dado instance = new Dado();
    private Dado() {
    }
    public static Dado getInstance(){
        return instance;
    }
    
    //Metodos
    int getValor(){
        return valor;
    }
    int tirar(){
        Random n = new Random();
        valor = n.nextInt(6)+1;
        return valor;
    }
    @Override
    public String toString() {
        return "Dado{" + "valor=" + valor + '}';
    }
}
