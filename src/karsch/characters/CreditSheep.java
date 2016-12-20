package karsch.characters;

import karsch.Values;
import karsch.level.LevelMap;
import karsch.resources.ModelCache;

import com.jme.animation.SpatialTransformer;
import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;

@SuppressWarnings("serial")
public class CreditSheep extends CharacterBase{
	private SheepCreditsController scController;
	
	public CreditSheep(int x, int y, LevelMap levelMap) {
		super(x, y, levelMap, "sheep");
		model = ModelCache.getInstance().get("sheep1.3ds");
		trans = (SpatialTransformer) model.getController(0);
		trans.setActive(true);
		trans.setRepeatType(Controller.RT_WRAP);
        trans.setSpeed(FastMath.nextRandomInt(28, 32) * Values.getInstance().getSpeed());
        trans.setMaxTime(19);
		model.setLocalScale(.06f);
		
		model.setLocalTranslation(new Vector3f(0,-2.5f,-2.5f));
		
		setLocalTranslation(new Vector3f(x*5+2.5f, 0, y*5+2.5f));
		attachChild(model);
		
		attachChild(new Shadow(1.5f,1.5f));
		
		scController = new SheepCreditsController(this, 15);
		scController.setSpeed(2 / Values.getInstance().getSpeed());
		addController(scController);
		
		lookAt(getLocalTranslation().clone().add(-1,0,0), Vector3f.UNIT_Y);
		
//		addSound("sheep", 1, 3, 4);
		
//		balloon = new SpeechBalloon();
//		attachChild(balloon);
		
		setModelBound(new BoundingBox());
		updateModelBound();
		
	}
	
	public void setPause(boolean pause){
		scController.setActive(!pause);
		trans.setActive(!pause);
		
	}
}


@SuppressWarnings("serial")
class SheepCreditsController extends Controller {
	private float time=0, rpf=0;
	private CharacterBase character;
	
	public SheepCreditsController(CharacterBase character, int fields) {
		this.character = character;
		setRepeatType(RT_WRAP);
		setMinTime(0);
		setMaxTime(5*fields);
		time = getMinTime();
	}

	@Override
	public void update(float tpf) {
		rpf = 5f * tpf / getSpeed();

		time += rpf;
		character.getLocalTranslation().x -= rpf;
		
		if (character.getLocalTranslation().x < (5*5)){
			character.setLocalTranslation(new Vector3f(
					18 * 5 + 2.5f, 0, character.getY() * 5 + 2.5f));
		}
	}

}
