// Proyecto EL MONITOR DORMILÓN - Clase Student
// Sistemas Operacionales - Juan Fernando Martínez Hidalgo y Diana Sofia Olano Montaño

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.*;

public class Student extends Thread{
    private final long studId; // ID del estudiante
    private final Semaphore tutorSemaphore; // Semáforo para controlar el acceso al tutor
    private final Semaphore chairsSemaphore; // Semáforo para controlar el acceso a las sillas
    private final Tutor tutor; // Objeto Tutor
    private final Queue<Student> chairs; // Cola para las sillas
    private final SynchronousQueue<Student> waitingForHelpQueue; // Cola para los estudiantes que esperan ayuda
    private final Random randomNumberGenerator; // Generador de números aleatorios

    public Student (long studId, Semaphore tutorSemaphore, Tutor tutor, Semaphore chairsSemaphore, BlockingQueue<Student> chairs, Random randomNumberGenerator){
        super();
        this.studId = studId;
        this.tutorSemaphore = tutorSemaphore;
        this.tutor = tutor;
        this.chairsSemaphore = chairsSemaphore;
        this.chairs = chairs;
        waitingForHelpQueue = new SynchronousQueue<>();
        this.randomNumberGenerator = randomNumberGenerator;
    }

    @Override
    public void run() {
        try {
            boolean helpReceived = false;
            System.out.println("Estudiante con código "+ studId +" está programando en la sala");
            programInComputerRoom();

            while (!helpReceived) {
                //Limita la cola de las sillas y el hilo del monitor para que puedan ser usados por un solo hilo
                //Esto para evitar conflictos
                chairsSemaphore.acquire();
                tutorSemaphore.acquire();

                if(tutor.isSleeping()){
                    //Si el monitor está dormido, lo despierta
                    tutor.wakeUp(this);
                    System.out.println("Estudiante con código " + studId + " despierta al monitor");
                }

                if (!chairs.offer(this)){
                    // Si no hay sillas disponibles, el estudiante va a programar a la sala de computadoras
                    System.out.println("Estudiante con código "+ studId +" no encontró sillas disponibles, fue a programar a la sala");
                    chairsSemaphore.release();
                    tutorSemaphore.release();
                    programInComputerRoom();
                    continue;
                }
                else{
                    //Sí hay sillas, se sienta a esperar a que el monitor lo llame
                    System.out.println("Estudiante con código "+ studId +" encontró una silla en el corredor y está esperando");
                }

                //Se libera los semáforos
                chairsSemaphore.release();
                tutorSemaphore.release();

                waitingForHelpQueue.put(this); //Bloquea el hilo del estudiante mientras espera y mientras recibe la ayuda
                helpReceived = true;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Método para obtener el ID del estudiante
    public long getStudId() {
        return studId;
    }

    // Método para simular la programación en la sala de computadoras
    private void programInComputerRoom() throws InterruptedException {
        sleep(Math.abs(randomNumberGenerator.nextInt()) % 3000);
    }

    // Método para obtener la cola de estudiantes que esperan ayuda
    public SynchronousQueue<Student> getWaitingForHelpQueue() {
        return waitingForHelpQueue;
    }
}


