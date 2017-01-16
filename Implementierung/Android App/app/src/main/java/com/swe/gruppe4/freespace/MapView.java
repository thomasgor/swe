package com.swe.gruppe4.freespace;

import android.content.Context;
import android.content.Intent;
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

/**
 * Diese Klasse dient zur darstellung der Karte auf dem "Karte" Tab im Home screen
 * sowie für die Navigation in der NavigationActivity. Als parameter bekommt sie die in XML layout definierten Attribute
 * Die Höhe der Karte ist auf 900dp ausgelegt alle berechnungen der elemente die hier gezeichnet werden arbeiten mit den 900dp
 * Die Höhe wird mit der Methode  GetDipsFromPixel(float pix) berechnet , die breite wird je nach Bildschirmgröße automatisch angepasst
 *
 * @author Kestutis Janavicius
 * @version 1.0
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
    private ArrayList<String> weg;
    private Paint mapPaint;
    private Paint startPaint;
    private Paint zielPaint;
    private Paint navigationPaint;
    private ArrayList<Raum> rooms;
    private SparseArray<Rect> roomMap;
    private ArrayList<RoomEnterance> roomEnterance;
    private static final String TAG = "MapView";
    public int opacity;
    public  String endPoint = "";
    public  String startPoint = "";
    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        opacity = 90;
        // Pinsel für die Navigation
        navigationPaint = new Paint();
        navigationPaint.setColor(Color.BLUE);
        navigationPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        navigationPaint.setStrokeWidth(20);
        // Pinsel für den start punkt
        startPaint = new Paint();
        startPaint.setColor(Color.GREEN);
        startPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        startPaint.setStrokeWidth(20);
        // Pinsel für den end punkt
        zielPaint = new Paint();
        zielPaint.setColor(Color.RED);
        zielPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        zielPaint.setStrokeWidth(20);
        // Info über die Räume aus der DB
        rooms = new RestConnection(context).raumGet();
        // Eingangspunkte zu den Räumen
        roomEnterance =  new RestConnection(context).raumEingangGet();
        // Rechtecke / Farben Map
        roomMap = new SparseArray<Rect>();
    }

    /**
     *  Die standard Methode um in Android Elemente (Buttons, Figuren etc) zu zeichnen
     *  Wird von der Activity aufgerufen die diese Custom View benutzt
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setCoordinates();
        drawMap(canvas);
        /**
         * Zeichne die Route ein falls ein start und ein endpunkt gegeben ist (dürfen nicht gleich sein)
         */
        if(!startPoint.equals("") && !endPoint.equals("")){
            weg = new RestConnection(context).wegGet(startPoint,endPoint);
            canvas.drawPath(drawNavigation(weg,canvas),navigationPaint);
        }

    }

    /**
     *  Wandelt die eingegebenen pixel in DPis um,
     *  diese Methode ist nur für den y (Höhe) Wert Relevant
     * @param pixels
     * @return
     */
    public int GetDipsFromPixel(float pixels)
    {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    /**
     * Falls diese Methode aufgerufen wurde werden die Start/End Punkte gesetzt und die navigation wird Eingezeichnet
     * Zurückgegeben wird die komplette View mit der eingezeichneten Route
     *
     * @param start
     * @param end
     * @return
     */
    public MapView startNavigation(String start, String end){
        startPoint = start;
        endPoint = end;
        return this;
    }

    /**
     * Diese Methode wandelt die speciellen Räume(G111,G107) so um,
     * dass diese zu den Punkten in dem RoomEnterence array richtig zugeordnet werden können.
     * Da diese Räume 2 eingänge haben müssen sie auch speciell  behandelt werden
     * @param weg
     */
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

    /**
     * Diese Methode erwartet die Flurknoten punkte aus dem Server sovie einen canvas aus der onDraw Methode von android
     * Mittels 3 schleifen werden die Start bzw Endpunkte rausgesucht und dazwischen die Flurknoten eingezeichnet um die Gesuchten Räume
     * mittels einer eingezeichneten Route zu verbinden
     * @param weg
     * @param canvas
     * @return
     */
    public Path drawNavigation(ArrayList<String> weg , Canvas canvas){
        speciaRooms(weg);
        String start = weg.get(0);
        String end = weg.get(weg.size()-1);
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
        //Ziel
        for(int i=0;i<roomEnterance.size();i++){
            if(roomEnterance.get(i).getName().equals(end)){
                canvas.drawCircle(getLeft()+(getRight()/roomEnterance.get(i).getX()),GetDipsFromPixel(roomEnterance.get(i).getY()),20,zielPaint);
                nav.lineTo(getLeft()+(getRight()/roomEnterance.get(i).getX()),GetDipsFromPixel(roomEnterance.get(i).getY()));
            }
        }

        return nav;
    }

    /**
     *  Die Methode setCoordinates() definiert die Rechtecke die auf der Karte eingezeichnet werden
     *  Sie dienen zur visualisierung des Status eines Raumes (Rot,Gelb,Grün,Grau)
     */
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

    /**
     * Die Methode zeichnet die vorhin definierten Rechtecke auf der Karte ein
     * jetzt werden sie auch eingefärbt und zwar mit dem Status der Räume aus der DB
     * @param canvas
     */
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

    /**
     * In dieser Methode wird abgefragt wo der benutzer auf der Karte gedrückt hat.
     * Je nachdem wo sich der onTouch event ereignet,
     * wird eine Activity gestartet wo die Raumdetails des passenden Raumes angezeigt werden
     * @param event
     * @return
     */
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

            if (x > getLeft()+(int)(getRight()/1.31) && x < getRight() && y > GetDipsFromPixel(200) && y < getBottom()-GetDipsFromPixel(480)) {
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

            if (x > getLeft()+(int)(getRight()/1.31) && x < getRight() && y > GetDipsFromPixel(525) && y < getBottom()-GetDipsFromPixel(150)) {
                Intent intent = new Intent(context, RoomDetailsActivity.class);
                intent.putExtra("id", 4);
                context.startActivity(intent);
            }
        }
        return true;
    }

}
