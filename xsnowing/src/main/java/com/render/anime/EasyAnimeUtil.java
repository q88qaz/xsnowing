package com.render.anime;

import android.view.View;

public class EasyAnimeUtil {

    /**
     * Attention
     */
    public static void bounce(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Attention.Bounce(v));
        render.start();
    }

    public static void flash(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Attention.Flash(v));
        render.start();
    }

    public static void pulse(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Attention.Pulse(v));
        render.start();
    }

    public static void ruberband(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Attention.RuberBand(v));
        render.start();
    }

    public static void shake(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Attention.Shake(v));
        render.start();
    }

    public static void standup(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Attention.StandUp(v));
        render.start();
    }

    public static void swing(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Attention.Swing(v));
        render.start();
    }

    public static void tada(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Attention.Tada(v));
        render.start();
    }

    public static void wave(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Attention.Wave(v));
        render.start();
    }

    public static void wobble(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Attention.Wobble(v));
        render.start();
    }

    /**
     * Bounce
     */
    public static void bounceInDown(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Bounce.InDown(v));
        render.start();
    }

    public static void bounceInUp(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Bounce.InUp(v));
        render.start();
    }

    public static void bounceInLeft(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Bounce.InLeft(v));
        render.start();
    }

    public static void bounceInRight(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Bounce.InRight(v));
        render.start();
    }

    public static void bounceIn(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Bounce.In(v));
        render.start();
    }

    /**
     * Fade
     */

    public static void fadeInDown(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Fade.InDown(v));
        render.start();
    }

    public static void fadeInUp(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Fade.InUp(v));
        render.start();
    }

    public static void fadeInLeft(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Fade.InLeft(v));
        render.start();
    }

    public static void fadeInRight(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Fade.InRight(v));
        render.start();
    }

    public static void fadeOutDown(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Fade.OutDown(v));
        render.start();
    }

    public static void fadeOutUp(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Fade.OutUp(v));
        render.start();
    }

    public static void fadeOutLeft(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Fade.OutLeft(v));
        render.start();
    }

    public static void fadeOutRight(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Fade.OutRight(v));
        render.start();
    }

    public static void fadeIn(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Fade.In(v));
        render.start();
    }

    public static void fadeOut(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Fade.OutUp(v));
        render.start();
    }

    /**
     * Flip
     */
    public static void flipInX(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Flip.InX(v));
        render.start();
    }

    public static void flipInY(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Flip.InY(v));
        render.start();
    }

    public static void flipOutX(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Flip.OutX(v));
        render.start();
    }

    public static void flipOutY(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Flip.OutY(v));
        render.start();
    }

    /**
     * Rotate
     */
    public static void rotateInDownLeft(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Rotate.InDownLeft(v));
        render.start();
    }

    public static void rotateInDownRight(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Rotate.InDownRight(v));
        render.start();
    }

    public static void rotateInUpLeft(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Rotate.InUpLeft(v));
        render.start();
    }

    public static void rotateInUpRight(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Rotate.InUpRight(v));
        render.start();
    }

    public static void rotateOutDownLeft(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Rotate.OutDownLeft(v));
        render.start();
    }

    public static void rotateOutDownRight(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Rotate.OutDownRight(v));
        render.start();
    }

    public static void rotateOutUpLeft(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Rotate.OutUpLeft(v));
        render.start();
    }

    public static void rotateOutUpRight(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Rotate.OutUpRight(v));
        render.start();
    }

    /**
     * Slide
     */
    public static void slideInDown(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Slide.InDown(v));
        render.start();
    }

    public static void slideInUp(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Slide.InUp(v));
        render.start();
    }

    public static void slideInLeft(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Slide.InLeft(v));
        render.start();
    }

    public static void slideInRight(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Slide.InRight(v));
        render.start();
    }

    public static void slideOutDown(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Slide.OutDown(v));
        render.start();
    }

    public static void slideOutUp(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Slide.OutUp(v));
        render.start();
    }

    public static void slideOutLeft(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Slide.OutLeft(v));
        render.start();
    }

    public static void slideOutRight(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Slide.OutRight(v));
        render.start();
    }

    /**
     * Zoom
     */
    public static void zoomInDown(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Zoom.InDown(v));
        render.start();
    }

    public static void zoomInUp(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Zoom.InUp(v));
        render.start();
    }

    public static void zoomInLeft(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Zoom.InLeft(v));
        render.start();
    }

    public static void zoomInRight(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Zoom.InRight(v));
        render.start();
    }

    public static void zoomOutDown(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Zoom.OutDown(v));
        render.start();
    }

    public static void zoomOutUp(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Zoom.OutUp(v));
        render.start();
    }

    public static void zoomOutLeft(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Zoom.OutLeft(v));
        render.start();
    }

    public static void zoomOutRight(View v) {
        if (isNoValidaView(v)) return;
        Render render = new Render(v.getContext());
        render.setAnimation(Zoom.OutRight(v));
        render.start();
    }

    /**
     * @param view
     * @return
     */
    private static boolean isNoValidaView(View view) {
        if (view == null || view.getContext() == null) return true;
        return false;
    }
}
