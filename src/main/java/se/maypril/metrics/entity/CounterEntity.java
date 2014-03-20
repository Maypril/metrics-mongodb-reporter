
package se.maypril.metrics.entity;


/**
 * @author callegustafsson
 *
 */
public class CounterEntity extends CoreEntity {


    private Object count;

    public Object getCount() {
        return count;
    }

    public void setCount(final Object count) {
        this.count = count;
    }

}
