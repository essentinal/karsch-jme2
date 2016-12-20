package karsch.states;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;

import karsch.input.KarschInputHandler;
import karsch.resources.TextureCache;

import org.fenggui.Container;
import org.fenggui.Display;
import org.fenggui.FengGUI;
import org.fenggui.Label;
import org.fenggui.binding.render.Font;
import org.fenggui.binding.render.ITexture;
import org.fenggui.binding.render.Pixmap;
import org.fenggui.binding.render.lwjgl.LWJGLBinding;
import org.fenggui.decorator.background.PixmapBackground;
import org.fenggui.layout.RowLayout;
import org.fenggui.util.Alignment;
import org.fenggui.util.Color;
import org.fenggui.util.Point;
import org.fenggui.util.Spacing;
import org.lwjgl.opengl.GL13;

import com.jme.util.GameTaskQueueManager;
import com.jmex.game.state.GameState;

public class HUDGameState extends GameState{
	private Display disp;
	private Label textLabel;
	private ShowThread showThread;
	
	private KarschInputHandler input;
	private boolean blocks = false;
	private double timer = 0;
	private Label babyLabel, keyLabel, helpLabel, timerLabel;
	
	private Queue<TextTime> queue = new LinkedList<TextTime>();
	
	public HUDGameState() {
		setupHud();
		input = KarschInputHandler.getInstance();
	}
	
	@Override
	public void cleanup() {	}
	
