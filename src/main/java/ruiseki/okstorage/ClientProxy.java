package ruiseki.okstorage;

import net.minecraftforge.common.MinecraftForge;

import ruiseki.okcore.init.ModBase;
import ruiseki.okcore.proxy.ClientProxyComponent;
import ruiseki.okstorage.client.audio.JukeboxSoundManager;

public class ClientProxy extends ClientProxyComponent {

    public ClientProxy() {
        super(new CommonProxy());
    }

    @Override
    public ModBase getMod() {
        return OKStorage.instance;
    }

    @Override
    public void registerEventHooks() {
        super.registerEventHooks();
        MinecraftForge.EVENT_BUS.register(JukeboxSoundManager.getInstance());
    }
}
