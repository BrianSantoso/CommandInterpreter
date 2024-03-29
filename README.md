# CommandInterpreter

Easy to use plugin tool for defining and interpreting simple console commands

<h1> Usage </h1>
Define a command:

```
SheepCMD cmd = new SheepCMD("/watchlist add <Player> [String:reason=Toxicity] [Time:time=time1d]");
```

```/watchlist``` Name of command <br>
```add``` Sub-command <br>
```<Player>``` Required Player argument <br>
```[String:reason=Toxicity]``` Optional String argument named ```reason``` and default value ```"Toxicity"``` <br>
```[Time:time=time1d]``` Optional Time argument named ```time``` and default value ```1d``` (1 day) <br>

Define command's function:
```
cmd.setFunction(args -> {
    System.out.println(args[0] + " , " + args[1] + " , " + args[2]);
});
```

Add command to executor:

```
SheepCMDTree commands = new SheepCMDTree();
commands.addCommand(cmd);
```

Execute a command:
```
SheepCMDTree commands = new SheepCMDTree();
commands.addCommand(cmd);

commands.execute("/watchlist add Bob reason:\"grief\" time:3d");
```

