import org.apache.xmlrpc.AsyncCallback;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.Vector;

/**
 * Klasa klienta
 */
public class MathRPCClient {

    private static final String SERVER_NAME = "mathServer.";
    private static final String SHOW_METHOD = "show";
    private static final String FIB_METHOD = "fib";
    private static final String POW_METHOD = "pow";
    private static final String ROUND_METHOD = "round";
    private static final String PI_METHOD = "pi";
    private static final String LOG_METHOD = "log";
    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Metoda uruchamiająca klienta
     */
    public static void main(String[] args) {
        System.out.print("Podaj adres (np. localhost lub 192.168.43.178) serwera do którego chcesz się podłączyć\nAdres: ");
        String address = SCANNER.nextLine();
        System.out.print("Port: ");
        int serverPort = SCANNER.nextInt();
        try {
            XmlRpcClient client = new XmlRpcClient(address, serverPort);
            Object execute = client.execute("mathServer.show", new Vector<>());
            System.out.println(execute);
            System.out.println("Wybierz methodę, którą chcesz wywołać.\nWpisz \"exit\" aby wyjść.");
            chooseRunMethod(client);
        } catch (XmlRpcException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda wybierająca działanie do wykonania
     *
     * @param client klient, z którego wysyłamy zapytania
     * @throws XmlRpcException
     * @throws IOException
     */
    public static void chooseRunMethod(XmlRpcClient client) throws XmlRpcException, IOException {
        Object execute;
        Vector<Object> params = new Vector<>();
        while (SCANNER.hasNext()) {
            String command = SCANNER.next();
            if (command.equals("exit") || command.equals("Exit")) break;
            switch (command) {
                case "show":
                    execute = client.execute(SERVER_NAME + SHOW_METHOD, params);
                    System.out.println(execute);
                    break;
                case "fib":
                    System.out.println("Podaj argument metody");
                    System.out.print("Wartość ciągu fin do obliczenia: ");
                    int n = SCANNER.nextInt();
                    params.addElement(n);
                    AsyncCall call = new AsyncCall();
                    client.executeAsync(SERVER_NAME + FIB_METHOD, params, call);
                    break;
                case "pow":
                    System.out.println("Podaj argument metody");
                    System.out.print("a: ");
                    double a = SCANNER.nextDouble();
                    System.out.print("b: ");
                    double b = SCANNER.nextDouble();
                    params.addElement(a);
                    params.addElement(b);
                    execute = client.execute(SERVER_NAME + POW_METHOD, params);
                    System.out.println(execute);
                    break;
                case "round":
                    System.out.println("Podaj argument metody");
                    System.out.print("Liczba do zaokrąglenia: ");
                    double number = SCANNER.nextDouble();
                    System.out.print("Precyzja zaokrąglenia: ");
                    int precision = SCANNER.nextInt();
                    params.addElement(number);
                    params.addElement(precision);
                    execute = client.execute(SERVER_NAME + ROUND_METHOD, params);
                    System.out.println(execute);
                    break;
                case "pi":
                    System.out.println("Podaj argument metody");
                    System.out.println("Liczba rzutów:");
                    int numThrows = SCANNER.nextInt();
                    params.addElement(numThrows);
                    execute = client.execute(SERVER_NAME + PI_METHOD, params);
                    System.out.println(execute);
                    break;
                case "log":
                    System.out.println("Podaj argument metody");
                    System.out.print("Podaj typ logarytmu: ");
                    SCANNER.nextLine();
                    String logtype = SCANNER.nextLine();
                    System.out.print("Podaj liczbę liczonych logarytmów: ");
                    int range = SCANNER.nextInt();
                    System.out.print("Podaj timeout: ");
                    int timeout = SCANNER.nextInt();
                    params.addElement(logtype);
                    params.addElement(range);
                    params.addElement(timeout);
                    client.executeAsync(SERVER_NAME + LOG_METHOD, params, new AsyncCall());
                    break;
                default:
                    System.out.println("Nie znana methoda");
                    break;
            }
            params = new Vector<>();
            System.out.println("Wybierz methodę, którą chcesz wywołać");
        }
    }

    /**
     * Klasa do zapytań asynchronicznych
     */
    public static class AsyncCall implements AsyncCallback {

        /**
         * Metoda obsługująca odpowiedz od serwera
         */
        @Override
        public void handleResult(Object o, URL url, String s) {
            System.out.println("Wywołana metoda: " + s);
            System.out.println("Rezultat: " + o.toString());
        }

        /**
         * Metoda obsługująca wyjątki od serwera
         */
        @Override
        public void handleError(Exception e, URL url, String s) {
            System.out.println("Error: " + e.toString());
            System.out.println("URL: " + url.toString());
            System.out.println("Metoda: " + s);
        }
    }
}
