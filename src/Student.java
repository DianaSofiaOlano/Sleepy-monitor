import java.util.Random;
import java.util.concurrent.Semaphore;

public class Student extends Thread{
    private int id;
    private Semaphore chairsAvailable;
    private Semaphore sleepingTutor;
    private Semaphore availableTutor;
    private Random GenAleat;

    public Student (int id, Semaphore cA, Semaphore sT, Semaphore aT, long seed){
        super();
        this.id = id;
        chairsAvailable = cA;
        sleepingTutor = sT;
        availableTutor = aT;
        GenAleat = new Random(seed);;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (chairsAvailable.tryAcquire()) {
                    System.out.println("Estudiante con código "+id+" encontró una silla en el corredor y está esperando");

                    // Intenta obtener el semáforo de monitor disponible
                    if (availableTutor.tryAcquire()) {
                        // El monitor está disponible, despiértalo si está dormido
                        if (sleepingTutor.availablePermits() == 1) {
                            sleepingTutor.acquire();
                            System.out.println("Estudiante con código "+id+ " despierta al monitor");
                        }

                        // Simula tiempo recibiendo ayuda del monitor
                        System.out.println("Estudiante con código "+id+" está recibiendo ayuda del monitor.");
                        sleep(Math.abs(GenAleat.nextInt()) % 1000);

                        // Libera el semáforo del monitor disponible
                        availableTutor.release();
                        
                        // Libera la silla en el corredor
                        chairsAvailable.release();
                    }
                    else {
                        // El monitor está ocupado
                        System.out.println("Estudiante con código "+id+" está esperando a que el monitor esté disponible");
                    }
                }
                else{
                    // Si no hay sillas disponibles, va a programar en la sala de computadoras
                    System.out.println("Estudiante con código "+id+" no encontró sillas disponibles en el corredor. Regresa a programar en la sala.");
                    sleep(Math.abs(GenAleat.nextInt()) % 1000);
                }
            } catch (InterruptedException e) {
            }
        }
    }
}
