#///////////////////////////////////////////////////////////////////////////////
#// Autor: Alejandro Manzanares Lemus
#// 2º GII A1
#///////////////////////////////////////////////////////////////////////////////
module ModeloQytetet
  class TituloPropiedad
    def initialize(nom, precioC, alquiler, factor, precioE, hipoteca)
        @nombre = nom
        @hipotecada = false
        @precio_compra = precioC
        @alquiler_base = alquiler
        @factor_revalorizacion = factor
        @hipoteca_base = hipoteca
        @precio_edificar = precioE
        @num_casas = 0
        @num_hoteles = 0
        @propietario = nil
    end

    attr_reader :nombre, :precio_compra, :alquiler_base, :factor_revalorizacion,
      :hipoteca_base, :precio_edificar, :num_casas, :num_hoteles
    attr_accessor :hipotecada, :propietario
    
    def calcular_coste_cancelar()
      coste_cancelar = (calcular_coste_hipotecar() + calcular_coste_hipotecar() *0.1).to_i
    end
    def calcular_coste_hipotecar()
      coste_hipoteca = @hipoteca_base + (@num_casas * 0.5 * @hipoteca_base + @num_hoteles * @hipoteca_base)#.to_i
    end
    def calcular_importe_alquiler()
      coste_alquiler = @alquiler_base + (@num_casas*0.5 + @num_hoteles*2).to_i
    end
    def calcular_precio_venta()
      precio_venta = @precio_compra + (@num_casas + @num_hoteles) * @precio_edificar * @factor_revalorizacion
    end
    def cancelar_hipoteca()
      @hipotecada = false
    end
    def cobrar_alquiler(coste)
      raise NotImplementedError
    end
    def edificar_casa()
      @num_casas +=1
    end
    def edificar_hotel()
      @num_hoteles +=1
    end
    def hipotecar()
      @hipotecada = true
      coste_hipoteca = calcular_coste_hipotecar
    end
    def pagar_alquiler()
      coste_alquiler = calcular_importe_alquiler()
      @propietario.modificar_saldo(coste_alquiler)
    end
    def propietario_encarcelado()
      return @encarcelado
    end
    def tengo_propietario()
      return !@propietario.nil?
    end
    def to_s()
      if (@propietario.nil?)
        nombreprop = "Nadie"
      else 
        nombreprop = @propietario.nombre
      end
      "TITULO_PROPIEDAD:
Nombre:#{@nombre}
Propietario:#{nombreprop}
Hipotecada:#{@hipotecada}
Precio_compra:#{@precio_compra}
Alquiler_base:#{@alquiler_base}
Factor_revalorizacion:#{@factor_revalorizacion}
Hipoteca_base:#{@hipoteca_base}
Precio_edificar:#{@precio_edificar}
Num_casas:#{@num_casas}
Num_hoteles:#{@num_hoteles}"
    end
  end
end