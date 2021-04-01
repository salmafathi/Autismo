package com.salma.loginlayout;

import android.os.Handler;

import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.ux.BaseGestureRecognizer;
import com.google.ar.sceneform.ux.BaseTransformableNode;
import com.google.ar.sceneform.ux.BaseTransformationController;
import com.google.ar.sceneform.ux.DragGesture;
import com.google.ar.sceneform.ux.DragGestureRecognizer;

public class DragRotationController extends BaseTransformationController<DragGesture>{

    private DragTransformableNode transformableNode;
    private DragGestureRecognizer gestureRecognizer ;
    private static Double initialLat = 13.145;
    private static Double initialLong = 9.1245;
    private float rotationRateDegrees = 0.1f ;

    Double lat  ;
    Double longe ;

    public DragRotationController( DragTransformableNode transformableNode1, DragGestureRecognizer gestureRecognizer1) {
        super(transformableNode1, gestureRecognizer1);
        this.transformableNode = transformableNode1;
        this.gestureRecognizer = gestureRecognizer1;
        lat = initialLat ;
        longe = initialLong;
    }



    @Override
    protected boolean canStartTransformation(DragGesture gesture) {
        return transformableNode.isSelected();
    }

    @Override
    protected void onContinueTransformation(DragGesture gesture) {
        float rotationAmountY = gesture.getDelta().y * rotationRateDegrees;
       float rotationAmountX = gesture.getDelta().x * rotationRateDegrees;
        Double deltaAngleY = Double.valueOf(rotationAmountY);
        Double deltaAngleX = Double.valueOf(rotationAmountX);
        longe -= deltaAngleX ;
        lat += deltaAngleY ;
        transformCamera(lat, longe);
    }

    @Override
    protected void onEndTransformation(DragGesture gesture) {

    }

    @Override
    public void onActivated(Node node) {
        super.onActivated(node);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                transformCamera(lat, longe);
            }
        }, 0);
    }


    private float getX(Double lat, Double longe){
        return (float) (transformableNode.raduis * Math.cos(Math.toRadians(lat)) * Math.sin(Math.toRadians(longe)));
    }

    private float getY(Double lat, Double longe){
        return (float) (transformableNode.raduis * Math.sin(Math.toRadians(lat)));
    }

    private float getZ(Double lat, Double longe){
        return (float) (transformableNode.raduis * Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(longe)));
    }

    private void transformCamera(Double lat,Double longe){

        Camera camera = transformableNode.getScene().getCamera();

        Quaternion rot = Quaternion.eulerAngles(new Vector3(0F, 0F, 0F));
        Vector3 pos =new Vector3(getX(lat, longe), getY(lat, longe), getZ(lat, longe));
        rot = Quaternion.multiply(rot, new Quaternion(Vector3.up(),longe.floatValue()));
        rot = Quaternion.multiply(rot, new Quaternion(Vector3.right(), -lat.floatValue()));
        camera.setLocalRotation(rot);
        camera.setLocalPosition(pos);

    }

    public void  resetInitialState() {
        transformCamera(initialLat, initialLong);
    }
}
