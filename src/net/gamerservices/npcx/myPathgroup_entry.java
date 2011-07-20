package net.gamerservices.npcx;

import org.bukkit.Location;

public class myPathgroup_entry {
	public int id;
	public String name;
	public Location location;
	public int pathgroupid;
	public int spot;
	public myPathgroup parent;
	
	myPathgroup_entry(Location location, int pathgroupid, myPathgroup pathgroup, int spot)
	{
		this.location = location;
		this.pathgroupid = pathgroupid;
		this.spot = spot;
		this.parent = pathgroup;
	}
	
	
	
}
