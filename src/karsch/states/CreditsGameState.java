package karsch.states;

import karsch.KarschSimpleGame;
import karsch.Values;
import karsch.debug.MapViewer;
import karsch.effects.Marquee;
import karsch.level.Level;
import karsch.level.LevelFactory;
import karsch.level.LevelManager;
import karsch.sound.SoundManager;

import com.acarter.scenemonitor.SceneMonitor;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.light.LightNode;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.CameraNode;
import com.jme.scene.Controller;
import com.jme.scene.Spatial;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.RenderState;
import com.jme.system.DisplaySystem;
import com.jme.util.Timer;
import com.jmex.audio.AudioSystem;
import com.jmex.game.state.CameraGameStateDefaultCamera;

public class CreditsGameState extends CameraGameStateDefaultCamera {
	private DisplaySystem display;
	private Values values;
	
	private LightNode lightNode;
	
	private SceneMonitor monitor;
	
	private boolean pause = false;
	private Camera cam;
	private Level level;
	
	public CreditsGameState() {
		super("credits");
		
		KeyBindingManager.getKeyBindingManager().add("ESCAPE", KeyInput.KEY_ESCAPE);
		
		values = Values.getInstance();
		display = DisplaySystem.getDisplaySystem();
		
		display.getRenderer().getQueue().setTwoPassTransparency(false);
        CullState cs = display.getRenderer().createCullState();
        cs.setCullFace(CullState.Face.Back);
        cs.setEnabled(true);
        
        rootNode.setRenderState(cs);
        rootNode.setCullHint(Spatial.CullHint.Dynamic);
        
        cam = display.getRenderer().createCamera(display.getWidth(), display.getHeight());
        cam.setFrustumPerspective(65, 1.3f, 0.5f, 1000);
        cam.update();
        
//        flycam = display.getRenderer().createCamera(display.getWidth(), display.getHeight());
//        flycam.setFrustumPerspective(65, 1.3f, 0.5f, 1000);
//        flycam.update();
        
        init();
	}

	@Override
	protected void onActivate() {
		DisplaySystem.getDisplaySystem().setTitle("Karsch Credits");
		display.getRenderer().setCamera(cam);
		
		setPause(false);
	}
	
	@Override
	protected void onDeactivate() {
		super.onDeactivate();
	}
	
	@Override
	protected void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("ESCAPE")){
			LevelManager.getInstance().unloadCredits();
			SoundManager.getInstance().playMusic();
		}

		if (KarschSimpleGame.DEBUG){
			if (monitor != null)
				monitor.updateViewer(tpf);
			
	        MapViewer.getInstance().update();
		}
	}
	
	
	public void init(){
		
		SoundManager.getInstance().playCreditMusic();
		rootNode.addController(new TimeController(107));
		
		level = LevelFactory.getInstance().createLevel(null, 8);
		rootNode.attachChild(level);

        rootNode.updateGeometricState(0, true);
        rootNode.updateRenderState();
        
        if (KarschSimpleGame.DEBUG){
	        monitor = values.getMonitor();
			monitor.registerNode(rootNode);
			monitor.showViewer(true);
        }
		
		display.getRenderer().setCamera(cam);
		
		CameraNode camNode = new CameraNode("cam", cam);
		rootNode.attachChild(new Marquee());
		camNode.lookAt(new Vector3f(0,-0.0f,-1), Vector3f.UNIT_Y);
		camNode.setLocalTranslation(60, 5, 100);
		rootNode.attachChild(camNode);
		
//		TimeController tc = new TimeController(3);
//		rootNode.addController(tc);
		setupLight(0.3f);
		
		Timer.getTimer().reset();
		
	}
	
	private void setupLight(float factor){
		PointLight l = new PointLight();
		l.setDiffuse(ColorRGBA.white);
		l.setShadowCaster(true);
		l.setEnabled(true);
		l.setConstant(1/factor);
		l.setLocation(new Vector3f(0,5,0));
		
		lightNode = new LightNode("lightnode");
		lightNode.setLight(l);
		lightNode.setLocalTranslation(new Vector3f(0,1,5));
		
		PointLight l2 = new PointLight();
		l2.setEnabled(true);
		l2.setAttenuate(true);
		l2.setConstant(1.5f/factor);
		l2.setLocation(new Vector3f(0,80,100));
		
		LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
		lightState.attach(l);
		lightState.attach(l2);
		rootNode.clearRenderState(RenderState.RS_LIGHT);
		rootNode.setRenderState(lightState);
		rootNode.updateRenderState();
	}
	
	public void setPause(boolean pause){
		this.pause = pause;

		values.setPause(pause);
		level.setPause(pause);
	}
	
	@SuppressWarnings("serial")
	private class TimeController extends Controller{
		float time = 0, targetTime;
		Controller controller;
		
		public TimeController(float maxTime) {
			this.controller = this;
			setMaxTime(maxTime);
			setActive(true);
		}
		
		@Override
		public void update(float tpf) {
			if ((time += tpf) > getMaxTime()){
				LevelManager.getInstance().unloadCredits();
				SoundManager.getInstance().playMusic();
			}
		}
	}
}
