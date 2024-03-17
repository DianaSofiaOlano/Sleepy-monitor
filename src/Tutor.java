import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class Tutor extends Thread{
    private Semaphore sleepingTutor;
    private Semaphore availableTutor;
    private ConcurrentLinkedQueue<Long> waitingStudents;
    private Random GenAleat;

    public Tutor (Semaphore sT, Semaphore aT, ConcurrentLinkedQueue<Long> wS, long seed){
        super();
        sleepingTutor = sT;
        availableTutor = aT;
        waitingStudents = wS;
        GenAleat = new Random(seed);
    }

    @Override
    public void run() {
        while (true) {
            try {
                // The tutor always tries to sleep after helping a student
                System.out.println("Tutor está durmiendo");
                sleepingTutor.acquire();

                // Adquirir el semáforo availableTutor antes de atender a un estudiante
                availableTutor.acquire();

                // Atiende a los estudiantes en espera en el orden en que llegaron
                Long studentId = waitingStudents.poll();
                if (studentId != null) {
                    System.out.println("El monitor está ayudando al estudiante con código: " + studentId);
                    sleep(Math.abs(GenAleat.nextInt()) % 2000);;
                    System.out.println("El monitor ha terminado de ayudar al estudiante con código: " + studentId);

                    // Release the availableTutor semaphore to indicate that the tutor is available
                    System.out.println("Monitor disponible");
                    availableTutor.release();
                }

                else{
                    // Si no hay estudiantes en espera, el monitor se duerme
                    System.out.println("No hay estudiantes esperando, el monitor se duerme");
                    sleepingTutor.release();
                }
            } catch (InterruptedException e) {
            }
        }
    }
}
