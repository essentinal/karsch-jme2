package karsch.menu;

import karsch.states.MenuGameState;

public class OptionsMenu extends SubMenu{

	public OptionsMenu(MenuGameState menuGameState) {
		super(menuGameState, "karsch2.jpg");
		
		
//		addEntry("Graphic");
		addEntry("Sound");
		addEntry("Back");
		
		init();
	}

	@Override
	public void select() {
		/*if (counter == 0){
			menuGameState.activateMenu("graphic");
		} else */
		if (counter == 0){
			menuGameState.activateMenu("sound");
		} else if (counter == 1){
			menuGameState.activateMenu("main");
		}
	}
	
	@Override
	public void escape() {
		menuGameState.activateMenu("main");
	}

}
