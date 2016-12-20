package karsch.level.tiles;

import java.awt.image.BufferedImage;

import karsch.Values;
import karsch.characters.Karsch;
import karsch.controller.KarschController;
import karsch.interfaces.Collectable;
import karsch.interfaces.KarschPassable;
import karsch.level.LevelManager;
import karsch.level.LevelText;
import karsch.resources.ModelCache;

import com.jme.animation.SpatialTransformer;
import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;
import com.jme.scene.Node;

@SuppressWarnings("serial")
public class LevelEntrance extends Tile implements KarschPassable, Collectable{
	private int levelOffset;
	private int direction = KarschController.DIRECTION_NODIR;
	private boolean isStartPos = false;
	public LevelEntrance(int x, int y, BufferedImage levelImage, int levelOffset, LevelText levelText, Vector3f center, boolean isStartPos) {
		super("leveloffset " + levelOffset, x ,y);
		this.isStartPos = isStartPos;
		this.levelOffset = levelOffset;
		
		if (levelOffset > 0){
			direction = levelText.getRotationExit();
		} else {
			direction = levelText.getRotationEntrance();
		}
		
		if (!isStartPos){
			model = ModelCache.getInstance().get("sign1.3ds");
			model.setLocalScale(.05f);
			attachChild(model);
			setModelBound(new BoundingBox());
			updateModelBound();
			
			SpatialTransformer trans = (SpatialTransformer) model.getController(0);
			trans.setRepeatType(Controller.RT_WRAP);
			trans.setActive(true);
			
			if (direction == KarschController.DIRECTION_RIGHT) {
				getLocalRotation().fromAngleAxis(FastMath.PI+0.2f, Vector3f.UNIT_Y);
				setLocalTranslation(x*5+4.5f,-.5f,y*5+1.0f);
			} else if (direction == KarschController.DIRECTION_LEFT) {
				setLocalTranslation(x*5+0.5f,-.5f,y*5+1.0f);
				getLocalRotation().fromAngleAxis(-0.2f, Vector3f.UNIT_Y);
			} else if (direction == KarschController.DIRECTION_DOWN) {
				setLocalTranslation(x*5+4.5f,-.5f,y*5+4.5f);
				getLocalRotation().fromAngleAxis(FastMath.HALF_PI+0.3f, Vector3f.UNIT_Y);
			} else if(direction == KarschController.DIRECTION_UP) {
				getLocalRotation().fromAngleAxis(-FastMath.HALF_PI+0.3f, Vector3f.UNIT_Y);
				setLocalTranslation(x*5+4.5f,-.5f,y*5+0.5f);
			}
		} else {
			setLocalTranslation(x*5+4.5f,-.5f,y*5+4.5f);
			getLocalRotation().fromAngleAxis(FastMath.HALF_PI+0.3f, Vector3f.UNIT_Y);
		}
		
	}
	
//	public LevelEntrance(int x, int y, BufferedImage levelImage, int levelOffset, Vector3f center) {
//		this(x, y, levelImage, levelOffset, center, false);
//	}

//	@Override
	public void collect(Node source) {
		if (source instanceof Karsch && !isStartPos) {
			if (levelOffset > 0){
				System.out.println("ENTRANCE: level +" + levelOffset);
			} else {
				System.out.println("ENTRANCE: level " + levelOffset);
			}
			
			Karsch karsch = Values.getInstance().getLevelGameState().getKarsch();
			karsch.setBabiesFound(0);
			LevelManager.getInstance().loadLevel(levelOffset, this);
			
			karsch.getController().setLastDirection(direction);	

		}
	}
	
	public Vector3f getDirectionVector(){
		if (direction == KarschController.DIRECTION_RIGHT)
			return new Vector3f(-1,0,0);
		else if (direction == KarschController.DIRECTION_LEFT)
			return new Vector3f(1,0,0);
		else if (direction == KarschController.DIRECTION_DOWN)
			return new Vector3f(0,0,-1);
		else
			return new Vector3f(0,0,1);
	}
	
	public int getDirection(){
		return direction;
	}

//	@Override
	public boolean canPass() {
		return true;
	}

	public int getLevelOffset() {
		return levelOffset;
	}

}
