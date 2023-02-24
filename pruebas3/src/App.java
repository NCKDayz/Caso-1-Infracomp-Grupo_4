import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CyclicBarrier;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("INICIO");
        
        int tamanioBuffer = 0;
        int cantidadProcesos = 0;
        int cantidadProductos = 0;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader br3 = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Ingrese la cantidad de procesos");
            cantidadProcesos = Integer.parseInt(br.readLine());
            System.out.println("Ingrese la cantidad de productos");
            cantidadProductos = Integer.parseInt(br2.readLine());
            System.out.println("Ingrese el tamaño del buffer");
            tamanioBuffer = Integer.parseInt(br3.readLine());
        } catch (Exception e) {
            System.out.println("ERROR READER");
        }
        
    
        Buffer bufferInicial = new Buffer(tamanioBuffer);
        Buffer bufferMitad = new Buffer(tamanioBuffer);
        Buffer bufferFinal = new Buffer(2000000000);

        CyclicBarrier barrier = new CyclicBarrier(cantidadProcesos + 1);
        
        //Prueba: Tengo una idea con el buffer

        Proceso procesoNaranjaUno = new Proceso(0, "naranja", cantidadProductos, bufferInicial, bufferMitad, bufferFinal, cantidadProcesos, barrier);
        Proceso procesoNaranjaDos = new Proceso(1, "naranja", cantidadProductos, bufferInicial, bufferMitad, bufferFinal, cantidadProcesos, barrier);
        Proceso procesoNaranjaTres = new Proceso(2, "naranja", cantidadProductos, bufferInicial, bufferMitad, bufferFinal, cantidadProcesos, barrier);
        procesoNaranjaUno.start();
        procesoNaranjaDos.start();
        procesoNaranjaTres.start();

        for (int i = 1; i < cantidadProcesos; i++) {
            Proceso procesoAzulUno = new Proceso(0, "azul", cantidadProductos, bufferInicial, bufferMitad, bufferFinal, cantidadProcesos, barrier);
            Proceso procesoAzulDos = new Proceso(1, "azul", cantidadProductos, bufferInicial, bufferMitad, bufferFinal, cantidadProcesos, barrier);
            Proceso procesoAzulTres = new Proceso(2, "azul", cantidadProductos, bufferInicial, bufferMitad, bufferFinal, cantidadProcesos, barrier);
            procesoAzulUno.start();
            procesoAzulDos.start();
            procesoAzulTres.start();
        }

        Proceso etaFinal = new Proceso(3, "final", cantidadProductos, bufferInicial, bufferMitad, bufferFinal, cantidadProcesos, barrier);
        etaFinal.start();

        barrier.await();

    }
}
