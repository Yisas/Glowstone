package net.glowstone.io.nosql;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import net.glowstone.GlowServer;
import net.glowstone.GlowWorld;
import net.glowstone.command.CommandUtils;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.io.json.JsonPlayerStatisticIoService;
import net.glowstone.util.StatisticMap;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Bukkit.class, CommandUtils.class, GlowServer.class, GlowWorld.class})
public class MongoDbPlayerStasticIoServiceTest {
    
    private GlowServer server;
    private Player fakePlayer1;
    private GlowWorld world;
    
    private MongoDbPlayerStatisticIoService mongostat;
    private File statsDir = new File(".");
    @Before
    public void before() {
        mongostat = new MongoDbPlayerStatisticIoService(server, statsDir);

        world = PowerMockito.mock(GlowWorld.class);
        
        fakePlayer1 = PowerMockito.mock(GlowPlayer.class);
        UUID uuid = UUID.fromString("334419cc-e197-427a-9414-dd828b401fc4");
        final Location location = new Location(world, 10.5, 20.0, 30.5);
        Mockito.when(fakePlayer1.getName()).thenReturn("player1");
        Mockito.when(fakePlayer1.getLocation()).thenReturn(location);
        Mockito.when(fakePlayer1.getType()).thenReturn(EntityType.PLAYER);
        Mockito.when(fakePlayer1.getUniqueId()).thenReturn(uuid);
        
        // mock statistics
        StatisticMap fakeData = new StatisticMap();
        fakeData.add(Statistic.DAMAGE_TAKEN, 24);
        fakeData.add(Statistic.DAMAGE_DEALT, 70);
        fakeData.add(Statistic.CROUCH_ONE_CM, 91);
        fakeData.add(Statistic.DEATHS, 1);
        Mockito.when(((GlowPlayer) fakePlayer1).getStatisticMap()).thenReturn(fakeData);
        
        // {"stat.damageTaken":24,"stat.damageDealt":70,"stat.crouchOneCm":91,"stat.deaths":1,"stat.walkOneCm":95710,"stat.swimOneCm":2737,"stat.playOneMinute":217346,"stat.mobKills":4,"stat.leaveGame":13,"stat.sneakTime":57}

        server = PowerMockito.mock(GlowServer.class);
        server.createProfile(uuid, "player1");
        
        // fake json database
        JsonPlayerStatisticIoService jp = new JsonPlayerStatisticIoService(server, new File(statsDir, "stats"));
        
        // do not really need following
        Mockito.when(server.getPlayerStatisticIoService()).thenReturn(jp);
                
        // JsonPlayerStatisticIoService jp = (JsonPlayerStatisticIoService) server.getPlayerStatisticIoService();
        jp.writeStatistics((GlowPlayer)fakePlayer1);
    }
    
    @Test
    public void testMongoDbStatistic() {
        mongostat.forklift((GlowPlayer) fakePlayer1);

        mongostat.writeStatistics((GlowPlayer) fakePlayer1);
       
    }
}
