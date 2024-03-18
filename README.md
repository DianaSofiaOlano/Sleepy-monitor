# Monitor Dormilón
Este proyecto ofrece una solución en Java utilizando hilos y semáforos para coordinar las actividades de un monitor y los estudiantes en el departamento de CSI de la Universidad Icesi. El objetivo es simular la dinámica de atención del monitor a los estudiantes que requieren ayuda con tareas de programación, considerando su disponibilidad y la posibilidad de que el monitor pueda dormir en caso de que no haya estudiantes esperando.

## Descripción del Problema ✍️
El departamento de CSI de la Universidad Icesi cuenta con un monitor que asiste a los estudiantes de los cursos de algoritmos con sus tareas de programación. La oficina del monitor es pequeña y cuenta con un espacio limitado para un escritorio, una silla para el monitor, una silla de visita y un computador. Además, hay tres sillas en el corredor donde los estudiantes pueden esperar en caso de que el monitor esté ocupado atendiendo a otro estudiante.

## Solución Propuesta 💻
Para resolver este problema, se han implementado hilos en Java para representar tanto al monitor como a los estudiantes. Los estudiantes alternan entre programar en la sala de cómputo y buscar la ayuda del monitor. Si el monitor está disponible, recibirán asistencia; de lo contrario, esperarán en una silla del corredor. En caso de que no haya sillas disponibles, los estudiantes volverán a programar en la sala y regresarán más tarde.

+ **Estudiantes:** Cada estudiante se representa como un hilo independiente. Alternan entre programar y buscar ayuda del monitor. Si encuentran al monitor dormido, lo despiertan. Si el monitor está ocupado, esperan en el corredor respetando el orden de llegada.
+ **Monitor:** El monitor también se ejecuta como un hilo independiente. Si está dormido y un estudiante lo despierta, atiende al estudiante y luego revisa el corredor para ayudar a los demás estudiantes en orden de llegada. Si no hay estudiantes presentes, el monitor puede volver a dormirse.

## Requisitos previos 📋
+ Debes tener java versión 17 o superior para ejecutar correctamente el programa.
+ Un editor de código con el que te sientas familiarizado en el entorno Java.

## Autores 👨‍💻👩‍💻
+ [JuanF2019](https://github.com/JuanF2019)
+ [DianaSofiaOlano](https://github.com/DianaOlanoU)

## Documentación 📚
### [Diagrama de secuencia](https://github.com/DianaSofiaOlano/Sleepy-monitor/blob/master/docs/sleepy-monitor-seq-diag-wakeup.pdf "PDF")
