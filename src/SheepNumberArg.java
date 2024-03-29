import java.util.regex.Pattern;

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
        Pattern pattern_numeric = Pattern.compile("[0-9.]*");
        if(pattern_numeric.matcher(value).matches()){
            return Double.parseDouble(value);
        } else {
            throw new NumberFormatException("Value is not numeric");
        }

    }
}
