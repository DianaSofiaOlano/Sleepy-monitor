import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Tutor extends Thread{
    private final Semaphore sleepingTutorSemaphore; // Semáforo para controlar cuándo el tutor está durmiendo
    private final Semaphore chairsSemaphore; // Semáforo para controlar el acceso a las sillas
    private final Queue<Student> chairs; // Cola de sillas donde los estudiantes esperan
    private final Random randomNumberGenerator; // Generador de números aleatorios para simular el tiempo de ayuda
    private boolean sleeping; // Indica si el tutor está durmiendo

    public Tutor (Semaphore sleepingTutorSemaphore, Semaphore chairsSemaphore, Queue<Student> chairs, Random randomNumberGenerator){
        super();
        this.sleepingTutorSemaphore = sleepingTutorSemaphore;
        this.chairsSemaphore = chairsSemaphore;
        this.chairs = chairs;
        this.randomNumberGenerator = randomNumberGenerator;
    }

    // Adquiere el semáforo para dormir
    private void sleep() throws InterruptedException {
        sleeping = true;
        System.out.println("El monitor está durmiendo");
        sleepingTutorSemaphore.acquire();
    }

    // Libera el semáforo para despertarse
    public void wakeUp(Student student){
        sleeping = false;
        sleepingTutorSemaphore.release();
    }

    @Override
    public void run() {
        try {
            sleep();
            while (true) {
                // Adquiere el semáforo para acceder a las sillas
                chairsSemaphore.acquire();

                if(chairs.isEmpty()){
                    //Si las sillas están vacías no hay estudiantes esperando y el monitor se duerme
                    System.out.println("No hay estudiantes esperando");
                    chairsSemaphore.release();
                    sleep();
                }
                else{
                    //Si hay estudiantes sentados, atiende al siguiente, en orden
                    helpNextStudent();
                }
            }
        } catch (InterruptedException ignored) {
        }
    }

    public boolean isSleeping() {
        return sleeping;
    }

    // Método para obtener el siguiente estudiante en la cola y ayudarlo
    public void helpNextStudent() throws InterruptedException {
        // Atiende a los estudiantes en espera en el orden en que llegaron
        Student student = chairs.poll();
        chairsSemaphore.release();

        System.out.println("El monitor está ayudando al estudiante con código: " + student.getStudId());
        sleep(Math.abs(randomNumberGenerator.nextInt()) % 2000);
        System.out.println("El monitor ha terminado de ayudar al estudiante con código: " + student.getStudId());

        //Desbloquea el hilo del estudiante una vez termina de ayudarle
        student.getWaitingForHelpQueue().take();
    }
}
