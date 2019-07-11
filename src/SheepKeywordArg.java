/**
 * DEPRECATED CLASS
 */
public class SheepKeywordArg extends SheepArg<String> {

    public SheepKeywordArg() {
    }

    public SheepKeywordArg(boolean required, String identifier) {
        super(required, identifier);
    }

    @Override
    public boolean matches(String arg) {
        return false;
    }

    @Override
    public String parse(String value) {
        return this.getDefaultValue();
    }
}
