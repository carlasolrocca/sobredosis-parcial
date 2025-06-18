# Enunciado: Sobredosis de TV

Queremos modelar un sistema que nos permita gestionar la programación de un canal de televisión. Por lo pronto sabemos que cada programa tiene un título, varios conductores principales, un presupuesto base, sponsors de publicidad, el día (Lunes / Domingo) y la duración en minutos. También conocemos los ratings de las últimas 5 emisiones.

## Punto 1: Restricciones

No es fácil mantener un programa, existen muchas restricciones:

- en algunos casos, el promedio de rating de las últimas 5 emisiones debe ser superior a un valor (por ejemplo 5 ó 15 puntos)
- en otros casos, queremos que haya como máximo *n* conductores principales (el *n* debe poder configurarse)
- o bien, en otros casos se pide que dentro de los conductores principales esté **"Juan Carlos Pérez Loizeau"** o **"Mario Monteverde"** (por supuesto que se pueda configurar la persona que conduce)
- en otros, los programas no deben excederse de un cierto presupuesto
- también queremos poder configurar:
    - "ráting mayor a 5 ó máximo 3 conductores"
    - y la opción "que conduzca Pinky y que el programa no se exceda de \$100.000"

## Punto 2: Acciones

Como el tiempo en televisión es tirano, necesitamos poder tomar acciones, y queremos hacerlo lo más rápidamente posible. Por ejemplo:

- podríamos querer **partir el programa en 2**:
    - Se generan 2 programas en los que queda: la mitad de los conductores principales en el primero y la segunda mitad en el segundo.
    - El presupuesto se divide en partes iguales para ambos programas.
    - Los sponsors se comparten (son los mismos).
    - La duración es la mitad de minutos para cada programa.
    - Los títulos se armarían de la siguiente manera:
        - `"{primera palabra del título} en el aire!"` y
        - `"{segunda palabra del título}"` o `"Programa sin nombre"` si no existe segunda palabra (asumir que siempre hay al menos una palabra para el título).
    - Los días serían los mismos.

- el programa **desaparecería** y se pondría en su lugar la repetición de **Los Simpsons** (invente ud. los datos)

- el programa se **fusionaría con el siguiente de la grilla** (si es el último tomar el primero):
    - se genera un nuevo programa:
        - como conductores estarían la primera persona que conduzca cada uno de los programas,
        - el presupuesto sería el menor de los 2 programas (si uno es de \$50.000 y otro es de \$75.000 nos quedamos con \$50.000).
        - El sponsor será uno de los dos programas al azar.
        - La duración será el total de ambos programas.
        - Los días serán los del primer programa.
        - El título será elegido al azar también, entre `"Impacto total"` o `"Un buen día"`.

- el programa se **movería de día**, por ejemplo de Domingo a Lunes. En este caso la persona que programa el canal luego haría ajustes en forma manual para que la grilla cierre.

## Punto 3: El proceso

Queremos que, dada una grilla (una lista de programas del canal), una persona encargada:

- pueda definir una serie de restricciones a cumplir, y las acciones que deben ocurrir en caso de que la restricción no se cumpla.  
  Por ejemplo: si un programa no tiene más de 5 puntos de rating debe mover el programa al día martes.
- pueda agregar una serie de programas para **"revisión"**
- en algún momento de la semana, disparar el **proceso de revisión propiamente dicho**, que debe analizar cada uno de los programas y ver si alguna de las restricciones no la cumple, en cuyo caso debe tomar la acción asociada a dicha restricción.  
  *No importa si hay otra restricción que no se cumpla, solo debe ejecutar la acción asociada a la primera restricción que no se cumpla.*
- cada vez que se crea un nuevo programa, se pide que:
    - mande un mail a cada una de las personas conductoras con el asunto `"Oportunidad!"` y el contenido  
      `"Fuiste seleccionado para conducir {titulo}! Ponete en contacto con la gerencia."`
    - si el programa tiene un presupuesto mayor a \$100.000, debe pasar a la agencia publicitaria **"Cliowin"** el siguiente mensaje de texto:  
      `"{PRESUPUESTO} - {TITULO DEL PROGRAMA} - CONSEGUIR SPONSOR URGENTE!"`
    - quite los programas de la **"revisión"** a todos los que ahora no forman parte de la grilla

*Nuevas cosas pueden agregarse a futuro cuando se agregue un programa, queremos que se puedan agregar o quitar fácilmente.*

---

## Qué se pide

Debe resolver los 3 puntos, marcando explícitamente los métodos donde comienzan cada uno de ellos. En el punto 1 y el 2 puede no implementar alguno de los puntos salvo la combinación de restricciones en el punto 1.

- Realice un **diagrama de clases** de la solución general.
- Explique brevemente qué ideas de diseño surgieron, qué alternativas surgieron y por qué se decidió por la solución que presentó.