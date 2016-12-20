package karsch.characters;

import karsch.Values;
import karsch.effects.ParticleEffect;
import karsch.interfaces.Collectable;
import karsch.interfaces.KarschPassable;
import karsch.level.Freefield;
import karsch.level.LevelMap;
import karsch.resources.ModelCache;
import karsch.sound.SoundManager;

import com.jme.animation.SpatialTransformer;
import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.scene.Controller;
import com.jme.scene.Node;

@SuppressWarnings("serial")
public class Pig extends CharacterBase implements KarschPassable, Collectable{
	private ParticleEffect particleEffect;
	public Pig(int x, int y, LevelMap levelMap){
		super(x, y, levelMap, "babypig");
		model = ModelCache.getInstance().get("babypig.3ds");
		trans = (SpatialTransformer) model.getController(0);
		trans.setRepeatType(Controller.RT_WRAP);
		trans.setSpeed(0.5f + FastMath.rand.nextFloat());

        model.setLocalScale(.004f);
        model.setModelBound(new BoundingBox());
        model.setLocalTranslation(0,.5f,0);
        model.updateModelBound();
        
        attachChild(model);
        attachChild(new Shadow(1.5f,1.5f));
        
        particleEffect = new ParticleEffect(5, "HEART.PNG");
        
        addSound("pig", 1, 3, 5);
	}

//	@Override
	public void collect(Node source) {
		particleEffect.setActive(true);
		if (track != null)
			track.stop();
		SoundManager.getInstance().playSoundOnce("pig_juhu.ogg");
		Values values = Values.getInstance();
		values.getLevelGameState().getLevel().getNpcNode().detachChild(this);
		values.getLevelGameState().getKarsch().getController().setActField(new Freefield());
		if (audioController != null)
			removeController(audioController);
		int babies = values.getBabies();
		values.setBabies(++babies);
		values.getHudState().setBabies(babies);
		values.getHudState().displayTextTime("You rescued a baby, congratulations!", 2000);
		
		Karsch karsch = values.getLevelGameState().getKarsch();
		karsch.incBabiesFound();
	}

//	@Override
	public boolean canPass() {
		return true;
	}
}
