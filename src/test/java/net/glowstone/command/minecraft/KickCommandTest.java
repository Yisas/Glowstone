package net.glowstone.command.minecraft;

import net.glowstone.GlowServer;
import net.glowstone.GlowWorld;
import net.glowstone.command.CommandUtils;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;




import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import net.glowstone.command.minecraft.KickCommand;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Bukkit.class, CommandUtils.class, GlowServer.class, GlowWorld.class})
public class KickCommandTest {

   private CommandSender sender, opSender;
   private Command command;
   private Player player1;
   private GlowServer server;

   @Before
   public void before() {
       PowerMockito.mockStatic(Bukkit.class);

       // create the mocks
       sender = PowerMockito.mock(CommandSender.class);
       opSender = PowerMockito.mock(CommandSender.class);
       server = mock(GlowServer.class);
       player1 = mock(Player.class);

       command = new KickCommand();

       Mockito.when(player1.getName()).thenReturn("player1");
       Mockito.when(player1.getType()).thenReturn(EntityType.PLAYER);
       Mockito.when(server.getOfflinePlayerAsync("player1")).thenReturn(CompletableFuture.completedFuture(player1));


       // stub
       Mockito.when(opSender.hasPermission(Mockito.anyString())).thenReturn(true);

       mockStatic(Bukkit.class);
       Mockito.when(Bukkit.getServer()).thenReturn(server);
       Mockito.when(Bukkit.getPlayerExact("player1")).thenReturn(player1);

   }

   @Test
   public void testExecuteFailsWithoutPermission() {

       final boolean commandResult = command.execute(sender, "someLabel", new String[0]);

       assertThat(commandResult, is(false));
       Mockito.verify(sender).sendMessage(eq(ChatColor.RED
               + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error."));
   }

   @Test
   public void testExecuteFailsWithoutParameters() {

       final boolean commandResult = command.execute(opSender, "someLabel", new String[0]);

       assertThat(commandResult, is(false));
       Mockito.verify(opSender).sendMessage(eq(ChatColor.RED + "Usage: /kick <player> [reason]"));
   }

   @Test
   public void testExecutePlayerNotOnline() {
       final boolean commandResult = command.execute(opSender, "someLabel", new String[] {null});

       assertThat(commandResult, is(false));
       Mockito.verify(opSender).sendMessage(ChatColor.RED + "Player 'null' is not online");
   }

   @Test
   public void testExecutePlayerIsKicked() {

       final boolean commandResult = command.execute(opSender, "someLabel", new String[] {"player1"});

       assertThat(commandResult, is(true));
       Mockito.verify(opSender).sendMessage("Kicked player1");
   }
   
   @Test
	public void testTabComplete() {
		assertThat(command.tabComplete(sender, "", new String[0]),
				is(Collections.emptyList()));
	}
   
   
}