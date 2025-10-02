package de.farmwelt.farmweltplugin;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Optional;

/**
 * Command that connects a player to a configured server.
 */
public final class ConnectCommand implements SimpleCommand {

    private static final Component PREFIX = LegacyComponentSerializer.legacyAmpersand()
            .deserialize("&b&lCraftCity &8&l»&r ");

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
            source.sendMessage(PREFIX.append(Component.text("Dieser Befehl kann nur von Spielern verwendet werden.", NamedTextColor.RED)));
            return;
        }

        Optional<RegisteredServer> server = proxyServer.getServer(targetServerId);
        if (server.isEmpty()) {
            player.sendMessage(PREFIX.append(Component.text("Der Server " + displayName + " ist nicht verfügbar.", NamedTextColor.RED)));
            return;
        }

        player.createConnectionRequest(server.get()).connect().thenAccept(result -> {
            if (!result.isSuccessful()) {
                player.sendMessage(PREFIX.append(Component.text("Verbindung zu " + displayName + " fehlgeschlagen. Bitte versuche es später erneut.", NamedTextColor.RED)));
            } else {
                player.sendMessage(PREFIX.append(Component.text("Du wirst nun mit " + displayName + " verbunden...", NamedTextColor.GREEN)));
            }
        });
    }
}
