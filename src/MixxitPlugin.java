import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class MixxitPlugin extends Plugin
{
  private String name = "MixxitPlugin";
  private String version = "1.17";

  static final Logger log = Logger.getLogger("Minecraft");
  static MixxitListener listener;

  private String getDateTime()
  {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();
    return dateFormat.format(date);
  }

  public void enable()
  {
    	etc.getInstance().addCommand("/health", "- Shows your current health.");
		etc.getInstance().addCommand("/setdrop", "- Sets if a player drops items on death.");
		etc.getInstance().addCommand("/setdist", "- Sets the distance you can hit.");
		etc.getInstance().addCommand("/setguild", "- Sets a player's guild.");
	    etc.getInstance().addCommand("/createguild", "- Creates a guild <name> <ownerplayername>");
	    etc.getInstance().addCommand("/whoisguild", "- Views <playernames> guild");
	    etc.getInstance().addCommand("/setguildowner", "- Sets a guild owner <playername> <guildid>");
	    etc.getInstance().addCommand("/guilds", "- Lists all guilds");
	    etc.getInstance().addCommand("/guild", "- Shows your guild");
	    etc.getInstance().addCommand("/enablecombatlog", "- Enables your combat log");
	    etc.getInstance().addCommand("/disablecombatlog", "- Disables your combat log");
	    etc.getInstance().addCommand("/compressedcombatlog", "- Compresses your combat messages");
	    etc.getInstance().addCommand("/pvpenable", "- Enable PVP");
	    etc.getInstance().addCommand("/pvpdisable", "- Disable PVP");
	    etc.getInstance().addCommand("/heal", "- Sets you to full health");
	    etc.getInstance().addCommand("/MixxitDebug", "-Debug Information for MixxitPlugin");
	    etc.getInstance().addCommand("/level", "- Shows your level");
	    etc.getInstance().addCommand("/enableboomers", "- Enables boomers");
	    etc.getInstance().addCommand("/disableboomers", "- Disables boomers");
	    etc.getInstance().addCommand("/setfaction", "- Sets your PVP Faction 0=None,1,2");
	    etc.getInstance().addCommand("/setguildspawn", "- Sets guild spawn point to current location (owner only)");
	    etc.getInstance().addCommand("/guildspawn", "- Warps you to the guild spawn point");
	    etc.getInstance().addCommand("/guildlist", "- Lists all players within a <guild>");
	    etc.getInstance().addCommand("/stats", "- Shows your stats");
    listener = new MixxitListener();
    log.info(this.name + " " + this.version + " enabled");
    //System.out.println(getDateTime() + " [INFO] " + this.name + " " + this.version + " enabled");
  }

  public void disable()
  {
	  
	    //etc.getInstance().removeCommand("/createguild");

	    etc.getInstance().removeCommand("/health");
	    etc.getInstance().removeCommand("/setdrop");
	    etc.getInstance().removeCommand("/setdist");
	    etc.getInstance().removeCommand("/setguild");
	    etc.getInstance().removeCommand("/createguild");
	    etc.getInstance().removeCommand("/whoisguild");
	    etc.getInstance().removeCommand("/setguildowner");
	    etc.getInstance().removeCommand("/guilds");
	    etc.getInstance().removeCommand("/guild");
	    etc.getInstance().removeCommand("/enablecombatlog");
	    etc.getInstance().removeCommand("/disablecombatlog");
	    etc.getInstance().removeCommand("/compressedcombatlog");
	    etc.getInstance().removeCommand("/pvpenable");
	    etc.getInstance().removeCommand("/pvpdisable");
	    etc.getInstance().removeCommand("/heal"); 
	    etc.getInstance().removeCommand("/MixxitDebug");
	    etc.getInstance().removeCommand("/level");
	    etc.getInstance().removeCommand("/enableboomers");
	    etc.getInstance().removeCommand("/disableboomers");
	    etc.getInstance().removeCommand("/setfaction");
	    etc.getInstance().removeCommand("/setguildspawn");
	    etc.getInstance().removeCommand("/guildspawn");
	    etc.getInstance().removeCommand("/guildlist");
	    etc.getInstance().removeCommand("/stats");

	    log.info(this.name + " " + this.version + " disabled");
	    //System.out.println(getDateTime() + " [INFO] " + this.name + " " + this.version + " disabled");
  }

  public void initialize()
  {
	  etc.getLoader().addListener(PluginLoader.Hook.ARM_SWING, listener, this, PluginListener.Priority.MEDIUM);
	  etc.getLoader().addListener(PluginLoader.Hook.LOGIN, listener, this, PluginListener.Priority.MEDIUM);
	  etc.getLoader().addListener(PluginLoader.Hook.DISCONNECT, listener, this, PluginListener.Priority.MEDIUM);
	    
	  etc.getLoader().addListener(PluginLoader.Hook.COMMAND, listener, this, PluginListener.Priority.MEDIUM);
	  etc.getLoader().addListener(PluginLoader.Hook.BLOCK_CREATED, listener, this, PluginListener.Priority.MEDIUM);
  }
}