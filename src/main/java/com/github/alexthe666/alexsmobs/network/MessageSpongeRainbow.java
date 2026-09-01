package com.github.alexthe666.alexsmobs.network;

import com.github.alexthe666.alexsmobs.entity.util.RainbowUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Client -> Server: {@link net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickEmpty}
 * never runs on the dedicated server, so self-sponge rainbow removal must be requested explicitly.
 */
public record MessageSpongeRainbow() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MessageSpongeRainbow> ID =
        new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("alexsmobs", "sponge_rainbow"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageSpongeRainbow> CODEC = new StreamCodec<>() {
        @Override
        public MessageSpongeRainbow decode(RegistryFriendlyByteBuf buf) {
            return new MessageSpongeRainbow();
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, MessageSpongeRainbow packet) {
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

    public static void handleServer(MessageSpongeRainbow payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player == null) {
                return;
            }
            if (RainbowUtil.tryWashOffRainbow(player, player.getItemInHand(InteractionHand.MAIN_HAND))) {
                return;
            }
            RainbowUtil.tryWashOffRainbow(player, player.getItemInHand(InteractionHand.OFF_HAND));
        });
    }
}
