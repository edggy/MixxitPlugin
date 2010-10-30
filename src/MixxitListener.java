import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.logging.Logger;

public class MixxitListener extends PluginListener
{
  public PropertiesFile properties = new PropertiesFile("MixxitPlugin.properties");
  public PropertiesFile guilds = new PropertiesFile("MixxitPlugin.guilds");
  static final Logger log = Logger.getLogger("Minecraft");
  
  boolean pvp;// = false;
  boolean pvpteams = false;
  boolean drop = false;
  boolean boomers = false;
  public int MaxHP = 200;

  public int totaldmg = 0;
  public int totalplydmg = 0;
  public int countcompress2 = 0;
  public int countcompress3 = 0;
  public int countcompress4 = 0;
  public int countcompress5 = 0;

  int Combattimer = 700;
  int savetimer = 60000;
  
  int woodensword = 6;
  int stonesword = 7;
  int ironsword = 8;
  int goldsword = 10;
  int diamondsword = 20;
  int woodenspade = 4;
  int stonespade = 5;
  int ironspade = 6;
  int goldspade = 8;
  int diamondspade = 10;
  int woodenpickaxe = 4;
  int stonepickaxe = 5;
  int ironpickaxe = 6;
  int goldpickaxe = 8;
  int diamondpickaxe = 10;
  int woodenaxe = 5;
  int stoneaxe = 6;
  int ironaxe = 7;
  int goldaxe = 10;
  int diamondaxe = 18;
  int basedamage = 3;

  int goldenapple = 100;
  int friedbacon = 20;
  int apple = 10;
  int bread = 15;
  public Timer timer;
  public Timer saveTimer;
  public ArrayList<MixxitPlayer> playerList;
  public ArrayList<MixxitGuild> guildList;
  
  public MixxitListener()
  {
    this.timer = new Timer();
    this.saveTimer = new Timer();
    
    this.timer.scheduleAtFixedRate(new RemindTask(this), 0L, this.Combattimer);
    //this.saveTimer.scheduleAtFixedRate(new SaveCombat(this), 0L, this.savetimer);

    
    //System.out.println(getDateTime() + " [INFO] Melee Combat Task Scheduled.");
    log.info("Melee Combat Task Scheduled.");

    this.playerList = new ArrayList();
    this.guildList = new ArrayList<MixxitGuild>();
    loadPlayerList();
    loadGuilds();
    loadProperties();
    this.saveTimer.scheduleAtFixedRate(new SaveCombat(this), 0L, this.savetimer);


  }
  
  public void loadGuilds()
  {
	  this.guilds = new PropertiesFile("MixxitPlugin.guilds");
	    this.guilds.load();
	    
	    	
	    	// max 1000 guilds
	    	for (int i = 1; i < 1001; i++)
	    	{
	    		try
	    	    {
		    		String fileval = this.guilds.getString(Integer.toString(i), "0:Default Guild:Nobody");
		    		
		    		if (fileval.equals("0:Default Guild:Nobody") == true)
		    		{
		    			// skip
		    		} else {
		    			String[] guilddata = fileval.split(":");
		    			
		    			if (Integer.parseInt(guilddata[0]) > 0 && !guilddata[1].equals("") && !guilddata[1].equals("Default Guild") && !guilddata[2].equals("Nobody") && !guilddata[2].equals(""))
		    			{
			    			MixxitGuild newguild = new MixxitGuild();
			    			
			    			newguild.guildid = Integer.parseInt(guilddata[0]);
			    			newguild.name = guilddata[1];
			    			newguild.owner = guilddata[2];
			    			newguild.home = guilddata[3];
			    			System.out.println(getDateTime() + "[DEBUG] Guild Loaded: " + newguild.guildid + ":" + newguild.name);
			    			this.guildList.add(newguild);
		    			}
		    		}
	    	    }
	    		catch (Exception localException)
	    	    {
	    	    }
	    	}
  }
  
  public void loadProperties()
  {
    this.properties = new PropertiesFile("MixxitPlugin.properties");
    this.properties.load();
    try
    {
      this.pvp = this.properties.getBoolean("pvp", true);
      this.pvpteams = this.properties.getBoolean("pvpteams", true);
      this.drop = this.properties.getBoolean("drop-inventory", true);
      this.MaxHP = this.properties.getInt("MaxHP", 200);
      this.Combattimer = this.properties.getInt("combat-timer", 700);
      this.savetimer = this.properties.getInt("savetimer", 120000);
      this.woodensword = this.properties.getInt("wooden-sword", 6);
      this.stonesword = this.properties.getInt("stone-sword", 7);
      this.ironsword = this.properties.getInt("iron-sword", 8);
      this.goldsword = this.properties.getInt("gold-sword", 10);
      this.diamondsword = this.properties.getInt("diamond-sword", 20);
      this.woodenspade = this.properties.getInt("wooden-spade", 4);
      this.stonespade = this.properties.getInt("stone-spade", 5);
      this.ironspade = this.properties.getInt("iron-spade", 6);
      this.goldspade = this.properties.getInt("gold-spade", 8);
      this.diamondspade = this.properties.getInt("diamond-spade", 10);
      this.woodenpickaxe = this.properties.getInt("wooden-pickaxe", 4);
      this.stonepickaxe = this.properties.getInt("stone-pickaxe", 5);
      this.ironpickaxe = this.properties.getInt("iron-pickaxe", 6);
      this.goldpickaxe = this.properties.getInt("gold-pickaxe", 8);
      this.diamondpickaxe = this.properties.getInt("diamond-pickaxe", 10);
      this.woodenaxe = this.properties.getInt("wooden-axe", 5);
      this.stoneaxe = this.properties.getInt("stone-axe", 6);
      this.ironaxe = this.properties.getInt("iron-axe", 7);
      this.goldaxe = this.properties.getInt("gold-axe", 10);
      this.diamondaxe = this.properties.getInt("diamond-axe", 18);
      this.goldenapple = this.properties.getInt("goldenapple", 100);
      this.friedbacon = this.properties.getInt("friedbacon", 20);
      this.apple = this.properties.getInt("apple", 10);
      this.bread = this.properties.getInt("bread", 15);
    }
    catch (Exception localException)
    {
    }
  }

