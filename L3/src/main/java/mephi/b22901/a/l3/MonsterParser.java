
package mephi.b22901.a.l3;



import java.io.File;
import java.util.List;

public interface MonsterParser {
    void setNext(MonsterParser next);                
    MonsterParser getNext();                         
    List<Monster> handle(File file);                
    boolean canHandle(File file);                              
    void export(File file, List<Monster> monsters);
    void setStorage(MonsterStorage storage);

}