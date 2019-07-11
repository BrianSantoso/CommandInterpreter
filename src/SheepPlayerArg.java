import java.util.UUID;

public class SheepPlayerArg extends SheepArg<UUID> {
    public SheepPlayerArg() {
    }

    public SheepPlayerArg(boolean required, String identifier) {
        super(required, identifier);
    }

    @Override
    public boolean matches(String arg) {
        return false;
    }

    @Override
    public UUID parse(String value) {
        return null;
}
}
