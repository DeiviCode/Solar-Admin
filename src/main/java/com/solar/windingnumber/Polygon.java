/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solar.windingnumber;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.*;
import org.springframework.stereotype.Component;


/**
 *
 * @author teleupb
 * @source http://geomalgorithms.com/a03-_inclusion.html#wn_PnPoly()
 * @dependencies org.json -> https://mvnrepository.com/artifact/org.json/json/20200518
 *
 */

@Component
public class Polygon {
    private Point[] points;

    public Polygon() throws IOException, URISyntaxException {
        start();
    }

    public Polygon(Point[] points) throws IOException, URISyntaxException {
        if (points.length > 0)
            this.points = points;
        else
            start();
    }

    public Point[] getPoints() {
        return points;
    }

    public void setPoints(Point[] points) {
        this.points = points;
    }
    
    private double isLeft(Point P0, Point P1, Point P2){
        return ( (P1.x - P0.x) * (P2.y - P0.y)
            - (P2.x -  P0.x) * (P1.y - P0.y) );
    }
    public int inPolygon(Point P){
        int wn = 0;
        int n = points.length;
        for (int i=0; i<n; i++) {   // edge from points[i] to  points[i+1]
        int j = i+1;
        if (j>=n)
            j = 0;
        if (points[i].y <= P.y) {          // start y <= P.y
            if (points[j].y  > P.y)      // an upward crossing
                 if (isLeft( points[i], points[j], P) > 0)  // P left of  edge
                     ++wn;            // have  a valid up intersect
        }
        else {                        // start y > P.y (no test needed)
            if (points[j].y  <= P.y)     // a downward crossing
                 if (isLeft( points[i], points[j], P) < 0)  // P right of  edge
                     --wn;            // have  a valid down intersect
        }
    }
    return wn;
    }
    private void start() throws FileNotFoundException, IOException, URISyntaxException{
    	URI uri = ClassLoader.getSystemResource("Metropolitana.border.geo.json").toURI();
    	String mainPath = Paths.get(uri).toString();
        String text = new String(Files.readAllBytes(Paths.get(mainPath)), StandardCharsets.UTF_8);
        JSONObject obj = new JSONObject(text);
        
        JSONArray array = obj.getJSONArray("geometries")
                .getJSONObject(0)
                .getJSONArray("coordinates")
                .getJSONArray(0);
        
        Point[] temp = new Point[array.length()];
        
        for (int i = 0; i < array.length(); i++) {
            JSONArray punto = array.getJSONArray(i);
            temp[i] = new Point(punto.getDouble(0),punto.getDouble(1));
        }
        
        this.points = temp;
        
    }
    
}
