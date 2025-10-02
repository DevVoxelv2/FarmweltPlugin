package de.farmwelt.farmweltplugin;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Optional;

/**
 * Command that connects a player to a configured server.
 */
public final class ConnectCommand implements SimpleCommand {

    private final ProxyServer proxyServer;
    private final String targetServerId;
    private final String displayName;

    public ConnectCommand(ProxyServer proxyServer, String targetServerId, String displayName) {
        this.proxyServer = proxyServer;
        this.targetServerId = targetServerId;
        this.displayName = displayName;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();

        if (!(source instanceof Player player)) {
            source.sendMessage(Component.text("Dieser Befehl kann nur von Spielern verwendet werden.", NamedTextColor.RED));
            return;
        }

        Optional<RegisteredServer> server = proxyServer.getServer(targetServerId);
        if (server.isEmpty()) {
            player.sendMessage(Component.text("Der Server " + displayName + " ist nicht verfügbar.", NamedTextColor.RED));
            return;
        }

        player.createConnectionRequest(server.get()).connect().thenAccept(result -> {
            if (!result.isSuccessful()) {
                Component reason = result.getReason().orElse(Component.text("Unbekannter Fehler", NamedTextColor.RED));
                player.sendMessage(Component.text("Verbindung zu " + displayName + " fehlgeschlagen: ", NamedTextColor.RED).append(reason));
            } else {
                player.sendMessage(Component.text("Du wirst nun mit " + displayName + " verbunden...", NamedTextColor.GREEN));
            }
        });
    }
}
