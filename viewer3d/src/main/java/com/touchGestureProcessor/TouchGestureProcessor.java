package com.touchGestureProcessor;

import android.graphics.PointF;
import android.opengl.Matrix;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;

public class TouchGestureProcessor
{
    public void         addTouchGestureListener (TouchGestureListener touchGestureListener)
    {
        this.touchGestureListener = touchGestureListener;
    }
    public void  touchEventHandle        (MotionEvent e)
    {
        switch ( MotionEventCompat.getActionMasked(e) )
        {
            case MotionEvent.ACTION_MOVE:
                someFingersAreMoving(e);
                break;
            case MotionEvent.ACTION_DOWN:
                firstFingerTouched(e);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                secondFinderTouched(e);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                prevSessionsDistance += currentSessionDistance;
                break;
        }
    }

    private int     getPointerId        (MotionEvent e)
    {
        int pointerIndex    = e.getActionIndex  ();
        int pointerId       = e.getPointerId    (pointerIndex);

        return pointerId;
    }
    private PointF  getPointerPosition  (MotionEvent e, int pointerId)
    {
        float x = e.getX(pointerId);
        float y = e.getY(pointerId);

        return new PointF(x, y);
    }
    private float   getPointersDistance (PointF pointer1, PointF pointer2)
    {
        float x = Math.abs(pointer1.x - pointer2.x);
        float y = Math.abs(pointer1.y - pointer2.y);

        return Matrix.length(x, y, 0.0f);
    }

    private void    firstFingerTouched  (MotionEvent e)
    {
        firstPointerId      = getPointerId(e);
        prevFirstPointerPos = getPointerPosition(e ,firstPointerId);
    }
    private void    secondFinderTouched (MotionEvent e)
    {
        //--save handle to second finger
        secondPointerId = getPointerId(e);

        //--get current position first and second finger on the screen
        PointF p1 = getPointerPosition(e, firstPointerId);
        PointF p2 = getPointerPosition(e, secondPointerId);

        //--calculate initial distance between fingers
        //--for scale gesture i would like to know how this distance
        //--is varying instead what is current distance
        initialDistance = getPointersDistance(p1, p2);
    }
    private void    someFingersAreMoving(MotionEvent e)
    {
        switch ( e.getPointerCount() )
        {
            case 1:
                moveGestureHandling(e);
                break;
            case 2:
                scaleGestureHandling(e, firstPointerId, secondPointerId);
                break;
            default:
                break;
        }
    }

    private void    moveGestureHandling (MotionEvent e)
    {
        PointF currentFirstPointerPos = new PointF( e.getX(), e.getY() );

        float dx = currentFirstPointerPos.x - prevFirstPointerPos.x;
        float dy = currentFirstPointerPos.y - prevFirstPointerPos.y;

        prevFirstPointerPos = currentFirstPointerPos;

        touchGestureListener.onMoveTouch(dx, dy);
    }
    private void    scaleGestureHandling(MotionEvent e, int firstPointerId, int secondPointerId)
    {
        PointF p1 = getPointerPosition(e, firstPointerId);
        PointF p2 = getPointerPosition(e, secondPointerId);

        float currentDistance   = getPointersDistance(p1, p2);
        currentSessionDistance  = currentDistance - initialDistance;

        float totalDistance     = prevSessionsDistance + currentSessionDistance;

        touchGestureListener.onScaleTouch(totalDistance);
    }

    private TouchGestureListener touchGestureListener;

    private PointF  prevFirstPointerPos;

    private int     firstPointerId
                    , secondPointerId;

    private float   initialDistance
                    , prevSessionsDistance
                    , currentSessionDistance;
}
