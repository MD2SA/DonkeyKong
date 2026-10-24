package pt.iscte.poo;

import java.util.List;
import java.util.ArrayList;

import objects.animations.Animation;
import pt.iscte.poo.gui.ImageGUI;

public class AnimationList {

	private static AnimationList instance;

	private final List<Animation> oldAnimations = new ArrayList<>();
	private final List<Animation> newAnimations = new ArrayList<>();
	
	private AnimationList() {}
	
	public static AnimationList getInstance() {
		if ( instance == null )
			instance = new AnimationList();
		return instance;
	}

    public void addAnimation(Animation animation){
        if( animation != null )
        newAnimations.add(animation);
    }

    public void displayAll(){
        newAnimations.removeIf(n->{
            oldAnimations.add(n);
            ImageGUI.getInstance().addImage(n);
            return true;
        });
    }

    public void removeAll(){
        oldAnimations.removeIf(o->{
            ImageGUI.getInstance().removeImage(o);
            return true;
        });
    }
}
