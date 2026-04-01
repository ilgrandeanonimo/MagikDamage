package it.magnianonymous.magikDamage.configuration.serializers;

import de.exlll.configlib.Serializer;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LegacyFiltersSerializer implements Serializer<Map<NamespacedKey, String>, List<LegacyFiltersSerializer.FilterRecord>> {
    @Override
    public List<FilterRecord> serialize(Map<NamespacedKey, String> namespacedKeyStringMap) {
        final List<FilterRecord> list = new ArrayList<>();
        for(Map.Entry<NamespacedKey, String> entry : namespacedKeyStringMap.entrySet()) {
            list.add(new FilterRecord(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    @Override
    public Map<NamespacedKey, String> deserialize(List<FilterRecord> filterRecordList) {
        final Map<NamespacedKey, String> map = new HashMap<>();
        for(FilterRecord record : filterRecordList) {
            map.put(record.key, record.value);
        }
        return map;
    }

    public record FilterRecord(NamespacedKey key, String value) {}
}