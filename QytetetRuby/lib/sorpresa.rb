#///////////////////////////////////////////////////////////////////////////////
#// Autor: Alejandro Manzanares Lemus
#// 2º GII A1
#///////////////////////////////////////////////////////////////////////////////
module ModeloQytetet
  class Sorpresa
    def initialize(desc, val, type)
      @texto = desc
      @tipo = type
      @valor = val
    end
    
    attr_reader:texto, :valor, :tipo

    def to_s()
      "Texto: #{@texto}
Valor: #{@valor}
Tipo: #{@tipo}"
    end
  end
end
