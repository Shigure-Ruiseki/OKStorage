package ruiseki.okstorage.config;

import com.gtnewhorizon.gtnhlib.config.Config;
import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;

import ruiseki.okstorage.Reference;

@Config.LangKey("config.generalConfig")
@Config(modid = Reference.MOD_ID, configSubDirectory = Reference.MOD_ID, category = "general")
public class ModConfig {

    public static void registerConfig() throws ConfigException {
        ConfigurationManager.registerConfig(ModConfig.class);
    }

    @Config.DefaultInt(27)
    @Config.RangeInt(min = 1)
    public static int basicStorageSlots;

    @Config.DefaultInt(54)
    @Config.RangeInt(min = 1)
    public static int ironStorageSlots;

    @Config.DefaultInt(81)
    @Config.RangeInt(min = 1)
    public static int goldStorageSlots;

    @Config.DefaultInt(108)
    @Config.RangeInt(min = 1)
    public static int diamondStorageSlots;

    @Config.DefaultInt(120)
    @Config.RangeInt(min = 1)
    public static int obsidianStorageSlots;

    @Config.DefaultInt(1)
    @Config.RangeInt(min = 1)
    public static int basicUpgradeSlots;

    @Config.DefaultInt(2)
    @Config.RangeInt(min = 1)
    public static int ironUpgradeSlots;

    @Config.DefaultInt(3)
    @Config.RangeInt(min = 1)
    public static int goldUpgradeSlots;

    @Config.DefaultInt(5)
    @Config.RangeInt(min = 1)
    public static int diamondUpgradeSlots;

    @Config.DefaultInt(7)
    @Config.RangeInt(min = 1)
    public static int obsidianUpgradeSlots;

    @Config.DefaultDouble(1.5)
    @Config.RangeDouble(min = 0.1)
    public static double stackUpgradeStarterMul;

    @Config.DefaultDouble(2)
    @Config.RangeDouble(min = 1)
    public static double stackUpgradeTier1Mul;

    @Config.DefaultDouble(4)
    @Config.RangeDouble(min = 1)
    public static double stackUpgradeTier2Mul;

    @Config.DefaultDouble(8)
    @Config.RangeDouble(min = 1)
    public static double stackUpgradeTier3Mul;

    @Config.DefaultDouble(16)
    @Config.RangeDouble(min = 1)
    public static double stackUpgradeTier4Mul;

    @Config.DefaultDouble(33554431)
    @Config.RangeDouble(min = 1)
    public static double stackUpgradeTierOmegaMul;

    @Config.DefaultInt(5)
    public static int magnetRange;
}
