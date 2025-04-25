
package mephi.b22901.a.l3;


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