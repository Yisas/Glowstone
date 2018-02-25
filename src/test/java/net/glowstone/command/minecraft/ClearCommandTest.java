package net.glowstone.command.minecraft;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import net.glowstone.GlowServer;
import net.glowstone.GlowWorld;
import net.glowstone.command.CommandUtils;
import net.glowstone.constants.ItemIds;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.inventory.GlowPlayerInventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Bukkit.class, CommandUtils.class, GlowServer.class, GlowWorld.class, ItemStack[].class,
		ItemStack.class, ItemIds.class })
public class ClearCommandTest {

	private Player fakePlayer1, fakePlayer2;
	private GlowPlayerInventory fakeEmptyPlayerInventory, fakePlayerInventory;
	private ItemStack[] fakeInventoryContents;
	
    private static final ItemStack fakeInventoryItem1 = new ItemStack(Material.IRON_LEGGINGS, 1);
    private static final ItemStack fakeInventoryItem2 = new ItemStack(Material.IRON_HELMET, 1);
	private String fakeInventoryItem1Name = "minecraft:iron_leggings";
	private String fakeInventoryItem2Name = "invalid_item_name";

	private CommandSender senderWithoutPermission, senderWithPermission; // sender will not have permission,
																					// opSender will

	private ClearCommand testedCommand;

	@Before
	public void before() {
		testedCommand = new ClearCommand();

		senderWithoutPermission = PowerMockito.mock(CommandSender.class);
		senderWithPermission = PowerMockito.mock(CommandSender.class);

		fakePlayer1 = PowerMockito.mock(GlowPlayer.class);
		fakePlayer2 = PowerMockito.mock(GlowPlayer.class);

		// Give player 1 an empty inventory
		fakeEmptyPlayerInventory = PowerMockito.mock(GlowPlayerInventory.class);
		Mockito.when(fakePlayer1.getInventory()).thenReturn(fakeEmptyPlayerInventory);
		Mockito.when(fakeEmptyPlayerInventory.getContents()).thenReturn(new ItemStack[10]);

		// Give player 2 a non-empty inventory (2 items)
		fakeInventoryContents = new ItemStack[2];	
		fakeInventoryContents[0] = fakeInventoryItem1;
		fakeInventoryContents[1] = fakeInventoryItem2;
		fakePlayerInventory = PowerMockito.mock(GlowPlayerInventory.class);
		Mockito.when(fakePlayerInventory.getContents()).thenReturn(fakeInventoryContents);
		Mockito.when(fakePlayer2.getInventory()).thenReturn(fakePlayerInventory);

		Mockito.when(fakePlayer1.getName()).thenReturn("player1");
		Mockito.when(fakePlayer2.getName()).thenReturn("player2");

		Mockito.when(fakePlayer1.getType()).thenReturn(EntityType.PLAYER);
		Mockito.when(fakePlayer2.getType()).thenReturn(EntityType.PLAYER);

		Mockito.when(senderWithPermission.hasPermission(Mockito.anyString())).thenReturn(true);

		PowerMockito.mockStatic(Bukkit.class);
		Mockito.when(Bukkit.getPlayerExact("player1")).thenReturn(fakePlayer1);
		Mockito.when(Bukkit.getPlayerExact("player2")).thenReturn(fakePlayer2);
	}

	@Test
	public void testExecuteFailsWithoutPermission() {
		assertThat(testedCommand.execute(senderWithoutPermission, "label", new String[0]), is(false));
		Mockito.verify(senderWithoutPermission).sendMessage(eq(ChatColor.RED
				+ "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error."));
	}

	/**
	 * When executing the command with no parameters by a non-player, command should return false and a message should
	 * be sent to remind user of command usage
	 */
	@Test
	public void testInformUserOfUsage() {
		assertThat(testedCommand.execute(senderWithPermission, "labed", new String[0]), is(false));
		verify(senderWithPermission, times(1))
				.sendMessage(ChatColor.RED + "Usage: /clear <player> [item] [data] [maxCount]");
	}

	/**
	 * When sending a command to a player not on the server, command should return false and display appropriate error
	 * message
	 */
	@Test
	public void testExecuteFailsUnknownTarget() {
		assertThat(testedCommand.execute(senderWithPermission, "label", new String[] { "nonExistentPlayer" }),
				is(false));
		Mockito.verify(senderWithPermission)
				.sendMessage(eq(ChatColor.RED + "Player 'nonExistentPlayer' cannot be found"));
	}

	/**
	 * When sending a command to a player without anything in their inventory, command should return false and display
	 * appropriate error message
	 */
	@Test
	public void testCommandOnPlayerWithEmptyInventory() {
		assertThat(testedCommand.execute(senderWithPermission, "label", new String[] { "player1" }), is(false));
		Mockito.verify(senderWithPermission)
				.sendMessage(eq(ChatColor.RED + "Could not clear the inventory of player1, no items to remove"));
	}

	/**
	 * Test that all items are cleared form a players inventory when calling this command with no args other than the
	 * playername
	 */
	@Test
	public void testClearAll() {
		assertThat(testedCommand.execute(senderWithPermission, "label", new String[] { "player2" }), is(true));
		Mockito.verify(senderWithPermission).sendMessage(eq("Cleared the inventory of player2, removing 2 items"));
	}

	/**
	 * Test that a single item is cleared when executing a clear command and specifying the item to remove
	 */
	@Test
	public void testClearSingle() {
		assertThat(testedCommand.execute(senderWithPermission, "label",
				new String[] { "player2", fakeInventoryItem1Name }), is(true));
		Mockito.verify(senderWithPermission).sendMessage(eq("Cleared the inventory of player2, removing 1 items"));
	}

	/**
	 * Test that no item is removed when specifying an invalid item name, as well as return false for the command
	 */
	@Test
	public void testClearInvalidItemName() {
		assertThat(testedCommand.execute(senderWithPermission, "label",
				new String[] { "player2", fakeInventoryItem2Name }), is(false));
		Mockito.verify(senderWithPermission)
				.sendMessage(eq(ChatColor.RED + "There is no such item with name minecraft:" + fakeInventoryItem2Name));
	}
}
