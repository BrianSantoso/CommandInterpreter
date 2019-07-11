import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Pattern;

public class SheepCMD {

    /***
     *
     *  /watchlist add <Player> [Time] [Reason]
     *  /watchlist list [number]
     *
     */

    public static final String ALLOWED_VAR_CHARS = "[A-Za-z0-9_]*";

    private String command;
    private String commandIdentifier;
    private SheepArg[] args;
    private HashMap<String, Integer> argIndexMap;
    private SheepCMDFunction function;
    private int argStartIndex;

    public SheepCMD(String command){


        this.function = null;
        String cmd = removeSlash(command).trim();
        this.command = cmd;
        String[] args = splitSpace(cmd);

        int i = 0;

        String str = "";
        while(i < args.length && isKeyword(args[i])){
            str += args[i++] + " ";
        }
        this.commandIdentifier = str.trim();
        this.args = new SheepArg[args.length - i];
        this.argStartIndex = i;
        for(int j = 0; i < args.length; i++, j++){
            String arg = args[i];
            boolean required = isRequiredArg(arg);
            arg = arg.substring(1, arg.length() - 1);
            String[] specs = arg.split(":");
            String type = specs[0];
            String identifier = "";
            String defaultValue = null;
            if(specs.length > 1){
                String[] idAndArg = splitIgnoreQuotes(specs[1], "=");
                identifier = idAndArg[0];
                if (idAndArg.length > 1){
                    if(isRequiredArg(arg)){
                        defaultValue = idAndArg[1];
                    } else {
                        // Default values should not be specified for required arguments
                        throw new IllegalArgumentException();
                    }
                }

            }

            SheepArg sheepArg = constructArg(type, required, identifier, defaultValue);
            this.args[j] = sheepArg;
        }

        this.argIndexMap = constructIndexMap(this.args);

    }

    public HashMap<String, Integer> getArgIndexMap() {
        return argIndexMap;
    }

    public int getArgStartIndex() {
        return argStartIndex;
    }

    public String getCommandIdentifier(){
        return commandIdentifier;
    }

    public void addCase(){

    }

    public void setFunction(SheepCMDFunction function){
        this.function = function;
    }

    public SheepCMDFunction getFunction() {
        return function;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public SheepArg[] getArgs() {
        return args;
    }

    public void setArgs(SheepArg[] args) {
        this.args = args;
    }

    private HashMap<String, Integer> constructIndexMap(SheepArg[] args){
        HashMap indexMap = new HashMap<>();
        for(int i = 0; i < args.length; i++){
            if(args[i].getIdentifier() != null)
                indexMap.put(args[i].getIdentifier(), i);
        }
        return indexMap;
    }

    private static HashMap<String, SheepArgTypeBuilder<SheepArg>> argTypeMap = new HashMap<String, SheepArgTypeBuilder<SheepArg>>(){
        {
            put("player", new SheepArgTypeBuilder<SheepArg>() {
                @Override
                public SheepPlayerArg build() {
                    return new SheepPlayerArg();
                }
            });
            ;
            put("time", new SheepArgTypeBuilder<SheepArg>() {
                @Override
                public SheepTimeArg build() {
                    return new SheepTimeArg();
                }
            });
            put("string", new SheepArgTypeBuilder<SheepArg>() {
                @Override
                public SheepStringArg build() {
                    return new SheepStringArg();
                }
            });
            put("number", new SheepArgTypeBuilder<SheepArg>() {
                @Override
                public SheepNumberArg build() {
                    return new SheepNumberArg();
                }
            });
        }
    };

    private static SheepArg constructArg(String type, boolean required, String identifier, String defaultValue){
        String ignoreCase = type.toLowerCase();
        SheepArgTypeBuilder sheepArgTypeBuilder = argTypeMap.get(ignoreCase);
        SheepArg sheepArg = (SheepArg) sheepArgTypeBuilder.build();
        try {
            sheepArg.setRequired(required);
            sheepArg.setIdentifier(identifier);
            sheepArg.setDefaultValue(defaultValue);
        } catch (NullPointerException e){
            throw new IllegalArgumentException();
        }
        return sheepArg;
    }

    public static String[] splitSpace(String line){

        String otherThanQuote = " [^\"] ";
        String quotedString = String.format(" \" %s* \" ", otherThanQuote);
        String regex = String.format("(?x) "+ // enable comments, ignore white spaces
                        "\\s+                      "+ // match a <space+>
                        "(?=                       "+ // start positive look ahead
                        "  (?:                     "+ //   start non-capturing group 1
                        "    %s*                   "+ //     match 'otherThanQuote' zero or more times
                        "    %s                    "+ //     match 'quotedString'
                        "  )*                      "+ //   end group 1 and repeat it zero or more times
                        "  %s*                     "+ //   match 'otherThanQuote'
                        "  $                       "+ // match the end of the string
                        ")                         ", // stop positive look ahead
                otherThanQuote, quotedString, otherThanQuote);

        String[] tokens = line.split(regex, -1);
        return tokens;
    }

    public static String[] splitIgnoreQuotes(String line, String delimeter){

        String otherThanQuote = " [^\"] ";
        String quotedString = String.format(" \" %s* \" ", otherThanQuote);
        String regex = String.format("(?x) "+ // enable comments, ignore white spaces
                        delimeter+ // match a <delimeter>
                        "(?=                       "+ // start positive look ahead
                        "  (?:                     "+ //   start non-capturing group 1
                        "    %s*                   "+ //     match 'otherThanQuote' zero or more times
                        "    %s                    "+ //     match 'quotedString'
                        "  )*                      "+ //   end group 1 and repeat it zero or more times
                        "  %s*                     "+ //   match 'otherThanQuote'
                        "  $                       "+ // match the end of the string
                        ")                         ", // stop positive look ahead
                otherThanQuote, quotedString, otherThanQuote);

        String[] tokens = line.split(regex, -1);
        return tokens;
    }

    public static boolean isKeyword(String arg){
        Pattern pattern_alphanumeric = Pattern.compile(ALLOWED_VAR_CHARS);
        return pattern_alphanumeric.matcher(arg).matches();
    }

    private static boolean isRequiredArg(String arg){
        String first = arg.substring(0, 1);
        String last = arg.substring(arg.length() - 1);

        if(first.equals("<")){
            if(last.equals(">")){
                return true;
            } else {
                throw new IllegalArgumentException();
            }
        } else if(first.equals("[")) {
            if(last.equals("]")){
                return false;
            } else {
                throw new IllegalArgumentException();
            }
        }

        return true;
    }

    public static String removeSlash(String command){
        if(command.charAt(0) == '/')
            return command.substring(1);
        return command;
    }

    @Override
    public String toString() {
        return "SheepCMD{" +
                "args=" + Arrays.toString(args) +
                '}';
    }



    public static void main(String[] arg0){

        SheepCMD cmd1 = new SheepCMD("/watchlist add <Player> [String:reason=Toxicity] [Time:time=time1d]");
        cmd1.setFunction(args -> {
            System.out.println(args[0] + " , " + args[1] + " , " + args[2]);
        });
        SheepCMDTree commands = new SheepCMDTree();
        commands.addCommand(cmd1);

        SheepCMD cmd2 = new SheepCMD("/owo blackjack <Number:bet=1>");
        cmd2.setFunction(args -> {
            System.out.println(args[0]);
        });
        commands.addCommand(cmd2);

        commands.execute("/watchlist add TauCubed reason:\"grief\" time:3d");
        commands.execute("/owo blackjack 3");

    }

}
