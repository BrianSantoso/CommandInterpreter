public class SheepNumberArg extends SheepArg<Number> {

    public SheepNumberArg() {

    }

    public SheepNumberArg(boolean required, String identifier) {
        super(required, identifier);
    }

    @Override
    public boolean matches(String arg) {
        return false;
    }

    @Override
    public Number parse(String value) {
        return Double.parseDouble(value);
    }
}
