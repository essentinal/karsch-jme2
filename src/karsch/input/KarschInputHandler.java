package karsch.input;

import karsch.Values;
import karsch.characters.Karsch;
import karsch.level.LevelManager;

import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;
import com.jme.math.FastMath;

public class KarschInputHandler extends InputHandler{
	private Karsch karsch;
	static KarschInputHandler instance;
	private KarschInputHandler() {
		init();
	}
	
	public static KarschInputHandler getInstance(){
		if (instance == null)
			instance = new KarschInputHandler();
		return instance;
	}
	
	private void init() {
		addAction(new KeyUpAction(), InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_UP,InputHandler.AXIS_NONE, true);
		addAction(new KeyDownAction(), InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_DOWN, InputHandler.AXIS_NONE, true);
		addAction(new KeyLeftAction(), InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_LEFT, InputHandler.AXIS_NONE, true);
		addAction(new KeyRightAction(), InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_RIGHT, InputHandler.AXIS_NONE, true);
		
		addAction(new KeyPauseAction(), InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_P, InputHandler.AXIS_NONE, false);
		addAction(new KeyTestAction(), InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_RETURN, InputHandler.AXIS_NONE, false);
		addAction(new KeyInteractionAction(), InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_SPACE, InputHandler.AXIS_NONE, false);
		addAction(new KeyMenuAction(), InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_ESCAPE, InputHandler.AXIS_NONE, false);
		addAction(new KeyHelpAction(), InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_F1, InputHandler.AXIS_NONE, false);
		
	}
	
	public void setKarsch(Karsch karsch){
		this.karsch = karsch;
		clearActions();
		init();
	}
	
	class KeyUpAction extends KeyInputAction{
//		@Override
		public void performAction(InputActionEvent evt) {
			if (karsch != null){
				if (!Values.getInstance().isPause())
					karsch.moveUp();
			}
		}
	}
	class KeyDownAction extends KeyInputAction{
//		@Override
		public void performAction(InputActionEvent evt) {
			if (karsch != null){
				if (!Values.getInstance().isPause())
					karsch.moveDown();
			}
		}
	}
	class KeyLeftAction extends KeyInputAction{
//		@Override
		public void performAction(InputActionEvent evt) {
			if (karsch != null){
				if (!Values.getInstance().isPause())
					karsch.moveLeft();
			}
		}
	}
	class KeyRightAction extends KeyInputAction{
//		@Override
		public void performAction(InputActionEvent evt) {
			if (karsch != null){
				if (!Values.getInstance().isPause())
					karsch.moveRight();
			}
		}
	}
	class KeyPauseAction extends KeyInputAction{
//		@Override
		public void performAction(InputActionEvent evt) {
			if (karsch != null && evt.getTriggerPressed()){
				boolean pause = !Values.getInstance().isPause();
				Values.getInstance().getLevelGameState().setPause(pause, true);
			}
		}
	}
	class KeyMenuAction extends KeyInputAction{
//		@Override
		public void performAction(InputActionEvent evt) {
			LevelManager.getInstance().loadMenu();
		}
	}
	class KeyTestAction extends KeyInputAction{
//		@Override
		public void performAction(InputActionEvent evt) {
			if (karsch != null){
				if (evt.getTriggerPressed()){
//					String [] texts = {
//							"blabla", 
//							"it is only a test, for the speech balloon! blabla",
//							"wtf", 
//							"stfu"};
//					int rand = FastMath.rand.nextInt(3);
//					
//					karsch.speak(texts[rand]);
				}
			}
		}
	}
	
	class KeyInteractionAction extends KeyInputAction{
//		@Override
		public void performAction(InputActionEvent evt) {
			if (karsch != null){
				if (evt.getTriggerPressed()){
					karsch.checkInteractions();
				}
			}
		}
		
	}
	
	class KeyHelpAction extends KeyInputAction{
//		@Override
		public void performAction(InputActionEvent evt) {
			if (evt.getTriggerPressed()){
				Values.getInstance().getHudState().displayHelp();
			}
		}
	}
}
