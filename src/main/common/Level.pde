public class Level {
    String name; //maybe give levels cute names :)
    Tile[][] tiles;
    Spikes[] spikes;
    ArrayList<Enemy> enemies;
    Player player;
    PVector startPosition;
    BoundingBox endRegion;
    boolean finished;

    public Level(String name, Tile[][] tiles, Player player, PVector startPosition, BoundingBox endRegion, ArrayList<Spikes> spikes, ArrayList<Enemy> enemies) {
        this.name = name;
        this.tiles = tiles;
        this.player = player;
        this.startPosition = startPosition;
        this.endRegion = endRegion;
        this.spikes = new Spikes[spikes.size()];
        spikes.toArray(this.spikes);
        this.enemies = enemies;
        finished = false;
    }

    public void update() {
        if (BoundingShape.isColliding(player.bb, endRegion, new CollisionResult()) != null) finished = true;
        // Get closest 9 tiles to player
        List<Tile> closestTiles = new ArrayList(9);
        PVector closestTileIndex = getClosestTileIndexToPlayer();

        closestTiles.add(tiles[floor(closestTileIndex.y)][floor(closestTileIndex.x)]);

        for (int y = floor(closestTileIndex.y) - 1; y < floor(closestTileIndex.y) + 2; y += 1) {
            if (y < 0 || y > tiles.length -1) continue;
            for (int x = floor(closestTileIndex.x) - 1; x < floor(closestTileIndex.x) + 2; x += 1) {
                if (x < 0 || x > tiles[y].length -1) continue;
                Tile tile = tiles[y][x];
                tile.outline = true;
                closestTiles.add(tile);
            }
        }
        // Check which (if any) of the tiles the player is colliding with.
        boolean collision = false;
        boolean collTri = false;
        PVector triNormal = null; 
        CollisionResult lastCol = new CollisionResult();
        for(Tile tile: closestTiles) {
            for (BoundingShape bs: tile.collision) {
                CollisionResult result = new CollisionResult();
                result = BoundingShape.isColliding(player.bb, bs, result);
                if (result != null) {
                    
                    PVector pVel = player.velocity.copy();
                    int loopOverride = 10;
                    while((result == null || abs(result.delta.y) > 5 || abs(result.delta.x) > 5) && loopOverride-- > 0) {
                        pVel.div(2);
                        PVector offset = pVel.copy();
                        if (result != null) offset.mult(-1);
                        else result = new CollisionResult();
                        player.moveBy(offset);
                        result = BoundingShape.isColliding(player.bb, bs, result);
                    }
                    if (result == null) continue; // Smth went wrong so just tap out
                    lastCol = result;
                    tile.colliding = true;
                    collision = true;
                    player.collisionFrames = 3;
                    // If: player state is jumping, hanging or falling then...
                    if (!player.state.isLanded()) {
                        // Check player angle and move dir against tile rotation to
                        // determine crash or boost. Change player state.
                        // float playerRot = player.rotation < 0 ? player.rotation + PI : player.rotation;
                        // if (playerRot >= radians(315) || playerRot <= radians(45))
                        //     player.velocity.x = player.isRight ? player.BOOST : -player.BOOST;
                        // else player.velocity.x = player.isRight ? player.CRASH : -player.CRASH;
                        // check the actual speed direction
                        CollisionResult newResult = new CollisionResult();
                        newResult = BoundingShape.isColliding(player.boostBox, bs, newResult);
                        if(newResult != null) { //Boost
                            player.musicManager.playSFX(Names.SFX_NAMES[0]);
                            //player.velocity = player.isRight ? PVector.fromAngle(player.currentRotation).mult(player.BOOST) : PVector.fromAngle(player.currentRotation + PI).mult(player.BOOST) // would allow player to boost in current direction, but messes with collision
                            player.velocity.x = player.isRight ? player.BOOST : -player.BOOST;
                            if (player.MAX_VELOCITY < player.LIMIT_VELOCITY) {
                                player.MAX_VELOCITY += player.MAX_VELOCITY_INCREASEMENT;
                            }
                        } else { //Crash
                            player.musicManager.playSFX(Names.SFX_NAMES[3]);
                            player.MAX_VELOCITY = 4;
                        }

                        // Falling by default
                        player.state = CharState.falling;
                    }
                    // Else: determine force on player based on tile type
                    // PVector divided = result.delta.copy().div(2);
                    // player.position.add(divided.mult(collisionSmoothener(result.delta)));
                    player.moveBy(result.delta);
                    
                    //println("RESULT: Delta - " + result.delta + "      Normal - " + result.normal);
                    // player.velocity.add(result.delta);
                    float resultNormX = abs(result.normal.x);
                    float resultNormY = abs(result.normal.y);
                    
                    if (bs instanceof BoundingTriangle) {
                        collTri = true;
                        triNormal = result.normal.copy(); 
                      player.velocity.y = 0;
                    //   player.adjustRotation(result.normal);
                      player.state = CharState.landed;
                    } else if (resultNormX > resultNormY) {
                        player.velocity.x = 0;
                    } else { //if (resultNormX < resultNormY)
                        player.velocity.y = 0;
                        // Landing in special case
                        if (result.normal.y < 0) {
                        //   if (!player.state.isLanded()) player.adjustRotation(new PVector(0, -1));
                          player.state = CharState.landed;
                        }
                    }
                }
            }
        }
        for(Spikes spike: spikes) {
            if(BoundingShape.isColliding(player.bb, spike.collision, new CollisionResult()) != null) {
                player.musicManager.playSFX(Names.SFX_NAMES[3]);
                player.MAX_VELOCITY = 4;

            }
                
        }
        for(Enemy enemy: enemies) {
            if(BoundingShape.isColliding(player.bb, enemy.collision, new CollisionResult()) != null) {
                player.musicManager.playSFX(Names.SFX_NAMES[3]);
                player.MAX_VELOCITY = 4;
            }
        }

        if (collision) {
            // print("Col");
            if (collTri) {println("HEEHOO"); player.adjustRotation(triNormal);}
            else player.adjustRotation(new PVector(0, -1));
        }

        // If: player was landed change state to cayote
        if (!collision && player.collisionFrames <= 0 && player.state.isLanded()) {
            player.state = CharState.coyote;
        }
        //sort out enemy collision

        // Else:

    }

    public void draw() {
        for(int y = 0; y < tiles.length; y++) {
            for(int x = 0; x < tiles[y].length; x++) {
                if(tiles[y][x].location.dist(player.position) <= width / 4 * 3)
                tiles[y][x].draw();
            }
        }
        for(int i = 0; i < enemies.size(); i++) {
            if(!enemies.get(i).isAlive) enemies.remove(i); else enemies.get(i).draw();
        }
        if(player.position.dist(endRegion.topLeft) <= width / 2) {
            pushStyle();
            fill(0,200,0,100);
            rect(endRegion.topLeft.x, endRegion.topLeft.y, endRegion.size.x, endRegion.size.y);
            popStyle();
        }
    }

    public void reset() {
        finished = false;
    }

    private PVector getClosestTileIndexToPlayer() {
        PVector closestTileIndex = player.position.copy().div(JSONReader.TILE_SIZE);
        int closestY = floor(closestTileIndex.y);
        if (closestY < 0) closestY = 0;
        if (closestY > tiles.length - 1) closestY = tiles.length - 1;

        int closestX = floor(closestTileIndex.x);
        if (closestX < 0) closestX = 0;
        if (closestX > tiles[closestY].length - 1) closestX = tiles[closestY].length - 1;
        return new PVector(closestX, closestY);
    }

    //helper method to adjust collision
    private int collisionSmoothener(PVector delta) {
        float angle = delta.heading();
        if(angle == 0 || angle == PI / 2 || angle == PI || angle == 3 * PI / 2) {
            return 2;
        } else return 1;
    }
}
