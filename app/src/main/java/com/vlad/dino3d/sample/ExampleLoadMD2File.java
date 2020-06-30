package com.vlad.dino3d.sample;

import com.vlad.dino3d.min3d.animation.AnimationObject3d;
import com.vlad.dino3d.min3d.core.RendererActivity;
import com.vlad.dino3d.min3d.parser.IParser;
import com.vlad.dino3d.min3d.parser.Parser;
import com.vlad.dino3d.min3d.vos.Light;

public class ExampleLoadMD2File extends RendererActivity {
	private AnimationObject3d ogre;

	@Override
	public void initScene() {
		
		scene.lights().add(new Light());
		
		IParser parser = Parser.createParser(Parser.Type.MD2,
				getResources(), "com.vlad.dino3d:raw/ogro", false);
		parser.parse();

		ogre = parser.getParsedAnimationObject();
		ogre.scale().x = ogre.scale().y = ogre.scale().z = .07f;
		ogre.rotation().z = -90;
		ogre.rotation().x = -90;
		scene.addChild(ogre);
		ogre.setFps(70);
		ogre.play();
	}

	@Override
	public void updateScene() {

	}

}
