
package mephi.b22901.a.l3;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class XmlParser implements MonsterParser {
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
        return file.getName().toLowerCase().endsWith(".xml");
    }


    @Override
    public List<Monster> handle(File file) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE); 

            List<Monster> monsters = xmlMapper.readValue(
                file,
                xmlMapper.getTypeFactory().constructCollectionType(List.class, Monster.class)
            );
            monsters.forEach(m -> m.setSource(file.getName()));
            if (storage != null) {
                storage.add("", monsters);
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
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            xmlMapper.writerWithDefaultPrettyPrinter().writeValue(file, monsters);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

