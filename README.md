![mineweb-logo](mineweb.png)

Minecraft Bukkit plugin to fetch current server information via JSON. With this tool it is possible to provide current server data in realtime for your website.

## Getting started

When you put the plugin ```mineweb.jar``` in the plugin folder and start Minecraft the first time, a new file will be created, called ```mineweb.properties```. There you can set for instance the port:

```properties
port = 25555
```

Afterwards you can run your server properly.

### Fetching data

It is now possible to fetch the data on your website by calling ```http://minecraftip:port```. Here is a small **JQuery** example:

```javascript
$.ajax({
                type: "GET",
                url: "proxy.php?url=" + ip + ":" + serverPort,
                dataType: "json",
                crossDomain: true,
                success: function(data) {
                    console.log("The current port is: " + data['port']);
                }
            });
```
