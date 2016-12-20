package karsch.effects;

import karsch.Values;
import karsch.characters.Karsch;
import karsch.level.LevelManager;
import karsch.resources.ModelCache;
import karsch.resources.TextureCache;
import karsch.sound.SoundManager;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.BillboardNode;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.scene.state.ZBufferState.TestFunction;
import com.jme.system.DisplaySystem;

@SuppressWarnings("serial")
public class Pow extends BillboardNode{
	private MaterialState ms;
	private PowController powController;
	private Quad quad;
	private int type;
	private Node ghost;
	
	public static final int TYPE_COWPOW = 1;
	public static final int TYPE_SPIKEOUCH = 2;
	
	public Pow(Karsch karsch, int type) {
		super("pow");
		this.type = type;
		quad = new Quad("pow1",9,9);
		
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
	    
		if (type == TYPE_COWPOW){
			ts.setTexture(TextureCache.getInstance().getTexture("POW1.PNG"),0);
		} else if (type == TYPE_SPIKEOUCH){
			ts.setTexture(TextureCache.getInstance().getTexture("OUCH1.PNG"),0);
		}

	    quad.setRenderState(ts);
	    
	    BlendState bs = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        bs.setBlendEnabled(true);
        bs.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        bs.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
        bs.setTestEnabled(true);
        bs.setTestFunction(BlendState.TestFunction.GreaterThan);
        bs.setReference(0.1f);

        bs.setEnabled(true);
        
        ZBufferState zstate = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
	    zstate.setEnabled(true);
	    zstate.setWritable(true);
	    zstate.setFunction(TestFunction.Always);
        
        ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
        ms.setDiffuse(ColorRGBA.white);
        ms.setAmbient(ColorRGBA.white);
        quad.setRenderState(ms);
        quad.setRenderState(zstate);
	    
        quad.setRenderState(bs);
	    
	    attachChild(quad);
	    
	    updateGeometricState(0, false);
        updateRenderState();
	    
	    powController = new PowController();
		quad.addController(powController);
		
		setAlignment(SCREEN_ALIGNED);
		
		setLocalTranslation(0, 3, 0);
		
		karsch.attachChild(this);
		
		ghost = ModelCache.getInstance().get("karschghost.3ds");
		ghost.setLocalScale(.008f);
		ghost.setModelBound(new BoundingBox());
		ghost.setLocalTranslation(0,0,0);
		ghost.updateModelBound();
		ghost.lookAt(new Vector3f(0,1,0), Vector3f.UNIT_Z);
		karsch.attachChild(ghost);
		karsch.updateRenderState();
	}
	
	public void setActive(){
		powController.setActive(true);
		Values.getInstance().getHudPass().setEnabled(false);
		
		if (type == TYPE_COWPOW){
			SoundManager.getInstance().playSoundOnce("cow1.ogg");
			SoundManager.getInstance().playSoundOnce("scream1.ogg");
		} else if (type == TYPE_SPIKEOUCH){
			SoundManager.getInstance().playSoundOnce("scream2.ogg");
		}
	}
	
	public class PowController extends Controller{
		private float time = 0;
		public PowController() {
			setMinTime(0);
			setMaxTime(5);
			setSpeed(2f);
			setActive(false);
		}
		
		@Override
		public void update(float tpf) {
			
			time += tpf*getSpeed();
			if (time <= getMaxTime()){
				if (time < getMaxTime()/5){
					ms.getDiffuse().a = 4*FastMath.sin(time*5 / getMaxTime());
					setLocalScale(FastMath.sin(time*10 / getMaxTime()));
				} else {
					Values.getInstance().getLevelGameState().setPause(true);
				}
				
				ghost.getLocalTranslation().y += tpf*10;
			} else {
				setActive(false);
				LevelManager.getInstance().restartLevel();
			}
		}
	}
}




