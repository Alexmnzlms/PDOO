#///////////////////////////////////////////////////////////////////////////////
#// Autor: Alejandro Manzanares Lemus
#// 2º GII A1
#///////////////////////////////////////////////////////////////////////////////
module ModeloQytetet
  class Jugador
    def initialize(encar, nom, sald, cart_lib, prop, cas_act)
      @encarcelado = encar
      @nombre = nom
      @saldo = sald
      @carta_libertad = cart_lib
      @propiedades = prop
      @casilla_actual = cas_act
    end
    def self.nuevo(nom)
        self.new(false, nom, 7500, nil, Array.new, nil)
    end
    def self.copia(otro_jugador)
      self.new(otro_jugador.encarcelado, otro_jugador.nombre, otro_jugador.saldo, otro_jugador.carta_libertad, otro_jugador.propiedades, otro_jugador.casilla_actual)
    end
    
    attr_reader :nombre, :propiedades, :saldo
    attr_accessor :carta_libertad, :casilla_actual, :encarcelado
    
    def cancelar_hipoteca(titulo)
      cancelacion = false
      if tengo_saldo(titulo.calcular_coste_cancelar())
        modificar_saldo(-titulo.calcular_coste_cancelar())
        titulo.cancelar_hipoteca()
        cancelacion = true
      end
      return cancelacion
    end
    def comprar_titulo_propiedad()
      coste_compra = @casilla_actual.coste
      comprado = false
      if coste_compra < @saldo
        titulo = @casilla_actual.asignar_propietario(self)
        @propiedades<< titulo
        modificar_saldo(-coste_compra)
        comprado = true
      end
      return comprado
    end
    #convertirme
    def cuantas_casas_hoteles_tengo()
      num_casas_hoteles = 0
      for propiedad in @propiedades
        num_casas_hoteles = num_casas_hoteles + propiedad.num_casas + propiedad.num_hoteles
      end
      
      return num_casas_hoteles
    end
    #debo_ir_a_carcel
    def debo_pagar_alquiler()
      debo_pagar = false
      titulo = @casilla_actual.titulo
      if !es_de_mi_propiedad(titulo)
        tiene_propietario = titulo.tengo_propietario()
        if tiene_propietario
          encarcelado = titulo.propietario_encarcelado()
          if !encarcelado
            esta_hipotecada = titulo.hipotecada
            if !esta_hipotecada
              debo_pagar = true
            end
          end
        end
      end
      return debo_pagar
    end
    def devolver_carta_libertad()
      carta_libertad_devuelta = @carta_libertad
      @carta_libertad = nil
      
      return carta_libertad_devuelta
    end
    #edificar_casa
    #edificar_hotel
    def eliminar_de_mis_propiedades(titulo)
      cont = 0
      for aux in @propiedades
        if titulo == aux
          @propiedades.delete_at(cont)
        end
        cont += 1
      end
      titulo.propietario = nil
    end
    def es_de_mi_propiedad(titulo)  
      es_mia = false
      nombre_titulo = titulo.nombre
      for propiedad in @propiedades
        if nombre_titulo.eql?(propiedad.nombre)
          es_mia = true
        end
      end
      return es_mia 
    end
    def estoy_en_calle_libre()
      raise NotImplementedError
    end
    def hipotecar_propiedad(titulo)
      coste_hipoteca = titulo.hipotecar()
      modificar_saldo(coste_hipoteca)
    end
    def ir_a_carcel(casilla)
      @casilla_actual = casilla
      @encarcelado = true
    end
    def modificar_saldo(cantidad)
      @saldo = @saldo + cantidad
    end
    def obtener_capital()
      capital = @saldo
      for propiedad in @propiedades
        num_casas_propiedad = propiedad.num_casas
        num_hoteles_propiedad = propiedad.num_hoteles
        precio_compra_propiedad = propiedad.precio_compra
        precio_edificar_propiedad = propiedad.precio_edificar
        valor_propiedad = (num_casas_propiedad+num_hoteles_propiedad)*precio_edificar_propiedad+precio_compra_propiedad
        if propiedad.hipotecada
          valor_propiedad = valor_propiedad - propiedad.hipoteca_base
        end
        capital = capital + valor_propiedad
      end
      return capital
    end
    def obtener_propiedades(hipotecada)
      propiedades_segun_hipoteca = Array.new
      for propiedad in @propiedades
        if propiedad.hipotecada == hipotecada
          propiedades_segun_hipoteca<< propiedad
        end
      end
      return propiedades_segun_hipoteca
    end 
    def pagar_alquiler()
      coste_alquiler = @casilla_actual.pagar_alquiler()
      modificar_saldo(-coste_alquiler)
    end
    #pagar_impuesto
    def pagar_libertad(cantidad)
      tengo_saldo = tengo_saldo(cantidad)
      if tengo_saldo
        @encarcelado = false
        modificar_saldo(-cantidad)
      end
    end
    #puedo_edificar_casa
    #puedo_edificar_hotel
    def tengo_carta_libertad()
      tengo = false
      if !@carta_libertad.nil?
        tengo = true
      end 
      return tengo
    end
    #tengo_saldo
    def vender_propiedad(casilla)
      titulo = casilla.titulo
      eliminar_de_mis_propiedades(titulo)
      precio_venta = titulo.calcular_precio_venta()
      modificar_saldo(precio_venta)
    end
    def to_s()
      imprirmir = "JUGADOR:
Encarcelado: #{@encarcelado}
Nombre: #{@nombre}
Saldo: #{@saldo}
Carta_libertad: #{@carta_libertad}
Casilla_actual: #{@casilla_actual}"
      for propiedad in @propiedades
        imprirmir += propiedad.to_s
      end
      imprirmir += "Capital: #{obtener_capital}"
    end
    def <=>(otro_jugador)
      otro_jugador.obtener_capital <=> obtener_capital
    end
    def convertirme(fianza)
      e = Especulador.copia(self, fianza)
      e
    end
    def debo_ir_a_carcel()
      !tengo_carta_libertad()
    end
    def edificar_casa(titulo)
      edificada = false
      if puedo_edificar_casa(titulo)
        coste_edificar_casa = titulo.precio_edificar
        tengo_saldo = tengo_saldo(coste_edificar_casa)
        if tengo_saldo
          titulo.edificar_casa()
          modificar_saldo(-coste_edificar_casa)
          edificada = true
        end
      end
      return edificada
    end
    def edificar_hotel(titulo)
      edificada = false
      if puedo_edificar_hotel(titulo)
        coste_edificar_hotel = titulo.precio_edificar
        tengo_saldo = tengo_saldo(coste_edificar_hotel)
        if tengo_saldo
          titulo.edificar_hotel()
          modificar_saldo(-coste_edificar_hotel)
          edificada = true
        end
      end
      return edificada
    end
    def pagar_impuesto()
      @saldo = @saldo - @casilla_actual.coste
    end
    def puedo_edificar_casa(titulo)
        puedo = false
        if titulo.num_casas < 4
            puedo = true
        end
        puedo
    end
    def puedo_edificar_hotel(titulo)
        puedo = false
        if titulo.num_hoteles < 4 && titulo.num_casas == 4
            puedo = true
        end
        puedo
    end
    def tengo_saldo(cantidad)
      tengo = false
      if @saldo > cantidad
        tengo = true
      end
      return tengo
    end
    
    private :tengo_saldo, :eliminar_de_mis_propiedades, :es_de_mi_propiedad
    #protected :convertirme, :debo_ir_a_carcel, :pagar_impuesto, 
    protected :puedo_edificar_casa, :puedo_edificar_hotel, :tengo_saldo
  end
 end