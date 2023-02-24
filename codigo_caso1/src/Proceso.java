import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CyclicBarrier;

public class Proceso extends Thread {
    private int id = 0;
    private Buffer bufferEtapa1;
    private Buffer bufferEtapa2;
    private Buffer bufferEtapaFinal;
    private String tipo;
    static private int cantidadProductos;
    static private ArrayList<Producto> pFinal = new ArrayList<Producto>();
    static private Integer capProc;
    static private Identificador identificador = new Identificador();
    private CyclicBarrier barrier;

    public Proceso(int id, String tipo, int cantidadProductos, Buffer bufferEtapa1, Buffer bufferEtapa2, Buffer bufferEtapaFinal, Integer capProc, CyclicBarrier barrier) {
        this.id = id;
        this.tipo = tipo;
        Proceso.cantidadProductos = cantidadProductos;
        this.bufferEtapa1 = bufferEtapa1;
        this.bufferEtapa2 = bufferEtapa2;
        this.bufferEtapaFinal = bufferEtapaFinal;
        Proceso.capProc = capProc;
        this.barrier = barrier;
    }

    private void etapa1() {
        Integer temp = cantidadProductos;
        while (temp > 0)
        {
            //Se generan la cantidad de productos que se indicaron en el main, por cada proceso
            Producto productoNuevo = new Producto(identificador.getId(), tipo);
            try {
                //Sleep que asegura que se almacenen todos los prodcutos en el buffer
                sleep(500);
            } catch (Exception e) {
            }
            if (tipo == "naranja") {
                try {
                    sleep(500);
                } catch (Exception e) {
                }
                while (!bufferEtapa1.almacenarNaranja(productoNuevo)) 
                {
                    //Espera semiactiva y se almacena el producto naranja en el buffer 1
                    Thread.yield();
                }
            } else if (tipo == "azul") 
            {
                try {
                    sleep(500);
                } catch (Exception e) {
                }
                //Espera pasiva. Se almacena el producto azul en el buffer 1
                bufferEtapa1.almacenarAzul(productoNuevo);
            }
            else 
            {
                System.out.println("ERROR: Tipo de producto no reconocido");
            }
            temp--;

            //Se agrega la información de la etapa 1 de cada producto para luego mostrarla
            synchronized (this)
            {
                String mensaje = "Etapa 1: Proceso " + id + ".";

                String etapa = productoNuevo.getInfo();

                etapa += mensaje;

                productoNuevo.setInfo(etapa);
            }
        }
    }

    private void etapa2()
    {
        Integer temp = cantidadProductos;
        int numeroAleatorio = (int)(Math.random() * (500 - 50 + 1) + 50);
        while (temp > 0)
        {
            if (tipo == "naranja")
            {
                Producto productoExtraido;
                while((productoExtraido = bufferEtapa1.extraerNaranja()) == null)
                {
                    //Espera semiactiva, se extrae el producto naranja del buffer 1
                    Thread.yield();
                }
                try {
                    //Se procesa el producto naranja
                    sleep(numeroAleatorio);
                } catch (Exception e) {
                    System.out.println("Error en la espera");
                }
                //Se almacena el producto naranja en el buffer 2
                bufferEtapa2.almacenarNaranja(productoExtraido);

                //Se agrega la información de la etapa 2 de cada producto naranja para luego mostrarla
                synchronized (this) {

                    String mensaje = " Etapa 2: Proceso " + id + " proecesado en " + numeroAleatorio + " milisegundos.";
    
                    String etapa = productoExtraido.getInfo();
    
                    etapa += mensaje;
    
                    productoExtraido.setInfo(etapa);
                }
            }
            else if (tipo == "azul")
            {
                Producto productoExtraido;
                //Se extrae el producto azul del buffer 1
                productoExtraido = bufferEtapa1.extraerAzul();
                try {
                    //Se procesa el producto azul
                    sleep(numeroAleatorio);
                } catch (Exception e) {
                    
                }
                //Espera pasiva. Se almacena el producto azul en el buffer 2
                bufferEtapa2.almacenarAzul(productoExtraido);

                //Se agrega la información de la etapa 2 de cada producto azul para luego mostrarla
                synchronized (this) {

                    String mensaje = " Etapa 2: Proceso " + id + " proecesado en " + numeroAleatorio + " milisegundos.";
    
                    String etapa = productoExtraido.getInfo();
    
                    etapa += mensaje;
    
                    productoExtraido.setInfo(etapa);
                }
            }
            else
            {
                System.out.println("ERROR: Tipo de producto no reconocido");
            }
            temp--;
            
        }
    }

