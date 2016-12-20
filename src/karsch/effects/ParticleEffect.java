package karsch.effects;

import karsch.Values;
import karsch.resources.TextureCache;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Controller;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jmex.effects.particles.ParticleController;
import com.jmex.effects.particles.ParticleMesh;

@SuppressWarnings("serial")
public class ParticleEffect extends ParticleMesh{
	private ParticleController controller;
	private ParticleMesh particles;
	private TimeController timeController;
	public ParticleEffect(float time, String image, int number, Vector3f position, float degrees) {
		super("particles", number, ParticleType.Quad);
		particles = this;
        controller = new ParticleController(this);
        controller.setActive(false);
	    controller.setSpeed(0.1f);
	    controller.setControlFlow(true);
        addController(controller);
		
		DisplaySystem display = DisplaySystem.getDisplaySystem();
		
	    TextureState ts = display.getRenderer().createTextureState();
	    
	    ts.setTexture(TextureCache.getInstance().getTexture(image));
	    ts.setEnabled(true);
		
		BlendState as1 = display.getRenderer().createBlendState();
	    as1.setBlendEnabled(true);
	    as1.setConstantColor(new ColorRGBA(.5f,.5f,.5f,.5f));
	    as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
	    as1.setDestinationFunction(BlendState.DestinationFunction.One);

	    as1.setTestEnabled(true);
	    as1.setTestFunction(BlendState.TestFunction.GreaterThan);
	    as1.setEnabled(true);
	    as1.setBlendEnabled(true);
		
	    setEmissionDirection(new Vector3f(0.0f, 1.0f, 0.0f));
	    setMaximumAngle(FastMath.DEG_TO_RAD*degrees);
	    
	    setMinimumLifeTime(10.0f);
	    setMaximumLifeTime(50.0f);
	    setStartSize(.5f);
	    setEndSize(2f);

	    setStartColor(new ColorRGBA(1.0f, 0.612f, 0.121f, 0.9f));
	    setEndColor(new ColorRGBA(1.0f, 0.612f, 0.121f, 0.0f));
	    
	    setInitialVelocity(0.10f);
	    
	    setLightCombineMode(LightCombineMode.Off);
	    setTextureCombineMode(TextureCombineMode.Replace);
	    setParticlesInWorldCoords(true);
	    
	    ZBufferState zstate = display.getRenderer().createZBufferState();
	    zstate.setEnabled(true);
	    zstate.setWritable(false);
	    
	    setRenderState(ts);
	    setRenderState(as1);
	    setRenderState(zstate);
	    updateRenderState();
	    
	    setModelBound(new BoundingBox());
	    updateModelBound();
	    
	    setLocalTranslation(position);
	    
	    timeController = new TimeController(time);
	    
	    addController(timeController);
	}
	
	public ParticleEffect(float time, String image, Vector3f position) {
		this(time, image, 150, position, 45);
	}
	
	public ParticleEffect(float time, String image, float degrees) {
		this(time, image, 150, new Vector3f(0, 4, 0), degrees);
	}
	
	public ParticleEffect(float time, String image) {
		this(time, image, 150, new Vector3f(0, 4, 0), 45);
	}
	
	public void setActive(boolean active){
		if (active){
			Values.getInstance().getLevelGameState().getKarsch().attachChild(particles);
		}
		controller.setActive(active);
		timeController.setActive(active);
	}

	public class TimeController extends Controller{
		private float time = 0;
		public TimeController(float maxTime) {
			setRepeatType(RT_CLAMP);
			setMinTime(time);
			setMaxTime(maxTime);
			setActive(false);
		}
		
		@Override
		public void update(float tpf) {
			time += tpf * getSpeed();

			if (time >= getMaxTime() / 2) {
				particles.setReleaseRate(0);
			}
			if (time >= getMaxTime()) {
				Values.getInstance().getLevelGameState().getKarsch().detachChild(particles);

				try {
					finalize();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
	}
}
