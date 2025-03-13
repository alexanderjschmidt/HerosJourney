package heroes.journey.components.interfaces;

import com.badlogic.ashley.core.Component;

public interface ClonableComponent<T extends ClonableComponent<T>> extends Component, Cloneable {

    public T clone();

}