    private void etapa3()
    {
        Integer temp = cantidadProductos;
        Integer numeroAleatorio = (int)(Math.random() * (500 - 50 + 1) + 50);
        while (temp > 0)
        {
            Producto productoExtraido;
            //Se extrae el producto rojo del buffer 2
            while((productoExtraido = bufferEtapa2.extraerRojo()) == null)
            {
                //Nada espera activa
            }
            try {
                //Se procesa el producto rojo
                sleep(numeroAleatorio);
            } catch (Exception e) {
            }

            //Se almacena el producto rojo en el buffer final
            bufferEtapaFinal.almacenarRojo(productoExtraido);
            temp--;

            //Se agrega la información de la etapa 3 de cada producto rojo para luego mostrarla
            synchronized (this) {

                String mensaje = " Etapa 3: Proceso " + id + " proecesado en " + numeroAleatorio + " milisegundos.";

                String etapa = productoExtraido.getInfo();

                etapa += mensaje;

                productoExtraido.setInfo(etapa);
            }
        }
    }

    public void etapaFinal()
    {
        Integer temp = cantidadProductos * capProc;
        int numeroAleatorio = (int)(Math.random() * (500 - 50 + 1) + 50);
        while (temp > 0)
        {
            Producto productoExtraido;

            //Se extrae el producto rojo del buffer final
            while((productoExtraido = bufferEtapaFinal.extraerRojo()) == null)
            {
                //Nada espera activa
            }
            try {
                //Se procesa el producto rojo
                sleep(numeroAleatorio);
            } catch (Exception e) {
            }
            pFinal.add(productoExtraido);
            temp--;

            //Se ordenan los productos en el mismo orden en el que fueron creados por medio de su Identificador
            Collections.sort(pFinal, (p1, p2) -> p1.getId() - p2.getId());
        }
    }

    public void run() {
        if(id == 0)
        {
            etapa1();
        }
        else if(id == 1)
        {
            etapa2();
        }
        else if(id == 2)
        {
            etapa3();
            try {
                barrier.await();
            } catch (Exception e) {
                System.out.println("No se puede hacer el await");
            }
        }
        else if(id == 3)
        {
            etapaFinal();
            synchronized (this)
            {
                System.out.println("\n------- Etapa 1 -------");
                System.out.println("Productos restantes en el buffer de la etapa 1: " + bufferEtapa1.getTamanio());
                System.out.println("------------------------");

                System.out.println("\n------- Etapa 2 -------");
                System.out.println("Productos restantes en el buffer de la etapa 1: " + bufferEtapa2.getTamanio());
                System.out.println("------------------------");

                System.out.println("\n------- Etapa 3 -------");
                System.out.println("Productos restantes en el buffer de la etapa 3: " + bufferEtapaFinal.getTamanio());
                System.out.println("------------------------");

                System.out.println("\n------- Etapa final -------");
                System.out.println("------------------------");
                System.out.println("Estos son los productos de la etapa final:");

                for (int i = 0; i < pFinal.size(); i++) {
                    int id = pFinal.get(i).getId();
                    System.out.println("\n------- Producto " + id + " --------");
                    System.out.println("Producto " + id + " extraido" + " de tipo " + pFinal.get(i).getTipo());
                    System.out.println(pFinal.get(i).getInfo());
                }
                System.out.println("\nFin de la operación");
            }

        }
        else
        {
            System.out.println("ERROR: ID de etapa no reconocido");
        }
    }
}
