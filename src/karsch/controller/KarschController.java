package karsch.controller;

import karsch.Values;
import karsch.characters.Karsch;
import karsch.interfaces.Collectable;
import karsch.interfaces.Interactable;
import karsch.interfaces.KarschPassable;
import karsch.level.LevelMap;

import com.jme.animation.SpatialTransformer;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;

@SuppressWarnings("serial")
public class KarschController extends Controller{
	private Karsch karsch;
	private float time = 0; // is not TIME, is range to move
	private float rpf = 0; // range per frame
	private int direction = DIRECTION_NODIR;
	private LevelMap levelMap;
	private int x, y;
	private Object actField;
	private int lastDirection = DIRECTION_NODIR;
	
	private float angle = 0, lastAngle = 0;
	
	protected boolean run, rotateonly;
	
	private SpatialTransformer trans;
	
	public static final int DIRECTION_NODIR = -1;
	public static final int DIRECTION_RIGHT = 0;
	public static final int DIRECTION_LEFT = 1;
	public static final int DIRECTION_DOWN = 2;
	public static final int DIRECTION_UP = 3;
	

	
	public KarschController(Karsch karsch, SpatialTransformer trans, int x, int y) {
		this.karsch = karsch;
		this.trans = trans;
		this.levelMap = karsch.getLevelMap();
		
		this.x = x;
		this.y = y;
		
		setMinTime(0);
		setMaxTime(5);
		setSpeed(.5f / Values.getInstance().getSpeed());
		trans.update(.1f);
		setRun(false);
		pickUp();
	}
	
	@Override
	public void update(float tpf) {
		if (run){
			rpf = 5f*tpf/getSpeed();
			if(time + rpf <= getMaxTime()){
				if (lastDirection != DIRECTION_NODIR){
					rotate();
				}
				time += rpf;
			} else{
				rpf = getMaxTime() - time;
				time += rpf;
			}

			if (!rotateonly)
				move();
			
			if (time >= getMaxTime()){
				
				dontMove();
				setRun(false);
				time = getMinTime();
				checkActField();
				rotateonly = false;
			}
		}
	}
	
	private void checkActField(){
		if (actField instanceof Collectable){
			((Collectable)actField).collect(karsch);
		}
	}
	
	public void setRun(boolean run) {
		this.run = run;
		if (run && !rotateonly) {
			if (direction > DIRECTION_NODIR){
				trans.setActive(true);
			} 		
		} else {
			trans.setActive(false);
		}
	}
	
	public void dontMove(){
		setRun(false);
		trans.setActive(false);
		lastDirection = direction;
		direction = DIRECTION_NODIR;
//		time = getMinTime();
		
	}
	
	private void startRun(){
		if (!run){
			time = getMinTime();
			setRun(true);
			karsch.setX(x);
			karsch.setY(y);
			checkRotation();
		}
	}
	
	private void drop(){
//		System.out.println("drop " + actField);
		if (actField != null)
			levelMap.getLevelMap()[x][y] = actField;
	}
	
	private void pickUp(){
		actField = levelMap.getLevelMap()[x][y];
//		System.out.println("pick up " + actField);
		levelMap.getLevelMap()[x][y] = karsch;
		
//		if (actField instanceof LevelEntrance){
//			Vector3f dir = ((LevelEntrance)actField).getDirectionVector();
//			karsch.lookAt(karsch.getLocalTranslation().clone().add(new Vector3f(0,0,1)), Vector3f.UNIT_Y);
//			lastDirection = ((LevelEntrance)actField).getDirection();
//			if (dir != null)
//				karsch.lookAt(karsch.getLocalTranslation().clone().subtract(dir), Vector3f.UNIT_Y);
//		}
	}
	
	private void move(){
		if(direction == DIRECTION_RIGHT) {
			karsch.getLocalTranslation().x += rpf;
		} else if(direction == DIRECTION_LEFT) {
			karsch.getLocalTranslation().x -= rpf;
		} else if(direction == DIRECTION_DOWN) {
			karsch.getLocalTranslation().z += rpf;
		} else if(direction == DIRECTION_UP) {
			karsch.getLocalTranslation().z -= rpf;
		}
	}
	
	private void rotate(){
		if (time > getMaxTime()/2)
			return;
		
		float percent = time*2/(getMaxTime());
		float newAngle = FastMath.DEG_TO_RAD * FastMath.LERP(percent, lastAngle, angle);

		karsch.getLocalRotation().fromAngleNormalAxis(newAngle, Vector3f.UNIT_Y);
	}
	
