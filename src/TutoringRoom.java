import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class TutoringRoom {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Sintaxis: Carrera semilla_aleatoria");
            System.exit(1);
        }

        long seed=Long.parseLong(args[0]);

        int numStudents = 5;
        int numChairs = 3;

        // Define semáforos
        Semaphore chairsAvailable = new Semaphore(numChairs); // Semáforo para controlar el acceso a las sillas
        Semaphore sleepingTutor = new Semaphore(0, true); // Semáforo para indicar si el monitor está dormido
        Semaphore availableTutor = new Semaphore(1, true); // Semáforo para indicar si el monitor está disponible

        // Creación de la cola para mantener el orden de llegada de los estudiantes
        ConcurrentLinkedQueue<Long> waitingStudents = new ConcurrentLinkedQueue<>();

        //Creación del monitor
        Tutor tutor = new Tutor(sleepingTutor, availableTutor, waitingStudents, seed);

        //Creación de los estudiantes
        Student[] students = new Student[numStudents];
        for (int i = 0; i < numStudents; i++) {
            students[i] = new Student(i, chairsAvailable, sleepingTutor, availableTutor, waitingStudents, seed);
        }

        //Iniciar los hilos
        tutor.start();
        for (int i = 0; i < students.length; i++) {
            students[i].start();
        }

        // Esperar a que todos los estudiantes sean atendidos
        try {
            for (int i = 0; i < students.length; i++) {
                students[i].join();
            }
            tutor.interrupt(); // Detener el hilo del monitor
        } catch (InterruptedException e) {
        }
    }
}
