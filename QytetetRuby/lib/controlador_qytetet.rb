#///////////////////////////////////////////////////////////////////////////////
#// Autor: Alejandro Manzanares Lemus
#// 2º GII A1
#///////////////////////////////////////////////////////////////////////////////
require "singleton"
require_relative "qytetet.rb"
require_relative "jugador.rb"
require_relative "opcion_menu.rb"
require_relative "estado_juego.rb"
require_relative "metodo_salir_carcel.rb"
module ModuloControladorQytetet
  
  class ControladorQytetet
#    include Qytetet
#    include Jugador
    include Singleton
    
    @@modelo = ModeloQytetet::Qytetet.instance
    
    def initialize
      @nombre_jugadores = Array.new  
    end
    
    attr_accessor :nombre_jugadores
    
    def obtener_operaciones_juego_validas()
      operaciones = Array.new
      estado = @@modelo.estado_juego
      if @@modelo.jugadores.empty?
        operaciones<<OpcionMenu.index(:INICIARJUEGO)
      else
        case estado
        when ModeloQytetet::EstadoJuego::ALGUNJUGADORENBANCAROTA
          operaciones<<OpcionMenu.index(:OBTENERRANKING)
          
        when ModeloQytetet::EstadoJuego::JA_ENCARCELADO
          operaciones<<OpcionMenu.index(:PASARTURNO)
          
        when ModeloQytetet::EstadoJuego::JA_PREPARADO
          operaciones<<OpcionMenu.index(:JUGAR)
          
        when ModeloQytetet::EstadoJuego::JA_ENCARCELADOCONOPCIONDELIBERTAD
          operaciones<<OpcionMenu.index(:INTENTARSALIRCARCELPAGANDOLIBERTAD)
          operaciones<<OpcionMenu.index(:INTENTARSALIRCARCELTIRANDODADO) 
        
        when ModeloQytetet::EstadoJuego::JA_PUEDEGESTIONAR
          operaciones<<OpcionMenu.index(:HIPOTECARPROPIEDAD)
          operaciones<<OpcionMenu.index(:CANCELARHIPOTECA)
          operaciones<<OpcionMenu.index(:EDIFICARCASA)
          operaciones<<OpcionMenu.index(:EDIFICARHOTEL)
          operaciones<<OpcionMenu.index(:VENDERPROPIEDAD)
          operaciones<<OpcionMenu.index(:PASARTURNO)
          
        when ModeloQytetet::EstadoJuego::JA_PUEDECOMPRAROGESTIONAR
          operaciones<<OpcionMenu.index(:COMPRARTITULOPROPIEDAD)
          operaciones<<OpcionMenu.index(:HIPOTECARPROPIEDAD)
          operaciones<<OpcionMenu.index(:CANCELARHIPOTECA)
          operaciones<<OpcionMenu.index(:EDIFICARCASA)
          operaciones<<OpcionMenu.index(:EDIFICARHOTEL)
          operaciones<<OpcionMenu.index(:VENDERPROPIEDAD)
          operaciones<<OpcionMenu.index(:PASARTURNO)
          
        when ModeloQytetet::EstadoJuego::JA_CONSORPRESA
          operaciones<<OpcionMenu.index(:APLICARSORPRESA)
          
        end
        operaciones<<(OpcionMenu.index(:TERMINARJUEGO))
        operaciones<<(OpcionMenu.index(:MOSTRARJUGADORACTUAL))
        operaciones<<(OpcionMenu.index(:MOSTRARJUGADORES))
        operaciones<<(OpcionMenu.index(:MOSTRARTABLERO))
      end
    end
    def necesita_elegir_casilla(opcion_menu)
      necesita = false
      aux = Array.new
      aux<<(OpcionMenu.index(:HIPOTECARPROPIEDAD))
      aux<<(OpcionMenu.index(:CANCELARHIPOTECA))
      aux<<(OpcionMenu.index(:EDIFICARCASA))
      aux<<(OpcionMenu.index(:EDIFICARHOTEL))
      aux<<(OpcionMenu.index(:VENDERPROPIEDAD))

      if aux.include?(opcion_menu)
          necesita = true
      end
      necesita
      
    end
    def obtener_casillas_validas(opcion_menu)
      aux = Array.new
      if opcion_menu == OpcionMenu.index(:HIPOTECARPROPIEDAD)||
         opcion_menu == OpcionMenu.index(:EDIFICARCASA) ||
         opcion_menu == OpcionMenu.index(:EDIFICARHOTEL) ||
         opcion_menu == OpcionMenu.index(:VENDERPROPIEDAD)
          aux= @@modelo.obtener_propiedades_jugador_segun_estado_hipoteca(false);
      else 
        if opcion_menu == OpcionMenu.index(:CANCELARHIPOTECA)
          aux = @@modelo.obtener_propiedades_jugador_segun_estado_hipoteca(true);
        end
      end
      aux
    end
    
    def realizar_operacion(operacion_elegida, casilla_elegida)
      opcion = OpcionMenu[operacion_elegida];
      mensaje = ""
      case opcion

      when :INICIARJUEGO
         @@modelo.inicializar_juego(@nombre_jugadores)
         mensaje += realizar_operacion(OpcionMenu.index(:MOSTRARJUGADORES),0)
         mensaje += "\n\n"
         mensaje += "Empieza el jugador:\n"
         mensaje += @@modelo.jugador_actual.to_s()
         mensaje += "\n\n"
      
      when :JUGAR
         @@modelo.jugar()
         mensaje += "Valor del dado: "
         mensaje += @@modelo.get_valor_dado.to_s()
         mensaje += "\n\n"
         mensaje += @@modelo.jugador_actual.to_s()
         mensaje += "\n\n"
      
      when :APLICARSORPRESA
          mensaje += "La carta sorpresa es:\n"
          mensaje += @@modelo.carta_actual.to_s()
          @@modelo.aplicar_sorpresa()
          mensaje += "\n\n"
          
      when :INTENTARSALIRCARCELPAGANDOLIBERTAD
          aux = @@modelo.intentar_salir_carcel(MetodoSalirCarcel::PAGANDOLIBERTAD);
          if aux
              mensaje += "Has salido de la carcel, enhorabuena\n"
              mensaje += "\n\n"
          
          else
              mensaje += "Te quedas donde estas majo\n"
              mensaje += "\n\n"
          end
      
      when :INTENTARSALIRCARCELTIRANDODADO
          aux = @@modelo.intentar_salir_carcel(MetodoSalirCarcel::TIRANDODADO);
          if aux
              mensaje += "Has salido de la carcel, enhorabuena\n"
              mensaje += "\n\n"
          
          else
              mensaje += "Te quedas donde estas majo\n"
              mensaje += "\n\n"
          end
      
      when :COMPRARTITULOPROPIEDAD
          aux = @@modelo.comprar_titulo_propiedad()
          if aux
              mensaje += "Acabas de adquirir una propiedad, enhorabuena\n"
              mensaje += "\n\n"
          
          else
              mensaje += "Te quedas como estas majo\n"
              mensaje += "\n\n"
          end
      
      when :HIPOTECARPROPIEDAD
          @@modelo.hipotecar_propiedad(casilla_elegida)
          mensaje += "\n\n"
      
      when :CANCELARHIPOTECA
          aux = @@modelo.cancelar_hipoteca(casilla_elegida)
          if aux
              mensaje += "Acabas de cancelar una hipoteca, enhorabuena\n"
              mensaje += "\n\n"
          
          else
              mensaje += "Te quedas como estas majo\n"
              mensaje += "\n\n"
          end
      
      when :EDIFICARCASA
          aux = @@modelo.edificar_casa(casilla_elegida)
          if aux
              mensaje += "Acabas de edificar una casa enhorabuena\n"
              mensaje += "\n\n"
          
          else
              mensaje += "Te quedas como estas majo\n"
              mensaje += "\n\n"
          end
      
      when :EDIFICARHOTEL
          aux = @@modelo.edificar_hotel(casilla_elegida)
          if aux
              mensaje += "Acabas de edificar un hotel, enhorabuena\n"
              mensaje += "\n\n"
          
          else
              mensaje += "Te quedas como estas majo\n"
              mensaje += "\n\n"
          end
      
      when :VENDERPROPIEDAD
          @@modelo.vender_propiedad(casilla_elegida)
          mensaje += "\n\n"
      
      when :PASARTURNO
          mensaje += "Pasando el turno al jugador\n"
          @@modelo.siguiente_jugador()
          mensaje += @@modelo.jugador_actual.to_s()
          mensaje += "\n\n"
      
      when :OBTENERRANKING
          mensaje += "\n\n"
          @@modelo.obtener_ranking()
          mensaje += realizar_operacion(OpcionMenu.index(:MOSTRARJUGADORES),0)
          mensaje += "\n\n"
      
      when :TERMINARJUEGO
          mensaje += "\n\n"
          puts "GRACIAS POR JUGAR A QYTETET (Esta version esta en fase alpha, disculpen las molestias)"
          exit(0)
      
      when :MOSTRARJUGADORACTUAL
          mensaje += "\n\n"
          mensaje += @@modelo.jugador_actual.to_s()
          mensaje += "\n\n"
      
      when :MOSTRARJUGADORES
          mensaje += "\n\n"
          for jugador in @@modelo.jugadores
             mensaje += jugador.to_s()
             mensaje += "\n\n"
          end
          mensaje += "\n\n"
      
      when :MOSTRARTABLERO
          mensaje += "\n\n"
          for casilla in @@modelo.tablero.casillas
            mensaje += casilla.to_s()
            mensaje += "\n\n"
          end
          mensaje += "\n\n"
      
      end
      return mensaje
    end
  end
end

