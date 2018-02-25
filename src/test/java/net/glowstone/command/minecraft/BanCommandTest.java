package net.glowstone.command.minecraft;

import com.google.common.collect.ImmutableList;
import net.glowstone.GlowServer;
import net.glowstone.GlowWorld;
import net.glowstone.command.CommandUtils;
import net.glowstone.entity.meta.profile.GlowPlayerProfile;
import net.glowstone.entity.objects.GlowMinecart;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Bukkit.class, CommandUtils.class, GlowServer.class, GlowWorld.class})
public class BanCommandTest {

    private Command command;
    private CommandSender blockedSender, sender;
    private GlowServer server;
    private GlowWorld world;
    private Player player1, player2;
    private UUID uuid;


    @Before
    public void before() {
        command = new BanCommand();
        blockedSender = mock(CommandSender.class);
        sender = mock(CommandSender.class);
        server = mock(GlowServer.class);
        world = mock(GlowWorld.class);
        player1 = mock(Player.class);
        player2 = mock(Player.class);
        uuid = UUID.randomUUID();


        Location location = new Location(world, 10.5, 20.0, 30.5);
        Mockito.when(player1.getName()).thenReturn("player1");
        Mockito.when(player1.getLocation()).thenReturn(location);
        Mockito.when(player1.getType()).thenReturn(EntityType.PLAYER);
        Mockito.when(player2.getName()).thenReturn("player2");
        Mockito.when(player2.getLocation()).thenReturn(location);
        Mockito.when(player2.getType()).thenReturn(EntityType.PLAYER);

        Mockito.when(server.getOfflinePlayerAsync("player1")).thenReturn(CompletableFuture.completedFuture(player1));
        Mockito.when(server.getOfflinePlayerAsync("player2")).thenReturn(CompletableFuture.completedFuture(player2));

        Mockito.when(sender.hasPermission(Mockito.anyString())).thenReturn(true);
        Mockito.when(sender.getServer()).thenReturn(server);

        Mockito.when(server.getOfflinePlayerAsync("player3")).thenReturn(CompletableFuture.completedFuture(null));
        Mockito.when(server.getPlayer("player1")).thenReturn(player1);

        Mockito.when(world.getEntities())
                .thenReturn(ImmutableList.of(player1));
        PowerMockito.stub(PowerMockito.method(CommandUtils.class, "getWorld", CommandSender.class))
                .toReturn(world);

    }

    @Test
    public void testExecuteFailsWithoutPermission() {
        assertThat(command.execute(blockedSender, "label", new String[0]), is(false));
        Mockito.verify(blockedSender).sendMessage(eq(ChatColor.RED
                + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error."));
    }

    @Test
    public void testExecuteFailsOneArg() {
        assertThat(command.execute(sender, "label", new String[0]), is(false));
        Mockito.verify(sender).sendMessage(eq(ChatColor.RED + "Usage: /ban <player> [reason]"));
    }

    @Test
    public void testExecuteFailsNullPlayer() {
        String[] args = new String[]{"player3"};
        assertThat(command.execute(sender, "label", args), is(true));
        Mockito.verify(sender).sendMessage(eq(ChatColor.RED + "Could not ban player player3"));
    }

    @Test
    public void testExecuteOnePlayerSuccessful() {
        String[] args = new String[]{"player1"};
        assertThat(command.execute(sender, "label", args), is(true));
    }

    @Test
    public void testExecuteMultiplePlayersSuccessful() {
        String[] args = new String[]{"player1", "player2"};
        assertThat(command.execute(sender, "label", args), is(true));
    }

}
