/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.a.l3;

/**
 *
 * @author Andrey
 */

public abstract class BaseParser implements MonsterParser {
    private MonsterParser next;

    @Override
    public void setNext(MonsterParser next) {
        this.next = next;
    }

    @Override
    public MonsterParser getNext() {
        return next;
    }
}