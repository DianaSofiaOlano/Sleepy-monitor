import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class Student extends Thread{
    private long id;
    private Semaphore chairsAvailable;
    private Semaphore sleepingTutor;
    private Semaphore availableTutor;
    private ConcurrentLinkedQueue<Long> waitingStudents;
    private Random GenAleat;

    public Student (long id, Semaphore cA, Semaphore sT, Semaphore aT, ConcurrentLinkedQueue<Long> wS, long seed){
        super();
        this.id = id;
        chairsAvailable = cA;
        sleepingTutor = sT;
        availableTutor = aT;
        waitingStudents = wS;
        GenAleat = new Random(seed);;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (chairsAvailable.tryAcquire()) {
                    // El estudiante se añade a la cola de espera
                    waitingStudents.add(id);
                    System.out.println("Estudiante con código "+id+" encontró una silla en el corredor y está esperando");

                    // Esperar a que el monitor esté disponible
                    availableTutor.acquire();

                    if (sleepingTutor.tryAcquire()) {
                        // Si el monitor está dormido, despertarlo
                        System.out.println("Estudiante con código " + id + " despierta al monitor.");
                        sleepingTutor.release();// Release the sleepingTutor semaphore to wake up the tutor
                    }

                    // Simula tiempo recibiendo ayuda del monitor
                    System.out.println("Estudiante con código "+id+" está recibiendo ayuda del monitor");
                    sleep(Math.abs(GenAleat.nextInt()) % 2000);

                    // Libera la silla en el corredor
                    System.out.println("Estudiante con código "+id +" ha sido atendido y deja la silla disponible");
                    chairsAvailable.release();
                }
                else{
                    // Si no hay sillas disponibles, el estudiante va a programar a la sala de computadoras
                    System.out.println("Estudiante con código "+id+" no encontró sillas disponibles, irá a programar en la sala");
                    sleep(Math.abs(GenAleat.nextInt()) % 3000);
                }
            } catch (InterruptedException e) {
            }
        }
    }
}