	private void checkRotation(){
		if (lastDirection == DIRECTION_DOWN) {
			if (direction == DIRECTION_LEFT) {
				lastAngle = 360;
			} else {
				lastAngle = 0;
			}
		} else if (lastDirection == DIRECTION_UP) {
			lastAngle = 180;
		} else if (lastDirection == DIRECTION_RIGHT) {
			lastAngle = 90;
		} else if (lastDirection == DIRECTION_LEFT) {
			lastAngle = 270;
		}
		
		if (direction == DIRECTION_DOWN) {
			if (lastDirection == DIRECTION_LEFT){
				angle = 360;
			} else {
				angle = 0;
			}
		} else if (direction == DIRECTION_UP) {
			angle = 180;
		} else if (direction == DIRECTION_RIGHT) {
			angle = 90;
		} else if (direction == DIRECTION_LEFT) {
			angle = 270;
		}
	}
	
	
	public void moveRight(){
		if ( isActive() && (direction == DIRECTION_NODIR)){
			if ((levelMap.getLevelMap().length > x+1) &&
					(levelMap.getLevelMap()[x+1][y] instanceof KarschPassable)&&
					((KarschPassable)levelMap.getLevelMap()[x+1][y]).canPass()){
			
				drop();
				x++;
				pickUp();
				
				trans.setActive(true);
				rotateonly = false;
			} else {
				rotateonly = true;
			}
			
			direction = DIRECTION_RIGHT;
			
			startRun();
		}
	}
	
	public void moveLeft(){
		if ( isActive() && (direction == DIRECTION_NODIR)){
			if ((x > 0) && (levelMap.getLevelMap()[x-1][y] instanceof KarschPassable) &&
					((KarschPassable)levelMap.getLevelMap()[x-1][y]).canPass()){
			
				drop();
				x--;
				pickUp();
				
				trans.setActive(true);
				
				rotateonly = false;
			} else {
				rotateonly = true;
			}
			direction = DIRECTION_LEFT;

			startRun();
		}
	}
	
	public void moveDown() {
		if (isActive() && (direction == DIRECTION_NODIR)) {
			if ((levelMap.getLevelMap()[x].length > y + 1)
					&& (levelMap.getLevelMap()[x][y + 1] instanceof KarschPassable) &&
					((KarschPassable)levelMap.getLevelMap()[x][y + 1]).canPass()) {

				drop();
				y++;
				pickUp();

				trans.setActive(true);
				rotateonly = false;
			} else {
				rotateonly = true;
			}

			direction = DIRECTION_DOWN;

			startRun();
		}
	}
	
	public void moveUp() {
		if (isActive() && (direction == DIRECTION_NODIR)) {
			if ((y > 0) && (levelMap.getLevelMap()[x][y-1] instanceof KarschPassable) &&
					((KarschPassable)levelMap.getLevelMap()[x][y - 1]).canPass()){
			
				drop();
				y--;
				pickUp();
				trans.setActive(true);
				rotateonly = false;
			} else {
				rotateonly = true;
			}
			
			direction = DIRECTION_UP;
			
			startRun();
		}
	}
	
	public void setActField(int x, int y){
		actField = levelMap.getLevelMap()[x][y];
		levelMap.getLevelMap()[x][y] = karsch;
	}
	
	public void checkInteractions(){
		if (run)
			return;
		Interactable ia = null;
		if (lastDirection == DIRECTION_LEFT && x > 0 && levelMap.getLevelMap()[x-1][y] instanceof Interactable){
			ia = (Interactable) levelMap.getLevelMap()[x-1][y];
		} else if (lastDirection == DIRECTION_RIGHT && (levelMap.getLevelMap().length > x+1) && levelMap.getLevelMap()[x+1][y] instanceof Interactable){
			ia = (Interactable) levelMap.getLevelMap()[x+1][y];
		} else if (lastDirection == DIRECTION_UP && y > 0 && levelMap.getLevelMap()[x][y-1] instanceof Interactable){
			ia = (Interactable) levelMap.getLevelMap()[x][y-1];
		} else if (lastDirection == DIRECTION_DOWN && (levelMap.getLevelMap()[0].length > y+1) && levelMap.getLevelMap()[x][y+1] instanceof Interactable){
			ia = (Interactable) levelMap.getLevelMap()[x][y+1];
		}
		
		if (ia != null){
			ia.interact(karsch);
			
		}
	}
	
	public void setActField(Object o){
		actField = o;
	}
	
	public Object getActField(){
		return actField;
	}

	public void setLastDirection(int lastDirection) {
		this.lastDirection = lastDirection;
		switch (lastDirection) {
			case DIRECTION_RIGHT: karsch.lookAt(karsch.getLocalTranslation().clone().add(new Vector3f(1,0,0)), Vector3f.UNIT_Y);break; 
			case DIRECTION_LEFT: karsch.lookAt(karsch.getLocalTranslation().clone().add(new Vector3f(-1,0,0)), Vector3f.UNIT_Y);break;
			case DIRECTION_UP: karsch.lookAt(karsch.getLocalTranslation().clone().add(new Vector3f(0,0,-1)), Vector3f.UNIT_Y);break;
			case DIRECTION_DOWN: karsch.lookAt(karsch.getLocalTranslation().clone().add(new Vector3f(0,0,1)), Vector3f.UNIT_Y);break;
		}
	}
	
}
