#///////////////////////////////////////////////////////////////////////////////
#// Autor: Alejandro Manzanares Lemus
#// 2º GII A1
#///////////////////////////////////////////////////////////////////////////////
module ModeloQytetet
  class Casilla
    
    def initialize(numero, type)
      @numero_casilla = numero
      @tipo = type
      @coste = 0
    end
    
    attr_reader :numero_casilla, :tipo, :coste
    
    
    def soy_edificable()
      soy = false
      if @tipo == TipoCasilla::CALLE
        soy = true
      end     
      return soy
    end 
    
    def to_s()
      "NumeroCasilla:#{@numero_casilla}
Tipo:#{@tipo}
Coste:#{@coste}"
    end
    

  end
end
#
#
#def self.crear_casilla_no_titulo(numero, type)
#  self.new(numero, type, nil, 0)
#end
#def self.crear_casilla_titulo(numero, type, title)
#  self.new(numero, type, title, title.precio_compra)
#end
