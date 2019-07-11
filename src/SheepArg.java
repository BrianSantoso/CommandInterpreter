public abstract class SheepArg<T> {

    private boolean required;
    private String identifier;
    private String defaultValue;


    public SheepArg(){

    }

    public SheepArg(boolean required, String identifier){
        this.required = required;
        this.identifier = identifier;
    }

    public abstract boolean matches(String arg);

    public abstract T parse(String value);

    public boolean isRequired() {
        return required;
    }

    public String getIdentifier() {
        return identifier;
    }

    public T getParsedDefaultValue() { return parse(defaultValue); }

    public String getDefaultValue(){
        return defaultValue;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }



    @Override
    public String toString() {
        return "SheepArg{" +
                "type=" + getClass() +
                ", required=" + required +
                ", identifier=" + identifier +
                ", defaultValue=" + defaultValue +
                '}';
    }

}
