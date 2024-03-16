import java.util.Random;
import java.util.concurrent.Semaphore;

public class Tutor extends Thread{
    private Semaphore chairsAvailable;
    private Semaphore sleepingTutor;
    private Semaphore availableTutor;
    private Semaphore mutex;
    private Random GenAleat;

    public Tutor (Semaphore cA, Semaphore sT, Semaphore aT, Semaphore m, long seed){
        super();
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
