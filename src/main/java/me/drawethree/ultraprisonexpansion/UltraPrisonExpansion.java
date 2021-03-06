package me.drawethree.ultraprisonexpansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.drawethree.ultraprisoncore.UltraPrisonCore;
import me.drawethree.ultraprisoncore.ranks.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class will be registered through the register-method in the
 * plugins onEnable-method.
 */
public class UltraPrisonExpansion extends PlaceholderExpansion {

	private UltraPrisonCore plugin;

	/**
	 * Because this is a internal class, this check is not needed
	 * and we can simply return {@code true}
	 *
	 * @return Always true since it's an internal class.
	 */
	@Override
	public boolean canRegister(){
		return (plugin = (UltraPrisonCore) Bukkit.getPluginManager().getPlugin(getRequiredPlugin())) != null;
	}

	@Override
	public String getRequiredPlugin(){
		return "UltraPrisonCore";
	}

	/**
	 * The name of the person who created this expansion should go here.
	 * <br>For convienience do we return the author from the plugin.yml
	 *
	 * @return The name of the author as a String.
	 */
	@Override
	public String getAuthor() {
		return plugin.getDescription().getAuthors().toString();
	}

	/**
	 * The placeholder identifier should go here.
	 * <br>This is what tells PlaceholderAPI to call our onRequest
	 * method to obtain a value if a placeholder starts with our
	 * identifier.
	 * <br>This must be unique and can not contain % or _
	 *
	 * @return The identifier in {@code %<identifier>_<value>%} as String.
	 */
	@Override
	public String getIdentifier() {
		return "ultraprison";
	}

	/**
	 * This is the version of the expansion.
	 * <br>You don't have to use numbers, since it is set as a String.
	 * <p>
	 * For convienience do we return the version from the plugin.yml
	 *
	 * @return The version as a String.
	 */
	@Override
	public String getVersion() {
		return plugin.getDescription().getVersion();
	}

	/**
	 * This is the method called when a placeholder with our identifier
	 * is found and needs a value.
	 * <br>We specify the value identifier in this method.
	 * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
	 *
	 * @param player
	 * @param identifier A String containing the identifier/value.
	 * @return possibly-null String of the requested identifier.
	 */
	@Override
	public String onPlaceholderRequest(Player player, String identifier) {

		if (player == null) {
			return "";
		}


		if (identifier.equalsIgnoreCase("tokens")) {
			return String.format("%,d", plugin.getTokens().getTokensManager().getPlayerTokens(player));
		} else if (identifier.equalsIgnoreCase("gems")) {
			return String.format("%,d", plugin.getGems().getGemsManager().getPlayerGems(player));
		} else if (identifier.equalsIgnoreCase("blocks")) {
			return String.format("%,d", plugin.getTokens().getTokensManager().getPlayerBrokenBlocks(player));
		} else if (identifier.equalsIgnoreCase("multiplier")) {
			return String.format("%.2f", plugin.getMultipliers().getApi().getPlayerMultiplier(player));
		} else if (identifier.equalsIgnoreCase("multiplier_global")) {
			return String.format("%.2f", plugin.getMultipliers().getApi().getGlobalMultiplier());
		} else if (identifier.equalsIgnoreCase("rank")) {
			return plugin.getRanks().getApi().getPlayerRank(player).getPrefix();
		} else if (identifier.equalsIgnoreCase("next_rank")) {
			Rank nextRank = plugin.getRanks().getApi().getNextPlayerRank(player);
			return nextRank == null ? "" : nextRank.getPrefix();
		} else if (identifier.equalsIgnoreCase("prestige")) {
			return plugin.getRanks().getApi().getPlayerPrestige(player).getPrefix();
		} else if (identifier.equalsIgnoreCase("autominer_time")) {
			return plugin.getAutoMiner().getTimeLeft(player);
		} else if (identifier.equalsIgnoreCase("tokens_formatted")) {
			return formatNumber(plugin.getTokens().getTokensManager().getPlayerTokens(player));
		} else if (identifier.equalsIgnoreCase("gems_formatted")) {
			return formatNumber(plugin.getGems().getGemsManager().getPlayerGems(player));
		} else if (identifier.equalsIgnoreCase("rankup_progress")) {
			return String.format("%d%%", plugin.getRanks().getRankManager().getRankupProgress(player));
		}

		return null;
	}

	public static String formatNumber(double amount) {
		if (amount <= 1000.0D)
			return String.valueOf(amount);
		ArrayList<String> suffixes = new ArrayList<>(Arrays.asList(new String[]{
				"", "k", "M", "B", "T", "q", "Q", "QT", "S", "SP", "O",
				"N", "D"}));
		double chunks = Math.floor(Math.floor(Math.log10(amount) / 3.0D));
		amount /= Math.pow(10.0D, chunks * 3.0D - 1.0D);
		amount /= 10.0D;
		String suffix = suffixes.get((int) chunks);
		String format = String.valueOf(amount);
		if (format.replace(".", "").length() > 5)
			format = format.substring(0, 5);
		return format + suffix;
	}
}