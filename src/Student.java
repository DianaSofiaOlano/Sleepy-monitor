import java.util.Random;
import java.util.concurrent.Semaphore;

public class Student extends Thread{
    private int id;
    private Semaphore chairsAvailable;
    private Semaphore sleepingTutor;
    private Semaphore availableTutor;
    private Semaphore mutex;
    private Random GenAleat;

    public Student (int id, Semaphore cA, Semaphore sT, Semaphore aT, Semaphore m, long seed){
        super();
        this.id = id;
        chairsAvailable = cA;
        sleepingTutor = sT;
        availableTutor = aT;
        mutex = m;
        GenAleat = new Random(seed);;
    }

    @Override
    public void run() {

    }
}
