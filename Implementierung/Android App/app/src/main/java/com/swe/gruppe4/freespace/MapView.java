package com.swe.gruppe4.freespace;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.swe.gruppe4.freespace.Objektklassen.Raum;
import com.swe.gruppe4.freespace.Objektklassen.RoomEnterance;

import java.util.ArrayList;

/**
 * Created by Kiesa on 03.01.2017.
 */

public class MapView extends ImageView {
    Context context;
    private Rect G101;
    private Rect G102;
    private Rect G115;
    private Rect G107;
    private Rect G116;
    private Rect G112;
    private Rect G111;
    ArrayList<String> weg;
    Paint mapPaint;
    Paint colorG101;
    Paint colorG102;
    Paint navigationPaint;
    ArrayList<Raum> rooms;
    private ArrayList<RoomEnterance> roomEnterance;
    private static final String TAG = "MapView";
    private int opacity;
    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        opacity = 90;
        navigationPaint = new Paint();
        navigationPaint.setColor(Color.BLUE);
        navigationPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        navigationPaint.setStrokeWidth(20);
        rooms = new RestConnection(context).raumGet();;
        roomEnterance =  new RestConnection(context).raumEingangGet();
        //initPaint(connection);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setCoordinates();
        drawMap(canvas);
        //G116
        //canvas.drawRect(0,GetDipsFromPixel(600),GetDipsFromPixel(133),getBottom(),colorG101);
        //G112
        //canvas.drawRect(0,GetDipsFromPixel(500),GetDipsFromPixel(133),getBottom()-GetDipsFromPixel(300),colorG102);
        //G111
        // canvas.drawRect(GetDipsFromPixel(250),GetDipsFromPixel(525),GetDipsFromPixel(330),getBottom()-GetDipsFromPixel(150),colorG101);
        //G107
        // canvas.drawRect(GetDipsFromPixel(250),GetDipsFromPixel(200),GetDipsFromPixel(330),getBottom()-GetDipsFromPixel(480),mapPaint);
        //G115
        //canvas.drawRect(GetDipsFromPixel(130),GetDipsFromPixel(750),GetDipsFromPixel(430),getBottom(),colorG101);
        //canvas.drawRect(
        //        getLeft()+(getRight()-getLeft())/3,
        //        getTop()+(getBottom()-getTop())/3,
        //        getRight()-(getRight()-getLeft())/3,
        //        getBottom()-(getBottom()-getTop())/3,mapPaint);
        //weg = new RestConnection(context).wegGet("G102","G116");
        //canvas.drawPath(drawFloorNodes(weg),navigationPaint);
        //canvas.drawPath(drawNavigation(),navigationPaint);
        //canvas.drawRect(GetDipsFromPixel(280),0,GetDipsFromPixel(200),GetDipsFromPixel(190),mapPaint);

    }

    public int GetDipsFromPixel(float pixels)
    {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    private void speciaRooms(ArrayList<String> weg){
        //Start
        if(weg.get(0).equals("G107") && weg.get(1).equals("240")){
            weg.remove(0);
            weg.add(0,"G1070");
        }

        if(weg.get(0).equals("G107") && weg.get(1).equals("380")){
            weg.remove(0);
            weg.add(0,"G1071");
        }

        if(weg.get(0).equals("G111") && weg.get(1).equals("550")){
            weg.remove(0);
            weg.add(0,"G1110");
        }

        if(weg.get(0).equals("G111") && weg.get(1).equals("700")){
            weg.remove(0);
            weg.add(0,"G1111");
        }
        //Ende
        if(weg.get(weg.size()-1).equals("G107") && weg.get(weg.size()-2).equals("240")){
            weg.add(weg.size()-1,"G1070");
            weg.remove(weg.size()-1);
        }

        if(weg.get(weg.size()-1).equals("G107") && weg.get(weg.size()-2).equals("380")){
            weg.add(weg.size()-1,"G1071");
            weg.remove(weg.size()-1);
        }

        if(weg.get(weg.size()-1).equals("G111") && weg.get(weg.size()-2).equals("550")){
            weg.add(weg.size()-1,"G1110");
            weg.remove(weg.size()-1);
        }
        if(weg.get(weg.size()-1).equals("G111") && weg.get(weg.size()-2).equals("700")){
            weg.add(weg.size()-1,"G1111");
            weg.remove(weg.size()-1);
        }

    }

    public Path drawFloorNodes(ArrayList<String> weg){
        speciaRooms(weg);
        String start = weg.get(0);
        String end = weg.get(weg.size()-1);
        //String start = "G102";
        // String end = "G107";
        Path nav = new Path();
        //Start
        for(int i=0;i<roomEnterance.size();i++){
            if(roomEnterance.get(i).getName().equals(start)){
                nav.moveTo(getLeft()+(getRight()/roomEnterance.get(i).getX()),GetDipsFromPixel(roomEnterance.get(i).getY()));
            }
        }
        //Knoten Punkte
        for(int i=1;i<weg.size()-1;i++){
            nav.lineTo(getLeft()+(getRight()/2),GetDipsFromPixel(Float.valueOf(weg.get(i))));
            nav.moveTo(getLeft()+(getRight()/2),GetDipsFromPixel(Float.valueOf(weg.get(i))));
        }
        // getNodes(nav);
        //Ziel
        for(int i=0;i<roomEnterance.size();i++){
            if(roomEnterance.get(i).getName().equals(end)){
                nav.lineTo(getLeft()+(getRight()/roomEnterance.get(i).getX()),GetDipsFromPixel(roomEnterance.get(i).getY()));
            }
        }

        return nav;
    }

    private void getNodes(Path nav) {
        nav.moveTo(getLeft()+(getRight()/2),GetDipsFromPixel(240));
        nav.lineTo(getLeft()+(getRight()/2),GetDipsFromPixel(380));
        nav.moveTo(getLeft()+(getRight()/2),GetDipsFromPixel(380));
        nav.lineTo(getLeft()+(getRight()/2),GetDipsFromPixel(450));
        nav.moveTo(getLeft()+(getRight()/2),GetDipsFromPixel(450));
        nav.lineTo(getLeft()+(getRight()/2),GetDipsFromPixel(510));
        nav.moveTo(getLeft()+(getRight()/2),GetDipsFromPixel(510));
        nav.lineTo(getLeft()+(getRight()/2),GetDipsFromPixel(550));
        nav.moveTo(getLeft()+(getRight()/2),GetDipsFromPixel(550));
        nav.lineTo(getLeft()+(getRight()/2),GetDipsFromPixel(700));
        nav.moveTo(getLeft()+(getRight()/2),GetDipsFromPixel(700));
    }

    public Path drawNavigation(){
        Path nav = new Path();
        nav.moveTo(getLeft()+(float)(getRight()/2.5),GetDipsFromPixel(700));
        nav.lineTo(getLeft()+(float)(getRight()/1.3),GetDipsFromPixel(700));
        return nav;
    }


    private void setCoordinates(){
        G101 = new Rect(getLeft()+(int)(getRight()/2.5),0,getRight(),GetDipsFromPixel(195));
        G102 = new Rect(0,0,getLeft()+(int)(getRight()/2.43),GetDipsFromPixel(280));
        G107 = new Rect(getLeft()+(int)(getRight()/1.31),GetDipsFromPixel(195),getRight(),getBottom()-GetDipsFromPixel(480));
        G111 = new Rect(getLeft()+(int)(getRight()/1.31),GetDipsFromPixel(525),getRight(),getBottom()-GetDipsFromPixel(150));
        G112 = new Rect(0,GetDipsFromPixel(500),getLeft()+(int)(getRight()/2.5),getBottom()-GetDipsFromPixel(300));
        G115 = new Rect(getLeft()+(int)(getRight()/2.5),GetDipsFromPixel(750),getRight(),getBottom());
        G116 = new Rect(0,GetDipsFromPixel(600),getLeft()+(int)(getRight()/2.5),getBottom());
    }

    private void drawMap(Canvas canvas){
        mapPaint = new Paint();
        for(Raum r : rooms){
            switch (r.getId()){
                case 1:
                    if(r.getStatus().equals("grün")){
                        mapPaint.setColor(Color.GREEN);
                    } else if(r.getStatus().equals("gelb")){
                        mapPaint.setColor(Color.YELLOW);
                    } else if(r.getStatus().equals("rot")){
                        mapPaint.setColor(Color.RED);
                    }
                    mapPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                    mapPaint.setAlpha(opacity);
                    canvas.drawRect(G101, mapPaint);
                    break;
                case 2:
                    if(r.getStatus().equals("grün")){
                        mapPaint.setColor(Color.GREEN);
                    } else if(r.getStatus().equals("gelb")){
                        mapPaint.setColor(Color.YELLOW);
                    } else if(r.getStatus().equals("rot")){
                        mapPaint.setColor(Color.RED);
                    }
                    mapPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                    mapPaint.setAlpha(opacity);
                    canvas.drawRect(G102, mapPaint);
                    break;
                case 3:
                    if(r.getStatus().equals("grün")){
                        mapPaint.setColor(Color.GREEN);
                    } else if(r.getStatus().equals("gelb")){
                        mapPaint.setColor(Color.YELLOW);
                    } else if(r.getStatus().equals("rot")){
                        mapPaint.setColor(Color.RED);
                    }
                    mapPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                    mapPaint.setAlpha(opacity);
                    canvas.drawRect(G107, mapPaint);
                    break;
                case 4:
                    if(r.getStatus().equals("grün")){
                        mapPaint.setColor(Color.GREEN);
                    } else if(r.getStatus().equals("gelb")){
                        mapPaint.setColor(Color.YELLOW);
                    } else if(r.getStatus().equals("rot")){
                        mapPaint.setColor(Color.RED);
                    }
                    mapPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                    mapPaint.setAlpha(opacity);
                    canvas.drawRect(G111, mapPaint);
                    break;
                case 5:
                    if(r.getStatus().equals("grün")){
                        mapPaint.setColor(Color.GREEN);
                    } else if(r.getStatus().equals("gelb")){
                        mapPaint.setColor(Color.YELLOW);
                    } else if(r.getStatus().equals("rot")){
                        mapPaint.setColor(Color.RED);
                    }
                    mapPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                    mapPaint.setAlpha(opacity);
                    canvas.drawRect(G112, mapPaint);
                    break;
                case 6:
                    if(r.getStatus().equals("grün")){
                        mapPaint.setColor(Color.GREEN);
                    } else if(r.getStatus().equals("gelb")){
                        mapPaint.setColor(Color.YELLOW);
                    } else if(r.getStatus().equals("rot")){
                        mapPaint.setColor(Color.RED);
                    }
                    mapPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                    mapPaint.setAlpha(opacity);
                    canvas.drawRect(G115, mapPaint);
                    break;
                case 7:
                    if(r.getStatus().equals("grün")){
                        mapPaint.setColor(Color.GREEN);
                    } else if(r.getStatus().equals("gelb")){
                        mapPaint.setColor(Color.YELLOW);
                    } else if(r.getStatus().equals("rot")){
                        mapPaint.setColor(Color.RED);
                    }
                    mapPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                    mapPaint.setAlpha(opacity);
                    canvas.drawRect(G116, mapPaint);
                    break;

            }
        }
    }

    private void initPaint(VerbindungDUMMY connection){
        ArrayList<Raum> rooms = connection.raumGet();
        mapPaint = new Paint();
        colorG101 = new Paint();
        // Line color
        colorG101.setColor(Color.RED);
        colorG101.setStyle(Paint.Style.FILL);
        colorG101.setAlpha(80);

        colorG102 = new Paint();
        // Line color
        colorG102.setColor(Color.YELLOW);
        colorG102.setStyle(Paint.Style.FILL_AND_STROKE);
        colorG102.setAlpha(80);

        mapPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mapPaint.setAlpha(60);
        // Line width in pixels
        mapPaint.setStrokeWidth(8);
        mapPaint.setAntiAlias(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        //Toast.makeText(getApplicationContext(),"X: "+x+" Y: "+y,Toast.LENGTH_SHORT).show();
        if( event.getAction() == MotionEvent.ACTION_UP) {
            if (x > 0 && x < getLeft()+(int)(getRight()/2.43) && y > 0 && y < GetDipsFromPixel(280)) {
                // for (Raum r:räume) {
                //     if( r.getId() == 4711){
                Intent intent = new Intent(context, RoomDetailsActivity.class);
                intent.putExtra("id", 2);
                context.startActivity(intent);
                //    }
                //  }
            }
            if (x > getLeft()+(int)(getRight()/2.43) && x < GetDipsFromPixel(335) && y > 0 && y < GetDipsFromPixel(200)) {
                Intent intent = new Intent(context, RoomDetailsActivity.class);
                intent.putExtra("id", 1);
                context.startActivity(intent);
            }

            if (x > GetDipsFromPixel(250) && x < GetDipsFromPixel(335) && y > GetDipsFromPixel(200) && y < getBottom()-GetDipsFromPixel(480)) {
                Intent intent = new Intent(context, RoomDetailsActivity.class);
                intent.putExtra("id", 3);
                context.startActivity(intent);
            }
            if (x > 0 && x < getLeft()+(int)(getRight()/2.43) && y > GetDipsFromPixel(600) && y < getBottom()) {
                Intent intent = new Intent(context, RoomDetailsActivity.class);
                intent.putExtra("id", 7);
                context.startActivity(intent);
            }
            if (x > getLeft()+(int)(getRight()/2.43) && x < GetDipsFromPixel(430) && y > GetDipsFromPixel(750) && y < getBottom()) {
                Intent intent = new Intent(context, RoomDetailsActivity.class);
                intent.putExtra("id", 6);
                context.startActivity(intent);
            }

            if (x > 0 && x < getLeft()+(int)(getRight()/2.43) && y > GetDipsFromPixel(500) && y < getBottom()-GetDipsFromPixel(300)) {
                Intent intent = new Intent(context, RoomDetailsActivity.class);
                intent.putExtra("id", 5);
                context.startActivity(intent);
            }

            if (x > GetDipsFromPixel(250) && x < GetDipsFromPixel(330) && y > GetDipsFromPixel(525) && y < getBottom()-GetDipsFromPixel(150)) {
                Intent intent = new Intent(context, RoomDetailsActivity.class);
                intent.putExtra("id", 4);
                context.startActivity(intent);
            }
        }
        /**
         switch (event.getAction()) {

         case MotionEvent.ACTION_DOWN:
         //Check if the x and y position of the touch is inside the bitmap
         if (x > 0 && x < GetDipsFromPixel(135) && y > 0 && y < GetDipsFromPixel(280)) {
         // for (Raum r:räume) {
         //     if( r.getId() == 4711){
         Intent intent = new Intent(context, RoomDetailsActivity.class);
         intent.putExtra("id", 4711);
         context.startActivity(intent);
         //    }
         //  }
         }

         if (x > GetDipsFromPixel(135) && x < GetDipsFromPixel(335) && y > 0 && y < GetDipsFromPixel(200)) {
         Intent intent = new Intent(context, RoomDetailsActivity.class);
         intent.putExtra("id", 101);
         context.startActivity(intent);
         }
         //Toast.makeText(context,"X: "+GetDipsFromPixel(x)+" Y: "+GetDipsFromPixel(y),Toast.LENGTH_SHORT).show();
         Log.d(TAG, "Pixels : " + "X: " + x + " Y: " + y);
         return false;
         }
         **/
        return true;
    }
}
