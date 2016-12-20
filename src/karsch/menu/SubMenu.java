package karsch.menu;

import java.util.ArrayList;

import karsch.KarschSimpleGame;
import karsch.resources.TextureCache;
import karsch.states.MenuGameState;

import org.fenggui.Container;
import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.Label;
import org.fenggui.binding.render.Font;
import org.fenggui.binding.render.ITexture;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.decorator.background.PixmapBackground;
import org.fenggui.layout.GridLayout;
import org.fenggui.util.Alignment;
import org.fenggui.util.Color;
import org.fenggui.util.Point;
import org.fenggui.util.Spacing;

public abstract class SubMenu extends Container{
	protected ArrayList<MenuLabel> labels = new ArrayList<MenuLabel>(4);
	protected int counter = 0;
	private int maxcount = -1;
	private int mincount = 0;
	protected Display disp;
	
	protected MenuGameState menuGameState;
	protected Font coolSerifFont = TextureCache.getInstance().getBoldFont();
	protected Font coolSerifFont2 = TextureCache.getInstance().getFont();
	
	public SubMenu(MenuGameState menuGameState, String background) {
		super(new GridLayout(8,1));
		this.menuGameState = menuGameState;
		disp = menuGameState.getDisplay();
		
		removeAllWidgets();
		
		ITexture tex=TextureCache.getInstance().getScaledITexture(background, disp.getWidth(), disp.getHeight());
		
		getAppearance().add(new PixmapBackground(new Pixmap(tex)));
		
		setSize(disp.getWidth(), disp.getHeight());
		setPosition(new Point(0,0));
		
		getAppearance().setPadding(new Spacing(20, 20));
		
		getAppearance().setMargin(new Spacing(0, 0));
		
		// add 2 spacers
		new MenuLabel(this, " ");
		new MenuLabel(this, " ");
	}
	
	protected void init(){
		String versionText = "Version " + KarschSimpleGame.VERSION + " Copyright 2009 by " + KarschSimpleGame.AUTHORS;
		Container versionContainer = new Container();
		Label versionLabel = FengGUI.createLabel(versionContainer, versionText, Color.WHITE_HALF_TRANSPARENT);

		versionLabel.getAppearance().setAlignment(Alignment.BOTTOM_RIGHT);
		
		for (int i=0; i<4-maxcount;i++){
			addWidget(new MenuLabel(this, " "));
		}
		addWidget(versionLabel);
		
		disp.layout();
		labels.get(0).setActive(true);
		setActive(false);
	}
	
	public void addEntry(String name){
		MenuLabel label = new MenuLabel(this, name);
		labels.add(label);
		maxcount++;
	}
	
	public void up(){
		labels.get(counter).setActive(false);
		if (--counter < mincount)
			counter = maxcount;
		labels.get(counter).setActive(true);
		
	}
	
	public void down(){
		labels.get(counter).setActive(false);
		if (++counter > maxcount)
			counter = mincount;
		labels.get(counter).setActive(true);
	}
	
	public void right(){
		
	}
	
	public void left(){
		
	}
	
	public void setActive(boolean active){
		setVisible(active);
	}
	
	public abstract void select();
	
	public abstract void escape();
	
	class MenuLabel extends Label{
		ITexture tex;
		PixmapBackground background;
		public MenuLabel(Container c, String text) {
			super(text);
			getAppearance().getData().setActiveFont(coolSerifFont);
			getAppearance().setAlignment(Alignment.MIDDLE);
			
			tex=TextureCache.getInstance().getITexture("SHADOW_ROUND2.PNG");
			background = new PixmapBackground(new Pixmap(tex), true);
			getAppearance().add(background);
			
			setActive(false);
			c.addWidget(this);
		}
		
		public void setActive(boolean active){
			if (active) {
				getAppearance().getData().setColor(Color.YELLOW);
				background.setEnabled(true);
				
			} else {
				getAppearance().getData().setColor(Color.WHITE_HALF_TRANSPARENT);
				background.setEnabled(false);
			}
		}
	}
}
