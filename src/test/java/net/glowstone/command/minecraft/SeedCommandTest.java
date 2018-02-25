package net.glowstone.command.minecraft;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.hamcrest.CoreMatchers.is;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.VanillaCommand;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import static org.powermock.api.mockito.PowerMockito.when;
import net.glowstone.GlowServer;
import net.glowstone.GlowWorld;
import net.glowstone.command.CommandUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CommandUtils.class, World.class, CommandSender.class,VanillaCommand.class,GlowServer.class,GlowWorld.class})
public class SeedCommandTest {

	private SeedCommand command;
	private CommandSender sender;
	private World world;
	private VanillaCommand vCommand;
	private CommandUtils commandUtils;
    private GlowServer server ;
    private GlowWorld gworld;
        
	@Before
	public void before() {
		command = new SeedCommand();
		server = PowerMockito.mock(GlowServer.class);
		sender = PowerMockito.mock(CommandSender.class);
		world = PowerMockito.mock(World.class);
		gworld = PowerMockito.mock(GlowWorld.class);
		
		mockStatic(CommandUtils.class);
		when(CommandUtils.getWorld(sender)).thenReturn(gworld);
		when(world.getSeed()).thenReturn((long) 100);
		
	}
	
	
	
	@Test
	public void testExecuteFailure() {
		assertThat(command.execute(sender, "Label", new String[] {""}), is(false));
	}

}
