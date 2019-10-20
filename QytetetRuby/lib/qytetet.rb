#///////////////////////////////////////////////////////////////////////////////
#// Autor: Alejandro Manzanares Lemus
#// 2º GII A1
#///////////////////////////////////////////////////////////////////////////////
require "singleton"
require_relative "sorpresa.rb"
require_relative "tipo_sorpresa.rb"
require_relative "qytetet.rb"
require_relative "tablero.rb"
require_relative "tipo_casilla.rb"
require_relative "titulo_propiedad.rb"
require_relative "casilla.rb"
require_relative "dado.rb"
require_relative "jugador.rb"
require_relative "metodo_salir_carcel.rb"
require_relative "estado_juego.rb"
require_relative "especulador.rb"
require_relative "calle.rb"

module ModeloQytetet
  class Qytetet
    include Singleton
    
    @@MAX_JUGADORES = 4
    @@NUM_SORPRESAS = 10
    @@NUM_CASILLAS = 20
    @@PRECIO_LIBERTAD = 200 
    @@SALDO_SALIDA = 1000
    
    def initialize
      @carta_actual
      @jugadores = Array.new
      @jugador_actual
      @mazo = Array.new
      @tablero
      @dado = Dado.instance
      @estado_juego
    end
    
    attr_accessor :carta_actual
    attr_reader:mazo, :tablero, :dado, :jugador_actual, :jugadores, :estado_juego
    
    def self.get_max_jugadores()
      @@MAX_JUGADORES
    end
    
    def inicializar_juego(nombres)
      inicializar_jugadores(nombres)
      inicializar_tablero()
      inicializar_cartas_sorpresa()
      salida_jugadores()
    end
    def actuar_si_en_casilla_edificable()
      debo_pagar = @jugador_actual.debo_pagar_alquiler
      if debo_pagar
        @jugador_actual.pagar_alquiler
        if @jugador_actual.saldo <= 0
          @estado_juego = EstadoJuego::ALGUNJUGADORENBANCAROTA
        end
      end
      casilla = obtener_casilla_jugador_actual()
      tengo_propietario = casilla.tengo_propietario
      if @estado_juego != EstadoJuego::ALGUNJUGADORENBANCAROTA
        if tengo_propietario
          @estado_juego = EstadoJuego::JA_PUEDEGESTIONAR
        else
          @estado_juego = EstadoJuego::JA_PUEDECOMPRAROGESTIONAR
        end
      end
    end
    def actuar_si_en_casilla_no_edificable()
      @estado_juego = EstadoJuego::JA_PUEDEGESTIONAR
      casilla_actual = @jugador_actual.casilla_actual
      if casilla_actual.tipo == TipoCasilla::IMPUESTO
        @jugador_actual.pagar_impuesto
        if @jugador_actual.saldo <= 0
          @estado_juego = EstadoJuego::ALGUNJUGADORENBANCAROTA
        end
      else
        if casilla_actual.tipo == TipoCasilla::JUEZ
          encarcelar_jugador
        elsif casilla_actual.tipo == TipoCasilla::SORPRESA
          @carta_actual = @mazo[0]
          @mazo.delete_at(0)
          @estado_juego = EstadoJuego::JA_CONSORPRESA
        end
      end
    end
    def aplicar_sorpresa
      @estado_juego = EstadoJuego::JA_PUEDEGESTIONAR
      if @carta_actual.tipo == TipoSorpresa::SALIRCARCEL
        @jugador_actual.carta_libertad = @carta_actual
      else
        if @carta_actual.tipo == TipoSorpresa::PAGARCOBRAR
          @jugador_actual.modificar_saldo(@carta_actual.valor)
          if @jugador_actual.saldo <= 0
            @estado_juego = EstadoJuego::ALGUNJUGADORENBANCAROTA
          end
        elsif @carta_actual.tipo == TipoSorpresa::IRACASILLA
          valor = @carta_actual.valor
          casilla_carcel = @tablero.es_casilla_carcel(valor)
          if casilla_carcel
            encarcelar_jugador
          else
            mover(valor)
          end
        elsif @carta_actual.tipo == TipoSorpresa::PORCASAHOTEL
          cantidad = @carta_actual.valor
          numero_total = @jugador_actual.cuantas_casas_hoteles_tengo()
          @jugador_actual.modificar_saldo(cantidad*numero_total)
          if @jugador_actual.saldo < 0
            @estado_juego = EstadoJuego::ALGUNJUGADORENBANCAROTA
          end
        elsif @carta_actual.tipo == TipoSorpresa::PORJUGADOR
          for jugador in 0..@jugadores.length
            if jugador != @jugador_actual
              jugador.modificar_saldo(@carta_actual.valor)
              if jugador.saldo <= 0
                @estado_juego = EstadoJuego::ALGUNJUGADORENBANCAROTA
              end
              @jugador_actual.modificar_saldo(-@carta_Actual.valor)
              if @jugador_actual.saldo <= 0
                @estado_juego = EstadoJuego::ALGUNJUGADORENBANCAROTA
              end
            end
          end
        elsif @carta_actual.tipo == TipoSorpresa::CONVERTIRME
          fianza = @carta_actual.valor
          contador = 0
          for jugador in @jugadores
              if jugador == @jugador_actual
                  @jugadores[contador] = @jugador_actual.convertirme(fianza)
              end
              contador = contador + 1
          end
        end
      end
    end
    def cancelar_hipoteca(numero_casilla)
      casilla = @tablero.obtener_casilla_numero(numero_casilla)
      titulo = casilla.titulo
      cancelacion = @jugador_actual.cancelar_hipoteca(titulo)
      @estado_juego = EstadoJuego::JA_PUEDEGESTIONAR
      return cancelacion
    end
    def comprar_titulo_propiedad()
      comprado = @jugador_actual.comprar_titulo_propiedad()
      if comprado
        @estado_juego = EstadoJuego::JA_PUEDEGESTIONAR
      end
      return comprado
    end
    def edificar_casa(numero_casilla)
      casilla = @tablero.obtener_casilla_numero(numero_casilla)
      titulo = casilla.titulo
      edificada = @jugador_actual.edificar_casa(titulo)
      if edificada
        @estado_juego = EstadoJuego::JA_PUEDEGESTIONAR
      end
      return edificada
    end
    def edificar_hotel(numero_casilla)
      casilla = @tablero.obtener_casilla_numero(numero_casilla)
      titulo = casilla.titulo
      edificada = @jugador_actual.edificar_hotel(titulo)
      if edificada
        @estado_juego = EstadoJuego::JA_PUEDEGESTIONAR
      end
      return edificada
    end
    def encarcelar_jugador()
      if @jugador_actual.debo_ir_a_carcel()
        casilla_carcel =  @tablero.carcel
        @jugador_actual.ir_a_carcel(casilla_carcel)
        @estado_juego = EstadoJuego::JA_ENCARCELADO
      else
        carta = @jugador_actual.devolver_carta_libertad()
        @mazo<< carta
        @estado_juego = EstadoJuego::JA_PUEDEGESTIONAR
      end
    end
    def get_valor_dado()
      @dado.valor
    end
    def hipotecar_propiedad(numero_casilla)
      casilla = @tablero.obtener_casilla_numero(numero_casilla)
      titulo = casilla.titulo
      @jugador_actual.hipotecar_propiedad(titulo)
      @estado_juego = EstadoJuego::JA_PUEDEGESTIONAR
    end
    def inicializar_jugadores(nombres)
      for nombre in nombres
        jugadores<< Jugador.nuevo(nombre)
      end
    end
    def inicializar_cartas_sorpresa()
      @mazo<< Sorpresa.new("Level up, ahora eres especulador", 3000,
TipoSorpresa::CONVERTIRME)

      @mazo<< Sorpresa.new("Max. level up, eres un super-especulador", 5000,
TipoSorpresa::CONVERTIRME)

      @mazo<< Sorpresa.new("Has escuchado que en la casilla 2 estan
vendiendo una switch en una subasta 
ve a probar suerte", 1, TipoSorpresa::IRACASILLA)
      
      @mazo<< Sorpresa.new("Te tropiezas con una piel de platano
taaaan fuerte que acabas en la casila 13
mala suerte", 13, TipoSorpresa::IRACASILLA)
      
      @mazo<< Sorpresa.new("Ups quizas no deberias haber apostado tu
libertad a esa ultima mano de poker
Ve a la carcel", 10, TipoSorpresa::IRACASILLA)
      
      @mazo<< Sorpresa.new("Te encuentras una cartera llena de dinero
por la calle, la entregas en una comisaria pero
misteriosamente el dinero ha desaparecido
cosas que pasan", 2500, TipoSorpresa::PAGARCOBRAR)
      
      @mazo<< Sorpresa.new("Dices que un hombre vestido de santa claus
te ha atracado? si hombre y seguro que 
los reyes magos le esperaban en el trineo", -1350, TipoSorpresa::PAGARCOBRAR)
      
      @mazo<< Sorpresa.new("Un empresario chino quiere absorver
tus propiedades te da un adelanto 
para que lo pienses", 100,TipoSorpresa::PORCASAHOTEL)
      
      @mazo<< Sorpresa.new("Vaya, el nuevo equipo de gobierno ha aprobado
un nuevo impuesto de propiedades, una pena", -150, TipoSorpresa::PORCASAHOTEL)
      
      @mazo<< Sorpresa.new("Has perdido una apuesta con tus amigos
ya te dije yo que el mono de marco se llamaba
amedio y no amelio" , -50, TipoSorpresa::PORJUGADOR)
      
      @mazo<< Sorpresa.new("Creas un crowfunding y consigues que tus
colegas se rasquen el bolsillo para ayudar", 25, TipoSorpresa::PORJUGADOR)
      
      @mazo<< Sorpresa.new("Me estas diciendo enserio que te saco de
la carcel un chico con un sombrero de paja?", @tablero.carcel.numero_casilla,
TipoSorpresa::SALIRCARCEL)

      @mazo.shuffle!
    end
    def inicializar_tablero()
      @tablero = Tablero.new
    end
    def intentar_salir_carcel(metodo)
      if metodo == MetodoSalirCarcel::TIRANDODADO
        resultado = tirar_dado()
        if resultado >= 5
          @jugador_actual.encarcelado = false
        end
      elsif metodo == MetodoSalirCarcel::PAGANDOLIBERTAD
        @jugador_actual.pagar_libertad(@@PRECIO_LIBERTAD);
      end
      encarcelado = @jugador_actual.encarcelado
      if encarcelado
        @estado_juego = EstadoJuego::JA_ENCARCELADO
      else
        @estado_juego = EstadoJuego::JA_PREPARADO
      end
      return !encarcelado 
    end
    def jugar()
      tirada = @dado.tirar
      casilla_tirada = @tablero.obtener_casilla_final(@jugador_actual.casilla_actual, tirada)
      mover(casilla_tirada.numero_casilla)
    end
    def mover(num_casilla_destino)
      casilla_inicial = @jugador_actual.casilla_actual
      casilla_final = @tablero.obtener_casilla_numero(num_casilla_destino)
      @jugador_actual.casilla_actual = casilla_final
      if num_casilla_destino < casilla_inicial.numero_casilla
        @jugador_actual.modificar_saldo(@@SALDO_SALIDA)
      end
      if casilla_final.soy_edificable
        actuar_si_en_casilla_edificable
      else
        actuar_si_en_casilla_no_edificable
      end      
    end
    def obtener_casilla_jugador_actual()
      @jugador_actual.casilla_actual
    end
    def obtener_casillas_tablero()
      @tablero.casillas
    end
    def obtener_propiedades_jugador()
      num_casilla_jugador_actual = Array.new 
      for titulo in @jugador_actual.propiedades
        for casilla in tablero.casillas
          if titulo == casilla.titulo
            num_casilla__jugador_actual<< casilla.numero_casilla
          end
        end
      end
    end
    def obtener_propiedades_jugador_segun_estado_hipoteca(estado_hipoteca)
      num_casilla_estado_hipoteca_jugador_actual = Array.new
      propiedades_jugador = @jugador_actual.obtener_propiedades(estado_hipoteca)
      for titulo in propiedades_jugador
        for casilla in tablero.casillas
          if casilla.tipo == TipoCasilla::CALLE
            if titulo == casilla.titulo
              num_casilla_estado_hipoteca_jugador_actual<< casilla.numero_casilla
            end
          end
        end
      end
      
      return num_casilla_estado_hipoteca_jugador_actual
    end
    def obtener_ranking()
       @jugadores = @jugadores.sort
    end
    def obtener_saldo_jugador_actual()
      @jugador_actual.saldo
    end
    def salida_jugadores()
      @estado_juego = EstadoJuego::JA_PREPARADO
        for jugador in @jugadores
            jugador.casilla_actual= @tablero.obtener_casilla_numero(0)
        @jugador_actual = @jugadores[rand(0..@jugadores.length-1)] 
        end
    end
    def siguiente_jugador()
      pos = 0
      while @jugadores[pos] != @jugador_actual do
        pos = pos + 1
      end
      @jugador_actual = @jugadores[(pos+1)%@jugadores.length]
      if(@jugador_actual.encarcelado)
        @estado_juego = EstadoJuego::JA_ENCARCELADOCONOPCIONDELIBERTAD
      else
        @estado_juego = EstadoJuego::JA_PREPARADO
      end
    end
    def tirar_dado()
      @dado.tirar()
    end
    def vender_propiedad(numero_casilla)
      casilla = @tablero.obtener_casilla_numero(numero_casilla)
      @jugador_actual.vender_propiedad(casilla)
      @estado_juego = EstadoJuego::JA_PUEDEGESTIONAR
    end
    def to_s()
      "QYTETET:
Carta_actual: #{@carta_actual}
Mazo: #{@mazo}
Jugadores: #{@jugadores}
Jugador_actual: #{@jugador_actual}
Tablero: #{@tablero}
Dado: #{@dado}"
    end
    
    private :carta_actual=, :inicializar_jugadores, :inicializar_cartas_sorpresa,
      :inicializar_tablero, :salida_jugadores, :encarcelar_jugador
    
    def jugador_actual_en_calle_libre()
      @jugador_actual.casilla_actual.soy_edificable && !(@jugador_actual.casilla_actual.tengo_propietario)
    end
    def jugador_actual_encarcelado()
      @jugador_actual.encarcelado
    end
  end
end
