ChatAPI
=======

Gives Bungeecord and CraftBukkit an easy to use Chat Message Builder. Also a simple BBCode Parser can be used.

https://github.com/mickare/ChatAPI/wiki

CraftBukkit
-----------
```
IChatBuilder<Player> b = BukkitChatAPI.newBuilder();
b.appendText(ChatColor.RED + "TEST");
b.build().sendToPlayer(player);
```

```
IChatBBCodeBuilder<Player> b = BukkitChatAPI.newBBCodeBuilder();
b.appendBBCode(ChatColor.RED + "YES! [b]this is [i]bbcode[i] ![r] check out the reference on github[/r][/b]");
b.build().sendToPlayer(player);
```

Bungeecord (yet untested!)
-----------
```
IChatBuilder<ProxiedPlayer> b = BungeeChatAPI.newBuilder();
b.appendText(ChatColor.RED + "TEST");
b.build().sendToPlayer(player);
```

```
IChatBBCodeBuilder<ProxiedPlayer> b = BungeeChatAPI.newBBCodeBuilder();
b.appendBBCode(ChatColor.RED + "YES! [b]this is [i]bbcode[i] ![r] check out the reference on github[/r][/b]");
b.build().sendToPlayer(player);
```

Maven
-----

http://repo.rennschnitzel.de:8081/nexus/content/repositories/rs-public/

Download latest:
http://repo.rennschnitzel.de:8081/nexus/content/repositories/rs-public/de/mickare/chatapi/
