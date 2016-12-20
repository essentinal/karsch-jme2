package karsch.menu;

import karsch.states.MenuGameState;

import com.jme.input.InputHandler;
import com.jme.input.KeyInput;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;

/**
 * The input handler we use to navigating the menu.
 * 
 * @author Stephan
 */
public class MenuHandler extends InputHandler {
    private MenuGameState menuState;

    public MenuHandler( MenuGameState menuState ) {
    	this.menuState = menuState;
        setKeyBindings();
        
    }

    private void setKeyBindings() {
        addAction( new EnterAction(), InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_RETURN, InputHandler.AXIS_NONE, false  );
        addAction( new EscapeAction(), InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_ESCAPE, InputHandler.AXIS_NONE, false  );
        
        addAction( new DownAction(), InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_DOWN, InputHandler.AXIS_NONE, false  );
        addAction( new UpAction(), InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_UP, InputHandler.AXIS_NONE, false  );
        addAction( new LeftAction(), InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_LEFT, InputHandler.AXIS_NONE, false  );
        addAction( new RightAction(), InputHandler.DEVICE_KEYBOARD, KeyInput.KEY_RIGHT, InputHandler.AXIS_NONE, false  );
    }
    
    private class EscapeAction extends InputAction {
        public void performAction( InputActionEvent evt ) {
        	if (evt.getTriggerPressed())
        		menuState.escape();

        }
    }

    private class EnterAction extends InputAction {
        public void performAction( InputActionEvent evt ) {
        	if (evt.getTriggerPressed())
        		menuState.select();

        }
    }
    
    private class DownAction extends InputAction {
        public void performAction( InputActionEvent evt ) {
        	if (evt.getTriggerPressed())
        		menuState.down();
        }
    }
    
    private class UpAction extends InputAction {
        public void performAction( InputActionEvent evt ) {
        	if (evt.getTriggerPressed())
        		menuState.up();
        }
    }
    
    private class LeftAction extends InputAction {
        public void performAction( InputActionEvent evt ) {
        	if (evt.getTriggerPressed())
        		menuState.left();
        }
    }
    
    private class RightAction extends InputAction {
        public void performAction( InputActionEvent evt ) {
        	if (evt.getTriggerPressed())
        		menuState.right();
        }
    }
 
}