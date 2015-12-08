package net.dkahn.starter.apps.webapps.common.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ResourceMessageReaderUtils {

    public static Map<String,String> jsonToMap(InputStream is) throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        JsonParser jParser = factory.createParser(is);
        return toMap(mapper.readValue(jParser, HashMap.class), null);
    }

    public static Map<String,String> yamlToMap(InputStream in) {
        return toMap((Map<String, Object>) new Yaml().load(in), null);
    }


    private static Map<String,String> toMap(Map<String,Object> in,String str) {
        Map<String,String> res = new HashMap<>();


        for (Map.Entry<String, Object> entry : in.entrySet()) {
            if (entry.getValue() instanceof HashMap) {
                HashMap value = (HashMap) entry.getValue();
                if(str==null){
                    res.putAll(toMap(value, (String) entry.getKey()));
                }else{
                    res.putAll(toMap(value, str + "." + (String) entry.getKey()));
                }
            }else if (entry.getValue() instanceof String){
                if(str==null){
                    res.put(entry.getKey(), (String) entry.getValue());
                }else{
                    if(entry.getKey().isEmpty()){
                        res.put(str, (String) entry.getValue());
                    }else{
                        res.put(str+"."+entry.getKey(), (String) entry.getValue());
                    }
                }
            }
        }

        return res;
    }
}
