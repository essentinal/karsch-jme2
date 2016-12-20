package karsch.effects;

import java.awt.Color;
import java.awt.Font;

import karsch.resources.TextureCache;
import karsch.utils.TextQuadUtils;

import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.BillboardNode;
import com.jme.scene.Controller;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.scene.state.ZBufferState.TestFunction;
import com.jme.system.DisplaySystem;

@SuppressWarnings("serial")
public class SpeechBalloon extends BillboardNode{
	private MaterialState ms;
	private ZBufferState zbs;
	
	private BalloonController balloonController;
	private Quad balloonQuad;
	private Quad speechTextQuad;
	
	private TextQuadUtils quadUtils;
	
	private boolean visible = false;
	
	private BlendState bs2;
	public SpeechBalloon() {
		super("speechballoon");
		balloonQuad = new Quad("balloon",6f,3f);
		
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
	    
	    ts.setTexture(TextureCache.getInstance().getTexture("SPEECHBALLOON.PNG"),0);

	    balloonQuad.setRenderState(ts);
	    
	    BlendState bs = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        bs.setBlendEnabled(true);
        bs.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        bs.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
        bs.setTestEnabled(true);
        bs.setTestFunction(BlendState.TestFunction.GreaterThan);
        bs.setReference(0.1f);

        bs.setEnabled(true);
        
        ZBufferState zstate = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
	    zstate.setEnabled(true);
	    zstate.setWritable(true);
	    zstate.setFunction(TestFunction.Always);
        
        ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
        ms.setDiffuse(new ColorRGBA(1,1,1,0));
        ms.setAmbient(ColorRGBA.white);
        balloonQuad.setRenderState(ms);
        balloonQuad.setRenderState(zstate);
	    
        balloonQuad.setRenderState(bs);
        balloonQuad.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
	    
	    balloonQuad.setModelBound(new BoundingBox());
	    balloonQuad.updateModelBound();
	    attachChild(balloonQuad);
	    
	    balloonController = new BalloonController(this);
		balloonQuad.addController(balloonController);
		
		zbs = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
		zbs.setFunction(ZBufferState.TestFunction.Always);
		
		quadUtils = new TextQuadUtils(" test bla bla blubb trira ");
		quadUtils.setForeground(Color.BLACK);
		quadUtils.setFont(new Font("Helvetica", java.awt.Font.PLAIN, 12));
		quadUtils.setFontResolution(12);
		quadUtils.setMaxLineWidth(300);
		
		speechTextQuad = quadUtils.getQuad(1f);
		speechTextQuad.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
		
		speechTextQuad.setRenderState(zbs);
		
		bs2 = (BlendState) speechTextQuad.getRenderState(RenderState.RS_BLEND);
		
		speechTextQuad.updateRenderState();
		
		speechTextQuad.setCullHint(CullHint.Always);

		attachChild(speechTextQuad);
		updateGeometricState(0, false);
		updateRenderState();
		
		setLocalTranslation(0, 10, 0);
	}
	
	public void showText(String text){
		if (!balloonController.isActive()){
			balloonController.setActive(true);
		}
		
		if (text == null){
			if (visible){
				balloonController.fadeOut();
				visible = false;
			}
		} else {
			detachChild(speechTextQuad);
			quadUtils.setText(text);
			speechTextQuad = quadUtils.getQuad(1f);
			speechTextQuad.setRenderState(bs2);
			speechTextQuad.setRenderState(zbs);
			speechTextQuad.updateRenderState();
			
			balloonQuad.resize(speechTextQuad.getWidth() + 0.5f,
					speechTextQuad.getHeight() + 0.5f);
			
			attachChild(speechTextQuad);
			updateGeometricState(0, false);
			balloonController.fadeIn();
			visible = true;
		}
	}
	
	public class BalloonController extends Controller{
		private float time = 0;
		private boolean fadeIn = false, fadeOut = false;
		public BalloonController(SpeechBalloon balloon) {
			setMinTime(0);
			setMaxTime(1);
			setSpeed(2f);
			setActive(true);
		}
		
		public void fadeIn(){
			fadeOut = false;
			fadeIn = true;
			time = 0;
			setActive(true);
		}
		
		public void fadeOut(){
			fadeIn = false;
			fadeOut = true;
			time = 0;
		}
		
		@Override
		public void update(float tpf) {
			time += tpf*getSpeed();
			if (time <= getMaxTime()){
				if (fadeIn){
					float alpha = FastMath.LERP(time / getMaxTime(), 0f, 1f);
					ms.getDiffuse().a = alpha;
					speechTextQuad.setCullHint(CullHint.Never);
				}
				
				if (fadeOut){
					float alpha = FastMath.LERP(time / getMaxTime(), 1f, 0f);
					ms.getDiffuse().a = alpha;
					speechTextQuad.setCullHint(CullHint.Always);
				}
				
			} else {
				if (fadeOut)
					setActive(false);
				fadeOut = false;
				fadeIn = false;
			}
		}
	}
}




