import java.time.Duration;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SheepTimeArg extends SheepArg<Duration> {

    public SheepTimeArg() {
    }

    public SheepTimeArg(boolean required, String identifier) {
        super(required, identifier);
    }

    @Override
    public boolean matches(String arg) {
        return false;
    }

    @Override
    public Duration parse(String value) {
        //TODO: Parse Date
        // Convert string 3d to Date object

        value = value.toLowerCase();
        if(value.length() < 2)
            throw new IllegalArgumentException();

        Pattern p = Pattern.compile("\\p{L}");
        Matcher m = p.matcher(value);
        m.find();
        int indexOfUnit = m.start();

        HashMap<String, String> abbrevToWord = new HashMap<>();
        HashMap<String, DurationBuilder> wordToConversion = new HashMap<>();

        abbrevToWord.put("s", "seconds");
        abbrevToWord.put("h", "hours");
        abbrevToWord.put("d", "days");
        abbrevToWord.put("w", "weeks");
        abbrevToWord.put("m", "months");
        abbrevToWord.put("y", "years");

        abbrevToWord.put("sec", "seconds");
        abbrevToWord.put("secs", "seconds");
        abbrevToWord.put("seconds", "seconds");
        abbrevToWord.put("min", "minutes");
        abbrevToWord.put("mins", "minutes");
        abbrevToWord.put("minutes", "minutes");
        abbrevToWord.put("hr", "hours");
        abbrevToWord.put("hrs", "hours");
        abbrevToWord.put("hours", "hours");
        abbrevToWord.put("day", "days");
        abbrevToWord.put("days", "days");
        abbrevToWord.put("wk", "weeks");
        abbrevToWord.put("wks", "weeks");
        abbrevToWord.put("weeks", "weeks");
        abbrevToWord.put("mo", "months");
        abbrevToWord.put("mon", "months");
        abbrevToWord.put("month", "months");
        abbrevToWord.put("months", "months");
        abbrevToWord.put("y", "years");
        abbrevToWord.put("yr", "years");
        abbrevToWord.put("year", "years");
        abbrevToWord.put("years", "years");

        wordToConversion.put("seconds", (long d) -> { return Duration.ofSeconds(d); });
        wordToConversion.put("hours", (long d) -> { return Duration.ofHours(d); });
        wordToConversion.put("days", (long d) -> { return Duration.ofDays(d); });
        wordToConversion.put("weeks", (long d) -> { return Duration.ofDays(d*7); });
        wordToConversion.put("months", (long d) -> { return Duration.ofDays(d*30); });
        wordToConversion.put("years", (long d) -> { return Duration.ofDays(d*365); });

        // TODO: add error for invalid unit (if abbrevToWord does not contain)
        String amountStr = value.substring(0, indexOfUnit);
        //TODO: add try catch for NumberFormatException
        long amount = Long.parseLong(amountStr);
        String unitAbbrev = value.substring(indexOfUnit);
        String unitWord = abbrevToWord.get(unitAbbrev);
        if(unitWord == null)
            throw new IllegalArgumentException(unitAbbrev + " is illegal unit");
        Duration duration = wordToConversion.get(unitWord).buildDuration(amount);



        return duration;
    }

    private static interface DurationBuilder {
        Duration buildDuration(long d);
    }
}
