  #///////////////////////////////////////////////////////////////////////////////
#// Autor: Alejandro Manzanares Lemus
#// 2º GII A1
#///////////////////////////////////////////////////////////////////////////////
require "singleton"
module ModeloQytetet
  class Dado
    include Singleton
    
    def initialize
      @valor
    end
    
    attr_reader :valor
    
    def tirar()
      @valor = rand(1..6)
    end
    def to_s()
      "valor: #{@valor}"
    end
    
  end
end
