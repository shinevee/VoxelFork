package io.bluestaggo.voxelthing.gui;

import io.bluestaggo.voxelthing.Game;
import io.bluestaggo.voxelthing.assets.Texture;
import io.bluestaggo.voxelthing.math.MathUtil;
import io.bluestaggo.voxelthing.renderer.MainRenderer;
import io.bluestaggo.voxelthing.renderer.draw.Quad;
import io.bluestaggo.voxelthing.world.BlockRaycast;
import io.bluestaggo.voxelthing.world.Direction;
import io.bluestaggo.voxelthing.world.block.Block;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_9;

public class IngameGui extends GuiScreen {
	private static final List<Block> blocks = new ArrayList<>();

	private int heldIndex = 0;
	private int[] prevHoverProgress = new int[9];
	private int[] hoverProgress = new int[9];

	static {
		for (short i = 0; i < 9; i++) {
			Block block = Block.fromId(i);
			if (block != null) {
				blocks.add(block);
			}
		}
	}

	public IngameGui(Game game) {
		super(game);
	}

	@Override
	protected void onKeyPressed(int key) {
		if (key >= GLFW_KEY_1 && key <= GLFW_KEY_9) {
			heldIndex = key - GLFW_KEY_1;
		}
	}

	@Override
	public void tick() {
		for (int i = 0; i < 9; i++) {
			int hover = hoverProgress[i];
			prevHoverProgress[i] = hover;

			if (i == heldIndex) {
				hover++;
			} else {
				hover--;
			}

			hover = MathUtil.clamp(hover, 0, Game.TICKS_PER_SECOND / 4);
			hoverProgress[i] = hover;
		}
	}

	@Override
	public void draw() {
		MainRenderer r = game.renderer;

		Texture hotbarTexture = r.textures.getTexture("/assets/gui/hotbar.png");
		Texture blocksTexture = r.textures.getTexture("/assets/blocks.png");
		int slotWidth = hotbarTexture.width / 2;
		int slotHeight = hotbarTexture.height;

		float startX = (r.screen.getWidth() - slotWidth * 9) / 2.0f;
		float startY = r.screen.getHeight() - slotHeight - 5;

		var hotbarQuad = new Quad()
				.at(startX, startY)
				.size(slotWidth, slotHeight)
				.withTexture(hotbarTexture)
				.withUV(0.0f, 0.0f, 0.5f, 1.0f);
		var blockQuad = new Quad()
				.at(startX + (slotWidth - 16) / 2.0f, startY + (slotHeight - 16) / 2.0f)
				.size(16, 16)
				.withTexture(blocksTexture);

		for (int i = 0; i < 9; i++) {
			Block block = i < blocks.size() ? blocks.get(i) : null;

			float hover = MathUtil.lerp(prevHoverProgress[i], hoverProgress[i], (float) game.getPartialTick()) / (Game.TICKS_PER_SECOND / 4.0f);
			hover = MathUtil.squareOut(hover);
			hover *= slotHeight / 4.0f;

			hotbarQuad.offset(0, -hover);
			blockQuad.offset(0, -hover);

			float slotOffset = i == heldIndex ? 0.5f : 0.0f;
			r.draw2D.drawQuad(hotbarQuad.withUV(slotOffset, 0.0f, 0.5f + slotOffset, 1.0f));

			if (block != null) {
				Vector2i texture = block.getTexture().get(Direction.NORTH, null, 0, 0, 0);

				float minU = blocksTexture.uCoord(texture.x * 16);
				float minV = blocksTexture.vCoord(texture.y * 16);
				float maxU = minU + blocksTexture.uCoord(16);
				float maxV = minV + blocksTexture.vCoord(16);

				r.draw2D.drawQuad(blockQuad.withUV(minU, minV, maxU, maxV));
			}

			hotbarQuad.offset(slotWidth, hover);
			blockQuad.offset(slotWidth, hover);
		}

		Texture crosshairTexture = r.textures.getTexture("/assets/gui/crosshair.png");
		r.draw2D.drawQuad(Quad.shared()
				.at((r.screen.getWidth() - crosshairTexture.width) / 2.0f, (r.screen.getHeight() - crosshairTexture.height) / 2.0f)
				.withTexture(crosshairTexture));
	}

	private Block getPlacedBlock() {
		if (heldIndex < 0 || heldIndex >= blocks.size()) {
			return null;
		}
		return blocks.get(heldIndex);
	}

	@Override
	protected void onMouseClicked(int button) {
		BlockRaycast raycast = game.getBlockRaycast();
		if (raycast.blockHit()) {
			int x = raycast.getHitX();
			int y = raycast.getHitY();
			int z = raycast.getHitZ();
			Direction face = raycast.getHitFace();

			if (button == 0) {
				game.world.setBlockId(x, y, z, (short) 0);
			} else if (button == 1) {
				Block placedBlock = getPlacedBlock();
				if (placedBlock != null) {
					game.world.setBlock(x + face.X, y + face.Y, z + face.Z, placedBlock);
				}
			}
		}
	}
}
