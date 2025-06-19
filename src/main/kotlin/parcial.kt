package ar.edu.unsam.algo2

import java.time.DayOfWeek

//Exceptions
class BusinessException(mensaje: String) : Throwable()
/*
* Tengo un Programa con sus atributos. Ese Programa tiene que cumplir Restricciones.
* Hay una clase Restriccion con metodo cumpleRestriccion.
* Si el Programa no cumple las restricciones, se ejecutan Acciones.
* Hay una clase Accion y hay una lista de acciones asociadas a c/Restriccion.
* La Accion tiene un efecto sobre los Programas y por ende en la Grilla.
* Voy a tener una Grilla de programacion que posee Programas.
*/

// *** PUNTO 1: Restricciones ***
class Programa(val titulo : String,
               val conductoresPrincipales: MutableList<Conductor>,    //No se si hay class Conductor
               val presupuestoBase : Double,
               val sponsors: MutableList<String>,       //No se si hay class Sponsor
               var diaEmision: DayOfWeek,
               val duracionMinutos : Int)
{
    val restriccionesAsociadas : MutableList<Restriccion> = mutableListOf()
    val ratingUltimasEmisiones : MutableList<Double> = mutableListOf()

    fun agregarRating(rating : Double) {
        if(!cantidadMaxRatings()){
            ratingUltimasEmisiones.add(rating)
        }else{
            throw BusinessException("No se pueden agregar mas ratings")
        }
    }

    fun cantidadMaxRatings() : Boolean = ratingUltimasEmisiones.size == 5   //Solo admite 5 ratings
    fun ratingPromedio() : Double = ratingUltimasEmisiones.average()
    fun cantidadConductores() : Int = conductoresPrincipales.size
    fun loConduce(conductor : Conductor) : Boolean = conductoresPrincipales.contains(conductor)
    fun mitadConductores() : Int = conductoresPrincipales.size / 2

    //take toma los primeros "n" elementos y drop descarta los primeros "n" elementos
    fun primeraMitadConductores() : MutableList<Conductor> = conductoresPrincipales.take(mitadConductores()).toMutableList()
    fun segundaMitadConductores() : MutableList<Conductor> = conductoresPrincipales.drop(mitadConductores()).toMutableList()

    fun mitadPresupuesto() : Double = presupuestoBase / 2
    fun mitadDuracion() : Int = duracionMinutos / 2
    fun dividirTitulo() : List<String> = titulo.split(" ")

    fun cumpleRestricciones(grilla : Grilla) {
        val primeraRestriccionNoCumplida = restriccionesAsociadas.find{ restriccion -> restriccion.seCumple(this) }
        if(primeraRestriccionNoCumplida != null){
            primeraRestriccionNoCumplida.ejecutarAcciones(this, grilla)
        }else{
            print("Cumple todas las condiciones")
        }
    }

    //Arma una lista con solo los mails de los conductores
    fun mailsConductores() = conductoresPrincipales.map{ it.email }

    fun presupuestoMayorA(monto : Double) : Boolean = presupuestoBase > monto
}


abstract class Restriccion {
    val accionesAsociadas : MutableList<Accion> = mutableListOf()       //Punto 2 y 3

    abstract fun seCumple(programa: Programa): Boolean

    fun ejecutarAcciones(programa : Programa, grilla : Grilla) {
        accionesAsociadas.forEach { it.execute(programa, grilla) }
    }
}

class RatingSuperiorA(val valorRating : Double) : Restriccion() {
    override fun seCumple(programa: Programa): Boolean = programa.ratingPromedio() > valorRating
}

class CantidadMaxConductores(val cantidad : Int) : Restriccion() {
    override fun seCumple(programa: Programa): Boolean = programa.cantidadConductores() <= cantidad
}

class ConductorEspecifico(val conductorDeseado : Conductor) : Restriccion() {
    override fun seCumple(programa: Programa): Boolean = programa.loConduce(conductorDeseado)
}

class PresupuestoMaximo(val tope : Double) : Restriccion() {
    override fun seCumple(programa: Programa): Boolean = programa.presupuestoBase <= tope
}

//Composite de restricciones OR
class RestriccionCombinadaOR(val restricciones : MutableSet<Restriccion>) : Restriccion() {
    override fun seCumple(programa: Programa): Boolean = restricciones.any { it.seCumple(programa) }
}

