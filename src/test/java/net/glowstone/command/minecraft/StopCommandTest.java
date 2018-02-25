package net.glowstone.command.minecraft;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
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
@PrepareForTest({Bukkit.class, CommandUtils.class, GlowServer.class})

public class StopCommandTest {

	private Command command;
    private CommandSender sender, failedSender;

    @Mock
    GlowServer server;

	@Before
	public void before() throws Exception{
		 command = new StopCommand();
		 server = PowerMockito.mock(GlowServer.class);
	     sender = PowerMockito.mock(CommandSender.class);
	     failedSender = PowerMockito.mock(CommandSender.class);

		 PowerMockito.mockStatic(Bukkit.class);
		 when(Bukkit.getServer()).thenReturn(server);

		 PowerMockito.when(sender.hasPermission(Mockito.anyString())).thenReturn(true);
	     PowerMockito.when(sender.getServer()).thenReturn(server);

	}

	 @Test
	    public void testSuccessExecute() {
	        assertThat(command.execute(sender, "label", new String[] {"minecraft.command.stop"}), is(true));
       }




}



