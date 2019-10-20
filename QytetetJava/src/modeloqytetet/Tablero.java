////////////////////////////////////////////////////////////////////////////////
// Autor: Alejandro Manzanares Lemus
// 2º GII A1
////////////////////////////////////////////////////////////////////////////////
package modeloqytetet;
import java.util.ArrayList;

public class Tablero {
    //Atributos
    private ArrayList<Casilla> casillas = new ArrayList<Casilla>();
    private Casilla carcel;

    //Constructor
    public Tablero() {
        inicializar();
    }
    
    //Metodos
    boolean esCasillaCarcel(int numeroCasilla){
        boolean escarcel = false;
        if(numeroCasilla == carcel.getNumeroCasilla()){
            escarcel = true;
        }
        return escarcel;
    }
    public Casilla getCarcel() {
        return carcel;
    }
    public ArrayList<Casilla> getCasillas() {
        return casillas;
    }
    private void inicializar() {
        casillas = new ArrayList<Casilla>();
        TituloPropiedad Navajazo = new TituloPropiedad(
                "Calle Navajazo en el pecho",
        500, 50, (float) -0.1, 250, 150);
        TituloPropiedad Mariajuana = new TituloPropiedad(
                "Plaza de Maria y Juana",
        550, 55, (float) 0.1, 300, 160);
        TituloPropiedad LuciaS = new TituloPropiedad(
                "Calle Lucia S. y diamantes",
        600, 60, (float) 0.11, 315, 200);
        TituloPropiedad Mediocre = new TituloPropiedad(
                "Plaza Mediocre",
        600, 60, (float) 0.11, 315, 210);
        TituloPropiedad Mumotopia = new TituloPropiedad(
                "Calle Mumotopia",
        500, 75, (float) 0.2, 320, 270);
        TituloPropiedad Piltrafa = new TituloPropiedad(
                "Callejon de la piltrafa spooky",
        650, 80,(float) 0.15, 320, 350);
        TituloPropiedad Fantasma = new TituloPropiedad(
                "Calle Fantasma que recorre Europa",
        700, 80, (float) 0.15, 350, 400);
        TituloPropiedad Casinos = new TituloPropiedad(
                "Avenida Casinos",
        750, 85, (float) 0.15, 400, 450);
        TituloPropiedad Joestar = new TituloPropiedad(
                "Plaza Joestar",
        800, 85, (float) 0.15, 415, 550);
        TituloPropiedad Iluminada = new TituloPropiedad(
                "Calle Iluminada",
        850 , 90, (float) 0.15, 500, 600);
        TituloPropiedad Ariedad = new TituloPropiedad(
                "Avenida Ariedad",
        900, 95, (float) 0.15, 600, 700);
        TituloPropiedad Illuminati = new TituloPropiedad(
                "Parque Illuminati",
        1000, 100, (float) 0.2, 700, 900);
        
        casillas.add(new OtraCasilla(0, TipoCasilla.SALIDA));
        casillas.add(new Calle(1, Navajazo));
        casillas.add(new OtraCasilla(2, TipoCasilla.JUEZ));
        casillas.add(new Calle(3, Mariajuana));
        casillas.add(new Calle(4, LuciaS));
        casillas.add(new OtraCasilla(5,TipoCasilla.SORPRESA));
        casillas.add(new Calle(6, Mediocre));
        casillas.add(new OtraCasilla(7,TipoCasilla.IMPUESTO));
        casillas.add(new Calle(8, Mumotopia));
        casillas.add(new Calle(9, Piltrafa));
        casillas.add(new OtraCasilla(10, TipoCasilla.CARCEL));
        casillas.add(new Calle(11, Fantasma));
        casillas.add(new OtraCasilla(12,TipoCasilla.SORPRESA));
        casillas.add(new Calle(13, Casinos));
        casillas.add(new Calle(14, Joestar));
        casillas.add(new OtraCasilla(15, TipoCasilla.PARKING));
        casillas.add(new Calle(16, Iluminada));
        casillas.add(new OtraCasilla(17,TipoCasilla.SORPRESA));
        casillas.add(new Calle(18, Ariedad));
        casillas.add(new Calle(19, Illuminati));
        
        carcel = casillas.get(10);
    }  
    Casilla obtenerCasillaFinal(Casilla casilla, int desplazamiento){
       int tam = casillas.size();
       int numCasillaFinal = (casilla.getNumeroCasilla()+desplazamiento)%tam;
       return casillas.get(numCasillaFinal);
    }
    Casilla obtenerCasillaNumero(int numeroCasilla){
       int numCasillaObtenida = numeroCasilla;
       return casillas.get(numCasillaObtenida);
    }
    @Override
    public String toString() {
        return "Tablero{" + "casillas=" + casillas + ", carcel=" + carcel + '}';
    }
}
