package karsch.states;

import com.jme.renderer.pass.BasicPassManager;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.GameState;

/**
 * A Simple Wrapper GameState for the PassManager. 
 * updates and renders the RenderPasses.
 * @author Christoph Luder
 */
public class PassManagerGameState extends GameState {
    private BasicPassManager manager = null;
    
    public BasicPassManager getManager() {
        return manager;
    }
    public PassManagerGameState(int number) {
    	setName("PassManagerGamestate " + number);
        manager = new BasicPassManager();
    }
    @Override
    public void render(float tpf) {
    	manager.renderPasses(DisplaySystem.getDisplaySystem().getRenderer());
    }
    @Override
    public void update(float tpf) {
        manager.updatePasses(tpf);
    }
    @Override
    public void cleanup() {
    	manager.cleanUp();
    }
}
