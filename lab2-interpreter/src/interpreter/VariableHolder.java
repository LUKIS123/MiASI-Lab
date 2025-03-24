package interpreter;

public class VariableHolder<T> {
    public String Name;
    public T Value;

    public VariableHolder() {
    }

    public VariableHolder(String name) {
        Name = name;
    }

    public VariableHolder(String name, T value) {
        Name = name;
        Value = value;
    }
}
