public class Quantum {

    public static void main (String[] args) {
        long start = System.currentTimeMillis();
        long n = Long.valueOf(args[0]);
        long factor = 2l;
        String factors = args[0] + " = ";
        while (!Quantum.isPrimeNumber(n)) {
            if (Quantum.isPrimeNumber(factor) && n % factor == 0l) {
                n /= factor;
                factors += factor + ", ";
            } else {
                factor = factor + 1l;
            }
        }
        factors += String.valueOf(n);
        System.out.println("Factors: " + factors);
        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Elapsed:\nMillis: " + (elapsed % 1000l));
        elapsed /= 1000l;
        System.out.println("Seconds: " + (elapsed % 60l));
        elapsed /= 60l;
        System.out.println("Minutes: " + elapsed);
    }

    public static boolean isPrimeNumber (long n) {
        if (n > 3l) {
            for (long divisor = 2l; divisor <= n / 2l; divisor += 1l) {
                if (n % divisor == 0l) {
                    return false;
                }
            }
        }
        return true;
    }
}