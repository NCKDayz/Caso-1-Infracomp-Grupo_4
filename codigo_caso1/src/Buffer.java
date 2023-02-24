import java.util.ArrayList;

public class Buffer {
    private ArrayList<Producto> buff;
    private int tamanio;

    public Buffer(int tamanio) {
        // Constructor de la clase Buffer que recibe un tamaño y crea un nuevo ArrayList vacío de productos
        this.tamanio = tamanio;
        this.buff = new ArrayList<Producto>();
    }

    public Integer getTamanio() {
        // Retorna el tamaño actual del buffer
        return buff.size();
    }

    public synchronized Boolean almacenarNaranja(Producto p) {
        // Método sincronizado que intenta almacenar un producto naranja en el buffer
        if (buff.size() == tamanio) {
            // Si el buffer está lleno, retorna false
            return false;
        }
        else {
            // Si hay espacio en el buffer, agrega el producto y notifica a todos los hilos que estén esperando
            buff.add(p);
            notifyAll();
            return true;
        }
    }

    public synchronized void almacenarAzul(Producto p) {
        // Método sincronizado que intenta almacenar un producto azul en el buffer
        while (buff.size() == tamanio) {
            // Mientras el buffer esté lleno, espera a que haya espacio disponible
            try {                
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Cuando hay espacio en el buffer, agrega el producto y notifica a todos los hilos que estén esperando
        buff.add(p);
        notifyAll();
    }

    public synchronized void almacenarRojo(Producto p) {
        // Método sincronizado que intenta almacenar un producto rojo en el buffer
        while (buff.size() == tamanio) {
            // Espera activa mientras el buffer esté lleno
        }
        // Cuando hay espacio en el buffer, agrega el producto y notifica a todos los hilos que estén esperando
        buff.add(p);
        notifyAll();
    }

    public synchronized Producto extraerRojo() {
        // Método sincronizado que intenta extraer un producto rojo del buffer
        if (buff.isEmpty()) {
            // Si el buffer está vacío, retorna null
            return null;
        }
        else {
            // Si hay productos en el buffer, extrae el primero, lo remueve del buffer y notifica a todos los hilos que estén esperando
            Producto i = buff.remove(0);
            notifyAll();
            return i ;
        }
    }

    public synchronized Producto extraerNaranja() {
        // Método sincronizado que intenta extraer un producto naranja del buffer
        if (buff.isEmpty()) {
            // Si el buffer está vacío, retorna null
            return null;
    }
        else {
            // Si hay productos en el buffer, extrae el primero, lo remueve del buffer y notifica a todos los hilos que estén esperando
            Producto p = buff.remove(0);
            notifyAll();
            return p;
        }
    }

    public synchronized Producto extraerAzul() {
        // Método sincronizado que intenta extraer un producto azul del buffer
        while (buff.isEmpty()) {
            // Mientras el buffer esté vacío, espera a que haya productos disponibles
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Cuando hay productos en el buffer, extrae el primero, lo remueve del buffer y notifica a todos los hilos que estén esperando
        Producto i = buff.remove(0);
    notifyAll();
    return i ;
    }
}

    