import java.util.regex.Pattern;

public class SheepIntegerArg extends SheepNumberArg {

    public SheepIntegerArg() {
        super();
    }

    public SheepIntegerArg(boolean required, String identifier) {
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
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new NullPointerException("Value " + value + " is not an Integer");
            }
        } else {
            throw new NumberFormatException("Value is not numeric");
        }

    }
}