  public void loadPlayerList()
  {
    try
    {
      DataInputStream in = new DataInputStream(new FileInputStream("MixxitUsers.txt"));
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null)
      {
        if (line.substring(0, 1).matches("[#]"))
        {
          System.out.println(getDateTime() + " [DEBUG] Comment Skipped");
        }
        else
        {
          String slashedstring = line.replace("\\:", ":");

          String[] tokens = slashedstring.split("=");

          String[] params = tokens[1].split(":");

          int curhp = 0;
          int curexp = 0;
          int curmelee = 0;
          int curlevel = 0;
          int curfaction = 0;
          int curguild = 0;
          int curstr = 0;
          int cursta = 0;
          int curagi = 0;
          int curdex = 0;
          int curint = 0;
          int curwis = 0;
          int curcha = 0;
          int curlck = 0;
          boolean curdropitems = false;
          int curstat_acc = 0;
          
          try          
          {
        	  curhp = Integer.parseInt(params[0]);
          }
          catch (ArrayIndexOutOfBoundsException e)
          {

          }

          try          
          {
        	  curexp = Integer.parseInt(params[1]);
          }
          catch (ArrayIndexOutOfBoundsException e)
          {

          }

          try          
          {
        	  curmelee = Integer.parseInt(params[2]);
          }
          catch (ArrayIndexOutOfBoundsException e)
          {

          }

          try          
          {
        	  curlevel = Integer.parseInt(params[3]);
          }
          catch (ArrayIndexOutOfBoundsException e)
          {

          }
          
          try          
          {
        	  curfaction = Integer.parseInt(params[4]);
          }
          catch (ArrayIndexOutOfBoundsException e)
          {

          }
          
          try          
          {
        	  curguild = Integer.parseInt(params[5]);
          }
          catch (ArrayIndexOutOfBoundsException e)
          {

          }
          
          try          
          {
        	  curstr = Integer.parseInt(params[6]);
          }
          catch (ArrayIndexOutOfBoundsException e)
          {

          }
          
          try          
          {
        	  cursta = Integer.parseInt(params[7]);
          }
          catch (ArrayIndexOutOfBoundsException e)
          {

          }
          
          try          
          {
        	  curagi = Integer.parseInt(params[8]);
          }
          catch (ArrayIndexOutOfBoundsException e)
          {

          }
          
          try          
          {
        	  curdex = Integer.parseInt(params[9]);
          }
          catch (ArrayIndexOutOfBoundsException e)
          {

          }
          
          try          
          {
        	  curstr = Integer.parseInt(params[10]);
          }
          catch (ArrayIndexOutOfBoundsException e)
          {

          }
          
          try          
          {
        	  curwis = Integer.parseInt(params[11]);
          }
          catch (ArrayIndexOutOfBoundsException e)
          {

          }
          
          try          
          {
        	  curcha = Integer.parseInt(params[12]);
          }
          catch (ArrayIndexOutOfBoundsException e)
          {

          }
          
          try          
          {
        	  curlck = Integer.parseInt(params[13]);
          }
          catch (ArrayIndexOutOfBoundsException e)
          {

          }
          
          try          
          {
        	  curstat_acc = Integer.parseInt(params[14]);
          }
          catch (ArrayIndexOutOfBoundsException e)
          {
              System.out.println(getDateTime() + " [DEBUG] ArrayIndexOutOfBoundsException - " + e.getMessage());

          }
          
          try          
          {
        	  curdropitems = Boolean.parseBoolean(params[15]);
          }
          catch (ArrayIndexOutOfBoundsException e)
          {
              System.out.println(getDateTime() + " [DEBUG] ArrayIndexOutOfBoundsException - " + e.getMessage());

          }
          
          

          MixxitPlayer curplayer = new MixxitPlayer(tokens[0], curhp);
          curplayer.exp = curexp;
          curplayer.melee = curmelee;
          curplayer.level = curlevel;
          curplayer.faction = curfaction;
          curplayer.guild = curguild;
          curplayer.stat_str = curstr;
          curplayer.stat_sta = cursta;
          curplayer.stat_agi = curagi;
          curplayer.stat_dex = curdex;
          curplayer.stat_int = curint;
          curplayer.stat_wis = curwis;
          curplayer.stat_cha = curcha;
          curplayer.stat_lck = curlck;
          curplayer.drop = curdropitems;
          curplayer.stat_acc = curstat_acc;
          
          this.playerList.add(curplayer);
          //System.out.println(getDateTime() + " [DEBUG] new player: " + curplayer.name + " added with: " + curplayer.hp + ":" + curplayer.exp + ":" + curplayer.melee + ":" + curplayer.level + ":" + curplayer.faction + ":" + curplayer.guild + ":" + curplayer.drop + ":" + curplayer.stat_acc);
        }

      }

      in.close();
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void packParameters()
  {
	  System.out.println("Packing Parameters...");
  }

  public void packGuilds()
  {
	  System.out.println("Packing guilds...");
	  PropertiesFile configGuilds = new PropertiesFile("MixxitPlugin.guilds");
	    for (int i = 0; i < this.guildList.size(); i++) {
	      String guildData = ((MixxitGuild)this.guildList.get(i)).guildid + ":" + ((MixxitGuild)this.guildList.get(i)).name + ":" + ((MixxitGuild)this.guildList.get(i)).owner + ":" + ((MixxitGuild)this.guildList.get(i)).home;
	      System.out.println("Packing:" + guildData);
	      configGuilds.setString(Integer.toString(((MixxitGuild)this.guildList.get(i)).guildid), guildData);
	    }
  }
  
  public void packPlayers()
  {
	  System.out.println("Packing players...");
    PropertiesFile configPlayers = new PropertiesFile("MixxitUsers.txt");
    for (int i = 0; i < this.playerList.size(); i++) {
    	String playerData = ((MixxitPlayer)this.playerList.get(i)).hp + ":" + ((MixxitPlayer)this.playerList.get(i)).exp + ":" + ((MixxitPlayer)this.playerList.get(i)).melee + ":" + ((MixxitPlayer)this.playerList.get(i)).level + ":" + ((MixxitPlayer)this.playerList.get(i)).faction+ ":" + ((MixxitPlayer)this.playerList.get(i)).guild+ ":" + ((MixxitPlayer)this.playerList.get(i)).stat_str + ":" + ((MixxitPlayer)this.playerList.get(i)).stat_sta + ":" + ((MixxitPlayer)this.playerList.get(i)).stat_agi + ":" + ((MixxitPlayer)this.playerList.get(i)).stat_dex + ":" + ((MixxitPlayer)this.playerList.get(i)).stat_int + ":" + ((MixxitPlayer)this.playerList.get(i)).stat_wis + ":" + ((MixxitPlayer)this.playerList.get(i)).stat_cha + ":" + ((MixxitPlayer)this.playerList.get(i)).stat_lck + ":" + ((MixxitPlayer)this.playerList.get(i)).stat_acc + ":" + ((MixxitPlayer)this.playerList.get(i)).drop;
        configPlayers.setString(((MixxitPlayer)this.playerList.get(i)).name, playerData);
    }
  }

  private String getDateTime()
  {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();
    return dateFormat.format(date);
  }

  private double getDistance(Player a, Mob b)
  {
    double xPart = Math.pow(a.getX() - b.getX(), 2.0D);
    double yPart = Math.pow(a.getY() - b.getY(), 2.0D);
    double zPart = Math.pow(a.getZ() - b.getZ(), 2.0D);
    return Math.sqrt(xPart + yPart + zPart);
  }

  private double getPlayerDistance(Player a, Player b)
  {
    return getRealDistance(a.getLocation(), b.getLocation());
  }
  
  private double getRealDistance(Location a, Location b)
  {
    double xPart = Math.pow(a.x - b.x, 2.0D);
    double yPart = Math.pow(a.y - b.y, 2.0D);
    double zPart = Math.pow(a.z - b.z, 2.0D);
    return Math.sqrt(xPart + yPart + zPart);
  }
  
  public String  getPlayerName(String name)
  {
	  name = name.toLowerCase();
	  
    for (int i = 0; i < this.playerList.size(); i++) {
     String who = this.playerList.get(i).name.toLowerCase();
     //if (who.contains(name.subSequence(0, name.length())));
     if(who.startsWith(name))
     {
        return this.playerList.get(i).name;
     }
    }
    return null;
  }
  
  public int getPlayerHP(String name)
  {
    for (int i = 0; i < this.playerList.size(); i++) {
      if (this.playerList.get(i).name.equals(name))
      {
        return this.playerList.get(i).hp;
      }
    }
    return 0;
  }
  
  public MixxitPlayer getPlayer(Player player)
  {
    for (int i = 0; i < this.playerList.size(); i++) {
      if (this.playerList.get(i).name.equals(player.getName()))
      {
        return this.playerList.get(i);
      }
    }
    MixxitPlayer other = new MixxitPlayer("other",0);
    return other;
  }
  
  public MixxitPlayer getPlayer(String name)
  {
   name = name.toLowerCase();
   for (int i = 0; i < this.playerList.size(); i++) {
	   String who = playerList.get(i).name.toLowerCase();
	   if(who.startsWith(name))
      {
        return this.playerList.get(i);
      }
    }
    return null;
    //return getPlayer(getPlayerPlayer(name));
  }
  
  public Player getPlayerPlayer(String name)
  {
	  name = name.toLowerCase();
	  for (Player p : etc.getServer().getPlayerList())
	    {
		  String who = p.getName().toLowerCase();
		  if(who.startsWith(name))
	      {
	    	  return p;
	      }
	    }
	  Player other = new Player();
	    return other;
  }
  
  public int getPlayerHP(Player player)
  {
    for (int i = 0; i < this.playerList.size(); i++) {
      if (this.playerList.get(i).name.equals(player.getName()))
      {
        return this.playerList.get(i).hp;
      }
    }
    return 0;
  }

  public void setPlayerHP(Player player, Integer newhp)
  {
    for (int i = 0; i < this.playerList.size(); i++) {
      if (!this.playerList.get(i).name.equals(player.getName()))
        continue;
      int finalhp;
      if (newhp  > MaxHP)
      {
          finalhp = MaxHP;
      } else {
    	  finalhp = newhp;
      }
      this.playerList.get(i).hp = finalhp;
    }
  }
  
  public int getPlayerExperience(Player player)
  {
	  
	  for (int i = 0; i < this.playerList.size(); i++) {
	      if (this.playerList.get(i).name.equals(player.getName()))
	      {
	        return this.playerList.get(i).exp;
	      }
	  }
	    
	  return 0;
	
	  
  }
  
  public void getAllPlayers(Player player)
  {
	  for (Player p : etc.getServer().getPlayerList())
	  {
		  
		  player.sendMessage(p.getName() + " - " + getPlayerGuildName(p) + " " + getFactionName(getPlayerFaction(p)));
	  }
  }
  
  public String getFactionName(int faction)
  {
	  if (faction == 0)
	  {
		  return "Civilian";
	  }
	  if (faction == 1)
	  {
		  return "Villain";		  
	  }
	  if (faction == 2)
	  {
		  return "Hero";
	  }
	  
	  return "";
  }

  public int getPlayerMelee(Player player)
  {
    for (int i = 0; i < this.playerList.size(); i++) {
      if (this.playerList.get(i).name.equals(player.getName()))
      {
        return this.playerList.get(i).melee;
      }
    }
    return 0;
  }
  
  public int getPlayerstat_acc(Player player)
  {
    for (int i = 0; i < this.playerList.size(); i++) {
      if (this.playerList.get(i).name.equals(player.getName()))
      {
        return this.playerList.get(i).stat_acc;
      }
    }
    return 0;
  }
  
  public void DoPanic(Mob m, Player p, int basedamage)
  {
    double dist = getDistance(p, m);
    if (dist <= getPlayer(p).killdist)
    {
      p.sendMessage("Distance check:" + dist);
      Random generator = new Random();
      int index = generator.nextInt(basedamage);
      int thisdmg = index;

      if (getCombatLog(p) == 1)
      {
        p.sendMessage("The " + m.getName() + " hit you back! For " + thisdmg + " damage! (CurrHP: " + p.getHealth() + ")");
      }

      if (p.getHealth() < thisdmg)
      {
        DoPlayerDeath(p);

        p.teleportTo(etc.getServer().getSpawnLocation());
      }
      else {
        p.setHealth(p.getHealth() - thisdmg);
      }
    }
  }
  
  public int getPlayerLevel(Player player)
  {
	  for (int i = 0; i < this.playerList.size(); i++) {
	      if (this.playerList.get(i).name.equals(player.getName()))
	      {
	        return this.playerList.get(i).level;
	      }
	    }
	    return -1;
  }

  public void setPlayerLevel(Player player, int level)
  {
	  for (int i = 0; i < this.playerList.size(); i++) {
	      if (((MixxitPlayer)this.playerList.get(i)).name.equals(player.getName()))
	      {
	        ((MixxitPlayer)this.playerList.get(i)).level = level;
	        player.sendMessage("Congratulations, you reached Level " + level + "!");
	        /*this.playerList.get(i).stat_str++;
	        this.playerList.get(i).stat_sta++;
	        this.playerList.get(i).stat_agi++;
	        this.playerList.get(i).stat_dex++;
	        this.playerList.get(i).stat_int++;
	        this.playerList.get(i).stat_wis++;
	        this.playerList.get(i).stat_lck++;*/

	      }
	    }
  }
  
  public boolean isPlayerPVP(Player player)
  {
	  for (int i = 0; i < this.playerList.size(); i++) {
	      if (this.playerList.get(i).name.equals(player.getName()))
	      {
	        if (this.playerList.get(i).faction > 0)
	        {
	        	return true;
	        }
	      }
	    }
	    return false;
  }

  
  public int getPlayerFaction(Player player)
  {
	  for (int i = 0; i < this.playerList.size(); i++) {
	      if (this.playerList.get(i).name.equals(player.getName()))
	      {
	        return this.playerList.get(i).faction;
	      }
	    }
	    return -1;
  }

  public void setPlayerFaction(Player player, int faction)
  {
	  for (int i = 0; i < this.playerList.size(); i++) {
	      if (this.playerList.get(i).name.equals(player.getName()))
	      {
	    	  	if (faction == 0)
	    	  	{
	    	  		this.playerList.get(i).faction = faction;
		        	player.sendMessage("You have joined the ranks of the Civilians. Phew!");
	    	  	}
	    	  	
	    	  
	    	  	if (faction == 1)
	    	  	{
	    	  		this.playerList.get(i).faction = faction;
		        	player.sendMessage("You have joined the ranks of the Villains!");
	    	  	}

	    	  	if (faction == 2)
	    	  	{
	    	  		this.playerList.get(i).faction = faction;
	        		player.sendMessage("You have joined the ranks of the Heroes!");
	    	  	}
	      }
	    }
  }
  
  public int getMaxBaseHealth(Player player)
  {
	  int bonus = (getPlayer(player).stat_sta + 5) * 5;
	  return bonus;
  }
  
  public int getMaxBaseDamage(Player player)
  {
	  int bonus = (getPlayer(player).stat_str + 5) * 5;
	  return bonus;
  }
  
  public void setGuildHome(int guildid, String guildlocation)
  {
	  for (int i = 0; i < this.guildList.size(); i++) {
		  if (this.guildList.get(i).guildid == guildid)
		  {
			  this.guildList.get(i).home = guildlocation;
		  }
      }
  }
  public String getGuildHome(int guildid)
  {
	  String location = "";
	  for (int i = 0; i < this.guildList.size(); i++) {
		  if (this.guildList.get(i).guildid == guildid)
		  {
			  return this.guildList.get(i).home;
		  }
      }
	  return location;
  }
  
  public void setCombatLog(Player player, int value)
  {
    for (int i = 0; i < this.playerList.size(); i++) {
      if (!this.playerList.get(i).name.equals(player.getName()))
        continue;
      this.playerList.get(i).combatlog = value;
    }
  }

  public int getCombatLog(Player player)
  {
    for (int i = 0; i < this.playerList.size(); i++) {
      if (this.playerList.get(i).name.equals(player.getName()))
      {
        return this.playerList.get(i).combatlog;
      }
    }
    return -1;
  }
  
  public void setGuildOwner(String playername, int guildid)
  {
	  for (int i = 0; i < this.guildList.size(); i++) {
	      if (((MixxitGuild)this.guildList.get(i)).guildid == guildid)
	      {
	        this.guildList.get(i).owner = playername;
	      }
	    }
  }
  
  public void setGuild(Player player, int value)
  {
    for (int i = 0; i < this.playerList.size(); i++) {
      if (!((MixxitPlayer)this.playerList.get(i)).name.equals(player.getName()))
        continue;
      ((MixxitPlayer)this.playerList.get(i)).guild = value;
      player.sendMessage("Your guild (" + value + ") has been set.");
    }
  }
  
  public int getHighestGuildID()
  {
	  int id = 0;
	  for (int i = 0; i < this.guildList.size(); i++) {
		  
		  if (this.guildList.get(i).guildid > id)
		  {
			  id = this.guildList.get(i).guildid;
		  }
	  }
	  return id;	  
  }
  
  public void createGuild(String name, String owner)
  {
	  MixxitGuild newguild = new MixxitGuild();
	  newguild.guildid = getHighestGuildID() + 1;
	  newguild.name = name;
	  newguild.owner = owner;
	  this.guildList.add(newguild);
	  
	  for (Player p : etc.getServer().getPlayerList())
	  {
		  if (p.getName().equals(owner) == true)
		  {
			  setGuild(p,newguild.guildid);
		  }
	  }
	  
  }

  public int getGuild(Player player)
  {
    for (int i = 0; i < this.playerList.size(); i++) {
      if (((MixxitPlayer)this.playerList.get(i)).name.equals(player.getName()))
      {
        return this.playerList.get(i).guild;
      }
    }
    return -1;
  }
  
  public String getPlayerGuildName(Player player)
  {
	  return getGuildName(getGuild(player));
  }
  
  public int getGuildID(String name)
  {
	  for (int i = 0; i < this.guildList.size(); i++) {
		  if (this.guildList.get(i).name.equals(name))
		  {
			  return this.guildList.get(i).guildid;
		  }
	  }
	  
	  return 0;
  }
  
  public String getGuildOwner(String name)
  {
	  for (int i = 0; i < this.guildList.size(); i++) {
		  if (this.guildList.get(i).name.equals(name))
		  {
			  return this.guildList.get(i).owner;
		  }
	  }
	  
	  return "Nobody";
  }
  
  public String getGuildName(int id)
  {
	  for (int i = 0; i < this.guildList.size(); i++) {
		  if (this.guildList.get(i).guildid == id)
		  {
			  return this.guildList.get(i).name;
		  }
	  }
	  
	  return "Unguilded";
  }
  
  public int getPlayerGuildID(String name)
  {
	  for (Player p : etc.getServer().getPlayerList())
	  {
		  if (p.getName().equals(name) == true)
		  {
			  return getGuild(p);
		  }
	  }
	  return 0;
  }
  
  public void setDrop(Player player, boolean value)
  {
    for (int i = 0; i < this.playerList.size(); i++) {
      if (!this.playerList.get(i).name.equals(player.getName()))
        continue;
      this.playerList.get(i).drop = value;
    }
  }

  public boolean getDrop(Player player)
  {
    for (int i = 0; i < this.playerList.size(); i++) {
      if (this.playerList.get(i).name.equals(player.getName()))
      {
        return this.playerList.get(i).drop;
      }
    }
    return false;
  }
  
  public void setDist(Player player, double value)
  {
	 if(value >= 20)
		 value = 20;
	    for (int i = 0; i < this.playerList.size(); i++) {
	      if (!this.playerList.get(i).name.equals(player.getName()))
	        continue;
	      this.playerList.get(i).killdist = value;
	      player.sendMessage("Your killdist (" + value + ") has been set.");
	    }
    }

  public double getDist(Player player)
  {
    for (int i = 0; i < this.playerList.size(); i++) {
      if (this.playerList.get(i).name.equals(player.getName()))
      {
        return this.playerList.get(i).killdist;
      }
    }
    return -1;
  }

  public void enableCombatLog(Player player)
  {
    player.sendMessage("Combat Log Enabled - to disable /disablecombatlog");
    setCombatLog(player, 1);
  }

  public void disableCombatLog(Player player)
  {
    player.sendMessage("Combat Log Disabled - to enable /enablecombatlog");
    setCombatLog(player, 0);
  }

  public void compressedCombatLog(Player player, int base)
  {
    player.sendMessage("Combat Log Compressed");
    setCombatLog(player, base);
  }

  public boolean onCommand(Player player, String[] split)
  {
   if ((split[0].equalsIgnoreCase("/health")) && (player.canUseCommand("/health")))
    {
	   try          
       {
		   if(split[1] != null)
		    	  player.sendMessage(getPlayerName(split[1])+ "'s HP: " + getPlayerHP(split[1]));
       }
       catch (ArrayIndexOutOfBoundsException e)
       {
    	   player.sendMessage("HP: " + getPlayerHP(player));
       }
      return true;
    }
    
    if ((split[0].equalsIgnoreCase("/setdrop")) && (player.canUseCommand("/setdrop")))
    {
    	try          
        {
    		  String name = split[2];
    		  //player.sendMessage(name);
    	      Boolean drop = Boolean.parseBoolean(split[1]);
    	      //player.sendMessage(name+":"+drop);
    	      getPlayer(name).drop = drop;
    	      player.sendMessage(getPlayerName(split[2]) + "'s drop (" + split[1] + ") has been set.");
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
        	getPlayer(player).drop = Boolean.parseBoolean(split[1]);
        	player.sendMessage("Your drop (" + split[1] + ") has been set.");
  	      
        }
       return true;
      
    }
    if ((split[0].equalsIgnoreCase("/setdist")) && (player.canUseCommand("/setdist")))
    {
      setDist(player, Double.parseDouble(split[1]));
      return true;
    }
    /*if ((split[0].equalsIgnoreCase("/gotomob")) && (player.canUseCommand("/gotomob")))
    {
    	Mob mtemp = etc.getServer().getMobList().get(0);// = new Mob("none", player.getLocation());
    	int dist = 9999;
    	for (Mob m : etc.getServer().getMobList())
        {
    		if(getDistance(player, m) < dist && m.getY() >= player.getY() - 5)
    			mtemp = m;
        }
    	if(mtemp != null)
    	{
    		
    		player.teleportTo(mtemp);
    	}
      return true;
    }*/
    if ((split[0].equalsIgnoreCase("/enablecombatlog")) && (player.canUseCommand("/enablecombatlog")))
    {
      enableCombatLog(player);
      return true;
    }
    if ((split[0].equalsIgnoreCase("/setguild")) && (player.canUseCommand("/setguild")))
    {
    	try
    	{
    	if (split[1].equals("") == true)
    	{
    		player.sendMessage("Syntax: <playername> <guildid>");
    	} else {
    		setGuild(getPlayerPlayer(split[1]), Integer.parseInt(split[2]));
    		player.sendMessage("Player set to guild.");
    	}
    	}
    	catch (ArrayIndexOutOfBoundsException e)
    	{
    		player.sendMessage("Syntax: <playername> <guildid>");
    	}
      return true;
    }
    
    if ((split[0].equalsIgnoreCase("/createguild")) && (player.canUseCommand("/createguild")))
    {
    	try
    	{
    	if (split[1].equals("") == true)
    	{
    		player.sendMessage("Syntax: <playername> <guildname>");
    	} else {
    		if (split[2].equals("") == true)
    		{
    			player.sendMessage("Syntax: <playername> <guildname>");
    		} else {
    			createGuild(split[2],split[1]);
    		}
    	}
    	}
    	catch (ArrayIndexOutOfBoundsException e)
    	{
    		player.sendMessage("Syntax: <playername> <guildid>");
    	}
      return true;
    }
    
    if ((split[0].equalsIgnoreCase("/whoisguild")) && (player.canUseCommand("/whoisguild")))
    {
      player.sendMessage(getGuildName(getPlayerGuildID(split[1])) + " Player Guild ID: "+ getPlayerGuildID(split[1]));
      return true;
    }
    
    if ((split[0].equalsIgnoreCase("/setguildowner")) && (player.canUseCommand("/setguildowner")))
    {
      setGuildOwner(split[1], Integer.parseInt(split[2]));
      return true;
    }
    
    if ((split[0].equalsIgnoreCase("/guilds")) && (player.canUseCommand("/guilds")))
    {
      
      for (int i = 0; i < this.guildList.size(); i++) {
    	  int guildid = this.guildList.get(i).guildid;
    	  String guildname = this.guildList.get(i).name;
    	  player.sendMessage(guildid + " " + guildname);
      }
      return true;
    }
    
    if ((split[0].equalsIgnoreCase("/guild")) && (player.canUseCommand("/guild")))
    {
      player.sendMessage("You are in guild: " + getPlayerGuildName(player));
      return true;
    }
    
    if ((split[0].equalsIgnoreCase("/disablecombatlog")) && (player.canUseCommand("/disablecombatlog")))
    {
      disableCombatLog(player);
      return true;
    }
    if ((split[0].equalsIgnoreCase("/compresscombatlog")) && (player.canUseCommand("/compresscombatlog")))
    {
      compressedCombatLog(player,Integer.parseInt(split[1]));
      return true;
    }

    if ((split[0].equalsIgnoreCase("/pvpenable")) && (player.canUseCommand("/pvpenable")))
    {
      this.pvp = true;
      player.sendMessage("PVP Free-for-all Enabled");
      return true;
    }

    if ((split[0].equalsIgnoreCase("/pvpdisable")) && (player.canUseCommand("/pvpdisable")))
    {
      this.pvp = false;
      player.sendMessage("PVP Disabled");
      return true;
    }

    if ((split[0].equalsIgnoreCase("/heal")) && (player.canUseCommand("/heal")))
    {
      setPlayerHP(player, Integer.valueOf(getMaxBaseHealth(player)));
      player.sendMessage("You have been fully healed. HP:" + getPlayerHP(player));
      return true;
    }

    if ((split[0].equalsIgnoreCase("/MixxitDebug")) && (player.canUseCommand("/MixxitDebug")))
    {
      player.sendMessage("You have been fully healed. HP: " + getPlayerHP(player));

      player.sendMessage(" [DEBUG] MixxitPlugin - Properties Loader: pvp = " + this.pvp);
      player.sendMessage(" [DEBUG] MixxitPlugin - Properties Loader: drop inventory = " + this.drop);
      player.sendMessage(" [DEBUG] MixxitPlugin - Properties Loader: combat timer = " + this.Combattimer);

      return true;
    }

    if ((split[0].equalsIgnoreCase("/level")) && (player.canUseCommand("/level")))
    {
      player.sendMessage("Level: " + this.getPlayerLevel(player));

      return true;
    }
    
    if ((split[0].equalsIgnoreCase("/enableboomers")) && (player.canUseCommand("/enableboomers")))
    {
      player.sendMessage("Boomers enabled.");
      this.boomers = true;
      return true;
    }
    if ((split[0].equalsIgnoreCase("/disableboomers")) && (player.canUseCommand("/disableboomers")))
    {
      player.sendMessage("Boomers disabled.");
      this.boomers = false;
      return true;
    }

 
    if ((split[0].equalsIgnoreCase("/setfaction")) && (player.canUseCommand("/setfaction")))
    {
    	String[] groups = { "default" };
    	
    	groups[0] = "default";
    	if (split[1].equals(Integer.toString(1)))
    	groups[0] = "evil";
    	if (split[1].equals(Integer.toString(2)))
    	groups[0] = "good";
    	
    	player.setGroups(groups);
    	setPlayerFaction(player, Integer.parseInt(split[1]));
      return true;
    }
    
    if ((split[0].equalsIgnoreCase("/setguildspawn")) && (player.canUseCommand("/setguildspawn")))
    {
    	player.sendMessage(getGuildOwner(getGuildName(getPlayerGuildID(player.getName()))));
    	if (getGuildOwner(getGuildName(getPlayerGuildID(player.getName()))).equals(player.getName()) == true)
    	{
    		setGuildHome(getPlayerGuildID(player.getName()),player.getX() + "#"+player.getY() + "#"+ player.getZ());
    		player.sendMessage(getPlayerGuildID(player.getName())+ " Guild home set at your location " + player.getX() + ":"+player.getY() + ":"+ player.getZ());
    	}
    	
    	
    	
      return true;
    }
    
    if ((split[0].equalsIgnoreCase("/guildspawn")) && (player.canUseCommand("/guildspawn")))
    {
    	for (int i = 0; i < this.guildList.size(); i++) {
  	      String guildData = ((MixxitGuild)this.guildList.get(i)).home;
      	player.sendMessage(getPlayerGuildName(player) + " Home: " + guildData);
      	if (!getGuildHome(getPlayerGuildID(player.getName())).equals(""))
    	{
      		String[] guildspawn = getGuildHome(getPlayerGuildID(player.getName())).split("[#]");
      		Location guildloc = new Location();
      		guildloc.x = Double.parseDouble(guildspawn[0]);
      		guildloc.y = Double.parseDouble(guildspawn[1]);
      		guildloc.z = Double.parseDouble(guildspawn[2]);
      		player.teleportTo(guildloc);
    	}

    	}

    	
    	
      return true;
    }
    
    if ((split[0].equalsIgnoreCase("/guildlist")) && (player.canUseCommand("/guildlist")))
    {
    	try
    	{
    		if (split[1].equals("") == true)
        	{
        		player.sendMessage("Syntax /guildlist <guildname>");
        	} else {
        		for (Player p : etc.getServer().getPlayerList())
        		  {
        			if (getGuildName(getGuild(p)).equals(split[1]))
        			{
             			  player.sendMessage(p.getName() + " - " + getPlayerGuildName(p) + " " + getFactionName(getPlayerFaction(p)));
             			        				
        			}
        		  }
        	}
    	}
    	catch (ArrayIndexOutOfBoundsException e)
    	{
    		player.sendMessage("Syntax /guildlist <guildname>");
    	}
      return true;
    }
 
    if ((split[0].equalsIgnoreCase("/setfaction")) && (player.canUseCommand("/setfaction")))
    {
    	setPlayerFaction(player, Integer.parseInt(split[1]));
      return true;
    }
    
    if ((split[0].equalsIgnoreCase("/stats")) && (player.canUseCommand("/stats")))
    {
    	for (int i = 0; i < this.playerList.size(); i++) {
    	      if (!((MixxitPlayer)this.playerList.get(i)).name.equals(player.getName()))
    	        continue;
    	      player.sendMessage("STR: " + Integer.toString(((MixxitPlayer)this.playerList.get(i)).stat_str));
    	      player.sendMessage("STA: " + Integer.toString(((MixxitPlayer)this.playerList.get(i)).stat_sta));
    	      player.sendMessage("AGI: " + Integer.toString(((MixxitPlayer)this.playerList.get(i)).stat_agi));
    	      player.sendMessage("DEX: " + Integer.toString(((MixxitPlayer)this.playerList.get(i)).stat_dex));
    	      player.sendMessage("INT: " + Integer.toString(((MixxitPlayer)this.playerList.get(i)).stat_int));
    	      player.sendMessage("WIS: " + Integer.toString(((MixxitPlayer)this.playerList.get(i)).stat_wis));
    	      player.sendMessage("CHA: " + Integer.toString(((MixxitPlayer)this.playerList.get(i)).stat_cha));
    	      player.sendMessage("LCK: " + Integer.toString(((MixxitPlayer)this.playerList.get(i)).stat_lck));
    	      player.sendMessage("ACC: " + Integer.toString(((MixxitPlayer)this.playerList.get(i)).stat_acc));
    	      player.sendMessage("Drop: " + Boolean.toString(((MixxitPlayer)this.playerList.get(i)).drop));

    	}
    	return true;
    }
    
    return false;
  }
  

  public void onLogin(Player player)
  {

    int exists = 0;

    for (int i = 0; i < this.playerList.size(); i++) {
      if (!this.playerList.get(i).name.equals(player.getName()))
        continue;
      exists = 1;
      player.sendMessage("Welcome back! HP: " + getPlayerHP(player));
      player.sendMessage("PVP MODE: "+ this.pvp + " PVP Teams: " + this.pvpteams);
      player.sendMessage("Boomers: "+ this.boomers);
      //System.out.println(getPlayerHP(player)+":"+getPlayerExperience(player));
    }

    if (exists == 0)
    {
      MixxitPlayer play = new MixxitPlayer(player.getName(), 25);
      play.faction = 0;
      this.playerList.add(play);
      player.sendMessage("Welcome, you have been registered by the hp system! HP: " + getPlayerHP(player)+ "/" + getMaxBaseHealth(player) );
    }
  }

  public void GiveExperience(Player player, int amount)
  {
    player.sendMessage("Pending experience...");

    for (int i = 0; i < this.playerList.size(); i++) {
      if (!this.playerList.get(i).name.equals(player.getName())) {
        continue;
      }
      this.playerList.get(i).exp += amount;
      int exp = this.playerList.get(i).exp;
      int melee = this.playerList.get(i).melee;
      player.sendMessage("§eYou gain experience (" + exp + ")!");
      Random generator = new Random();
      int gen = 2 * melee - exp / 10 + 1;
      if(gen < 1) gen = 1;
      int index = generator.nextInt(gen);

      if (index != 0)
        continue;
      this.playerList.get(i).melee += 1;
      player.sendMessage("§9You get better at melee! (" + melee + ")!");
      
      /*for (int ilevel = 1; ilevel < 41; ilevel++)
      {
    	  if (exp == (i * 10) * i)
    	  {
    		  setPlayerLevel(player, ilevel);
    	  }
      }*/
    }
  }

  public void GiveStat_acc(Player player)
  {

    for (int i = 0; i < this.playerList.size(); i++) {
      if (!this.playerList.get(i).name.equals(player.getName())) {
        continue;
      }
     
      int exp = this.playerList.get(i).exp;
      int stat_acc = this.playerList.get(i).stat_acc;
      Random generator = new Random();
      int gen = 3 * stat_acc - exp / 8;
      if(gen < 1) gen = 1;
      int index = generator.nextInt(gen);

      if (index != 0)
        continue;
      this.playerList.get(i).stat_acc += 1;
      player.sendMessage("§9You get better at stat_acc! (" + stat_acc + ")!");
    }
  }
  
  public void GiveStat_sta(Player player)
  {

    for (int i = 0; i < this.playerList.size(); i++) {
      if (!this.playerList.get(i).name.equals(player.getName())) {
        continue;
      }
     
      int exp = this.playerList.get(i).exp;
      int stat_sta = this.playerList.get(i).stat_sta;
      Random generator = new Random();
      int gen = stat_sta - exp / 8;
      if(gen < 1) gen = 1;
      int index = generator.nextInt(gen);

      if (index != 0)
        continue;
      this.playerList.get(i).stat_sta += 1;
      player.sendMessage("§9You increased your stat_sta! (" + stat_sta + ")!");
    }
  }
  
  public int PlayerHasHit(Player player)
  {
	GiveStat_acc(player);
    int stat_acc = getPlayerstat_acc(player);
    Random generator = new Random();
    int index = generator.nextInt(125);
    if (index + stat_acc >= 100)
    {
      return 1;
    }
    return 0;
  }

  public void DropPlayerItems(Player player)
  {
    for (int slot = 0; slot < 36; slot++)
    {
      try
      {
        Item item = player.getInventory().getItemFromSlot(slot);
        int itemid = item.getItemId();
        int amount = item.getAmount();

        player.giveItemDrop(itemid, amount);
      }
      catch (NullPointerException localNullPointerException)
      {
      }

      player.getInventory().removeItem(slot);
    }

    player.getInventory().updateInventory();
  }

  public void DoPlayerDeath(Player player)
  {
    player.sendMessage("You have been killed");
    //player.sendMessage("Current Health: " + this.getPlayerHP(player));
    if (getPlayer(player).drop)
    {
      DropPlayerItems(player);
    }
    setPlayerHP(player, Integer.valueOf(getMaxBaseHealth(player)));
    player.teleportTo(etc.getServer().getSpawnLocation());
    //setPlayerHP(player, Integer.valueOf(100));
  }

  public String getItemName(int itemId)
  {
    String itemname = "fashioned weapon";

    if (itemId == 268)
    {
      itemname = "Wooden Sword";
    }

    if (itemId == 272)
    {
      itemname = "Stone Sword";
    }

    if (itemId == 267)
    {
      itemname = "Iron Sword";
    }

    if (itemId == 283)
    {
      itemname = "Gold Sword";
    }

    if (itemId == 276)
    {
      itemname = "Diamond Sword";
    }

    return itemname;
  }

  public int getItemDamage(int itemId)
  {
    int itembasedamage = this.basedamage;

    if (itemId == 268)
    {
      itembasedamage = this.woodensword;
    }
    if (itemId == 269)
    {
      itembasedamage = this.woodenspade;
    }
    if (itemId == 270)
    {
      itembasedamage = this.woodenpickaxe;
    }
    if (itemId == 271)
    {
      itembasedamage = this.woodenaxe;
    }

    if (itemId == 272)
    {
      itembasedamage = this.stonesword;
    }
    if (itemId == 273)
    {
      itembasedamage = this.stonespade;
    }
    if (itemId == 274)
    {
      itembasedamage = this.stonepickaxe;
    }
    if (itemId == 275)
    {
      itembasedamage = this.stoneaxe;
    }

    if (itemId == 276)
    {
      itembasedamage = this.diamondsword;
    }
    if (itemId == 277)
    {
      itembasedamage = this.diamondspade;
    }
    if (itemId == 278)
    {
      itembasedamage = this.diamondpickaxe;
    }
    if (itemId == 279)
    {
      itembasedamage = this.diamondaxe;
    }

    if (itemId == 267)
    {
      itembasedamage = this.ironsword;
    }
    if (itemId == 256)
    {
      itembasedamage = this.ironspade;
    }
    if (itemId == 257)
    {
      itembasedamage = this.ironpickaxe;
    }
    if (itemId == 258)
    {
      itembasedamage = this.ironaxe;
    }

    if (itemId == 283)
    {
      itembasedamage = this.goldsword;
    }
    if (itemId == 284)
    {
      itembasedamage = this.goldspade;
    }
    if (itemId == 285)
    {
      itembasedamage = this.goldpickaxe;
    }
    if (itemId == 286)
    {
      itembasedamage = this.goldaxe;
    }
    return itembasedamage;
  }

  public int getPlayerDamage(Player player)
  {
    int itemId = player.getItemInHand();

    int damage = getItemDamage(itemId);

    damage += getPlayerMelee(player);

    Random generator = new Random();
    int index = generator.nextInt(damage);

    index++;

    return index;
  }
  
  public void onDisconnect(Player player)
  {
	  	SaveCombat saver = new SaveCombat(this);
	  	//saver.run();
	  	System.out.println(getDateTime() + " [INFO] MixxitPlugin - Saving");
        this.packPlayers();
        this.packGuilds();
	  	//System.out.println(player.getName() + " quit")
	  	
  }
  
  public long getPlayerLastMove(Player player)
  {
	  long lastmove = 0;
	  for (int i = 0; i < this.playerList.size(); i++) {
	      if (((MixxitPlayer)this.playerList.get(i)).name.equals(player.getName()))
	      {
	        return ((MixxitPlayer)this.playerList.get(i)).lastmove;
	      }
	  }
	  return lastmove;
  }


  public long setPlayerLastMove(Player player)
  {
	  long lastmove = 0;
	  for (int i = 0; i < this.playerList.size(); i++) {
	      if (this.playerList.get(i).name.equals(player.getName()))
	      {
	        return this.playerList.get(i).lastmove = System.currentTimeMillis()/1000;
	      }
	  }
	  return lastmove;
  }

  public void onArmSwing(Player player)
  {
	  // this prevents people from spamming attack too fast (defaults to combattimer (700))
	if ((System.currentTimeMillis()/1000 - this.Combattimer/1000) <= getPlayerLastMove(player))
	{
		//player.sendMessage("You must wait before making another attack." + (System.currentTimeMillis()/1000 - this.Combattimer/1000) + "<=" + getPlayerLastMove(player));
		return;
	}
		
	//long lastmove = 0; // This was local?
	// records time of current armswing
	for (int i = 0; i < this.playerList.size(); i++) {
	      if (((MixxitPlayer)this.playerList.get(i)).name.equals(player.getName()))
	      {
	        ((MixxitPlayer)this.playerList.get(i)).lastmove = System.currentTimeMillis()/1000;
	        //player.sendMessage("lastmove set:" + getPlayerLastMove(player));
			
	        //lastmove = ((MixxitPlayer)this.playerList.get(i)).lastmove;
	      }
	}
	
    int iteminhand = player.getItemInHand();
    Inventory inv;
    Random r = new Random();
    int rand[] = new int[5];
    rand[0] = r.nextInt(6) - 1;
    rand[1] = r.nextInt(6) - 1;
    rand[2] = r.nextInt(6) - 1;
    rand[3] = r.nextInt(6) - 1;
    rand[4] = r.nextInt(6) - 1;
    
    if (((iteminhand == 297) || (iteminhand == 260) || (iteminhand == 320) || (iteminhand == 322)) && getPlayerHP(player) < MaxHP)
    {
      String item = "";

      if (iteminhand == 322)
      {
        setPlayerHP(player, Integer.valueOf(getPlayerHP(player) + goldenapple));
        item = "Golden Apple";
      }
      if (iteminhand == 320)
      {
        setPlayerHP(player, Integer.valueOf(getPlayerHP(player) + friedbacon));
        item = "fried bacon";
      }

      if (iteminhand == 260)
      {
        setPlayerHP(player, Integer.valueOf(getPlayerHP(player) + apple));
        item = "apple";
      }

      if (iteminhand == 297)
      {
        setPlayerHP(player, Integer.valueOf(getPlayerHP(player) + bread));
        item = "bread";
      }

      //if (getPlayerHP(player) < 100)
      //{
        player.sendMessage("The " + item + " heals you to " + getPlayerHP(player) + ".");
      /*} else {
        player.sendMessage("The " + item + " heals you to full health.");
        setPlayerHP(player, Integer.valueOf(100));
      }*/

      inv = player.getInventory();
      inv.removeItem(new Item(iteminhand, 1));
      inv.updateInventory();
    }
    else if (((iteminhand == 297) || (iteminhand == 260) || (iteminhand == 320) || (iteminhand == 322)) && getPlayerHP(player) >= MaxHP)
    {
    	player.sendMessage("You are full");
    }

    for (Player p : etc.getServer().getPlayerList())
    {
      if ((p == null) || 
        (p.getName() == player.getName())) {
        continue;
      }
      if (!this.pvp && !this.pvpteams)
        continue;
     
      if (pvpteams)
      {
    	  if (isPlayerPVP(p) == false)
    		  continue;
    	  if (isPlayerPVP(player) == false)
    		  continue;
    	  if (isPlayerPVP(player) == true && isPlayerPVP(p) == true)
    	  {
    		  if (getPlayerFaction(player) == getPlayerFaction(p))
    		  {
    			  continue;
    		  }
    	  } else {
    		  continue;
    	  }
      }
      
      double dist = getPlayerDistance(player, p);
      if (dist >= getPlayer(p).killdist)
        continue;
      if (PlayerHasHit(player) == 0)
      {
        if (getPlayerHP(p) < 1)
        {
          continue;
        }
        if (getCombatLog(player) != 1)
          continue;
        player.sendMessage("§7You try to strike " + p.getName() + " HP: (" + getPlayerHP(p) + ") but miss! Your HP: " + getPlayerHP(player));
      }
      else
      {
        int thisdmg = getPlayerDamage(player);
        this.totaldmg += thisdmg;
       //this.setPlayerHP(p,getPlayerHP(p) - thisdmg);
        if (getCombatLog(player) == 1)
        {
          player.sendMessage("You strike " + p.getName() + " for " + thisdmg + " damage. Your HP: " + getPlayerHP(player) + " Their HP: " + getPlayerHP(p));
        }
        if (getCombatLog(player) == 2)
        {
          player.sendMessage("You strike " + p.getName() + " for " + thisdmg + " damage. Your HP: " + getPlayerHP(player) + " Their HP: " + getPlayerHP(p));
        }
        else if (getCombatLog(player) >= 3)
        {
          if (this.countcompress4 == getCombatLog(player) + rand[0])
          {
            player.sendMessage("Total damage done " + this.totaldmg + ". Current Health: " + getPlayerHP(player) + ".");
            this.countcompress4 = 0;
            this.totaldmg = 0;
            rand[0] = r.nextInt(6) - 1;
          } else {
            this.countcompress4 += 1;
          

        }
        }

        if (getPlayerHP(p) <= thisdmg)
        {
          player.sendMessage("You have killed " + p.getName() + "!");
          p.sendMessage("§cYou have been killed by " + player.getName() + "!");
          GiveExperience(player, 2);
          System.out.println(getDateTime() + " [INFO] MixxitPlugin - Saving");
          this.packPlayers();

          DoPlayerDeath(p);
        } else {
          setPlayerHP(p, Integer.valueOf(getPlayerHP(p) - thisdmg));
          GiveStat_sta(p);
          this.totalplydmg += thisdmg;
          if (getCombatLog(p) == 1)
          {
            p.sendMessage("§cYou have been hit by " + player.getName() + " for " + thisdmg + " damage. /nYour HP: " + getPlayerHP(p) + " Their HP: " + getPlayerHP(player));
          }
          if (getCombatLog(p) == 2)
          {
            p.sendMessage("§cYou have been hit by " + player.getName() + " for " + thisdmg + " damage. /nYour HP: " + getPlayerHP(p) + " Their HP: " + getPlayerHP(player));
          }
          else if (getCombatLog(p) < 3)
              continue;
            if (this.countcompress5 == getCombatLog(player) + rand[1])
            {
              p.sendMessage("Total damage recieved " + this.totalplydmg + " from " + player.getName() + ". Current Health: " + getPlayerHP(p) + ".");
              this.countcompress5 = 0;
              this.totalplydmg = 0;
              rand[1] = r.nextInt(6) - 1;
            } else {
              this.countcompress5 += 1;
            }

          

        }

      }

    }

    for (Mob m : etc.getServer().getMobList())
    {
      if (m != null) {
        double dist1 = getDistance(player, m);

        if (dist1 >= getPlayer(player).killdist)
          continue;
        if (PlayerHasHit(player) == 0)
        {
          if (m.getHealth() < 1)
          {
            continue;
          }

          if (getCombatLog(player) == 1)
          {
            player.sendMessage("§7You try to strike a " + m.getName() + " HP: (" + m.getHealth() + ") but miss! Your HP: " + getPlayerHP(player));
          }
          else
          {
            if (getCombatLog(player) < 3)
              continue;
            if (this.countcompress3 == getCombatLog(player) + rand[2])
            {
              player.sendMessage("You have missed " + this.countcompress3 + " times. Current Health: " + getPlayerHP(player) + ".");
              this.countcompress3 = 0;
              rand[2] = r.nextInt(6) - 1;
            }
            else {
              this.countcompress3 += 1;
            }

          }

        }
        else
        {
          if (m.getHealth() < 1)
          {
            continue;
          }

          int thisdmg = getPlayerDamage(player);

          this.totaldmg += thisdmg;

          if (getCombatLog(player) == 1)
          {
            player.sendMessage("You strike " + m.getName() + " HP: (" + m.getHealth() + ") for " + thisdmg + " damage. Your HP: " + getPlayerHP(player));
          }
          if (getCombatLog(player) == 2)
          {
            player.sendMessage("You strike " + m.getName() + " HP: (" + m.getHealth() + ") for " + thisdmg + " damage. Your HP: " + getPlayerHP(player));
          }
          else if (getCombatLog(player) >= 3)
          {
            if (this.countcompress2 == getCombatLog(player) + rand[3])
            {
              player.sendMessage("Total damage done " + this.totaldmg + ". Current Health: " + getPlayerHP(player) + ".");
              this.countcompress2 = 0;
              this.totaldmg = 0;
              rand[3] = r.nextInt(6) - 1;
            } else {
              this.countcompress2 += 1;
            }

          }

          if (m.getHealth() <= thisdmg)
          {
            player.sendMessage("You have killed a " + m.getName() + "!");
            m.setHealth(0);
            GiveExperience(player, 1);
            System.out.println(getDateTime() + " [INFO] MixxitPlugin - Saving");
            this.packPlayers();
          } else {
            m.setHealth(m.getHealth() - thisdmg);
          }
        }
      }
    }
  }

 /* public class p1
  {
    public String name;
    public int hp;
    public int exp = 0;
    public int melee = 0;
    public int stat_acc = 0;
    public int level = 0;
    public int faction = 1;
    public int combatlog = 1;
    public int guild = 0;
    public double killdist = 2.0;
    boolean drop = false;
    public p1(String name, int hp)
    {
      this.name = name;
      this.hp = hp;
    }
  }*/
}