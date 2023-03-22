import java.util.concurrent.Semaphore;

public class DiningPhilosophers {
    
    private static final int NUM_PHILOSOPHERS = 5; // number of philosophers
    private static final Semaphore[] forks = new Semaphore[NUM_PHILOSOPHERS]; // array of binary semaphores for forks
    private static final Semaphore waiter = new Semaphore(NUM_PHILOSOPHERS - 1); // semaphore for waiter to limit number of philosophers eating at the same time

    public static void main(String[] args) {
        // Initialize forks
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            forks[i] = new Semaphore(1);
        }

        // Initialize philosophers and start their threads
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            new Philosopher(i).start();
        }
    }

    // Philosopher class extends Thread to represent each philosopher as a separate thread
    static class Philosopher extends Thread {
        private final int id; // ID of the philosopher
        private Semaphore leftFork; // binary semaphore for the philosopher's left fork
        private Semaphore rightFork; // binary semaphore for the philosopher's right fork

        public Philosopher(int id) {
            this.id = id;
            leftFork = forks[id];
            rightFork = forks[(id + 1) % NUM_PHILOSOPHERS]; // wrap around for last philosopher
        }

        public void run() {
            try {
                while (true) {
                    // Think for a while
                    System.out.println("Philosopher " + id + " is thinking.");
                    Thread.sleep((long) (Math.random() * 10000));

                    // Acquire waiter semaphore to limit number of philosophers eating at the same time
                    waiter.acquire();

                    // Acquire left and right forks
                    leftFork.acquire();
                    rightFork.acquire();
                    System.out.println("Philosopher " + id + " is eating.");

                    // Release left and right forks
                    leftFork.release();
                    rightFork.release();

                    // Release waiter semaphore
                    waiter.release();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
