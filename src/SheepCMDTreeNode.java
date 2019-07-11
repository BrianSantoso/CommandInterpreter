import java.util.Arrays;
import java.util.HashMap;

public class SheepCMDTreeNode {

    private String val;
    private SheepCMD command;
    private HashMap<String, SheepCMDTreeNode> children;

    public SheepCMDTreeNode(String val, SheepCMD command){
        this.val = val;
        this.command = command;
        children = new HashMap<>();
    }

    public SheepCMD getCommand() {
        return command;
    }

    public SheepCMD getCMD(){
        return getCommand();
    }

    public void setCommand(SheepCMD command) {
        this.command = command;
    }

    public boolean hasChildren(){
        return !children.isEmpty();
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public HashMap<String, SheepCMDTreeNode> getChildren() {
        return children;
    }

    public SheepCMDTreeNode addChild(SheepCMD command, String keywordIdentifier){

        if(children.containsKey(keywordIdentifier)){
            return children.get(keywordIdentifier);
        } else {
            SheepCMDTreeNode node = new SheepCMDTreeNode(keywordIdentifier, command);
            children.put(keywordIdentifier, node);
            return node;

        }

    }

    public SheepCMDTreeNode getChild(String keywordIdentifier){
        return children.get(keywordIdentifier);
    }

    public void setChildren(HashMap<String, SheepCMDTreeNode> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "SheepCMDTreeNode{" +
                "val='" + val + '\'' +
                ", children=" + children +
                '}';
    }
}
