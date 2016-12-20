package karsch.items;

import karsch.Values;
import karsch.characters.CharacterBase;
import karsch.characters.Shadow;
import karsch.effects.ParticleEffect;
import karsch.interfaces.Collectable;
import karsch.interfaces.KarschPassable;
import karsch.level.Freefield;
import karsch.level.LevelMap;
import karsch.resources.ModelCache;
import karsch.sound.SoundManager;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;
import com.jme.scene.Node;

@SuppressWarnings("serial")
public class Key extends CharacterBase implements Collectable, KarschPassable{
	private ParticleEffect particleEffect;
	private KeyController keyRot;
	public Key(int x, int y, LevelMap levelMap){
		super(x, y, levelMap, "key");
		model = ModelCache.getInstance().get("key.3ds");

        model.setLocalScale(.020f);
        model.setModelBound(new BoundingBox());
        model.setLocalTranslation(0,1.5f,0);
        model.updateModelBound();
        
        attachChild(model);
        attachChild(new Shadow(1.0f,1.0f));

        particleEffect = new ParticleEffect(5, "STAR1.PNG");        
        
        setLocalTranslation(new Vector3f(x*5+2.5f, 0, y*5+2.5f));
        
        keyRot = new KeyController(this);
		keyRot.setMaxTime(30);
		keyRot.setSpeed(.1f);
		keyRot.setActive(true);
		addController(keyRot);
	}

//	@Override
	public void collect(Node source) {
		SoundManager.getInstance().playSoundOnce("twinkle.ogg");

		Values values = Values.getInstance();
		values.getLevelGameState().getKarsch().getController().setActField(new Freefield());
		values.getHudState().displayTextTime("You found a key, maybe you can open a door with it", 1000);
		particleEffect.setActive(true);
		
		values.getLevelGameState().getLevel().getNpcNode().detachChild(this);
		
		int keys = values.getKeys();
		values.setKeys(keys+1);
		values.getHudState().setKeys(keys+1);
		
		try {
			finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public class KeyController extends Controller{
		private Key key;
		private float time;
		public KeyController(Key key) {
			this.key = key;
			setRepeatType(RT_WRAP);
		}
		
		@Override
		public void update(float tpf) {
			if ( (time += tpf * getSpeed()) <= getMaxTime()){
				float rot = FastMath.LERP(time/getMaxTime(), 0, 360);
				key.getLocalRotation().fromAngleAxis(rot , Vector3f.UNIT_Y);

			} else {
				time = getMinTime();
			}
		}
	}

//	@Override
	public boolean canPass() {
		return true;
	}
}