//Composite de restricciones AND
class RestriccionCombinadaAND(val restricciones : MutableSet<Restriccion>) : Restriccion() {
    override fun seCumple(programa: Programa): Boolean = restricciones.all { it.seCumple(programa) }
}

// *** FIN PUNTO 1 ***

// *** PUNTO 2: Acciones ***
//Cuando parte el programa en 2, se elimina el original de la grilla y se agregan los nuevos programas!
//el programa desaparece y lo reemplaza los simpsons
//se fusionan el programa con el siguiente de la GRILLA
//el programa se mueve de dia

interface Accion {
    abstract fun execute(programa : Programa, grilla: Grilla)
}

class DividirPrograma() : Accion {
    override fun execute(programa: Programa, grilla: Grilla) {
        val primeraMitad = programa.primeraMitadConductores()
        val segundaMitad = programa.segundaMitadConductores()

        fun calculoSegundoTitulo(programa: Programa): String {
            val tituloPotencial = programa.dividirTitulo().getOrNull(1) //Hago calculo de 2da palabra
            if (tituloPotencial != null) {
                return tituloPotencial
            } else {
                return "Programa sin nombre"
            }
        }

        val programa1 = Programa(
            titulo = "${programa.dividirTitulo().first()} en el aire!",   //delimita por espacio y trae la 1era palabra
            conductoresPrincipales = primeraMitad,
            presupuestoBase = programa.mitadPresupuesto(),
            sponsors = programa.sponsors,
            diaEmision = programa.diaEmision,
            duracionMinutos = programa.mitadDuracion()
        )

        val programa2 = Programa(
            titulo = "${calculoSegundoTitulo(programa)}", //busca la 2da palabra o devuelve null
            conductoresPrincipales = segundaMitad,
            presupuestoBase = programa.mitadPresupuesto(),
            sponsors = programa.sponsors,
            diaEmision = programa.diaEmision,
            duracionMinutos = programa.mitadDuracion()
        )

        grilla.eliminarPrograma(programa)   //elimina el programa original y agrega los 2 nuevos
        grilla.agregarPrograma(programa1)
        grilla.agregarPrograma(programa2)
    }
}

class ReemplazarPorLosSimpsons() : Accion {
    override fun execute(programa: Programa, grilla: Grilla) {
        grilla.eliminarPrograma(programa)
        val repeticionSimpsons = Programa(
            titulo = "Los Simpsons",
            conductoresPrincipales = mutableListOf(),
            presupuestoBase = 100.0,
            sponsors = mutableListOf("Fox"),
            diaEmision = programa.diaEmision,
            duracionMinutos = 30
        )
        grilla.agregarPrograma(repeticionSimpsons)  //Es una implementacion medio boluda, no?
    }
}

class FusionarPrograma() : Accion {
    //Elige al azar un sponsor de cada programa
    private fun sponsorAzaroso(programa: Programa, otroPrograma: Programa): MutableList<String> = mutableListOf(programa.sponsors.random(), otroPrograma.sponsors.random())
    //Trae al primer conductor de cada programa
    private fun conductoresFusionados(programa: Programa, otroPrograma: Programa): MutableList<Conductor> = mutableListOf(programa.conductoresPrincipales.first(), otroPrograma.conductoresPrincipales.first())

    private fun tituloAzaroso() : String = mutableListOf("Impacto total", "Un buen dia").random()
    private fun minimoPresupuesto(programa : Programa, otroPrograma : Programa) : Double = minOf(programa.presupuestoBase, otroPrograma.presupuestoBase)
    private fun duracionFusionada(programa: Programa, otroPrograma: Programa): Int = programa.duracionMinutos + otroPrograma.duracionMinutos


    override fun execute(programa: Programa, grilla: Grilla) {
        val programaFusionado = Programa(
            titulo = tituloAzaroso(),
            conductoresPrincipales = conductoresFusionados(programa, grilla.siguientePrograma(programa)),
            presupuestoBase = minimoPresupuesto(programa, grilla.siguientePrograma(programa)),
            sponsors = sponsorAzaroso(programa, grilla.siguientePrograma(programa)),
            diaEmision = programa.diaEmision,
            duracionMinutos = duracionFusionada(programa, grilla.siguientePrograma(programa)))

        grilla.eliminarPrograma(programa)
        grilla.eliminarPrograma(grilla.siguientePrograma(programa))
        grilla.agregarPrograma(programaFusionado)
    }
}

