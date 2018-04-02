package net.glowstone.command.minecraft;

import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.ImmutableList;

import net.glowstone.GlowServer;
import net.glowstone.GlowWorld;
import net.glowstone.command.CommandUtils;
import net.glowstone.entity.GlowPlayer;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Bukkit.class, CommandUtils.class, GlowServer.class, GlowWorld.class})
public class TellrawCommandTest {
    
    private Command command;
    private CommandSender sender;
    private GlowServer server;
    private GlowWorld world;
    private Player fakePlayer1, opPlayer;
    
    @Before
    public void before() {
        command = new TellrawCommand();
        sender = PowerMockito.mock(CommandSender.class);
        server = PowerMockito.mock(GlowServer.class);
        world = PowerMockito.mock(GlowWorld.class);
        
        fakePlayer1 = PowerMockito.mock(GlowPlayer.class);
        
        // set up one player online
        final Location location = new Location(world, 10.5, 20.0, 30.5);
        Mockito.when(fakePlayer1.getName()).thenReturn("player1");
        Mockito.when(fakePlayer1.getLocation()).thenReturn(location);
        Mockito.when(fakePlayer1.getType()).thenReturn(EntityType.PLAYER);
       
        Mockito.when(sender.hasPermission(Mockito.anyString())).thenReturn(true);
        Mockito.when(sender.getServer()).thenReturn(server);
        
        Mockito.doReturn(ImmutableList.of(fakePlayer1))
            .when(server).getOnlinePlayers();
        Mockito.when(world.getEntities())
            .thenReturn(ImmutableList.of(fakePlayer1));
        
        PowerMockito.mockStatic(Bukkit.class);
        Mockito.when(Bukkit.getPlayerExact("player1")).thenReturn(fakePlayer1);
        
        PowerMockito.stub(PowerMockito.method(CommandUtils.class, "getWorld", CommandSender.class))
        .toReturn(world);
    }
    
    @Test
    public void testSuccessExecute() {
        JSONObject json = new JSONObject();
        json.put("text", "hello");
        assertThat(command.execute(sender, "label", new String[] {"player1", json.toJSONString()}), is(true));
        /* It is not possible to spy on what the player sends back because the 
         * TextComponent is difficult to match
         */
        // Mockito.verify(fakePlayer1).sendMessage(eq(TEXTCOMPONENT goes here));
    }

    @Test
    public void testFailExecute() {
        JSONObject json = new JSONObject();
        // the key "hello" is not a valid TextComponent
        json.put("hello", "world");
        assertThat(command.execute(sender, "label", new String[] {"player1", json.toJSONString()}), is(false));
        Mockito.verify(sender).sendMessage(eq("Invalid message"));
    }
}
