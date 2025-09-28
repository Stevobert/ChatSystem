# ChatSystem

ChatSystem is a full chat solution mainly focusing on chat colors but also including a lot of other features.

I built this from scratch as my first major project, with barely any Java knowledge.
I learned everything along the way, which is also the reason why it's not fully polished and probably contains a lot of bugs.

Feel free to modify and improve this plugin as you like.

## Dependencies
- [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
- The plugin was built on 1.21+. It may work on lower versions but I did't test it.

## Features

Most features are fully customizable through the config.

- Custom chat color supporting legacy and hex colors as well as gradients and styling 
- GUI to modify chat color and style
- Duplicated message checker
- Message cooldown
- Player pings and @everyone ping
- Clearchat command
- Mutechat command
- Staff chat command
- Admin chat command
- Showing the tool with "[i]"

## Commands
- '/chatcolor <player> <color|#default|#gradient|#hex|#style> [colors|styles]'
    - Change someone's chat color or open the GUI
- '/chatsystem <reload|ver>'
    - Main plugin command
- '/staffchat [message]'
    - Toggle the staff chat or send a message to it
- '/adminchat [message]'
    - Toggle the admin chat or send a message to it
- '/clearchat'
    - Clear the chat for everyone
- '/mutechat'
    - Mute the chat for everyone
- '/togglepings'
    - Toggle pings
