#///////////////////////////////////////////////////////////////////////////////
#// Autor: Alejandro Manzanares Lemus
#// 2º GII A1
#///////////////////////////////////////////////////////////////////////////////
module ModeloQytetet
class Calle < Casilla

  def initialize(numero, titulo)
    type = TipoCasilla::CALLE
    super(numero, type)
    @coste = titulo.precio_compra
    @titulo = titulo
  end
  
  attr_accessor :titulo
  
  def tengo_propietario()
      return @titulo.tengo_propietario
  end 
  def asignar_propietario(jugador)
      @titulo.propietario = jugador
      return @titulo
  end
  def pagar_alquiler()
    coste_alquiler = @titulo.pagar_alquiler()
  end
  def propietario_encarcelado()
    return @titulo.propietario_encarcelado
  end
  def to_s()
    imprimir = super + "Titulo:#{@titulo}"
    imprimir
  end
  
  private :titulo=
end
end