class CambiarDiaEmision(val diaElegido : DayOfWeek) : Accion {
    override fun execute(programa: Programa, grilla: Grilla) {  //me parece bastante boludo esto
        programa.diaEmision = diaElegido
    }
}

// *** FIN PUNTO 2 ***

/*
Hay una clase Grilla que:
1. agrega restricciones y sus acciones si no se cumplen: programa tiene su lista de restricciones
y las restricciones tienen su lista de acciones, no me parece responsabilidad de la Grilla ocuparse de eso.
2. tiene una lista de Programas / Programas en Revision
3. Dispara el proceso de Revision: ve si los programas cumplen la restriccion o no
4. cada vez que se crea un programa (se agrega a la grilla) dispara Observers
*/

//Creo la data class Conductor porque para el observer de envio de mails a Conductores no tiene
//sentido que le mande a un String. Que sea object no me sirve porque no quiero un Singleton.
data class Conductor(val nombre : String, val email : String)

// *** PUNTO 3: El Proceso ***
class Grilla(){
    val listaDeProgramas : MutableList<Programa> = mutableListOf()
    val listaProgramasRevision : MutableList<Programa> = mutableListOf()
    val observersProgramaCreado : MutableList<ProgramaObserver> = mutableListOf()

    fun agregarPrograma(programa: Programa) {
        listaDeProgramas.add(programa)                      //podria agregar validacion para saber si está
        observersProgramaCreado.forEach{ observer -> observer.tareaProgramaCreado(programa) } //dispara los observers
    }

    fun eliminarPrograma(programa: Programa) = listaDeProgramas.remove(programa)

    fun agregarObserver(observer : ProgramaObserver) = observersProgramaCreado.add(observer)
    fun eliminarObserver(observer : ProgramaObserver) = observersProgramaCreado.remove(observer)

    fun devuelveIndicePrograma(programa : Programa) : Int = listaDeProgramas.indexOf(programa)

    fun siguientePrograma(programa : Programa) : Programa {
        val indiceActual = devuelveIndicePrograma(programa)
        return if (indiceActual < listaDeProgramas.size - 1) { //si no es el ultimo
            listaDeProgramas[indiceActual + 1]
        }else {
            listaDeProgramas.first()
        }
    }

    fun agregarProgramaRevision(programa: Programa) = listaProgramasRevision.add(programa)  //de nuevo, podria haber validacion
    fun eliminarProgramaRevision(programa : Programa) = listaProgramasRevision.remove(programa)

    fun procesoRevision() = listaProgramasRevision.forEach{ programa -> programa.cumpleRestricciones(this) }

}

interface ProgramaObserver {
    abstract fun tareaProgramaCreado(programa: Programa)
}

class MailConductores(val mailSender : MailSender) : ProgramaObserver {
    override fun tareaProgramaCreado(programa : Programa) {
        programa.mailsConductores().forEach{ it -> mailSender.sendMail(Mail(
            from = "unsamTV@gmail.com",
            to = it,        //cada mail sobre el que itera
            subject = "OPORTUNIDAD!",
            content = "Fuiste seleccionado para conducir ${programa.titulo}! Ponete en contacto con la gerencia"
        )) }
    }
}

//El monto es 100000 pero es mas escalable si recibe un valor. Podria tener una variable de instancia
//con el monto 100.000 tambien.

//No me queda en claro qué es la Agencia Publicitaria Cliowin, deberia ser una entidad?
class MsjTextoPresupuesto(val mensajeSender : MsjTextoSender, val monto : Double) : ProgramaObserver {
    override fun tareaProgramaCreado(programa : Programa) {
        if(programa.presupuestoMayorA(monto)){
            mensajeSender.sendMensaje(MsjTexto(
                from = "unsamTV@gmail.com",
                to = "Cliowin",
                content = "${programa.presupuestoBase} -- ${programa.titulo} -- CONSEGUIR SPONSOR URGENTE!"
            ))
        }
    }
}
class SacoProgramaRevision() : ProgramaObserver {
    override fun tareaProgramaCreado(programa : Programa) {
        TODO("Not yet implemented")
    }
}
// *** FIN PUNTO 3 ***
interface MailSender {
    fun sendMail(mail: Mail)
}

interface MsjTextoSender {
    fun sendMensaje(mensaje: MsjTexto)
}
data class Mail(val from : String, val to : String, val subject : String, val content : String)
data class MsjTexto(val from : String, val to : String, val content : String)