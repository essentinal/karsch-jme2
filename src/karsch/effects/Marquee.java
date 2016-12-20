package karsch.effects;

import java.awt.Font;

import karsch.resources.TextureCache;

import com.jme.math.Vector3f;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jmex.font3d.Font3D;
import com.jmex.font3d.Text3D;

@SuppressWarnings("serial")
public class Marquee extends Node{
//	private Queue<Text3D> texts = new LinkedList<Text3D>();
	private Font3D font = new Font3D(new Font("Arial", Font.PLAIN, 24), 0.001f, true, true, true);
	private float y=0;
	public Marquee() {
		
		addText("This is the demo version of the game:      ", 1);
		addText("Karsch the Pig - Episode One: The Lost Babies", 2);
		addImage("KARSCH2.PNG", 2);
		addText("Created by David Walter and Stephan Dreyer", 5);
		addText("for the course Multimedia Production at the", 1);
		addText("University of Applied Sciences Brandenburg.", 1);
		
		addText("Concept:", 4);
		addText("Stephan Dreyer", 2);
		addText("David Walter", 1);
		
		addText("Programming:", 4);
		addText("David Walter", 2);
		addText("Stephan Dreyer", 1);
		addText("Quertz Keyboard", 1);
		
		addText("3D Models and Animation:", 4);
		addText("Stephan Dreyer", 2);
		
		addText("Textures:", 4);
		addText("Stephan Dreyer", 2);
			
		addText("Level Design:", 4);
		addText("David Walter", 2);
		
		addText("Story:", 4);
		addText("David Walter", 1);
		
		addText("Music:", 4);
		addText("Stephan Dreyer and his imaginary", 2);
		addText("friend Tyler Durden", 1);
		
		addText("Voices:", 4);
		addText("Karsch", 2);
		addText("Stephan Dreyer", 1);
		
		addText("Mrs Karsch", 2);
		addText("Stephan Dreyer", 1);
		
		addText("Karsch's Babies", 2);
		addText("Stephan Dreyer", 1);
		
		addText("Gunther", 2);
		addText("Stephan Dreyer", 1);
		
		
		addText("Made with the power of jMonkeyEngine", 4);
		addImage("MONKEY.PNG", 2);
		
		addText("Thanks to the developers", 3);
		

		addText("No animals were harmed during the production", 4);
		addText("of this game, only humans.", 1);
		addText("(c) 2009 by David Walter, Stephan Dreyer", 4);
		
		setLocalTranslation(56, 0, 94);
		
		addController(new MarqueeController());
	}
	
	private void addText(String strng, float p){
		Text3D text = font.createText(strng, 20.0f, 0);
		text.setLocalScale(new Vector3f(0.4f, 0.4f, 0.01f));
		text.setLightCombineMode(LightCombineMode.Off);
		attachChild(text);
		y-= (0.5f*p);
		text.setLocalTranslation(0, y, 0);
		
	}
	
	private void addImage(String fileName, float p){
		Quad quad = new Quad("image");
		
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setTexture(TextureCache.getInstance().getTexture(fileName));
		quad.setRenderState(ts);
		
	    BlendState bs = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        bs.setBlendEnabled(true);
        bs.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        bs.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
        bs.setTestEnabled(true);
        bs.setTestFunction(BlendState.TestFunction.GreaterThan);
		quad.setRenderState(bs);
        
		quad.updateRenderState();
		
		quad.setLocalScale(new Vector3f(1.5f, 1.5f, 0.01f));
		quad.setLightCombineMode(LightCombineMode.Off);
		attachChild(quad);
		y-= (0.5f*p);
		quad.setLocalTranslation(0.5f, y, 0);
		
	}
	
	class MarqueeController extends Controller{
		private float rpf;
		private final float maxZ = 20;
		
		public MarqueeController() {
			setRepeatType(RT_WRAP);
			setMinTime(0);
			setMaxTime(5);
			setSpeed(9.5f);
			setActive(true);
		}
		
		@Override
		public void update(final float tpf) {
			rpf = 5f * tpf / getSpeed();

			getLocalTranslation().y += rpf;
			
			if (getLocalTranslation().y >= maxZ){
			}
		}
		
	}
}
