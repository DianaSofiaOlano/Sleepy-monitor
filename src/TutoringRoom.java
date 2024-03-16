public class TutoringRoom {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Sintaxis: Carrera semilla_aleatoria");
            System.exit(1);
        }

        long seed=Long.parseLong(args[0]);
    }
}
