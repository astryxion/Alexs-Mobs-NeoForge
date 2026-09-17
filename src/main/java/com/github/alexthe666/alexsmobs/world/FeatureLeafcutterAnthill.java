package com.github.alexthe666.alexsmobs.world;

import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityLeafcutterAnt;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityLeafcutterAnthill;
import com.mojang.serialization.MapCodec;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.Feature;

public class FeatureLeafcutterAnthill implements Feature {
   public static final MapCodec<FeatureLeafcutterAnthill> CODEC = MapCodec.unit(FeatureLeafcutterAnthill::new);

   public MapCodec<? extends Feature> codec() {
      return CODEC;
   }

   public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
      return this.placeAnthill(level, origin);
   }

   private boolean placeAnthill(WorldGenLevel level, BlockPos pos) {
      if (level.getRandom().nextFloat() > 0.0175F) {
         return false;
      } else {
         int x = 8;
         int z = 8;
         int y = level.getHeight(Types.WORLD_SURFACE_WG, pos.getX() + x, pos.getZ() + z);
         BlockPos heightPos = new BlockPos(pos.getX() + x, y, pos.getZ() + z);
         if (!level.getFluidState(heightPos.below()).isEmpty()) {
            return false;
         } else {
            int outOfGround = 2 + level.getRandom().nextInt(2);

            for(int i = 0; i < outOfGround; ++i) {
               float size = (float)(outOfGround - i);
               int lvt_8_1_ = (int)(Math.floor((double)size) * (double)level.getRandom().nextFloat()) + 2;
               int lvt_10_1_ = (int)(Math.floor((double)size) * (double)level.getRandom().nextFloat()) + 2;
               float radius = (float)(lvt_8_1_ + lvt_10_1_) * 0.333F;

               for(BlockPos lvt_13_1_ : BlockPos.betweenClosed(heightPos.offset(-lvt_8_1_, 0, -lvt_10_1_), heightPos.offset(lvt_8_1_, 3, lvt_10_1_))) {
                  if (lvt_13_1_.distSqr(heightPos) <= (double)(radius * radius)) {
                     BlockState block = Blocks.COARSE_DIRT.defaultBlockState();
                     if (level.getRandom().nextFloat() < 0.2F) {
                        block = Blocks.DIRT.defaultBlockState();
                     }

                     level.setBlock(lvt_13_1_, block, 4);
                  }
               }
            }

            Random chunkSeedRandom = new Random(pos.asLong());
            outOfGround -= chunkSeedRandom.nextInt(1) + 1;
            heightPos = heightPos.offset(-chunkSeedRandom.nextInt(2), 0, -chunkSeedRandom.nextInt(2));
            if (level.getBlockState(heightPos.above(outOfGround + 1)).getBlock() != AMBlockRegistry.LEAFCUTTER_ANTHILL.get() && level.getBlockState(heightPos.above(outOfGround - 1)).getBlock() != AMBlockRegistry.LEAFCUTTER_ANTHILL.get()) {
               level.setBlock(heightPos.above(outOfGround), ((Block)AMBlockRegistry.LEAFCUTTER_ANTHILL.get()).defaultBlockState(), 4);
               BlockEntity tileentity = level.getBlockEntity(heightPos.above(outOfGround));
               if (tileentity instanceof TileEntityLeafcutterAnthill) {
                  TileEntityLeafcutterAnthill beehivetileentity = (TileEntityLeafcutterAnthill)tileentity;
                  int j = 3 + chunkSeedRandom.nextInt(3);
                  if (beehivetileentity.hasNoAnts()) {
                     for(int k = 0; k < j; ++k) {
                        EntityLeafcutterAnt beeentity = new EntityLeafcutterAnt((EntityType)AMEntityRegistry.LEAFCUTTER_ANT.get(), level.getLevel());
                        beeentity.setQueen(k == 0);
                        beehivetileentity.tryEnterHive(beeentity, false, level.getRandom().nextInt(599));
                     }
                  }
               }

               if (level.getRandom().nextBoolean()) {
                  level.setBlock(heightPos.above(outOfGround).north(), Blocks.COARSE_DIRT.defaultBlockState(), 4);
                  level.setBlock(heightPos.above(outOfGround - 1).north(), Blocks.COARSE_DIRT.defaultBlockState(), 4);
                  level.setBlock(heightPos.above(outOfGround - 2).north(), Blocks.COARSE_DIRT.defaultBlockState(), 4);
               }

               if (level.getRandom().nextBoolean()) {
                  level.setBlock(heightPos.above(outOfGround).east(), Blocks.COARSE_DIRT.defaultBlockState(), 4);
                  level.setBlock(heightPos.above(outOfGround - 1).east(), Blocks.COARSE_DIRT.defaultBlockState(), 4);
                  level.setBlock(heightPos.above(outOfGround - 2).east(), Blocks.COARSE_DIRT.defaultBlockState(), 4);
               }

               if (level.getRandom().nextBoolean()) {
                  level.setBlock(heightPos.above(outOfGround).south(), Blocks.COARSE_DIRT.defaultBlockState(), 4);
                  level.setBlock(heightPos.above(outOfGround - 1).south(), Blocks.COARSE_DIRT.defaultBlockState(), 4);
                  level.setBlock(heightPos.above(outOfGround - 2).south(), Blocks.COARSE_DIRT.defaultBlockState(), 4);
               }

               if (level.getRandom().nextBoolean()) {
                  level.setBlock(heightPos.above(outOfGround).west(), Blocks.COARSE_DIRT.defaultBlockState(), 4);
                  level.setBlock(heightPos.above(outOfGround - 1).west(), Blocks.COARSE_DIRT.defaultBlockState(), 4);
                  level.setBlock(heightPos.above(outOfGround - 2).west(), Blocks.COARSE_DIRT.defaultBlockState(), 4);
               }

               for(int airs = 1; airs < 3; ++airs) {
                  level.setBlock(heightPos.above(outOfGround + airs), Blocks.AIR.defaultBlockState(), 4);
               }
            }

            int i = outOfGround;
            int down = level.getRandom().nextInt(2) + 1;

            while(i > -down) {
               --i;
               level.setBlock(heightPos.above(i), ((Block)AMBlockRegistry.LEAFCUTTER_ANT_CHAMBER.get()).defaultBlockState(), 4);
            }

            float size = (float)(chunkSeedRandom.nextInt(1) + 1);
            int lvt_8_1_ = (int)(Math.floor((double)size) * (double)level.getRandom().nextFloat()) + 1;
            int lvt_9_1_ = (int)(Math.floor((double)size) * (double)level.getRandom().nextFloat()) + 1;
            int lvt_10_1_ = (int)(Math.floor((double)size) * (double)level.getRandom().nextFloat()) + 1;
            float radius = (float)(lvt_8_1_ + lvt_9_1_ + lvt_10_1_) * 0.333F + 0.5F;
            heightPos = heightPos.below(down + lvt_9_1_).offset(chunkSeedRandom.nextInt(2), 0, chunkSeedRandom.nextInt(2));

            for(BlockPos lvt_13_1_ : BlockPos.betweenClosed(heightPos.offset(-lvt_8_1_, -lvt_9_1_, -lvt_10_1_), heightPos.offset(lvt_8_1_, lvt_9_1_, lvt_10_1_))) {
               if (lvt_13_1_.distSqr(heightPos) < (double)(radius * radius)) {
                  BlockState block = ((Block)AMBlockRegistry.LEAFCUTTER_ANT_CHAMBER.get()).defaultBlockState();
                  level.setBlock(lvt_13_1_, block, 4);
               }
            }

            return true;
         }
      }
   }
}
