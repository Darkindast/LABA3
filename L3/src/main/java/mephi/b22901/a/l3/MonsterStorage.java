/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.a.l3;

/**
 *
 * @author Andrey
 */


import java.util.*;

import java.util.*;
import java.util.stream.Collectors;

public class MonsterStorage {
    private final Map<String, List<Monster>> monstersByFile = new HashMap<>();

    public void add(String source, List<Monster> monsters) {
        monstersByFile.put(source, monsters);
    }

    public void removeBySource(String source) {
        monstersByFile.remove(source);
    }

    public List<Monster> getAll() {
        return monstersByFile.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

}

