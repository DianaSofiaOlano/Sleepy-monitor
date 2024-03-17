import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Tutor extends Thread{
    private final Semaphore sleepingTutorSemaphore;
    private final Semaphore chairsSemaphore;
    private final Queue<Student> chairs;
    private final Random randomNumberGenerator;

    private boolean sleeping;

    public Tutor (Semaphore sleepingTutorSemaphore, Semaphore chairsSemaphore, Queue<Student> chairs, Random randomNumberGenerator){
        super();
        this.sleepingTutorSemaphore = sleepingTutorSemaphore;
        this.chairsSemaphore = chairsSemaphore;
        this.chairs = chairs;
        this.randomNumberGenerator = randomNumberGenerator;
    }

    private void sleep() throws InterruptedException {
        sleeping = true;
        System.out.println("El monitor está durmiendo");
        sleepingTutorSemaphore.acquire();
    }

    public void wakeUp(Student student){
        sleeping = false;
        sleepingTutorSemaphore.release();
        //System.out.println("El monitor fue despertado por el estudiante con código " + student.getStudId());
    }

    @Override
    public void run() {
        try {
            sleep();
            while (true) {
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

    public void helpNextStudent() throws InterruptedException {
        // Atiende a los estudiantes en espera en el orden en que llegaron
        Student student = chairs.poll();
        chairsSemaphore.release();

        System.out.println("El monitor está ayudando al estudiante con código: " + student.getStudId());
        sleep(Math.abs(randomNumberGenerator.nextInt()) % 2000);;
        System.out.println("El monitor ha terminado de ayudar al estudiante con código: " + student.getStudId());

        //Desbloquea el hilo del estudiante una vez termina de ayudarle
        student.getWaitingForHelpQueue().take();
    }

}
