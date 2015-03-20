package dk.muj.derius.blocks;

import java.util.HashSet;
import java.util.Set;

import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.TimeUnit;
import com.massivecraft.massivecore.xlib.gson.annotations.SerializedName;

public class MChunk extends Entity<MChunk>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public static MChunk get(Object oid)
	{
		return MChunkColl.get().get(oid);
	}
	
	public static MChunk get(Object oid, boolean creative)
	{
		return MChunkColl.get().get(oid, creative);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: ENTITY
	// -------------------------------------------- //
	
	@Override
	public MChunk load(MChunk that)
	{
		if (that == null || that == this) return that;
		this.setBlocks(that.getBlocks());
		this.setLastActive(that.getLastActive());
		
		return this;
	}
	
	@Override
	public boolean isDefault()
	{
		return this.getBlocks() == null || this.getBlocks().isEmpty();
	}
	
	@Override
	public void changed()
	{
		this.activeNow();
		super.changed();
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	@SerializedName("b")
	private Set<PS> blocks = new HashSet<>();
	public Set<PS> getBlocks() { return this.blocks; }
	public void setBlocks(Set<PS> blocks) { this.blocks = blocks; this.changed();}
	
	@SerializedName("l")
	private long location = System.currentTimeMillis();
	public long getLastActive() { return this.location; }
	public void setLastActive(long lastActive) { this.location = lastActive; this.changed(); }
	public void activeNow() { this.setLastActive(System.currentTimeMillis()); }
	
	// -------------------------------------------- //
	// BLOCK GETTERS & SETTERS
	// -------------------------------------------- //
	
	public boolean addBlock(final PS ps)
	{
		final PS block = ps.getBlockCoords(true);
		if ( ! DeriusBlocks.getChunkId(ps).equals(this.getId())) return false;
		
		boolean ret = this.getBlocks().add(block);
		if (ret) this.changed();
	
		return ret;
	}
	
	public boolean removeBlock(final PS ps)
	{
		final PS block = ps.getBlockCoords(true);
		if ( ! DeriusBlocks.getChunkId(ps).equals(this.getId())) return false;
		
		boolean ret = this.getBlocks().remove(block);
		if (ret) this.changed();
		
		return ret;
	}
	
	// -------------------------------------------- //
	// CONVENIENCE
	// -------------------------------------------- //
	
	public void tryDetach()
	{
		if ( ! (this.isDefault() || this.isOld()) ) return;
		this.detach();
		this.blocks = null;
	}

	public boolean isOld()
	{
		long oldWhen = this.getLastActive() + TimeUnit.MILLIS_PER_DAY * Const.DAYS_BEFORE_OLD;
		return System.currentTimeMillis() >= oldWhen;
	}
	
}
