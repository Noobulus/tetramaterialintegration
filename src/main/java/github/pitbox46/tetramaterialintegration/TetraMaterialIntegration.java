package github.pitbox46.tetramaterialintegration;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("tetramaterialintegration")
public class TetraMaterialIntegration {
    public static Logger LOGGER = LogManager.getLogger("tetramaterialintegration");

    public TetraMaterialIntegration() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(Config.class);
    }
}
