package it.magnianonymous.magikDamage.configuration.serializers;

import de.exlll.configlib.Serializer;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FiltersSerializer implements Serializer<Map<NamespacedKey, String>, List<String>> {
    @Override
    public List<String> serialize(Map<NamespacedKey, String> keyStringMap) {
        var list = new ArrayList<String>();
        keyStringMap.forEach((key, value) ->{
            if(value != null)
                list.add(String.format("%s=%s", key, value));
            else
                list.add(key.asString());
        }
        );
        return list;
    }

    @Override
    public Map<NamespacedKey, String> deserialize(List<String> strings) {
        var map = new HashMap<NamespacedKey, String>();
        for (String string : strings) {
            var components = string.split("=", 2);
            if(components.length < 2) {
                map.put(NamespacedKey.fromString(components[0]), null);
            } else {
                map.put(NamespacedKey.fromString(components[0]), components[1]);
            }
        }
        return map;
    }
}
