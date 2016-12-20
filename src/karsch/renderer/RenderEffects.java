package karsch.renderer;

import com.jme.math.Plane;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.pass.BasicPassManager;
import com.jme.renderer.pass.RenderPass;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.state.FogState;
import com.jme.system.DisplaySystem;
import com.jmex.effects.glsl.BloomRenderPass;
import com.jmex.effects.glsl.MotionBlurRenderPass;
import com.jmex.effects.water.ProjectedGrid;
import com.jmex.effects.water.WaterHeightGenerator;
import com.jmex.effects.water.WaterRenderPass;

public class RenderEffects {
	private Node rootNode;
	private BasicPassManager pManager;
	private BloomRenderPass bloomRenderPass;
	private MotionBlurRenderPass motionBlurRenderPass;
    private WaterRenderPass waterEffectRenderPass;
//    private ShadowedRenderPass shadowPass;
    
    private ProjectedGrid projectedGrid;
    
	public RenderEffects(Node rootNode, BasicPassManager pManager){
		this.pManager=pManager;
		this.rootNode=rootNode;
		
		//setup the renderpass for the rootnode
		RenderPass rootPass = new RenderPass();
        rootPass.add(rootNode);
        pManager.add(rootPass);

	}
	
	public void addBloom(Camera cam){
		bloomRenderPass = new BloomRenderPass(cam, 4);

		if (!bloomRenderPass.isSupported()) {
			System.err.println("GLSL Not supported on this computer.");
		} else {
			bloomRenderPass.add(rootNode);
			bloomRenderPass.setUseCurrentScene(true);
			bloomRenderPass.setBlurIntensityMultiplier(1f);
			pManager.add(bloomRenderPass);
		}
	}
	
	@Deprecated
	public void addWaterEffect(Camera cam, Spatial spatial, Skybox skybox){
		waterEffectRenderPass = new WaterRenderPass(cam, 4, true, true);
        waterEffectRenderPass.setClipBias(0.5f);
        waterEffectRenderPass.setWaterMaxAmplitude(5.0f);
        
        waterEffectRenderPass.setWaterPlane(new Plane(new Vector3f(0.0f, 1.0f,0.0f), 0.0f));
        waterEffectRenderPass.setWaterEffectOnSpatial(spatial);
       
        waterEffectRenderPass.setReflectedScene(rootNode);
        waterEffectRenderPass.setSkybox(skybox);
        waterEffectRenderPass.setUseRefraction(true);
        waterEffectRenderPass.setUseReflection(true);
        
        pManager.add(waterEffectRenderPass);
        projectedGrid = new ProjectedGrid("ProjectedGrid", cam, 100, 100, 0.02f,
                new WaterHeightGenerator());
        projectedGrid.setLocalTranslation(spatial.getLocalTranslation());
        
        rootNode.attachChild(projectedGrid);
	}
	
	public void addMotionBlur(Camera cam){
		motionBlurRenderPass = new MotionBlurRenderPass(cam);
		
		if (!motionBlurRenderPass.isSupported()) {
			System.err.println("GLSL Not supported on this computer.");
		} else {
			motionBlurRenderPass.add(rootNode);
			motionBlurRenderPass.addMotionBlurSpatial(rootNode);
			// motionBlurRenderPass.addMotionBlurSpatial(torus);
			motionBlurRenderPass.setUseCurrentScene(true);
			motionBlurRenderPass.setBlurStrength(1f);
			pManager.add(motionBlurRenderPass);
		}
	}
	
//	public void addShadowPass(Node shadowNode){
//		shadowPass = new ShadowedRenderPass();
//		shadowPass.add(rootNode);
//        shadowPass.addOccluder(shadowNode);
//        shadowPass.setRenderShadows(true);
//        shadowPass.setShadowColor(new ColorRGBA(0.0f,0.0f,0.0f,0.5f));
//        shadowPass.setLightingMethod(ShadowedRenderPass.LightingMethod.Additive);
//       
//        
//        pManager.add(shadowPass);
//        new ShadowTweaker(shadowPass).setVisible(true);
//        
//        new Thread() {
//        	private SourceFunction lightPassBlendSrcFunction = BlendState.SourceFunction.OneMinusDestinationColor;
//			private DestinationFunction lightPassDstFunction = BlendState.DestinationFunction.One;
//			private SourceFunction texPassSrcFunction = BlendState.SourceFunction.DestinationColor;
//			private DestinationFunction texPassDstFunction = BlendState.DestinationFunction.Zero;
//
//			public void run() {
//        		while(ShadowedRenderPass.blended == null
//        				|| ShadowedRenderPass.blendTex == null) {
//        			try {
//						Thread.sleep(200);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//        		}
//
//				ShadowedRenderPass.blended.setSourceFunction(lightPassBlendSrcFunction );
//		        ShadowedRenderPass.blended.setDestinationFunction(lightPassDstFunction );
//		        ShadowedRenderPass.blendTex.setSourceFunction(texPassSrcFunction );
//				ShadowedRenderPass.blendTex.setDestinationFunction(texPassDstFunction );
//        	}
//        }.start();
//	}
	
	public void addFog() {
		float fogstart=100;
		float fogend=200;
		DisplaySystem display = DisplaySystem.getDisplaySystem();
        FogState fogState = display.getRenderer().createFogState();
        fogState.setEnabled(true);
        fogState.setColor(new ColorRGBA(0f, 0f, 0f, 1f));
        fogState.setDensity(0.1f);
        fogState.setEnd(fogend);
        fogState.setStart(fogstart);
        fogState.setDensityFunction(FogState.DensityFunction.Linear);
        fogState.setQuality(FogState.Quality.PerVertex);
        rootNode.setRenderState(fogState);
    }
	
	public void render(){
		pManager.renderPasses(DisplaySystem.getDisplaySystem().getRenderer());
	}
	
	public void cleanup(){
		if (bloomRenderPass != null)
            bloomRenderPass.cleanup();
        if (waterEffectRenderPass != null)
        	waterEffectRenderPass.cleanup();
        if (motionBlurRenderPass != null)
        	motionBlurRenderPass.cleanup();
	}
	
}
