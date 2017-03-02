package com.rainbowclouds;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.UUID;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.Toast;

/**
 * Created by chandrasekaran on 26-02-2017.
 */

public class MainActivity extends AppCompatActivity implements OnClickListener
{

    private DrawingView drawView;

    private ImageView alphaImage;

    private float smallBrush, mediumBrush, largeBrush;

    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn;

    private int brushSize, eraserSize;

    private ArrayList<Bitmap> lstBitmaps;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawView = (DrawingView)findViewById(R.id.drawing);
        drawBtn = (ImageButton)findViewById(R.id.draw_btn);
        alphaImage = (ImageView)findViewById(R.id.alphaImage);
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        brushSize = (int) smallBrush;
        eraserSize = (int) smallBrush;
        drawBtn.setOnClickListener(this);
        eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);
        newBtn = (ImageButton)findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);
        saveBtn = (ImageButton)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        drawView.setBrushSize(smallBrush);

        lstBitmaps = new ArrayList<>();

    }

    public void paintClicked(View view){
        //use chosen color

        if(view!=currPaint){
            //update color
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;
        }
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.draw_btn){
            //draw button clicked
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            SeekBar brushSizeSelector = (SeekBar) brushDialog.findViewById(R.id.seekBar);
            brushSizeSelector.setProgress(brushSize);
            brushSizeSelector.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    brushSize = seekBar.getProgress();
                    drawView.setBrushSize(seekBar.getProgress());
                    drawView.setLastBrushSize(seekBar.getProgress());
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });
//            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
//            smallBtn.setOnClickListener(new OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    drawView.setBrushSize(smallBrush);
//                    drawView.setLastBrushSize(smallBrush);
//                    drawView.setErase(false);
//                    brushDialog.dismiss();
//                }
//            });
//            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
//            mediumBtn.setOnClickListener(new OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    drawView.setBrushSize(mediumBrush);
//                    drawView.setLastBrushSize(mediumBrush);
//                    drawView.setErase(false);
//                    brushDialog.dismiss();
//                }
//            });
//
//            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
//            largeBtn.setOnClickListener(new OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    drawView.setBrushSize(largeBrush);
//                    drawView.setLastBrushSize(largeBrush);
//                    drawView.setErase(false);
//                    brushDialog.dismiss();
//                }
//            });
            brushDialog.show();
        }else if(view.getId()==R.id.erase_btn){
            //switch to erase - choose size
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            SeekBar eraserSizeSelector = (SeekBar) brushDialog.findViewById(R.id.seekBar);
            eraserSizeSelector.setProgress(eraserSize);
            eraserSizeSelector.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    eraserSize = seekBar.getProgress();
                    drawView.setErase(true);
                    drawView.setBrushSize(seekBar.getProgress());
                    brushDialog.dismiss();
                }
            });
//            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
//            smallBtn.setOnClickListener(new OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    drawView.setErase(true);
//                    drawView.setBrushSize(smallBrush);
//                    brushDialog.dismiss();
//                }
//            });
//            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
//            mediumBtn.setOnClickListener(new OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    drawView.setErase(true);
//                    drawView.setBrushSize(mediumBrush);
//                    brushDialog.dismiss();
//                }
//            });
//            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
//            largeBtn.setOnClickListener(new OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    drawView.setErase(true);
//                    drawView.setBrushSize(largeBrush);
//                    brushDialog.dismiss();
//                }
//            });
            brushDialog.show();
        }else if(view.getId()==R.id.new_btn){
            //new button
            startTimer();
//            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
//            newDialog.setTitle("New drawing");
//            newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
//            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
//                public void onClick(DialogInterface dialog, int which){
//                    drawView.startNew();
//                    dialog.dismiss();
//                }
//            });
//            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
//                public void onClick(DialogInterface dialog, int which){
//                    dialog.cancel();
//                }
//            });
//            newDialog.show();

        }else if(view.getId()==R.id.save_btn){
            //save drawing
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    //save drawing
                    Bitmap paintedImage = loadBitmapFromView(drawView);
                    alphaImage.setImageBitmap(paintedImage);
                    lstBitmaps.add(paintedImage);
//                    String imgSaved = MediaStore.Images.Media.insertImage(
//                            getContentResolver(), drawView.getDrawingCache(),
//                            UUID.randomUUID().toString()+".png", "drawing");
//                    if(imgSaved!=null){
//                        Toast savedToast = Toast.makeText(getApplicationContext(),
//                                "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
//                        savedToast.show();
//                    }
//                    else{
//                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
//                                "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
//                        unsavedToast.show();
//                    }
                    drawView.startNew();
                    drawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();
            drawView.setDrawingCacheEnabled(true);

        }
    }
    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap( v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }

    private void startTimer() {
        AnimationDrawable anim = new AnimationDrawable();
        for(Bitmap bitmap : lstBitmaps)
        anim.addFrame(new BitmapDrawable(getResources(), bitmap),
                250);
        //......So on, so forth until you have a satisfying animation sequence


        //set ImageView to AnimatedDrawable
        alphaImage.setImageDrawable(anim);

        //if you want the animation to loop, set false
        anim.setOneShot(false);
        anim.start();
    }
}
