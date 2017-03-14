package me.zachbears27;
import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
 
public class ChatManager extends JavaPlugin implements Listener { 
	public static ChatManager plugin;
    public File getPlayerFile(UUID playerName) {

        File playerFile =  new File (this.getDataFolder() + File.separator + "PlayerData", playerName + ".yml");
        return playerFile;
    }

	List<String> bannedFromChat = new ArrayList<String>();
	
    private int chatDelay;
    private String warnMessage;
    private boolean chat;

    private HashMap<UUID, Long> delayMap = new HashMap<>();
	
		public void onDisable() {
			log.sendMessage(intro + ChatColor.RED + "Plugin Has Been Disabled.");
                log.sendMessage(intro + ChatColor.YELLOW + "Plugin By:" + ChatColor.GOLD + " A_Brave_Panda");
                log2.warning(warnMessage);
        }
		
        public void onEnable() {
        	
        	log.sendMessage(intro + ChatColor.GREEN + "Plugin Has Been Enabled.");
        	log.sendMessage(intro + ChatColor.YELLOW + "Plugin By:" + ChatColor.GOLD + " A_Brave_Panda");
            registerConfig();
            getServer().getPluginManager().registerEvents(this, this);
            chatDelay = getConfig().getInt("chatDelay", 2500);
            warnMessage = getConfig().getString("warnMessage", null);
            enabled = true;
            try {
                Metrics metrics = new Metrics(this);
                metrics.start();
                log.sendMessage(intro + ChatColor.GREEN + "Metrics Loaded!");
            } catch (IOException e) {
               log.sendMessage(intro + ChatColor.RED + "Metrics Failed To Load");
            }
            
           
            
            
            
    }
        
        public void registerConfig() {
        	saveDefaultConfig();
        	
    		
    	}
        

       
       
      	//Intro
      	String intro = ChatColor.translateAlternateColorCodes('&',getConfig().getString("Prefix") + " ");
      	Logger log2 = Bukkit.getLogger();
      	ConsoleCommandSender log = this.getServer().getConsoleSender();
    	String version = this.getConfig().getString("Version");
    	boolean enabled;
    	
      	boolean console = getConfig().getBoolean("ConsoleMessagesEnabled");
      	
    	String Helphelp = ChatColor.AQUA + "/chatmanager help" + ChatColor.GOLD + " Show help menu." ;
    	String HelpVersion = ChatColor.AQUA + "/chatmanager version" + ChatColor.GOLD + " Show plugin version.";
    	String HelpMenu = ChatColor.AQUA + "/chatmanager menu" + ChatColor.GOLD + " Show chatmanager GUI."  ;
    	String HelpInfo = ChatColor.AQUA + "/chatmanager info" + ChatColor.GOLD + " Learn more about chatmanager" ;
    	String HelpLeave = ChatColor.AQUA + "/chatmanager leave" + ChatColor.GOLD + " Fake Leave."  ;
    	String HelpJoin = ChatColor.AQUA + "/chatmanager join" + ChatColor.GOLD + " Fake Join."  ;
    	String HelpCC = ChatColor.AQUA + "/cc" + ChatColor.GOLD + " Clear global chat."  ;
    	String HelpPCC = ChatColor.AQUA  + "/pcc [player]" + ChatColor.GOLD + " Clear a player's chat."  ;
    	String HelpSCC = ChatColor.AQUA + "/scc" + ChatColor.GOLD + " Clear your own chat."  ;
    	String HelpOn = ChatColor.AQUA + "/chat on" + ChatColor.GOLD + " Enable chat."  ;
    	String HelpOff = ChatColor.AQUA + "/chat off" + ChatColor.GOLD + " Disable chat."  ;
    	String HelpBC = ChatColor.AQUA + "/broadcast [msg]" + ChatColor.GOLD + " Broadcast a message."  ;
    	String HelpNBC = ChatColor.AQUA + "/namebroadcast [msg]" + ChatColor.GOLD + " Broadcast a message."  ;
    	String HelpMute = ChatColor.AQUA + "/mute [player]" + ChatColor.GOLD + " Mute someone."  ;
    	String HelpUnmute = ChatColor.AQUA + "/unmute [player]" + ChatColor.GOLD + " Unmute someone."  ;
    	

    	public String message(String[] args) {
    		StringBuilder builder = new StringBuilder();
    		for (int i = 0; i < args.length; i++) {
    		builder.append(args[i]);
    		builder.append(" ");
    		}
    		return builder.toString().trim();
    	}
        
		
		public int i = 50;
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
          	Player p = (Player)sender;
          
          	if (cmd.getName().equalsIgnoreCase("msg")) {
          	    if (args.length < 2) {
          	        sender.sendMessage(intro + ChatColor.RED + "Usage: /msg <player> <msg>");
          	        return true;
          	    } else if (args.length >= 2) {
          	        Player target = Bukkit.getPlayer(args[0]);
          	        String message = StringUtils.join(args, ' ', 1, args.length);
          	        if (target != null) {
          	        	
          	        	String msgformatto = ChatColor.translateAlternateColorCodes('&',getConfig().getString("MessageFormatTo")
          	    				.replace("{fromname}", sender.getName()).replace("{toname}", target.getName()).replace("{message}", message));
          	        	String msgformatfrom = ChatColor.translateAlternateColorCodes('&',getConfig().getString("MessageFormatFrom")
          	    				.replace("{fromname}", sender.getName()).replace("{toname}", target.getName()).replace("{message}", message));
          	        	String msgformattoadmin = ChatColor.translateAlternateColorCodes('&',getConfig().getString("MessageFormatToAdmin")
          	    				.replace("{fromname}", sender.getName()).replace("{toname}", target.getName()).replace("{message}", message));
          	        	
          	        	
          	            target.sendMessage(intro + msgformatto);
          	            sender.sendMessage(intro + msgformatfrom);
          	            
          	            
          	          boolean cmdpeek = getConfig().getBoolean("MessagePeek");
          	          
          	          if(cmdpeek == true) {
          	        	  
                  		for(Player ps : Bukkit.getOnlinePlayers()) {
                    		if(ps.hasPermission("messagepeek.chatmanager")) {
                    					
                    			ps.sendMessage(intro + msgformattoadmin);
                    		} else { }
                    		}
          	        	  
          	          } else { }
          	            
          	            
          	            
          	            
          	            
            	        } else {
            	        	String msgformatnotonline = ChatColor.translateAlternateColorCodes('&',getConfig().getString("MessageFormatNotOnline")
              	    				.replace("{fromname}", sender.getName()).replace("{toname}", args[0]).replace("{name}", p.getName()));
            	        	
          	            sender.sendMessage(intro + msgformatnotonline);
          	        }
          	    }
          	}
          	
          	
          	
          	
          	
          	//Config
          	

          
          
          	
          	//Global
          	String gtochat = ChatColor.translateAlternateColorCodes('&',getConfig().getString("GlobalToChat")
    				.replace("{name}", p.getName()));
          	String gtosender = ChatColor.translateAlternateColorCodes('&',getConfig().getString("GlobalToSender")
    				.replace("{name}", p.getName()));
          	
          	//Self
          	String stosender = ChatColor.translateAlternateColorCodes('&',getConfig().getString("SelfToSender")
    				.replace("{name}", p.getName()));
          	
          	//No Permission
          	String noperm = ChatColor.translateAlternateColorCodes('&',getConfig().getString("NoPermission")
    				.replace("{name}", p.getName()));
          	
