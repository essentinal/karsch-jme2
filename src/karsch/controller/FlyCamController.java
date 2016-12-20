package karsch.controller;

import karsch.Values;

import com.jme.curve.Curve;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.CameraNode;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;

public class FlyCamController extends Controller {
    private static final long serialVersionUID = 1L;
	private Spatial mover;
    private Curve curve;
    private Vector3f up = Vector3f.UNIT_Y;
    private Vector3f lookAt;
    private float currentTime = 0.0f;
    private float deltaTime = 0.0f;
    private Camera camToSet;
    private Node rootNode;
    private CameraNode camnode;

    private boolean cycleForward = true;
    
    public FlyCamController(Curve curve, Camera cam, Camera camToSet, Node rootNode) {
        this.curve = curve;
        camnode = new CameraNode("camnode", cam);
        this.mover = camnode;
        this.camToSet = camToSet;
        this.rootNode = rootNode;
        setMinTime(0);
        setMaxTime(Float.MAX_VALUE);
        setRepeatType(Controller.RT_CLAMP);
        setSpeed(.1f);
        DisplaySystem.getDisplaySystem().getRenderer().setCamera(cam);
        camnode.addController(this);
        rootNode.attachChild(camnode);
        Values.getInstance().getHudPass().setEnabled(false);
    }
    
    public FlyCamController(Curve curve, Spatial mover) {
        this.curve = curve;
        this.mover = mover;
        setMinTime(0);
        setMaxTime(Float.MAX_VALUE);
        setRepeatType(Controller.RT_CLAMP);
        setSpeed(1.0f);
    }

    public FlyCamController(
        Curve curve,
        Spatial mover,
        float minTime,
        float maxTime) {
    	
        this.curve = curve;
        this.mover = mover;
        setMinTime(minTime);
        setMaxTime(maxTime);
        setRepeatType(Controller.RT_CLAMP);
    }
    
    public void setLookAt(Vector3f lookAt){
    	this.lookAt = lookAt;
    }

    public void update(float time) {
        if(mover == null || curve == null || up == null) {
            return;
        }
        currentTime += time * getSpeed();

        if (currentTime >= getMinTime() && currentTime <= getMaxTime()) {

            if (getRepeatType() == RT_CLAMP) {
                deltaTime = currentTime - getMinTime();
                mover.setLocalTranslation(curve.getPoint(deltaTime,mover.getLocalTranslation()));
                if (lookAt != null) {
                	mover.lookAt(lookAt, up);
                }
            } else if (getRepeatType() == RT_WRAP) {
                deltaTime = (currentTime - getMinTime()) % 1.0f;
                if (deltaTime > 1) {
                    currentTime = 0;
                    deltaTime = 0;
                }
                mover.setLocalTranslation(curve.getPoint(deltaTime,mover.getLocalTranslation()));
                if (lookAt != null) {
                	mover.lookAt(lookAt, up);
                }
            } else if (getRepeatType() == RT_CYCLE) {
                float prevTime = deltaTime;
                deltaTime = (currentTime - getMinTime()) % 1.0f;
                if (prevTime > deltaTime) {
                    cycleForward = !cycleForward;
                }
                if (cycleForward) {
                    mover.setLocalTranslation(curve.getPoint(deltaTime,mover.getLocalTranslation()));
                    if (lookAt != null) {
                    	mover.lookAt(lookAt, up);
                    }
                } else {
                    mover.setLocalTranslation(
                        curve.getPoint(1.0f - deltaTime,mover.getLocalTranslation()));
                    if (lookAt != null) {
                    	mover.lookAt(lookAt, up);
                    }
                }
            } else {
                return;
            }
        } else {
        	System.out.println("flycamcontroller done");
        	DisplaySystem.getDisplaySystem().getRenderer().setCamera(camToSet);
        	setActive(false);
        	rootNode.detachChild(camnode);
        	camnode.removeController(this);
        	Values.getInstance().getHudPass().setEnabled(true);
        	Values.getInstance().getLevelGameState().startLevel();
        	try {
				finalize();
			} catch (Throwable e) {
				e.printStackTrace();
			}
        }
    }
}
