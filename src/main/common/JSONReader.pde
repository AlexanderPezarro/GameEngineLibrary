import java.io.FileReader;
import java.util.*;
import java.io.*;

public final class JSONReader {

    //may need to hard-code dimensions of tileset

    static final int TILE_SIZE = 32;
    final int TILESET_WIDTH = 6;

    //objects
    PVector startPos;
    BoundingBox endPos;
    ArrayList<Enemy> enemies;
    ArrayList<Spikes> spikes;

    Player playerRef;

    public Level readTilemap(String name, String levelPath, String tilesetPath, Player playerRef) {

        this.playerRef = playerRef;
        JSONObject levelJSON = loadJSONObject(levelPath);
        int levelHeight = levelJSON.getInt("height");
        int levelWidth = levelJSON.getInt("width");

        JSONArray layersJSON = levelJSON.getJSONArray("layers");
        JSONArray tilemapJSON = layersJSON.getJSONObject(0).getJSONArray("data");
        JSONArray objectsJSON = layersJSON.getJSONObject(1).getJSONArray("objects");

        //Need to convert this array into a 2D int array. Each array element is an array representing
        //a row of tiles. Each element in that array is a column position.
        //i.e. [y[x,x,...,x], y[x,x,...,x], ..., y[x,x,...,x]]

        Tile[][] tilemap = new Tile[levelHeight][levelWidth];

        PImage tileset = loadImage(tilesetPath);

        int tileVal;
        //fill out tile map sprites and collision
        for (int i = 0; i < tilemapJSON.size() && i / levelWidth < levelHeight; i++) {
            int tileX = i % levelWidth;
            int tileY = i / levelWidth;

            tileVal = tilemapJSON.getInt(i) % 36;
            int[] tilesetOffsets = getOffsets(tileVal);

            PVector location = new PVector(tileX * TILE_SIZE, tileY * TILE_SIZE);
            ArrayList<BoundingShape> collisionShapes = setCollisionShapes(tileVal, location);
            tilemap[tileY][tileX] = new Tile(tileset, tilesetOffsets[0], tilesetOffsets[1], collisionShapes, location);
        }
        
        JSONObject currentObj;
        enemies = new ArrayList<Enemy>();
        spikes = new ArrayList<Spikes>();
        readObjects(objectsJSON);

        return new Level(name, tilemap, playerRef, startPos, endPos, spikes, enemies);
    }

    void draw() {
        
    }

    //MAKE SURE ALL TILESETS ARE JUST TEXTURE SWAPS OF THE SAME ASSETS
    public int[] getOffsets(int tileNum) {

        int heightOffset = tileNum / TILESET_WIDTH * TILE_SIZE;
        if(tileNum % TILESET_WIDTH == 0) heightOffset -= TILE_SIZE;

        int widthOffset = tileNum % TILESET_WIDTH;
        if(widthOffset == 0) widthOffset = TILESET_WIDTH - 1; else widthOffset--;
        widthOffset *= TILE_SIZE;
        
        int[] offsets = {widthOffset, heightOffset};
        return offsets;
    }

