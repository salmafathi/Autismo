package com.salma.loginlayout;

import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.ux.TransformationSystem;
import com.salma.loginlayout.activites.ViewModel;

public class DragTransformableNode extends TransformableNode {

    public float raduis;

    public DragTransformableNode(Float radius , TransformationSystem transformationSystem) {
        super(transformationSystem);
        this.raduis = radius ;
        new DragRotationController(this, transformationSystem.getDragRecognizer());
    }
}
