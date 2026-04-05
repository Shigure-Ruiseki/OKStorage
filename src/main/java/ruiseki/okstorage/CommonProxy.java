package ruiseki.okstorage;

import ruiseki.okcore.init.ModBase;
import ruiseki.okcore.network.PacketHandler;
import ruiseki.okcore.proxy.CommonProxyComponent;
import ruiseki.okstorage.common.network.PacketJukeboxPlaybackState;
import ruiseki.okstorage.common.network.PacketJukeboxPositionUpdate;
import ruiseki.okstorage.common.network.PacketRecordDuration;

public class CommonProxy extends CommonProxyComponent {

    @Override
    public ModBase getMod() {
        return OKStorage.instance;
    }

    @Override
    public void registerPacketHandlers(PacketHandler packetHandler) {
        super.registerPacketHandlers(packetHandler);
        packetHandler.register(PacketJukeboxPlaybackState.class);
        packetHandler.register(PacketJukeboxPositionUpdate.class);
        packetHandler.register(PacketRecordDuration.class);
    }
}