    //give each tile a set of bounding shapes based on its shape
    //TODO: Update for spikes
    public ArrayList<BoundingShape> setCollisionShapes(int tileNum, PVector location) {

        ArrayList<BoundingShape> collisionShapes = new ArrayList<BoundingShape>();
        switch(tileNum) {
            case 2:
            case 6:
            case 12:
            case 13:
            case 14:
                {//all of these just need a 32x32 BoundingBox covering the whole tile
                PVector topLeft = location.copy();
                PVector topRight = new PVector(location.x + TILE_SIZE, location.y);
                PVector bottomLeft = new PVector(location.x, location.y + TILE_SIZE);
                BoundingBox collBox = new BoundingBox(topLeft, topRight, bottomLeft);
                collisionShapes.add(collBox);
                break;}
            case 3:
            case 4:
            case 5:
                {//all of these need a 32x4 BoundingBox on the bottom of the tile area
                PVector topLeft = new PVector(location.x, location.y + TILE_SIZE - 3);
                PVector topRight = new PVector(location.x + TILE_SIZE, location.y + TILE_SIZE - 3);
                PVector bottomLeft = new PVector(location.x, location.y + TILE_SIZE);
                BoundingBox collBox = new BoundingBox(topLeft, topRight, bottomLeft);
                collisionShapes.add(collBox);
                break;}
            case 15:
            case 16:
            case 17:
                    {//all of these need a 32x4 BoundingBox on the top of the tile area
                    PVector topLeft = location.copy();
                    PVector topRight = new PVector(location.x + TILE_SIZE, location.y);
                    PVector bottomLeft = new PVector(location.x, location.y + 4);
                    BoundingBox collBox = new BoundingBox(topLeft, topRight, bottomLeft);
                    collisionShapes.add(collBox);
                    break;}
            case 18:
            case 19:
            case 20:
                {//all of these need a 4x32 BoundingBox on the right of the tile area
                PVector topLeft = new PVector(location.x + TILE_SIZE - 4, location.y);
                PVector topRight = new PVector(location.x + TILE_SIZE, location.y);
                PVector bottomLeft = new PVector(location.x + TILE_SIZE - 4, location.y);
                BoundingBox collBox = new BoundingBox(topLeft, topRight, bottomLeft);
                collisionShapes.add(collBox);
                break;}
            case 21:
            case 22:
            case 23:
                {//all of these need a 4x32 BoundingBox on the left of the tile area
                PVector topLeft = location.copy();
                PVector topRight = new PVector(location.x + 4, location.y);
                PVector bottomLeft = new PVector(location.x, location.y + TILE_SIZE);
                BoundingBox collBox = new BoundingBox(topLeft, topRight, bottomLeft);
                collisionShapes.add(collBox);
                break;}
            case 7: //maybe create a new BoundingShape type for this later?
            case 8:
                {//all of these need three shapes:
                // 2 BoundingBox shapes covering bottom and right of tile
                PVector boxOneTL = new PVector(location.x + TILE_SIZE - 3, location.y);
                PVector boxOneTR = new PVector(location.x + TILE_SIZE, location.y);
                PVector boxOneBL = new PVector(location.x + TILE_SIZE - 3, location.y + TILE_SIZE);
                BoundingBox boxOne = new BoundingBox(boxOneTL, boxOneTR, boxOneBL);
                boxOne.normal = new PVector(-0.7071068, -0.7071068, 0.0);
                collisionShapes.add(boxOne);

                PVector boxTwoTL = new PVector(location.x, location.y + TILE_SIZE - 3);
                PVector boxTwoTR = new PVector(location.x + TILE_SIZE - 3, location.y + TILE_SIZE - 3);
                PVector boxTwoBL = new PVector(location.x, location.y + TILE_SIZE);
                BoundingBox boxTwo = new BoundingBox(boxTwoTL, boxTwoTR, boxTwoBL);
                boxTwo.normal = new PVector(-0.7071068, -0.7071068, 0.0);
                collisionShapes.add(boxTwo);
    
                // 1 BoundingTriangle shape covering rest of tile
                PVector triangleT = new PVector(location.x + TILE_SIZE - 3, location.y);
                PVector triangleBL = new PVector(location.x, location.y + TILE_SIZE - 3);
                PVector triangleBR = new PVector(location.x + TILE_SIZE - 3, location.y + TILE_SIZE - 3);
                BoundingTriangle tri = new BoundingTriangle(triangleT, triangleBL, triangleBR);
                tri.normal = new PVector(-0.7071068, -0.7071068, 0.0);
                collisionShapes.add(tri);

                break;}
            case 9:
                {//all of these need three shapes:
                // 2 BoundingBox shapes covering bottom and left of tile
                PVector boxOneTL = location.copy();
                PVector boxOneTR = new PVector(location.x + 3, location.y);
                PVector boxOneBL = new PVector(location.x, location.y + TILE_SIZE);
                BoundingBox boxOne = new BoundingBox(boxOneTL, boxOneTR, boxOneBL);
                collisionShapes.add(boxOne);

                PVector boxTwoTL = new PVector(location.x + 3, location.y + TILE_SIZE - 3);
                PVector boxTwoTR = new PVector(location.x + TILE_SIZE, location.y + TILE_SIZE - 3);
                PVector boxTwoBL = new PVector(location.x + 3, location.y + TILE_SIZE);
                BoundingBox boxTwo = new BoundingBox(boxTwoTL, boxTwoTR, boxTwoBL);
                collisionShapes.add(boxTwo);
    
                // 1 BoundingTriangle shape covering rest of tile
                PVector triangleT = new PVector(location.x + 3, location.y);
                PVector triangleBL = new PVector(location.x + 3, location.y + TILE_SIZE - 3);
                PVector triangleBR = new PVector(location.x + TILE_SIZE, location.y + TILE_SIZE - 3);
                BoundingTriangle tri = new BoundingTriangle(triangleT, triangleBL, triangleBR);
                collisionShapes.add(tri);

                break;}
            
            case 10:
                {//all of these need three shapes:
                // 2 BoundingBox shapes covering top and right of tile
                PVector boxOneTL = location.copy();
                PVector boxOneTR = new PVector(location.x + TILE_SIZE, location.y);
                PVector boxOneBL = new PVector(location.x, location.y + 3);
                BoundingBox boxOne = new BoundingBox(boxOneTL, boxOneTR, boxOneBL);
                collisionShapes.add(boxOne);

                PVector boxTwoTL = new PVector(location.x + TILE_SIZE - 3, location.y + 3);
                PVector boxTwoTR = new PVector(location.x + TILE_SIZE, location.y + 3);
                PVector boxTwoBL = new PVector(location.x + TILE_SIZE - 3, location.y + TILE_SIZE);
                BoundingBox boxTwo = new BoundingBox(boxTwoTL, boxTwoTR, boxTwoBL);
                collisionShapes.add(boxTwo);
    
                // 1 BoundingTriangle shape covering rest of tile
                PVector triangleT = new PVector(location.x + TILE_SIZE - 3, location.y + TILE_SIZE);
                PVector triangleBL = new PVector(location.x, location.y + 3);
                PVector triangleBR = new PVector(location.x + TILE_SIZE, location.y + 3);
                BoundingTriangle tri = new BoundingTriangle(triangleT, triangleBL, triangleBR);
                collisionShapes.add(tri);

                break;}
            
            case 11:
                {//all of these need three shapes:
                // 2 BoundingBox shapes covering top and left of tile
                PVector boxOneTL = location.copy();
                PVector boxOneTR = new PVector(location.x + TILE_SIZE, location.y);
                PVector boxOneBL = new PVector(location.x, location.y + 3);
                BoundingBox boxOne = new BoundingBox(boxOneTL, boxOneTR, boxOneBL);
                collisionShapes.add(boxOne);

                PVector boxTwoTL = new PVector(location.x, location.y + 3);
                PVector boxTwoTR = new PVector(location.x + 3, location.y + 3);
                PVector boxTwoBL = new PVector(location.x, location.y + TILE_SIZE);
                BoundingBox boxTwo = new BoundingBox(boxTwoTL, boxTwoTR, boxTwoBL);
                collisionShapes.add(boxTwo);
    
                // 1 BoundingTriangle shape covering rest of tile
                PVector triangleT = new PVector(location.x + 3, location.y + TILE_SIZE);
                PVector triangleBL = new PVector(location.x + 3, location.y + 3);
                PVector triangleBR = new PVector(location.x + TILE_SIZE, location.y + 3);
                BoundingTriangle tri = new BoundingTriangle(triangleT, triangleBL, triangleBR);
                collisionShapes.add(tri);

                break;}
            case 24:
                {//1 BoundingTriangle shape
                PVector top = new PVector(location.x + TILE_SIZE, location.y + TILE_SIZE - 3);
                PVector bottomLeft = new PVector(location.x + TILE_SIZE - 3, location.y + TILE_SIZE);
                PVector bottomRight = new PVector(location.x + TILE_SIZE, location.y + TILE_SIZE);
                BoundingTriangle tri = new BoundingTriangle(top, bottomLeft, bottomRight);
                tri.normal = new PVector(-0.7071068, -0.7071068, 0.0);
                collisionShapes.add(tri);

                break;}
            case 25:
                {//1 BoundingTriangle shape
                PVector top = new PVector(location.x, location.y + TILE_SIZE - 3);
                PVector bottomLeft = new PVector(location.x, location.y + TILE_SIZE);
                PVector bottomRight = new PVector(location.x + 3, location.y + TILE_SIZE);
                BoundingTriangle triangle = new BoundingTriangle(top, bottomLeft, bottomRight);
                collisionShapes.add(triangle);

                break;}
            case 26:
            {//1 BoundingTriangle shape
            PVector top = new PVector(location.x + TILE_SIZE, location.y + 3);
            PVector bottomLeft = new PVector(location.x + TILE_SIZE - 3, location.y);
            PVector bottomRight = new PVector(location.x + TILE_SIZE, location.y);
            BoundingTriangle tri = new BoundingTriangle(top, bottomLeft, bottomRight);
            collisionShapes.add(tri);}

                break;
            case 27:
                {//1 BoundingTriangle shape
                PVector top = new PVector(location.x, location.y + 3);
                PVector bottomLeft = location.copy();
                PVector bottomRight = new PVector(location.x + 3, location.y);
                BoundingTriangle triangle = new BoundingTriangle(top, bottomLeft, bottomRight);
                collisionShapes.add(triangle);

                break;}
        }

        return collisionShapes;
    }

