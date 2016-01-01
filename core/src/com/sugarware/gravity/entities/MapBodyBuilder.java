package com.sugarware.gravity.entities;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.sugarware.gravity.CollisionBits;
import com.sugarware.gravity.Console;
import com.sugarware.gravity.GameStateManager;
import com.sugarware.gravity.levels.PlayState;

import box2dLight.ConeLight;
import box2dLight.RayHandler;

public class MapBodyBuilder {

    // The pixels per tile. If your tiles are 16x16, this is set to 16f
    private static final float ppt = 8;

    public static Array<Body> buildShapes(TiledMap map, World world) {
       
        MapObjects objects = map.getLayers().get("Collisions").getObjects();

        Array<Body> bodies = new Array<Body>();

        for(MapObject object : objects) {

            if (object instanceof TextureMapObject) {
                continue;
            }

            Shape shape;

            if (object instanceof RectangleMapObject) {
                shape = getRectangle((RectangleMapObject)object);
            }
            else if (object instanceof PolygonMapObject) {
                shape = getPolygon((PolygonMapObject)object);
            }
            else if (object instanceof PolylineMapObject) {
                shape = getPolyline((PolylineMapObject)object);
            }
            else if (object instanceof CircleMapObject) {
                shape = getCircle((CircleMapObject)object);
            }
            else {
                continue;
            }

            BodyDef bd = new BodyDef();
            bd.type = BodyType.StaticBody;
            Body body = world.createBody(bd);
            body.createFixture(shape, 1);
           
            body.setUserData("world");
            bodies.add(body);

            shape.dispose();
        }
        return bodies;
    }

    
    private static Vector2 rectToCoords(Vector2 coords,Rectangle rectangle){
    	coords.x = (rectangle.x + rectangle.width * 0.5f) / ppt;
    	coords.y = (rectangle.y + rectangle.height * 0.5f ) / ppt;
    	return coords;
    }
    
    private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / ppt,
                                   (rectangle.y + rectangle.height * 0.5f ) / ppt);
        polygon.setAsBox(rectangle.width * 0.5f / ppt,
                         rectangle.height * 0.5f / ppt,
                         size,
                         0.0f);
        return polygon;
    }

    private static CircleShape getCircle(CircleMapObject circleObject) {
        Circle circle = circleObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius / ppt);
        circleShape.setPosition(new Vector2(circle.x / ppt, circle.y / ppt));
        return circleShape;
    }

    private static PolygonShape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            System.out.println(vertices[i]);
            worldVertices[i] = vertices[i] / ppt;
        }

        polygon.set(worldVertices);
        return polygon;
    }

    private static ChainShape getPolyline(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] / ppt;
            worldVertices[i].y = vertices[i * 2 + 1] / ppt;
        }

        ChainShape chain = new ChainShape(); 
        chain.createChain(worldVertices);
        return chain;
    }

    
    private static Vector2 coords = new Vector2();
	public static void buildEntities(PlayState gs) {
		  MapObjects objects = gs.tilemap.getLayers().get("Entities").getObjects();
		  RayHandler rh = gs.rayHandler;
		  for(MapObject object : objects) {
			  if(object.getName() !=null){
				  String n = object.getName();
				  String[] pts = n.split(" ");
				  
				  if(object instanceof RectangleMapObject){
					  coords = rectToCoords(coords,((RectangleMapObject) object).getRectangle());
					 float w= ((RectangleMapObject) object).getRectangle().getWidth();
					 float h= ((RectangleMapObject) object).getRectangle().getHeight();
					 
					  
						if(pts[0].equals("box")){
							
							Box b = new Box((PlayState) GameStateManager.getInstance().currentState, coords.x, coords.y, w/ppt,h/ppt
									);
							((PlayState)GameStateManager.getInstance().currentState).entities.add(b);
							continue;
						}
						  
				
					  
					if(pts[0].equals("gswitch")){
						
								 Console.cmd("add switch " +
										 		coords.x +" " + coords.y + " " +
										 		pts[1] + " " + pts[2]);			
								 continue;
				  	}
				 
				  
				  
					if(pts[0].equals("light")){
						//int nRays = Integer.parseInt(pts[1]);
					
						int dist = Integer.parseInt(pts[1]);
						Color col;
						int dir;
						int deg;
						if(pts.length >= 6){
							 col = colFromString(pts[2] + " " + pts[3] + " " + 
									                  pts[4] + " " +  "0.8" );
							 
							 dir = Integer.parseInt(pts[6]);
							 deg = Integer.parseInt(pts[7]);
						}else{
							 col = colFromString(pts[2]);
							 dir = Integer.parseInt(pts[3]);
							 deg = Integer.parseInt(pts[4]);
						}
						
						ConeLight light = new ConeLight(rh, 150, col, dist, (int)coords.x, (int)coords.y, dir, deg);
						light.setContactFilter(CollisionBits.CATEGORY_LIGHT, (short) 0, CollisionBits.MASK_LIGHT);
						continue;
						
					}}
					
				  
				  
				  
				  
				  
				  
				  }
				  
				  
				  
				  
				  
			  }
		  }
		 
		
	


	private static Color colFromString(String string) {
		if(string.equalsIgnoreCase("red")){
			return Color.RED;
		}else if(string.equalsIgnoreCase("blue")){
			return Color.BLUE;
		}else if(string.equalsIgnoreCase("white")){
			return Color.WHITE;
		}else if(string.equalsIgnoreCase("green")){
			return Color.GREEN;
		}else if(string.equalsIgnoreCase("black")){
			return Color.BLACK;
		}else if(string.equalsIgnoreCase("orange")){
			return Color.ORANGE;
		}else if(string.equalsIgnoreCase("yellow")){
			return Color.YELLOW;
		}else if(string.equalsIgnoreCase("purple")){
			return Color.PURPLE;
		}else if(string.split(" ").length == 4){
			String[] pts = string.split(" ");
			float r = Float.parseFloat(pts[0]),
				  g = Float.parseFloat(pts[1]),
				  b = Float.parseFloat(pts[2]),
				  a = Float.parseFloat(pts[3]);
			return new Color(r, g, b, a);
		}
		return null;
	}


	
}