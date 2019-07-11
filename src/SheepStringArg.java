public class SheepStringArg extends SheepArg<String> {

    public SheepStringArg() {
    }

    public SheepStringArg(boolean required, String identifier) {
        super(required, identifier);
    }

    @Override
    public boolean matches(String arg) {
        return false;
    }

    @Override
    public String parse(String value) {
        return value;
    }
}