          	//ChatEnable/Disable
          	String chatenable = ChatColor.translateAlternateColorCodes('&',getConfig().getString("ChatEnabledMessage")
    				.replace("{name}", p.getName()));
          	
          	String chatdisable = ChatColor.translateAlternateColorCodes('&',getConfig().getString("ChatDisabledMessage")
    				.replace("{name}", p.getName()));
          	
          	String nomessage = ChatColor.translateAlternateColorCodes('&',getConfig().getString("NoMessage")
    				.replace("{name}", p.getName()));

          	String color = ChatColor.translateAlternateColorCodes('&',getConfig().getString("Color"));
          	String name = ChatColor.translateAlternateColorCodes('&',getConfig().getString("NamePart").replace("{name}", p.getName()));
          	
          	//ConsoleMessages
          	boolean console = getConfig().getBoolean("ConsoleMessagesEnabled");
          	
          
          	
            //Continues at Player
          	
          //ALWAYS REMEMBER TO UPDATE VERSION
          //ALWAYS REMEMBER TO UPDATE VERSION

    		if (cmd.getName().equalsIgnoreCase("broadcast")) {
    			if (!(sender instanceof Player)) {
    				sender.sendMessage(intro + "Players Only!");

    			} else {
    				if (!p.hasPermission("broadcast.chatmanager")) {
    					p.sendMessage(intro + noperm);

    				} else if (args.length == 0) {
    					p.sendMessage(intro + nomessage);
    				} else {
    					getServer().broadcastMessage(intro + color + message(args));
    				}

    			}
    		}
    		if (cmd.getName().equalsIgnoreCase("namebroadcast")) {
    			if (!(sender instanceof Player)) {
    				sender.sendMessage(intro + "Players Only!");

    			} else {
    				if (!p.hasPermission("namebroadcast.chatmanager")) {
    					p.sendMessage(intro + noperm);

    				} else if (args.length == 0) {
    					p.sendMessage(intro + nomessage);
    				} else {
    					getServer().broadcastMessage(intro + name + color + message(args));
    				}

    			}
    		}
          
          	
           	if (cmd.getName().equalsIgnoreCase("chatmanager")) {
           		boolean permhelp = getConfig().getBoolean("PermissionHelp");
           		if(sender instanceof Player){
           		if (args.length == 0) {
                  	p.sendMessage(intro + ChatColor.RED + "Incorrect Arguments! Usage: " + ChatColor.DARK_RED + "/chatmanager [help,version,menu,info]"); }
           		
           
                        
                  else if (args.length == 1) {
                	  if (args[0].equalsIgnoreCase("help")) {
                		  if(permhelp == true) {
                    	  p.sendMessage(" ");p.sendMessage(" ");
                    	  p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&m&l-----------------------------------"));
                    	  p.sendMessage(" ");p.sendMessage(" ");
                    	  p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&l     [ChatManager Help]"));
                    	  p.sendMessage(" ");p.sendMessage(" ");
                          p.sendMessage(intro + Helphelp);
                          p.sendMessage(intro + HelpVersion );
                          if (p.hasPermission("gui.chatmanager")) {
                          p.sendMessage(intro + HelpMenu );
                          } else { }
                          p.sendMessage(intro + HelpInfo );
						  if (p.hasPermission("join.chatmanager")) {
							  p.sendMessage(intro + HelpJoin );
							  } else { }
						  if (p.hasPermission("leave.chatmanager")) {
							  p.sendMessage(intro + HelpLeave );
							  } else { }
						  if (p.hasPermission("global.chatmanager")) {
                          p.sendMessage(intro + HelpCC );
						  } else { }
						  if (p.hasPermission("other.chatmanager")) {
                          p.sendMessage(intro + HelpPCC); 
						  } else { }
                          p.sendMessage(intro + HelpSCC );
						  if (p.hasPermission("chaton.chatmanager")) {
                          p.sendMessage(intro + HelpOn );
						  } else { }
						  if (p.hasPermission("chatoff.chatmanager")) {
                          p.sendMessage(intro + HelpOff );
						  } else { }
						  if (p.hasPermission("broadcast.chatmanager")) {
	                          p.sendMessage(intro + HelpBC );
							  } else { }
						  if (p.hasPermission("namebroadcast.chatmanager")) {
	                          p.sendMessage(intro + HelpNBC );
							  } else { }
						  if (p.hasPermission("mute.chatmanager")) {
	                          p.sendMessage(intro + HelpMute );
							  } else { }
						  if (p.hasPermission("unmute.chatmanager")) {
	                          p.sendMessage(intro + HelpUnmute );
							  } else { }
                          p.sendMessage(" ");
                          p.sendMessage(ChatColor.RED + "       " + ChatColor.BOLD + ChatColor.UNDERLINE +"Key:");
                          p.sendMessage("");
                          p.sendMessage(ChatColor.AQUA + "          " + ChatColor.BOLD + "Aqua:" + ChatColor.GREEN + " Command.");
                          p.sendMessage(ChatColor.GOLD + "          " + ChatColor.BOLD + "Gold:" + ChatColor.GREEN + " Description.");
                          p.sendMessage(" ");p.sendMessage(" ");
                          p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&m&l-----------------------------------"));
                      
                		  }
                		  
                		  if(permhelp == false) {
                			  p.sendMessage(" ");p.sendMessage(" ");
                        	  p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&m&l-----------------------------------"));
                        	  p.sendMessage(" ");p.sendMessage(" ");
                        	  p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&l     [ChatManager Help]"));
                        	  p.sendMessage(" ");p.sendMessage(" ");
                              p.sendMessage(intro + Helphelp);
                              p.sendMessage(intro + HelpVersion);
                              p.sendMessage(intro + HelpMenu);
                              p.sendMessage(intro + HelpInfo);
                              p.sendMessage(intro + HelpLeave);
                              p.sendMessage(intro + HelpJoin);
                              p.sendMessage(intro + HelpCC);
                              p.sendMessage(intro + HelpPCC);
                              p.sendMessage(intro + HelpSCC);
                              p.sendMessage(intro + HelpOn);
                              p.sendMessage(intro + HelpOff);
                              p.sendMessage(intro + HelpBC);
                              p.sendMessage(intro + HelpNBC);
                              p.sendMessage(intro + HelpMute);
                              p.sendMessage(intro + HelpUnmute);
                              p.sendMessage(" ");
                              p.sendMessage(ChatColor.RED + "       " + ChatColor.BOLD + ChatColor.UNDERLINE +"Key:");
                              p.sendMessage("");
                              p.sendMessage(ChatColor.AQUA + "          " + ChatColor.BOLD + "Aqua:" + ChatColor.GREEN + " Command.");
                              p.sendMessage(ChatColor.GOLD + "          " + ChatColor.BOLD + "Gold:" + ChatColor.GREEN + " Description.");
                              p.sendMessage(" ");p.sendMessage(" ");
                              p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&m&l-----------------------------------"));
                          
                    		  }
                      }
                          
                         
                	  
                	  else if (args[0].equalsIgnoreCase("join")) {
                		  if (p.hasPermission("join.chatmanager")) {
                          Bukkit.broadcastMessage(ChatColor.YELLOW + p.getName() + " joined the game");
                		  for(Player ps : Bukkit.getOnlinePlayers()) {
                			  if(ps.isOp()) { 
                				  p.sendMessage(intro + ChatColor.RED + p.getName() + " fake joined.");
                			  }
                		  }
                		  }
                	  }
                	  else if (args[0].equalsIgnoreCase("leave")) {
                		  if (p.hasPermission("leave.chatmanager")) {
                          Bukkit.broadcastMessage(ChatColor.YELLOW + p.getName() + " left the game");
                		  for(Player ps : Bukkit.getOnlinePlayers()) {
                			  if(ps.isOp()) { 
                				  p.sendMessage(intro + ChatColor.RED + p.getName() + " fake left.");
                			  }
                		  }
                		  }
                	  }
                		  
                	  
                	  
                          else if (args[0].equalsIgnoreCase("version")) {
                              p.sendMessage(intro + ChatColor.DARK_RED + "" + ChatColor.BOLD + "You Are Running ChatManager Version: " + ChatColor.RED + version );
                          }
                          else if (args[0].equalsIgnoreCase("info")) {
                          p.sendMessage(intro + ChatColor.AQUA + "" + ChatColor.BOLD + "Plugin Created By: " + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "A_Brave_Panda.");
                          p.sendMessage(intro + ChatColor.AQUA + "" + ChatColor.BOLD + "Plugin Spigot Page: " + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "https://www.spigotmc.org/resources/chatclear.16452/" );
                          p.sendMessage(intro + ChatColor.AQUA + "" + ChatColor.BOLD + "Other: " + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Happy First Birthday Plugin! (as of 12/31/16)" );
                          }
           		
                  else if (args[0].equalsIgnoreCase("menu")) {
             		if(p.hasPermission("gui.chatmanager")) {
                	Inventory inv = Bukkit.createInventory(null, 54, ChatColor.RED + "" + ChatColor.BOLD + intro + ChatColor.RED + "Menu");
                	
                  	int globalc = this.getConfig().getInt("Global");
                  	int selfc = this.getConfig().getInt("Self");
                  	int otherc = this.getConfig().getInt("Other Player");
                  	int onc = this.getConfig().getInt("Enable Chat");
                  	int offc = this.getConfig().getInt("Disable Chat");
                  	int closec = this.getConfig().getInt("Close Menu");
                  	int helpc = this.getConfig().getInt("Help");
                  	int fj = this.getConfig().getInt("FakeJoin");
                  	int fl = this.getConfig().getInt("FakeLeave");
                  	
                	ItemStack fakejoin = nameItem(Material.EMERALD_BLOCK,intro + ChatColor.YELLOW + "" + ChatColor.BOLD +  "Fake Join");
                	ItemMeta imjoin = fakejoin.getItemMeta();
                	List<String> lore100 = new ArrayList<String>(); 
                	lore100.add(" ");
                	lore100.add(" ");
                	lore100.add(ChatColor.translateAlternateColorCodes('&', "&a&m&l-----------------------------------"));
                	lore100.add(" ");
                	lore100.add(" ");
                	lore100.add(intro + ChatColor.GOLD + "Fake Join");
                	lore100.add(" ");
                	lore100.add(" ");
                	lore100.add(ChatColor.translateAlternateColorCodes('&', "&a&m&l-----------------------------------"));
                	imjoin.setLore(lore100);
                	fakejoin.setItemMeta(imjoin);
                	inv.setItem(fj, fakejoin);
                	p.openInventory(inv);
                	
                	ItemStack fakeleave = nameItem(Material.REDSTONE_BLOCK,intro + ChatColor.YELLOW + "" + ChatColor.BOLD +  "Fake Leave");
                	ItemMeta imleave = fakeleave.getItemMeta();
                	List<String> lore101 = new ArrayList<String>(); 
                	lore101.add(" ");
                	lore101.add(" ");
                	lore101.add(ChatColor.translateAlternateColorCodes('&', "&a&m&l-----------------------------------"));
                	lore101.add(" ");
                	lore101.add(" ");
                	lore101.add(intro + ChatColor.GOLD + "Fake Leave");
                	lore101.add(" ");
                	lore101.add(" ");
                	lore101.add(ChatColor.translateAlternateColorCodes('&', "&a&m&l-----------------------------------"));
                	imleave.setLore(lore101);
                	fakeleave.setItemMeta(imleave);
                	inv.setItem(fl, fakeleave);
                	p.openInventory(inv);
                	
                	ItemStack global = nameItem(Material.BOOK_AND_QUILL,intro + ChatColor.YELLOW + "" + ChatColor.BOLD +  "Global");
                	ItemMeta imglobal = global.getItemMeta();
                	List<String> lore1 = new ArrayList<String>(); 
                	lore1.add(" ");
                	lore1.add(" ");
                	lore1.add(ChatColor.translateAlternateColorCodes('&', "&a&m&l-----------------------------------"));
                	lore1.add(" ");
                	lore1.add(" ");
                	lore1.add(intro + ChatColor.GOLD + "Clear The Global Chat");
                	lore1.add(" ");
                	lore1.add(" ");
                	lore1.add(ChatColor.translateAlternateColorCodes('&', "&a&m&l-----------------------------------"));
                	imglobal.setLore(lore1);
                	global.setItemMeta(imglobal);
                	inv.setItem(globalc, global);
                	p.openInventory(inv);
                	
                	ItemStack self = nameItem(Material.EMPTY_MAP,intro + ChatColor.YELLOW + "" + ChatColor.BOLD +  "Self");
                	ItemMeta imself = self.getItemMeta();
                	List<String> lore2 = new ArrayList<String>(); 
                	lore2.add(" ");
                	lore2.add(" ");
                	lore2.add(ChatColor.translateAlternateColorCodes('&', "&a&m&l-----------------------------------"));
                	lore2.add(" ");
                	lore2.add(" ");
                	lore2.add(intro + ChatColor.GOLD + "Clear Your Chat");
                	lore2.add(" ");
                	lore2.add(" ");
                	lore2.add(ChatColor.translateAlternateColorCodes('&', "&a&m&l-----------------------------------"));
                	imself.setLore(lore2);
                	self.setItemMeta(imself);
                	inv.setItem(selfc, self);
                	p.openInventory(inv);
                	
                	ItemStack other = nameItem(Material.NAME_TAG,intro + ChatColor.YELLOW + "" + ChatColor.BOLD +  "Other Player");
                	ItemMeta imother = other.getItemMeta();
                	List<String> lore3 = new ArrayList<String>(); 
                	lore3.add(" ");
                	lore3.add(" ");
                	lore3.add(ChatColor.translateAlternateColorCodes('&', "&a&m&l-----------------------------------"));
                	lore3.add(" ");
                	lore3.add(" ");
                	lore3.add(intro + ChatColor.GOLD + "Clear Another Player's Chat");
                	lore3.add(" ");
                	lore3.add(" ");
                	lore3.add(ChatColor.translateAlternateColorCodes('&', "&a&m&l-----------------------------------"));
                	imother.setLore(lore3);
                	other.setItemMeta(imother);
                	inv.setItem(otherc, other);
                	p.openInventory(inv);
                	
                	ItemStack on = nameItem(Material.SLIME_BALL,intro + ChatColor.GREEN + "" + ChatColor.BOLD +  "Enable Chat");
                	ItemMeta imon = on.getItemMeta();
                	List<String> lore4 = new ArrayList<String>(); 
                	lore4.add(" ");
                	lore4.add(" ");
                	lore4.add(ChatColor.translateAlternateColorCodes('&', "&a&m&l-----------------------------------"));
                	lore4.add(" ");
                	lore4.add(" ");
                	lore4.add(intro + ChatColor.GOLD + "Enable Global Chat");
                	lore4.add(" ");
                	lore4.add(" ");
                	lore4.add(ChatColor.translateAlternateColorCodes('&', "&a&m&l-----------------------------------"));
                	imon.setLore(lore4);
                	on.setItemMeta(imon);
                	inv.setItem(onc, on);
                	p.openInventory(inv);
                	
                	ItemStack off = nameItem(Material.FIREBALL,intro + ChatColor.RED + "" + ChatColor.BOLD +  "Disable Chat");
                	ItemMeta imoff = off.getItemMeta();
                	List<String> lore5 = new ArrayList<String>(); 
                	lore5.add(" ");
                	lore5.add(" ");
                	lore5.add(ChatColor.translateAlternateColorCodes('&', "&a&m&l-----------------------------------"));
                	lore5.add(" ");
                	lore5.add(" ");
                	lore5.add(intro + ChatColor.GOLD + "Disable Global Chat");
                	lore5.add(" ");
                	lore5.add(" ");
                	lore5.add(ChatColor.translateAlternateColorCodes('&', "&a&m&l-----------------------------------"));
                	imoff.setLore(lore5);
                	off.setItemMeta(imoff);
                	inv.setItem(offc, off);
                	p.openInventory(inv);
                	
                	ItemStack close = nameItem(Material.BARRIER,intro + ChatColor.DARK_RED + "" + ChatColor.BOLD +  "Close Menu");
                	inv.setItem(closec, close);
                	p.openInventory(inv);
                	
                	ItemStack help = nameItem(Material.NETHER_STAR, intro + ChatColor.YELLOW + "" + ChatColor.BOLD +  "Help");
                	ItemMeta imhelp = help.getItemMeta();
                	List<String> lore = new ArrayList<String>();
          		  if(permhelp == true) {
                	  lore.add(" ");lore.add(" ");
                	  lore.add(ChatColor.translateAlternateColorCodes('&', "&a&m&l-----------------------------------"));
                	  lore.add(" ");lore.add(" ");
                	  lore.add(ChatColor.translateAlternateColorCodes('&', "&e&l     [ChatManager Help]"));
                	  lore.add(" ");lore.add(" ");
                      lore.add(intro + Helphelp);
                      lore.add(intro + HelpVersion );
                      if (p.hasPermission("gui.chatmanager")) {
                      lore.add(intro + HelpMenu );
                      } else { }
                      lore.add(intro + HelpInfo );
					  if (p.hasPermission("join.chatmanager")) {
						  lore.add(intro + HelpJoin );
						  } else { }
					  if (p.hasPermission("leave.chatmanager")) {
						  lore.add(intro + HelpLeave );
						  } else { }
					  if (p.hasPermission("global.chatmanager")) {
                      lore.add(intro + HelpCC );
					  } else { }
					  if (p.hasPermission("other.chatmanager")) {
                      lore.add(intro + HelpPCC); 
					  } else { }
                      lore.add(intro + HelpSCC );
					  if (p.hasPermission("chaton.chatmanager")) {
                      lore.add(intro + HelpOn );
					  } else { }
					  if (p.hasPermission("chatoff.chatmanager")) {
                      lore.add(intro + HelpOff );
					  } else { }
					  if (p.hasPermission("broadcast.chatmanager")) {
	                      lore.add(intro + HelpBC );
						  } else { }
					  if (p.hasPermission("namebroadcast.chatmanager")) {
	                      lore.add(intro + HelpNBC );
						  } else { }
					  if (p.hasPermission("mute.chatmanager")) {
                          lore.add(intro + HelpMute );
						  } else { }
					  if (p.hasPermission("unmute.chatmanager")) {
						  lore.add(intro + HelpUnmute );
						  } else { }
                      lore.add(" ");
                      lore.add(ChatColor.RED + "       " + ChatColor.BOLD + ChatColor.UNDERLINE +"Key:");
                      lore.add("");
                      lore.add(ChatColor.AQUA + "          " + ChatColor.BOLD + "Aqua:" + ChatColor.GREEN + " Command.");
                      lore.add(ChatColor.GOLD + "          " + ChatColor.BOLD + "Gold:" + ChatColor.GREEN + " Description.");
                      lore.add(" ");lore.add(" ");
                      lore.add(ChatColor.translateAlternateColorCodes('&', "&a&m&l-----------------------------------"));
                  	imhelp.setLore(lore);
                  	help.setItemMeta(imhelp);
                  	inv.setItem(helpc, help);
                  	p.openInventory(inv);
                  	
                  
            		  }
          		if(permhelp == false) {
                	lore.add(" ");lore.add(" ");
                	lore.add(ChatColor.translateAlternateColorCodes('&', "&a&m&l-----------------------------------"));
                	lore.add(" ");lore.add(" ");
                	lore.add(ChatColor.translateAlternateColorCodes('&', "&e&l     [ChatManager Help]"));
                	lore.add(" ");lore.add(" ");
                    lore.add(intro + Helphelp);
                    lore.add(intro + HelpVersion);
                    lore.add(intro + HelpMenu);
                    lore.add(intro + HelpInfo);
                    lore.add(intro + HelpLeave);
                    lore.add(intro + HelpJoin);
                    lore.add(intro + HelpCC);
                    lore.add(intro + HelpPCC);
                    lore.add(intro + HelpSCC);
                    lore.add(intro + HelpOn);
                    lore.add(intro + HelpOff);
                    lore.add(intro + HelpBC );
                    lore.add(intro + HelpNBC );
                    lore.add(intro + HelpMute);
                    lore.add(intro + HelpUnmute);
                	lore.add(" ");
                	lore.add(ChatColor.RED + "       " + ChatColor.BOLD + ChatColor.UNDERLINE +"Key:");
                	lore.add("");
                	lore.add(ChatColor.AQUA + "          " + ChatColor.BOLD + "Aqua:" + ChatColor.GREEN + " Command.");
                	lore.add(ChatColor.GOLD + "          " + ChatColor.BOLD + "Gold:" + ChatColor.GREEN + " Description.");
                	lore.add(" ");lore.add(" ");
                	lore.add(ChatColor.translateAlternateColorCodes('&', "&a&m&l-----------------------------------"));
                	imhelp.setLore(lore);
                	help.setItemMeta(imhelp);
                	inv.setItem(helpc, help);
                	p.openInventory(inv);
                	
          		}
                	} else {
                		p.sendMessage(intro + noperm);
                	}
             	}
           	}
           	} else {log.sendMessage(intro + ChatColor.RED + "" + ChatColor.BOLD + "Error: You Must Be A Player To Preform That Command"); } 
           	}
          	
           	
          	if (cmd.getName().equalsIgnoreCase("cc")) {
          		
                  	if (sender.hasPermission("global.chatmanager"))
                  	{
              		
                  		for(Player ps : Bukkit.getOnlinePlayers()) {
                  			if(console == true) {
                  				log.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[TO CONSOLE] " + intro + ChatColor.GOLD + " " + ChatColor.BOLD + sender.getName() + " Has Cleared Global Chat!");
                  			}
              	        	for(int i = 0; i<=250; i++){
              	        		ps.sendMessage("");
              	        	}ps.sendMessage(intro + gtochat);
                    	}
              		p.sendMessage(intro + gtosender);
                  	} 
                  	
                  	else {
                  		
                  	p.sendMessage(intro + noperm);
                  	} 
                  return true;
          		
          	}
          
                  if (cmd.getName().equalsIgnoreCase("scc")) {
                	  if(console == true) {
                		  log.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[TO CONSOLE] " + intro + ChatColor.GOLD + " " + ChatColor.BOLD + sender.getName() + " Has Cleared Their Chat!");
                	  }
        	        	for(int i = 0; i<=250; i++){
          	        		p.sendMessage("");
          	        	}
              		p.sendMessage(intro + stosender);
                  }
               
                  Player player = (Player) sender;
              		 if (cmd.getName().equalsIgnoreCase("pcc")) {
                       	if (sender.hasPermission("other.chatmanager")) {
              			 if (args.length == 0) {
              				//Player only typed '/heal' so lets heal them back!
                       		player.sendMessage(intro + ChatColor.RED + "Incorrect Arguments! Usage: " + ChatColor.DARK_RED + "/pcc {name}"); ;
              				} else {
              				//Player typed something more
              				Player target = Bukkit.getPlayerExact(args[0]);
              				if (target == null) {
              				//Target is not online
              				player.sendMessage(intro + ChatColor.RED + args[0] + " Is Not Online!");
              				} else {
              				//Targets online
              		          	//Other Player
              		          	String ptosender = ChatColor.translateAlternateColorCodes('&',getConfig().getString("PlayerToSender")
              		    				.replace("{name}", p.getName()).replace("{target}", target.getName()));
              		          String ptotarget = ChatColor.translateAlternateColorCodes('&',getConfig().getString("PlayerToTarget")
            		    				.replace("{name}", p.getName()).replace("{target}", target.getName()));
              				player.sendMessage(ptosender);
              				if(console == true) {
              					log.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[TO CONSOLE] " + intro + ChatColor.GOLD + " " + ChatColor.BOLD + sender.getName() + " Has Cleared " + target.getName() + "\'s Chat!");
              				}
              	        	for(int i = 0; i<=250; i++){
              	        		target.sendMessage("");
              	        	}
              				target.sendMessage(intro + ptotarget);
              				}
              				}
                       	} else {
                          	p.sendMessage(intro + noperm);
                       	}

              		 }
              		for(Player ps : Bukkit.getOnlinePlayers()) { 
              		if (cmd.getName().equalsIgnoreCase("chat")) {
              			
                   		if (args.length == 0) {
                          	p.sendMessage(intro + ChatColor.RED + "Incorrect Arguments! Usage: " + ChatColor.DARK_RED + "/chat [on,off]"); }
                                
                          else if (args.length == 1) {
                                  if (args[0].equalsIgnoreCase("on")) {
                                	  if (sender.hasPermission("chaton.chatmanager")) {ps.sendMessage(intro + chatenable);
                                	  enabled = true;
                        				if(console == true) {log.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[TO CONSOLE] " + intro + ChatColor.GOLD + " " + ChatColor.BOLD + sender.getName() + " Has Turned On Chat!");}
                                	  
                                  } else { p.sendMessage(intro + noperm); } 
                          }
                                  else if (args[0].equalsIgnoreCase("off")) {if (sender.hasPermission("chatoff.chatmanager")) {ps.sendMessage(intro + chatdisable);
                                	  
                                	  enabled = false;
                                	  if(console == true) {log.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[TO CONSOLE] " + intro + ChatColor.GOLD + " " + ChatColor.BOLD + sender.getName() + " Has Turned Off Chat!");}
                                	  } else { p.sendMessage(intro + noperm); }  
                                  }
                          }
              		}
              		
             		}
              		
              		if(cmd.getName().equalsIgnoreCase("mute")) {
              			if(p.hasPermission("mute.chatmanager")) {
              			if (args.length != 0) {
              				Player target = Bukkit.getPlayerExact(args[0]);
                  			String mutetostaff = ChatColor.translateAlternateColorCodes('&',getConfig().getString("MuteToStaff")
        		    				.replace("{name}", p.getName()).replace("{target}", target.getName()));
                  			String mutetoplayer = ChatColor.translateAlternateColorCodes('&',getConfig().getString("MuteToPlayer")
        		    				.replace("{name}", p.getName()).replace("{target}", target.getName()));
                		File playerFile = getPlayerFile(target.getUniqueId());
            	        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

            	     if(playerData.get("stats.muted").equals("False")) {
            	        playerData.set("stats.muted", "True");
            	        p.sendMessage(intro + mutetostaff);
            	        target.sendMessage(intro + mutetoplayer);
            	        
                	        try {
            						playerData.save(playerFile);
            						
            					} catch (IOException e1) {
            						e1.printStackTrace();
            					}

            	       } else {
            	       	p.sendMessage(intro + ChatColor.RED + "That person is already muted.");
            	       }
              		} else {
              			p.sendMessage(intro + ChatColor.RED + "Incorrect Arguments! Usage: " + ChatColor.DARK_RED + "/mute {player}");
              		}
            	        
              		} else {
              			p.sendMessage(intro + noperm);
              		}
              		}
              		
              		if(cmd.getName().equalsIgnoreCase("unmute")) {
              			if(p.hasPermission("unmute.chatmanager")) {
              			if (args.length != 0) {
              				Player target = Bukkit.getPlayerExact(args[0]);
                  			String unmutetostaff = ChatColor.translateAlternateColorCodes('&',getConfig().getString("UnmuteToStaff")
        		    				.replace("{name}", p.getName()).replace("{target}", target.getName()));
                  			String unmutetoplayer = ChatColor.translateAlternateColorCodes('&',getConfig().getString("UnmuteToPlayer")
        		    				.replace("{name}", p.getName()).replace("{target}", target.getName()));
                		File playerFile = getPlayerFile(target.getUniqueId());
            	        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

            	        if(playerData.get("stats.muted").equals("True")) {
            	        playerData.set("stats.muted", "False");
            	        p.sendMessage(intro + unmutetostaff);
            	        target.sendMessage(intro + unmutetoplayer);
            	        
                	        try {
            						playerData.save(playerFile);
            						
            					} catch (IOException e1) {
            						e1.printStackTrace();
            					}
              			
            	        } else {
                	       	p.sendMessage(intro + ChatColor.RED + "That person is not muted.");
                	       }
              			
              		} else {
              			p.sendMessage(intro + ChatColor.RED + "Incorrect Arguments! Usage: " + ChatColor.DARK_RED + "/unmute {player}");
              		}
              		} else {
              			p.sendMessage(noperm);
              		}
              		}
              		

              		 
    					return true;
		}
		
    	@EventHandler
    	public void onInventoryClick(InventoryClickEvent e) {
    		Inventory inv = e.getInventory();
    		HumanEntity p = e.getWhoClicked();
    		String gtochat = ChatColor.translateAlternateColorCodes('&',getConfig().getString("GlobalToChat")
    				.replace("{name}", p.getName()));
          	String gtosender = ChatColor.translateAlternateColorCodes('&',getConfig().getString("GlobalToSender")
    				.replace("{name}", p.getName()));
          	
          	//Self
          	String stosender = ChatColor.translateAlternateColorCodes('&',getConfig().getString("SelfToSender")
    				.replace("{name}", p.getName()));
          	
          	//No Permission
          	String noperm = ChatColor.translateAlternateColorCodes('&',getConfig().getString("NoPermission")
    				.replace("{name}", p.getName()));
          	
          	//ChatEnable/Disable
          	String chatenable = ChatColor.translateAlternateColorCodes('&',getConfig().getString("ChatEnabledMessage")
    				.replace("{name}", p.getName()));
          	
          	String chatdisable = ChatColor.translateAlternateColorCodes('&',getConfig().getString("ChatDisabledMessage")
    				.replace("{name}", p.getName()));
          	
          	//ConsoleMessages
          	boolean console = getConfig().getBoolean("ConsoleMessagesEnabled");
          	
          	
          	
          	
    		if (!inv.getTitle().equals(ChatColor.RED + "" + ChatColor.BOLD + intro + ChatColor.RED + "Menu")) return;
    		
    		Player player = (Player) e.getWhoClicked();
    		ItemStack item = e.getCurrentItem();
    		
    		

    		if(item.getType() == Material.AIR) {
    			p.sendMessage(intro + ChatColor.RED + "Error:" + ChatColor.DARK_RED + " This Is Not An Item Silly!"); 
    			
    			e.setCancelled(true);
    			
    			
    		}
    		if(item.getType() == Material.BOOK_AND_QUILL) {
              	if (p.hasPermission("global.chatmanager"))
              	{
          		
              		for(Player ps : Bukkit.getOnlinePlayers()) {
              			if(console == true) {
              				log.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[TO CONSOLE] " + intro + ChatColor.GOLD + " " + ChatColor.BOLD + p.getName() + " Has Cleared Global Chat!");
              			}
          	        	for(int i = 0; i<=250; i++){
          	        		ps.sendMessage("");
          	        	}ps.sendMessage(intro + gtochat);
                	}
          		p.sendMessage(intro + gtosender);
              	} 
              	
              	else {
              		
              	p.sendMessage(intro + noperm);
              	} 
    			
    			e.setCancelled(true);
    			player.closeInventory();
    			
    			
    			
    		} else {
    			e.setCancelled(true);
    			player.closeInventory();	
    		}
    		
    		if(item.getType() == Material.EMPTY_MAP) {
          	  if(console == true) {
          		log.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[TO CONSOLE] " + intro + ChatColor.GOLD + " " + ChatColor.BOLD + p.getName() + " Has Cleared Their Chat!");
          	  }
	        	for(int i = 0; i<=250; i++){
  	        		p.sendMessage("");
  	        	}
        		p.sendMessage(intro + stosender);
    			
    			e.setCancelled(true);
    			player.closeInventory();
    			
    			
    		} else {
    			e.setCancelled(true);
    			player.closeInventory();	
    		}
    		
    		
    		if(item.getType() == Material.NAME_TAG) {
    			if (p.hasPermission("other.chatmanager")) {
    			p.sendMessage(intro + ChatColor.GOLD + "Enter Players Name In Chat!");

    			chat = true;
    			} else {
    				p.sendMessage(intro + noperm);
    			}
    			
    			
    			e.setCancelled(true);
    			player.closeInventory();
    			
    			
    		} else {
    			e.setCancelled(true);
    			player.closeInventory();	
    		}
    		
    		if(item.getType() == Material.BARRIER) {
    			p.closeInventory();
    			
    			e.setCancelled(true);
    			player.closeInventory();
    			
    			
    		} else {
    			e.setCancelled(true);
    			player.closeInventory();	
    		
    		}
    		
    		if(item.getType() == Material.FIREBALL) {
    			for(Player ps : Bukkit.getOnlinePlayers()) {
          	  if (p.hasPermission("chatoff.chatmanager")) { 
          	  ps.sendMessage(intro + chatdisable);
          	  
          	  enabled = false;
          	  if(console == true) {
          		log.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[TO CONSOLE] " + intro + ChatColor.GOLD + " " + ChatColor.BOLD + p.getName() + " Has Turned Off Chat!");
      				}
          	  } else { p.sendMessage(intro + noperm); } }
    			
    			e.setCancelled(true);
    			player.closeInventory();
    			
    		} else {
    			e.setCancelled(true);
    			player.closeInventory();	
    		}
    		
    		if(item.getType() == Material.EMERALD_BLOCK) {
          	  if (p.hasPermission("join.chatmanager")) { 

          		  Bukkit.broadcastMessage(ChatColor.YELLOW + player.getName() + " joined the game");
          		  
          	  if(console == true) {
          		log.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[TO CONSOLE] " + intro + ChatColor.GOLD + " " + ChatColor.BOLD + p.getName() + " Has Fake Joined!");
      				}
          	  } else { p.sendMessage(intro + noperm); } 
    			
    			e.setCancelled(true);
    			player.closeInventory();
    			
    		} else {
    			e.setCancelled(true);
    			player.closeInventory();	
    		}
    		
    		if(item.getType() == Material.REDSTONE_BLOCK) {
          	  if (p.hasPermission("leave.chatmanager")) { 

          		  Bukkit.broadcastMessage(ChatColor.YELLOW + player.getName() + " left the game");
          		  
          	  if(console == true) {
          		log.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[TO CONSOLE] " + intro + ChatColor.GOLD + " " + ChatColor.BOLD + p.getName() + " Has Fake Left!");
      				}
          	  } else { p.sendMessage(intro + noperm); } 
    			
    			e.setCancelled(true);
    			player.closeInventory();
    			
    		} else {
    			e.setCancelled(true);
    			player.closeInventory();	
    		}
    		
    		if(item.getType() == Material.SLIME_BALL) {
    			for(Player ps : Bukkit.getOnlinePlayers()) {
                 	  if (p.hasPermission("chaton.chatmanager")) { 
                    	  ps.sendMessage(intro + chatenable);
                    	  enabled = true;
            				if(console == true) {
            					log.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[TO CONSOLE] " + intro + ChatColor.GOLD + " " + ChatColor.BOLD + p.getName() + " Has Turned On Chat!");
                  				}
                    	  
                      }else { p.sendMessage(intro + noperm); }  }
    			
    			e.setCancelled(true);
    			player.closeInventory();
    			
    		} else {
    			e.setCancelled(true);
    			player.closeInventory();	
    		}
    	}
		
        @EventHandler
		public void onPlayerChat(AsyncPlayerChatEvent e) {
			Player p = e.getPlayer();
          	String chatoff = ChatColor.translateAlternateColorCodes('&',getConfig().getString("ChatOffMessage")
    				.replace("{name}", p.getName()));
          	
          	
			if(enabled == false) {
				if(p.hasPermission("exempt.chatmanager")) {
					
				} else {
					e.setCancelled(true);
					p.sendMessage(intro + chatoff); 
				}
			} else { }

		}
        
      
        
    	private ItemStack nameItem(ItemStack item, String name) {
    		ItemMeta meta = item.getItemMeta();
    		meta.setDisplayName(name);
    		item.setItemMeta(meta);
    		return item;
    		
    	}
    	
    	private ItemStack nameItem(Material item, String name) {
    		return nameItem(new ItemStack(item), name);
    	} 
    	 
    	@EventHandler
    	public void onPlayerName(AsyncPlayerChatEvent e) {
    		Player p = e.getPlayer();
    		String m = e.getMessage();
    		Player target = Bukkit.getPlayerExact(m);
    		if(chat == true) {
    			if (p.hasPermission("other.chatmanager")) {
    			chat = false;
					e.setCancelled(true);
    			if (target == null) {
      				p.sendMessage(intro + ChatColor.RED + m + " Is Not Online!");
      				} else {
      		          String ptotarget = ChatColor.translateAlternateColorCodes('&',getConfig().getString("PlayerToTarget")
    		    				.replace("{name}", p.getName()).replace("{target}", target.getName()));
      	        	for(int i = 0; i<=250; i++){
      	        		target.sendMessage("");
      	        	}
      					target.sendMessage(intro + ptotarget);
      					if(console == true) {
      						log.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[TO CONSOLE] " + intro + ChatColor.GOLD + " " + ChatColor.BOLD + p.getName() + " Has Cleared " + target.getName() + "\'s Chat!");
              				}
      				}
    			
    			
    		}
    		}
    	}
    	

    	
    	@EventHandler
    	public void onPlayerJoin(PlayerJoinEvent e) {
    		Player p = e.getPlayer();
    
    		File playerFile = getPlayerFile(p.getUniqueId());
	        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);

	        
	        playerData.set("stats.totalLogins", playerData.getInt("stats.totalLogins") + 1);
	        playerData.set("info.username", p.getName());
    	        try {
						playerData.save(playerFile);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
						if(playerData.get("stats.muted") == null) {
	            	        playerData.set("stats.muted", "False");
	                	        try {
	            						playerData.save(playerFile);
	            					} catch (IOException e11) {
	            						e11.printStackTrace();
	            					}}
    		
    		if(p.isOp()) {
    			p.sendMessage(intro + ChatColor.translateAlternateColorCodes('&', "&aChatManager Is Running Version: " + version));
    		}
					}
    	

        @EventHandler
        public void onChat(AsyncPlayerChatEvent event) {
                UUID id = event.getPlayer().getUniqueId();
                Player p = event.getPlayer();
        		File playerFile = getPlayerFile(p.getUniqueId());
    	        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
                if(!p.hasPermission("exempt.chatmanager")) {
                if(playerData.get("stats.muted").equals("True")) {
                	
                 	String muted = ChatColor.translateAlternateColorCodes('&',getConfig().getString("TalkDenyMessage")
            				.replace("{name}", p.getName()));
                	
                	event.setCancelled(true);
                	p.sendMessage(intro + muted);
                	
                }
                } else { 
                
                }
                
 
                if (!delayMap.containsKey(id)) {
                        delayMap.put(id, System.currentTimeMillis());
                        return;
                }
                
                long lastChat = delayMap.get(id);
                long now = System.currentTimeMillis();
 
                long diff = now - lastChat;
                long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
                int time = (int) (TimeUnit.MILLISECONDS.toSeconds(chatDelay) - seconds);
 
                if (!(diff >= chatDelay)) {
                	
                	if(p.hasPermission("exempt.chatmanager")) {
                        return;
                	}
                	else {
                		event.setCancelled(true);
                	}
 
                        if (warnMessage != null && !warnMessage.isEmpty()) {
                        	if(enabled == true) {
                        	
                        	if(time == 1) {
                                String message = warnMessage.replace("{name}", event.getPlayer().getName()).replace("[seconds]", "second").replace("{time}", Integer.toString(time));
                                message = ChatColor.translateAlternateColorCodes('&', message);
                                event.getPlayer().sendMessage(intro + message);
                        	}
                        	else if(time == 0) {
                        		String message = warnMessage.replace("{name}", event.getPlayer().getName()).replace("[seconds]", "seconds").replace("{time}", Integer.toString(time));
                                message = ChatColor.translateAlternateColorCodes('&', message);
                                event.getPlayer().sendMessage(intro + message);
                        	}
                        	else if(time > 1) {
                        		String message = warnMessage.replace("{name}", event.getPlayer().getName()).replace("[seconds]", "seconds").replace("{time}", Integer.toString(time));
                                message = ChatColor.translateAlternateColorCodes('&', message);
                                event.getPlayer().sendMessage(intro + message);
                        	}
                                
                        	} else {
                        		return;
                        	}
                                
                        }
 
                        return;
                }
 
                delayMap.put(id, System.currentTimeMillis());
        }
 
        @EventHandler
        public void onQuit(PlayerQuitEvent event) {
                UUID id = event.getPlayer().getUniqueId();
 
                if (delayMap.containsKey(id)) {
                        delayMap.remove(id);
                }
        }
 
        @EventHandler
        public void onKick(PlayerKickEvent event) {
                UUID id = event.getPlayer().getUniqueId();
 
                if (delayMap.containsKey(id)) {
                        delayMap.remove(id);
                }
        }
        
        //SIGN STUFFS
        
        @EventHandler
        public void onSignChange(SignChangeEvent e) {
        Player p = e.getPlayer();
        if(e.getLine(1).equalsIgnoreCase("Global") && e.getLine(0).equalsIgnoreCase("[ChatManager]")) {
        if(p.hasPermission("sign.chatmanager")) {
        e.setLine(0, ChatColor.GRAY + "" + ChatColor.BOLD + "[" + ChatColor.GREEN + ChatColor.BOLD + "ChatManager" + ChatColor.GRAY + ChatColor.BOLD + "]");
        e.setLine(1, ChatColor.GOLD + "" + ChatColor.BOLD + "Global");
        p.sendMessage(intro + ChatColor.GOLD + "You have sucessfully made a " + ChatColor.BOLD + "ChatManager Global Sign");
        	}
        	}
        if(e.getLine(1).equalsIgnoreCase("Self") && e.getLine(0).equalsIgnoreCase("[ChatManager]")) {
            if(p.hasPermission("sign.chatmanager")) {
            e.setLine(0, ChatColor.GRAY + "" + ChatColor.BOLD + "[" + ChatColor.GREEN + ChatColor.BOLD + "ChatManager" + ChatColor.GRAY + ChatColor.BOLD + "]");
            e.setLine(1, ChatColor.GOLD + "" + ChatColor.BOLD + "Self");
            p.sendMessage(intro + ChatColor.GOLD + "You have sucessfully made a " + ChatColor.BOLD + "ChatManager Self Sign");
            }
            }
        if(e.getLine(1).equalsIgnoreCase("Chat") && e.getLine(0).equalsIgnoreCase("[ChatManager]") && e.getLine(2).equalsIgnoreCase("On")) {
            if(p.hasPermission("sign.chatmanager")) {
            e.setLine(0, ChatColor.GRAY + "" + ChatColor.BOLD + "[" + ChatColor.GREEN + ChatColor.BOLD + "ChatManager" + ChatColor.GRAY + ChatColor.BOLD + "]");	
            e.setLine(1, ChatColor.GOLD + "" + ChatColor.BOLD + "Chat");
            e.setLine(2, ChatColor.GREEN + "" + ChatColor.BOLD + "On");
            p.sendMessage(intro + ChatColor.GOLD + "You have sucessfully made a " + ChatColor.BOLD + "ChatManager Chat On Sign");
            }
            }
        if(e.getLine(1).equalsIgnoreCase("Chat") && e.getLine(0).equalsIgnoreCase("[ChatManager]") && e.getLine(2).equalsIgnoreCase("Off")) {
            if(p.hasPermission("sign.chatmanager")) {
            e.setLine(0, ChatColor.GRAY + "" + ChatColor.BOLD + "[" + ChatColor.GREEN + ChatColor.BOLD + "ChatManager" + ChatColor.GRAY + ChatColor.BOLD + "]");
            e.setLine(1, ChatColor.GOLD + "" + ChatColor.BOLD + "Chat");
            e.setLine(2, ChatColor.RED + "" + ChatColor.BOLD + "Off");
            p.sendMessage(intro + ChatColor.GOLD + "You have sucessfully made a " + ChatColor.BOLD + "ChatManager Chat Off Sign");
            }
            }
        }
        
        @EventHandler
        public void SignClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK){ //Checks if person is right clicking on a block
        if (e.getClickedBlock().getState() instanceof Sign) { //This checks if the player is right clicking on the sign and it'll do all the code below if it is a sign.
        Sign sign = (Sign) e.getClickedBlock().getState();
        if(sign.getLine(0).equalsIgnoreCase(ChatColor.GRAY + "" + ChatColor.BOLD + "[" + ChatColor.GREEN + ChatColor.BOLD + "ChatManager" + ChatColor.GRAY + ChatColor.BOLD + "]") && sign.getLine(1).equalsIgnoreCase(ChatColor.GOLD + "" + ChatColor.BOLD + "Global")) {
        	if(p.hasPermission("global.chatmanager")) {
        	for(int i = 0; i<=250; i++){
        		Bukkit.broadcastMessage("");
        	}
          	String gtochat = ChatColor.translateAlternateColorCodes('&',getConfig().getString("GlobalToChat")
    				.replace("{name}", p.getName()));
        	Bukkit.broadcastMessage(intro + gtochat);
        	}
        	else {
              	String noperm = ChatColor.translateAlternateColorCodes('&',getConfig().getString("NoPermission")
        				.replace("{name}", p.getName()));
        		p.sendMessage(noperm);
        	}
        }
        if(sign.getLine(0).equalsIgnoreCase(ChatColor.GRAY + "" + ChatColor.BOLD + "[" + ChatColor.GREEN + ChatColor.BOLD + "ChatManager" + ChatColor.GRAY + ChatColor.BOLD + "]") && sign.getLine(1).equalsIgnoreCase(ChatColor.GOLD + "" + ChatColor.BOLD + "Self")) {
        	for(int i = 0; i<=250; i++){
        		p.sendMessage("");
        	}
          	String ptoself = ChatColor.translateAlternateColorCodes('&',getConfig().getString("SelfToSender")
    				.replace("{name}", p.getName()));
        	p.sendMessage(intro + ptoself);
        }
        if(sign.getLine(0).equalsIgnoreCase(ChatColor.GRAY + "" + ChatColor.BOLD + "[" + ChatColor.GREEN + ChatColor.BOLD + "ChatManager" + ChatColor.GRAY + ChatColor.BOLD + "]") && sign.getLine(1).equalsIgnoreCase(ChatColor.GOLD + "" + ChatColor.BOLD + "Chat") && sign.getLine(2).equalsIgnoreCase(ChatColor.GREEN + "" + ChatColor.BOLD + "On")) {
        	if(p.hasPermission("chaton")) {
            	enabled = true;
              	String chaton = ChatColor.translateAlternateColorCodes('&',getConfig().getString("ChatEnabledMessage").replace("{name}", p.getName()));
              	Bukkit.broadcastMessage(intro + chaton);
        	} else {
              	String noperm = ChatColor.translateAlternateColorCodes('&',getConfig().getString("NoPermission")
        				.replace("{name}", p.getName()));
        		p.sendMessage(intro + noperm);
        	}
        }
        if(sign.getLine(0).equalsIgnoreCase(ChatColor.GRAY + "" + ChatColor.BOLD + "[" + ChatColor.GREEN + ChatColor.BOLD + "ChatManager" + ChatColor.GRAY + ChatColor.BOLD + "]") && sign.getLine(1).equalsIgnoreCase(ChatColor.GOLD + "" + ChatColor.BOLD + "Chat") && sign.getLine(2).equalsIgnoreCase(ChatColor.RED + "" + ChatColor.BOLD + "Off")) {
        	if(p.hasPermission("chatoff")) {
            	enabled = false;
              	String chaton = ChatColor.translateAlternateColorCodes('&',getConfig().getString("ChatDisabledMessage")
        				.replace("{name}", p.getName()));
              	Bukkit.broadcastMessage(intro + chaton);
        	} else {
              	String noperm = ChatColor.translateAlternateColorCodes('&',getConfig().getString("NoPermission")
        				.replace("{name}", p.getName()));
        		p.sendMessage(intro + noperm);
        	}
        }
        }
        }
        }
        
        
        @EventHandler
        public void OnChat(AsyncPlayerChatEvent e) {
        	Player p = e.getPlayer();
        	String d = p.getDisplayName();
        	String m = e.getMessage();
        	String noperm = ChatColor.translateAlternateColorCodes('&',getConfig().getString("NoPermission")
    				.replace("{name}", p.getName()));
        	if(m.length() > 8) {
           	String correct = m.substring(0,9);
        	
        	

           	
        	
        	if(e.getMessage().contains("StaffChat")  && correct.equalsIgnoreCase("StaffChat")) {
        		if(p.hasPermission("staffchat.send.chatmanager")) {
        		if(m.length() > 8) {
        			String pm = m.substring(10, m.length());
        		for(Player ps : Bukkit.getOnlinePlayers()) {
        		if(ps.hasPermission("staffchat.receive.chatmanager")) {
        			
                   	String stm = ChatColor.translateAlternateColorCodes('&',getConfig().getString("StaffChatFormat")
            				.replace("{name}", d).replace("{message}", pm));
        			
        			ps.sendMessage(intro + stm);
            		e.setCancelled(true);
        		} else { }
        		}
        		}  else {
        			p.sendMessage(intro + noperm); 
        			
        		}
        	} else { }
        	} else { }
        	
        	if(e.getMessage().contains("StaffHelp")  && correct.equalsIgnoreCase("StaffHelp")) {
        		if(m.length() > 8) {
        		String pm = m.substring(10, m.length());
        		for(Player ps : Bukkit.getOnlinePlayers()) {
        		if(ps.hasPermission("staffchat.receive.chatmanager")) {
        			
                   	String stm = ChatColor.translateAlternateColorCodes('&',getConfig().getString("StaffHelpFormat")
            				.replace("{name}", d).replace("{message}", pm));
        			ps.sendMessage(intro + stm);
        		}
        		else { }
        		}
        		e.setCancelled(true);
        		
        		String stm = ChatColor.translateAlternateColorCodes('&',getConfig().getString("StaffHelpSuccess")
        				.replace("{name}", d));
        		p.sendMessage(intro + stm);
        	} else { }
        	} else { }
        } else { }
        }
        