    //read object properties appropriately for each level component
    public void readObjects(JSONArray objects) {
        JSONObject obj;
        for(int i = 0; i < objects.size(); i++) {
            obj = objects.getJSONObject(i);
            switch(obj.getString("name")) {
                case Names.START_POS_LABEL:
                    {startPos = new PVector(obj.getFloat("x") + TILE_SIZE / 2, obj.getFloat("y") + TILE_SIZE / 2);
                    break;}
                case Names.END_BOX_LABEL:
                    {PVector topLeft = new PVector(obj.getFloat("x"), obj.getFloat("y"));
                    PVector topRight = new PVector(obj.getFloat("x") + obj.getFloat("width"), obj.getFloat("y"));
                    PVector bottomLeft = new PVector(obj.getFloat("x"), obj.getFloat("y") + obj.getFloat("height"));
                    endPos = new BoundingBox(topLeft, topRight, bottomLeft);
                    break;}
                case Names.SPIKES_LABEL:
                    {PVector topLeft = new PVector(obj.getFloat("x"), obj.getFloat("y"));
                    PVector topRight = new PVector(obj.getFloat("x") + obj.getFloat("width"), obj.getFloat("y"));
                    PVector bottomLeft = new PVector(obj.getFloat("x"), obj.getFloat("y") + obj.getFloat("height"));
                    spikes.add(new Spikes(topLeft, topRight, bottomLeft));
                    break;}
                case Names.ENEMY_LABEL:
                    {

                    PVector pathOffsets = new PVector(obj.getFloat("x"), obj.getFloat("y"));
                    //get enemy path
                    JSONArray pathJSON = obj.getJSONArray("polyline");
                    JSONObject pathPointJSON;
                    int pathSize = pathJSON.size();
                    PVector[] path = new PVector[pathSize];

                    //for first point, this will be the initial location of enemy
                    pathPointJSON = pathJSON.getJSONObject(0);
                    path[0] = new PVector(pathOffsets.x + pathPointJSON.getFloat("x"), pathOffsets.y + pathPointJSON.getFloat("y") - TILE_SIZE / 2);

                    PVector location = path[0].copy();
                    BoundingBox enemyColl = new BoundingBox(location, Enemy.SIZE, Enemy.SIZE, 0);
                    for(int j = 1; j < pathSize; j++) {
                        pathPointJSON = pathJSON.getJSONObject(j);
                        path[j] = new PVector(pathOffsets.x + pathPointJSON.getFloat("x"), pathOffsets.y + pathPointJSON.getFloat("y") - TILE_SIZE / 2);
                    }
                    enemies.add(new Enemy(location, path, enemyColl));
                    break;}

            }
        }
    }
}