package dk.martinersej.chatevents.hooks;

import dk.martinersej.coinsystem.CoinSystem;
import dk.martinersej.coinsystem.managers.sqlite.CoinsManager;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class CoinsHook {

    private static CoinsManager getCoinSystem() {
        return CoinSystem.getCoinsManager();
    }

    public static void addCoins(UUID player, double amount) {
        getCoinSystem().getCPlayer(player).addCoins(amount);
    }

    public static void addCoins(OfflinePlayer player, double amount) {
        getCoinSystem().getCPlayer(player).addCoins(amount);
    }

    public static void removeCoins(UUID player, double amount) {
        getCoinSystem().getCPlayer(player).removeCoins(amount);
    }

    public static double getCoins(UUID player) {
        return getCoinSystem().getCPlayer(player).getCoins().doubleValue();
    }

    public static void setCoins(UUID player, double amount) {
        getCoinSystem().getCPlayer(player).setCoins(amount);
    }
}
