package karsch.interfaces;

import karsch.characters.Karsch;

public interface Interactable {
	
	public void interact(Karsch karsch);
	
	public void stopInteraction(Karsch karsch);
}
