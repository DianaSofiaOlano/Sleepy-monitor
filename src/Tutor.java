import java.util.Random;
import java.util.concurrent.Semaphore;

public class Tutor extends Thread{
    private Semaphore chairsAvailable;
    private Semaphore sleepingTutor;
    private Semaphore availableTutor;
    private Random GenAleat;

    public Tutor (Semaphore cA, Semaphore sT, Semaphore aT, long seed){
        super();
        chairsAvailable = cA;
        sleepingTutor = sT;
        availableTutor = aT;
        GenAleat = new Random(seed);
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Espera a que un estudiante lo despierte
                sleepingTutor.acquire();

                //Notifica que está disponible
                availableTutor.release();

                // Simula tiempo ayudando al estudiante
                long currentStudent = Thread.currentThread().getId();
                System.out.println("El monitor está ayudando al estudiante con ID: "+currentStudent);
                sleep(Math.abs(GenAleat.nextInt()) % 1000);

                //Verificar si hay más estudiantes esperando
                if (chairsAvailable.availablePermits() < 3) {
                    availableTutor.acquire();
                    long nextStudent = Thread.currentThread().getId();
                    System.out.println("El monitor esta ayudando al estudiante con ID: "+nextStudent);
                    availableTutor.release();
                } else {
                    // Si no hay estudiantes esperando, vuelve a dormir
                    System.out.println("El monitor esta durmiendo");
                    sleepingTutor.release();
                    sleep(Math.abs(GenAleat.nextInt()) % 1000);
                }
            } catch (InterruptedException e) {
            }
        }
    }
}
