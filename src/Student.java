import java.util.Queue;
import java.util.Random;
import java.util.concurrent.*;

public class Student extends Thread{
    public long getStudId() {
        return studId;
    }

    private final long studId;
    private final Semaphore tutorSemaphore;
    private final Semaphore chairsSemaphore;
    private final Tutor tutor;
    private final Queue<Student> chairs;
    private final SynchronousQueue<Student> waitingForHelpQueue;
    private final Random randomNumberGenerator;

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
                    System.out.println("Estudiante con código " + studId + " despierta al monitor.");
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

                chairsSemaphore.release();
                tutorSemaphore.release();

                waitingForHelpQueue.put(this); //Bloquea el hilo del estudiante mientras espera y mientras recibe la ayuda
                helpReceived = true;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void programInComputerRoom() throws InterruptedException {
        //System.out.println("Estudiante con código "+ studId +" está programando en la sala");
        sleep(Math.abs(randomNumberGenerator.nextInt()) % 3000);
    }

    public SynchronousQueue<Student> getWaitingForHelpQueue() {
        return waitingForHelpQueue;
    }
}


