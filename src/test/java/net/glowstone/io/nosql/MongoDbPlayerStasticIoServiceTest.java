package net.glowstone.io.nosql;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
    private Player fakePlayer2;
    private Player fakePlayer3;
    private StatisticMap fakeData1;
    private StatisticMap fakeData2;
    private StatisticMap fakeData3;
    
    private GlowWorld world;
    
    private MongoDbPlayerStatisticIoService mongostat;
    private File statsDir = new File(".");    
    
    private JsonPlayerStatisticIoService jp;
    
    @Before
    public void before() {
        mongostat = new MongoDbPlayerStatisticIoService(server, new File(statsDir, "stats"));

        world = PowerMockito.mock(GlowWorld.class);
        
        fakePlayer1 = PowerMockito.mock(GlowPlayer.class);
        UUID uuid1 = UUID.fromString("334419cc-e197-427a-9414-dd828b401fc4");
        final Location location1 = new Location(world, 10.5, 20.0, 30.5);
        Mockito.when(fakePlayer1.getName()).thenReturn("player1");
        Mockito.when(fakePlayer1.getLocation()).thenReturn(location1);
        Mockito.when(fakePlayer1.getType()).thenReturn(EntityType.PLAYER);
        Mockito.when(fakePlayer1.getUniqueId()).thenReturn(uuid1);          
        fakeData1 = new StatisticMap();
        fakeData1.add(Statistic.DAMAGE_TAKEN, 24);
        fakeData1.add(Statistic.DAMAGE_DEALT, 70);
        fakeData1.add(Statistic.CROUCH_ONE_CM, 91);
        fakeData1.add(Statistic.DEATHS, 1);
        Mockito.when(((GlowPlayer) fakePlayer1).getStatisticMap()).thenReturn(fakeData1);
        
		fakePlayer2 = PowerMockito.mock(GlowPlayer.class);
		UUID uuid2 = UUID.fromString("334419cc-e197-427a-9414-dd828b401fc5");
		final Location location2 = new Location(world, 200.5, 145.0, 332.0);
		Mockito.when(fakePlayer2.getName()).thenReturn("player2");
		Mockito.when(fakePlayer2.getLocation()).thenReturn(location2);
		Mockito.when(fakePlayer2.getType()).thenReturn(EntityType.PLAYER);
		Mockito.when(fakePlayer2.getUniqueId()).thenReturn(uuid2);
		fakeData2 = new StatisticMap();
		fakeData2.add(Statistic.DAMAGE_TAKEN, 12);
		fakeData2.add(Statistic.DAMAGE_DEALT, 89);
		fakeData2.add(Statistic.CROUCH_ONE_CM, 12);
		fakeData2.add(Statistic.DEATHS, 10);
		Mockito.when(((GlowPlayer) fakePlayer2).getStatisticMap()).thenReturn(fakeData2);
		
		fakePlayer3 = PowerMockito.mock(GlowPlayer.class);
		UUID uuid3 = UUID.fromString("334419cc-e197-427a-9414-dd828b401fc6");
		final Location location3 = new Location(world, 126.0, 92.0, 85.5);
		Mockito.when(fakePlayer3.getName()).thenReturn("player3");
		Mockito.when(fakePlayer3.getLocation()).thenReturn(location3);
		Mockito.when(fakePlayer3.getType()).thenReturn(EntityType.PLAYER);
		Mockito.when(fakePlayer3.getUniqueId()).thenReturn(uuid3);
		fakeData3 = new StatisticMap();
		fakeData3.add(Statistic.DAMAGE_TAKEN, 21);
		fakeData3.add(Statistic.DAMAGE_DEALT, 32);
		fakeData3.add(Statistic.CROUCH_ONE_CM, 49);
		fakeData3.add(Statistic.DEATHS, 3);
		Mockito.when(((GlowPlayer) fakePlayer3).getStatisticMap()).thenReturn(fakeData3);
        
        // {"stat.damageTaken":24,"stat.damageDealt":70,"stat.crouchOneCm":91,"stat.deaths":1,"stat.walkOneCm":95710,"stat.swimOneCm":2737,"stat.playOneMinute":217346,"stat.mobKills":4,"stat.leaveGame":13,"stat.sneakTime":57}

        server = PowerMockito.mock(GlowServer.class);
        server.createProfile(uuid2, "player1");
        
        // fake json database
        jp = new JsonPlayerStatisticIoService(server, new File(statsDir, "stats"));
        
        // do not really need following
        Mockito.when(server.getPlayerStatisticIoService()).thenReturn(jp);
                
        // JsonPlayerStatisticIoService jp = (JsonPlayerStatisticIoService) server.getPlayerStatisticIoService();
        jp.writeStatistics((GlowPlayer)fakePlayer1);
    }
    
    @Test
    public void testMongoDbStatistic() {
    	List<GlowPlayer> playerList = new ArrayList<GlowPlayer>();    	
    	playerList.add((GlowPlayer) fakePlayer1);
    	playerList.add((GlowPlayer) fakePlayer2);
    	playerList.add((GlowPlayer) fakePlayer3);
    	
        mongostat.forklift(playerList);

        mongostat.writeStatistics((GlowPlayer) fakePlayer1);
        
        mongostat.readStatistics((GlowPlayer) fakePlayer1);
        
        mongostat.jsonObjsAreEqual((GlowPlayer) fakePlayer1);
        
        mongostat.checkInconsistency((GlowPlayer) fakePlayer1);

        // System.out.println(((GlowPlayer) fakePlayer1).getStatisticMap().getValues());
    }
    
    @Test
    public void testInconsistency() {
        // change something in the data
        fakeData1.set(Statistic.DEATHS, 2);
        // write it out
        jp.writeStatistics((GlowPlayer)fakePlayer1);
        
        int result = mongostat.checkInconsistency((GlowPlayer) fakePlayer1);
        
        assertEquals(1, result);
    }
}
