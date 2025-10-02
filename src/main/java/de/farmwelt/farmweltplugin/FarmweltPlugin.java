package de.farmwelt.farmweltplugin;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

/**
 * Registers quick connect commands for Farmwelt and CityBuild servers.
 */
@Plugin(id = "farmweltplugin", name = "FarmweltPlugin", version = "1.0.0")
public final class FarmweltPlugin {

    private final ProxyServer proxyServer;
    private final Logger logger;

    @Inject
    public FarmweltPlugin(ProxyServer proxyServer, Logger logger) {
        this.proxyServer = proxyServer;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CommandManager commandManager = proxyServer.getCommandManager();

        commandManager.register("farmwelt", new ConnectCommand(proxyServer, "farmwelt", "Farmwelt"));
        commandManager.register("citybuild", new ConnectCommand(proxyServer, "citybuild", "CityBuild"));

        logger.info("FarmweltPlugin commands registered: /farmwelt, /citybuild");
    }
}
