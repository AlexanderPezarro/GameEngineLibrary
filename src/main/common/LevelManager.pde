public class LevelManager {
    
    Player player;
    JSONReader jsonReader;
    Level currentLevel;
    Map<String, Level> levels;

    MusicManager musicManager;

    public LevelManager(MusicManager mm, Player player) {
        this.player = player;
        musicManager = mm;
        jsonReader = new JSONReader();
        levels = new HashMap();
    }

    public void draw() {
        currentLevel.draw();
    }

    public void update() {
        currentLevel.update();
    }

    public void reset() {
        currentLevel.reset();
    }

    public void addLevel(String levelName, Level level) {
        levels.put(levelName, level);
    }

    public void addLevel(String levelName, String levelPath, String tilesetPath) {
        Level newLevel = jsonReader.readTilemap(levelName,levelPath,tilesetPath, player);
        addLevel(levelName, newLevel);
    }

    // Change the current level to the given level and
    // calls its setup method.
    public void changeLevel(Level level) {
        currentLevel = level;

    }

    // Change the current scene to the given sceneName.
    // If the sceneName doesn't exist, do nothing;
    public void changeLevel(String levelName) {
        changeLevel(levels.getOrDefault(levelName, currentLevel));
    }
}