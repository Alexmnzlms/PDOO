#///////////////////////////////////////////////////////////////////////////////
#// Autor: Alejandro Manzanares Lemus
#// 2º GII A1
#///////////////////////////////////////////////////////////////////////////////
module ModeloQytetet
  class Especulador < Jugador 
    
    def self.copia(jugador,fianza)
      e = super(jugador)
      e.fianza = fianza
      e
    end
    
    attr_writer :fianza
    
    def debo_ir_a_carcel
      debo = false
      if super && !pagar_fianza()
        debo = true
      end
      debo
    end
    def convertirme(fianza)
      self
    end
    def pagar_fianza()
      pago = false
      if tengo_saldo(fianza)
        pago = true
        modificar_saldo(fianza)
      end
      pago
    end
    def pagar_impuesto()
      @saldo = @saldo - @casilla_actual.coste / 2
    end
    def puedo_edificar_casa(titulo)
        puedo = false;
        if titulo.num_casas < 8
            puedo = true;
        end
        puedo;
    end
    def puedo_edificar_hotel(titulo)
        puedo = false;
        if titulo.num_hoteles < 8 && titulo.num_casas == 4
            puedo = true;
        end
        puedo;
    end
    def to_s()
      imprimir = super + "Fianza: #{@fianza}"
      imprimir
    end
    
    private :pagar_fianza
    protected :pagar_impuesto, :initialize, :convertirme, :debo_ir_a_carcel,
      :puedo_edificar_casa, :puedo_edificar_hotel
  end
end