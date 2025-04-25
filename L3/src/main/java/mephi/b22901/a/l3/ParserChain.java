/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.a.l3;

/**
 *
 * @author Andrey
 */
import java.io.File;
import java.util.List;

public class ParserChain {
    private final MonsterParser head;

    public ParserChain() {
        MonsterStorage storage = new MonsterStorage();
        MonsterParser json = new JsonParser();
        MonsterParser xml = new XmlParser();
        MonsterParser yaml = new YamlParser();
        
        json.setStorage(storage);
        xml.setStorage(storage);
        yaml.setStorage(storage);
        
        json.setNext(xml);
        xml.setNext(yaml);

        this.head = json;
    }

    public List<Monster> handle(File file) {
        MonsterParser parser = head;
        while (parser != null) {
            if (parser.canHandle(file)) {
                return parser.handle(file);
            }
            parser = parser.getNext();
        }
        return null;
    }

    public void export(File file, List<Monster> monsters) {
        MonsterParser parser = head;
        while (parser != null) {
            if (parser.canHandle(file)) {
                parser.export(file, monsters);
                return;
            }
            parser = parser.getNext();
        }
    }
}


