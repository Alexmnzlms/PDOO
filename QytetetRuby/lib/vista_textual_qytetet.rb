#///////////////////////////////////////////////////////////////////////////////
#// Autor: Alejandro Manzanares Lemus
#// 2º GII A1
#///////////////////////////////////////////////////////////////////////////////
require_relative "qytetet.rb"
require_relative "controlador_qytetet.rb"
require_relative "opcion_menu.rb"
module ModuloVistaTextualQytetet
  
  class VistaTextualQytetet
#    include Qytetet
#    include ControladorQytetet
    
    @@controlador = ModuloControladorQytetet::ControladorQytetet.instance
    
    def initialize
      
    end
    
    def obtener_nombre_jugadores()
      puts "Introduce el numero de jugadores: "
     numero_jugadores_valido = Array.new
      for i in 2..ModeloQytetet::Qytetet.get_max_jugadores()
          numero_jugadores_valido<<(i.to_s);
      end

      num_jugadores = leer_valor_correcto(numero_jugadores_valido).to_i()

      nombres = Array.new
      for i in 0..num_jugadores-1
          puts "Introduce el nombre del jugador: "
          nombres<<(gets.chomp())
      end
      nombres
    end
    
    def elegir_operacion()
      aux = @@controlador.obtener_operaciones_juego_validas()
      if aux.empty?
          return (-1)
      else
          aux_str = Array.new
          for numero in aux
              aux_str<<(numero.to_s())
          end
          leer_valor_correcto(aux_str).to_i()
      end
    end
    
    def elegir_casilla(opcion_menu)
      aux = @@controlador.obtener_casillas_validas(opcion_menu);
      if aux.empty?
          return (-1)
      else
          aux_str = Array.new
          for numero in aux
              aux_str<<(numero.to_s())
          end
          leer_valor_correcto(aux_str).to_i()
      end
    end
    
    def leer_valor_correcto(valores_correctos)
      entrada = ""
      loop do
        entrada = gets.chomp
        break if valores_correctos.include?(entrada)
        end
      entrada
    end
    
    def imprimir_operaciones_juego_validas()
        aux = @@controlador.obtener_operaciones_juego_validas()
        for i in aux
            puts "#{i}  #{ModuloControladorQytetet::OpcionMenu[i]}"
        end
    end
    
    def self.main()
      ui = VistaTextualQytetet.new
      @@controlador.nombre_jugadores = ui.obtener_nombre_jugadores()
      operacion_elegida = 0
      casilla_elegida = 0

      loop do
        ui.imprimir_operaciones_juego_validas()
        operacion_elegida = ui.elegir_operacion()
        necesita_elegir_casilla = @@controlador.necesita_elegir_casilla(operacion_elegida);
        if necesita_elegir_casilla
            puts "Por favor, elige la casilla:\n"
            casilla_elegida = ui.elegir_casilla(operacion_elegida)
        end
        if !necesita_elegir_casilla || casilla_elegida >= 0
          puts @@controlador.realizar_operacion(operacion_elegida, casilla_elegida)
        end
      end
    end
  end
  VistaTextualQytetet.main
end