	private void setupHud(){
		disp = new Display(new LWJGLBinding());
		
		Font coolSerifFont = TextureCache.getInstance().getBoldFont();
		Font coolSerifFont2 = TextureCache.getInstance().getFont();
		
		Container hudContainer = new Container(new RowLayout(true));
		hudContainer.setPosition(new Point(disp.getSize().getWidth()-250,disp.getSize().getHeight()-100));
		
		babyLabel = FengGUI.createLabel(hudContainer," 0x", Color.WHITE);
		babyLabel.getAppearance().getData().setActiveFont(coolSerifFont);
		
		ITexture tex = TextureCache.getInstance().getITexture("BABYPIGS.PNG");
		
		@SuppressWarnings("unused")
		final Label label = FengGUI.createLabel(hudContainer,tex);
		
		keyLabel = FengGUI.createLabel(hudContainer," 0x", Color.WHITE);
		keyLabel.getAppearance().getData().setActiveFont(coolSerifFont);
		
		ITexture keyTex = TextureCache.getInstance().getITexture("KEY.PNG");
		
		@SuppressWarnings("unused")
		final Label label2 = FengGUI.createLabel(hudContainer,keyTex);
		
		ITexture tex2 = TextureCache.getInstance().getITexture("GUI1.PNG");
		
		Pixmap pixmap = new Pixmap(tex2);
		hudContainer.getAppearance().add(new PixmapBackground(pixmap, true));
		hudContainer.setSize(100, 50);
		hudContainer.getAppearance().setPadding(new Spacing(10,20,20,10));
		
		
		Container textContainer = new Container(new RowLayout(true));
		
		textContainer.setSize(disp.getWidth(), 50);
		
		
		textLabel = FengGUI.createLabel(textContainer, " ", Color.WHITE_HALF_TRANSPARENT);
//		textLabel.setSize((disp.getWidth()/4)*3, 50);
		textLabel.setPosition(new Point(50,50));
		textLabel.getAppearance().getData().setActiveFont(coolSerifFont2);
		textLabel.getAppearance().setAlignment(Alignment.LEFT);
		disp.addWidget(textLabel);
		
		ITexture helptex = TextureCache.getInstance().getScaledITexture("KEYBOARD.PNG", disp.getWidth(), disp.getHeight());
		
		// pre-cache textures
		TextureCache.getInstance().getScaledITexture("KEYBOARD_ARROWS.PNG", disp.getWidth(), disp.getHeight());
		TextureCache.getInstance().getScaledITexture("KEYBOARD_BOTH.PNG", disp.getWidth(), disp.getHeight());
		TextureCache.getInstance().getScaledITexture("KEYBOARD_ESC.PNG", disp.getWidth(), disp.getHeight());
		TextureCache.getInstance().getScaledITexture("KEYBOARD_SPACE.PNG", disp.getWidth(), disp.getHeight());
		
		helpLabel = new Label();
		helpLabel.setPixmap(new Pixmap(helptex));
		helpLabel.layout();
		
		helpLabel.setPosition(new Point(0,-120));
		helpLabel.setVisible(false);
		
		ITexture tex3 = TextureCache.getInstance().getITexture("GUI2.PNG");
		
		Container footerContainer = new Container(new RowLayout(true));
		footerContainer.setPosition(new Point(0,0));
		footerContainer.setSize(disp.getWidth(), 20);
		footerContainer.getAppearance().setPadding(new Spacing(10, 10));
		
		timerLabel = FengGUI.createLabel(footerContainer,"0", Color.WHITE_HALF_TRANSPARENT);
		timerLabel.getAppearance().setAlignment(Alignment.RIGHT);
//		timerLabel.getAppearance().getData().setActiveFont(coolSerifFont);
		
		Pixmap pixmap2 = new Pixmap(tex3);
		footerContainer.getAppearance().add(new PixmapBackground(pixmap2, true));
		
		disp.addWidget(hudContainer);
		disp.addWidget(footerContainer);
		
		disp.addWidget(helpLabel);
		
		disp.layout();
	}
	
	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		
	}
	
	public void disableProgressbar(){
		new Thread(){
			@Override
			public void run() {
				float alpha = textLabel.getAppearance().getData().getColor().getAlpha();
				while (alpha > 0){
					alpha -= .05f;
					textLabel.getAppearance().getData().setColor(new Color(1f,1f,1f,alpha));
					try {
						sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				textLabel.setVisible(false);
				blocks=false;
				
				// finally check if there are other items in the queue and display them
				TextTime next = queue.poll();
				if (next != null){
					displayTextTime(next.text, next.time);
				}
				
			}
		}.start();
	}
	
	public void displayHelp(){
		new HelpThread().start();
	}
	
	public void displayText(String text){
		if (blocks){
			return;
		}
		blocks = true;
		textLabel.setText(text);
		textLabel.setVisible(true);
		showThread = new ShowThread();
		showThread.start();
		
	}
	
	public void displayTextTime(String text, long time){
		if (blocks){
			queue.offer(new TextTime(text, time));
			return;
		}
		blocks = true;
		textLabel.setText(text);
		textLabel.setVisible(true);
		showThread = new ShowThread(time);
		showThread.start();
		
	}
	
	public void setBabies(int count){
		babyLabel.setText(count + "x");
	}
	
	public void setKeys(int count){
		keyLabel.setText(count + "x");
	}

	@Override
	public void update(float tpf) {
		input.update(tpf);
		timer += tpf;
		
		int minutes = (int)timer / 60;
		int seconds = (int)timer % 60;
		
		timerLabel.setText( String.format("%02d:%02d",minutes ,seconds ));
	}
	
	public void render(float tpf) {
		
		// Set back to first texture unit so GUI displays properly
		GL13.glActiveTexture(GL13.GL_TEXTURE0);

		// Draw GUI
		disp.display();
	}
	
	public KarschInputHandler getInputHandler() {
		return input;
	}
	
	class ShowThread extends Thread{
		private long time;
		private boolean blendout = false;
		private float alpha = 0;
		public ShowThread() {
			time = 1000;
		}
		
		public ShowThread(long time) {
			this.time = time;
			blendout = true;
			textLabel.getAppearance().getData().setColor(new Color(1f,1f,1f,0));
		}
		
		@Override
		public void run() {
			fadeIn();
			
			if (blendout){
				long starttime = System.currentTimeMillis();
						
				while (starttime+time > System.currentTimeMillis()){
					try {
						sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				disableProgressbar();
			}
		}
		private void fadeIn(){
			
			while (alpha < .6f){
				alpha += .05f;
				GameTaskQueueManager.getManager().update(new Callable<Object>() { // <----- here is the secret that I was searching  :-D  :-D
		            public Object call() throws Exception {
				
		            	textLabel.getAppearance().getData().setColor(new Color(1f,1f,1f,alpha));
		            	return null;
		            }
				});
				
				try {
					sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class HelpThread extends Thread{
		public HelpThread() {}
		
		@Override
		public void run() {
			input.setEnabled(false);
			helpLabel.setVisible(true);
			displayTextTime("This will show you the game controls", 3000);
			sleepFor(6000);
			
			ITexture helptex = TextureCache.getInstance().getScaledITexture("KEYBOARD_BOTH.PNG", disp.getWidth(), disp.getHeight());
			helpLabel.setPixmap(new Pixmap(helptex));
			displayTextTime("You'll need only a few keys to control Karsch", 3000);
			sleepFor(6000);
			
			helptex = TextureCache.getInstance().getScaledITexture("KEYBOARD_ARROWS.PNG", disp.getWidth(), disp.getHeight());
			helpLabel.setPixmap(new Pixmap(helptex));
			displayTextTime("Use the arrow keys to move Karsch left, right, up and down", 3000);
			sleepFor(6000);
			
			helptex = TextureCache.getInstance().getScaledITexture("KEYBOARD_SPACE.PNG", disp.getWidth(), disp.getHeight());
			helpLabel.setPixmap(new Pixmap(helptex));
			displayTextTime("Use the space key to interact with other characters or things", 3000);
			sleepFor(6000);
			
			helptex = TextureCache.getInstance().getScaledITexture("KEYBOARD_ESC.PNG", disp.getWidth(), disp.getHeight());
			helpLabel.setPixmap(new Pixmap(helptex));
			displayTextTime("Pressing the Escape key will show up the game menu", 3000);
			sleepFor(6000);
			
			helptex = TextureCache.getInstance().getScaledITexture("KEYBOARD.PNG", disp.getWidth(), disp.getHeight());
			helpLabel.setPixmap(new Pixmap(helptex));
			displayTextTime("That's all", 3000);
			sleepFor(4000);
			input.setEnabled(true);
			
			helpLabel.setVisible(false);

		}
		
		private void sleepFor(long time){
			try {
				sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class TextTime {
		public String text;
		public long time;
		public TextTime(String text, long time) {
			this.text = text;
			this.time = time;
		}
	}
}
