package github.pitbox46.tetramaterialintegration;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Mod("tetramaterialintegration")
public class TetraMaterialIntegration {
    public static Logger LOGGER = LogManager.getLogger();

    public TetraMaterialIntegration() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(Config.class);
    }
}
