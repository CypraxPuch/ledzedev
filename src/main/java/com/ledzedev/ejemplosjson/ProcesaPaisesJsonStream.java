package com.ledzedev.ejemplosjson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.IOException;
import java.io.InputStream;

/**
 * Código generado por Gerado Pucheta Figueroa
 * Twitter: @ledzedev
 * 27/Oct/2016.
 */
public class ProcesaPaisesJsonStream {

    private static final Logger log = LoggerFactory.getLogger(ProcesaPaisesJsonStream.class);

    public static void main(String[] args) {
        String paisABuscar = "Alaska";
        String emailDelPais = ProcesaPaisesJsonStream.obtenerEmailConPais(paisABuscar);

        log.info("El e-mail de la dirección ubicada en " + paisABuscar + ", es: " + emailDelPais);
    }

    public static String obtenerEmailConPais(String pais) {

        JsonParser jsonParser = null;
        String email = null;

        //paso 1. Obtener el JSON, en este caso viene de un archivo, pero puede venir de un servicio en web.
        /*  Ejemplo si viene por web
            URL url = new URL("http://restcountries.eu/rest/v1/all");
            InputStream inputStream = url.openStream();
         */
        try (InputStream inputStream = EjemploPaisesJson.class.getClassLoader().getResourceAsStream("json-generator-file.json")) {
            jsonParser = Json.createParser(inputStream);

            //paso 2. recorrer el JSON hasta que se procese|encuentre lo que se necesite.
            boolean paisEncontrado = false;

            while (jsonParser.hasNext() && !paisEncontrado) {

                JsonParser.Event event = jsonParser.next();
                if (event.equals(JsonParser.Event.KEY_NAME)) {

                    switch (jsonParser.getString()) {
                        //Nota: debido a que el api solo recorre hacia adelante y en el archivo el campo email está antes que el campo address,
                        // tenemos que guardar el valor de email, previo a que se valide el contenido de address.
                        case "email":
                            //guardamos el valor de email
                            jsonParser.next();
                            email = jsonParser.getString();
                            break;
                        case "address":
                            jsonParser.next();
                            String direccion = jsonParser.getString();
                            //si el pais que buscamos se encuentra en address entonces mandamos la variable paisEncontrado a true
                            paisEncontrado = (direccion != null && direccion.contains(pais));
                            break;
                    }
                }
            }
        } catch (IOException e) {
            log.error("error al leer el archivo json", e);
            return null;
        }
        return email;
    }
}
