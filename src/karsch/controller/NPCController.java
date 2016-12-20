package karsch.controller;

import karsch.characters.CharacterBase;
import karsch.interfaces.NPCPassable;
import karsch.level.LevelMap;

import com.jme.animation.SpatialTransformer;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;

@SuppressWarnings("serial")
public class NPCController extends Controller{
	private CharacterBase character;
	private float time = 0; // is not TIME, is range to move
	private float rpf = 0; // range per frame
	private int direction = DIRECTION_NODIR;
	protected LevelMap levelMap;
	protected int x, y;
	private Object actField;
	
	protected SpatialTransformer trans;
	
	public static final int DIRECTION_NODIR = -1;
	public static final int DIRECTION_RIGHT = 0;
	public static final int DIRECTION_LEFT = 1;
	public static final int DIRECTION_DOWN = 2;
	public static final int DIRECTION_UP = 3;
	
	private int lastDirection;
	
	private float angle = 0, lastAngle = 0;
	
	protected boolean run = true;
	
	public NPCController(CharacterBase character, SpatialTransformer trans, int x, int y) {
		this.character = character;
		this.levelMap = character.getLevelMap();
		this.trans = trans;
		
		this.x = x;
		this.y = y;
		
		setMinTime(0);
		setMaxTime(5);
		setActive(false);
		actField = levelMap.getLevelMap()[x][y];
	}
	
	@Override
	public void update(float tpf) {

		if (time == getMinTime()){
			checkDirections();
		}
		if (run){
			rpf = 5f*tpf/getSpeed();
			if(time + rpf <= getMaxTime()){
				rotate();
				if(time + rpf >= getMaxTime()/2){
					if (direction == DIRECTION_DOWN){
						levelMap.getLevelMap()[x][y-1] = actField;
					} else if (direction == DIRECTION_UP){
						levelMap.getLevelMap()[x][y+1] = actField;					
					} else if (direction == DIRECTION_LEFT){
						levelMap.getLevelMap()[x+1][y] = actField;
					} else if (direction == DIRECTION_RIGHT){
						levelMap.getLevelMap()[x-1][y] = actField;
					}
				}
				time += rpf;
			} else{
				rpf = getMaxTime() - time;
				time += rpf;
			}

			move();
			
			if (time >= getMaxTime()){
				dontMove();
				setRun(false);
				time = getMinTime();
			}
		}
	}
	
	private void dontMove(){
		trans.setActive(false);
		lastDirection = direction;
		direction = DIRECTION_NODIR;
	}
	
	private void startRun(){
		if (!run){
			
			time = getMinTime();
			checkRotation();
			setRun(true);
		}
	}
	
	public void setRun(boolean run) {
		this.run = run;
		
		if (run) {
			if (direction > DIRECTION_NODIR){
				trans.setActive(true);
			} 		
		} else {
			trans.setActive(false);
		}
	}
	
	private void checkDirections(){
		int random;
		boolean ok = false;
		while (!ok){
			random = FastMath.rand.nextInt(5);
			if (random == 0){
				ok = moveRight();
			} else if (random == 1){
				ok = moveLeft();
			} else if (random == 2){
				ok = moveUp();
			} else if (random == 3){
				ok = moveDown();
			} else {
				trans.setActive(false);
				direction = DIRECTION_NODIR;
				ok = true;
			}
		}
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
	
	private void rotate(){
		if (time >= getMaxTime()/2)
			return;
		
		float percent = time*2/(getMaxTime());
		float newAngle = FastMath.DEG_TO_RAD * FastMath.LERP(percent, lastAngle, angle);

		character.getLocalRotation().fromAngleNormalAxis(newAngle, Vector3f.UNIT_Y);
	}
	
	private void move(){
		if(direction == DIRECTION_RIGHT) {
			character.getLocalTranslation().x += rpf;
		} else if(direction == DIRECTION_LEFT) {
			character.getLocalTranslation().x -= rpf;
		} else if(direction == DIRECTION_DOWN) {
			character.getLocalTranslation().z += rpf;
		} else if(direction == DIRECTION_UP) {
			character.getLocalTranslation().z -= rpf;
		}
	}
	
	
	private boolean moveRight(){
		if ( (direction == DIRECTION_NODIR) &&
			 (levelMap.getLevelMap().length > x+1) &&
			 (levelMap.getLevelMap()[x+1][y] instanceof NPCPassable)){
			
			x++;
			actField = levelMap.getLevelMap()[x][y];
			levelMap.getLevelMap()[x][y] = character;
			trans.setActive(true);
			direction = DIRECTION_RIGHT;
			
			startRun();
			return true;
		} else {
			return false;
		}
		

	}
	
	private boolean moveLeft(){
		if ( (direction == DIRECTION_NODIR) &&
			 (x > 0) && (levelMap.getLevelMap()[x-1][y] instanceof NPCPassable)){
			
			x--;
			actField = levelMap.getLevelMap()[x][y];
			levelMap.getLevelMap()[x][y] = character;
			trans.setActive(true);
			direction = DIRECTION_LEFT;

			startRun();
			return true;
		} else {
			return false;
		}

	}
	
	private boolean moveDown(){
		if ( (direction == DIRECTION_NODIR) &&
			 (levelMap.getLevelMap()[x].length > y+1) && 
			 (levelMap.getLevelMap()[x][y+1] instanceof NPCPassable)){
			
			y++;
			actField = levelMap.getLevelMap()[x][y];
			levelMap.getLevelMap()[x][y] = character;
			trans.setActive(true);
			direction = DIRECTION_DOWN;

			startRun();
			return true;
		} else {
			return false;
		}

	}
	
	private boolean moveUp(){
		if ( (direction == DIRECTION_NODIR) &&
			 (y > 0) && (levelMap.getLevelMap()[x][y-1] instanceof NPCPassable)){
			
			y--;
			actField = levelMap.getLevelMap()[x][y];
			levelMap.getLevelMap()[x][y] = character;
			trans.setActive(true);
			direction = DIRECTION_UP;
			
			startRun();
			return true;
		} else {
			return false;
		}

	}
	
}
