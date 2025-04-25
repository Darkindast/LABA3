
package mephi.b22901.a.l3;


import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class YamlParser extends BaseParser {
    private MonsterParser next;
    private MonsterStorage storage;

    
    @Override
    public void setStorage(MonsterStorage storage) {
        this.storage = storage;
}


    @Override
    public void setNext(MonsterParser next) {
        this.next = next;
    }

    @Override
    public MonsterParser getNext() {
        return next;
    }

    @Override
    public boolean canHandle(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".yaml") || name.endsWith(".yml");
    }



    @Override
    public List<Monster> handle(File file) {
        try {
            YAMLMapper yamlMapper = new YAMLMapper();
            yamlMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

            List<Monster> monsters = yamlMapper.readValue(
                file,
                yamlMapper.getTypeFactory().constructCollectionType(List.class, Monster.class)
            );

            monsters.forEach(m -> m.setSource(file.getName()));


            if (storage != null) {
                storage.add("yaml", monsters);
            }

            return monsters;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void export(File file, List<Monster> monsters) {
        try {
            YAMLMapper yamlMapper = new YAMLMapper();
            yamlMapper.writerWithDefaultPrettyPrinter().writeValue(file, monsters);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

