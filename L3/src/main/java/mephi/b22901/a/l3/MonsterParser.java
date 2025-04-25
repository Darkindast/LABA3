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

public interface MonsterParser {
    void setNext(MonsterParser next);                
    MonsterParser getNext();                         
    List<Monster> handle(File file);                
    boolean canHandle(File file);                              
    void export(File file, List<Monster> monsters);
    void setStorage(MonsterStorage storage);

}