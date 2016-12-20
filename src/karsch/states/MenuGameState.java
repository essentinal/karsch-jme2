package karsch.states;

import java.util.HashMap;

import karsch.menu.GraphicsMenu;
import karsch.menu.MainMenu;
import karsch.menu.MenuHandler;
import karsch.menu.OptionsMenu;
import karsch.menu.SoundMenu;
import karsch.menu.SubMenu;

import org.fenggui.Display;
import org.fenggui.binding.render.Binding;
import org.fenggui.binding.render.lwjgl.LWJGLBinding;
import org.lwjgl.opengl.GL13;

import com.jmex.game.state.GameState;

public class MenuGameState extends GameState{
	private Display disp;
	private MenuHandler input;
	
	private HashMap<String, SubMenu> menus;
	private SubMenu actMenu;
	
	public MenuGameState() {
		setName("menu");
		
		init();
		
		input = new MenuHandler( this );
	}
	
	private void init(){
		disp = new Display(new LWJGLBinding());
		
		Binding.getInstance().setUseClassLoader(true);

		menus = new HashMap<String, SubMenu>(4);
		menus.put("main", new MainMenu(this));
		menus.put("options", new OptionsMenu(this));
		menus.put("sound", new SoundMenu(this));
		menus.put("graphic", new GraphicsMenu(this));
		actMenu = menus.get("main");
		actMenu.setActive(true);
		disp.addWidget(actMenu);
		disp.layout();
		disp.display();
	}
	
	@Override
	public void setActive(boolean active) {
		activateMenu("main");
		input.setEnabled(active);
		super.setActive(active);
	}
	
	@Override
	public void cleanup() {	}
	
	public void activateMenu(String menu){
		if (menus.containsKey(menu)){
			for (SubMenu subMenu: menus.values()){
				subMenu.setActive(false);
			}
			actMenu = menus.get(menu);
			actMenu.setActive(true);
			disp.removeAllWidgets();
			disp.addWidget(actMenu);
			disp.layout();
		}
		
	}
	
	public void select(){
		actMenu.select();
	}
	
	public void escape(){
		actMenu.escape();
	}
	
	public void up(){
		actMenu.up();
	}
	
	public void down(){
		actMenu.down();
	}
	
	public void right(){
		actMenu.right();
	}
	
	public void left(){
		actMenu.left();
	}

	@Override
	public void update(float tpf) {
		input.update(tpf);
	}
	
	public void render(float tpf) {
		// Set back to first texture unit so GUI displays properly
		GL13.glActiveTexture(GL13.GL_TEXTURE0);

		// Draw GUI
		disp.display();
	}

	public Display getDisplay() {
		return disp;
	}
	
}