        @EventHandler
        public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player p = event.getPlayer();
        
       
        if (this.getConfig().getBoolean("NotifyMessageEnabled") == true) {
        for (Player ps: Bukkit.getOnlinePlayers()) {
        String player = ps.getName();
        Player pp = Bukkit.getServer().getPlayer(player);

        if (message.toLowerCase().contains(player.toLowerCase())) {
        	
        	 String nm = ChatColor.translateAlternateColorCodes('&',getConfig().getString("NotifyMessageChat").replace("{sendername}", p.getName()).replace("{recievername}", pp.getName()));
        	pp.sendMessage(intro + nm);
        	pp.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0f, 2.0f);
        }

        }
        } else {}

        }
        


        @EventHandler
        public void onPlayerChat4(AsyncPlayerChatEvent e) {
        	
        	if (this.getConfig().getBoolean("AntiSwearEnabled") == true) {
        	
        	if(!e.getPlayer().hasPermission("swearallow.chatmanager")) {
            for (String word : e.getMessage().split(" ")) {
                    if (getConfig().getStringList("Swears").contains(word)) {
                    	
                            e.setCancelled(true);
                            String ns = ChatColor.translateAlternateColorCodes('&',getConfig().getString("NoSwearMessage").replace("{name}", e.getPlayer().getName()).replace("{word}", word));
                            String op = ChatColor.translateAlternateColorCodes('&',getConfig().getString("NoSwearAdmin").replace("{name}", e.getPlayer().getName()).replace("{word}", word));
                            e.getPlayer().sendMessage(intro + ns);
                    		for(Player ps : Bukkit.getOnlinePlayers()) {
                    			if(ps.hasPermission("seeswear.chatmanager")) {
                    				ps.sendMessage(intro + op);
                    			}
                    		}
                    }
            }
        	} else { e.setCancelled(false);}
        } else { }
        } 
        
        
}