// Proyecto EL MONITOR DORMILÓN - Clase TutoringRoom
// Sistemas Operacionales - Juan Fernando Martínez Hidalgo y Diana Sofia Olano Montaño

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class TutoringRoom {
    static final int NUM_STUDENTS = 5;
    static final int NUM_CHAIRS = 3;

    public static void main(String[] args) {
        // Verificar que se haya proporcionado un argumento para la semilla
        if (args.length != 1) {
            System.out.println("Sintaxis: TutoringRoom semilla_aleatoria");
            System.exit(1);
        }

        // Obtener la semilla aleatoria a partir del argumento
        long seed = Long.parseLong(args[0]);
        Random randomNumberGenerator = new Random(seed);

        // Se definen los semáforos
        Semaphore sleepingTutorSemaphore = new Semaphore(0, true); // Semáforo para indicar si el monitor está dormido
        Semaphore tutorSemaphore = new Semaphore(1, true); // Semáforo para indicar si el monitor está disponible
        Semaphore chairsSemaphore = new Semaphore(1, true); //Semáforo para controlar el acceso a las sillas

        // Creación de la cola para mantener el orden de llegada de los estudiantes
        BlockingQueue<Student> chairs = new ArrayBlockingQueue<>(NUM_CHAIRS);

        //Creación del monitor
        Tutor tutor = new Tutor(sleepingTutorSemaphore, chairsSemaphore, chairs, randomNumberGenerator);

        //Creación de los estudiantes
        Student[] students = new Student[NUM_STUDENTS];
        for (int i = 0; i < NUM_STUDENTS; i++) {
            students[i] = new Student(i, tutorSemaphore, tutor, chairsSemaphore, chairs, randomNumberGenerator);
        }

        //Iniciar los hilos
        tutor.start();
        for (Student student : students) {
            student.start();
        }

        // Esperar a que todos los estudiantes sean atendidos
        try {
            for (Student student : students) {
                student.join();
            }
            tutor.interrupt(); // Detener el hilo del monitor
        } catch (InterruptedException ignored) {
        }
    }
}
