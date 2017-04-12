package com.rainbowclouds;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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

    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn, undoBtn, redoBtn;

    private int brushSize, eraserSize;

    private ArrayList<Bitmap> lstBitmaps;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawView = (DrawingView)findViewById(R.id.drawing);

        alphaImage = (ImageView)findViewById(R.id.alphaImage);
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        brushSize = (int) smallBrush;
        eraserSize = (int) smallBrush;

        drawBtn = (ImageButton)findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);
        eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);
        newBtn = (ImageButton)findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);
        saveBtn = (ImageButton)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);
        undoBtn = (ImageButton)findViewById(R.id.undo_btn);
        undoBtn.setOnClickListener(this);
        redoBtn = (ImageButton)findViewById(R.id.redo_btn);
        redoBtn.setOnClickListener(this);

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

        int id = view.getId();

        switch (id)
        {
            case R.id.draw_btn:
                //draw button clicked

                final SeekBar brushSizeSelector = (SeekBar) findViewById(R.id.brushSize);
                brushSizeSelector.setVisibility(View.VISIBLE);
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
                        brushSizeSelector.setVisibility(View.GONE);
                    }
                });
                break;
            case R.id.erase_btn:
                //switch to erase - choose size
                final SeekBar eraserSizeSelector = (SeekBar) findViewById(R.id.eraserSize);
                eraserSizeSelector.setVisibility(View.VISIBLE);
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
                        eraserSizeSelector.setVisibility(View.GONE);
                    }
                });
                break;
            case R.id.new_btn:
                //new button
                startTimer();
                FileOutputStream outStream = null;
                try{
                    String root = Environment.getExternalStorageDirectory().toString();
                    File file = new File(root + "/amazing.gif");
                    if (file.exists())
                    {
                        file.delete();
                    }
                    file.createNewFile();
                    outStream = new FileOutputStream(file);
                    outStream.write(generateGIF());
                    outStream.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.save_btn:
                //save drawing
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
                saveDialog.setTitle("Save drawing");
                saveDialog.setMessage("Save drawing to device Gallery?");
                saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        //save drawing
                        Bitmap paintedImage = loadBitmapFromView(drawView);
                        alphaImage.setImageBitmap(paintedImage);
                        alphaImage.setAlpha(0.3f);
                        lstBitmaps.add(paintedImage);
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
                break;
            case R.id.undo_btn:
//                drawView.undoPaint();
                break;
            case R.id.redo_btn:
//                drawView.redoPaint();
                break;
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

        for(Bitmap bitmap : lstBitmaps) {
            anim.addFrame(new BitmapDrawable(getResources(), bitmap), 250);
        }

        //set ImageView to AnimatedDrawable
        alphaImage.setImageDrawable(anim);
        alphaImage.setAlpha(1f);

        //if you want the animation to loop, set false
        anim.setOneShot(false);
        anim.start();
    }

    public byte[] generateGIF() {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(bos);
        for (Bitmap bitmap : lstBitmaps) {
            encoder.addFrame(bitmap);
        }
        encoder.finish();
        return bos.toByteArray();
    }
}
