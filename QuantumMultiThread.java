import java.util.Queue;
import java.util.LinkedList;

public class QuantumMultiThread {

    public long n;
    public int processors;

    public boolean error;
    public Queue<Long> pendings;
    //public Queue<Long> primes;

    public Factorizer[] factorizers;

    public static void main (String[] args) {
        new QuantumMultiThread(Long.valueOf(args[0]), args.length > 1 ? args[1] : null);
    }

    public QuantumMultiThread (long n, String threads) {
        this.n = n;

        if (threads != null) {
            this.processors = Integer.valueOf(threads);
        } else {
            this.processors = Runtime.getRuntime().availableProcessors();
        }
        System.out.println("Using " + processors + " processors");

        this.error = false;
        this.pendings = new LinkedList<>();
        //this.primes = new LinkedList<>();
        
        if (n > 3l) {
            long start = System.currentTimeMillis();
            System.out.print(n + " = 1");
            this.factorizers = new Factorizer[processors];
            for (int i = 0; i < processors; i++) {
                this.factorizers[i] = new Factorizer(this, i);
                this.factorizers[i].start();
            }
            while (!this.pendings.offer(n));

            while (!this.error) {
                while (this.pendings.size() > 0) {
                    Long pending = null;
                    while (pending == null) {
                        pending = this.pendings.poll();
                    }
                    for (int i = 0; ; i++) {
                        if (this.factorizers[i%this.processors].factor == 0l) {
                            this.factorizers[i%this.processors].factor = pending;
                            break;
                        }
                    }
                }
                /*while (this.primes.size() > 0) {
                    Long prime = null;
                    while (prime == null) {
                        prime = this.primes.poll();
                    }
                    this.current *= prime;
                }*/
            }
            long elapsed = System.currentTimeMillis() - start;
            System.out.println("\nElapsed:\nMillis: " + (elapsed % 1000l));
            elapsed /= 1000l;
            System.out.println("Seconds: " + (elapsed % 60l));
            elapsed /= 60l;
            System.out.println("Minutes: " + elapsed);
        } else {
            System.out.println(n + " = 1, " + n);
        }
    }

    public boolean isPrimeNumber (long n) {
        if (n > 3l) {
            for (long divisor = 2l; divisor <= n / 2l; divisor += 1l) {
                if (n % divisor == 0l) {
                    return false;
                }
            }
        }
        return true;
    }

    private class Factorizer extends Thread {

        public String name;
        private QuantumMultiThread parent;
        public long factor;

        public Factorizer (QuantumMultiThread parent, int index) {
            this.parent = parent;
            this.name = "Factorizer " + (index + 1);
            this.factor = 0l;
        }

        @Override
        public void run () {
            //System.out.println(this.name + ".run");
            while (!this.parent.error) {
                if (this.factor > 0l) {
                    if (this.parent.isPrimeNumber(factor)) {
                        System.out.print(", " + factor);
                        //while (!this.parent.primes.offer(factor));
                        this.factor = 0l;
                    } else {
                        for (long i = 2l; i <= factor / 2l; i += 1l) {
                            if (factor % i == 0l) {
                                while (!this.parent.pendings.offer(i));
                                while (!this.parent.pendings.offer(factor / i));
                                this.factor = 0l;
                                break;
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                    this.parent.error = true;
                }
            }
        }
    }
}