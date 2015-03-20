package dk.muj.derius.blocks;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Material;

import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.ps.PSFormat;
import com.massivecraft.massivecore.ps.PSFormatFormal;

public final class DeriusBlocks extends MassivePlugin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static DeriusBlocks i;
	public static DeriusBlocks get() { return i; }
	public DeriusBlocks() { i = this; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	// We store it here instead of in the Mixin,
	// to not be core dependent.
	public static final Set<Material> LISTEN_FOR = new HashSet<>();
	
	// -------------------------------------------- //
	// OVERRIDE: PLUGIN
	// -------------------------------------------- //
	
	@Override
	public void onEnable()
	{
		if ( ! this.preEnable()) return;
	
		// Initialise database
		MChunkColl.get().init();
		
		// Activate engine
		EngineBlocks.get().activate();
		
		// Inject mixin
		this.integrate(IntegrationDerius.get());
		
		if ( ! IntegrationDerius.get().isActivated())
		{
			Level level = Level.WARNING;
			this.log(level, "Derius-Blocks is enabled without Derius-Core, this is possible wihout any issues.");
			this.log(level, "Derius-Blocks does however not server any purpose and is now completely useless.");
		}
		
		this.postEnable();
	}
	
	
	@Override
	public void onDisable()
	{
		EngineBlocks.get().deactivate();
		super.onDisable();
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static String getChunkId(final PS ps)
	{
		if (ps == null) throw new IllegalArgumentException("ps is null");
		if (ps.getWorld(true) == null) throw new IllegalArgumentException("ps must include a world");
		final PS chunk = ps.getChunk(true);
		
		if (chunk.getChunkX() == null || chunk.getChunkZ() == null) throw new IllegalArgumentException("passed ps does not have chunk info");
		
		PSFormat formatter = PSFormatFormal.get();
		return chunk.toString(formatter);
	}
	
}
