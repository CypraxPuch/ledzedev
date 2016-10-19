package com.ledzedev.ejemplosjson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Código generado por Gerardo Pucheta Figueroa
 * Twitter: @ledzedev
 * 19/Oct/2016
 *
 */
public class EjemploPaisesJson {

    private final Logger log = LoggerFactory.getLogger(EjemploPaisesJson.class);

    public JsonArray getJsonArrayFromFile(String archivo) throws FileNotFoundException {
        InputStream inputStream = EjemploPaisesJson.class.getClassLoader().getResourceAsStream(archivo);

        JsonReader jsonReader = Json.createReader(inputStream);
        JsonArray jsonArray = jsonReader.readArray();

        return jsonArray;
    }

    public void consultaObjetoJson(String llave){
        JsonArray jsonArray = null;
        String nombreArchivo = "paises.json";
        log.info("Leyendo {} llave: {}",nombreArchivo, llave);
        try {
            jsonArray = getJsonArrayFromFile(nombreArchivo);
        } catch (FileNotFoundException e) {
            log.error("ARCHIVO NO ENCONTRADO.",e);
            return;
        }

        List<JsonObject> lst = jsonArray.getValuesAs(JsonObject.class);

        lst.forEach(jsonObject -> {
            JsonValue jsonValue = jsonObject.get(llave);
            switch (jsonValue.getValueType()) {
                case STRING:
                    log.info("capital: " + jsonObject.getString(llave, "Llave desconocida!!!"));
                    break;
                case ARRAY:
                    JsonArray array = jsonObject.getJsonArray("idiomasOficiales");
                    String idiomas = array.getValuesAs(JsonObject.class)
                            .stream()
                            .map(lang -> lang.getString("idioma", ""))
                            .collect(Collectors.joining(", "));
                    log.info("idiomas: " + idiomas);
                    break;
                case NUMBER:
                    log.info("población: " + jsonObject.getInt("poblacion"));
                    break;
            }
            });
    }

    public static void main(String [] args){

        EjemploPaisesJson ejemploPaisesJson = new EjemploPaisesJson();

        ejemploPaisesJson.consultaObjetoJson("pais");
        ejemploPaisesJson.consultaObjetoJson("poblacion");
        ejemploPaisesJson.consultaObjetoJson("idiomasOficiales");

    }

}
