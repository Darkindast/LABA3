package mephi.b22901.a.l3;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Andrey
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonParser extends BaseParser {
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
        return file.getName().toLowerCase().endsWith(".json");
    }


    @Override
    public List<Monster> handle(File file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE); 

            List<Monster> monsters = mapper.readValue(
                file,
                mapper.getTypeFactory().constructCollectionType(List.class, Monster.class)
            );
            monsters.forEach(m -> m.setSource(file.getName()));
            if (storage != null) {
                storage.add("json", monsters);
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
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, monsters);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
