package com.swe.gruppe4.freespace;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.swe.gruppe4.freespace.Objektklassen.Raum;
import com.swe.gruppe4.freespace.Objektklassen.RoomEnterance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    Paint startPaint;
    Paint zielPaint;
    Paint navigationPaint;
    ArrayList<Raum> rooms;
    SparseArray<Rect> roomMap;
    private ArrayList<RoomEnterance> roomEnterance;
    private static final String TAG = "MapView";
    public int opacity;
    public  String endPoint = "";
    public  String startPoint = "";
    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        opacity = 90;
        navigationPaint = new Paint();
        navigationPaint.setColor(Color.BLUE);
        navigationPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        navigationPaint.setStrokeWidth(20);

        startPaint = new Paint();
        startPaint.setColor(Color.GREEN);
        startPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        startPaint.setStrokeWidth(20);

        zielPaint = new Paint();
        zielPaint.setColor(Color.RED);
        zielPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        zielPaint.setStrokeWidth(20);
        rooms = new RestConnection(context).raumGet();
        roomEnterance =  new RestConnection(context).raumEingangGet();
        roomMap = new SparseArray<Rect>();
        //initPaint(connection);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setCoordinates();
        drawMap(canvas);
        if(!startPoint.equals("") && !endPoint.equals("")){
            weg = new RestConnection(context).wegGet(startPoint,endPoint);
            canvas.drawPath(drawFloorNodes(weg,canvas),navigationPaint);
        }

    }

    public int GetDipsFromPixel(float pixels)
    {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    public MapView startNavigation(String start, String end){
        startPoint = start;
        endPoint = end;
        return this;
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

    public Path drawFloorNodes(ArrayList<String> weg , Canvas canvas){
        speciaRooms(weg);
        String start = weg.get(0);
        String end = weg.get(weg.size()-1);
        //String start = "G102";
        // String end = "G107";
        Path nav = new Path();
        //Start
        for(int i=0;i<roomEnterance.size();i++){
            if(roomEnterance.get(i).getName().equals(start)){
                canvas.drawCircle(getLeft()+(getRight()/roomEnterance.get(i).getX()),GetDipsFromPixel(roomEnterance.get(i).getY()),20,startPaint);
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
                canvas.drawCircle(getLeft()+(getRight()/roomEnterance.get(i).getX()),GetDipsFromPixel(roomEnterance.get(i).getY()),20,zielPaint);
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
        roomMap.put(1,G101);
        G102 = new Rect(0,0,getLeft()+(int)(getRight()/2.43),GetDipsFromPixel(280));
        roomMap.put(2,G102);
        G107 = new Rect(getLeft()+(int)(getRight()/1.31),GetDipsFromPixel(195),getRight(),getBottom()-GetDipsFromPixel(480));
        roomMap.put(3,G107);
        G111 = new Rect(getLeft()+(int)(getRight()/1.31),GetDipsFromPixel(525),getRight(),getBottom()-GetDipsFromPixel(150));
        roomMap.put(4,G111);
        G112 = new Rect(0,GetDipsFromPixel(500),getLeft()+(int)(getRight()/2.5),getBottom()-GetDipsFromPixel(300));
        roomMap.put(5,G112);
        G115 = new Rect(getLeft()+(int)(getRight()/2.5),GetDipsFromPixel(750),getRight(),getBottom());
        roomMap.put(6,G115);
        G116 = new Rect(0,GetDipsFromPixel(600),getLeft()+(int)(getRight()/2.5),getBottom());
        roomMap.put(7,G116);
    }

    private void drawMap(Canvas canvas){
        mapPaint = new Paint();
        for(Raum r : rooms){
            switch (r.getStatus()) {
                case "grün":
                    mapPaint.setColor(Color.GREEN);
                    break;
                case "gelb":
                    mapPaint.setColor(Color.YELLOW);
                    break;
                case "rot":
                    mapPaint.setColor(Color.RED);
                    break;
                default:
                    mapPaint.setColor(Color.BLACK);
                    break;
            }
            mapPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mapPaint.setAlpha(opacity);
            canvas.drawRect(roomMap.get(r.getId()),mapPaint);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if( event.getAction() == MotionEvent.ACTION_UP) {
            if (x > 0 && x < getLeft()+(int)(getRight()/2.43) && y > 0 && y < GetDipsFromPixel(280)) {
                Intent intent = new Intent(context, RoomDetailsActivity.class);
                intent.putExtra("id", 2);
                context.startActivity(intent);
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
        return true;
    }

}
