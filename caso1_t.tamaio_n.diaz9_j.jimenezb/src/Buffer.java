import java.util.ArrayList;

public class Buffer {
    private ArrayList<Producto> buff;
    private int tamanio;

    public Buffer(int tamanio) {
        this.tamanio = tamanio;
        this.buff = new ArrayList<Producto>();
    }
    
    public Integer getTamanio() {
        return buff.size();
    }
    public synchronized Boolean almacenarNaranja(Producto p) {
        if (buff.size() == tamanio) {
            return false;
        }
        else
        {
            buff.add(p);
            notifyAll();
            return true;
        }
    }

    public synchronized void almacenarAzul(Producto p) {
        while (buff.size() == tamanio) {
            try {                
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        buff.add(p);
        notifyAll();
    }

    public synchronized void almacenarRojo(Producto p) {
        while (buff.size() == tamanio) {
            // Espera activa mientras el buffer esté lleno
        }
        buff.add(p);
        notifyAll();
    }

    public synchronized Producto extraerRojo() {
            if (buff.isEmpty())
            {
                return null;
            }
            else
            {
                Producto i = buff.remove(0);
                notifyAll();
                return i ;
            }
    }

    public synchronized Producto extraerNaranja() {
        if (buff.isEmpty())
        {
                return null;
        }
        else
        {
            Producto p = buff.remove(0);
            notifyAll();
            return p;
        }
    }

    public synchronized Producto extraerAzul() {
            while (buff.isEmpty())
            {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Producto i = buff.remove(0);
            notifyAll();
            return i ;
    }

}
