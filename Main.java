public class GameController extends Actor {
   private ArrayList<BallController> bälle;
   private Schläger schläger;
   private BlockModel blockModel;
   private BlockView blockView;
   private ArrayList<FallendesItem> aktiveItems = new ArrayList<FallendesItem>();
   int level = Input.readInt("Geben sie den Levelgrad (1 - 5) an : "); // Eingabe 1–5
   int[][] blockData = LevelData.getBlockLevel(level);
   int[][] itemData = LevelData.getItemLevel(level);

   BlockModel model = new BlockModel(blockData, itemData);




   public GameController(/*int level, int[][] blockData, int[][] itemData*/) {
      this.level = level;
      blockModel = new BlockModel(blockData, itemData);
      blockView = new BlockView(blockModel, 80, 60);
      schläger = new Schläger(360, 580, 120, 20, "a", "d");

        // Multi-Ball-Verwaltung
      bälle = new ArrayList<BallController>();
      aktiveItems = new ArrayList<FallendesItem>();

      BallController ersterBall = new BallController(new BallModel(), schläger, blockModel, blockView, this);
      bälle.add(ersterBall);
   }

   public void act() {

        // Von hinten nach vorne iterieren, falls Bälle entfernt werden
      for (int i = bälle.size() - 1; i >= 0; i--) {
         BallController ball = bälle.get(i);
         if(ball.isDestroyed()) {
            bälle.remove(i);
         }
      }

      for (int i = aktiveItems.size() - 1; i >= 0; i--) {
         FallendesItem item = aktiveItems.get(i);
         item.act();

   // Prüfen, ob Item eingesammelt wurde
         if(item.collidesWith(schläger)) {
            aktiviereItemEffekt(item.getTyp());
            item.destroy(); // aus Szene entfernen
            aktiveItems.remove(i); // aus Liste entfernen
         }

   // Falls Item außerhalb Bildschirm
         else if(item.getCenterY() > 600) {
            item.destroy();
            aktiveItems.remove(i);
         }
      }


      if(bälle.isEmpty()) {
            // Zeige "Game Over"-Text
         Text title = new Text(400, 200, 100, "Game Over");
         title.setAlignment(Alignment.center);
         title.setFillColor(Color.orangered, 1);
         title.setBorderColor(Color.black);
         title.setStyle(true, false);
         title.setBorderWidth(7);
         System.exit(0);
      }
   }
   
   public void addItem(FallendesItem item) {
      aktiveItems.add(item);
   }

   public void aktiviereItemEffekt(int typ) {
      BallController.itemActive = true;
      Sound.playSound(Sound.short_bell);
      if(typ == 1) {
         bälle.get(0).addAdditionalBall(); // oder: neue Methode schreiben
      } else if(typ == 2) {
         schläger.makeLarger();
         schläger.setFillColor(Color.lime);
         Timer.executeLater(new MakeBatNormal(schläger), 10000);
      } else if(typ == 3) {
         schläger.makeSmaller();
         schläger.setFillColor(Color.orangered);
         Timer.executeLater(new MakeBatNormal(schläger), 10000);
      }
   }

   public void addBall(BallController neuerBall) {
      bälle.add(neuerBall);
   }

   /*public static void starteLevel(int level) {
      int[][] blockData = LevelData.getBlockLevel(level);
      int[][] itemData = LevelData.getItemLevel(level);
      GameController gc = new GameController(level, blockData, itemData);
   }

   public void restartLevel() {
      starteLevel(level);
   }

   public void startNextLevel() {
      if(level < 5) {
         int nextLevel = level + 1;
         int[][] blockData = LevelData.getBlockLevel(nextLevel);
         int[][] itemData = LevelData.getItemLevel(nextLevel);
         GameController neu = new GameController(nextLevel, blockData, itemData);
         System.exit(0); // oder alte Welt durch neue ersetzen
      }
   }*/
   
   /*public void showLevelEndScreen() {
    double y = 400;
    double fontsize = 20;

    GameButton retryButton = new GameButton(100, y, fontsize, "Retry", new Runnable() {
    @Override
    public void run() {
        restartLevel();
    }
});

GameButton nextButton = new GameButton(250, y, fontsize, "Next Level", new Runnable() {
    @Override
    public void run() {
        loadNextLevel();
    }
});

}
*/

}

new GameController();
