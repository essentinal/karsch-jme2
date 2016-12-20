package karsch.characters;

import karsch.Values;
import karsch.controller.AudioController;
import karsch.controller.NPCController;
import karsch.level.LevelMap;

import com.jme.animation.SpatialTransformer;
import com.jme.renderer.Camera;
import com.jme.scene.Node;
import com.jmex.audio.AudioTrack;
import com.jmex.audio.RangedAudioTracker;

@SuppressWarnings("serial")
public class CharacterBase extends Node {
	protected SpatialTransformer trans;
	protected LevelMap levelMap;
	protected int x, y;
	protected Node model;
	protected RangedAudioTracker tracker;
	protected AudioTrack track;
	
	protected NPCController controller;
	protected AudioController audioController;

	public LevelMap getLevelMap() {
		return levelMap;
	}

	public CharacterBase(int x, int y, LevelMap levelMap, String name){
		super(name + String.valueOf(x)+String.valueOf(y));
		this.x = x;
		this.y = y;
		this.levelMap = levelMap;
	}
	
	public CharacterBase(String name, int x, int y){
		super(name + String.valueOf(x)+String.valueOf(y));
		this.x = x;
		this.y = y;
	}
	
	public void setPause(boolean pause){
		if (controller != null)
			controller.setActive(!pause);
		if (trans != null)
			trans.setActive(!pause);
		if (track != null){
			if (pause){
				track.stop();
			} else {
				track.play();
			}
		}
		if (audioController != null)
			audioController.setActive(!pause);
	}
	
	protected void addSound(String baseName, int min, int max, int randomSteps){
		
		audioController = new AudioController(this,randomSteps);
		addController(audioController);
		
		for (int i = min; i<=max; i++){
			audioController.addTrack(baseName + i + ".ogg");
		}
		
	}
	
	public boolean isVisible(){
		updateModelBound();
		
		Camera cam = Values.getInstance().getLevelGameState().getCam();
		int planeState = cam.getPlaneState();
		cam.setPlaneState(0);
		boolean visible = cam.contains(getWorldBound()) == Camera.FrustumIntersect.Inside;
		cam.setPlaneState(planeState);
		
		return visible;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	

}
