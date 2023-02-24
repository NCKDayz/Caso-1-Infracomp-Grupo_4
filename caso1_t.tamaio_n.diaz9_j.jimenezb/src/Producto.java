public class Producto {
    private Integer id;
    private String tipo;
    private String info;

    public Producto(Integer id, String tipo) {
        this.id = id;
        this.tipo = tipo;
        this.info = "";
    }

    public Integer getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
