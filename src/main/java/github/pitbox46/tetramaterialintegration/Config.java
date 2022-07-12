package github.pitbox46.tetramaterialintegration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Config {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static File jsonFile;
    public static String materialTagName = "";
    public static Map<String, ArrayList<String>> specialToolParts = new HashMap<>();
    public static Map<String, String> tConstructMatCompat = new HashMap<>();

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        Config.init(event.getServer().getWorldPath(new LevelResource("serverconfig")));
    }

    public static void init(Path folder) {
        jsonFile = new File(FileUtils.getOrCreateDirectory(folder, "serverconfig").toFile(), "tetramaterialintegration.json");
        try {
            if (jsonFile.createNewFile()) {
                Path defaultConfigPath = FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()).resolve("tetramaterialintegration.json");
                if(defaultConfigPath.toFile().exists())
                    Files.copy(defaultConfigPath, jsonFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                else {
                    JsonObject defaultJsonObject = new JsonObject();
                    defaultJsonObject.addProperty("materialTagName", "tetraMaterial");
                    defaultJsonObject.add("specialTools", new JsonObject());
                    InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("assets/tetramaterialintegration/TConstructMatCompat.json")));
                    JsonObject defaultTConstructMatCompat = GSON.fromJson(reader, JsonObject.class);
                    defaultJsonObject.add("tConstructMaterialCompat", defaultTConstructMatCompat);
                    Files.write(jsonFile.toPath(), GSON.toJson(defaultJsonObject).getBytes(), StandardOpenOption.WRITE);
                }
            }
        } catch (IOException error) {
            LOGGER.warn(error.getMessage());
        }

        readConfig(jsonFile);
    }

    public static void readConfig(File file) {
        try (Reader reader = new FileReader(file)) {
            JsonObject json = GSON.fromJson(reader, JsonObject.class);
            materialTagName = json.getAsJsonPrimitive("materialTagName").getAsString();
            specialToolParts = GSON.fromJson(json.get("specialTools"), new TypeToken<Map<String, ArrayList<String>>>(){}.getType());
            tConstructMatCompat = GSON.fromJson(json.get("tConstructMaterialCompat"), new TypeToken<Map<String, String>>(){}.getType());
        } catch (IOException e) {
            LOGGER.warn(e);
            LOGGER.warn("Config did not read properly. It is suggested to delete the config and allow a new one to be created");
            materialTagName = "";
            specialToolParts = new HashMap<>();
        }
    }
}
