package net.glowstone.command.minecraft;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Collections;

import net.glowstone.GlowServer;
import net.glowstone.command.CommandUtils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Bukkit.class, CommandUtils.class, GlowServer.class})

public class SaveAllCommandTest {

	private CommandSender sender, failedSender;
	private Command command;

	@Mock
	GlowServer server;

	@Before
	public void before() {
		PowerMockito.mockStatic(Bukkit.class);

		sender = PowerMockito.mock(CommandSender.class);
		failedSender = PowerMockito.mock(CommandSender.class);
		server = PowerMockito.mock(GlowServer.class);
		command = new SaveAllCommand();

		PowerMockito.mockStatic(Bukkit.class);
		when(Bukkit.getServer()).thenReturn(server);

		PowerMockito.when(sender.hasPermission(Mockito.anyString()))
				.thenReturn(true);
		PowerMockito.when(sender.getServer()).thenReturn(server);

	}

	@Test
	public void testExecuteSucceeds() {
		assertThat(command.execute(sender, "label",
				new String[] { "minecraft.command.save-all" }), is(true));
		Mockito.verify(sender).sendMessage(eq("Saved the worlds"));

	}

	@Test
	public void testFailExecute() {

		assertThat(command.execute(failedSender, "label", new String[] { "" }),
				is(false));
	}

	@Test
	public void testTabComplete() {
		assertThat(command.tabComplete(null, null, null),
				is(Collections.emptyList()));
		assertThat(command.tabComplete(sender, "", new String[0]),
				is(Collections.emptyList()));
	}
}
