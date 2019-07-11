import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class SheepCMDTree {

    private SheepCMDTreeNode root;

    public SheepCMDTree(){
        root = new SheepCMDTreeNode(null, null);
    }

    public SheepCMDTree(List<SheepCMD> commands){
        for(SheepCMD cmd : commands){
            addCommand(cmd);
        }
    }

    public SheepCMD addCommand(SheepCMD command){
        // TODO: Put check somewhere to make sure command is correctly formatted and allow spaces and colons in things like strings. Also check if identifiers are alphanumeric
        String[] tokens = SheepCMD.splitSpace(command.getCommandIdentifier());
        SheepCMDTreeNode current = root;
        for(String keywordIdentifier : tokens){
            current = current.addChild(command, keywordIdentifier);
        }

        current.setCommand(command);
        return command;
    }

    public SheepCMD addCommand(String command){
        return addCommand(new SheepCMD(command));
    }

    public void execute(String command, String[] args){
        String str = command + " " + args.toString();
        execute(str);
    }

    public void execute(String commandString){

        SheepCMD correspondingCommand = findCMD(commandString);
        SheepArg[] argsOutline = correspondingCommand.getArgs();
        String[] values = new String[argsOutline.length];
        String[] tokens = SheepCMD.splitSpace(commandString);
        int i = correspondingCommand.getArgStartIndex();
        int numProvidedTokers = Math.min(tokens.length - i, argsOutline.length); // Ignore extra arguments
        LinkedHashSet<Integer> mappedIndices = new LinkedHashSet<>();

        int j = 0;
        for(; i < argsOutline.length + correspondingCommand.getArgStartIndex() && j < numProvidedTokers; i++, j++){
            String arg = tokens[i];
            // TODO: Allow colons in strings
            String[] argTokens = SheepCMD.splitIgnoreQuotes(arg, ":");
            boolean hasIdentifier = argTokens.length > 1;
            int mappedIndex = -1;
            String identifier = null;
            String value = null;

            if(hasIdentifier){
                identifier = argTokens[0];
                value = argTokens[1];
                mappedIndex = correspondingCommand.getArgIndexMap().get(identifier);
            } else {
                value = argTokens[0];
                mappedIndex = j;
            }

            if(mappedIndices.contains(mappedIndex)){
                //TODO: some error
            } else {
                mappedIndices.add(mappedIndex);
            }
            values[mappedIndex] = value;

        }

        if(mappedIndices.size() < numProvidedTokers){
            //TODO: add error for wrongly inputted args
            throw new IllegalArgumentException();
        }

        ArrayList<Integer> mappedIndicesList = new ArrayList<>(mappedIndices);

        while(j < argsOutline.length){
            values[j] = argsOutline[j].getDefaultValue();
            mappedIndicesList.add(j);
            j++;
        }

        Object[] parsedInputArgs = new Object[argsOutline.length];

        for(int l = 0; l < argsOutline.length; l++){
            int k = l + correspondingCommand.getArgStartIndex();
            SheepArg matchingArg = argsOutline[l];
            parsedInputArgs[l] = matchingArg.parse(values[l]);
        }

        correspondingCommand.getFunction().execute(parsedInputArgs);
    }

    public SheepCMD findCMD(String command){

        // TODO: Preprocess commands instead of splitting
        // Ex- allow spaces in strings and other things, allow colons in strings, etc.
        int depth = 0;
        String[] tokens = SheepCMD.splitSpace(SheepCMD.removeSlash(command));
        SheepCMDTreeNode current = root;
        for(String token : tokens) {
            if (current.hasChildren()) {
                current = current.getChild(token);
                depth++;
            } else {
                break;
            }
        }

        return current.getCommand();
    }

    public SheepCMDTreeNode getRoot() {
        return root;
    }

    public void setRoot(SheepCMDTreeNode root) {
        this.root = root;
    }
}
