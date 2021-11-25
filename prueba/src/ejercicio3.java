import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class ejercicio3 {
    public static final String FICHERO_SUPERMERCADO = "src/supermercado.csv";
    public static final String ARCHIVO_PRODUCTO = "src/productos.csv";
    public static final String ARCHIVO_CLIENTE = "src/clientes.csv";
    public static final String ARCHIVO_PEDIDO = "src/pedidos.csv";
    public static final String FICHERO_XML = "src/clientes.xml";

    public static void main(String[] args) {
        //Método para generar ficheros .csv de los clientes, productos y pedidos
        generarFicheros();

        try {
            generaXML("251");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private static void generarFicheros() {

        try (BufferedReader filtroLectura = new BufferedReader(new FileReader(FICHERO_SUPERMERCADO))) {
            PrintWriter flujoEscrituraProducto = new PrintWriter(new FileWriter(ARCHIVO_PRODUCTO));
            PrintWriter flujoEscrituraCliente = new PrintWriter(new FileWriter(ARCHIVO_CLIENTE));
            PrintWriter flujoEscrituraPedido = new PrintWriter(new FileWriter(ARCHIVO_PEDIDO));
            String linea;

            linea = filtroLectura.readLine();

            while (linea != null) {
                String[] cadenaAux;
                cadenaAux = linea.split(";");

                if (cadenaAux[0].equals("PR")) {
                    generarArchivoProductos(flujoEscrituraProducto, linea);
                } else {
                    if (cadenaAux[0].equals("CL")) {
                        generarArchivoClientes(flujoEscrituraCliente, linea);
                    } else {
                        generarArchivoPedidos(flujoEscrituraPedido, linea);
                    }
                }

                linea = filtroLectura.readLine();
            }

            flujoEscrituraProducto.close();
            flujoEscrituraCliente.close();
            flujoEscrituraPedido.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generarArchivoPedidos(PrintWriter flujoEscrituraPedido, String linea) {
        flujoEscrituraPedido.println(linea);
    }

    private static void generarArchivoClientes(PrintWriter flujoEscrituraCliente, String linea) {
        flujoEscrituraCliente.println(linea);
    }

    private static void generarArchivoProductos(PrintWriter flujoEscrituraProducto, String linea) {
        flujoEscrituraProducto.println(linea);
    }

    private static void generaXML(String idCliente) throws ParserConfigurationException, IOException, TransformerException {
        BufferedReader filtroLecturaCliente = new BufferedReader(new FileReader(ARCHIVO_CLIENTE));
        BufferedReader filtroLecturaProducto = new BufferedReader(new FileReader(ARCHIVO_PRODUCTO));
        BufferedReader filtroLecturaPedido = new BufferedReader(new FileReader(ARCHIVO_PEDIDO));
        String lineaCliente = null;
        String lineaPedido = null;
        String lineaProducto = null;
        String[] conjuntoDatosCliente;
        String[] conjuntoDatosPedido;
        String[] conjuntoDatosProducto;

        lineaCliente = filtroLecturaCliente.readLine();
        conjuntoDatosCliente = lineaCliente.split(";");

        while (!(conjuntoDatosCliente[1].equals(idCliente))){
            lineaCliente = filtroLecturaCliente.readLine();
            conjuntoDatosCliente = lineaCliente.split(";");
        }

        lineaPedido = filtroLecturaPedido.readLine();
        conjuntoDatosPedido = lineaPedido.split(";");

        while (!(conjuntoDatosPedido[2].equals(idCliente))){
            lineaPedido = filtroLecturaPedido.readLine();
            conjuntoDatosPedido = lineaPedido.split(";");
        }

        lineaProducto = filtroLecturaProducto.readLine();
        conjuntoDatosProducto = lineaProducto.split(";");

        while (!(conjuntoDatosProducto[1].equals(conjuntoDatosPedido[3]))){
            lineaProducto = filtroLecturaProducto.readLine();
            conjuntoDatosProducto = lineaProducto.split(";");
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();

        //Definimos el nodo raíz
        Element raiz = doc.createElement("info_cliente");
        doc.appendChild(raiz);

        //Definimos el nodo que contendrá los elementos cliente
        Element cliente = doc.createElement("cliente");
        raiz.appendChild(cliente);

        Element nombre = doc.createElement("nombre");
        nombre.appendChild(doc.createTextNode(conjuntoDatosCliente[2]));
        cliente.appendChild(nombre);

        Element apellido1 = doc.createElement("apellido1");
        apellido1.appendChild(doc.createTextNode(conjuntoDatosCliente[3]));
        cliente.appendChild(apellido1);

        Element apellido2 = doc.createElement("apellido2");
        apellido2.appendChild(doc.createTextNode(conjuntoDatosCliente[4]));
        cliente.appendChild(apellido2);

        //Definimos el nodo que contendrá los elementos pedido
        Element pedidos = doc.createElement("pedidos");
        raiz.appendChild(pedidos);

        Element pedido = doc.createElement("pedido");
        pedidos.appendChild(pedido);

        Element idPedido = doc.createElement("idPedido");
        idPedido.appendChild(doc.createTextNode(conjuntoDatosPedido[1]));
        pedido.appendChild(idPedido);

        //Nodo de los productos
        Element productos = doc.createElement("productos");
        pedido.appendChild(productos);

        Element producto = doc.createElement("producto");
        productos.appendChild(producto);

        Element nombreProducto = doc.createElement("nombre");
        nombreProducto.appendChild(doc.createTextNode(conjuntoDatosProducto[2]));
        producto.appendChild(nombreProducto);

        Element precio = doc.createElement("precio");
        precio.appendChild(doc.createTextNode(conjuntoDatosProducto[3]));
        producto.appendChild(precio);

        //Creamos el fichero XML
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(FICHERO_XML));

        transformer.transform(source, result);
    }
}
