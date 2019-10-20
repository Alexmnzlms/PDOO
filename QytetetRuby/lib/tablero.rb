#///////////////////////////////////////////////////////////////////////////////
#// Autor: Alejandro Manzanares Lemus
#// 2º GII A1
#///////////////////////////////////////////////////////////////////////////////
module ModeloQytetet
  class Tablero
    
    def initialize()
      @casillas
      @carcel
      inicializar
    end

    attr_reader :casillas, :carcel
    
    def es_casilla_carcel(numero_casilla)
      escarcel = false
      if numero_casilla == @carcel.numero_casilla
        escarcel = true
      end
      
      return escarcel
    end
    def obtener_casilla_final(casilla, desplazamiento)
       tam = @casillas.length()
       num_casilla_final = (casilla.numero_casilla + desplazamiento) % tam
       @casillas[num_casilla_final]
    end     
    def obtener_casilla_numero(numero_casilla)
      @casillas[numero_casilla]

    end
    def to_s()
      "TABLERO:
Casillas: #{@casillas}
Carcel: #{@carcel}"
    end
    
    private
    
    def inicializar()
      @casillas = Array.new
      @casillas<< Casilla.new(0, TipoCasilla::SALIDA)
      @casillas<< Calle.new(1, TituloPropiedad.new("Calle Navajazo en el pecho", 
          500, 50, -0.1, 250, 150))
      @casillas<< Casilla.new(2,TipoCasilla::JUEZ)
      @casillas<< Calle.new(3, TituloPropiedad.new("Plaza de Maria y Juana",
          550, 55, 0.1, 300, 160))
      @casillas<< Calle.new(4, TituloPropiedad.new("Calle Lucia S. y diamantes",
          600, 60, 0.11, 315, 200))
      @casillas<< Casilla.new(5,TipoCasilla::SORPRESA)
      @casillas<< Calle.new(6, TituloPropiedad.new("Plaza Mediocre",
          600, 60, 0.11, 315, 210))
      @casillas<< Casilla.new(7,TipoCasilla::IMPUESTO)
      @casillas<< Calle.new(8, TituloPropiedad.new("Calle Mumotopia",
          500, 75, 0.2, 320, 270))
      @casillas<< Calle.new(9, TituloPropiedad.new("Callejon de la piltrafa spooky",
          650, 80,0.15, 320, 350))
      @casillas<< Casilla.new(10, TipoCasilla::CARCEL)
      @casillas<< Calle.new(11, TituloPropiedad.new("Calle Fantasma que recorre Europa",
          700, 80, 0.15, 350, 400))
      @casillas<< Casilla.new(12,TipoCasilla::SORPRESA)
      @casillas<< Calle.new(13, TituloPropiedad.new("Avenida Casinos",
          750, 85, 0.15, 400, 450))
      @casillas<< Calle.new(14, TituloPropiedad.new("Plaza Joestar",
          800, 85, 0.15, 415, 550))
      @casillas<< Casilla.new(15,TipoCasilla::PARKING)
      @casillas<< Calle.new(16, TituloPropiedad.new("Calle Iluminada",
          850 , 90, 0.15, 500, 600))
      @casillas<< Casilla.new(17,TipoCasilla::SORPRESA)
      @casillas<< Calle.new(18, TituloPropiedad.new("Avenida Ariedad",
          900, 95, 0.15, 600, 700))
      @casillas<< Calle.new(19, TituloPropiedad.new("Parque Illuminati",
          1000, 100, 0.2, 700, 900))
      @carcel = @casillas[10]
    end
  end
end
